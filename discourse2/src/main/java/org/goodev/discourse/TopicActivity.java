package org.goodev.discourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import org.goodev.discourse.ui.TopicFragment;
import org.goodev.discourse.utils.Utils;

public class TopicActivity extends FragmentActivity {
    private static final String FRAG_TAG = "topic_frag";
    TopicFragment mFragment;
    private String mSlug;
    private long mId;

    static private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Intent intent = getIntent();
        mSlug = intent.getStringExtra(Utils.EXTRA_SLUG);
        mId = intent.getLongExtra(Utils.EXTRA_ID, -1);

        setupActionBar();

        if (savedInstanceState == null) {
            mFragment = new TopicFragment();
            Bundle args = new Bundle();
            args.putString(Utils.EXTRA_SLUG, mSlug);
            args.putLong(Utils.EXTRA_ID, mId);
            mFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mFragment, FRAG_TAG).commit();
        } else {
            mFragment = (TopicFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        }

    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topic, menu);
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
                // finish();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
