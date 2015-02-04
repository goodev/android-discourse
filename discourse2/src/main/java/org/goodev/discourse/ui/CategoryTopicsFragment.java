package org.goodev.discourse.ui;

import android.os.Bundle;
import android.text.TextUtils;

import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.utils.Utils;

public class CategoryTopicsFragment extends TopicsListFragment {
    private String mSlug;
    private String mTitle;
    private long mCatId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arg = getArguments();
        if (arg != null) {
            mTitle = arg.getString(Utils.EXTRA_TITLE);
            mSiteUrl = arg.getString(Utils.EXTRA_URL);
            mSlug = arg.getString(Utils.EXTRA_SLUG);
            mCatId = arg.getLong(Utils.EXTRA_ID, -1);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public CharSequence getTitle() {
        return getResources().getString(R.string.title_category, mTitle);
    }

    @Override
    public String getInitUrl() {
        if (!TextUtils.isEmpty(mSlug)) {
            return String.format(mSiteUrl + Api.CATEGORIY_SLUG, mSlug);
        }
        if (mCatId == -1) {
            return mSiteUrl + Api.UNCATEGORY;
        }
        return String.format(mSiteUrl + Api.CATEGORIY_ID, mCatId);
    }
}
