package org.goodev.discourse.api;

import android.os.AsyncTask;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import org.apache.http.HttpStatus;
import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.L;

import java.net.HttpURLConnection;

public class StarTask extends AsyncTask<Void, Void, Boolean> {
    public static final String STARRED_PARAM = "starred";
    private final String mSite;
    private final String mSlug;
    private final long mId;
    private final boolean mStar;
    private final CompoundButton mButton;
    private final OnCheckedChangeListener mListener;

    public StarTask(CompoundButton btn, OnCheckedChangeListener listener, String site, String slug, long id, boolean star) {
        mSlug = slug;
        mSite = site;
        mId = id;
        mStar = star;
        mButton = btn;
        mListener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String url = mSite + String.format(Api.STAR, mSlug, mId);
            HttpRequest hr = HttpRequest.put(url);
            HttpURLConnection connection = hr.getConnection();
            if (App.isLogin()) {
                App.getCookieManager().setCookies(connection);
            }
            int code = hr.form(STARRED_PARAM, String.valueOf(mStar)).code();
            L.i("%s star code %d " + mStar, url, code);
            L.i(hr.body());
            return code == HttpStatus.SC_OK;
        } catch (Exception e) {
            L.e(e);
            return false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (!result.booleanValue()) {
            mButton.setOnCheckedChangeListener(null);
            mButton.setChecked(!mStar);
            mButton.setOnCheckedChangeListener(mListener);
            Toast.makeText(mButton.getContext(), mStar ? R.string.add_star_failed : R.string.remove_star_failed, Toast.LENGTH_LONG).show();
        }
    }

}
