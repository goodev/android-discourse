package org.goodev.discourse.api;

import android.app.Activity;
import android.os.ResultReceiver;
import android.util.SparseArray;

import com.android.volley.Response.Listener;

import org.json.JSONObject;

public class JsonObjectListener implements Listener<JSONObject> {

    protected final int mType;
    private final SparseArray<ResultReceiver> mCallbacks;
    protected String mUrl;

    public JsonObjectListener(int type, String url, SparseArray<ResultReceiver> callbacks) {
        mType = type;
        mUrl = url;
        mCallbacks = callbacks;
    }

    @Override
    public void onResponse(JSONObject response) {
        switch (mType) {
            case Api.TYPE_LATEST:
                Api.parseLatestTopics(response);
                break;
            case Api.TYPE_CATEGORIES:
                Api.parseCategories(response);
                break;

        }
        ResultReceiver rr = mCallbacks.get(mType);
        if (rr != null) {
            rr.send(Activity.RESULT_OK, null);
            mCallbacks.delete(mType);
        }
    }

}
