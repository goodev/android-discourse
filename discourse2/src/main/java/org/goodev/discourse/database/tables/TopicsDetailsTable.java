package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the topicsDetailsTable.
 * This table is represented in the sqlite database as topicsDetails column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface TopicsDetailsTable {
    String TABLE_NAME = "topicsdetails";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String AUTO_CLOSE_AT = "auto_close_at";
    String CREATED_BY_UID = "created_by_uid";
    String CREATED_BY_USERNAME = "created_by_username";
    String CREATED_BY_AVATAR_TEMPLATE = "created_by_avatar_template";
    String LAST_POSTER_UID = "last_poster_uid";
    String LAST_POSTER_USERNAME = "last_poster_username";
    String LAST_POSTER_AVATAR_TEMPLATE = "last_poster_avatar_template";
    String TOPIC_ID = "topic_id";
    String SUGGESTED_TOPICS = "suggested_topics";
    String[] ALL_COLUMNS = new String[]{ID, AUTO_CLOSE_AT, CREATED_BY_UID, CREATED_BY_USERNAME, CREATED_BY_AVATAR_TEMPLATE, LAST_POSTER_UID, LAST_POSTER_USERNAME, LAST_POSTER_AVATAR_TEMPLATE, TOPIC_ID, SUGGESTED_TOPICS};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + AUTO_CLOSE_AT + " INTEGER" + "," + CREATED_BY_UID + " INTEGER" + "," + CREATED_BY_USERNAME + " TEXT" + "," + CREATED_BY_AVATAR_TEMPLATE + " TEXT" + "," + LAST_POSTER_UID + " INTEGER" + "," + LAST_POSTER_USERNAME + " TEXT" + "," + LAST_POSTER_AVATAR_TEMPLATE + " TEXT" + "," + TOPIC_ID + " INTEGER" + "," + SUGGESTED_TOPICS + " TEXT" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + AUTO_CLOSE_AT + "," + CREATED_BY_UID + "," + CREATED_BY_USERNAME + "," + CREATED_BY_AVATAR_TEMPLATE + "," + LAST_POSTER_UID + "," + LAST_POSTER_USERNAME + "," + LAST_POSTER_AVATAR_TEMPLATE + "," + TOPIC_ID + "," + SUGGESTED_TOPICS + ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
}
