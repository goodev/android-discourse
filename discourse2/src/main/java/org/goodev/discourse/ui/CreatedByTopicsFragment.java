package org.goodev.discourse.ui;

import android.os.Bundle;

import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.utils.Utils;

public class CreatedByTopicsFragment extends TopicsListFragment {

    public static CreatedByTopicsFragment newInstance(String username) {
        CreatedByTopicsFragment f = new CreatedByTopicsFragment();
        Bundle args = new Bundle();
        args.putString(Utils.EXTRA_NAME, username);
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
        return getResources().getString(R.string.title_created_by, getArguments().getString(Utils.EXTRA_NAME));
    }

    @Override
    public String getInitUrl() {
        String name = getArguments().getString(Utils.EXTRA_NAME);
        return mSiteUrl + String.format(Api.CREATED_BY, name);
    }
}
