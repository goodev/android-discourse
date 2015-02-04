package org.goodev.discourse.ui;

import android.os.Bundle;
import android.view.View;

import org.goodev.discourse.App;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.utils.Utils;

public class PrivateMsgFragment extends TopicsListFragment {

    public static PrivateMsgFragment newInstance(int type, String name) {
        PrivateMsgFragment f = new PrivateMsgFragment();
        Bundle args = new Bundle();
        args.putInt(Utils.EXTRA_TYPE, type);
        args.putString(Utils.EXTRA_NAME, name);
        args.putString(Utils.EXTRA_URL, App.getSiteUrl());
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public CharSequence getTitle() {
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundResource(android.R.color.background_light);
    }

    @Override
    public String getInitUrl() {
        int type = getArguments().getInt(Utils.EXTRA_TYPE);
        String name = getArguments().getString(Utils.EXTRA_NAME);
        String path = Api.MSG_URL;
        switch (type) {
            case UserActionsFragment.TYPE_ALL_PRIVATE_MSG:
                path = Api.MSG_URL;
                break;
            case UserActionsFragment.TYPE_MIN_PRIVATE_MSG:
                path = Api.MSG_MINE_URL;
                break;
            case UserActionsFragment.TYPE_UNREAD_PRIVATE_MSG:
                path = Api.MSG_UNREAD_URL;
                break;

        }
        return mSiteUrl + String.format(path, name);
    }
}
