package org.goodev.discourse.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.goodev.discourse.ActivityUtils;
import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.data.UserActions;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.HttpRequest.HttpRequestException;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class UserActionsFragment extends ListFragment implements LoaderCallbacks<List<UserActions>>, OnScrollListener {
    // 1:"Likes Given",2:"Likes Received",
    // 3:"Bookmarks",4:"Topics",5:"Posts",6:"Replies",
    // 7:"Mentions",9:"Quotes",
    // 10:"Favorites",11:"Edits",
    // 12:"Sent Items",13:"Inbox"}

    public static final int TYPE_ALL = 0;
    public static final int TYPE_LIKES_GIVEN = 1;
    public static final int TYPE_LIKES_RECEIVED = 2;
    public static final int TYPE_BOOKMARK = 3;
    public static final int TYPE_TOPIC = 4;
    public static final int TYPE_POST = 5;
    public static final int TYPE_REPLY = 6;
    /**
     * 喜爱
     */
    public static final int TYPE_LIKES = 10;
    public static final int TYPE_EDIT = 11;
    /**
     * Mentions 提到我的
     */
    public static final int TYPE_MENTIONS = 7;
    /**
     * Quotes
     */
    public static final int TYPE_QUOTES = 9;

    public static final int TYPE_SENT_ITEMSS = 12;
    public static final int TYPE_INBOX = 13;
    public static final int TYPE_ALL_PRIVATE_MSG = 20;
    public static final int TYPE_MIN_PRIVATE_MSG = 21;
    public static final int TYPE_UNREAD_PRIVATE_MSG = 22;

    public static final String REPLY_FILTER = "6,7,9";
    public static final String EXTRA_ADD_VERTICAL_MARGINS = "extra.ADD_VERTICAL_MARGINS";
    private static final int LOADER_ID = 11;
    private static final String STATE_POSITION = "position";
    private static final String STATE_TOP = "top";
    private final ActionsAdapter mAdapter = new ActionsAdapter();
    private String mUsername;
    private int mType;
    private List<UserActions> mActions = new ArrayList<UserActions>();
    private int mListViewStatePosition;
    private int mListViewStateTop;
    private ImageLoader mImageLoader;

    public static UserActionsFragment newInstance(String username, int type) {
        UserActionsFragment f = new UserActionsFragment();
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_NAME, username);
        args.putInt(Utils.EXTRA_TYPE, type);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mImageLoader = App.getImageLoader(getActivity(), getResources());
        mUsername = args.getString(Utils.EXTRA_NAME);
        mType = args.getInt(Utils.EXTRA_TYPE);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView listView = getListView();
        // TODO fix this
        // if (!UIUtils.isTablet(getActivity())) {
        // view.setBackgroundColor(getResources().getColor(R.color.plus_stream_spacer_color));
        // }

        // if (getArguments() != null
        // && getArguments().getBoolean(EXTRA_ADD_VERTICAL_MARGINS, false)) {
        // int verticalMargin = getResources().getDimensionPixelSize(
        // R.dimen.plus_stream_padding_vertical);
        // if (verticalMargin > 0) {
        // listView.setClipToPadding(false);
        // listView.setPadding(0, verticalMargin, 0, verticalMargin);
        // }
        // }

        listView.setOnScrollListener(this);
        listView.setDrawSelectorOnTop(true);
        // listView.setDivider(getResources().getDrawable(android.R.color.darker_gray));
        // listView.setDividerHeight(getResources()
        // .getDimensionPixelSize(R.dimen.divider_height));

        TypedValue v = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.clickableItemBackground, v, true);
        listView.setSelector(v.resourceId);

        setListAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter
        // in the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<UserActions>> onCreateLoader(int id, Bundle args) {
        return new ActionsLoader(getActivity(), mUsername, mType);
    }

    @Override
    public void onLoadFinished(Loader<List<UserActions>> loader, List<UserActions> data) {
        L.i("data: " + (data == null ? null : data.size()));
        if (data != null) {
            mActions = data;
        }
        mAdapter.notifyDataSetChanged();
        if (mListViewStatePosition != -1 && isAdded()) {
            getListView().setSelectionFromTop(mListViewStatePosition, mListViewStateTop);
            mListViewStatePosition = -1;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<UserActions>> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (isAdded()) {
            View v = getListView().getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            outState.putInt(STATE_POSITION, getListView().getFirstVisiblePosition());
            outState.putInt(STATE_TOP, top);
        }
        super.onSaveInstanceState(outState);
    }

    private boolean isStreamLoading() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(LOADER_ID);
            if (loader != null) {
                return ((ActionsLoader) loader).isLoading();
            }
        }
        return true;
    }

    private boolean streamHasMoreResults() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(LOADER_ID);
            if (loader != null) {
                return ((ActionsLoader) loader).hasMoreResults();
            }
        }
        return false;
    }

    private boolean streamHasError() {
        if (isAdded()) {
            final Loader loader = getLoaderManager().getLoader(LOADER_ID);
            if (loader != null) {
                return ((ActionsLoader) loader).hasError();
            }
        }
        return false;
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

    public void loadMoreResults() {
        if (isAdded()) {
            Loader loader = getLoaderManager().getLoader(LOADER_ID);
            if (loader != null) {
                loader.forceLoad();
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        UserActions action = (UserActions) l.getItemAtPosition(position - l.getHeaderViewsCount());
        if (action == null) {
            return;
        }
        String slug = action.slug;
        if (TextUtils.isEmpty(slug)) {
            slug = "topic";
        }
        ActivityUtils.startTopicActivity(getActivity(), slug, action.topic_id);
    }

    private static class ActionsLoader extends AsyncTaskLoader<List<UserActions>> {
        public static final int NO_MORE_DATA = -1;
        List<UserActions> mActions;
        /**
         * 如果offset为-1 则说明没有下一页了
         */
        private long mOffset;
        private int mType;
        private String mUsername;
        private boolean mIsLoading;
        private boolean mHasError;

        public ActionsLoader(Context context, String name, int type) {
            super(context);
            init(name, type);
        }

        private void init(String name, int type) {
            mActions = null;
            mIsLoading = true;
            mHasError = false;
            mOffset = 0;
            mType = type;
            mUsername = name;
        }

        @Override
        public List<UserActions> loadInBackground() {
            try {
                mIsLoading = true;
                String url = getUrl();
                L.i(url);
                HttpRequest hr = HttpRequest.get(url);
                HttpURLConnection conn = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(conn);
                }
                String body = hr.body();
                L.i("%s", body);
                JSONObject obj = new JSONObject(body);
                if (obj.has(Api.K_user_actions)) {
                    mHasError = false;
                    JSONArray data = obj.getJSONArray(Api.K_user_actions);
                    if (data.length() == 0) {
                        mOffset = NO_MORE_DATA;
                        return null;
                    }
                    List<UserActions> result = Api.getUserActions(data);
                    mOffset += result.size();
                    return result;
                }
            } catch (HttpRequestException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mHasError = true;
            return null;
        }

        private String getUrl() {
            if (mType == TYPE_ALL) {
                return App.getSiteUrl() + String.format(Api.GET_USER_ACTIONS, mOffset, mUsername);
            } else if (mType == TYPE_REPLY) {
                return App.getSiteUrl() + String.format(Api.GET_USER_ACTIONS_FILTER, mOffset, mUsername, REPLY_FILTER);
            }
            return App.getSiteUrl() + String.format(Api.GET_USER_ACTIONS_FILTER, mOffset, mUsername, mType + "");
        }

        @Override
        public void deliverResult(List<UserActions> activities) {
            mIsLoading = false;
            if (activities != null) {
                if (mActions == null) {
                    mActions = activities;
                } else {
                    mActions.addAll(activities);
                }
            }
            if (isStarted()) {
                // Need to return new ArrayList for some reason or onLoadFinished() is not called
                super.deliverResult(mActions == null ? null : new ArrayList<UserActions>(mActions));
            }
        }

        @Override
        protected void onStartLoading() {
            if (mActions != null) {
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
            mActions = null;
        }

        public boolean isLoading() {
            return mIsLoading;
        }

        public boolean hasMoreResults() {
            return mOffset != NO_MORE_DATA;
        }

        public boolean hasError() {
            return mHasError;
        }

        public void refresh() {
            reset();
            startLoading();
        }
    }

    private class ActionsAdapter extends BaseAdapter {
        private static final int VIEW_TYPE_ACTIVITY = 0;
        private static final int VIEW_TYPE_LOADING = 1;

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
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
            return mActions.size() + (
                    // show the status list row if...
                    ((isStreamLoading() && mActions.size() == 0) // ...this is the first load
                            || streamHasMoreResults() // ...or there's another page
                            || streamHasError()) // ...or there's an error
                            ? 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
            return (position >= mActions.size()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ACTIVITY;
        }

        @Override
        public Object getItem(int position) {
            return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? mActions.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            // TODO: better unique ID heuristic
            return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? mActions.get(position).hashCode() : -1;
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
                UserActions activity = (UserActions) getItem(position);
                if (convertView == null) {
                    convertView = getLayoutInflater(null).inflate(R.layout.list_item_user_action, parent, false);
                }

                UserActionViewBinder.bindActivityView(convertView, activity, mImageLoader, false);
                return convertView;
            }
        }
    }

}
