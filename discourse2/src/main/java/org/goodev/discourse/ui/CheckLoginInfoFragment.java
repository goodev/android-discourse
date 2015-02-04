package org.goodev.discourse.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.goodev.discourse.utils.Utils;

public class CheckLoginInfoFragment extends Fragment {

    private CheckLoginInfoCallback mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (CheckLoginInfoCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + CheckLoginInfoCallback.class.getSimpleName());
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
        Bundle args = getArguments();
        String url = args.getString(Utils.EXTRA_URL);
        String password = args.getString(Utils.EXTRA_PASSWORD);
        String name = args.getString(Utils.EXTRA_NAME);
        new CheckUrlTask(name, password).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }

    public interface CheckLoginInfoCallback {
        void onPreCheckExecute();

        void onCheckResult(boolean ok);
    }

    private class CheckUrlTask extends AsyncTask<String, Void, Boolean> {
        private final String mName;
        private final String mPassword;

        public CheckUrlTask(String name, String password) {
            mName = name;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String site = params[0];
            return Utils.checkLoginInfo(site, mName, mPassword);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mCallback != null) {
                mCallback.onPreCheckExecute();
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (mCallback != null) {
                mCallback.onCheckResult(result);
            }
        }

    }

}
