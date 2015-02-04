package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the topic_postersTable.
 * This table is represented in the sqlite database as topic_posters column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface Topic_postersTable {
    String TABLE_NAME = "topic_posters";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String EXTRAS = "extras";
    String DESCRIPTION = "description";
    String USER_ID = "user_id";
    String TOPIC_ID = "topic_id";
    String[] ALL_COLUMNS = new String[]{ID, EXTRAS, DESCRIPTION, USER_ID, TOPIC_ID};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + EXTRAS + " TEXT" + "," + DESCRIPTION + " TEXT" + "," + USER_ID + " INTEGER" + "," + TOPIC_ID + " INTEGER" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + EXTRAS + "," + DESCRIPTION + "," + USER_ID + "," + TOPIC_ID + ") VALUES ( ?, ?, ?, ? )";
}
