package org.goodev.discourse.api;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpStatus;
import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.ui.TopicFragment;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.L;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

public class TopicNotificationTask extends AsyncTask<Void, Void, Boolean> {
    public static final String NOTIF_PARAM = "notification_level";
    private final WeakReference<TopicFragment> mFragment;
    private final Spinner mSpinner;
    private final OnItemSelectedListener mListener;
    private final String mSite;
    private final long mId;
    private final int mLevel;
    private final int mOld;
    private final TextView mStatus;

    public TopicNotificationTask(TopicFragment frag, int level) {
        mFragment = new WeakReference<TopicFragment>(frag);
        mSite = frag.getSite();
        mSpinner = frag.getNotiSpinner();
        mListener = frag.getListener();
        mId = frag.getTopicId();
        mOld = frag.getOldNotiLevel();
        mLevel = level;
        mStatus = frag.getNotiDes();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mStatus.setText(R.string.update_notification_level);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String url = mSite + String.format(Api.NOTIFICATIONS, mId);
            HttpRequest hr = HttpRequest.post(url);
            HttpURLConnection connection = hr.getConnection();
            if (App.isLogin()) {
                App.getCookieManager().setCookies(connection);
            }
            int code = hr.form(NOTIF_PARAM, String.valueOf(mLevel)).code();
            L.i("%s star code %d " + mLevel, url, code);
            String body = hr.body();
            L.i(body);
            JSONObject result = new JSONObject(body);
            String ok = result.getString("success");
            return code == HttpStatus.SC_OK && "OK".equalsIgnoreCase(ok);
        } catch (Exception e) {
            L.e(e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (!result.booleanValue()) {
            mSpinner.setOnItemSelectedListener(null);
            mSpinner.setSelection(mOld);
            mSpinner.setOnItemSelectedListener(mListener);
            Toast.makeText(mSpinner.getContext(), R.string.change_notifi_level_failed, Toast.LENGTH_LONG).show();
            setNotificationDesView(mSpinner.getContext().getResources(), mOld);
        } else {
            TopicFragment f = mFragment.get();
            if (f != null) {
                f.setCurrentNotifLevel(mLevel);
            }
            setNotificationDesView(mSpinner.getContext().getResources(), mLevel);
        }
    }

    protected void setNotificationDesView(Resources res, int level) {
        String[] mNotificationDes = res.getStringArray(R.array.notifications_des);
        if (level >= mNotificationDes.length) {
            level = 0;
        }
        mStatus.setText(mNotificationDes[level]);
    }
}
