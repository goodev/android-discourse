package org.goodev.discourse.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;

import org.goodev.discourse.ActivityUtils;
import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.LatestTopics;
import org.goodev.discourse.api.LatestTopics.TopicList;
import org.goodev.discourse.api.StarTask;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.Utils;

public class TopicsListFragment extends ListFragment implements OnScrollListener, LoaderCallbacks<LatestTopics>, RefreshActionListener {

    public static final String EXTRA_ADD_VERTICAL_MARGINS = "extra_ADD_VERTICAL_MARGINS";
    private static final String STATE_POSITION = "extra_postion";
    private static final String STATE_TOP = "extra_top";
    private static final int TOPICS_LOADER_ID = 1;
    private static final int HOLDER_TAG_KEY = 2;
    private final TopicAdapter mAdapter = new TopicAdapter();
    protected String mSiteUrl;
    private LatestTopics mLatestTopics = new LatestTopics();
    private int mListViewStatePosition;
    private int mListViewStateTop;
    private ImageLoader mImageLoader;
    private RefreshActionItem mRefreshActionItem;
    private StarTask mStarTask;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // onCreate 在该函数后调用
    }

    public CharSequence getTitle() {
        return getResources().getString(R.string.title_latest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getTitle());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_topics_list, menu);
        MenuItem item = menu.findItem(R.id.action_refresh);
        mRefreshActionItem = (RefreshActionItem) item.getActionView();
        mRefreshActionItem.setMenuItem(item);
        mRefreshActionItem.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
        // mRefreshActionItem.setMax(100);
        mRefreshActionItem.setRefreshActionListener(this);
        if (isStreamLoading()) {
            mRefreshActionItem.showProgress(true);
        }
    }

    @Override
    public void onRefreshButtonClick(RefreshActionItem sender) {
        mListViewStatePosition = -1;
        refresh();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // case R.id.action_refresh:
            // return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.empty_topics));

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
        if (mRefreshActionItem != null)
            mRefreshActionItem.showProgress(true);
        refresh(false);
    }

    public void refresh(boolean forceRefresh) {
        if (isStreamLoading() && !forceRefresh) {
            return;
        }

        // clear current items
        mLatestTopics.clear();
        mAdapter.notifyDataSetInvalidated();

        if (isAdded()) {
            Loader loader = getLoaderManager().getLoader(TOPICS_LOADER_ID);
            ((LatestTopicsLoader) loader).init(mSiteUrl, getInitUrl());
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
    public Loader<LatestTopics> onCreateLoader(int id, Bundle args) {
        return new LatestTopicsLoader(getActivity(), mSiteUrl, getInitUrl());
    }

    @Override
    public void onLoadFinished(Loader<LatestTopics> loader, LatestTopics data) {
        if (mRefreshActionItem != null) {
            mRefreshActionItem.showProgress(false);
        }
        if (data != null) {
            mLatestTopics = data;
        }
        mAdapter.notifyDataSetChanged();
        if (mListViewStatePosition != -1 && isAdded()) {
            getListView().setSelectionFromTop(mListViewStatePosition, mListViewStateTop);
            mListViewStatePosition = -1;
        }
    }

    @Override
    public void onLoaderReset(Loader<LatestTopics> loader) {

    }

    private boolean isStreamLoading() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(TOPICS_LOADER_ID);
            if (loader != null) {
                return ((LatestTopicsLoader) loader).isLoading();
            }
        }
        return true;
    }

    private boolean streamHasMoreResults() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(TOPICS_LOADER_ID);
            if (loader != null) {
                return ((LatestTopicsLoader) loader).hasMoreResults();
            }
        }
        return false;
    }

    private boolean streamHasError() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(TOPICS_LOADER_ID);
            if (loader != null) {
                return ((LatestTopicsLoader) loader).hasError();
            }
        }
        return false;
    }

    public String getInitUrl() {
        return mSiteUrl + Api.LATEST;
    }

    public String getPopularUrl() {
        return mSiteUrl + Api.POPULAR;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Topic t = (Topic) l.getItemAtPosition(position);
        if (t == null) {
            return;
        }
        ActivityUtils.startTopicActivity(getActivity(), t.slug, t.id);
    }

    private static class LatestTopicsLoader extends AsyncTaskLoader<LatestTopics> {
        LatestTopics mLatestTopics;
        private boolean mIsLoading;
        private boolean mHasError;
        private String mSiteUrl;
        private String mInitUrl;

        public LatestTopicsLoader(Context context, String siteUrl, String initUrl) {
            super(context);
            init(siteUrl, initUrl);
        }

        private void init(String siteUrl, String initUrl) {
            mHasError = false;
            mIsLoading = true;
            mSiteUrl = siteUrl;
            mInitUrl = initUrl;
            mLatestTopics = null;
        }

        @Override
        public LatestTopics loadInBackground() {
            mIsLoading = true;
            if (mLatestTopics == null) {
                mLatestTopics = new LatestTopics();
            }
            TopicList tl = mLatestTopics.getTopicList();
            String url;
            if (tl != null && !TextUtils.isEmpty(tl.more_topics_url)) {
                url = Utils.removeLastSlash(mSiteUrl) + tl.more_topics_url;
            } else {
                url = mInitUrl;
            }

            boolean result = Api.getLatestTopics(mLatestTopics, url);
            //            if(!result && first) {
            //            	 result = Api.getLatestTopics(mLatestTopics, mSiteUrl+Api.POPULAR);
            //            }
            mHasError = !result;
            return result ? mLatestTopics : null;
        }

        @Override
        public void deliverResult(LatestTopics data) {
            mIsLoading = false;
            if (data != null) {
                if (mLatestTopics == null) {
                    mLatestTopics = data;
                } else {
                    // mLatestTopics 在 loadInBackground 函数中重用了 里面已经设置好数据了， 不用调用addAll()函数了
                    // mLatestTopics.addAll(data);
                }
            }
            if (isStarted()) {
                // Need to return new ArrayList for some reason or onLoadFinished() is not called
                super.deliverResult(mLatestTopics == null ? null : new LatestTopics(mLatestTopics));
            }
        }

        @Override
        protected void onStartLoading() {
            if (mLatestTopics != null) {
                // If we already have results and are starting up, deliver what we already have.
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
            mLatestTopics = null;
        }

        public boolean isLoading() {
            return mIsLoading;
        }

        public boolean hasMoreResults() {
            return mLatestTopics != null && mLatestTopics.getTopicList() != null && mLatestTopics.getTopicList().more_topics_url != null;
        }

        public boolean hasError() {
            return mHasError;
        }

        public void refresh() {
            reset();
            startLoading();
        }
    }

    private class TopicAdapter extends BaseAdapter {
        private static final int VIEW_TYPE_ITEM = 0;
        private static final int VIEW_TYPE_LOADING = 1;

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) == VIEW_TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getCount() {
            return mLatestTopics.getTopicSize() + (
                    // show the status list row if...
                    ((isStreamLoading() && mLatestTopics.getTopicSize() == 0) // ...this is the first load
                            || streamHasMoreResults() // ...or there's another page
                            || streamHasError()) // ...or there's an error
                            ? 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
            return (position >= mLatestTopics.getTopicSize()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public Object getItem(int position) {
            return (getItemViewType(position) == VIEW_TYPE_ITEM) ? mLatestTopics.getTopic(position) : null;
        }

        @Override
        public long getItemId(int position) {
            // TODO: better unique ID heuristic
            return (getItemViewType(position) == VIEW_TYPE_ITEM) ? mLatestTopics.getTopic(position).id.hashCode() : -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (getItemViewType(position) == VIEW_TYPE_LOADING) {
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

            } else {
                Topic topic = mLatestTopics.getTopic(position);
                if (convertView == null) {
                    convertView = getLayoutInflater(null).inflate(R.layout.topic_list_item, parent, false);
                }

                TopicRowViewBinder.bindItemView(convertView, topic, mImageLoader, false, mLatestTopics, mStarListener);
                return convertView;
            }
        }

    }

    private final OnCheckedChangeListener mStarListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Topic t = (Topic) buttonView.getTag();
            if (t == null || !App.isLogin()) {
                return;
            }

            if (mStarTask != null && !mStarTask.isCancelled()) {
                mStarTask.cancel(true);
            }
            mStarTask = new StarTask(buttonView, mStarListener, mSiteUrl, t.slug, t.id, isChecked);
            mStarTask.execute();
        }
    };


}
