package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the topics_propertiesTable.
 * This table is represented in the sqlite database as topics_properties column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface Topics_propertiesTable {
    String TABLE_NAME = "topics_properties";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String CAN_CREATE_TOPIC = "can_create_topic";
    String MORE_TOPICS_URL = "more_topics_url";
    String DRAFT = "draft";
    String DRAFT_KEY = "draft_key";
    String DRAFT_SEQUENCE = "draft_sequence";
    String[] ALL_COLUMNS = new String[]{ID, CAN_CREATE_TOPIC, MORE_TOPICS_URL, DRAFT, DRAFT_KEY, DRAFT_SEQUENCE};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + CAN_CREATE_TOPIC + " NUMERIC" + "," + MORE_TOPICS_URL + " TEXT" + "," + DRAFT + " TEXT" + "," + DRAFT_KEY + " TEXT" + "," + DRAFT_SEQUENCE + " INTEGER" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + CAN_CREATE_TOPIC + "," + MORE_TOPICS_URL + "," + DRAFT + "," + DRAFT_KEY + "," + DRAFT_SEQUENCE + ") VALUES ( ?, ?, ?, ?, ? )";
}
