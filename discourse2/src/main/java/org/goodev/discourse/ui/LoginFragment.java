package org.goodev.discourse.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import org.goodev.discourse.api.data.UserDetails;
import org.goodev.discourse.utils.Utils;

public class LoginFragment extends Fragment {
    private LoginCallback mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (LoginCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + LoginCallback.class.getSimpleName());
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

    public interface LoginCallback {
        void onPreExecute();

        /**
         * 如果登陆失败，或者没有设置登陆信息导致登陆失败，返回值为null
         *
         * @param user
         */
        void onResult(UserDetails user);
    }

    private class CheckUrlTask extends AsyncTask<String, Void, UserDetails> {
        private final String mName;
        private final String mPassword;

        public CheckUrlTask(String name, String password) {
            mName = name;
            mPassword = password;
        }

        @Override
        protected UserDetails doInBackground(String... params) {
            if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mPassword)) {
                return null;
            }
            String site = params[0];
            return Utils.login(site, mName, mPassword);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mCallback != null) {
                mCallback.onPreExecute();
            }
        }

        @Override
        protected void onPostExecute(UserDetails result) {
            super.onPostExecute(result);
            if (mCallback != null) {
                mCallback.onResult(result);
            }
        }

    }

}
