package org.goodev.discourse.api;

import android.os.AsyncTask;

import org.goodev.discourse.App;
import org.goodev.discourse.api.data.UserDetails;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.HttpRequest.HttpRequestException;
import org.goodev.discourse.utils.L;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class GetUserInfoTask extends AsyncTask<Void, Void, UserDetails> {

    private final String mSite;
    private final String mName;

    public GetUserInfoTask(String site, String name) {
        mSite = site;
        mName = name;
    }

    @Override
    protected UserDetails doInBackground(Void... params) {
        try {
            String url = mSite + String.format(Api.GET_USER, mName);
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
                return user;
            }
        } catch (HttpRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
