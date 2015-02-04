package org.goodev.discourse.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import org.goodev.discourse.database.Database;
import org.goodev.discourse.database.tables.CategoriesTable;
import org.goodev.discourse.database.tables.Category_group_permissionsTable;
import org.goodev.discourse.database.tables.Category_propertiesTable;
import org.goodev.discourse.database.tables.Featured_usersTable;
import org.goodev.discourse.database.tables.SiteTable;
import org.goodev.discourse.database.tables.Suggested_topicsTable;
import org.goodev.discourse.database.tables.Topic_postersTable;
import org.goodev.discourse.database.tables.TopicsDetailsTable;
import org.goodev.discourse.database.tables.TopicsParticipantsTable;
import org.goodev.discourse.database.tables.TopicsTable;
import org.goodev.discourse.database.tables.Topics_propertiesTable;
import org.goodev.discourse.database.tables.Topics_usersTable;
import org.goodev.discourse.database.tables.UserInfoTable;

/**
 * Content provider implementation
 * The authority of the content provider is: content://org.goodev.discourse.provider.discourse
 * <p/>
 * More information about content providers:
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 * @see <a href="http://developer.android.com/reference/android/content/ContentProvider.html">Reference</a>
 * @see <a href="http://developer.android.com/guide/topics/providers/content-providers.html">Tutorial</a>
 * @see <a href="http://developer.android.com/guide/topics/testing/contentprovider_testing.html">Content Provider Testing</a>
 * <p/>
 * Generated Class. Do not modify!
 */
public class Provider extends ContentProvider {
    public static final String AUTHORITY = "org.goodev.discourse.provider.discourse";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri SITE_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, SiteContent.CONTENT_PATH);
    public static final Uri USERINFO_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, UserInfoContent.CONTENT_PATH);
    public static final Uri FEATURED_USERS_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, Featured_usersContent.CONTENT_PATH);
    public static final Uri CATEGORY_PROPERTIES_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, Category_propertiesContent.CONTENT_PATH);
    public static final Uri CATEGORIES_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, CategoriesContent.CONTENT_PATH);
    public static final Uri TOPICS_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, TopicsContent.CONTENT_PATH);
    public static final Uri TOPICSDETAILS_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, TopicsDetailsContent.CONTENT_PATH);
    public static final Uri TOPICSPARTICIPANTS_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, TopicsParticipantsContent.CONTENT_PATH);
    public static final Uri SUGGESTED_TOPICS_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, Suggested_topicsContent.CONTENT_PATH);
    public static final Uri CATEGORY_GROUP_PERMISSIONS_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, Category_group_permissionsContent.CONTENT_PATH);
    public static final Uri TOPICS_USERS_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, Topics_usersContent.CONTENT_PATH);
    public static final Uri TOPICS_PROPERTIES_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, Topics_propertiesContent.CONTENT_PATH);
    public static final Uri TOPIC_POSTERS_CONTENT_URI = Uri.withAppendedPath(Provider.AUTHORITY_URI, Topic_postersContent.CONTENT_PATH);
    private static final String TAG = "org.goodev.discourse.contentprovider.Provider";
    private static final UriMatcher URI_MATCHER;
    private static final int SITE_DIR = 0;
    private static final int SITE_ID = 1;
    private static final int USERINFO_DIR = 2;
    private static final int USERINFO_ID = 3;
    private static final int FEATURED_USERS_DIR = 4;
    private static final int FEATURED_USERS_ID = 5;
    private static final int CATEGORY_PROPERTIES_DIR = 6;
    private static final int CATEGORY_PROPERTIES_ID = 7;
    private static final int CATEGORIES_DIR = 8;
    private static final int CATEGORIES_ID = 9;
    private static final int TOPICS_DIR = 10;
    private static final int TOPICS_ID = 11;
    private static final int TOPICSDETAILS_DIR = 12;
    private static final int TOPICSDETAILS_ID = 13;
    private static final int TOPICSPARTICIPANTS_DIR = 14;
    private static final int TOPICSPARTICIPANTS_ID = 15;
    private static final int SUGGESTED_TOPICS_DIR = 16;
    private static final int SUGGESTED_TOPICS_ID = 17;
    private static final int CATEGORY_GROUP_PERMISSIONS_DIR = 18;
    private static final int CATEGORY_GROUP_PERMISSIONS_ID = 19;
    private static final int TOPICS_USERS_DIR = 20;
    private static final int TOPICS_USERS_ID = 21;
    private static final int TOPICS_PROPERTIES_DIR = 22;
    private static final int TOPICS_PROPERTIES_ID = 23;
    private static final int TOPIC_POSTERS_DIR = 24;
    private static final int TOPIC_POSTERS_ID = 25;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, SiteContent.CONTENT_PATH, SITE_DIR);
        URI_MATCHER.addURI(AUTHORITY, SiteContent.CONTENT_PATH + "/#", SITE_ID);
        URI_MATCHER.addURI(AUTHORITY, UserInfoContent.CONTENT_PATH, USERINFO_DIR);
        URI_MATCHER.addURI(AUTHORITY, UserInfoContent.CONTENT_PATH + "/#", USERINFO_ID);
        URI_MATCHER.addURI(AUTHORITY, Featured_usersContent.CONTENT_PATH, FEATURED_USERS_DIR);
        URI_MATCHER.addURI(AUTHORITY, Featured_usersContent.CONTENT_PATH + "/#", FEATURED_USERS_ID);
        URI_MATCHER.addURI(AUTHORITY, Category_propertiesContent.CONTENT_PATH, CATEGORY_PROPERTIES_DIR);
        URI_MATCHER.addURI(AUTHORITY, Category_propertiesContent.CONTENT_PATH + "/#", CATEGORY_PROPERTIES_ID);
        URI_MATCHER.addURI(AUTHORITY, CategoriesContent.CONTENT_PATH, CATEGORIES_DIR);
        URI_MATCHER.addURI(AUTHORITY, CategoriesContent.CONTENT_PATH + "/#", CATEGORIES_ID);
        URI_MATCHER.addURI(AUTHORITY, TopicsContent.CONTENT_PATH, TOPICS_DIR);
        URI_MATCHER.addURI(AUTHORITY, TopicsContent.CONTENT_PATH + "/#", TOPICS_ID);
        URI_MATCHER.addURI(AUTHORITY, TopicsDetailsContent.CONTENT_PATH, TOPICSDETAILS_DIR);
        URI_MATCHER.addURI(AUTHORITY, TopicsDetailsContent.CONTENT_PATH + "/#", TOPICSDETAILS_ID);
        URI_MATCHER.addURI(AUTHORITY, TopicsParticipantsContent.CONTENT_PATH, TOPICSPARTICIPANTS_DIR);
        URI_MATCHER.addURI(AUTHORITY, TopicsParticipantsContent.CONTENT_PATH + "/#", TOPICSPARTICIPANTS_ID);
        URI_MATCHER.addURI(AUTHORITY, Suggested_topicsContent.CONTENT_PATH, SUGGESTED_TOPICS_DIR);
        URI_MATCHER.addURI(AUTHORITY, Suggested_topicsContent.CONTENT_PATH + "/#", SUGGESTED_TOPICS_ID);
        URI_MATCHER.addURI(AUTHORITY, Category_group_permissionsContent.CONTENT_PATH, CATEGORY_GROUP_PERMISSIONS_DIR);
        URI_MATCHER.addURI(AUTHORITY, Category_group_permissionsContent.CONTENT_PATH + "/#", CATEGORY_GROUP_PERMISSIONS_ID);
        URI_MATCHER.addURI(AUTHORITY, Topics_usersContent.CONTENT_PATH, TOPICS_USERS_DIR);
        URI_MATCHER.addURI(AUTHORITY, Topics_usersContent.CONTENT_PATH + "/#", TOPICS_USERS_ID);
        URI_MATCHER.addURI(AUTHORITY, Topics_propertiesContent.CONTENT_PATH, TOPICS_PROPERTIES_DIR);
        URI_MATCHER.addURI(AUTHORITY, Topics_propertiesContent.CONTENT_PATH + "/#", TOPICS_PROPERTIES_ID);
        URI_MATCHER.addURI(AUTHORITY, Topic_postersContent.CONTENT_PATH, TOPIC_POSTERS_DIR);
        URI_MATCHER.addURI(AUTHORITY, Topic_postersContent.CONTENT_PATH + "/#", TOPIC_POSTERS_ID);
    }

    private Database db;

    /**
     * Instantiate the database, when the content provider is created
     */
    @Override
    public final boolean onCreate() {
        db = new Database(getContext());
        return true;
    }

    /**
     * Providing information whether uri returns an item or an directory.
     *
     * @param uri from type Uri
     * @return content_type from type Content.CONTENT_TYPE or Content.CONTENT_ITEM_TYPE
     */
    @Override
    public final String getType(final Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case SITE_DIR:
                return SiteContent.CONTENT_TYPE;
            case SITE_ID:
                return SiteContent.CONTENT_ITEM_TYPE;
            case USERINFO_DIR:
                return UserInfoContent.CONTENT_TYPE;
            case USERINFO_ID:
                return UserInfoContent.CONTENT_ITEM_TYPE;
            case FEATURED_USERS_DIR:
                return Featured_usersContent.CONTENT_TYPE;
            case FEATURED_USERS_ID:
                return Featured_usersContent.CONTENT_ITEM_TYPE;
            case CATEGORY_PROPERTIES_DIR:
                return Category_propertiesContent.CONTENT_TYPE;
            case CATEGORY_PROPERTIES_ID:
                return Category_propertiesContent.CONTENT_ITEM_TYPE;
            case CATEGORIES_DIR:
                return CategoriesContent.CONTENT_TYPE;
            case CATEGORIES_ID:
                return CategoriesContent.CONTENT_ITEM_TYPE;
            case TOPICS_DIR:
                return TopicsContent.CONTENT_TYPE;
            case TOPICS_ID:
                return TopicsContent.CONTENT_ITEM_TYPE;
            case TOPICSDETAILS_DIR:
                return TopicsDetailsContent.CONTENT_TYPE;
            case TOPICSDETAILS_ID:
                return TopicsDetailsContent.CONTENT_ITEM_TYPE;
            case TOPICSPARTICIPANTS_DIR:
                return TopicsParticipantsContent.CONTENT_TYPE;
            case TOPICSPARTICIPANTS_ID:
                return TopicsParticipantsContent.CONTENT_ITEM_TYPE;
            case SUGGESTED_TOPICS_DIR:
                return Suggested_topicsContent.CONTENT_TYPE;
            case SUGGESTED_TOPICS_ID:
                return Suggested_topicsContent.CONTENT_ITEM_TYPE;
            case CATEGORY_GROUP_PERMISSIONS_DIR:
                return Category_group_permissionsContent.CONTENT_TYPE;
            case CATEGORY_GROUP_PERMISSIONS_ID:
                return Category_group_permissionsContent.CONTENT_ITEM_TYPE;
            case TOPICS_USERS_DIR:
                return Topics_usersContent.CONTENT_TYPE;
            case TOPICS_USERS_ID:
                return Topics_usersContent.CONTENT_ITEM_TYPE;
            case TOPICS_PROPERTIES_DIR:
                return Topics_propertiesContent.CONTENT_TYPE;
            case TOPICS_PROPERTIES_ID:
                return Topics_propertiesContent.CONTENT_ITEM_TYPE;
            case TOPIC_POSTERS_DIR:
                return Topic_postersContent.CONTENT_TYPE;
            case TOPIC_POSTERS_ID:
                return Topic_postersContent.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    /**
     * Insert given values to given uri. Uri has to be from type directory (see switch-cases).
     * Returns uri of inserted element.
     *
     * @param uri    from type Uri
     * @param values from type ContentValues
     * @return uri of inserted element from type Uri
     */
    @Override
    public final Uri insert(final Uri uri, final ContentValues values) {
        final SQLiteDatabase dbConnection = db.getWritableDatabase();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case SITE_DIR:
                case SITE_ID:
                    final long siteid = dbConnection.insertOrThrow(SiteTable.TABLE_NAME, null, values);
                    final Uri newSite = ContentUris.withAppendedId(SITE_CONTENT_URI, siteid);
                    getContext().getContentResolver().notifyChange(newSite, null);
                    dbConnection.setTransactionSuccessful();
                    return newSite;
                case USERINFO_DIR:
                case USERINFO_ID:
                    final long userinfoid = dbConnection.insertOrThrow(UserInfoTable.TABLE_NAME, null, values);
                    final Uri newUserInfo = ContentUris.withAppendedId(USERINFO_CONTENT_URI, userinfoid);
                    getContext().getContentResolver().notifyChange(newUserInfo, null);
                    dbConnection.setTransactionSuccessful();
                    return newUserInfo;
                case FEATURED_USERS_DIR:
                case FEATURED_USERS_ID:
                    final long featured_usersid = dbConnection.insertOrThrow(Featured_usersTable.TABLE_NAME, null, values);
                    final Uri newFeatured_users = ContentUris.withAppendedId(FEATURED_USERS_CONTENT_URI, featured_usersid);
                    getContext().getContentResolver().notifyChange(newFeatured_users, null);
                    dbConnection.setTransactionSuccessful();
                    return newFeatured_users;
                case CATEGORY_PROPERTIES_DIR:
                case CATEGORY_PROPERTIES_ID:
                    final long category_propertiesid = dbConnection.insertOrThrow(Category_propertiesTable.TABLE_NAME, null, values);
                    final Uri newCategory_properties = ContentUris.withAppendedId(CATEGORY_PROPERTIES_CONTENT_URI, category_propertiesid);
                    getContext().getContentResolver().notifyChange(newCategory_properties, null);
                    dbConnection.setTransactionSuccessful();
                    return newCategory_properties;
                case CATEGORIES_DIR:
                case CATEGORIES_ID:
                    final long categoriesid = dbConnection.insertOrThrow(CategoriesTable.TABLE_NAME, null, values);
                    final Uri newCategories = ContentUris.withAppendedId(CATEGORIES_CONTENT_URI, categoriesid);
                    getContext().getContentResolver().notifyChange(newCategories, null);
                    dbConnection.setTransactionSuccessful();
                    return newCategories;
                case TOPICS_DIR:
                case TOPICS_ID:
                    final long topicsid = dbConnection.insertOrThrow(TopicsTable.TABLE_NAME, null, values);
                    final Uri newTopics = ContentUris.withAppendedId(TOPICS_CONTENT_URI, topicsid);
                    getContext().getContentResolver().notifyChange(newTopics, null);
                    dbConnection.setTransactionSuccessful();
                    return newTopics;
                case TOPICSDETAILS_DIR:
                case TOPICSDETAILS_ID:
                    final long topicsdetailsid = dbConnection.insertOrThrow(TopicsDetailsTable.TABLE_NAME, null, values);
                    final Uri newTopicsDetails = ContentUris.withAppendedId(TOPICSDETAILS_CONTENT_URI, topicsdetailsid);
                    getContext().getContentResolver().notifyChange(newTopicsDetails, null);
                    dbConnection.setTransactionSuccessful();
                    return newTopicsDetails;
                case TOPICSPARTICIPANTS_DIR:
                case TOPICSPARTICIPANTS_ID:
                    final long topicsparticipantsid = dbConnection.insertOrThrow(TopicsParticipantsTable.TABLE_NAME, null, values);
                    final Uri newTopicsParticipants = ContentUris.withAppendedId(TOPICSPARTICIPANTS_CONTENT_URI, topicsparticipantsid);
                    getContext().getContentResolver().notifyChange(newTopicsParticipants, null);
                    dbConnection.setTransactionSuccessful();
                    return newTopicsParticipants;
                case SUGGESTED_TOPICS_DIR:
                case SUGGESTED_TOPICS_ID:
                    final long suggested_topicsid = dbConnection.insertOrThrow(Suggested_topicsTable.TABLE_NAME, null, values);
                    final Uri newSuggested_topics = ContentUris.withAppendedId(SUGGESTED_TOPICS_CONTENT_URI, suggested_topicsid);
                    getContext().getContentResolver().notifyChange(newSuggested_topics, null);
                    dbConnection.setTransactionSuccessful();
                    return newSuggested_topics;
                case CATEGORY_GROUP_PERMISSIONS_DIR:
                case CATEGORY_GROUP_PERMISSIONS_ID:
                    final long category_group_permissionsid = dbConnection.insertOrThrow(Category_group_permissionsTable.TABLE_NAME, null, values);
                    final Uri newCategory_group_permissions = ContentUris.withAppendedId(CATEGORY_GROUP_PERMISSIONS_CONTENT_URI, category_group_permissionsid);
                    getContext().getContentResolver().notifyChange(newCategory_group_permissions, null);
                    dbConnection.setTransactionSuccessful();
                    return newCategory_group_permissions;
                case TOPICS_USERS_DIR:
                case TOPICS_USERS_ID:
                    final long topics_usersid = dbConnection.insertOrThrow(Topics_usersTable.TABLE_NAME, null, values);
                    final Uri newTopics_users = ContentUris.withAppendedId(TOPICS_USERS_CONTENT_URI, topics_usersid);
                    getContext().getContentResolver().notifyChange(newTopics_users, null);
                    dbConnection.setTransactionSuccessful();
                    return newTopics_users;
                case TOPICS_PROPERTIES_DIR:
                case TOPICS_PROPERTIES_ID:
                    final long topics_propertiesid = dbConnection.insertOrThrow(Topics_propertiesTable.TABLE_NAME, null, values);
                    final Uri newTopics_properties = ContentUris.withAppendedId(TOPICS_PROPERTIES_CONTENT_URI, topics_propertiesid);
                    getContext().getContentResolver().notifyChange(newTopics_properties, null);
                    dbConnection.setTransactionSuccessful();
                    return newTopics_properties;
                case TOPIC_POSTERS_DIR:
                case TOPIC_POSTERS_ID:
                    final long topic_postersid = dbConnection.insertOrThrow(Topic_postersTable.TABLE_NAME, null, values);
                    final Uri newTopic_posters = ContentUris.withAppendedId(TOPIC_POSTERS_CONTENT_URI, topic_postersid);
                    getContext().getContentResolver().notifyChange(newTopic_posters, null);
                    dbConnection.setTransactionSuccessful();
                    return newTopic_posters;
                default:
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } catch (Exception e) {
            Log.e(TAG, "Insert Exception", e);
        } finally {
            dbConnection.endTransaction();
        }

        return null;
    }

    /**
     * Updates given values of given uri, returning number of affected rows.
     *
     * @param uri           from type Uri
     * @param values        from type ContentValues
     * @param selection     from type String
     * @param selectionArgs from type String[]
     * @return number of affected rows from type int
     */
    @Override
    public final int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {

        final SQLiteDatabase dbConnection = db.getWritableDatabase();
        int updateCount = 0;

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {

                case SITE_DIR:
                    updateCount = dbConnection.update(SiteTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case SITE_ID:
                    final Long siteId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(SiteTable.TABLE_NAME, values, SiteTable.ID + "=" + siteId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case USERINFO_DIR:
                    updateCount = dbConnection.update(UserInfoTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case USERINFO_ID:
                    final Long userinfoId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(UserInfoTable.TABLE_NAME, values, UserInfoTable.ID + "=" + userinfoId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case FEATURED_USERS_DIR:
                    updateCount = dbConnection.update(Featured_usersTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case FEATURED_USERS_ID:
                    final Long featured_usersId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(Featured_usersTable.TABLE_NAME, values, Featured_usersTable.ID + "=" + featured_usersId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case CATEGORY_PROPERTIES_DIR:
                    updateCount = dbConnection.update(Category_propertiesTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_PROPERTIES_ID:
                    final Long category_propertiesId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(Category_propertiesTable.TABLE_NAME, values, Category_propertiesTable.ID + "=" + category_propertiesId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case CATEGORIES_DIR:
                    updateCount = dbConnection.update(CategoriesTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORIES_ID:
                    final Long categoriesId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(CategoriesTable.TABLE_NAME, values, CategoriesTable.ID + "=" + categoriesId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case TOPICS_DIR:
                    updateCount = dbConnection.update(TopicsTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICS_ID:
                    final Long topicsId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(TopicsTable.TABLE_NAME, values, TopicsTable.ID + "=" + topicsId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case TOPICSDETAILS_DIR:
                    updateCount = dbConnection.update(TopicsDetailsTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICSDETAILS_ID:
                    final Long topicsdetailsId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(TopicsDetailsTable.TABLE_NAME, values, TopicsDetailsTable.ID + "=" + topicsdetailsId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case TOPICSPARTICIPANTS_DIR:
                    updateCount = dbConnection.update(TopicsParticipantsTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICSPARTICIPANTS_ID:
                    final Long topicsparticipantsId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(TopicsParticipantsTable.TABLE_NAME, values, TopicsParticipantsTable.ID + "=" + topicsparticipantsId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case SUGGESTED_TOPICS_DIR:
                    updateCount = dbConnection.update(Suggested_topicsTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case SUGGESTED_TOPICS_ID:
                    final Long suggested_topicsId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(Suggested_topicsTable.TABLE_NAME, values, Suggested_topicsTable.ID + "=" + suggested_topicsId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case CATEGORY_GROUP_PERMISSIONS_DIR:
                    updateCount = dbConnection.update(Category_group_permissionsTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_GROUP_PERMISSIONS_ID:
                    final Long category_group_permissionsId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(Category_group_permissionsTable.TABLE_NAME, values, Category_group_permissionsTable.ID + "=" + category_group_permissionsId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case TOPICS_USERS_DIR:
                    updateCount = dbConnection.update(Topics_usersTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICS_USERS_ID:
                    final Long topics_usersId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(Topics_usersTable.TABLE_NAME, values, Topics_usersTable.ID + "=" + topics_usersId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case TOPICS_PROPERTIES_DIR:
                    updateCount = dbConnection.update(Topics_propertiesTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICS_PROPERTIES_ID:
                    final Long topics_propertiesId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(Topics_propertiesTable.TABLE_NAME, values, Topics_propertiesTable.ID + "=" + topics_propertiesId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;

                case TOPIC_POSTERS_DIR:
                    updateCount = dbConnection.update(Topic_postersTable.TABLE_NAME, values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPIC_POSTERS_ID:
                    final Long topic_postersId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(Topic_postersTable.TABLE_NAME, values, Topic_postersTable.ID + "=" + topic_postersId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updateCount;

    }

    /**
     * Deletes given elements by their uri (items or directories) and returns number of deleted rows.
     *
     * @param uri           from type Uri
     * @param selection     from type String
     * @param selectionArgs from type String[]
     * @return number of deleted rows from type int
     */
    @Override
    public final int delete(final Uri uri, final String selection, final String[] selectionArgs) {

        final SQLiteDatabase dbConnection = db.getWritableDatabase();
        int deleteCount = 0;

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case SITE_DIR:
                    deleteCount = dbConnection.delete(SiteTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case SITE_ID:
                    deleteCount = dbConnection.delete(SiteTable.TABLE_NAME, SiteTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case USERINFO_DIR:
                    deleteCount = dbConnection.delete(UserInfoTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case USERINFO_ID:
                    deleteCount = dbConnection.delete(UserInfoTable.TABLE_NAME, UserInfoTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case FEATURED_USERS_DIR:
                    deleteCount = dbConnection.delete(Featured_usersTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case FEATURED_USERS_ID:
                    deleteCount = dbConnection.delete(Featured_usersTable.TABLE_NAME, Featured_usersTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_PROPERTIES_DIR:
                    deleteCount = dbConnection.delete(Category_propertiesTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_PROPERTIES_ID:
                    deleteCount = dbConnection.delete(Category_propertiesTable.TABLE_NAME, Category_propertiesTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORIES_DIR:
                    deleteCount = dbConnection.delete(CategoriesTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORIES_ID:
                    deleteCount = dbConnection.delete(CategoriesTable.TABLE_NAME, CategoriesTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICS_DIR:
                    deleteCount = dbConnection.delete(TopicsTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICS_ID:
                    deleteCount = dbConnection.delete(TopicsTable.TABLE_NAME, TopicsTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICSDETAILS_DIR:
                    deleteCount = dbConnection.delete(TopicsDetailsTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICSDETAILS_ID:
                    deleteCount = dbConnection.delete(TopicsDetailsTable.TABLE_NAME, TopicsDetailsTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICSPARTICIPANTS_DIR:
                    deleteCount = dbConnection.delete(TopicsParticipantsTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICSPARTICIPANTS_ID:
                    deleteCount = dbConnection.delete(TopicsParticipantsTable.TABLE_NAME, TopicsParticipantsTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case SUGGESTED_TOPICS_DIR:
                    deleteCount = dbConnection.delete(Suggested_topicsTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case SUGGESTED_TOPICS_ID:
                    deleteCount = dbConnection.delete(Suggested_topicsTable.TABLE_NAME, Suggested_topicsTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_GROUP_PERMISSIONS_DIR:
                    deleteCount = dbConnection.delete(Category_group_permissionsTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_GROUP_PERMISSIONS_ID:
                    deleteCount = dbConnection.delete(Category_group_permissionsTable.TABLE_NAME, Category_group_permissionsTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICS_USERS_DIR:
                    deleteCount = dbConnection.delete(Topics_usersTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICS_USERS_ID:
                    deleteCount = dbConnection.delete(Topics_usersTable.TABLE_NAME, Topics_usersTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICS_PROPERTIES_DIR:
                    deleteCount = dbConnection.delete(Topics_propertiesTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPICS_PROPERTIES_ID:
                    deleteCount = dbConnection.delete(Topics_propertiesTable.TABLE_NAME, Topics_propertiesTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPIC_POSTERS_DIR:
                    deleteCount = dbConnection.delete(Topic_postersTable.TABLE_NAME, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case TOPIC_POSTERS_ID:
                    deleteCount = dbConnection.delete(Topic_postersTable.TABLE_NAME, Topic_postersTable.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (deleteCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleteCount;

    }

    /**
     * Executes a query on a given uri and returns a Cursor with results.
     *
     * @param uri           from type Uri
     * @param projection    from type String[]
     * @param selection     from type String
     * @param selectionArgs from type String[]
     * @param sortOrder     from type String
     * @return cursor with results from type Cursor
     */
    @Override
    public final Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {

        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final SQLiteDatabase dbConnection = db.getReadableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case SITE_ID:
                queryBuilder.appendWhere(SiteTable.ID + "=" + uri.getPathSegments().get(1));
            case SITE_DIR:
                queryBuilder.setTables(SiteTable.TABLE_NAME);
                break;
            case USERINFO_ID:
                queryBuilder.appendWhere(UserInfoTable.ID + "=" + uri.getPathSegments().get(1));
            case USERINFO_DIR:
                queryBuilder.setTables(UserInfoTable.TABLE_NAME);
                break;
            case FEATURED_USERS_ID:
                queryBuilder.appendWhere(Featured_usersTable.ID + "=" + uri.getPathSegments().get(1));
            case FEATURED_USERS_DIR:
                queryBuilder.setTables(Featured_usersTable.TABLE_NAME);
                break;
            case CATEGORY_PROPERTIES_ID:
                queryBuilder.appendWhere(Category_propertiesTable.ID + "=" + uri.getPathSegments().get(1));
            case CATEGORY_PROPERTIES_DIR:
                queryBuilder.setTables(Category_propertiesTable.TABLE_NAME);
                break;
            case CATEGORIES_ID:
                queryBuilder.appendWhere(CategoriesTable.ID + "=" + uri.getPathSegments().get(1));
            case CATEGORIES_DIR:
                queryBuilder.setTables(CategoriesTable.TABLE_NAME);
                break;
            case TOPICS_ID:
                queryBuilder.appendWhere(TopicsTable.ID + "=" + uri.getPathSegments().get(1));
            case TOPICS_DIR:
                queryBuilder.setTables(TopicsTable.TABLE_NAME);
                break;
            case TOPICSDETAILS_ID:
                queryBuilder.appendWhere(TopicsDetailsTable.ID + "=" + uri.getPathSegments().get(1));
            case TOPICSDETAILS_DIR:
                queryBuilder.setTables(TopicsDetailsTable.TABLE_NAME);
                break;
            case TOPICSPARTICIPANTS_ID:
                queryBuilder.appendWhere(TopicsParticipantsTable.ID + "=" + uri.getPathSegments().get(1));
            case TOPICSPARTICIPANTS_DIR:
                queryBuilder.setTables(TopicsParticipantsTable.TABLE_NAME);
                break;
            case SUGGESTED_TOPICS_ID:
                queryBuilder.appendWhere(Suggested_topicsTable.ID + "=" + uri.getPathSegments().get(1));
            case SUGGESTED_TOPICS_DIR:
                queryBuilder.setTables(Suggested_topicsTable.TABLE_NAME);
                break;
            case CATEGORY_GROUP_PERMISSIONS_ID:
                queryBuilder.appendWhere(Category_group_permissionsTable.ID + "=" + uri.getPathSegments().get(1));
            case CATEGORY_GROUP_PERMISSIONS_DIR:
                queryBuilder.setTables(Category_group_permissionsTable.TABLE_NAME);
                break;
            case TOPICS_USERS_ID:
                queryBuilder.appendWhere(Topics_usersTable.ID + "=" + uri.getPathSegments().get(1));
            case TOPICS_USERS_DIR:
                queryBuilder.setTables(Topics_usersTable.TABLE_NAME);
                break;
            case TOPICS_PROPERTIES_ID:
                queryBuilder.appendWhere(Topics_propertiesTable.ID + "=" + uri.getPathSegments().get(1));
            case TOPICS_PROPERTIES_DIR:
                queryBuilder.setTables(Topics_propertiesTable.TABLE_NAME);
                break;
            case TOPIC_POSTERS_ID:
                queryBuilder.appendWhere(Topic_postersTable.ID + "=" + uri.getPathSegments().get(1));
            case TOPIC_POSTERS_DIR:
                queryBuilder.setTables(Topic_postersTable.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }

        Cursor cursor = queryBuilder.query(dbConnection, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    /**
     * Provides the content information of the SiteTable.
     * <p/>
     * CONTENT_PATH: site (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.site (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.site (String)
     * ALL_COLUMNS: Provides the same information as SiteTable.ALL_COLUMNS (String[])
     */
    public static final class SiteContent implements BaseColumns {
        /**
         * Specifies the content path of the SiteTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/site
         */
        public static final String CONTENT_PATH = "site";

        /**
         * Specifies the type for the folder and the single item of the SiteTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.site";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.site";

        /**
         * Contains all columns of the SiteTable
         */
        public static final String[] ALL_COLUMNS = SiteTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the UserInfoTable.
     * <p/>
     * CONTENT_PATH: userinfo (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.userinfo (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.userinfo (String)
     * ALL_COLUMNS: Provides the same information as UserInfoTable.ALL_COLUMNS (String[])
     */
    public static final class UserInfoContent implements BaseColumns {
        /**
         * Specifies the content path of the UserInfoTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/userinfo
         */
        public static final String CONTENT_PATH = "userinfo";

        /**
         * Specifies the type for the folder and the single item of the UserInfoTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.userinfo";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.userinfo";

        /**
         * Contains all columns of the UserInfoTable
         */
        public static final String[] ALL_COLUMNS = UserInfoTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the Featured_usersTable.
     * <p/>
     * CONTENT_PATH: featured_users (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.featured_users (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.featured_users (String)
     * ALL_COLUMNS: Provides the same information as Featured_usersTable.ALL_COLUMNS (String[])
     */
    public static final class Featured_usersContent implements BaseColumns {
        /**
         * Specifies the content path of the Featured_usersTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/featured_users
         */
        public static final String CONTENT_PATH = "featured_users";

        /**
         * Specifies the type for the folder and the single item of the Featured_usersTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.featured_users";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.featured_users";

        /**
         * Contains all columns of the Featured_usersTable
         */
        public static final String[] ALL_COLUMNS = Featured_usersTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the Category_propertiesTable.
     * <p/>
     * CONTENT_PATH: category_properties (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.category_properties (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.category_properties (String)
     * ALL_COLUMNS: Provides the same information as Category_propertiesTable.ALL_COLUMNS (String[])
     */
    public static final class Category_propertiesContent implements BaseColumns {
        /**
         * Specifies the content path of the Category_propertiesTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/category_properties
         */
        public static final String CONTENT_PATH = "category_properties";

        /**
         * Specifies the type for the folder and the single item of the Category_propertiesTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.category_properties";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.category_properties";

        /**
         * Contains all columns of the Category_propertiesTable
         */
        public static final String[] ALL_COLUMNS = Category_propertiesTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the CategoriesTable.
     * <p/>
     * CONTENT_PATH: categories (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.categories (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.categories (String)
     * ALL_COLUMNS: Provides the same information as CategoriesTable.ALL_COLUMNS (String[])
     */
    public static final class CategoriesContent implements BaseColumns {
        /**
         * Specifies the content path of the CategoriesTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/categories
         */
        public static final String CONTENT_PATH = "categories";

        /**
         * Specifies the type for the folder and the single item of the CategoriesTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.categories";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.categories";

        /**
         * Contains all columns of the CategoriesTable
         */
        public static final String[] ALL_COLUMNS = CategoriesTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the TopicsTable.
     * <p/>
     * CONTENT_PATH: topics (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.topics (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.topics (String)
     * ALL_COLUMNS: Provides the same information as TopicsTable.ALL_COLUMNS (String[])
     */
    public static final class TopicsContent implements BaseColumns {
        /**
         * Specifies the content path of the TopicsTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/topics
         */
        public static final String CONTENT_PATH = "topics";

        /**
         * Specifies the type for the folder and the single item of the TopicsTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.topics";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.topics";

        /**
         * Contains all columns of the TopicsTable
         */
        public static final String[] ALL_COLUMNS = TopicsTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the TopicsDetailsTable.
     * <p/>
     * CONTENT_PATH: topicsdetails (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.topicsdetails (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.topicsdetails (String)
     * ALL_COLUMNS: Provides the same information as TopicsDetailsTable.ALL_COLUMNS (String[])
     */
    public static final class TopicsDetailsContent implements BaseColumns {
        /**
         * Specifies the content path of the TopicsDetailsTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/topicsdetails
         */
        public static final String CONTENT_PATH = "topicsdetails";

        /**
         * Specifies the type for the folder and the single item of the TopicsDetailsTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.topicsdetails";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.topicsdetails";

        /**
         * Contains all columns of the TopicsDetailsTable
         */
        public static final String[] ALL_COLUMNS = TopicsDetailsTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the TopicsParticipantsTable.
     * <p/>
     * CONTENT_PATH: topicsparticipants (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.topicsparticipants (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.topicsparticipants (String)
     * ALL_COLUMNS: Provides the same information as TopicsParticipantsTable.ALL_COLUMNS (String[])
     */
    public static final class TopicsParticipantsContent implements BaseColumns {
        /**
         * Specifies the content path of the TopicsParticipantsTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/topicsparticipants
         */
        public static final String CONTENT_PATH = "topicsparticipants";

        /**
         * Specifies the type for the folder and the single item of the TopicsParticipantsTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.topicsparticipants";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.topicsparticipants";

        /**
         * Contains all columns of the TopicsParticipantsTable
         */
        public static final String[] ALL_COLUMNS = TopicsParticipantsTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the Suggested_topicsTable.
     * <p/>
     * CONTENT_PATH: suggested_topics (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.suggested_topics (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.suggested_topics (String)
     * ALL_COLUMNS: Provides the same information as Suggested_topicsTable.ALL_COLUMNS (String[])
     */
    public static final class Suggested_topicsContent implements BaseColumns {
        /**
         * Specifies the content path of the Suggested_topicsTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/suggested_topics
         */
        public static final String CONTENT_PATH = "suggested_topics";

        /**
         * Specifies the type for the folder and the single item of the Suggested_topicsTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.suggested_topics";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.suggested_topics";

        /**
         * Contains all columns of the Suggested_topicsTable
         */
        public static final String[] ALL_COLUMNS = Suggested_topicsTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the Category_group_permissionsTable.
     * <p/>
     * CONTENT_PATH: category_group_permissions (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.category_group_permissions (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.category_group_permissions (String)
     * ALL_COLUMNS: Provides the same information as Category_group_permissionsTable.ALL_COLUMNS (String[])
     */
    public static final class Category_group_permissionsContent implements BaseColumns {
        /**
         * Specifies the content path of the Category_group_permissionsTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/category_group_permissions
         */
        public static final String CONTENT_PATH = "category_group_permissions";

        /**
         * Specifies the type for the folder and the single item of the Category_group_permissionsTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.category_group_permissions";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.category_group_permissions";

        /**
         * Contains all columns of the Category_group_permissionsTable
         */
        public static final String[] ALL_COLUMNS = Category_group_permissionsTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the Topics_usersTable.
     * <p/>
     * CONTENT_PATH: topics_users (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.topics_users (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.topics_users (String)
     * ALL_COLUMNS: Provides the same information as Topics_usersTable.ALL_COLUMNS (String[])
     */
    public static final class Topics_usersContent implements BaseColumns {
        /**
         * Specifies the content path of the Topics_usersTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/topics_users
         */
        public static final String CONTENT_PATH = "topics_users";

        /**
         * Specifies the type for the folder and the single item of the Topics_usersTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.topics_users";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.topics_users";

        /**
         * Contains all columns of the Topics_usersTable
         */
        public static final String[] ALL_COLUMNS = Topics_usersTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the Topics_propertiesTable.
     * <p/>
     * CONTENT_PATH: topics_properties (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.topics_properties (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.topics_properties (String)
     * ALL_COLUMNS: Provides the same information as Topics_propertiesTable.ALL_COLUMNS (String[])
     */
    public static final class Topics_propertiesContent implements BaseColumns {
        /**
         * Specifies the content path of the Topics_propertiesTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/topics_properties
         */
        public static final String CONTENT_PATH = "topics_properties";

        /**
         * Specifies the type for the folder and the single item of the Topics_propertiesTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.topics_properties";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.topics_properties";

        /**
         * Contains all columns of the Topics_propertiesTable
         */
        public static final String[] ALL_COLUMNS = Topics_propertiesTable.ALL_COLUMNS;
    }

    /**
     * Provides the content information of the Topic_postersTable.
     * <p/>
     * CONTENT_PATH: topic_posters (String)
     * CONTENT_TYPE: vnd.android.cursor.dir/vnd.mdsdacp.topic_posters (String)
     * CONTENT_ITEM_TYPE: vnd.android.cursor.item/vnd.mdsdacp.topic_posters (String)
     * ALL_COLUMNS: Provides the same information as Topic_postersTable.ALL_COLUMNS (String[])
     */
    public static final class Topic_postersContent implements BaseColumns {
        /**
         * Specifies the content path of the Topic_postersTable for the required uri
         * Exact URI: content://org.goodev.discourse.provider.discourse/topic_posters
         */
        public static final String CONTENT_PATH = "topic_posters";

        /**
         * Specifies the type for the folder and the single item of the Topic_postersTable
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mdsdacp.topic_posters";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mdsdacp.topic_posters";

        /**
         * Contains all columns of the Topic_postersTable
         */
        public static final String[] ALL_COLUMNS = Topic_postersTable.ALL_COLUMNS;
    }

}
