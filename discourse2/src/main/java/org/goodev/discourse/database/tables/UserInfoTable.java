package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the userInfoTable.
 * This table is represented in the sqlite database as userInfo column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface UserInfoTable {
    String TABLE_NAME = "userinfo";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String NAME = "name";
    String PASSWORD = "password";
    String SITEID = "siteid";
    String SITEURL = "siteurl";
    String[] ALL_COLUMNS = new String[]{ID, NAME, PASSWORD, SITEID, SITEURL};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + NAME + " TEXT" + "," + PASSWORD + " TEXT" + "," + SITEID + " INTEGER" + "," + SITEURL + " TEXT" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + NAME + "," + PASSWORD + "," + SITEID + "," + SITEURL + ") VALUES ( ?, ?, ?, ? )";
}
