package org.goodev.discourse.api;

import android.os.AsyncTask;

import org.goodev.discourse.App;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.HttpRequest.HttpRequestException;
import org.goodev.discourse.utils.L;

import java.io.IOException;
import java.net.HttpURLConnection;

public abstract class SearchTask<Result> extends AsyncTask<String, Void, Result> {

    @Override
    protected Result doInBackground(String... params) {

        try {
            String url = App.getSiteUrl() + params[0];
            L.i(url);
            HttpRequest hr = HttpRequest.get(url);
            HttpURLConnection conn = hr.getConnection();
            if (App.isLogin()) {
                App.getCookieManager().setCookies(conn);
            }
            String body = hr.body();
            L.i(body);
            return parse(body);
        } catch (HttpRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract Result parse(String body);

}
