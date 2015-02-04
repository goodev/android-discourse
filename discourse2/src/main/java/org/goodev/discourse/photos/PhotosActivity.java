package org.goodev.discourse.photos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;
import com.viewpagerindicator.CirclePageIndicator;

import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.utils.Tools;
import org.goodev.discourse.utils.Utils;

public class PhotosActivity extends FragmentActivity implements RefreshActionListener, AdListener {

    // public static final String ORIENTATION = "orientation";
    // public static final String LEFT = "org.goodev.beauty.left";
    // public static final String RIGHT = "org.goodev.beauty.right";
    // public static final String TOP = "org.goodev.beauty.top";
    // public static final String BOTTOM = "org.goodev.beauty.bottom";
    // public static final String WIDTH = "org.goodev.beauty.width";
    // public static final String HEIGHT = "org.goodev.beauty.height";

    ViewPager mViewPager;
    CirclePageIndicator mIndicator;
    CursorPagerAdapter<PhotoFragment> mAdapter;
    RefreshActionItem mRefreshActionItem;
    private int mAlbumId;
    private String[] mUrls;
    private View mInitPhotoLayout;
    private boolean mIsFirst;
    private boolean mRefreshFinished;
    /**
     * The interstitial ad.
     */
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        getWindow().setBackgroundDrawable(null);

        final Intent intent = getIntent();
        mAlbumId = intent.getIntExtra(Utils.EXTRA_NUMBER, 0);
        mUrls = intent.getStringArrayExtra(Utils.EXTRA_URL);

        mViewPager = (ViewPager) findViewById(R.id.content_pager);
        mAdapter = new CursorPagerAdapter<PhotoFragment>(getSupportFragmentManager(), PhotoFragment.class, mUrls);
        mViewPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator) findViewById(R.id.titles);
        mIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(mAlbumId);
        // Show the Up button in the action bar.
        setupActionBar();
        // setupInitPhotoLayout();

        if (Tools.isShowInterstitialAd(this)) {
            setupAds();
        }

    }

    private void setupInitPhotoLayout() {
        final View rootView = findViewById(R.id.init_photo_layout);
        mInitPhotoLayout = rootView;
        NetworkPhotoView photoView = (NetworkPhotoView) rootView.findViewById(R.id.photo);
        photoView.setImageUrl(mUrls[mAlbumId], App.getImageLoader());
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.photos, menu);
    // MenuItem item = menu.findItem(R.id.action_refresh);
    // if (mRefreshFinished) {
    // item.setVisible(false);
    // } else {
    // mRefreshActionItem = (RefreshActionItem) item.getActionView();
    // mRefreshActionItem.setMenuItem(item);
    // mRefreshActionItem.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
    // mRefreshActionItem.setRefreshActionListener(this);
    // mRefreshActionItem.showProgress(true);
    // }
    // return true;
    // }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

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
                finish();
                // NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onRefreshButtonClick(RefreshActionItem sender) {
        Toast.makeText(this, R.string.action_refresh, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDismissScreen(Ad arg0) {

    }

    @Override
    public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {

    }

    @Override
    public void onLeaveApplication(Ad arg0) {

    }

    @Override
    public void onPresentScreen(Ad arg0) {

    }

    @Override
    public void onReceiveAd(Ad arg0) {
        if (interstitialAd.isReady()) {
            interstitialAd.show();
            Tools.updateInterstitialAdTime(this);
        }
    }

    private void setupAds() {
        // Create an ad.
        interstitialAd = new InterstitialAd(this, "a151e7d92c31de5");

        // Set the AdListener.
        interstitialAd.setAdListener(this);
        // Load the interstitial ad. Check logcat output for the hashed
        // device ID
        // to get test ads on a physical device.
        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
        adRequest.addTestDevice("F324042096EAD64362E1164ACEA57782");
        interstitialAd.loadAd(adRequest);
    }
}
