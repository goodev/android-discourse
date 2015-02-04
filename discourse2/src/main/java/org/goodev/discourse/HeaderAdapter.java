package org.goodev.discourse;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HeaderAdapter extends BaseAdapter {
    public final static int LATEST = 0;
    public final static int CATEGORY = 5;
    public final static int NEW = 1;
    public final static int UNREAD = 2;
    public final static int READ = 3;
    public final static int FAVORITE = 4;
    private static final int TYPE_LOADING = 0;
    private static final int TYPE_CATEGORY = 1;
    private static final int TYPE_HEADER_NAV = 2;
    private static final int TYPE_NAVIGATION = 3;
    private static final int TYPE_HEADER_CATEGORY = 4;
    private final Context mContext;
    private final DrawerCategoryAdapter mAdapter;
    private boolean mIsLogin;
    private boolean mHasSite;

    public HeaderAdapter(Context ctx, DrawerCategoryAdapter adapter) {
        mContext = ctx;
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(new MyDataSetObserver());
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        int type = getItemViewType(position);
        if (type == TYPE_CATEGORY || type == TYPE_NAVIGATION) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER_NAV;
        }
        int nav = getNavCount();
        if (position <= nav) {
            return TYPE_NAVIGATION;
        }
        if (position == nav + 1) {
            return TYPE_HEADER_CATEGORY;
        }
        int count = mAdapter.getCount();
        if (count == 0 && position > nav + 1) {
            return TYPE_LOADING;
        }

        return TYPE_CATEGORY;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    private int getNavCount() {
        return mIsLogin ? 5 : 1; // TODO 去掉 category 导航
    }

    @Override
    public int getCount() {
        if (!mHasSite) {
            // 如果没有设置网站，则只显示Navigation header。
            return 1;
        }
        int count = mAdapter.getCount();
        int nav = getNavCount();
        return (count == 0 ? 1 : count) + nav + 2;// 2个header
    }

    @Override
    public Object getItem(int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_HEADER_NAV:
            case TYPE_HEADER_CATEGORY:
            case TYPE_LOADING:
                return null;
            case TYPE_NAVIGATION:
                return Integer.valueOf(position - 1);
            case TYPE_CATEGORY:
                int count = mAdapter.getCount();
                return count == 0 ? null : mAdapter.getItem(getCategoryPosition(position));

        }
        return null;
    }

    private int getCategoryPosition(int position) {
        return position - getNavCount() - 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_HEADER_NAV:
                TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.drawer_list_header_item, parent, false);
                view.setText(R.string.drawer_nav_header);
                return view;
            case TYPE_HEADER_CATEGORY:
                view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.drawer_list_header_item, parent, false);
                view.setText(R.string.drawer_categories_title);
                return view;
            case TYPE_LOADING:
                return LayoutInflater.from(mContext).inflate(R.layout.drawer_category_item_loading, parent, false);
            case TYPE_NAVIGATION:
                view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.drawer_nav_item, parent, false);
                view.setText(getNavTitle(position - 1));
                return view;
            case TYPE_CATEGORY:
                return mAdapter.getView(getCategoryPosition(position), convertView, parent);

        }
        return null;
    }

    private CharSequence getNavTitle(int i) {
        switch (i) {
            case LATEST:
                return getString(R.string.title_section1);
            // case CATEGORY:
            // return getString(R.string.title_section2);
            case NEW:
                return getString(R.string.title_new);
            case UNREAD:
                return getString(R.string.title_unread);
            case READ:
                return getString(R.string.title_section5);
            case FAVORITE:
                return getString(R.string.title_section6);
        }
        return "";
    }

    private CharSequence getString(int resId) {
        return mContext.getString(resId);
    }

    public void swapCursor(Cursor data) {
        mAdapter.swapCursor(data);
    }

    public void setLogin(boolean login) {
        if (mIsLogin == login) {
            return;
        }
        mIsLogin = login;
        notifyDataSetChanged();
    }

    public void setHasSite(boolean has) {
        mHasSite = has;
    }

    private class MyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            notifyDataSetInvalidated();
        }
    }
}
