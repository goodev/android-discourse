package org.goodev.discourse;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.SearchTask;
import org.goodev.discourse.api.data.SearchTopic;
import org.goodev.discourse.api.data.SearchUser;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends FragmentActivity implements OnQueryTextListener, OnCheckedChangeListener, OnItemClickListener {

    public static final String KEY_SEARCH_QUERY = "query_text";
    private ListView mListView;
    private RadioButton mTopicsBtn;
    private RadioButton mUsersBtn;
    private RadioGroup mRadioGroup;
    private TopicsAdapter mTopicsAdapter;
    private UsersAdapter mUsersAdapter;

    private String mQuery;
    private String mPreUserQuery;
    private String mPreTopicQuery;

    private View mEmptyView;
    private View mLoadingView;

    private LayoutInflater mLayoutInflater;
    private SearchTopicsTask mTopicsTask;
    private SearchUsersTask mUsersTask;
    private ArrayList<SearchUser> mUsers = new ArrayList<SearchUser>();
    private ArrayList<SearchTopic> mTopics = new ArrayList<SearchTopic>();
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_search);
        mImageLoader = App.getImageLoader(this, getResources());
        mLayoutInflater = LayoutInflater.from(this);
        if (savedInstanceState != null) {
            mQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, null);
        }
        // Show the Up button in the action bar.
        setupActionBar();
        setupViews();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setupViews() {

        mLoadingView = findViewById(R.id.loading_layout);
        mUsersAdapter = new UsersAdapter();
        mTopicsAdapter = new TopicsAdapter();
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        mListView = (ListView) findViewById(android.R.id.list);
        mEmptyView = findViewById(android.R.id.empty);
        mListView.setEmptyView(mEmptyView);
        mListView.setAdapter(mTopicsAdapter);
        mListView.setOnItemClickListener(this);

        mUsersBtn = (RadioButton) findViewById(R.id.users_btn);
        mTopicsBtn = (RadioButton) findViewById(R.id.topics_btn);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        if (mQuery != null) {
            searchView.setQuery(mQuery, false);
        }
        // Associate searchable configuration with the SearchView
        // SearchManager searchManager =
        // (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // searchView.setSearchableInfo(
        // searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SEARCH_QUERY, mQuery);
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
            case R.id.action_search:
                L.i();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        L.i(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        L.i(query);
        mQuery = query;
        startSearch();
        return false;
    }

    /**
     * TODO 缓存搜索结果，当mQuery 没有变化的时候 不再重新请求数据。。。。
     */
    private void startSearch() {
        if (mUsersBtn.isChecked()) {
            if (mQuery != null && mQuery.equals(mPreUserQuery) && mUsers.size() > 0) {
                mListView.setAdapter(mUsersAdapter);
                return;
            }
            searchStart();
            mPreUserQuery = null;
            mUsers.clear();
            mListView.setAdapter(mUsersAdapter);
            searchUsers(mQuery);
        } else if (mTopicsBtn.isChecked()) {
            if (mQuery != null && mQuery.equals(mPreTopicQuery) && mTopics.size() > 0) {
                mListView.setAdapter(mTopicsAdapter);
                return;
            }
            searchStart();
            mPreTopicQuery = null;
            mTopics.clear();
            mListView.setAdapter(mTopicsAdapter);
            searchTopics(mQuery);
        }
    }

    private void searchTopics(String query) {
        if (mTopicsTask != null) {
            mTopicsTask.cancel(true);
        }
        mTopicsTask = new SearchTopicsTask();
        mTopicsTask.execute(String.format(Api.SEARCH_TOPIC, query));
    }

    private void searchUsers(String query) {
        if (mUsersTask != null) {
            mUsersTask.cancel(true);
        }
        mUsersTask = new SearchUsersTask();
        mUsersTask.execute(String.format(Api.SEARCH_USER, query));

    }

    void searchStart() {
        mListView.setEmptyView(mLoadingView);
        mEmptyView.setVisibility(View.INVISIBLE);
        //		mListView.setVisibility(View.INVISIBLE);
        //		mLoadingView.setVisibility(View.VISIBLE);
    }

    void searchFinished() {
        mListView.setEmptyView(mEmptyView);
        mLoadingView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (TextUtils.isEmpty(mQuery)) {
            return;
        }

        startSearch();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
        if (item instanceof SearchUser) {
            openUserActivity((SearchUser) item);
        } else if (item instanceof SearchTopic) {
            openTopicActivity((SearchTopic) item);
        }
    }

    private void openTopicActivity(SearchTopic item) {
        if (item.url != null && item.url.startsWith("/t")) {
            String[] paths = item.url.split("/");
            ActivityUtils.startTopicActivity(this, paths[2], paths[3]);
        } else {
            L.e("open search topics error:%s ", item.url);
        }
    }

    private void openUserActivity(SearchUser item) {
        ActivityUtils.openUserActivity(this, item.id);
    }

    class UsersAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mUsers.size();
        }

        @Override
        public Object getItem(int position) {
            return mUsers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.search_user_item, parent, false);
            }
            ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            SearchUser user = (SearchUser) getItem(position);
            name.setText(user.title);
            mImageLoader.get(Utils.getAvatarUrl(user.avatar_template, Api.AVATAR_SIZE_BIG), iv);
            return convertView;
        }

        public void setData(ArrayList<SearchUser> result) {
            mUsers.clear();
            mUsers.addAll(result);
            notifyDataSetChanged();
        }

    }

    class TopicsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTopics.size();
        }

        @Override
        public Object getItem(int position) {
            return mTopics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v;
            if (convertView == null) {
                v = (TextView) mLayoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                v = (TextView) convertView;
            }
            SearchTopic topic = (SearchTopic) getItem(position);
            v.setText(topic.title);
            return v;
        }

        public void setData(ArrayList<SearchTopic> result) {
            mTopics.clear();
            mTopics.addAll(result);
            notifyDataSetChanged();
        }

    }

    class SearchTopicsTask extends SearchTask<ArrayList<SearchTopic>> {

        @Override
        public ArrayList<SearchTopic> parse(String body) {
            ArrayList<SearchTopic> topics = new ArrayList<SearchTopic>();
            try {
                JSONArray array = new JSONArray(body);
                if (array.length() == 0) {
                    return null;
                }
                JSONObject obj = array.getJSONObject(0);
                if (Api.TYPE_TOPIC.equals(obj.getString(Api.TYPE))) {
                    JSONArray results = obj.getJSONArray(Api.RESULTS);
                    int length = results.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject o = results.getJSONObject(i);
                        SearchTopic topic = Api.getJSONObject(o, SearchTopic.class);
                        topics.add(topic);
                    }
                    return topics;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected void onPostExecute(ArrayList<SearchTopic> result) {
            super.onPostExecute(result);
            setProgressBarIndeterminateVisibility(false);
            searchFinished();
            if (mTopicsTask != this) {
                return;
            }
            if (result != null) {
                mTopicsAdapter.setData(result);
            }
            mPreTopicQuery = mQuery;
            mListView.setAdapter(mTopicsAdapter);
        }

    }

    class SearchUsersTask extends SearchTask<ArrayList<SearchUser>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected void onPostExecute(ArrayList<SearchUser> result) {
            super.onPostExecute(result);
            setProgressBarIndeterminateVisibility(false);
            searchFinished();
            if (mUsersTask != this) {
                return;
            }
            if (result != null) {
                mUsersAdapter.setData(result);
            }
            mPreUserQuery = mQuery;
            mListView.setAdapter(mUsersAdapter);
        }

        @Override
        public ArrayList<SearchUser> parse(String body) {
            ArrayList<SearchUser> users = new ArrayList<SearchUser>();
            try {
                JSONArray array = new JSONArray(body);
                if (array.length() == 0) {
                    return null;
                }
                JSONObject obj = array.getJSONObject(0);
                if (Api.TYPE_USER.equals(obj.getString(Api.TYPE))) {
                    JSONArray results = obj.getJSONArray(Api.RESULTS);
                    int length = results.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject o = results.getJSONObject(i);
                        SearchUser user = Api.getJSONObject(o, SearchUser.class);
                        users.add(user);
                    }
                    return users;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
