package org.goodev.discourse.api;

import android.app.Activity;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.SparseArray;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

import org.goodev.discourse.utils.Utils;

import java.nio.charset.Charset;

public class MyErrorListener implements ErrorListener {
    public static final int STATUS_CODE_UNKNOWN = -1;

    protected final int mType;
    protected final String mUrl;
    private final SparseArray<ResultReceiver> mCallbacks;

    public MyErrorListener(int type, String url, SparseArray<ResultReceiver> callbacks) {
        mType = type;
        mUrl = url;
        mCallbacks = callbacks;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Bundle data = new Bundle();
        if (error == null) {
            data.putInt(Utils.EXTRA_STATUS_CODE, STATUS_CODE_UNKNOWN);
            onError(data);
            return;
        }
        if (error.networkResponse == null) {
            data.putInt(Utils.EXTRA_STATUS_CODE, STATUS_CODE_UNKNOWN);
            onError(data);
            return;
        }
        NetworkResponse response = error.networkResponse;
        if (response.data == null) {
            data.putInt(Utils.EXTRA_STATUS_CODE, response.statusCode);
            onError(data);
            return;
        }
        String msg = null;
        try {
            msg = new String(response.data, Charset.forName("UTF-8"));
        } catch (Exception e) {
            msg = new String(response.data, Charset.defaultCharset());
        }
        data.putInt(Utils.EXTRA_STATUS_CODE, response.statusCode);
        data.putString(Utils.EXTRA_MSG, msg);
        onError(data);
    }

    public void onError(Bundle data) {
        ResultReceiver rr = mCallbacks.get(mType);
        if (rr != null) {
            // for error type
            rr.send(Activity.RESULT_CANCELED, data);
            mCallbacks.delete(mType);
        }
    }

}
