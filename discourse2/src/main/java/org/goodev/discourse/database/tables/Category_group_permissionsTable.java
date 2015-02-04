package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the category_group_permissionsTable.
 * This table is represented in the sqlite database as category_group_permissions column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface Category_group_permissionsTable {
    String TABLE_NAME = "category_group_permissions";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String CATEGORY_ID = "category_id";
    String PERMISSION_TYPE = "permission_type";
    String GROUP_NAME = "group_name";
    String[] ALL_COLUMNS = new String[]{ID, CATEGORY_ID, PERMISSION_TYPE, GROUP_NAME};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + CATEGORY_ID + " INTEGER" + "," + PERMISSION_TYPE + " INTEGER" + "," + GROUP_NAME + " TEXT" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + CATEGORY_ID + "," + PERMISSION_TYPE + "," + GROUP_NAME + ") VALUES ( ?, ?, ? )";
}
