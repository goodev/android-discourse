package org.goodev.discourse.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;

import org.apache.http.HttpStatus;
import org.goodev.discourse.ActivityUtils;
import org.goodev.discourse.App;
import org.goodev.discourse.EditorActivity;
import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.PostActionTask;
import org.goodev.discourse.api.TopicNotificationTask;
import org.goodev.discourse.api.TopicStream;
import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.api.data.Links;
import org.goodev.discourse.api.data.Post;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.api.data.TopicDetails;
import org.goodev.discourse.contentprovider.Provider;
import org.goodev.discourse.database.tables.CategoriesTable;
import org.goodev.discourse.ui.EditorChangeTitleFragment.ChangeTopicListener;
import org.goodev.discourse.ui.dialog.InviteReplyDialogFragment;
import org.goodev.discourse.ui.dialog.InviteReplyDialogFragment.OnInviteListener;
import org.goodev.discourse.ui.dialog.LinksDialogFragment;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.Random;

public class TopicFragment extends ListFragment implements LoaderCallbacks<TopicStream>, OnScrollListener, ChangeTopicListener, AdListener, OnClickListener, OnInviteListener {

    public static final String EXTRA_ADD_VERTICAL_MARGINS = "extra_ADD_VERTICAL_MARGINS";
    private static final String STATE_POSITION = "extra_postion";
    private static final String STATE_TOP = "extra_top";
    private static final int TOPICS_LOADER_ID = 1;
    private static final int EDIT_POST_CODE = 111;
    protected final PostAdapter mAdapter = new PostAdapter();
    private final OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.user_icon:
                case R.id.post_user_info_layout:
                    onUserClicked(v);
                    break;
                case R.id.overflow_menu:
                    showPopupActioinMenu(v);
                    break;
                case R.id.post_like:
                    onLikeClicked(v);
                    break;
                case R.id.post_replay:
                    onReplyClicked(v);
                    break;
                case R.id.post_edit:
                    onEditClicked(v);
                    break;
                case R.id.post_recover:
                    onRecoverClicked(v);
                    break;
                case R.id.post_share:
                    onShareClicked(v);
                    break;
                case R.id.category:
                    openCategoryActivity(v);
                    break;

            }
        }
    };
    protected String mSiteUrl;
    protected String mSlug;
    protected long mId;
    ProgressFragment mProgressFragment;
    private TopicStream mData = new TopicStream();
    private int mListViewStatePosition;
    private int mListViewStateTop;
    private ImageLoader mImageLoader;
    private Category mCat;
    private TextView mEmptyView;
    private Spinner mNotificationSpinner;
    private TextView mCategoryView;
    private TextView mStatusView;
    private TextView mNotificationDesView;
    private String[] mNotificationDes;
    private int mNotifiCurrentLevel;
    private TopicNotificationTask mNotiTask;
    private final OnItemSelectedListener mNotifListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            L.d("spinner notifications %d : %s", position, parent.getItemAtPosition(position));
            if (mNotifiCurrentLevel == position) {
                return;
            }
            // setNotificationDesView(position);
            if (mNotiTask != null && mNotiTask.isCancelled()) {
                mNotiTask.cancel(true);
            }
            mNotiTask = new TopicNotificationTask(TopicFragment.this, position);
            mNotiTask.execute();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            L.i("Notings selected");
        }
    };
    /**
     * 当打开每个post的 菜单的时候， 记录该post数据，方便处理菜单功能
     */
    private Post mMenuPost;
    private final OnMenuItemClickListener mMenuListener = new OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mMenuPost == null) {
                return false;
            }
            L.i("menu item clicked: %s . post: %s", item.getTitle(), mMenuPost.cooked);
            switch (item.getItemId()) {
                case R.id.menu_post_links:
                    openPostLinksDialog();
                    return true;
                case R.id.menu_replay_new:
                    openReplayNewActivity();
                    return true;
                case R.id.menu_post_bookmark:
                    bookmarkClicked(item.isChecked());
                    return true;
                case R.id.menu_post_delete:
                    deletePostClicked();
                    return true;

                default:
                    break;
            }
            return false;
        }
    };
    /**
     * 第一个帖子（Topic）的连接为 details对象中的连接
     */
    private TopicDetails mMenuTopicDetails;
    private LikeActionTask mLikeTask;
    private UnlikeActionTask mUnlikeTask;
    private int mPostPosition;
    // ----------------------- admob
    private AdView mAdView;
    private View mCloseAd;
    private AdRequest mAdRequest;

    public TopicFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mImageLoader = App.getImageLoader(getActivity(), getResources());
        mSiteUrl = App.getSiteUrl();
        if (savedInstanceState != null) {
            mSlug = savedInstanceState.getString(Utils.EXTRA_SLUG);
            mId = savedInstanceState.getLong(Utils.EXTRA_ID);
            mCat = (Category) savedInstanceState.getSerializable(Utils.EXTRA_OBJ_C);
        } else {
            Bundle arg = getArguments();
            if (arg != null) {
                mSlug = arg.getString(Utils.EXTRA_SLUG);
                mId = arg.getLong(Utils.EXTRA_ID);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View view = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        mEmptyView = (TextView) view.findViewById(android.R.id.empty);
        if (savedInstanceState != null) {
            mListViewStatePosition = savedInstanceState.getInt(STATE_POSITION, -1);
            mListViewStateTop = savedInstanceState.getInt(STATE_TOP, 0);
        } else {
            mListViewStatePosition = -1;
            mListViewStateTop = 0;
        }
        return view;
    }

    private void setupNotificationSpinner() {
        String[] objects = getResources().getStringArray(R.array.notifications_title);
        ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1, objects);
        mNotificationSpinner.setAdapter(a);
        a.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // mNotificationSpinner.setOnItemSelectedListener(mNotifListener);
    }

    protected void setNotificationDesView(int level) {
        if (mNotificationDes == null) {
            mNotificationDes = getResources().getStringArray(R.array.notifications_des);
        }
        if (level >= mNotificationDes.length) {
            level = 0;
        }
        mNotificationDesView.setText(mNotificationDes[level]);
    }

    public String getSite() {
        return mSiteUrl;
    }

    public long getTopicId() {
        return mId;
    }

    public Spinner getNotiSpinner() {
        return mNotificationSpinner;
    }

    public TextView getNotiDes() {
        return mNotificationDesView;
    }

    public int getOldNotiLevel() {
        return mNotifiCurrentLevel;
    }

    public void setCurrentNotifLevel(int l) {
        mNotifiCurrentLevel = l;
    }

    public OnItemSelectedListener getListener() {
        return mNotifListener;
    }

    private void setNotificationStatus(int level) {
        mNotifiCurrentLevel = level;
        mNotificationSpinner.setOnItemSelectedListener(null);
        mNotificationSpinner.setSelection(level);
        setNotificationDesView(level);
        mNotificationSpinner.setOnItemSelectedListener(mNotifListener);
    }

    private void setTopicStatus(Topic t) {
        int left = 0;
        int right = 0;
        if (t.pinned) {
            right = R.drawable.ic_pin;
        }
        if (t.closed) {
            left = R.drawable.ic_locked;
        }
        mStatusView.setCompoundDrawablesWithIntrinsicBounds(left, 0, right, 0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setEmptyView(mEmptyView);
        mEmptyView.setText(getString(R.string.empty_topics));

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter
        // in the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        getLoaderManager().initLoader(TOPICS_LOADER_ID, null, this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView listView = getListView();
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.topic_header, listView, false);
        mNotificationDesView = (TextView) header.findViewById(R.id.notification_des);
        mCategoryView = (TextView) header.findViewById(R.id.category);
        mNotificationSpinner = (Spinner) header.findViewById(R.id.notification_spinner);
        mStatusView = (TextView) header.findViewById(R.id.topic_status_view);
        mAdView = (AdView) view.findViewById(R.id.adView);
        mCloseAd = view.findViewById(R.id.close_ad);
        mCloseAd.setOnClickListener(this);
        mAdView.setAdListener(this);
        setupNotificationSpinner();
        listView.addHeaderView(header);
        if (!Utils.isTablet(getActivity())) {
            // TODO ...
            // view.setBackgroundColor(getResources().getColor(R.color.plus_stream_spacer_color));
        }

        if (getArguments() != null && getArguments().getBoolean(EXTRA_ADD_VERTICAL_MARGINS, false)) {
            int verticalMargin = getResources().getDimensionPixelSize(R.dimen.topics_list_padding_vertical);
            if (verticalMargin > 0) {
                listView.setClipToPadding(false);
                listView.setPadding(0, verticalMargin, 0, verticalMargin);
            }
        }

        listView.setOnScrollListener(this);
        listView.setDrawSelectorOnTop(true);
        // listView.setDivider(getResources().getDrawable(android.R.color.transparent));
        // listView.setDividerHeight(getResources()
        // .getDimensionPixelSize(R.dimen.page_margin_width));

        TypedValue v = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.clickableItemBackground, v, true);
        listView.setSelector(v.resourceId);

        setListAdapter(mAdapter);
    }

    private void setCategoryView() {
        if (mCat == null) {
            L.i(" category is null ");
        } else {
            mCategoryView.setOnClickListener(mClickListener);
        }

        Utils.setCategoryView(mCat, mCategoryView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Utils.EXTRA_OBJ_C, mCat);
        outState.putString(Utils.EXTRA_SLUG, mSlug);
        outState.putLong(Utils.EXTRA_ID, mId);
        if (isAdded()) {
            View v = getListView().getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            outState.putInt(STATE_POSITION, getListView().getFirstVisiblePosition());
            outState.putInt(STATE_TOP, top);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Pause disk cache access to ensure smoother scrolling
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
            mImageLoader.stopProcessingQueue();
        } else {
            mImageLoader.startProcessingQueue();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!isStreamLoading() && streamHasMoreResults() && visibleItemCount != 0 && firstVisibleItem + visibleItemCount >= totalItemCount - 1) {
            loadMoreResults();
        }
    }

    public void refresh() {
        refresh(false);
    }

    public void refresh(boolean forceRefresh) {
        if (isStreamLoading() && !forceRefresh) {
            return;
        }

        // clear current items
        mData.clear();
        mAdapter.notifyDataSetInvalidated();

        if (isAdded()) {
            Loader loader = getLoaderManager().getLoader(TOPICS_LOADER_ID);
            ((TopicStreamLoader) loader).init(mSiteUrl, mSlug, mId);
        }

        loadMoreResults();
    }

    public void loadMoreResults() {
        if (isAdded()) {
            Loader loader = getLoaderManager().getLoader(TOPICS_LOADER_ID);
            if (loader != null) {
                loader.forceLoad();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.topic_fragment, menu);
        // MenuItem notificationsItem = menu.findItem(R.id.action_notifications);
        // View view = notificationsItem.getActionView();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mData == null || mData.mTopic == null || mData.mTopicDetails == null) {
            return;
        }
        MenuItem edit = menu.findItem(R.id.action_edit_topic);
        edit.setVisible(mData.mTopicDetails.can_edit);
        MenuItem invite = menu.findItem(R.id.action_invite_reply);
        invite.setVisible(mData.mTopicDetails.can_invite_to);
        MenuItem reply = menu.findItem(R.id.action_reply);
        reply.setVisible(mData.mTopicDetails.can_create_post);
        // MenuItem replyNew = menu.findItem(R.id.action_reply_as_new);
        // replyNew.setVisible(mData.mTopicDetails.can_reply_as_new_topic);
        MenuItem star = menu.findItem(R.id.action_star);
        final boolean checked = mData.mTopic.starred;
        star.setVisible(true);
        star.setChecked(checked);
        star.setIcon(checked ? R.drawable.btn_star_on_normal : R.drawable.btn_star_off_normal);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean r = super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_star:
                L.i("topic star : " + item.isChecked());
                boolean checked = !item.isChecked();
                item.setChecked(checked);
                item.setIcon(checked ? R.drawable.btn_star_on_normal : R.drawable.btn_star_off_normal);
                return true;
            case R.id.action_reply:
                if (mData != null && mData.mTopic != null) {
                    Post post = (Post) mAdapter.getItem(0);
                    openReplayTopicActivity(mData.mTopic, post);
                }
                return true;
            case R.id.action_edit_topic:
                // 编辑标题和分类
                Topic t = mData.mTopic;
                EditorChangeTitleFragment fragment = new EditorChangeTitleFragment();
                Bundle args = new Bundle();
                args.putLong(Utils.EXTRA_ID, t.category_id);
                args.putString(Utils.EXTRA_TITLE, t.title);
                fragment.setArguments(args);
                fragment.show(getChildFragmentManager(), "edit_title");
                L.i("edit topic title: %s - cat: %d", t.title, t.category_id);
                return true;
            case R.id.action_invite_reply:
                showInviteReplyDialog();
                return true;

        }
        return r;
    }

    @Override
    public void onTopicChange(String title, String category, long categoryId) {
        L.i("t: %s ，c: %s", title, category);
        Topic t = mData.mTopic;
        if (t.title.equals(title) && t.category_id == categoryId) {
            return;
        }
        changeTopicTitleAndCategory(t, title, category, categoryId);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Object data = l.getItemAtPosition(position);
        if (data instanceof Topic) {
            Topic t = (Topic) data;
            ActivityUtils.startTopicActivity(getActivity(), t.slug, t.id);
        }
        L.i("%d : %s", position, l.getItemAtPosition(position));
    }

    @Override
    public Loader<TopicStream> onCreateLoader(int id, Bundle args) {
        return new TopicStreamLoader(getActivity(), mSiteUrl, mSlug, mId);
    }

    @Override
    public void onLoadFinished(Loader<TopicStream> loader, TopicStream data) {
        checkAdView();
        if (data != null) {
            mData = data;
            postTopicTimings();
        }

        mAdapter.notifyDataSetChanged();
        mNotificationSpinner.setVisibility(View.VISIBLE);
        if (mData.mTopicDetails != null) {
            setNotificationStatus((int) mData.mTopicDetails.notification_level);
            L.i("can_invite_to...." + mData.mTopicDetails.can_invite_to);

        }
        if (mData.mTopic != null) {
            setTopicStatus(mData.mTopic);
            setTitle(mData.mTopic.title);
            if (mCat == null) {
                loadCategory(mData.mTopic.category_id);
            } else {
                setCategoryView();
            }
        }
        getActivity().invalidateOptionsMenu();
        if (mListViewStatePosition != -1 && isAdded()) {
            getListView().setSelectionFromTop(mListViewStatePosition, mListViewStateTop);
            mListViewStatePosition = -1;
        }
    }

    private void setTitle(String title) {
        getActivity().setTitle(title);
    }

    private void loadCategory(long cid) {
        Cursor c = getActivity().getContentResolver().query(Provider.CATEGORIES_CONTENT_URI, CategoriesTable.ALL_COLUMNS, CategoriesTable.UID + " = " + cid, null, null);
        if (c != null && c.moveToFirst()) {
            mCat = new Category(c);
            setCategoryView();
        }
    }

    @Override
    public void onLoaderReset(Loader<TopicStream> loader) {

    }

    private boolean isStreamLoading() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(TOPICS_LOADER_ID);
            if (loader != null) {
                return ((TopicStreamLoader) loader).isLoading();
            }
        }
        return true;
    }

    private boolean streamHasMoreResults() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(TOPICS_LOADER_ID);
            if (loader != null) {
                return ((TopicStreamLoader) loader).hasMoreResults();
            }
        }
        return false;
    }

    private boolean streamHasError() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(TOPICS_LOADER_ID);
            if (loader != null) {
                return ((TopicStreamLoader) loader).hasError();
            }
        }
        return false;
    }

    private int getPositionForView(View v) {
        int position = getListView().getPositionForView(v) - getListView().getHeaderViewsCount();
        return position;
    }

    protected void openReplayNewActivity() {
        ActivityUtils.openNewEditorActivity(getActivity(), mMenuPost);
    }

    protected void openPostLinksDialog() {
        Links[] links;
        if (mMenuTopicDetails != null) {
            links = mMenuTopicDetails.links;
        } else {
            links = mMenuPost.link_counts;
        }
        LinksDialogFragment frag = new LinksDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(Utils.EXTRA_LINKS, links);
        frag.setArguments(args);
        frag.show(getChildFragmentManager(), "links_frag");
    }

    protected void showPopupActioinMenu(View v) {
        int position = getPositionForView(v);
        L.d("postion %d at all %d", position, mAdapter.getCount());
        Post post = (Post) mAdapter.getItem(position);
        mMenuPost = post;
        int links;
        if (position == 0) {
            mMenuTopicDetails = mData.mTopicDetails;
            links = mData.getTopicLinksSize();
        } else {
            links = post.getLinksSize();
            mMenuTopicDetails = null;
        }
        PopupMenu menu = new PopupMenu(getActivity(), v);
        menu.inflate(R.menu.post_action_menu);
        Menu m = menu.getMenu();

        m.findItem(R.id.menu_post_flag).setVisible(post.showFlag() && false);// TODO 第一个版本 不支持该功能
        MenuItem bookmark = m.findItem(R.id.menu_post_bookmark);
        bookmark.setVisible(App.isLogin());
        bookmark.setChecked(post.bookmarked);

        MenuItem link = m.findItem(R.id.menu_post_links);
        link.setVisible(links > 0);
        link.setTitle(getResources().getString(R.string.menu_links, links));
        MenuItem posters = m.findItem(R.id.menu_poster_count);
        if (position == 0) {
            posters.setVisible(false);// TODO 应该为true，第一个版本不支持该功能
            int posterSize = mData.getPosterSize();
            posters.setTitle(getResources().getString(R.string.menu_poster_count, posterSize));
        } else {
            posters.setVisible(false);
        }

        m.findItem(R.id.menu_post_delete).setVisible(post.can_delete);
        menu.setOnMenuItemClickListener(mMenuListener);
        menu.show();
    }

    protected void onReplyClicked(View v) {
        int position = getPositionForView(v);
        Topic t = mData.mTopic;
        Post post = (Post) mAdapter.getItem(position);
        if (position == 0) {
            openReplayTopicActivity(t, post);
        } else {
            L.i("reply " + post.name);
            openReplayPostActivity(t, post, position + 1);
        }

    }

    private void openReplayPostActivity(Topic t, Post post, int postNum) {
        ActivityUtils.openReplayPostActivity(getActivity(), t, post, postNum);
    }

    private void openReplayTopicActivity(Topic t, Post p) {
        ActivityUtils.openReplayTopicActivity(getActivity(), t, p);
    }

    protected void onShareClicked(View v) {
        int position = getPositionForView(v);
        // Post post = (Post) mAdapter.getItem(position);
        String url = null;
        if (App.isLogin()) {
            url = String.format(Api.SHARE_LOGIN, mSlug, mId, position + 1, App.getUsername());
        } else {
            url = String.format(Api.SHARE, mSlug, mId, position + 1);
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, mSiteUrl + url);
        intent.setType("text/plain");
        startActivity(intent);

    }

    protected void onLikeClicked(View v) {
        int position = getPositionForView(v);
        Post post = (Post) mAdapter.getItem(position);
        L.d("like postion %d at all %d :%s", position, mAdapter.getCount(), post.username);
        Utils.cancelTask(mLikeTask);
        Utils.cancelTask(mUnlikeTask);
        if (!post.isLikeActed()) {
            mLikeTask = new LikeActionTask(TopicFragment.this, position, mSiteUrl, post.id, PostActionTask.TYPE_LIKE);
            mLikeTask.execute();
        } else if (post.isLikeCanUndo()) {
            mUnlikeTask = new UnlikeActionTask(TopicFragment.this, position, mSiteUrl, post.id);
            mUnlikeTask.execute();
        }
    }

    protected void onUserClicked(View v) {
        // TODO 显示PopupMenu（查看该用户帖子 ； 查看用户信息 ），或者打开用户信息？
        int position = getPositionForView(v);
        Post post = (Post) mAdapter.getItem(position);
        L.d("postion %d at all %d :%s", position, mAdapter.getCount(), post.username);
        ActivityUtils.openUserActivity(getActivity(), post.username);

    }

    protected void openCategoryActivity(View v) {
        if (mCat == null) {
            return;
        }
        ActivityUtils.openCategoryActivity(getActivity(), mCat, mSiteUrl);
    }

    private void changeTopicTitleAndCategory(Topic t, String title, String category, long cid) {
        new ChangeTopicTask(t, title, category, cid).execute();
    }

    protected void bookmarkClicked(boolean checked) {
        // TODO Auto-generated method stub
        L.i("%s : %s", mMenuPost.name, String.valueOf(checked));
        new BookmarkTask(!checked).execute();

    }

    protected void postTopicTimings() {
        new TopicTimingsTask(mId).execute();
    }

    protected void deletePostClicked() {
        new DeleteTask().execute();
    }

    protected void onRecoverClicked(View v) {
        int position = getPositionForView(v);
        Post post = (Post) mAdapter.getItem(position);
        L.i("recover " + post.name);
        new RecoverTask(post, position).execute();

    }

    protected void onEditClicked(View v) {
        int position = getPositionForView(v);
        Post post = (Post) mAdapter.getItem(position);
        mPostPosition = position;
        L.i("edit " + post.name);
        Intent intent = new Intent();
        intent.setClass(getActivity(), EditorActivity.class);
        intent.putExtra(Utils.EXTRA_NAME, post.username);
        intent.putExtra(Utils.EXTRA_NUMBER, position + 1);
        intent.putExtra(Utils.EXTRA_ID, post.id);
        intent.putExtra(Utils.EXTRA_IS_EDIT_POST, true);
        startActivityForResult(intent, EDIT_POST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == EDIT_POST_CODE) {
            String re = data.getStringExtra(Utils.EXTRA_MSG);
            try {
                JSONObject obj = new JSONObject(re);
                Post p = Api.getPost(obj.getJSONObject(Api.K_post));
                L.i(re);
                L.i("-------------\r %d .. %s", mPostPosition, p.cooked);
                // TODO fix this , FIXME 修改后 不显示新内容
                mAdapter.setPost(mPostPosition, p);
            } catch (JSONException e) {
                L.e(e, "edit post error");
            }
        }
    }

    private void hideAdView() {
        mAdView.setVisibility(View.GONE);
        mCloseAd.setVisibility(View.GONE);
    }

    private void checkAdView() {
        boolean showAd = new Random().nextInt(100) < Api.AD_RANDOM;
        if (showAd && mAdView.getVisibility() != View.VISIBLE) {
            showAdView();
        }
    }

    // ------------- edit post

    private void showAdView() {
        if (mAdRequest == null) {
            mAdRequest = new AdRequest();
        }
        mCloseAd.setVisibility(View.VISIBLE);
        mAdView.setVisibility(View.VISIBLE);
        mAdView.loadAd(mAdRequest);
    }

    @Override
    public void onDismissScreen(Ad arg0) {

    }

    @Override
    public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
        hideAdView();
    }

    @Override
    public void onLeaveApplication(Ad arg0) {

    }

    @Override
    public void onPresentScreen(Ad arg0) {

    }

    @Override
    public void onReceiveAd(Ad arg0) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_ad:
                hideAdView();
                break;

        }
    }

    private Topic[] getSuggestionTopics() {
        TopicDetails td = mData.mTopicDetails;
        if (td != null) {
            Topic[] topics = td.suggested_topics;
            return topics;
        }
        return null;
    }

    private void showInviteReplyDialog() {
        InviteReplyDialogFragment frag = new InviteReplyDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        frag.show(getChildFragmentManager(), "invite_reply");
    }

    @Override
    public void onInvite(String email) {
        //TODO ...
        new InviteTask(email, mId).execute();
    }

    private static class TopicStreamLoader extends AsyncTaskLoader<TopicStream> {
        public TopicStream mData;
        private boolean mIsLoading;
        private boolean mHasError;
        private String mSiteUrl;
        private Long mId;
        private String mSlug;

        public TopicStreamLoader(Context context, String siteUrl, String slug, Long id) {
            super(context);
            init(siteUrl, slug, id);
        }

        private void init(String siteUrl, String slug, Long id) {
            mHasError = false;
            mIsLoading = true;
            mSiteUrl = siteUrl;
            mSlug = slug;
            mId = id;
            mData = null;
        }

        @Override
        public TopicStream loadInBackground() {
            mIsLoading = true;
            String url = null;
            if (hasMoreResults()) {
                url = Utils.buildMorePostsUrl(App.getSiteUrl(), mId, mData.mPostStreams, mData.mPosts.size());
            } else {
                url = Utils.buildPostsUrl(App.getSiteUrl(), mId, mSlug);
            }
            TopicStream data = Api.getPostsOfTopic(url);
            mHasError = data == null;
            return data;
        }

        @Override
        public void deliverResult(TopicStream data) {
            mIsLoading = false;
            if (data != null) {
                if (mData == null) {
                    mData = data;
                } else {
                    mData.addAll(data);
                }
            }
            if (isStarted()) {
                super.deliverResult(mData == null ? null : new TopicStream(mData));
            }
        }

        @Override
        protected void onStartLoading() {
            if (mData != null) {
                deliverResult(null);
            } else {
                forceLoad();
            }
        }

        @Override
        protected void onStopLoading() {
            mIsLoading = false;
            cancelLoad();
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
            mData = null;
        }

        public boolean isLoading() {
            return mIsLoading;
        }

        public boolean hasError() {
            return mHasError;
        }

        public void refresh() {
            reset();
            startLoading();
        }

        public boolean hasMoreResults() {
            if (mData == null) {
                return false;
            }
            if (mData.mPostStreams == null) {
                return false;
            }
            if (mData.mPostStreams.length <= mData.mPosts.size()) {
                return false;
            }
            return true;
        }

    }

    static class UnlikeActionTask extends AsyncTask<Void, Void, Boolean> {
        private final String mSite;
        private final long mId;
        private final int mIndex;
        private final WeakReference<TopicFragment> mF;

        public UnlikeActionTask(TopicFragment f, int index, String site, long id) {
            mF = new WeakReference<TopicFragment>(f);
            mSite = site;
            mIndex = index;
            mId = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // TODO 删除 喜欢 出错。。。。
                String url = mSite + String.format(Api.DELETE_POST_ACTIONS, mId);
                HttpRequest hr = HttpRequest.delete(url);
                HttpURLConnection connection = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(connection);
                }
                hr.contentType(HttpRequest.CONTENT_TYPE_FORM, HttpRequest.CHARSET_UTF8);
                hr.accept("*/*");
                int code = hr.code();
                L.i("%s star code %d ", url, code);
                String body = hr.body();
                L.i(body);
                return code == HttpStatus.SC_OK;
            } catch (Exception e) {
                L.e(e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            TopicFragment f = mF.get();
            if (result && f != null) {
                try {
                    Post post = (Post) f.mAdapter.getItem(mIndex);
                    if (post == null) {
                        return;
                    }
                    post.updateLikeAction(false);
                    f.mAdapter.setPost(mIndex, post);
                } catch (Exception e) {
                    L.d(e);
                }
            }
        }
    }

    static class LikeActionTask extends PostActionTask {
        private final WeakReference<TopicFragment> mF;
        private final int mIndex;

        public LikeActionTask(TopicFragment f, int index, String site, long id, int type) {
            super(site, id, type);
            mF = new WeakReference<TopicFragment>(f);
            mIndex = index;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            TopicFragment f = mF.get();
            if (result != null && f != null) {
                try {
                    Post post = Api.getPost(new JSONObject(result));
                    post.updateLikeAction(true);
                    f.mAdapter.setPost(mIndex, post);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class PostAdapter extends BaseAdapter {
        private static final int VIEW_TYPE_ITEM = 0;
        private static final int VIEW_TYPE_LOADING = 1;
        private static final int VIEW_TYPE_TOPIC_HEADER = 2;
        private static final int VIEW_TYPE_TOPIC = 3;

        public void setPost(int index, Post data) {
            mData.setPost(index, data);
            notifyDataSetChanged();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            int type = getItemViewType(position);
            return type == VIEW_TYPE_ITEM || type == VIEW_TYPE_TOPIC;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        /**
         * 显示建议的帖子， 当帖子滚动到底 没有新的帖子的时候 ，返回 建议的主题。。。。 PostAdapter 添加一种Topic类型的数据
         */
        @Override
        public int getCount() {
            int postSize = mData.getSize();
            int topicCount = 0;
            if (!streamHasMoreResults() && postSize > 0) {
                Topic[] topics = getSuggestionTopics();
                // L.i("suggested topices: %d , %b", topics.length, streamHasMoreResults());
                if (topics != null && topics.length > 0) {
                    topicCount = topics.length + 1;// 1为topic header
                }
            }
            //@formatter:off
            return topicCount + mData.getSize() +
                    ( // show the status list row if...
                            ((isStreamLoading() && mData.getSize() == 0) // ...this is the first load
                                    || streamHasMoreResults() // ...or there's another page
                                    || streamHasError() // ...or there's an error
                            ) ? 1 : 0);
            //@formatter:on
        }

        @Override
        public int getItemViewType(int position) {
            int postSize = mData.getSize();
            if (streamHasMoreResults() || postSize == 0) {
                return (position >= postSize) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
            } else {
                if (position < postSize) {
                    return VIEW_TYPE_ITEM;
                } else if (position == postSize) {
                    return VIEW_TYPE_TOPIC_HEADER;
                } else {
                    return VIEW_TYPE_TOPIC;
                }
            }
        }

        @Override
        public Object getItem(int position) {
            int postSize = mData.getSize();
            int type = getItemViewType(position);
            if (type == VIEW_TYPE_ITEM) {
                return mData.getPost(position);
            } else if (type == VIEW_TYPE_TOPIC) {
                Topic[] topics = getSuggestionTopics();
                if (topics != null && topics.length > 0) {
                    int index = position - postSize - 1;
                    L.i("post size: %d  position: %d index: %d", postSize, position, index);
                    if (index >= 0 && index < topics.length) {
                        return topics[index];
                    }
                }
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO: better unique ID heuristic
            return position;
            // return (getItemViewType(position) == VIEW_TYPE_ITEM)
            // ? mData.getPost(position).id.hashCode()
            // : -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            if (type == VIEW_TYPE_LOADING) {
                if (convertView == null) {
                    convertView = getLayoutInflater(null).inflate(R.layout.list_item_stream_status, parent, false);
                }

                if (streamHasError()) {
                    convertView.findViewById(android.R.id.progress).setVisibility(View.GONE);
                    ((TextView) convertView.findViewById(android.R.id.text1)).setText(R.string.stream_error);
                } else {
                    convertView.findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
                    ((TextView) convertView.findViewById(android.R.id.text1)).setText(R.string.loading);
                }

                return convertView;

            } else if (type == VIEW_TYPE_ITEM) {
                Post data = mData.getPost(position);
                if (convertView == null) {
                    convertView = getLayoutInflater(null).inflate(R.layout.post_list_item, parent, false);
                }

                PostRowViewBinder.bindItemView(getActivity(), convertView, data, mImageLoader, mData, position, mClickListener);
                return convertView;
            } else if (type == VIEW_TYPE_TOPIC_HEADER) {
                if (convertView == null) {
                    convertView = getLayoutInflater(null).inflate(R.layout.suggest_topic_header, parent, false);
                }
                return convertView;
            } else if (type == VIEW_TYPE_TOPIC) {
                Topic topic = (Topic) getItem(position);
                if (convertView == null) {
                    convertView = getLayoutInflater(null).inflate(R.layout.topic_list_item, parent, false);
                }

                TopicRowViewBinder.bindItemView(convertView, topic, mImageLoader, false, null, null);
                return convertView;
            }
            // Can not go to heare
            return convertView;
        }
    }

    class ChangeTopicTask extends AsyncTask<Void, Void, String> {
        private static final String TITLE_PARAM = "title";
        private static final String CATEGORY_PARAM = "category";
        private final Topic mTopic;
        private final String mTitle;
        private final String mCategory;
        private final long mCategoryId;

        public ChangeTopicTask(Topic t, String title, String category, long cid) {
            mTopic = t;
            mTitle = title;
            mCategory = category;
            mCategoryId = cid;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String url = App.getSiteUrl() + String.format(Api.EDIT_TOPIC_TITLE, mTopic.slug, mTopic.id);
                HttpRequest hr = HttpRequest.put(url);
                HttpURLConnection connection = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(connection);
                }
                hr.form(TITLE_PARAM, mTitle);
                hr.form(CATEGORY_PARAM, mCategory);
                int code = hr.code();
                L.i("%s star code %d ", url, code);
                String body = hr.body();
                L.i(body);
                return body;
            } catch (Exception e) {
                L.e(e);
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            L.i("posts topic 1");
            mProgressFragment = new ProgressFragment();
            Bundle args = new Bundle();
            args.putString(Utils.EXTRA_MSG, getString(R.string.saving_title));
            mProgressFragment.setArguments(args);
            mProgressFragment.show(getFragmentManager(), "progress");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            L.i("posts topic 2");
            getFragmentManager().beginTransaction().remove(mProgressFragment).commit();
            mProgressFragment = null;

            try {
                JSONObject topic = new JSONObject(result);
                if (topic.has(Api.K_basic_topic)) {
                    setTitle(mTitle);
                    mData.mTopic.category_id = mCategoryId;
                    loadCategory(mCategoryId);
                }

            } catch (JSONException e) {
                L.e(e, "create posts error");
            }
        }

    }

    class BookmarkTask extends AsyncTask<Void, Void, String> {
        private static final String BOOKMARKED_PARAM = "bookmarked";
        boolean mMarked;

        public BookmarkTask(boolean marked) {
            mMarked = marked;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Post p = mMenuPost;
                String url = App.getSiteUrl() + String.format(Api.BOOKMARK, p.id);
                HttpRequest hr = HttpRequest.put(url);
                HttpURLConnection connection = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(connection);
                }
                int code = hr.form(BOOKMARKED_PARAM, String.valueOf(mMarked)).code();
                L.i("%s star code %d ", url, code);
                String body = hr.body();
                L.i(body);
                return String.valueOf(code);
            } catch (Exception e) {
                L.e(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int res = mMarked ? R.string.bookmark_add_failure : R.string.bookmark_remove_failure;
            if ("200".equals(result)) {
                res = mMarked ? R.string.bookmark_add_success : R.string.bookmark_remove_success;
                mMenuPost.bookmarked = mMarked;
            }
            Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
        }

    }

    class TopicTimingsTask extends AsyncTask<Void, Void, String> {
        long id;

        public TopicTimingsTask(long id) {
            this.id = id;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String url = App.getSiteUrl() + Api.TOPIC_TIMINGS;
                HttpRequest hr = HttpRequest.post(url);
                HttpURLConnection connection = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(connection);
                }
                int code = hr.form(Api.Params.TOPIC_ID, mId).form(Api.Params.TOPIC_TIME, 1000).code();
                L.i("%s topic timings %d ", url, code);
                return String.valueOf(code);
            } catch (Exception e) {
                L.e(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }

    class DeleteTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Post p = mMenuPost;
                String url = App.getSiteUrl() + String.format(Api.DELETE_POST, p.id);
                HttpRequest hr = HttpRequest.delete(url);
                HttpURLConnection connection = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(connection);
                }
                int code = hr.code();
                L.i("%s star code %d ", url, code);
                String body = hr.body();
                L.i(body);
                return String.valueOf(code);
            } catch (Exception e) {
                L.e(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int res = R.string.post_delete_failure;
            if ("200".equals(result)) {
                res = R.string.post_delete_success;
                final Post p = mMenuPost;
                p.cooked = getResources().getString(R.string.post_delete_content);
                p.can_delete = false;
                p.can_recover = true;
                p.can_edit = false;
                mAdapter.notifyDataSetChanged();
            }
            Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
        }

    }

    class RecoverTask extends AsyncTask<Void, Void, String> {
        Post mPost;
        int mIndex;

        public RecoverTask(Post p, int i) {
            mPost = p;
            mIndex = i;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Post p = mPost;
                String url = App.getSiteUrl() + String.format(Api.RECOVER_POST, p.id);
                HttpRequest hr = HttpRequest.put(url);
                HttpURLConnection connection = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(connection);
                }
                int code = hr.code();
                L.i("%s star code %d ", url, code);
                String body = hr.body();
                L.i(body);
                return body;
            } catch (Exception e) {
                L.e(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject obj = new JSONObject(result);
                Post p = Api.getPost(obj);
                mAdapter.setPost(mIndex, p);
            } catch (JSONException e) {
                int res = R.string.post_recover_failure;
                Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
            }
        }

    }

    class InviteTask extends AsyncTask<Void, Void, Boolean> {
        private static final String SUCCESS = "success";
        private static final String FAILED = "failed";
        private String mEmail;
        private long mId;

        public InviteTask(String email, long id) {
            mEmail = email;
            mId = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String url = App.getSiteUrl() + String.format(Api.INVITE, mId);
                HttpRequest hr = HttpRequest.post(url);
                HttpURLConnection connection = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(connection);
                }
                int code = hr.form("user", mEmail).code();
                L.i("%s star code %d ", url, code);
                String body = hr.body();
                L.i(body);
                try {
                    JSONObject obj = new JSONObject(body);
                    return obj.has(SUCCESS);
                } catch (Exception e) {
                }
            } catch (Exception e) {
                L.e(e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            String txt = null;
            if (result) {
                txt = getResources().getString(R.string.invite_success, mEmail);
            } else {
                txt = getResources().getString(R.string.invite_error);
            }
            Toast.makeText(getActivity(), txt, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}
