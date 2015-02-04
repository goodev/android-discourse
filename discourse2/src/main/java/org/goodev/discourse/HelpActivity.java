package org.goodev.discourse;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;

import org.goodev.discourse.utils.Utils;

public class HelpActivity extends Activity {
    private static final String BASE_URL = "file:///android_asset/html-en/";

    private WebView webView;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_help);

        webView = (WebView) findViewById(R.id.help_contents);

        String page = getIntent().getStringExtra(Utils.EXTRA_URL);
        if (TextUtils.isEmpty(page)) {
            page = "faq.html";
        }
        if (page.equals("login.html")) {
            setTitle(R.string.title_get_password);
        }

        if (icicle == null) {
            webView.loadUrl(BASE_URL + page);
        } else {
            webView.restoreState(icicle);
        }
        setupActionBar();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        String url = webView.getUrl();
        if (url != null && !url.isEmpty()) {
            webView.saveState(state);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
