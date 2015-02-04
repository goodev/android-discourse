package org.goodev.discourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import org.goodev.discourse.ui.CategoryTopicsFragment;
import org.goodev.discourse.ui.TopicsListFragment;
import org.goodev.discourse.utils.Utils;

public class CategoryActivity extends FragmentActivity {
    public static final String FRAGMENT_TAG = "frag_tag";
    private String mUrl;
    private String mTitle;
    private String mSlug;
    private long mCatId;
    private TopicsListFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        final Intent intent = getIntent();
        mUrl = intent.getStringExtra(Utils.EXTRA_URL);
        mTitle = intent.getStringExtra(Utils.EXTRA_TITLE);
        mSlug = intent.getStringExtra(Utils.EXTRA_SLUG);
        mCatId = intent.getLongExtra(Utils.EXTRA_ID, -1);
        setTitle(getString(R.string.title_activity_category, mTitle));
        setupActionBar();
        if (savedInstanceState == null) {
            mFragment = new CategoryTopicsFragment();
            Bundle args = new Bundle();
            args.putString(Utils.EXTRA_URL, mUrl);
            args.putString(Utils.EXTRA_TITLE, mTitle);
            args.putString(Utils.EXTRA_SLUG, mSlug);
            args.putLong(Utils.EXTRA_ID, mCatId);
            mFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mFragment, FRAGMENT_TAG).commit();
        } else {
            mFragment = (CategoryTopicsFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
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
        getMenuInflater().inflate(R.menu.category, menu);
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
                // finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
