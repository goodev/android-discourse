package org.goodev.discourse.api.data;

import android.text.TextUtils;

import org.goodev.discourse.api.Api;

import java.io.Serializable;

public class Links implements Serializable {
    public String url;
    public String title;
    public String fancy_title;
    public long clicks;
    public boolean internal;
    public boolean reflection;
    public Long user_id;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(title)) {
            sb.append(url);
        } else {
            sb.append(title);
        }
        sb.append(" ");
        sb.append(clicks);
        return sb.toString();
    }

    public CharSequence getTitle() {
        if (TextUtils.isEmpty(title) || Api.NULL.equals(title)) {
            return url;
        }
        return title;
    }
}
