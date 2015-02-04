package org.goodev.discourse.api;

import android.os.AsyncTask;

import org.goodev.discourse.App;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.L;

import java.net.HttpURLConnection;

public class PostActionTask extends AsyncTask<Void, Void, String> {
    public static final int TYPE_LIKE = 2;
    public static final String ID_PARAM = "id";
    public static final String TYPE_ID_PARAM = "post_action_type_id";
    protected String mSite;
    protected long mId;
    protected int mType;

    public PostActionTask(String site, long id, int type) {
        mSite = site;
        mId = id;
        mType = type;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            String url = mSite + Api.POST_ACTIONS;
            HttpRequest hr = HttpRequest.post(url);
            HttpURLConnection connection = hr.getConnection();
            if (App.isLogin()) {
                App.getCookieManager().setCookies(connection);
            }
            int code = hr.form(ID_PARAM, String.valueOf(mId)).form(TYPE_ID_PARAM, String.valueOf(mType)).code();
            L.i("%s star code %d ", url, code);
            String body = hr.body();
            L.i(body);
            return body;
        } catch (Exception e) {
            L.e(e);
            return null;
        }
    }

}
