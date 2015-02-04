package org.goodev.discourse.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.goodev.discourse.api.Api;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.Utils;
import org.json.JSONObject;

public class CheckUrlFragment extends Fragment {

    private CheckUrlCallback mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (CheckUrlCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CheckUrlCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        String url = getArguments().getString(Utils.EXTRA_URL);
        new CheckUrlTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }

    public interface CheckUrlCallback {
        void onPreExecute();

        void onResult(boolean ok);
    }

    private class CheckUrlTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String latestUrl = params[0] + Api.LATEST;
            try {
                String body = HttpRequest.get(latestUrl).acceptJson().body();
                JSONObject obj = new JSONObject(body);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mCallback != null) {
                mCallback.onPreExecute();
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (mCallback != null) {
                mCallback.onResult(result);
            }
        }

    }

}
