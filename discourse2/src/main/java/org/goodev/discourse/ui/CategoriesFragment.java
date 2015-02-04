package org.goodev.discourse.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.Categories;
import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.Utils;

import static org.goodev.discourse.api.Categories.VIEW_TYPE_CATEGORY_FOOTER;
import static org.goodev.discourse.api.Categories.VIEW_TYPE_LOADING;
import static org.goodev.discourse.api.Categories.VIEW_TYPE_TOPIC_ITEM;

public class CategoriesFragment extends ListFragment implements LoaderCallbacks<Categories> {
    public static final String EXTRA_ADD_VERTICAL_MARGINS = "extra_ADD_VERTICAL_MARGINS";
    private static final String STATE_POSITION = "extra_postion";
    private static final String STATE_TOP = "extra_top";
    private static final int CATEGORIES_LOADER_ID = 2;
    private final CategoryAdapter mAdapter = new CategoryAdapter();
    private Categories mCategories = new Categories();
    private ImageLoader mImageLoader;

    private String mSiteUrl;
    private int mListViewStatePosition;
    private int mListViewStateTop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageLoader = App.getImageLoader(getActivity(), getResources());
        if (savedInstanceState != null) {
            mSiteUrl = savedInstanceState.getString(Utils.EXTRA_URL);
        } else {
            Bundle arg = getArguments();
            if (arg != null) {
                mSiteUrl = arg.getString(Utils.EXTRA_URL);
            }
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mListViewStatePosition = savedInstanceState.getInt(STATE_POSITION, -1);
            mListViewStateTop = savedInstanceState.getInt(STATE_TOP, 0);
        } else {
            mListViewStatePosition = -1;
            mListViewStateTop = 0;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(R.string.title_categories);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.empty_topics));

        // In support library r8, calling initLoader for a fragment in a
        // FragmentPagerAdapter
        // in the fragment's onCreate may cause the same LoaderManager to be
        // dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the
        // activity yet). Thus,
        // we do this in onActivityCreated.
        getLoaderManager().initLoader(CATEGORIES_LOADER_ID, null, this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView listView = getListView();
        if (!Utils.isTablet(getActivity())) {
            // TODO ...
            // view.setBackgroundColor(getResources().getColor(R.color.plus_stream_spacer_color));
        }

        if (getArguments() != null && getArguments().getBoolean(EXTRA_ADD_VERTICAL_MARGINS, false)) {
            int verticalMargin = getResources().getDimensionPixelSize(R.dimen.categories_list_padding_vertical);
            if (verticalMargin > 0) {
                listView.setClipToPadding(false);
                listView.setPadding(0, verticalMargin, 0, verticalMargin);
            }
        }

        listView.setDrawSelectorOnTop(true);
        // listView.setDivider(getResources().getDrawable(android.R.color.transparent));
        // listView.setDividerHeight(getResources()
        // .getDimensionPixelSize(R.dimen.page_margin_width));

        TypedValue v = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.clickableItemBackground, v, true);
        listView.setSelector(v.resourceId);

        setListAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Utils.EXTRA_URL, mSiteUrl);
        if (isAdded()) {
            View v = getListView().getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            outState.putInt(STATE_POSITION, getListView().getFirstVisiblePosition());
            outState.putInt(STATE_TOP, top);
        }
        super.onSaveInstanceState(outState);
    }

    private boolean streamHasError() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(CATEGORIES_LOADER_ID);
            if (loader != null) {
                return ((CategoriesLoader) loader).hasError();
            }
        }
        return false;
    }

    private boolean isStreamLoading() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(CATEGORIES_LOADER_ID);
            if (loader != null) {
                return ((CategoriesLoader) loader).isLoading();
            }
        }
        return true;
    }

    @Override
    public Loader<Categories> onCreateLoader(int id, Bundle args) {
        return new CategoriesLoader(getActivity(), mSiteUrl);
    }

    @Override
    public void onLoadFinished(Loader<Categories> loader, Categories data) {
        if (data != null) {
            mCategories = data;
        }
        mAdapter.notifyDataSetChanged();
        if (mListViewStatePosition != -1 && isAdded()) {
            getListView().setSelectionFromTop(mListViewStatePosition, mListViewStateTop);
            mListViewStatePosition = -1;
        }
    }

    @Override
    public void onLoaderReset(Loader<Categories> arg0) {
        // TODO Auto-generated method stub

    }

    public void refresh() {
        refresh(false);
    }

    public void refresh(boolean forceRefresh) {
        if (isStreamLoading() && !forceRefresh) {
            return;
        }

        // clear current items
        mCategories.clear();
        mAdapter.notifyDataSetInvalidated();

        if (isAdded()) {
            Loader loader = getLoaderManager().getLoader(CATEGORIES_LOADER_ID);
            ((CategoriesLoader) loader).init(mSiteUrl);
        }

        loadMoreResults();
    }

    public void loadMoreResults() {
        if (isAdded()) {
            Loader loader = getLoaderManager().getLoader(CATEGORIES_LOADER_ID);
            if (loader != null) {
                loader.forceLoad();
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Object data = l.getItemAtPosition(position);
        L.i("dta: " + data);
    }

    private static class CategoriesLoader extends AsyncTaskLoader<Categories> {

        Categories mData;
        private boolean mIsLoading;
        private boolean mHasError;
        private String mSiteUrl;

        public CategoriesLoader(Context context, String siteUrl) {
            super(context);
            init(siteUrl);
        }

        private void init(String siteUrl) {
            mHasError = false;
            mIsLoading = true;
            mSiteUrl = siteUrl;
            mData = null;
        }

        @Override
        public Categories loadInBackground() {
            mIsLoading = true;
            if (mData == null) {
                mData = new Categories();
            }
            String url;
            url = mSiteUrl + Api.CATEGORIES;

            mData = Api.getCategories(url);
            mHasError = false;
            return mData;
        }

        @Override
        public void deliverResult(Categories data) {
            mIsLoading = false;
            if (data != null) {
                mData = data;
            }
            if (isStarted()) {
                // Need to return new ArrayList for some reason or
                // onLoadFinished() is not called
                super.deliverResult(mData);
            }
        }

        @Override
        protected void onStartLoading() {
            if (mData != null) {
                // If we already have results and are starting up, deliver what
                // we already have.
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

    }

    private class CategoryAdapter extends BaseAdapter {

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) != VIEW_TYPE_LOADING;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getCount() {
            return mCategories.getSize() + (
                    // show the status list row if...
                    ((isStreamLoading() && mCategories.getSize() == 0) // ...this
                            // is
                            // the
                            // first
                            // load
                            || streamHasError()) // ...or there's an error
                            ? 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
            return mCategories.getItemType(position);
        }

        @Override
        public Object getItem(int position) {
            return mCategories.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO: better unique ID heuristic
            return position;
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

            } else if (type == VIEW_TYPE_TOPIC_ITEM) {

                Topic topic = (Topic) mCategories.getItem(position);
                if (convertView == null) {
                    convertView = getLayoutInflater(null).inflate(R.layout.category_topic_list_item, parent, false);
                }

                CategoryTopicRowViewBinder.bindTopicItemView(convertView, topic);
                return convertView;
            } else if (type == VIEW_TYPE_CATEGORY_FOOTER) {

                Category c = (Category) mCategories.getItem(position);
                if (convertView == null) {
                    convertView = getLayoutInflater(null).inflate(R.layout.category_footer_list_item, parent, false);
                }

                CategoryTopicRowViewBinder.bindCategoryFooterView(convertView, c);
                return convertView;
            }
            {

                Category c = (Category) mCategories.getItem(position);
                if (convertView == null) {
                    convertView = getLayoutInflater(null).inflate(R.layout.category_header_list_item, parent, false);
                }

                CategoryTopicRowViewBinder.bindCategoryHeaderView(mImageLoader, convertView, c, mCategories);
                return convertView;
            }
        }

    }

}
