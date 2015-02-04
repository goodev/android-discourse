package org.goodev.discourse.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.goodev.discourse.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Tools {
    public static final String CATEGORY_UPDATE_PREFIX = "cat_";
    public static final String PREFERENCES_NAME = "pref";
    public static final long HALF_DATY = 12 * 60 * 60 * 1000;
    public static final String DOWNLOAD_DIR_KEY = "download_dir";
    public static final String DOWNLOAD_KEY = "first_download";
    public static final String CATEGORY_MAKER = "update_done_";
    public static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 200; // 200MB
    public static final long FIFTY_DAYS = 1000 * 24 * 60 * 60 * 50;
    public static final long FIVE_DAYS = 1000 * 24 * 60 * 60 * 5;
    public static final long TEN_DAYS = 1000 * 24 * 60 * 60 * 10;
    // 06/21 10:48
    private final static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);
    private final static String sYear = Calendar.getInstance().get(Calendar.YEAR) + "/";
    // yyyy-MM-dd HH:mmZ 1969-12-31 16:00-0800
    // yyyy-MM-dd'T'HH:mm:ss.SSSZ
    // 1969-12-31T16:00:00.000-0800
    // 2013-08-30T11:25:50-04:00
    private final static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US);
    private final static SimpleDateFormat mDateFormatZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    private static final String FIRST_RUN_TIME = "first_run_time";
    private static final String AD_UPDATE_TIME = "ad_time";
    public static int yearDiff = 0;

    public static long convertDateString(String date) {
        if (TextUtils.isEmpty(date)) {
            return 0;
        }
        try {
            return mDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            try {
                return mDateFormatZ.parse(date).getTime();
            } catch (ParseException e1) {
                Utils.logi("convert date ", e1);
            }
        }
        return System.currentTimeMillis();
    }

    public static Date convertDate(String date) {
        try {
            // TODO 处理 跨年的问题
            // if (date.startsWith("12/")) {
            // yearDiff++;
            // }
            date = sYear + date;
            // date = (Calendar.getInstance().get(Calendar.YEAR) - yearDiff) +
            // "/" + date;
            Date d = mSimpleDateFormat.parse(date);
            return d;
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String convertDateToStr(long time) {
        return mSimpleDateFormat.format(new Date(time));
    }

    public static boolean needUpdateAlbum(Context ctx, int cat) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        long lastUpdateDate = pref.getLong(CATEGORY_UPDATE_PREFIX + cat, 0L);
        if (lastUpdateDate + HALF_DATY < System.currentTimeMillis()) {
            pref.edit().putLong(CATEGORY_UPDATE_PREFIX + cat, System.currentTimeMillis()).commit();
            return true;
        }
        return false;
    }

    public static String getDownloadFolder(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getString(DOWNLOAD_DIR_KEY, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
    }

    public static void setDownloadFolder(Context ctx, String path) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        pref.edit().putString(DOWNLOAD_DIR_KEY, path).commit();
    }

    public static void setDownloadRemember(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        pref.edit().putBoolean(DOWNLOAD_KEY, true).commit();
    }

    public static boolean isRememberDownload(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(DOWNLOAD_KEY, false);
    }

    public static void updateAlbumRequestMaker(Context ctx, int cat, boolean isDone) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        pref.edit().putBoolean(CATEGORY_MAKER + cat, isDone).commit();
    }

    public static boolean isAlbumRequestFinished(Context ctx, int cat) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(CATEGORY_MAKER + cat, false);
    }

    public static boolean isSdcardMounted() {
        String externalStorageState = Environment.getExternalStorageState();
        return externalStorageState.equals(Environment.MEDIA_MOUNTED);
    }

    public static File getPictureCacheDir(Context ctx) {
        File file = ctx.getExternalCacheDir();
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static final boolean dbExist(Context ctx, String dbName) {
        File path = ctx.getDatabasePath(dbName);
        return path.exists();
    }

    public static boolean copyFile(String file, String destDir) {
        try {
            InputStream is = new FileInputStream(new File(file));
            File dir = new File(destDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File destFile = new File(dir, System.currentTimeMillis() + ".jpeg");
            FileOutputStream fout = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int c;
            while ((c = is.read(buffer)) > 0) {
                fout.write(buffer, 0, c);
            }

            fout.flush();
            fout.close();
            is.close();
            return true;
        } catch (Exception e) {
            Log.e("Copy", "copy file ", e);
        }
        return false;
    }

    public static void updateInterstitialAdTime(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        pref.edit().putLong(AD_UPDATE_TIME, System.currentTimeMillis()).commit();
    }

    public static void setupFirstRunTime(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (!pref.contains(FIRST_RUN_TIME)) {
            pref.edit().putLong(FIRST_RUN_TIME, System.currentTimeMillis()).commit();
        }
    }

    public static long getFirstRunTime(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getLong(FIRST_RUN_TIME, System.currentTimeMillis());
    }

    public static boolean isShowInterstitialAd(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        long lastShowTime = pref.getLong(AD_UPDATE_TIME, System.currentTimeMillis());
        long first = getFirstRunTime(ctx);
        long current = System.currentTimeMillis();
        if (current - first > TEN_DAYS && current - lastShowTime > FIVE_DAYS) {
            return true;
        }
        return false;
    }

    public static boolean isShowBannerAd(Context ctx) {
        long first = getFirstRunTime(ctx);
        long current = System.currentTimeMillis();
        if (first - current > FIFTY_DAYS) {
            setupFirstRunTime(ctx);
        }
        if (current - first > TEN_DAYS) {
            return true;
        }
        return false;
    }

    public static final String writeBitmapToCacheDir(Bitmap bitmap) {
        if (Tools.isSdcardMounted()) {
            try {
                String file_path = App.getContext().getExternalCacheDir().getAbsolutePath() + "/share/";
                File dir = new File(file_path);
                if (!dir.exists())
                    dir.mkdirs();
                File file = new File(dir, "tmp.jpeg");
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 95, fOut);
                fOut.flush();
                fOut.close();
                return file.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
