package org.goodev.discourse;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.utils.BitmapLruCache;
import org.goodev.discourse.utils.CookieManager;
import org.goodev.discourse.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;

public class App extends Application {
    private static App sThis;
    private static RequestQueue sQueue;
    private static ImageLoader mImageLoader;
    private static int PLACEHOLDER_USER_IMAGE = 0;
    private static int PLACEHOLDER_POST_IMAGE = 1;
    private static int PLACEHOLDER_MEDIA_IMAGE = 2;
    private final HashMap<Long, Category> mCategories = new HashMap<Long, Category>();
    private String mCurrentSiteUrl = "";
    private String mUsername;
    private String mPassword;
    private String mXsrToken;
    private CookieManager mCookieManager;
    private boolean mIsLogin;
    private OkHttpClient mOkHttpClient;

    public static String getXsrToken() {
        if (sThis == null) {
            return null;
        }
        return sThis.mXsrToken;
    }

    public static void setXsrToken(String token) {
        if (sThis == null) {
            return;
        }
        sThis.mXsrToken = token;
    }

    public static void setUserInfo(String name, String password) {
        if (sThis == null) {
            return;
        }
        sThis.mUsername = name;
        sThis.mPassword = password;
    }

    public static String getUsername() {
        if (sThis == null) {
            return "";
        }
        return sThis.mUsername;
    }

    public static String getPassword() {
        if (sThis == null) {
            return null;
        }
        return sThis.mPassword;
    }

    public static String getSiteUrl() {
        if (sThis == null) {
            return null;
        }
        return sThis.mCurrentSiteUrl;
    }

    public static void setSiteUrl(String url) {
        if (sThis == null) {
            return;
        }
        sThis.mCurrentSiteUrl = url;
    }

    public synchronized static CookieManager getCookieManager() {
        if (sThis == null) {
            return new CookieManager();
        }
        if (sThis.mCookieManager == null) {
            sThis.mCookieManager = new CookieManager();
        }
        return sThis.mCookieManager;
    }

    public synchronized static boolean isLogin() {
        if (sThis == null) {
            return false;
        }
        return sThis.mIsLogin;
    }

    public synchronized static void setLogin(boolean login) {
        if (sThis == null) {
            return;
        }
        sThis.mIsLogin = login;
    }

    public synchronized static OkHttpClient getOkHttp() {
        if (sThis == null) {
            return new OkHttpClient();
        }
        if (sThis.mOkHttpClient == null) {
            sThis.mOkHttpClient = new OkHttpClient();
        }
        return sThis.mOkHttpClient;
    }

    public synchronized static RequestQueue getRequestQueue() {
        if (sQueue == null)
            sQueue = Volley.newRequestQueue(sThis);
        return sQueue;
    }

    public synchronized static ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            RequestQueue queue = getRequestQueue();
            ImageCache cache = new BitmapLruCache();
            mImageLoader = new ImageLoader(queue, cache);
        }
        return mImageLoader;
    }

    public static Context getContext() {
        return sThis;
    }

    public static org.goodev.discourse.utils.ImageLoader getImageLoader(FragmentActivity activity, Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int largestWidth = metrics.widthPixels > metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels;

        // Create list of placeholder drawables (this ImageLoader requires two different
        // placeholder images).
        ArrayList<Drawable> placeHolderDrawables = new ArrayList<Drawable>(3);
        placeHolderDrawables.add(PLACEHOLDER_USER_IMAGE, resources.getDrawable(org.goodev.discourse.R.drawable.ic_person));
        placeHolderDrawables.add(PLACEHOLDER_POST_IMAGE, resources.getDrawable(org.goodev.discourse.R.drawable.ic_logo));
        placeHolderDrawables.add(PLACEHOLDER_MEDIA_IMAGE, new ColorDrawable(resources.getColor(android.R.color.transparent)));

        // Create ImageLoader instance
        return new org.goodev.discourse.utils.ImageLoader(activity, placeHolderDrawables).setMaxImageSize(largestWidth);
    }

    public static Category getCategory(long cid) {
        if (sThis == null) {
            return null;
        }
        return sThis.mCategories.get(cid);
    }

    public static void setCategories(HashMap<Long, Category> categories) {
        if (sThis == null) {
            return;
        }
        sThis.mCategories.clear();
        sThis.mCategories.putAll(categories);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sThis = this;
        Tools.setupFirstRunTime(sThis);
    }
}
