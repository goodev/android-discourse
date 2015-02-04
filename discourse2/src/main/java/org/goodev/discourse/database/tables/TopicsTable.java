package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the topicsTable.
 * This table is represented in the sqlite database as topics column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface TopicsTable {
    String TABLE_NAME = "topics";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String UID = "uid";
    String TITLE = "title";
    String FANCY_TITLE = "fancy_title";
    String SLUG = "slug";
    String POSTS_COUNT = "posts_count";
    String REPLY_COUNT = "reply_count";
    String HIGHEST_POST_NUMBER = "highest_post_number";
    String IMAGE_URL = "image_url";
    String CREATED_AT = "created_at";
    String LAST_POSTED_AT = "last_posted_at";
    String BUMPED = "bumped";
    String BUMPED_AT = "bumped_at";
    String UNSEEN = "unseen";
    String PINNED = "pinned";
    String VISIBLE = "visible";
    String CLOSED = "closed";
    String ARCHIVED = "archived";
    String DRAFT = "draft";
    String DRAFT_KEY = "draft_key";
    String DRAFT_SEQUENCE = "draft_sequence";
    String DELETED_BY = "deleted_by";
    String VIEWS = "views";
    String HAS_BEST_OF = "has_best_of";
    String ARCHETYPE = "archetype";
    String CATEGORY_ID = "category_id";
    String DELETED_AT = "deleted_at";
    String IS_FEATURED = "is_featured";
    String LIKE_COUNT = "like_count";
    String STARRED = "starred";
    String[] ALL_COLUMNS = new String[]{ID, UID, TITLE, FANCY_TITLE, SLUG, POSTS_COUNT, REPLY_COUNT, HIGHEST_POST_NUMBER, IMAGE_URL, CREATED_AT, LAST_POSTED_AT, BUMPED, BUMPED_AT, UNSEEN, PINNED, VISIBLE, CLOSED, ARCHIVED, DRAFT, DRAFT_KEY, DRAFT_SEQUENCE, DELETED_BY, VIEWS, HAS_BEST_OF, ARCHETYPE, CATEGORY_ID, DELETED_AT, IS_FEATURED, LIKE_COUNT, STARRED};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + UID + " INTEGER" + "," + TITLE + " TEXT" + "," + FANCY_TITLE + " TEXT" + "," + SLUG + " TEXT" + "," + POSTS_COUNT + " INTEGER" + "," + REPLY_COUNT + " INTEGER" + "," + HIGHEST_POST_NUMBER + " INTEGER" + "," + IMAGE_URL + " TEXT" + "," + CREATED_AT + " INTEGER" + "," + LAST_POSTED_AT + " INTEGER" + "," + BUMPED + " NUMERIC" + "," + BUMPED_AT + " INTEGER" + "," + UNSEEN + " NUMERIC" + "," + PINNED + " NUMERIC" + "," + VISIBLE + " NUMERIC" + "," + CLOSED + " NUMERIC" + "," + ARCHIVED + " NUMERIC" + "," + DRAFT + " TEXT" + "," + DRAFT_KEY + " TEXT" + "," + DRAFT_SEQUENCE + " INTEGER" + "," + DELETED_BY + " INTEGER" + "," + VIEWS + " INTEGER" + "," + HAS_BEST_OF + " NUMERIC" + "," + ARCHETYPE + " TEXT" + "," + CATEGORY_ID + " INTEGER" + "," + DELETED_AT + " INTEGER" + "," + IS_FEATURED + " NUMERIC" + "," + LIKE_COUNT + " INTEGER" + "," + STARRED + " NUMERIC" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + UID + "," + TITLE + "," + FANCY_TITLE + "," + SLUG + "," + POSTS_COUNT + "," + REPLY_COUNT + "," + HIGHEST_POST_NUMBER + "," + IMAGE_URL + "," + CREATED_AT + "," + LAST_POSTED_AT + "," + BUMPED + "," + BUMPED_AT + "," + UNSEEN + "," + PINNED + "," + VISIBLE + "," + CLOSED + "," + ARCHIVED + "," + DRAFT + "," + DRAFT_KEY + "," + DRAFT_SEQUENCE + "," + DELETED_BY + "," + VIEWS + "," + HAS_BEST_OF + "," + ARCHETYPE + "," + CATEGORY_ID + "," + DELETED_AT + "," + IS_FEATURED + "," + LIKE_COUNT + "," + STARRED + ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
}
