package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the topics_usersTable.
 * This table is represented in the sqlite database as topics_users column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface Topics_usersTable {
    String TABLE_NAME = "topics_users";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String UID = "uid";
    String USERNAME = "username";
    String AVATAR_TEMPLATE = "avatar_template";
    String[] ALL_COLUMNS = new String[]{ID, UID, USERNAME, AVATAR_TEMPLATE};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + UID + " INTEGER" + "," + USERNAME + " TEXT" + "," + AVATAR_TEMPLATE + " TEXT" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + UID + "," + USERNAME + "," + AVATAR_TEMPLATE + ") VALUES ( ?, ?, ? )";
}
