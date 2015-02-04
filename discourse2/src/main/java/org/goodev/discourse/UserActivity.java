package org.goodev.discourse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.MenuItem;
import android.view.Window;

import org.goodev.discourse.ui.CreatedByTopicsFragment;
import org.goodev.discourse.ui.PrivateMsgFragment;
import org.goodev.discourse.ui.UserActionsFragment;
import org.goodev.discourse.ui.UserFragment;
import org.goodev.discourse.ui.UserFragment.UserActionsListener;
import org.goodev.discourse.utils.Utils;

public class UserActivity extends FragmentActivity implements UserActionsListener {

    SlidingPaneLayout mSlidingPane;
    private String mUsername;
    private int mCurrentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_user);
        mUsername = getIntent().getStringExtra(Utils.EXTRA_NAME);

        setupActionBar();
        initViews(savedInstanceState);
    }

    private void initViews(Bundle savedInstanceState) {
        mSlidingPane = (SlidingPaneLayout) findViewById(R.id.sliding_pane);
        mSlidingPane.setShadowResource(R.drawable.drawer_shadow);
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().add(R.id.pane1, UserFragment.newInstance(mUsername), "pane1").commit();
            getSupportFragmentManager().beginTransaction().add(R.id.pane2, UserActionsFragment.newInstance(mUsername, UserActionsFragment.TYPE_ALL), "pane1").commit();
        }
        mSlidingPane.openPane();
    }

    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);
        CharSequence title = getString(R.string.title_activity_user, mUsername);
        getActionBar().setTitle(title);
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
                // finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActionClicked(int type) {
        if (type == mCurrentType) {
            mSlidingPane.closePane();
            return;
        }
        mCurrentType = type;
        Fragment f = null;
        if (type == UserActionsFragment.TYPE_ALL_PRIVATE_MSG || type == UserActionsFragment.TYPE_MIN_PRIVATE_MSG || type == UserActionsFragment.TYPE_UNREAD_PRIVATE_MSG) {
            f = PrivateMsgFragment.newInstance(type, mUsername);
        } else if (type == UserActionsFragment.TYPE_TOPIC) {
            f = CreatedByTopicsFragment.newInstance(mUsername);
        } else {
            f = UserActionsFragment.newInstance(mUsername, type);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.pane2, f, "pane1").commit();
        mSlidingPane.closePane();
    }

    @Override
    public void onBackPressed() {
        if (!mSlidingPane.openPane()) {
            super.onBackPressed();
        }
    }

}
