package org.goodev.discourse.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.goodev.discourse.App;

public class PrefsUtils {

    private static final String NAME = "dis_pref";
    private static final String KEY_SITE = "key_site";

    private static final SharedPreferences getPref() {
        final Context ctx = App.getContext();
        SharedPreferences pref = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return pref;
    }

    public static final String getCurrentSiteUrl() {
        SharedPreferences pref = getPref();
        return pref.getString(KEY_SITE, null);

    }

    public static final void setCurrentSiteUrl(String url) {
        SharedPreferences pref = getPref();
        pref.edit().putString(KEY_SITE, url).commit();
    }
}
