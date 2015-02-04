package org.goodev.discourse.ui;

import android.os.Bundle;

import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;

public class ReadTopicsFragment extends TopicsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public CharSequence getTitle() {
        return getResources().getString(R.string.title_read);
    }

    @Override
    public String getInitUrl() {
        return mSiteUrl + Api.READ;
    }
}
