package org.goodev.discourse.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import org.goodev.discourse.R;
import org.goodev.discourse.SettingsActivity;
import org.goodev.discourse.contentprovider.Provider;
import org.goodev.discourse.database.tables.SiteTable;
import org.goodev.discourse.database.tables.UserInfoTable;
import org.goodev.discourse.model.Site;
import org.goodev.discourse.ui.SimpleSectionedListAdapter.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * {@see com.google.android.apps.iosched.ui.SimpleSectionedListAdapter 用discourse网站把用户分组}
 * <p/>
 * <pre>
 * site one
 *    user one
 *    user two
 * site two
 *    user one
 *    user two
 * </pre>
 */
public class SettingsFragment extends ListFragment implements LoaderCallbacks<Cursor> {
    public static final int LOADER_USER_ID = 0;
    public static final int LOADER_SITE_ID = 1;
    private final ContentObserver mObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            if (!isAdded()) {
                return;
            }

            getLoaderManager().restartLoader(LOADER_SITE_ID, null, SettingsFragment.this);
        }

    };
    private final HashMap<Long, Site> mSitesMap = new HashMap<Long, Site>();
    private String mCurrentSiteUrl;
    private final OnClickListener mAddUserListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mCurrentSiteUrl = (String) v.getTag();
            if (v.getId() == R.id.delete_site) {
                deleteSite();
                return;
            }
            if (getActivity() instanceof SettingsActivity) {
                SettingsActivity a = (SettingsActivity) getActivity();
                a.setCurrentSite(mCurrentSiteUrl);
            }

            if (hasUserInfo()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.remove_exist_user);
                builder.setMessage(Html.fromHtml(getResources().getString(R.string.remove_exist_user_msg, mCurrentSiteUrl)));
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeExistUser();
                                showAddUserDialog();
                            }

                        });
                builder.create().show();
            } else {
                showAddUserDialog();
            }
        }
    };
    private SimpleSectionedListAdapter mAdapter;
    private UserAdapter mUserAdapter;

    private void removeExistUser() {
        Cursor cursor = getActivity().getContentResolver().query(Provider.USERINFO_CONTENT_URI, UserInfoTable.ALL_COLUMNS, UserInfoTable.SITEURL + " = \"" + mCurrentSiteUrl + "\"", null, null);
        cursor.moveToFirst();
        ContentResolver cr = getActivity().getContentResolver();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(cursor.getColumnIndex(UserInfoTable.ID));
            cr.delete(Provider.USERINFO_CONTENT_URI, UserInfoTable.ID + "=" + id, null);
            cursor.moveToNext();
        }

    }

    protected void deleteSite() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_site);
        builder.setMessage(Html.fromHtml(getResources().getString(R.string.delete_site_msg, mCurrentSiteUrl)));
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentResolver cr = getActivity().getContentResolver();
                        cr.delete(Provider.SITE_CONTENT_URI, SiteTable.URL + " = \"" + mCurrentSiteUrl + "\"", null);
                        cr.delete(Provider.USERINFO_CONTENT_URI, UserInfoTable.SITEURL + " = \"" + mCurrentSiteUrl + "\"", null);
                    }

                });
        builder.create().show();

    }

    private boolean hasUserInfo() {
        Cursor cursor = getActivity().getContentResolver().query(Provider.USERINFO_CONTENT_URI, UserInfoTable.ALL_COLUMNS, UserInfoTable.SITEURL + " = \"" + mCurrentSiteUrl + "\"", null, null);
        return cursor.getCount() > 0;
    }

    private void showAddUserDialog() {
        new AddUserFragment().show(getFragmentManager(), "add_user_tag");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserAdapter = new UserAdapter(getActivity());
        mAdapter = new SimpleSectionedListAdapter(getActivity(), mUserAdapter, mAddUserListener);

        setListAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        getLoaderManager().initLoader(LOADER_SITE_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.getContentResolver().registerContentObserver(Provider.SITE_CONTENT_URI, true, mObserver);
        activity.getContentResolver().registerContentObserver(Provider.USERINFO_CONTENT_URI, true, mObserver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle data) {
        String userSort = UserInfoTable.SITEID + " ASC";
        String siteSort = SiteTable.ID + " ASC";
        switch (id) {
            case LOADER_SITE_ID:
                return new CursorLoader(getActivity(), Provider.SITE_CONTENT_URI, SiteTable.ALL_COLUMNS, null, null, siteSort);
            case LOADER_USER_ID:
                return new CursorLoader(getActivity(), Provider.USERINFO_CONTENT_URI, UserInfoTable.ALL_COLUMNS, null, null, userSort);
        }
        return null;
    }

    // TODO 先获取 site的数据，获取到site数据后，再获取 user数据
    // 好计算site的section 位置
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (!isAdded()) {
            return;
        }

        Context ctx = getActivity();
        if (loader.getId() == LOADER_SITE_ID) {

            mSitesMap.clear();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Site site = new Site(cursor);
                mSitesMap.put(site.getId(), site);
                cursor.moveToNext();
            }
            getLoaderManager().initLoader(LOADER_USER_ID, null, SettingsFragment.this);
        } else {
            List<Section> sections = new ArrayList<SimpleSectionedListAdapter.Section>();
            cursor.moveToFirst();
            long previousSiteId = -1;
            long siteId;
            List<Long> ids = new ArrayList<Long>();
            while (!cursor.isAfterLast()) {
                siteId = cursor.getLong(cursor.getColumnIndex(UserInfoTable.SITEID));
                Site site = mSitesMap.get(siteId);
                if (previousSiteId != siteId && site != null) {
                    ids.add(siteId);
                    sections.add(new Section(cursor.getPosition(), site.getTitle(), site.getUrl(), site.getId()));
                }
                previousSiteId = siteId;
                cursor.moveToNext();
            }

            int count = cursor.getCount();

            mUserAdapter.swapCursor(cursor);

            Set<Long> keys = mSitesMap.keySet();
            for (Long id : keys) {
                if (!ids.contains(id)) {
                    Site site = mSitesMap.get(id);
                    sections.add(new Section(count, site.getTitle(), site.getUrl(), site.getId()));
                }
            }
            Section[] dummy = new Section[sections.size()];
            mAdapter.setSections(sections.toArray(dummy));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class UserAdapter extends CursorAdapter {

        public UserAdapter(Context context) {
            super(context, null, 0);
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            final String name = cursor.getString(cursor.getColumnIndex(UserInfoTable.NAME));
            final TextView nameView = (TextView) view.findViewById(android.R.id.text1);
            nameView.setText(name);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

    }

}
