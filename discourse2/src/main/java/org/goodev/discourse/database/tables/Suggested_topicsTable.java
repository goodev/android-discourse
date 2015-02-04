package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the suggested_topicsTable.
 * This table is represented in the sqlite database as suggested_topics column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface Suggested_topicsTable {
    String TABLE_NAME = "suggested_topics";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String TOPIC_ID = "topic_id";
    String UID = "uid";
    String[] ALL_COLUMNS = new String[]{ID, TOPIC_ID, UID};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + TOPIC_ID + " INTEGER" + "," + UID + " INTEGER" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + TOPIC_ID + "," + UID + ") VALUES ( ?, ? )";
}
