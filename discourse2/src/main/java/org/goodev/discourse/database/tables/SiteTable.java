package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the siteTable.
 * This table is represented in the sqlite database as site column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface SiteTable {
    String TABLE_NAME = "site";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String URL = "url";
    String TITLE = "title";
    String UPDATETIME = "updatetime";
    String[] ALL_COLUMNS = new String[]{ID, URL, TITLE, UPDATETIME};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + URL + " TEXT" + "," + TITLE + " TEXT" + "," + UPDATETIME + " INTEGER" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + URL + "," + TITLE + "," + UPDATETIME + ") VALUES ( ?, ?, ? )";
}
