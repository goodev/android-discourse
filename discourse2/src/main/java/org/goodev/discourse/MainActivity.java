package org.goodev.discourse;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.uservoice.uservoicesdk.Config;
import com.uservoice.uservoicesdk.UserVoice;

import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.GetUserInfoTask;
import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.api.data.UserDetails;
import org.goodev.discourse.contentprovider.Provider;
import org.goodev.discourse.database.Database;
import org.goodev.discourse.database.tables.CategoriesTable;
import org.goodev.discourse.database.tables.SiteTable;
import org.goodev.discourse.database.tables.UserInfoTable;
import org.goodev.discourse.model.Categories;
import org.goodev.discourse.model.Site;
import org.goodev.discourse.model.UserInfo;
import org.goodev.discourse.ui.CategoriesFragment;
import org.goodev.discourse.ui.CategoryTopicsFragment;
import org.goodev.discourse.ui.FavoritedTopicsFragment;
import org.goodev.discourse.ui.LoginFragment;
import org.goodev.discourse.ui.LoginFragment.LoginCallback;
import org.goodev.discourse.ui.NewTopicsFragment;
import org.goodev.discourse.ui.ProgressFragment;
import org.goodev.discourse.ui.ReadTopicsFragment;
import org.goodev.discourse.ui.TopicsListFragment;
import org.goodev.discourse.ui.UnreadTopicsFragment;
import org.goodev.discourse.ui.dialog.DialogStandardFragment;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.PrefsUtils;
import org.goodev.discourse.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor>, LoginCallback, OnClickListener {
    public static final String RESET_INDEX = "delete from sqlite_sequence where name='%1s';";
    // private static final String TAG_LATEST = "latest_frag";
    // private static final String TAG_CATEGORY = "cat_frag";
    private static final String FRAGMENT_TAG = "tag_frag";
    // ----------------loader
    private static final int LOADER_ID_SITE = 100;
    private final ContentObserver mObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            getSupportLoaderManager().restartLoader(LOADER_ID_SITE, null, MainActivity.this);
        }

    };
    private static final int LOADER_ID_USER = 1001;
    private static final int LOADER_ID_CATEGORY = 1002;
    private static final String TAG_PRO = "progress";
    private static final String TAG_LOGIN = "login";
    private final OnCheckedChangeListener mSiteChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            siteChanged(group, checkedId);
        }
    };
    private final Handler mHandler = new Handler();
    // ----- Left Drawer
    private ImageView mUserIcon;
    private TextView mUserDisplayName;
    private TextView mUserName;
    private DrawerLayout mDrawerLayout;
    private View mDrawer;
    private ListView mDrawerList;
    private ViewGroup mMainContent;
    private HeaderAdapter mDrawerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private RadioGroup mSiteRadioGroup;
    private int mDrawerPosition = ListView.INVALID_POSITION;
    // TODO fix this , not load data from database
    // 当前选择的 分类 ID或者 导航条目 ID
    private long mCategoryId = -2;
    private int mNavId = -1;
    private Site mCurrentSite;
    private String mCurrentSiteUrl;
    private Fragment mCurrentFrag;
    private LoginFragment mLoginFragment;
    private ProgressFragment mProgressFragment;
    private UserDetails mUser;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        if (state != null) {
            mCurrentSiteUrl = state.getString(Utils.EXTRA_URL, null);
            mUser = (UserDetails) state.getSerializable(Utils.EXTRA_OBJ);
            mDrawerPosition = state.getInt(Utils.EXTRA_NUMBER, ListView.INVALID_POSITION);
            mCurrentFrag = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        }

        initViews();
        initLoaders();
        setupUserVoiceSdk();
        checkShowChangeLog();
    }

    private void setupUserVoiceSdk() {
        Config config = new Config("goodev.uservoice.com");
        UserVoice.init(config, this);
        config.setShowForum(false);
        config.setShowContactUs(true);
        config.setShowPostIdea(false);
        config.setShowKnowledgeBase(false);
        // hack to always show the overflow menu in the action bar
        try {
            ViewConfiguration viewConfig = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(viewConfig, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }

    private void initViews() {
        mMainContent = (ViewGroup) findViewById(R.id.content_frame);
        if (mCurrentFrag != null) {
            mMainContent.removeAllViews();
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // set a custom shadow that overlays the main content when the drawer open
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setFocusableInTouchMode(false);
        mDrawer = findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        View listHeaderView = getLayoutInflater().inflate(R.layout.drawer_left_header, mDrawerList, false);

        listHeaderView.findViewById(R.id.user_layout).setOnClickListener(this);
        mUserDisplayName = (TextView) listHeaderView.findViewById(R.id.user_name);
        mUserName = (TextView) listHeaderView.findViewById(R.id.user_login_name);
        mUserIcon = (ImageView) listHeaderView.findViewById(R.id.user_icon);
        mSiteRadioGroup = (RadioGroup) listHeaderView.findViewById(R.id.site_radio_group);
        mSiteRadioGroup.setOnCheckedChangeListener(mSiteChangeListener);
        mDrawerList.addHeaderView(listHeaderView);
        mDrawerAdapter = new HeaderAdapter(this, new DrawerCategoryAdapter(this));
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerList.setItemChecked(position, true);
                onDrawerListItemClick(parent.getItemAtPosition(position));
            }
        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                // getActionBar().setTitle(mTitle);
                // creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // getActionBar().setTitle(mDrawerTitle);
                // creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    protected void onDrawerListItemClick(Object data) {
        if (data instanceof Cursor) {
            mCurrentFrag = new CategoryTopicsFragment();
            Categories c = new Categories((Cursor) data);
            if (mCategoryId != c.getUid()) {
                mCategoryId = c.getUid();
                mNavId = -1;
                Bundle args = new Bundle();
                args.putString(Utils.EXTRA_URL, mCurrentSiteUrl);
                L.d("Open Category: %s", c.getName());
                args.putString(Utils.EXTRA_TITLE, c.getName());
                args.putString(Utils.EXTRA_SLUG, c.getSlug());
                args.putLong(Utils.EXTRA_ID, c.getUid());
                mCurrentFrag.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mCurrentFrag, FRAGMENT_TAG).commit();
            }

        } else if (data instanceof Integer) {
            int id = ((Integer) data).intValue();
            if (mNavId != id) {
                mNavId = id;
                mCategoryId = -2;
                if (id == HeaderAdapter.LATEST) {
                    setLatestTopicsFragment();
                } else if (id == HeaderAdapter.CATEGORY) {
                    setCategoriesFragment();
                } else if (id == HeaderAdapter.NEW) {
                    setNewFragment();
                } else if (id == HeaderAdapter.UNREAD) {
                    setUnreadFragment();
                } else if (id == HeaderAdapter.READ) {
                    setReadFragment();
                } else if (id == HeaderAdapter.FAVORITE) {
                    setFavFragment();
                }
                // TODO add more
            }
        }
        closeDrawer();
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(mDrawer);
    }

    private void openCategoryActivity(Categories c) {
        Intent intent = new Intent();
        intent.setClass(this, CategoryActivity.class);
        intent.putExtra(Utils.EXTRA_URL, mCurrentSiteUrl);
        intent.putExtra(Utils.EXTRA_TITLE, c.getName());
        intent.putExtra(Utils.EXTRA_SLUG, c.getSlug());
        intent.putExtra(Utils.EXTRA_ID, c.getUid());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during onPostCreate() and onConfigurationChanged()...
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    /**
     * Backward-compatible version of {@link ActionBar#getThemedContext()} that simply returns the {@link android.app.Activity} if
     * <code>getThemedContext</code> is unavailable.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private Context getActionBarThemedContextCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getActionBar().getThemedContext();
        } else {
            return this;
        }
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawer);
        // menu.findItem(R.id.action_about).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettingsActivity();
                return true;
            case R.id.action_search:
                openSearchActivity();
                return true;
            case R.id.action_faq:
                openFaqActivity();
                return true;
            case R.id.action_feedback:
                openFeedbackActivity();
                return true;
            case R.id.action_create_topic:
                openCreateTopicActivity();
                return true;
            case R.id.action_changelog:
                openDialogFragment(new DialogStandardFragment());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openCreateTopicActivity() {
        if (!App.isLogin()) {
            Toast.makeText(this, R.string.create_topic_not_login, Toast.LENGTH_LONG).show();
            return;
        }
        ActivityUtils.openNewEditorActivity(this, null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Utils.EXTRA_NUMBER, mDrawerList.getCheckedItemPosition());
        outState.putSerializable(Utils.EXTRA_OBJ, mUser);
        if (!TextUtils.isEmpty(mCurrentSiteUrl)) {
            outState.putString(Utils.EXTRA_URL, mCurrentSiteUrl);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(Provider.SITE_CONTENT_URI, true, mObserver);

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mUser == null && mCurrentSite != null) {
            L.i("loading user......... " + mCurrentSiteUrl);
            loadUserInfo(mCurrentSite, true);
        }
    }

    private void initLoaders() {
        getSupportLoaderManager().initLoader(LOADER_ID_SITE, null, this);
        getSupportLoaderManager().initLoader(LOADER_ID_CATEGORY, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String userSort = UserInfoTable.SITEID + " ASC";
        String siteSort = SiteTable.ID + " ASC";
        switch (id) {
            case LOADER_ID_SITE:
                return new CursorLoader(this, Provider.SITE_CONTENT_URI, SiteTable.ALL_COLUMNS, null, null, siteSort);
            case LOADER_ID_USER:
                return new CursorLoader(this, Provider.USERINFO_CONTENT_URI, UserInfoTable.ALL_COLUMNS, null, null, userSort);
            case LOADER_ID_CATEGORY:
                return new CursorLoader(this, Provider.CATEGORIES_CONTENT_URI, CategoriesTable.ALL_COLUMNS, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID_SITE:
                setupSiteGroup(data);
                break;
            case LOADER_ID_USER:

                break;
            case LOADER_ID_CATEGORY:
                // TODO discourse api修改， http://meta.discourse.org/latest.json 返回值不再包含Caegory信息 所以需要做全局缓存
                setupAppCategoryCache(data);
                setupDrawerCategory(data);
                break;

        }
    }

    private void setupAppCategoryCache(Cursor data) {
        HashMap<Long, Category> categories = new HashMap<Long, Category>();
        if (data != null && data.moveToFirst()) {
            while (!data.isAfterLast()) {
                Category c = new Category(data);
                categories.put(c.id, c);
                data.moveToNext();
            }
            App.setCategories(categories);
        }
    }

    private void setupDrawerCategory(Cursor data) {
        mDrawerAdapter.swapCursor(data);
        if (mDrawerPosition != ListView.INVALID_POSITION) {
            mDrawerAdapter.setLogin(App.isLogin());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawerList.setItemChecked(mDrawerPosition, true);
                    mDrawerPosition = ListView.INVALID_POSITION;
                }
            }, 500);
        } else {
            // 第一个为 Navigation header，第二个为 最新topic 分类
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNavId = 0;
                    mDrawerList.setItemChecked(2, true);
                }
            }, 500);
        }
    }

    private RadioButton getSiteButton() {
        return (RadioButton) getLayoutInflater().inflate(R.layout.drawer_site_radio_button, mSiteRadioGroup, false);
    }

    private void setupSiteGroup(Cursor data) {
        final RadioGroup group = mSiteRadioGroup;
        group.removeAllViews();
        String siteUrl = mCurrentSiteUrl;
        // if (siteUrl == null) {
        // siteUrl = PrefsUtils.getCurrentSiteUrl();
        // }
        if (data.getCount() == 0) {
            setNoSiteContentView();
            RadioButton button = getSiteButton();
            group.addView(button);
            mDrawerAdapter.setHasSite(false);
        } else {
            mDrawerAdapter.setHasSite(true);
            final String lastSite = PrefsUtils.getCurrentSiteUrl();
            data.moveToFirst();
            while (!data.isAfterLast()) {
                RadioButton button = getSiteButton();
                Site site = new Site(data);
                button.setTag(site);
                String url = site.getUrl();
                String displayUrl = url;
                if (url.endsWith(Utils.SLASH)) {
                    displayUrl = url.substring(0, url.length() - 1);
                }
                String title = getString(R.string.radio_btn_text, site.getTitle(), displayUrl);
                button.setText(Html.fromHtml(title));
                group.addView(button);
                if (url.equals(siteUrl)) {
                    button.setChecked(true);
                } else if (url.equals(lastSite)) {
                    button.setChecked(true);
                }
                data.moveToNext();
            }
            if (lastSite == null && siteUrl == null) {
                ((RadioButton) group.getChildAt(0)).setChecked(true);
            }
        }
    }

    private void setNoSiteContentView() {
        View button = getLayoutInflater().inflate(R.layout.no_site_tips, mMainContent, false);
        mMainContent.addView(button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });

    }

    // TODO first try login, then load other data.
    protected void siteChanged(RadioGroup group, int checkedId) {
        if (checkedId == -1) {
            return;
        }
        RadioButton button = (RadioButton) group.findViewById(checkedId);
        Site site = (Site) button.getTag();
        if (site != null) {
            App.setSiteUrl(site.getUrl());
        }
        if (site == null) {
            group.clearCheck();
            openSettingsActivity();
        } else if (!site.getUrl().equals(mCurrentSiteUrl)) { // TODO 第一次启动 加载上次查看的url。
            mDrawerPosition = ListView.INVALID_POSITION;
            mCurrentSite = site;
            mCurrentSiteUrl = site.getUrl();
            PrefsUtils.setCurrentSiteUrl(mCurrentSiteUrl);
            App.setLogin(false);
            clearDatabase();
            // 登陆完成后，再加载其他信息
            loadUserInfo(site, false);
        } else {
            setupUserInfo(mUser);
        }
        getActionBar().setSubtitle(mCurrentSiteUrl);
    }

    private void clearDatabase() {
        SQLiteDatabase db = new Database(this).getWritableDatabase();
        getContentResolver().delete(Provider.CATEGORIES_CONTENT_URI, null, null);
        db.execSQL(String.format(RESET_INDEX, CategoriesTable.TABLE_NAME));

        // getContentResolver().delete(Provider.CATEGORY_GROUP_PERMISSIONS_CONTENT_URI, null, null);
        // db.execSQL(String.format(RESET_INDEX, Category_group_permissionsTable.TABLE_NAME));
        //
        // getContentResolver().delete(Provider.CATEGORY_PROPERTIES_CONTENT_URI, null, null);
        // db.execSQL(String.format(RESET_INDEX, Category_propertiesTable.TABLE_NAME));
        //
        // getContentResolver().delete(Provider.FEATURED_USERS_CONTENT_URI, null, null);
        // db.execSQL(String.format(RESET_INDEX, Featured_usersTable.TABLE_NAME));
        //
        // getContentResolver().delete(Provider.SUGGESTED_TOPICS_CONTENT_URI, null, null);
        // db.execSQL(String.format(RESET_INDEX, Suggested_topicsTable.TABLE_NAME));
        //
        // getContentResolver().delete(Provider.TOPICS_CONTENT_URI, null, null);
        // db.execSQL(String.format(RESET_INDEX, TopicsTable.TABLE_NAME));
        //
        // getContentResolver().delete(Provider.TOPICSDETAILS_CONTENT_URI, null, null);
        // db.execSQL(String.format(RESET_INDEX, TopicsDetailsTable.TABLE_NAME));
        //
        // getContentResolver().delete(Provider.TOPICSPARTICIPANTS_CONTENT_URI, null, null);
        // db.execSQL(String.format(RESET_INDEX, TopicsParticipantsTable.TABLE_NAME));
        //
        // getContentResolver().delete(Provider.TOPIC_POSTERS_CONTENT_URI, null, null);
        // db.execSQL(String.format(RESET_INDEX, Topic_postersTable.TABLE_NAME));
        //
        // getContentResolver().delete(Provider.TOPICS_PROPERTIES_CONTENT_URI, null, null);
        // db.execSQL(String.format(RESET_INDEX, Topics_propertiesTable.TABLE_NAME));
        //
        // getContentResolver().delete(Provider.TOPICS_USERS_CONTENT_URI, null, null);
        // db.execSQL(String.format(RESET_INDEX, Topics_usersTable.TABLE_NAME));

    }

    private void updateMainFragment() {
        mMainContent.removeAllViews();
        setLatestTopicsFragment();
        // 第一个为 Navigation header，第二个为 最新topic 分类
        mDrawerList.setItemChecked(1, true);

    }

    private void setLatestTopicsFragment() {
        mCurrentFrag = new TopicsListFragment();
        updateFragment();
    }

    private void setCategoriesFragment() {
        mCurrentFrag = new CategoriesFragment();
        updateFragment();
    }

    private void setNewFragment() {
        mCurrentFrag = new NewTopicsFragment();
        updateFragment();
    }

    private void setUnreadFragment() {
        mCurrentFrag = new UnreadTopicsFragment();
        updateFragment();
    }

    private void setReadFragment() {
        mCurrentFrag = new ReadTopicsFragment();
        updateFragment();
    }

    private void setFavFragment() {
        mCurrentFrag = new FavoritedTopicsFragment();
        updateFragment();
    }

    private void updateFragment() {
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_URL, mCurrentSiteUrl);
        mCurrentFrag.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mCurrentFrag, FRAGMENT_TAG).commit();
    }

    private void loadSiteCategory() {
        Intent intent = Utils.getService(this, mCurrentSiteUrl, Api.TYPE_CATEGORIES);
        ResultReceiver rr = new ResultReceiver(null) {

            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == Activity.RESULT_OK) {
                    getSupportLoaderManager().restartLoader(LOADER_ID_CATEGORY, null, MainActivity.this);
                }
            }

        };
        intent.putExtra(Utils.EXTRA_CALLBACK, rr);
        startService(intent);
    }

    private void loadUserInfo(Site site, boolean nullCancel) {
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(Provider.USERINFO_CONTENT_URI, UserInfoTable.ALL_COLUMNS, UserInfoTable.SITEURL + " = \"" + site.getUrl() + "\"", null, null);
        UserInfo user = null;
        if (c.moveToFirst()) {
            user = new UserInfo(c);
        }
        final String name = user == null ? null : user.getName();
        final String password = user == null ? null : user.getPassword();
        if (nullCancel && (name == null || password == null)) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                login(mCurrentSiteUrl, name, password);
            }
        });
        if (name != null) {
            new GetUserInfoTask(mCurrentSiteUrl, name).execute();
        }
    }

    // TODO 如果用户没有设置登录信息，则依然执行该函数，返回值为null，表示登录失败
    private void login(String site, String name, String password) {
        FragmentManager manager = getSupportFragmentManager();
        if (mLoginFragment != null) {
            manager.beginTransaction().remove(mLoginFragment).commit();
        }
        mLoginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_URL, mCurrentSiteUrl);
        args.putString(Utils.EXTRA_NAME, name);
        args.putString(Utils.EXTRA_PASSWORD, password);
        mLoginFragment.setArguments(args);
        manager.beginTransaction().add(mLoginFragment, TAG_LOGIN).commit();

    }

    @Override
    public void onPreExecute() {
        mProgressFragment = new ProgressFragment();
        mProgressFragment.show(getSupportFragmentManager(), TAG_PRO);
    }

    @Override
    public void onResult(UserDetails data) {
        mUser = data;
        // 横竖屏切换的时候 该fragment为null
        if (mProgressFragment == null) {
            mProgressFragment = (ProgressFragment) getSupportFragmentManager().findFragmentByTag(TAG_PRO);
        }
        if (mLoginFragment == null) {
            mLoginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(TAG_LOGIN);
        }
        L.i("remove dialog  PF:" + mProgressFragment + "   LF:" + mLoginFragment);
        getSupportFragmentManager().beginTransaction().remove(mProgressFragment).remove(mLoginFragment).commit();
        mProgressFragment = null;
        mLoginFragment = null;
        setupUserInfo(data);
        boolean login = data != null;
        App.setLogin(login);
        mDrawerAdapter.setLogin(login);
        loadSiteCategory();
        updateMainFragment();
    }

    private void setupUserInfo(UserDetails data) {
        if (data == null) {
            mUserDisplayName.setText(R.string.not_login);
            mUserName.setText(R.string.click_to_login);
            mUserIcon.setImageResource(R.drawable.ic_person);
            return;
        }
        App.setUserInfo(data.username, null);
        mUserDisplayName.setText(data.name);
        mUserName.setText(data.username);
        ImageLoader imageLoader = App.getImageLoader(this, getResources());
        int size = Api.AVATAR_SIZE_BIG;
        String iconUrl = Utils.getAvatarUrl(data.avatar_template, size);
        imageLoader.setMaxImageSize(size);
        imageLoader.get(iconUrl, mUserIcon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_layout:
                if (mUser == null) {
                    showAddUserDialog();
                } else {
                    openUserActivity();
                }
                break;

            default:
                break;
        }
    }

    private void showAddUserDialog() {
        openSettingsActivity();

    }

    private void openFaqActivity() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void openFeedbackActivity() {
        UserVoice.launchContactUs(this);
    }

    private void openUserActivity() {
        L.i(mUser.username);
        ActivityUtils.openUserActivity(this, mUser.username);
    }

    @Override
    public void onBackPressed() {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawer);
        L.i("back　" + drawerOpen);
        if (!drawerOpen) {
            mDrawerLayout.openDrawer(mDrawer);
            Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }

    }

    private void checkShowChangeLog() {
        boolean first = Utils.isFirstRun();
        if (first) {
            openDialogFragment(new DialogStandardFragment());
        }
    }

    private void openDialogFragment(DialogStandardFragment dialogStandardFragment) {
        if (dialogStandardFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("log_dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            dialogStandardFragment.show(ft, "log_dialog");
        }
    }

}
