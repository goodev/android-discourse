package org.goodev.discourse;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.goodev.discourse.contentprovider.Provider;
import org.goodev.discourse.database.tables.SiteTable;
import org.goodev.discourse.database.tables.UserInfoTable;
import org.goodev.discourse.ui.AddSiteFragment;
import org.goodev.discourse.ui.AddSiteFragment.AddSiteListener;
import org.goodev.discourse.ui.AddUserFragment.AddUserListener;
import org.goodev.discourse.ui.CheckLoginInfoFragment;
import org.goodev.discourse.ui.CheckLoginInfoFragment.CheckLoginInfoCallback;
import org.goodev.discourse.ui.CheckUrlFragment;
import org.goodev.discourse.ui.CheckUrlFragment.CheckUrlCallback;
import org.goodev.discourse.ui.ProgressFragment;
import org.goodev.discourse.ui.SettingsFragment;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.MCrypt;
import org.goodev.discourse.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;

public class SettingsActivity extends FragmentActivity implements AddSiteListener, AddUserListener, CheckUrlCallback, CheckLoginInfoCallback {
    private static final String FRAG_TAG = "fragment_tag";

    private Fragment mFragment;

    private String mCurrentSiteUrl;
    private String mName;
    private String mPassword;
    private boolean mRememberLoginInfo;
    private CheckUrlFragment mCheckUrlFragment;
    private String mSiteName;
    private String mSiteUrl;
    private ProgressFragment mProgressFragment;
    private CheckLoginInfoFragment mCheckLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_settings);
        setupActionBar();
        if (savedInstanceState == null) {
            mFragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, mFragment, FRAG_TAG).commit();
        } else {
            mFragment = getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        }

    }

    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add_website:
                showAddSiteDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddSiteDialog() {
        new AddSiteFragment().show(getSupportFragmentManager(), "add_site_tag");
    }

    @Override
    public void add(String name, String url) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, R.string.add_site_url_is_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            name = url;
        }
        if (url.startsWith(Utils.HTTP_PREFIX) || url.startsWith(Utils.HTTPS_PREFIX)) {
            // nothing
        } else {
            url = Utils.HTTP_PREFIX + url;
        }
        if (!url.endsWith(Utils.SLASH)) {
            url = url + Utils.SLASH;
        }
        mSiteName = name;
        mSiteUrl = url;
        try {
            URL urlTest = new URL(url);
        } catch (MalformedURLException e) {
            Toast.makeText(this, R.string.add_site_url_error, Toast.LENGTH_LONG).show();
            return;
        }

        FragmentManager manager = getSupportFragmentManager();
        if (mCheckUrlFragment != null) {
            manager.beginTransaction().remove(mCheckUrlFragment).commit();
        }
        mCheckUrlFragment = new CheckUrlFragment();
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_URL, url);
        mCheckUrlFragment.setArguments(args);
        manager.beginTransaction().add(mCheckUrlFragment, "check_url").commit();
    }

    @Override
    public void onPreExecute() {
        mProgressFragment = new ProgressFragment();
        mProgressFragment.show(getSupportFragmentManager(), "progress");
    }

    @Override
    public void onResult(boolean ok) {
        getSupportFragmentManager().beginTransaction().remove(mProgressFragment).commit();
        if (ok) {
            saveSite();
        } else {
            Toast.makeText(this, getString(R.string.add_site_url_error, mSiteUrl), Toast.LENGTH_LONG).show();
        }
    }

    private void saveSite() {
        Cursor cursor = getContentResolver().query(Provider.SITE_CONTENT_URI, SiteTable.ALL_COLUMNS, SiteTable.URL + " = \"" + mSiteUrl + "\"", null, null);
        if (cursor.getCount() > 0) {
            Toast.makeText(this, getString(R.string.add_site_is_exist, mSiteUrl), Toast.LENGTH_LONG).show();
            return;
        } else {
            ContentValues values = new ContentValues();
            values.put(SiteTable.TITLE, mSiteName);
            values.put(SiteTable.URL, mSiteUrl);
            getContentResolver().insert(Provider.SITE_CONTENT_URI, values);
        }
    }

    public void setCurrentSite(String url) {
        mCurrentSiteUrl = url;
    }

    @Override
    public void add(String name, String password, boolean remember) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.name_and_password_is_empty, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(mCurrentSiteUrl)) {
            return;
        }
        mRememberLoginInfo = remember;
        mName = name;
        mPassword = password;
        FragmentManager manager = getSupportFragmentManager();
        if (mCheckLoginFragment != null) {
            manager.beginTransaction().remove(mCheckLoginFragment).commit();
        }
        mCheckLoginFragment = new CheckLoginInfoFragment();
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_URL, mCurrentSiteUrl);
        args.putString(Utils.EXTRA_NAME, name);
        args.putString(Utils.EXTRA_PASSWORD, password);
        mCheckLoginFragment.setArguments(args);
        manager.beginTransaction().add(mCheckLoginFragment, "check_user").commit();

    }

    private void saveLoginInfo(String name, String password) {
        App.setUserInfo(name, password);
        if (mRememberLoginInfo) {
            Cursor cursor = getContentResolver().query(Provider.SITE_CONTENT_URI, SiteTable.ALL_COLUMNS, SiteTable.URL + " = \"" + mCurrentSiteUrl + "\"", null, null);
            if (cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndex(SiteTable.ID));

                ContentValues values = new ContentValues();
                values.put(UserInfoTable.NAME, name);
                String crypto;
                try {
                    crypto = MCrypt.bytesToHex(new MCrypt().encrypt(password));
                    L.i("p: '%s'  c: '%s'", password, crypto);
                } catch (Exception e) {
                    crypto = password;
                }
                values.put(UserInfoTable.PASSWORD, crypto);
                values.put(UserInfoTable.SITEID, id);
                values.put(UserInfoTable.SITEURL, mCurrentSiteUrl);
                getContentResolver().insert(Provider.USERINFO_CONTENT_URI, values);
            }
        }
    }

    @Override
    public void onPreCheckExecute() {
        mProgressFragment = new ProgressFragment();
        mProgressFragment.show(getSupportFragmentManager(), "progress");
    }

    @Override
    public void onCheckResult(boolean ok) {
        getSupportFragmentManager().beginTransaction().remove(mProgressFragment).commit();
        if (ok) {
            saveLoginInfo(mName, mPassword);
        } else {
            Toast.makeText(this, R.string.name_and_password_is_error, Toast.LENGTH_LONG).show();
        }
    }

}
