package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the category_propertiesTable.
 * This table is represented in the sqlite database as category_properties column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface Category_propertiesTable {
    String TABLE_NAME = "category_properties";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String CAN_CREATE_CATEGORY = "can_create_category";
    String CAN_CREATE_TOPIC = "can_create_topic";
    String DRAFT = "draft";
    String DRAFT_KEY = "draft_key";
    String DRAFT_SEQUENCE = "draft_sequence";
    String[] ALL_COLUMNS = new String[]{ID, CAN_CREATE_CATEGORY, CAN_CREATE_TOPIC, DRAFT, DRAFT_KEY, DRAFT_SEQUENCE};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + CAN_CREATE_CATEGORY + " NUMERIC" + "," + CAN_CREATE_TOPIC + " NUMERIC" + "," + DRAFT + " TEXT" + "," + DRAFT_KEY + " TEXT" + "," + DRAFT_SEQUENCE + " INTEGER" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + CAN_CREATE_CATEGORY + "," + CAN_CREATE_TOPIC + "," + DRAFT + "," + DRAFT_KEY + "," + DRAFT_SEQUENCE + ") VALUES ( ?, ?, ?, ?, ? )";
}
