package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the topicsParticipantsTable.
 * This table is represented in the sqlite database as topicsParticipants column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface TopicsParticipantsTable {
    String TABLE_NAME = "topicsparticipants";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String TOPIC_ID = "topic_id";
    String UID = "uid";
    String USERNAME = "username";
    String AVATAR_TEMPLATE = "avatar_template";
    String POST_COUNT = "post_count";
    String[] ALL_COLUMNS = new String[]{ID, TOPIC_ID, UID, USERNAME, AVATAR_TEMPLATE, POST_COUNT};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + TOPIC_ID + " INTEGER" + "," + UID + " INTEGER" + "," + USERNAME + " TEXT" + "," + AVATAR_TEMPLATE + " TEXT" + "," + POST_COUNT + " INTEGER" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + TOPIC_ID + "," + UID + "," + USERNAME + "," + AVATAR_TEMPLATE + "," + POST_COUNT + ") VALUES ( ?, ?, ?, ?, ? )";
}
