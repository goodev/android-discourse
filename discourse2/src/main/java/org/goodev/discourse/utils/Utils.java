package org.goodev.discourse.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import org.goodev.discourse.App;
import org.goodev.discourse.BuildConfig;
import org.goodev.discourse.R;
import org.goodev.discourse.Service;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.api.data.UserDetails;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Utils {
    // ---- start 处理App内部链接跳转的常量
    public static final String USERS = "users";
    public static final String T = "t";
    public static final String COLOR_PREFIX = "#";

    // ---- end 处理App内部链接跳转的常量
    public static final String AVATAR_HTTP_PREFIX = "http:";
    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";
    public static final String SLASH2 = "//";
    public static final String SLASH = "/";
    public static final String JSON = ".json";
    public static final String TRACK_VISIT = "?track_visit=true";
    public static final String TOPIC = "t";
    public static final String POSTS = "posts.json";
    public static final String POSTS_ID = "post_ids[]";
    public static final String EQ = "=";
    public static final String MARK = "?";
    public static final String AND = "&";
    public static final int PAGE_COUNT = 20;
    public static final String EXTRA_SLUG = "extra_slug";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_MSG = "extra_msg";
    public static final String EXTRA_NUMBER = "extra_number";
    public static final String EXTRA_CAT_INDEX = "extra_cat_index";
    public static final String EXTRA_STATUS_CODE = "extra_code";
    public static final String EXTRA_CALLBACK = "extra_callback";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_IS_EDIT_POST = "extra_edit_post";
    public static final String EXTRA_IS_PRIVATE_MSG = "extra_private_msg";
    public static final String EXTRA_OBJ = "extra_obj";
    public static final String EXTRA_LINKS = "extra_links";
    public static final String EXTRA_OBJ_C = "extra_objc";
    public static final String EXTRA_PASSWORD = "extra_password";
    public static final int ANIMATION_FADE_IN_TIME = 250;
    /**
     * 30天的时间
     */
    public static final long DAY_30 = 30 * 24 * 60 * 60 * 1000L;
    public static final String GRAVATAR_PREFIX = "//www.gravatar.com";
    public static final String GRAVATAR_HTTP_PREFIX = "http:";
    public static final String GRAVATAR_SIZE = "{size}";
    public static final String TEXT = "<p>I know I'd like to visit various new Discourse servers once <blockquote>in awhile</blockquote> as they come up. As you'd like a visitor or two, please post your URL here and a description of the purpose of your site and any other notes...</p> <p>So far, the public Discourse servers I know about are:</p> <ul> <li> <a href=\"http://meta.discourse.org\">meta.discourse.org</a> [This one]</li> <li> <a href=\"http://try.discourse.org\">try.discourse.org</a> [daily-reset sandbox]</li> <li> <a href=\"http://discourse.jcsims.me/\" rel=\"nofollow\">discourse.jcsims.me</a> Engineering and Computer Science Interest Group - University of Rhode Island by <a href=\"/users/jcsims\" class=\"mention\">@jcsims</a> </li> <li> <a href=\"http://bitcoindiscourse.com\" rel=\"nofollow\">bitcoindiscourse.com</a> about BitCoins by <a href=\"/users/ian_purton\" class=\"mention\">@ian_purton</a> </li> <li> <a href=\"http://forums.ctrlcmdesc.com/\" rel=\"nofollow\">forums.ctrlcmdesc.com/</a> Control Command Escape (Mac Games) by <a href=\"/users/ninjafoodstuff\" class=\"mention\">@NinjaFoodstuff</a> </li> <li> <a href=\"http://answers.joomlart.com/\" rel=\"nofollow\">answers.joomlart.com/</a> by <a href=\"/users/hung_dinh\" class=\"mention\">@Hung_Dinh</a> </li> <li> <a href=\"http://forum.greenheartgames.com/\" rel=\"nofollow\">forum.greenheartgames.com</a> by <a href=\"/users/pakl\" class=\"mention\">@pakl</a> </li> </ul>";
    private final static SimpleDateFormat mPostDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
    public static String ENCODED_POSTS_ID;

    static {
        try {
            ENCODED_POSTS_ID = URLEncoder.encode(POSTS_ID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ENCODED_POSTS_ID = POSTS_ID;
        }
    }

    public static long ONE_MIN = 1000 * 60;
    public static long ONE_HOUR = 1000 * 60 * 60;
    public static long ONE_DAY = 1000 * 60 * 60 * 24;
    private static NumberFormat nf = new DecimalFormat("#.#K");
    private static double THOUSAND = 1000.0;

    public static final int parseColor(String color) {
        if (!TextUtils.isEmpty(color)) {
            if (color.length() == 3) {
                StringBuilder b = new StringBuilder();
                for (int i = 0; i < 3; i++) {
                    b.append(color.charAt(i));
                    b.append(color.charAt(i));
                }
                color = b.toString();
            }
            color = Utils.COLOR_PREFIX + color;
        }
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
        }
        return Color.WHITE;
    }

    public static final Intent getService(Context ctx, String siteUrl, int type) {
        Intent intent = new Intent();
        intent.setClass(ctx, Service.class);
        intent.putExtra(Utils.EXTRA_TYPE, type);
        intent.putExtra(Utils.EXTRA_URL, siteUrl);
        return intent;
    }

    public static void loge(String msg, Exception e) {
        if (BuildConfig.DEBUG)
            Log.e("DiscourseException", msg, e);
    }

    public static void logi(String msg, Exception e) {
        if (BuildConfig.DEBUG)
            Log.i("DiscourseException", msg, e);
    }

    public static void loge(Exception e) {
        if (BuildConfig.DEBUG)
            Log.e("DiscourseException", e.getMessage(), e);
    }

    // ---- 格式化显示 第一帖和最后一贴日期

    /**
     * format topics list first post time and last post time
     *
     * @param time
     * @return
     */
    public static CharSequence formatActivityTime(long time) {
        return DateUtils.formatSameDayTime(time, System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM);
    }

    // /t/4116/posts.json?post_ids[]=15557&post_ids[]=15558&post_ids[]=15560
    public static String buildMorePostsUrl(String site, Long id, Long[] streams, int index) {
        StringBuilder sb = new StringBuilder(site);
        sb.append(TOPIC).append(SLASH).append(id.longValue()).append(SLASH).append(POSTS).append(MARK);
        for (int i = index; i < streams.length && i < index + PAGE_COUNT; i++) {
            sb.append(ENCODED_POSTS_ID).append(EQ).append(streams[i]).append(AND);
        }

        return sb.toString();
    }

    // t/discourse/549
    public static String buildPostsUrl(String site, Long id, String slug) {
        StringBuilder sb = new StringBuilder(site);
        sb.append(TOPIC).append(SLASH).append(slug).append(SLASH).append(id.longValue()).append(JSON).append(TRACK_VISIT);
        return sb.toString();
    }

    public static String removeLastSlash(String url) {
        if (url.endsWith(Utils.SLASH)) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static String getUserAgent(Context context) {
        return "Mozilla/5.0 (Windows; U; Windows NT 5.2) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/29.0.1547.66 m";
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    public static boolean isToday(long when) {
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year) && (thenMonth == time.month) && (thenMonthDay == time.monthDay);
    }

    public static boolean isInOneMonth(long when) {
        Time time = new Time();
        time.set(when + DAY_30);
        Time now = new Time();
        now.setToNow();
        if (time.before(now)) {
            return false;
        }
        return true;
    }

    public static final CharSequence formatPostTime(long then) {
        CharSequence s = DateUtils.getRelativeTimeSpanString(then, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
        return s;
        // if (isToday(then)) {
        // return getTodayPostTimeDisplay(then);
        // } else if (isInOneMonth(then)) {
        // return getInOneMonthPostTimeDisplay(then);
        // } else {
        // return getOutOneMonthPostTimeDisplay(then);
        // }

    }

    private static CharSequence getTodayPostTimeDisplay(long then) {
        long now = System.currentTimeMillis();
        long delta = (now - then) / ONE_HOUR;
        if (delta > 0) {
            return App.getContext().getString(R.string.post_time_in_hour, delta);
        }
        delta = (now - then) / ONE_MIN;
        if (delta > 0) {
            return App.getContext().getString(R.string.post_time_in_min, delta);
        }

        return App.getContext().getString(R.string.post_time_in_sec);
    }

    private static CharSequence getOutOneMonthPostTimeDisplay(long then) {
        return mPostDateFormat.format(new Date(then));
    }

    private static CharSequence getInOneMonthPostTimeDisplay(long then) {
        long timeOne = then;
        long timeTwo = System.currentTimeMillis();
        long delta = (timeTwo - timeOne) / ONE_DAY;
        return App.getContext().getString(R.string.post_time_in_day, delta);
    }

    public static final String getAvatarUrl(String template, int size) {
        if (TextUtils.isEmpty(template)) {
            return null;
        }
        if (template.startsWith(GRAVATAR_PREFIX)) {
            template = template.replace(GRAVATAR_SIZE, size + "");
            return GRAVATAR_HTTP_PREFIX + template;
        }

        return template.replace(GRAVATAR_SIZE, size + "");
    }

    public static final boolean checkLoginInfo(String site, String name, String password) {
        try {
            CookieManager cm = App.getCookieManager();
            URL url = new URL(site + Api.CSRF_URL);
            URLConnection conn = url.openConnection();
            conn.connect();
            cm.storeCookies(conn);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();

            String csrfs = response.toString();

            JSONObject csrf = new JSONObject(csrfs);
            String csrfV = csrf.getString("csrf");
            Map<String, String> data = new HashMap<String, String>();
            data.put("login", name);
            data.put("password", password);
            data.put("authenticity_token", csrfV);
            // login=rain_hust&password=147852

            HttpRequest r = HttpRequest.post(site + Api.SESSION_URL);
            HttpURLConnection con = r.getConnection();
            cm.setCookies(con);

            r.header("X-CSRF-Token", csrfV).
                    header("Content-Type", "application/json").
                    form(data);
            String user = r.body();
            cm.storeCookies(con);

            HttpRequest r2 = HttpRequest.post(site + Api.LOGIN_URL);
            HttpURLConnection con2 = r2.getConnection();
            cm.setCookies(con2);

            r2.header("X-CSRF-Token", csrfV).
                    header("Content-Type", "application/json").
                    form(data);

            String user2 = r2.body();
            cm.storeCookies(con2);
            HttpRequest g = HttpRequest.get(site + String.format(Api.MSG_URL, name));

            HttpURLConnection con3 = g.getConnection();
            cm.setCookies(con3);
            g.header("X-CSRF-Token", csrfV);

            String b = g.body();

            try {
                JSONObject rr = new JSONObject(b);
            } catch (JSONException e) {
                return false;
            }
            return true;
        } catch (Exception ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    public static final UserDetails login(String site, String name, String password) {
        try {
            CookieManager cm = App.getCookieManager();
            URL url = new URL(site + Api.CSRF_URL);
            URLConnection conn = url.openConnection();
            conn.connect();
            cm.storeCookies(conn);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();

            String csrfs = response.toString();

            JSONObject csrf = new JSONObject(csrfs);
            String csrfV = csrf.getString("csrf");
            Map<String, String> data = new HashMap<String, String>();
            L.i("login: name %s : pwd: %s  csrfv: %s", name, password, csrfV);
            data.put("login", name);
            data.put("password", password);
            data.put("authenticity_token", csrfV);
            App.setXsrToken(csrfV);
            // login=rain_hust&password=147852

            // 返回用户数据
            HttpRequest r = HttpRequest.post(site + Api.SESSION_URL);
            HttpURLConnection con = r.getConnection();
            cm.setCookies(con);

            r.header("X-CSRF-Token", csrfV).
                    header("Content-Type", "application/json").
                    form(data);
            String user = r.body();
            cm.storeCookies(con);
            L.d(user);

            // 返回首页网页html内容
            HttpRequest r2 = HttpRequest.post(site + Api.LOGIN_URL);
            HttpURLConnection con2 = r2.getConnection();
            cm.setCookies(con2);

            r2.header("X-CSRF-Token", csrfV).
                    header("Content-Type", "application/json").
                    form(data);
            String user2 = r2.body();
            cm.storeCookies(con2);
            JSONObject obj = new JSONObject(user);
            if (obj.has(Api.K_user)) {
                obj = obj.getJSONObject(Api.K_user);
                UserDetails result = Api.getJSONObject(obj, UserDetails.class);
                return result;
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public static final void setCategoryView(Category c, TextView view) {
        Category cat = c;
        if (cat == null) {
            view.setText(null);
            view.setBackgroundColor(Color.TRANSPARENT);
        } else {
            setCategoryView(view, cat.name, cat.color, cat.text_color);
        }
    }

    public static final void setCategoryView(TextView view, String name, String bgColor, String textColor) {
        view.setText(name);
        int color = Utils.parseColor(bgColor);
        int tc = Utils.parseColor(textColor);
        if (color == tc) {
            tc = Color.WHITE;
        }
        view.setBackgroundColor(color);
        view.setTextColor(tc);
    }

    public static final void cancelTask(@SuppressWarnings("rawtypes") AsyncTask task) {
        if (task != null && task.isCancelled()) {
            task.cancel(true);
        }
    }

    public static boolean isFirstRun() {
        Context ctx = App.getContext();
        SharedPreferences pref = ctx.getSharedPreferences("first_run", Context.MODE_PRIVATE);
        int version = ctx.getResources().getInteger(R.integer.app_version_code);
        final String key = "version" + version;
        boolean first = pref.getBoolean(key, true);
        if (first) {
            pref.edit().putBoolean(key, false).commit();
        }
        return first;
    }

    public static String formatTopicViews(long views) {
        if (views < 1000) {
            return String.valueOf(views);
        }
        return nf.format(views / THOUSAND);
    }

    /**
     * 返回未读主题标题后面的 星号 ※
     *
     * @param title
     * @return
     */
    public static CharSequence getNewTitleSpan(String title) {
        Spannable span = SpannableStringBuilder.valueOf(title + Api.NEW_SIGN);
        int start = title.length();
        int end = span.length();
        // ff0099cc
        span.setSpan(new ForegroundColorSpan(0xff0099cc), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new AbsoluteSizeSpan(22, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
