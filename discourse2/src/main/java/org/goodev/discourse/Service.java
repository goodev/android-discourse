package org.goodev.discourse;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.SparseArray;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.JsonObjectListener;
import org.goodev.discourse.api.MyErrorListener;
import org.goodev.discourse.utils.Utils;
import org.json.JSONObject;

public class Service extends IntentService {
    private final SparseArray<ResultReceiver> mCallbacks = new SparseArray<ResultReceiver>();

    public Service() {
        super("DiscourseService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final int type = intent.getIntExtra(Utils.EXTRA_TYPE, -1);
        if (type == -1) {
            return;
        }
        final String url = intent.getStringExtra(Utils.EXTRA_URL);
        ResultReceiver rr = intent.getParcelableExtra(Utils.EXTRA_CALLBACK);
        mCallbacks.put(type, rr);

        Listener<JSONObject> listener = new JsonObjectListener(type, url, mCallbacks);
        ErrorListener errorListener = new MyErrorListener(type, url, mCallbacks);
        String requestUrl = null;
        switch (type) {
            case Api.TYPE_LATEST:
                requestUrl = url + Api.LATEST;
                break;
            case Api.TYPE_CATEGORIES:
                requestUrl = url + Api.CATEGORIES;
                break;

        }

        JsonObjectRequest request = new JsonObjectRequest(requestUrl, null, listener, errorListener);
        App.getRequestQueue().add(request);

    }

    // private ErrorListener getCateogriesErrorListener(int type, String url) {
    // ErrorListener listener = new MyErrorListener(type, url, mCallbacks);
    // return listener;
    // }
    //
    // private Listener<JSONObject> getCateogriesListener(int type, String url) {
    // Listener<JSONObject> listener = new JsonObjectListener(type) {
    //
    // @Override
    // public void onResponse(JSONObject response) {
    // Api.parseCategories(response);
    // ResultReceiver rr = mCallbacks.get(mType);
    // if (rr != null) {
    // rr.send(Activity.RESULT_OK, null);
    // mCallbacks.delete(mType);
    // }
    // }
    //
    // };
    // return listener;
    // }
    //
    // private ErrorListener getLatestErrorListener(int type, String url) {
    // return new MyErrorListener(type, url, mCallbacks);
    // }
    //
    // private Listener<JSONObject> getLatestListener(int type, String url) {
    // Listener<JSONObject> listener = new JsonObjectListener(type) {
    //
    // @Override
    // public void onResponse(JSONObject response) {
    // Api.parseCategories(response);
    // ResultReceiver rr = mCallbacks.get(mType);
    // if (rr != null) {
    // rr.send(Activity.RESULT_OK, null);
    // mCallbacks.delete(mType);
    // }
    // }
    // };
    // return listener;
    // }
}
