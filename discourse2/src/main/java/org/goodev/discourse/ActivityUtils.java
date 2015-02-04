package org.goodev.discourse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.TextUtils;
import android.widget.Toast;

import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.api.data.Post;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.photos.PhotosActivity;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.Utils;

import java.util.List;

public class ActivityUtils {
    public static void openCategoryActivity(Activity a, Category c, String url) {
        Intent intent = new Intent();
        intent.setClass(a, CategoryActivity.class);
        intent.putExtra(Utils.EXTRA_URL, url);
        intent.putExtra(Utils.EXTRA_TITLE, c.name);
        intent.putExtra(Utils.EXTRA_SLUG, c.slug);
        intent.putExtra(Utils.EXTRA_ID, c.id);
        a.startActivity(intent);
    }

    public static void openPhotosActivity(Context a, int index, String[] imgs) {
        Intent intent = new Intent(a, PhotosActivity.class);
        intent.putExtra(Utils.EXTRA_NUMBER, index);
        intent.putExtra(Utils.EXTRA_URL, imgs);
        if (!(a instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        a.startActivity(intent);
    }

    public static void openNewPrivateMsgEditorActivity(Activity a, String username) {
        Intent intent = new Intent(a, EditorActivity.class);
        intent.putExtra(Utils.EXTRA_NAME, username);
        intent.putExtra(Utils.EXTRA_IS_PRIVATE_MSG, true);
        a.startActivity(intent);

    }

    public static void openNewEditorActivity(Activity a, Post post) {
        Intent intent = new Intent(a, EditorActivity.class);
        if (post != null) {
            intent.putExtra(Utils.EXTRA_ID, post.id);
        }
        a.startActivity(intent);

    }

    public static void openReplayTopicActivity(Activity a, Topic t, Post p) {
        Intent intent = new Intent(a, EditorActivity.class);
        intent.putExtra(Utils.EXTRA_OBJ, t);
        intent.putExtra(Utils.EXTRA_NAME, p.username);
        intent.putExtra(Utils.EXTRA_NUMBER, 1);
        intent.putExtra(Utils.EXTRA_ID, p.id);
        a.startActivity(intent);

    }

    public static void openReplayPostActivity(Activity a, Topic t, Post post, int postNum) {
        Intent intent = new Intent(a, EditorActivity.class);
        intent.putExtra(Utils.EXTRA_NAME, post.username);
        intent.putExtra(Utils.EXTRA_NUMBER, postNum);
        intent.putExtra(Utils.EXTRA_ID, post.id);
        intent.putExtra(Utils.EXTRA_OBJ, t);
        a.startActivity(intent);
    }

    public static void startTopicActivity(Context a, String slug, String idStr) {
        L.i("topic id: %s ----------%b", idStr, (a instanceof Activity));
        long id = 0;
        ;
        try {
            id = Long.valueOf(idStr);
        } catch (NumberFormatException e) {
            L.e(e);
            return;
        }
        Intent intent = new Intent(a, TopicActivity.class);
        intent.putExtra(Utils.EXTRA_ID, id);
        intent.putExtra(Utils.EXTRA_SLUG, slug);
        if (!(a instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        a.startActivity(intent);
    }

    public static void startTopicActivity(Activity a, String slug, long id) {
        L.i("topic id: %d", id);
        Intent intent = new Intent(a, TopicActivity.class);
        intent.putExtra(Utils.EXTRA_ID, id);
        intent.putExtra(Utils.EXTRA_SLUG, slug);
        a.startActivity(intent);
    }

    public static void startTopicActivity(Activity a, Topic t, Category c, String url) {
        L.i("topic category: %s", t.category);
        Intent intent = new Intent(a, TopicActivity.class);
        intent.putExtra(Utils.EXTRA_OBJ, t);
        intent.putExtra(Utils.EXTRA_OBJ_C, c);
        intent.putExtra(Utils.EXTRA_ID, t.id.longValue());
        intent.putExtra(Utils.EXTRA_TITLE, t.title);
        intent.putExtra(Utils.EXTRA_SLUG, t.slug);
        intent.putExtra(Utils.EXTRA_URL, url);
        intent.putExtra(Utils.EXTRA_NUMBER, t.last_read_post_number);
        a.startActivity(intent);
    }

    public static void openUserActivity(Context a, String name) {
        Intent intent = new Intent(a, UserActivity.class);
        intent.putExtra(Utils.EXTRA_NAME, name);
        if (!(a instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        a.startActivity(intent);
    }

    public static void openSocialHelpActivity(Activity a) {
        Intent intent = new Intent(a, HelpActivity.class);
        intent.putExtra(Utils.EXTRA_URL, "login.html");
        a.startActivity(intent);

    }

    public static void openUrl(Context c, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, c.getPackageName());
        checkContextIsActivity(c, intent);
        c.startActivity(intent);
    }

    private static void checkContextIsActivity(Context a, Intent intent) {
        if (!(a instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }

    public static void openDiscourseLinks(Context ctx, String url) {
        final String site = App.getSiteUrl();
        if (TextUtils.isEmpty(site)) {
            Toast.makeText(App.getContext(), R.string.reboot_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (url.startsWith(Utils.SLASH)) {
            url = url.substring(Utils.SLASH.length());
            url = site + url;
        }

        L.i("... %s ", url);
        Uri uri = Uri.parse(url);
        List<String> paths = uri.getPathSegments();
        if (paths.size() >= 2) {
            final String path0 = paths.get(0);
            if (path0.equals(Utils.USERS)) {
                String name = paths.get(1);
                ActivityUtils.openUserActivity(ctx, name);
            } else if (Utils.T.equals(path0)) {
                String slug = paths.get(1);
                String id = paths.get(2);
                // TODO post index
                // String postIndex = paths.get(3);
                ActivityUtils.startTopicActivity(ctx, slug, id);
            } else {
                // TODO handler more in app nav...
                L.e("handler more in app nav...");
            }
        }
    }
}
