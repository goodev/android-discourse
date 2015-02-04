package org.goodev.discourse.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.goodev.discourse.ActivityUtils;
import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.data.UserDetails;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.HttpRequest.HttpRequestException;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.SerializableSparseIntArray;
import org.goodev.discourse.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class UserFragment extends Fragment implements LoaderCallbacks<UserDetails>, OnClickListener {

    private static final int LOADER_ID = 1;
    private String mUsername;

    private ImageView mUserIcon;
    private TextView mNameTV;
    private TextView mUsernameTV;
    private TextView mBioRawTV;
    private TextView mWebsiteTV;
    private TextView mCreateAtTV;
    private TextView mLastPostTV;
    private TextView mLastSeenTV;
    private TextView mTrustLevelTV;
    private Button mAllBtn;
    private Button mTopicsBtn;
    private Button mPostsBtn;
    private Button mRepliesBtn;
    private Button mLikesGivenBtn;
    private Button mLikesReceivedBtn;
    private Button mEditsBtn;
    private Button mBookmarksBtn;
    private Button mLikesBtn;
    private View mMainContent;
    private boolean mCanSendPrivateMsg;
    private View mActivatedBtn;
    private int mActivatedId;
    private UserActionsListener mListener;
    private MenuItem mPrivateMsgMenu;

    public static UserFragment newInstance(String username) {
        UserFragment f = new UserFragment();
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_NAME, username);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = getArguments().getString(Utils.EXTRA_NAME);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mActivatedId = savedInstanceState.getInt(Utils.EXTRA_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_layout, container, false);
        mMainContent = v.findViewById(R.id.user_content);
        mUserIcon = (ImageView) v.findViewById(R.id.user_icon);
        mNameTV = (TextView) v.findViewById(R.id.name);
        mUsernameTV = (TextView) v.findViewById(R.id.user_name);
        mBioRawTV = (TextView) v.findViewById(R.id.bio_raw);
        mWebsiteTV = (TextView) v.findViewById(R.id.website);
        mCreateAtTV = (TextView) v.findViewById(R.id.create_at);
        mLastPostTV = (TextView) v.findViewById(R.id.last_post);
        mLastSeenTV = (TextView) v.findViewById(R.id.last_seen);
        mTrustLevelTV = (TextView) v.findViewById(R.id.trust_level);
        mAllBtn = (Button) v.findViewById(R.id.all_action);
        mAllBtn.setOnClickListener(this);
        mTopicsBtn = (Button) v.findViewById(R.id.topics_action);
        mTopicsBtn.setOnClickListener(this);
        mPostsBtn = (Button) v.findViewById(R.id.posts_action);
        mPostsBtn.setOnClickListener(this);
        mRepliesBtn = (Button) v.findViewById(R.id.replies_action);
        mRepliesBtn.setOnClickListener(this);
        mLikesGivenBtn = (Button) v.findViewById(R.id.likes_given_action);
        mLikesGivenBtn.setOnClickListener(this);
        mLikesReceivedBtn = (Button) v.findViewById(R.id.likes_received_action);
        mLikesReceivedBtn.setOnClickListener(this);
        mEditsBtn = (Button) v.findViewById(R.id.edits_action);
        mEditsBtn.setOnClickListener(this);
        mBookmarksBtn = (Button) v.findViewById(R.id.bookmarks_action);
        mBookmarksBtn.setOnClickListener(this);
        mLikesBtn = (Button) v.findViewById(R.id.likes_action);
        mLikesBtn.setOnClickListener(this);
        if (isYou()) {
            ViewStub vs = (ViewStub) v.findViewById(R.id.private_msg_stub);
            View msgLayout = vs.inflate();
            msgLayout.findViewById(R.id.all_private_msg).setOnClickListener(this);
            msgLayout.findViewById(R.id.unread_private_msg).setOnClickListener(this);
            msgLayout.findViewById(R.id.mine_private_msg).setOnClickListener(this);
        }

        if (mActivatedId == 0) {
            mActivatedBtn = mAllBtn;
            mActivatedBtn.setActivated(true);
        } else {
            View view = v.findViewById(mActivatedId);
            checkAndSetActivatedView(view);
        }
        return v;
    }

    private void checkAndSetActivatedView(View v) {
        if (v.getId() == mActivatedId) {
            v.setActivated(true);
            mActivatedBtn = v;
        }
    }

    public boolean isYou() {
        if (TextUtils.isEmpty(mUsername)) {
            return false;
        }
        return mUsername.equals(App.getUsername());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter
        // in the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_NAME, mUsername);
        getLoaderManager().initLoader(LOADER_ID, args, this);
        showActonBarProgress();
    }

    private void setupData(UserDetails data) {
        mMainContent.setVisibility(View.VISIBLE);
        hideActonBarProgress();
        if (data == null) {
            return;
        }
        mCanSendPrivateMsg = data.can_send_private_message_to_user;
        if (mPrivateMsgMenu != null) {
            mPrivateMsgMenu.setVisible(data.can_send_private_message_to_user);
        }
        L.i("UserDetails: " + data.name + "  " + data.created_at);
        int maxSize = Api.AVATAR_SIZE_BIG;
        String iconUrl = Utils.getAvatarUrl(data.avatar_template, maxSize);
        ImageLoader loader = App.getImageLoader(getActivity(), getResources());
        loader.get(iconUrl, mUserIcon);

        mNameTV.setText(data.name);
        mUsernameTV.setText(data.username);
        String bio = (data.bio_raw == null || data.bio_raw.equals(Api.NULL)) ? getString(R.string.empty_bio, data.name) : data.bio_raw;
        mBioRawTV.setText(bio);
        boolean noSite = TextUtils.isEmpty(data.website) || Utils.HTTP_PREFIX.equals(data.website) || data.website.equals(Api.NULL);
        int showSite = noSite ? View.GONE : View.VISIBLE;
        mWebsiteTV.setVisibility(showSite);
        mWebsiteTV.setText(getString(R.string.website, data.website));
        mCreateAtTV.setText(getString(R.string.create_at, Utils.formatPostTime(data.created_at)));
        int showLastPost = data.last_posted_at > 0 ? View.VISIBLE : View.GONE;
        mLastPostTV.setVisibility(showLastPost);
        mLastPostTV.setText(getString(R.string.last_post, Utils.formatPostTime(data.last_posted_at)));
        mLastSeenTV.setText(getString(R.string.last_seen, Utils.formatPostTime(data.last_seen_at)));
        mTrustLevelTV.setText(getString(R.string.trust_level, getTrustLevel(data.trust_level)));

        SerializableSparseIntArray actions = data.stats;
        int count = 0;

        int num = actions.get(UserActionsFragment.TYPE_TOPIC, 0);
        boolean hasTopics = num > 0;
        mTopicsBtn.setVisibility(hasTopics ? View.VISIBLE : View.GONE);
        mTopicsBtn.setText(getString(R.string.actions_topics, num));
        count += num;

        num = actions.get(UserActionsFragment.TYPE_POST, 0);
        mPostsBtn.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        mPostsBtn.setText(getString(R.string.actions_posts, num));
        count += num;

        num = actions.get(UserActionsFragment.TYPE_REPLY, 0);
        mRepliesBtn.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        mRepliesBtn.setText(getString(R.string.actions_replies, num));
        count += num;

        num = actions.get(UserActionsFragment.TYPE_LIKES_GIVEN, 0);
        mLikesGivenBtn.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        mLikesGivenBtn.setText(getString(R.string.actions_likes_given, num));
        count += num;

        num = actions.get(UserActionsFragment.TYPE_LIKES_RECEIVED, 0);
        mLikesReceivedBtn.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        mLikesReceivedBtn.setText(getString(R.string.actions_likes_received, num));
        count += num;

        num = actions.get(UserActionsFragment.TYPE_EDIT, 0);
        mEditsBtn.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        mEditsBtn.setText(getString(R.string.actions_edits, num));
        count += num;

        num = actions.get(UserActionsFragment.TYPE_BOOKMARK, 0);
        mBookmarksBtn.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        mBookmarksBtn.setText(getString(R.string.actions_bookmarks, num));
        count += num;

        num = actions.get(UserActionsFragment.TYPE_LIKES, 0);
        mLikesBtn.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        mLikesBtn.setText(getString(R.string.actions_likes, num));
        count += num;

        mAllBtn.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        mAllBtn.setText(getString(R.string.actions_all, count));
    }

    private String getTrustLevel(int trust_level) {
        String[] tl = getResources().getStringArray(R.array.trust_levels);
        if (trust_level >= 0 && trust_level < tl.length) {
            return tl[trust_level];
        }
        // 如果获取不到信息 则返回为2 正常用户
        return tl[2];
    }

    @Override
    public Loader<UserDetails> onCreateLoader(int id, Bundle args) {
        return new UserLoader(getActivity(), args.getString(Utils.EXTRA_NAME));
    }

    @Override
    public void onLoadFinished(Loader<UserDetails> loader, UserDetails data) {
        setupData(data);
    }

    @Override
    public void onLoaderReset(Loader<UserDetails> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedBtn != null) {
            outState.putInt(Utils.EXTRA_ID, mActivatedBtn.getId());
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {

            if (mActivatedBtn != null) {
                mActivatedBtn.setActivated(false);
            }
            mActivatedBtn = v;
            mActivatedBtn.setActivated(true);
        }
        switch (v.getId()) {
            case R.id.all_action:
                viewActions(UserActionsFragment.TYPE_ALL);
                break;
            case R.id.topics_action:
                viewActions(UserActionsFragment.TYPE_TOPIC);

                break;
            case R.id.posts_action:
                viewActions(UserActionsFragment.TYPE_POST);

                break;
            case R.id.replies_action:
                viewActions(UserActionsFragment.TYPE_REPLY);

                break;
            case R.id.likes_action:
                viewActions(UserActionsFragment.TYPE_LIKES);

                break;
            case R.id.likes_given_action:
                viewActions(UserActionsFragment.TYPE_LIKES_GIVEN);

                break;
            case R.id.likes_received_action:
                viewActions(UserActionsFragment.TYPE_LIKES_RECEIVED);

                break;
            case R.id.edits_action:
                viewActions(UserActionsFragment.TYPE_EDIT);

                break;
            case R.id.bookmarks_action:
                viewActions(UserActionsFragment.TYPE_BOOKMARK);
                break;
            case R.id.all_private_msg:
                viewActions(UserActionsFragment.TYPE_ALL_PRIVATE_MSG);
                break;
            case R.id.unread_private_msg:
                viewActions(UserActionsFragment.TYPE_UNREAD_PRIVATE_MSG);
                break;
            case R.id.mine_private_msg:
                viewActions(UserActionsFragment.TYPE_MIN_PRIVATE_MSG);
                break;

        }
    }

    private void viewActions(int type) {
        if (mListener != null) {
            mListener.onActionClicked(type);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof UserActionsListener) {
            mListener = (UserActionsListener) activity;
        }
    }

    private void showActonBarProgress() {
        Activity a = getActivity();
        if (a != null) {
            a.setProgressBarIndeterminateVisibility(Boolean.TRUE);
        }
    }

    private void hideActonBarProgress() {
        Activity a = getActivity();
        if (a != null) {
            a.setProgressBarIndeterminateVisibility(Boolean.FALSE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_user, menu);
        mPrivateMsgMenu = menu.findItem(R.id.action_private_msg);
        mPrivateMsgMenu.setVisible(mCanSendPrivateMsg);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_private_msg:
                ActivityUtils.openNewPrivateMsgEditorActivity(getActivity(), mUsername);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface UserActionsListener {
        void onActionClicked(int type);
    }

    private static class UserLoader extends AsyncTaskLoader<UserDetails> {
        private final String mUsername;
        private boolean mIsLoading;
        private boolean mHasError;

        public UserLoader(Context context, String username) {
            super(context);
            this.mUsername = username;
            mIsLoading = true;
            mHasError = false;
        }

        @Override
        public UserDetails loadInBackground() {
            try {
                mIsLoading = true;
                String url = App.getSiteUrl() + String.format(Api.GET_USER, mUsername);
                L.i(url);
                HttpRequest hr = HttpRequest.get(url);
                HttpURLConnection conn = hr.getConnection();
                if (App.isLogin()) {
                    App.getCookieManager().setCookies(conn);
                }
                String body = hr.body();
                L.i(body);
                JSONObject obj = new JSONObject(body);
                if (obj.has(Api.K_user)) {
                    JSONObject userObj = obj.getJSONObject(Api.K_user);
                    UserDetails user = Api.getUserDetails(userObj);
                    mHasError = false;
                    return user;
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

        @Override
        public void deliverResult(UserDetails data) {
            mIsLoading = false;
            super.deliverResult(data);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
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
        }

        public boolean isLoading() {
            return mIsLoading;
        }

        public boolean hasError() {
            return mHasError;
        }
    }
}
