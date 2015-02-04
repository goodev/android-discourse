package org.goodev.discourse.api;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import org.goodev.discourse.App;
import org.goodev.discourse.api.Categories.CategoryList;
import org.goodev.discourse.api.LatestTopics.TopicList;
import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.api.data.Links;
import org.goodev.discourse.api.data.Post;
import org.goodev.discourse.api.data.PostAction;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.api.data.TopicDetails;
import org.goodev.discourse.api.data.TopicPoster;
import org.goodev.discourse.api.data.User;
import org.goodev.discourse.api.data.UserActions;
import org.goodev.discourse.api.data.UserDetails;
import org.goodev.discourse.contentprovider.Provider;
import org.goodev.discourse.database.tables.CategoriesTable;
import org.goodev.discourse.database.tables.Featured_usersTable;
import org.goodev.discourse.database.tables.Topic_postersTable;
import org.goodev.discourse.database.tables.TopicsTable;
import org.goodev.discourse.model.Categories;
import org.goodev.discourse.model.Topic_posters;
import org.goodev.discourse.model.Topics;
import org.goodev.discourse.model.Topics_properties;
import org.goodev.discourse.utils.HttpRequest;
import org.goodev.discourse.utils.Tools;
import org.goodev.discourse.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class Api {
    public static final String NEW_SIGN = "✲";// ✿

    public static final String RESULTS = "results";
    public static final String TYPE = "type";
    public static final String TYPE_TOPIC = "topic";
    public static final String TYPE_USER = "user";
    /**
     * 显示广告的概率 ，如果为100则代表 100%显示广告
     */
    public static final int AD_RANDOM = 5;
    public static final String MPW = "gdv";
    public static final int TYPE_LATEST = 1;
    public static final int TYPE_CATEGORIES = 2;
    public static final String NULL = "null";
    // 大头像和小头像尺寸， Discourse 上传的图片 只支持两个尺寸 25和 120
    // 不用小头像了 ，本地缓存浪费索引
    public static final int AVATAR_SIZE_SMALL = 120;
    public static final int AVATAR_SIZE_BIG = 120;
    public static final String ARCHETYPE_REGULAR = "regular";
    public static final String ARCHETYPE_PRIVATE_MSG = "private_message";
    public static final String SEARCH_USER = "search.json?term=%s&type_filter=user";
    public static final String SEARCH_TOPIC = "search.json?term=%s&type_filter=topic";
    public static final String CSRF_URL = "session/csrf.json";
    public static final String SESSION_URL = "session.json";
    public static final String LOGIN_URL = "login.json";
    public static final String MSG_URL = "topics/private-messages/%1s.json";
    public static final String MSG_UNREAD_URL = "topics/private-messages-unread/%1s.json";
    public static final String MSG_MINE_URL = "topics/private-messages-sent/%1s.json";
    public static final String CREATED_BY = "topics/created-by/%1s.json";
    public static final String RECOVER_POST = "posts/%1d/recover.json";
    public static final String GET_POST = "posts/%1d.json";
    public static final String GET_USER = "users/%1s.json";
    public static final String GET_USER_ACTIONS = "user_actions.json?offset=%1d&username=%2s";
    public static final String GET_USER_ACTIONS_FILTER = "user_actions.json?offset=%d&username=%s&filter=%s";
    // /posts/287
    public static final String DELETE_POST = "posts/%1d.json";
    // bookmarked true false
    public static final String BOOKMARK = "posts/%1d/bookmark.json";
    public static final String EDIT_TOPIC_TITLE = "t/%1s/%2d.json";
    /**
     * 告诉服务器 该topic 已经阅读过了
     */
    public static final String TOPIC_TIMINGS = "/topics/timings.json";
    // slug/id/index username
    public static final String SHARE = "t/%1s/%2d/%3d";
    public static final String SHARE_LOGIN = "t/%s/%d/%d?u=%s";
    // PUT /t/jeff-bezos-20/567/star // slug/id
    public static final String STAR = "t/%1s/%2d/star.json";
    public static final String NOTIFICATIONS = "t/%1d/notifications.json";
    public static final String POSTS = "posts.json";
    public static final String INVITE = "t/%d/invite.json";
    public static final String POST_ACTIONS = "post_actions.json";
    public static final String DELETE_POST_ACTIONS = "post_actions/%1d.json";
    public static final String UNREAD = "unread.json";
    public static final String READ = "read.json";
    public static final String FAV = "favorited.json";
    public static final String NEW = "new.json";
    public static final String LATEST = "latest.json";
    public static final String POPULAR = "popular.json";
    public static final String UNCATEGORY = "category/uncategorized.json";
    public static final String CATEGORIES = "categories.json";
    public static final String CATEGORIY_ID = "category/%1d-category.json";
    public static final String CATEGORIY_SLUG = "category/%1s.json";
    public static final String K_featured_users = "featured_users";
    public static final String K_id = "id";
    public static final String K_user = "user";
    public static final String K_user_actions = "user_actions";
    public static final String K_username = "username";
    public static final String K_topic_list = "topic_list";
    public static final String K_users = "users";
    public static final String K_avatar_template = "avatar_template";
    public static final String K_category_list = "category_list";
    public static final String K_can_create_category = "can_create_category";
    public static final String K_can_create_topic = "can_create_topic";
    public static final String K_draft = "draft";
    public static final String K_draft_key = "draft_key";
    public static final String K_draft_sequence = "draft_sequence";
    public static final String K_category = "category";
    public static final String K_categories = "categories";
    public static final String K_name = "name";
    public static final String K_color = "color";
    public static final String K_text_color = "text_color";
    public static final String K_slug = "slug";
    public static final String K_topic_count = "topic_count";
    public static final String K_topic_url = "topic_url";
    public static final String K_topics_week = "topics_week";
    public static final String K_topics_month = "topics_month";
    public static final String K_topics_year = "topics_year";
    public static final String K_description = "description";
    public static final String K_description_excerpt = "description_excerpt";
    public static final String K_featured_user_ids = "featured_user_ids";
    public static final String K_topics = "topics";
    public static final String K_posters = "posters";
    public static final String K_fancy_title = "fancy_title";
    public static final String K_title = "title";
    public static final String K_posts_count = "posts_count";
    public static final String K_reply_count = "reply_count";
    public static final String K_highest_post_number = "highest_post_number";
    public static final String K_image_url = "image_url";
    public static final String K_created_at = "created_at";
    public static final String K_last_posted_at = "last_posted_at";
    public static final String K_bumped = "bumped";
    public static final String K_bumped_at = "bumped_at";
    public static final String K_unseen = "unseen";
    public static final String K_pinned = "pinned";
    public static final String K_visible = "visible";
    public static final String K_closed = "closed";
    public static final String K_archived = "archived";
    public static final String K_post_stream = "post_stream";
    public static final String K_post = "post";
    public static final String K_posts = "posts";
    public static final String K_stream = "stream";
    public static final String K_link_counts = "link_counts";
    public static final String K_links = "links";
    public static final String K_actions_summary = "actions_summary";
    public static final String K_details = "details";
    public static final String K_auto_close_at = "auto_close_at";
    public static final String K_created_by = "created_by";
    public static final String K_last_poster = "last_poster";
    public static final String K_participants = "participants";
    public static final String K_suggested_topics = "suggested_topics";
    public static final String K_topic_slug = "topic_slug";
    public static final String K_topic_id = "topic_id";
    public static final String K_errors = "errors";
    public static final String K_basic_topic = "basic_topic";
    public static final String K_stats = "stats";
    public static final String K_count = "count";
    public static final String K_action_type = "action_type";
    public static final String K_ = "";
    // 查询是否数据已经存在， 所有表的应用id 字段名称为 uid
    public static final String[] PROJECTION_UID = new String[]{"_id", "uid"};
    public static final String SELECTION_UID = "uid = ";
    private static final Long UNCATEGORY_ID = -1L;

    public static void parseLatestTopics(JSONObject response) {
        final Context ctx = App.getContext();
        final ContentResolver cr = ctx.getContentResolver();
        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        // update category topic url
        try {
            JSONArray categories = response.getJSONArray(K_categories);
            int length = categories.length();
            Uri catUri = Provider.CATEGORIES_CONTENT_URI;
            for (int i = 0; i < length; i++) {
                JSONObject cate = categories.getJSONObject(i);
                long uid = cate.getLong(K_id);
                String topicUrl = cate.getString(K_topic_url);
                Cursor c = cr.query(catUri, PROJECTION_UID, SELECTION_UID + uid, null, null);
                if (c.getCount() == 0) {
                    // 数据不存在，插入新的
                    ContentValues cv = getFromJSONObject(cate, Categories.class);
                    cv.put(CategoriesTable.UID, uid);
                    ops.add(ContentProviderOperation.newInsert(catUri).withValues(cv).build());
                } else {
                    c.moveToFirst();
                    long rawId = c.getLong(0);
                    Uri uri = ContentUris.withAppendedId(Provider.CATEGORIES_CONTENT_URI, rawId);
                    ops.add(ContentProviderOperation.newUpdate(uri).withValue(CategoriesTable.TOPIC_URL, topicUrl).build());
                }
                closeCursorSafely(c);

            }
        } catch (JSONException e) {
            Utils.loge("update category topic url ", e);
        }

        try {
            JSONArray users = response.getJSONArray(K_users);
            Uri userUri = Provider.TOPICS_USERS_CONTENT_URI;
            int size = users.length();
            for (int i = 0; i < size; i++) {
                JSONObject user = users.getJSONObject(i);
                long id = user.getLong(K_id);
                Cursor c = cr.query(userUri, PROJECTION_UID, SELECTION_UID + id, null, null);

                if (c.getCount() == 0) {
                    String name = user.getString(K_username);
                    String avatar = user.getString(K_avatar_template);
                    ops.add(ContentProviderOperation.newInsert(userUri).withValue(Featured_usersTable.UID, id).withValue(Featured_usersTable.USERNAME, name).withValue(Featured_usersTable.AVATAR_TEMPLATE, avatar).build());
                }
                closeCursorSafely(c);

            }
        } catch (JSONException e) {
            Utils.loge("topic users ", e);
        }

        try {
            JSONObject topic_list = response.getJSONObject(K_topic_list);
            ContentValues cv = getFromJSONObject(topic_list, Topics_properties.class);
            Uri tp = Provider.TOPICS_PROPERTIES_CONTENT_URI;
            cr.delete(tp, null, null);
            ops.add(ContentProviderOperation.newInsert(tp).withValues(cv).build());

            JSONArray topics = topic_list.getJSONArray(K_topics);
            Uri topicUri = Provider.TOPICS_CONTENT_URI;
            int length = topics.length();
            for (int i = 0; i < length; i++) {
                JSONObject topic = topics.getJSONObject(i);
                long tid = topic.getLong(K_id);
                ContentValues values = getFromJSONObject(topic, Topics.class);
                values.put(TopicsTable.UID, tid);
                Cursor c = cr.query(topicUri, PROJECTION_UID, SELECTION_UID + tid, null, null);
                if (c.getCount() == 0) {
                    ops.add(ContentProviderOperation.newInsert(topicUri).withValues(values).build());
                } else {
                    c.moveToFirst();
                    long rawId = c.getLong(0);
                    ops.add(ContentProviderOperation.newUpdate(ContentUris.withAppendedId(topicUri, rawId)).withValues(values).build());
                }
                closeCursorSafely(c);

                try {
                    JSONArray posters = topic.getJSONArray(K_posters);
                    int size = posters.length();
                    Uri uri = Provider.TOPIC_POSTERS_CONTENT_URI;
                    cr.delete(uri, Topic_postersTable.TOPIC_ID + " = " + tid, null);
                    for (int j = 0; j < size; j++) {
                        JSONObject poster = posters.getJSONObject(j);
                        values = getFromJSONObject(poster, Topic_posters.class);
                        values.put(Topic_postersTable.TOPIC_ID, tid);
                        ops.add(ContentProviderOperation.newInsert(uri).withValues(values).build());
                    }
                } catch (Exception e) {
                    Utils.loge("add  topic posters.............  ", e);
                }

            }

        } catch (JSONException e1) {
            Utils.loge("add latest topics  ", e1);
        }

        try {
            ctx.getContentResolver().applyBatch(Provider.AUTHORITY, ops);
        } catch (RemoteException e) {
            Utils.loge("add latest topics ", e);
        } catch (OperationApplicationException e) {
            Utils.loge("add latest topics ", e);
        }
    }

    public static void parseCategories(JSONObject response) {
        final Context ctx = App.getContext();
        final ContentResolver cr = ctx.getContentResolver();
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // try {
        // JSONArray featuredUsers = response.getJSONArray(K_featured_users);
        // int size = featuredUsers.length();
        // for (int i = 0; i < size; i++) {
        // JSONObject user = featuredUsers.getJSONObject(i);
        // long id = user.getLong(K_id);
        // String name = user.getString(K_username);
        // String avatar = user.getString(K_avatar_template);
        // ops.add(ContentProviderOperation.newInsert(Provider.FEATURED_USERS_CONTENT_URI)
        // .withValue(Featured_usersTable.UID, id)
        // .withValue(Featured_usersTable.USERNAME, name)
        // .withValue(Featured_usersTable.AVATAR_TEMPLATE, avatar)
        // .build()
        // );
        // }
        // } catch (JSONException e) {
        // Utils.loge("featured users ", e);
        // }

        try {
            JSONObject categoryList = response.getJSONObject(K_category_list);
            // String draftKey = categoryList.getString(K_draft_key);
            // String draft = categoryList.getString(K_draft);
            // long draftSeq = 0;
            // try {
            // draftSeq = categoryList.getLong(K_draft_sequence);
            // } catch (Exception e) {
            // }
            // boolean can_create_category = categoryList.getBoolean(K_can_create_category);
            // boolean can_create_topic = categoryList.getBoolean(K_can_create_topic);
            // ops.add(ContentProviderOperation.newInsert(Provider.CATEGORY_PROPERTIES_CONTENT_URI)
            // .withValue(Category_propertiesTable.CAN_CREATE_CATEGORY, can_create_category)
            // .withValue(Category_propertiesTable.CAN_CREATE_TOPIC, can_create_topic)
            // .withValue(Category_propertiesTable.DRAFT, draft)
            // .withValue(Category_propertiesTable.DRAFT_KEY, draftKey)
            // .withValue(Category_propertiesTable.DRAFT_SEQUENCE, draftSeq)
            // .build()
            // );

            try {
                JSONArray categories = categoryList.getJSONArray(K_categories);
                int length = categories.length();
                for (int i = 0; i < length; i++) {
                    JSONObject c = categories.getJSONObject(i);
                    long id = -1;
                    try {
                        // 未分类 的 类别 id为 null
                        id = c.getLong(K_id);
                    } catch (Exception e1) {
                    }
                    ContentValues values = getFromJSONObject(c, Categories.class);
                    values.put(CategoriesTable.UID, id);
                    try {
                        JSONArray featured_user_ids = c.getJSONArray(K_featured_user_ids);
                        int size = featured_user_ids.length();
                        StringBuilder users = new StringBuilder();
                        for (int j = 0; j < size; j++) {
                            users.append(featured_user_ids.getInt(j)).append(",");
                        }
                        values.put(K_featured_user_ids, users.toString());
                    } catch (Exception e) {
                        Utils.loge("featured_user_ids " + values.getAsString(K_name), e);
                    }

                    Uri catUri = Provider.CATEGORIES_CONTENT_URI;
                    Cursor cursor = cr.query(catUri, PROJECTION_UID, SELECTION_UID + id, null, null);
                    if (cursor.getCount() == 0) {
                        // 数据不存在，插入新的
                        ops.add(ContentProviderOperation.newInsert(catUri).withValues(values).build());
                    } else {
                        cursor.moveToFirst();
                        long rawId = cursor.getLong(0);
                        Uri uri = ContentUris.withAppendedId(Provider.CATEGORIES_CONTENT_URI, rawId);
                        ops.add(ContentProviderOperation.newUpdate(uri).withValues(values).build());
                    }
                    closeCursorSafely(cursor);

                    // add topics in category

                    // if (!c.has(K_topics)) {
                    // continue;
                    // }
                    //
                    // JSONArray topics = c.getJSONArray(K_topics);
                    // int size = topics.length();
                    // Uri topicUri = Provider.TOPICS_CONTENT_URI;
                    // for (int j = 0; j < size; j++) {
                    // JSONObject topic = topics.getJSONObject(j);
                    // long tid = topic.getLong(K_id);
                    // ContentValues cv = getFromJSONObject(topic, Topics.class);
                    // cv.put(TopicsTable.UID, tid);
                    // cv.put(TopicsTable.CATEGORY_ID, id);
                    // cv.put(TopicsTable.IS_FEATURED, true);
                    //
                    // cursor = cr
                    // .query(topicUri, PROJECTION_UID, SELECTION_UID + tid, null, null);
                    // if (cursor.getCount() == 0) {
                    // ops.add(ContentProviderOperation
                    // .newInsert(topicUri)
                    // .withValues(cv)
                    // .build()
                    // );
                    // } else {
                    // cursor.moveToFirst();
                    // long rawId = cursor.getLong(0);
                    // ops.add(ContentProviderOperation
                    // .newUpdate(ContentUris.withAppendedId(topicUri, rawId))
                    // .withValues(cv)
                    // .build()
                    // );
                    // }
                    // closeCursorSafely(cursor);
                    //
                    // }
                }
            } catch (Exception e) {
                Utils.loge("categories ", e);
            }
        } catch (JSONException e) {
            Utils.loge("category list ", e);
        }

        try {
            ctx.getContentResolver().applyBatch(Provider.AUTHORITY, ops);
        } catch (RemoteException e) {
            Utils.loge("add categories ", e);
        } catch (OperationApplicationException e) {
            Utils.loge("add categories ", e);
        }
    }

    public static void closeCursorSafely(Cursor c) {
        try {
            c.close();
        } catch (Exception e) {
        }
    }

    public static final ContentValues getFromJSONObject(JSONObject obj, Class<?> calzz) {
        ContentValues values = new ContentValues();
        Field[] fields = calzz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            Class<?> type = field.getType();
            if (obj.has(name)) {
                if (type == long.class) {
                    try {
                        if (name.endsWith("_at")) {
                            values.put(name, Tools.convertDateString(obj.getString(name)));
                        } else if (name.equals("uid")) {
                            values.put(name, obj.getLong("id"));
                        } else {
                            values.put(name, obj.getLong(name));
                        }
                    } catch (JSONException e) {
                        Utils.loge("get long " + name, e);
                    }
                } else if (type == boolean.class) {
                    try {
                        values.put(name, obj.getBoolean(name));
                    } catch (JSONException e) {
                        Utils.loge("get boolean " + name, e);
                    }
                } else if (type == String.class) {
                    try {
                        values.put(name, obj.getString(name));
                    } catch (JSONException e) {
                        Utils.loge("get String " + name, e);
                    }
                }
            }
        }
        return values;
    }

    private static Links[] getTopicLinks(JSONArray array) {
        int size = array.length();
        Links[] result = new Links[size];

        for (int i = 0; i < size; i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                Links t = getJSONObject(obj, Links.class);
                result[i] = t;
            } catch (JSONException e) {
                Utils.loge("get Array [] " + Links.class.getSimpleName(), e);
            }
        }

        return result;

    }

    private static Topic[] getSuggestedTopics(JSONArray array) {
        int size = array.length();
        Topic[] result = new Topic[size];

        for (int i = 0; i < size; i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                Topic t = getJSONObject(obj, Topic.class);
                if (obj.has(K_category)) {
                    JSONObject catObj = obj.getJSONObject(K_category);
                    t.category = getJSONObject(catObj, Category.class);
                }
                result[i] = t;
            } catch (JSONException e) {
                Utils.loge("get Array [] " + Topic.class.getSimpleName(), e);
            }
        }

        return result;

    }

    @SuppressWarnings("unchecked")
    public static final <T> T[] getJSONArray(JSONArray array, Class<T> clazz) {
        int size = array.length();
        T[] result = (T[]) Array.newInstance(clazz, size);

        for (int i = 0; i < size; i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                T t = getJSONObject(obj, clazz);
                result[i] = t;
            } catch (JSONException e) {
                Utils.loge("get Array [] " + clazz.getSimpleName(), e);
            }
        }

        return result;
    }

    public static final <T> T getJSONObject(JSONObject obj, Class<T> calzz) {
        T result = null;
        try {
            result = calzz.newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        if (result == null) {
            return null;
        }
        Field[] fields = calzz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name = field.getName();
            Class<?> type = field.getType();
            if (obj.has(name)) {
                if (type == long.class) {
                    try {
                        if (name.endsWith("_at")) {
                            field.setLong(result, Tools.convertDateString(obj.getString(name)));
                        } else if (name.equals("uid")) {
                            field.setLong(result, obj.getLong("id"));
                        } else {
                            field.setLong(result, obj.getLong(name));
                        }
                    } catch (IllegalAccessException e) {
                        // Utils.loge("get long " + name, e);
                    } catch (JSONException e) {
                        // Utils.loge("get long " + name, e);
                    }
                } else if (type == Long.class) {
                    try {
                        field.set(result, obj.getLong(name));
                    } catch (Exception e) {
                        // Utils.loge("get Long " + name, e);
                    }
                } else if (type == long[].class) {
                    try {
                        JSONArray array = obj.getJSONArray(name);
                        int length = array.length();
                        long[] data = new long[length];
                        for (int j = 0; j < length; j++) {
                            data[j] = array.getLong(j);
                        }
                        field.set(result, data);
                    } catch (Exception e) {
                        // Utils.loge("get Long " + name, e);
                    }
                } else if (type == boolean.class) {
                    try {
                        field.setBoolean(result, obj.getBoolean(name));
                    } catch (IllegalAccessException e) {
                        // Utils.loge("get boolean " + name, e);
                    } catch (JSONException e) {
                        // Utils.loge("get boolean " + name, e);
                    }
                } else if (type == int.class) {
                    try {
                        field.setInt(result, obj.getInt(name));
                    } catch (IllegalAccessException e) {
                        // Utils.loge("get int " + name, e);
                    } catch (JSONException e) {
                        // Utils.loge("get int " + name, e);
                    }
                } else if (type == String.class) {
                    try {
                        field.set(result, obj.getString(name));
                    } catch (IllegalAccessException e) {
                        // Utils.loge("get String " + name, e);
                    } catch (JSONException e) {
                        // Utils.loge("get String " + name, e);
                    }
                }
            }
        }
        return result;
    }

    public static org.goodev.discourse.api.Categories getCategories(String url) {
        org.goodev.discourse.api.Categories result = new org.goodev.discourse.api.Categories();
        String res = get(url);
        if (res == null) {
            return result;
        }

        JSONObject response;
        try {
            response = new JSONObject(res);
        } catch (JSONException e) {
            Utils.loge("get categories error :" + url, e);
            return result;
        }

        try {
            JSONArray users = response.getJSONArray(K_featured_users);
            int size = users.length();
            for (int i = 0; i < size; i++) {
                JSONObject user = users.getJSONObject(i);
                User u = getJSONObject(user, User.class);
                result.putUser(u);
            }
        } catch (JSONException e) {
            Utils.loge("categories feature users ", e);
        }

        try {
            JSONObject list = response.getJSONObject(K_category_list);
            CategoryList tl = getJSONObject(list, CategoryList.class);
            result.setCategoryList(tl);

            JSONArray categories = list.getJSONArray(K_categories);
            int size = categories.length();
            for (int i = 0; i < size; i++) {
                JSONObject catObj = categories.getJSONObject(i);
                Category cat = getJSONObject(catObj, Category.class);
                if (cat.id == null) {
                    cat.id = UNCATEGORY_ID;
                }
                result.putCategory(cat);

                JSONArray topics = catObj.getJSONArray(K_topics);
                int length = topics.length();
                for (int ci = 0; ci < length; ci++) {
                    JSONObject topic = topics.getJSONObject(ci);
                    Topic t = getJSONObject(topic, Topic.class);
                    result.putTopic(cat.id, t);

                }
            }

        } catch (JSONException e1) {
            Utils.loge("get categories ", e1);
        }
        return result;
    }

    public static TopicStream getPostsOfTopic(String url) {
        String res = get(url);
        if (res == null) {
            return null;
        }
        JSONObject response;
        try {
            response = new JSONObject(res);
        } catch (JSONException e) {
            Utils.loge("get posts of topic error :" + url, e);
            return null;
        }
        TopicStream ts = new TopicStream();
        try {
            JSONObject post_stream = response.getJSONObject(K_post_stream);
            JSONArray stream = post_stream.getJSONArray(K_stream);
            int size = stream.length();
            Long[] streams = new Long[size];
            for (int i = 0; i < size; i++) {
                long id = stream.getLong(i);
                streams[i] = id;
            }
            ts.mPostStreams = streams;

            JSONArray posts = post_stream.getJSONArray(K_posts);
            size = posts.length();
            for (int i = 0; i < size; i++) {
                JSONObject post = posts.getJSONObject(i);
                Post p = getPost(post);

                ts.mPosts.add(p);
            }
        } catch (JSONException e) {
            Utils.loge("get post_stream of topic error :" + url, e);
        }

        Topic topic = getJSONObject(response, Topic.class);
        ts.mTopic = topic;

        try {
            JSONObject details = response.getJSONObject(K_details);
            TopicDetails td = getJSONObject(details, TopicDetails.class);

            JSONObject created_by = details.getJSONObject(K_created_by);
            td.created_by = getJSONObject(created_by, User.class);
            JSONObject last_poster = details.getJSONObject(K_last_poster);
            td.last_poster = getJSONObject(last_poster, User.class);
            if (details.has(K_participants)) {
                JSONArray array = details.getJSONArray(K_participants);
                td.participants = getJSONArray(array, User.class);
            }
            if (details.has(K_suggested_topics)) {
                JSONArray stArray = details.getJSONArray(K_suggested_topics);
                td.suggested_topics = getSuggestedTopics(stArray);
            }
            if (details.has(K_links)) {
                JSONArray linksArray = details.getJSONArray(K_links);
                td.links = getTopicLinks(linksArray);

            }
            ts.mTopicDetails = td;
        } catch (JSONException e) {
            Utils.loge("get topic details of topic error :" + url, e);
        }
        return ts;
    }

    public static Post getPost(JSONObject post) throws JSONException {
        Post p = getJSONObject(post, Post.class);
        if (post.has(K_link_counts)) {
            JSONArray links = post.getJSONArray(K_link_counts);
            p.link_counts = getJSONArray(links, Links.class);
        }
        if (post.has(K_actions_summary)) {
            JSONArray actions = post.getJSONArray(K_actions_summary);
            p.actions_summary = getJSONArray(actions, PostAction.class);
        }
        return p;
    }

    /**
     * 获取最新的topic数据
     *
     * @param data
     * @param url
     * @return 是否数据更新成功，如果更新失败(没有联网等)，则返回值为false
     */
    public static boolean getLatestTopics(LatestTopics data, String url) {
        if (data == null) {
            throw new IllegalArgumentException("data(LatestTopics) can not be null!");
        }
        String res = get(url);
        if (res == null) {
            return false;
        }

        // update category topic url
        JSONObject response;
        try {
            response = new JSONObject(res);
        } catch (JSONException e) {
            Utils.loge("update latest  topic error :" + url, e);
            return false;
        }
        try {
            // TODO 新版本中 分类信息 不存在了。。。。
            JSONArray categories = response.getJSONArray(K_categories);
            int length = categories.length();
            for (int i = 0; i < length; i++) {
                JSONObject cate = categories.getJSONObject(i);
                Category category = getJSONObject(cate, Category.class);
                data.putCategory(category);
            }
        } catch (JSONException e) {
            Utils.loge("update category topic url ", e);
        }

        try {
            JSONArray users = response.getJSONArray(K_users);
            int size = users.length();
            for (int i = 0; i < size; i++) {
                JSONObject user = users.getJSONObject(i);
                User u = getJSONObject(user, User.class);
                data.putUser(u);
            }
        } catch (JSONException e) {
            Utils.loge("topic users ", e);
        }

        try {
            JSONObject topic_list = response.getJSONObject(K_topic_list);
            TopicList tl = getJSONObject(topic_list, TopicList.class);
            data.setTopicList(tl);

            JSONArray topics = topic_list.getJSONArray(K_topics);
            int length = topics.length();
            for (int i = 0; i < length; i++) {
                JSONObject topic = topics.getJSONObject(i);
                Topic t = getJSONObject(topic, Topic.class);
                data.putTopic(t);

                try {
                    JSONArray posters = topic.getJSONArray(K_posters);
                    int size = posters.length();
                    for (int j = 0; j < size; j++) {
                        JSONObject poster = posters.getJSONObject(j);
                        TopicPoster p = getJSONObject(poster, TopicPoster.class);
                        data.putPoster(t.id, p);
                    }
                } catch (Exception e) {
                    Utils.loge("add  topic posters.............  ", e);
                }

            }

        } catch (JSONException e1) {
            Utils.loge("add latest topics  ", e1);
        }

        return true;
    }

    // TODO 添加 cookie 保存和 设置
    public static String get(String url) {
        try {
            HttpURLConnection connection = HttpRequest.get(url).getConnection();
            if (App.isLogin()) {
                App.getCookieManager().setCookies(connection);
            }
            // final OkHttpClient client = App.getOkHttp();
            // HttpURLConnection connection = client.open(new URL(url));
            InputStream in = null;
            try {
                // Read the response.
                in = connection.getInputStream();
                byte[] response = readFully(in);
                if (App.isLogin()) {
                    App.getCookieManager().storeCookies(connection);
                }
                return new String(response, "UTF-8");
            } finally {
                if (in != null)
                    in.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

    public static UserDetails getUserDetails(JSONObject userObj) {
        UserDetails user = getJSONObject(userObj, UserDetails.class);
        if (userObj.has(K_stats)) {
            try {
                JSONArray statsArray = userObj.getJSONArray(K_stats);
                int length = statsArray.length();
                for (int i = 0; i < length; i++) {
                    try {
                        JSONObject action = statsArray.getJSONObject(i);
                        int type = action.getInt(K_action_type);
                        int count = action.getInt(K_count);
                        user.stats.put(type, count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public static List<UserActions> getUserActions(JSONArray obj) {
        int length = obj.length();
        ArrayList<UserActions> actions = new ArrayList<UserActions>();
        for (int i = 0; i < length; i++) {
            try {
                JSONObject o = obj.getJSONObject(i);
                UserActions a = getJSONObject(o, UserActions.class);
                actions.add(a);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return actions;
    }

    public interface Params {
        static final String TOPIC_ID = "topic_id";
        static final String TOPIC_TIME = "topic_time";
        static final String TIMINGS = "timings[%d]";
    }

}
