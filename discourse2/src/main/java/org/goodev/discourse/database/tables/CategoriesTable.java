package org.goodev.discourse.database.tables;

/**
 * This interface represents the columns and SQLite statements for the categoriesTable.
 * This table is represented in the sqlite database as categories column.
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public interface CategoriesTable {
    String TABLE_NAME = "categories";
    String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    String ID = "_id";
    String WHERE_ID_EQUALS = ID + "=?";
    String UID = "uid";
    String NAME = "name";
    String COLOR = "color";
    String TEXT_COLOR = "text_color";
    String SLUG = "slug";
    String TOPIC_COUNT = "topic_count";
    String TOPICS_WEEK = "topics_week";
    String TOPICS_MONTH = "topics_month";
    String TOPICS_YEAR = "topics_year";
    String DESCRIPTION = "description";
    String DESCRIPTION_EXCERPT = "description_excerpt";
    String FEATURED_USER_IDS = "featured_user_ids";
    String FEATURED_TOPICS_IDS = "featured_topics_ids";
    String TOPIC_URL = "topic_url";
    String HOTNESS = "hotness";
    String READ_RESTRICTED = "read_restricted";
    String PERMISSION = "permission";
    String AVAILABLE_GROUPS = "available_groups";
    String AUTO_CLOSE_DAYS = "auto_close_days";
    String[] ALL_COLUMNS = new String[]{ID, UID, NAME, COLOR, TEXT_COLOR, SLUG, TOPIC_COUNT, TOPICS_WEEK, TOPICS_MONTH, TOPICS_YEAR, DESCRIPTION, DESCRIPTION_EXCERPT, FEATURED_USER_IDS, FEATURED_TOPICS_IDS, TOPIC_URL, HOTNESS, READ_RESTRICTED, PERMISSION, AVAILABLE_GROUPS, AUTO_CLOSE_DAYS};
    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," + UID + " INTEGER" + "," + NAME + " TEXT" + "," + COLOR + " TEXT" + "," + TEXT_COLOR + " TEXT" + "," + SLUG + " TEXT" + "," + TOPIC_COUNT + " INTEGER" + "," + TOPICS_WEEK + " INTEGER" + "," + TOPICS_MONTH + " INTEGER" + "," + TOPICS_YEAR + " INTEGER" + "," + DESCRIPTION + " TEXT" + "," + DESCRIPTION_EXCERPT + " TEXT" + "," + FEATURED_USER_IDS + " TEXT" + "," + FEATURED_TOPICS_IDS + " TEXT" + "," + TOPIC_URL + " TEXT" + "," + HOTNESS + " INTEGER" + "," + READ_RESTRICTED + " NUMERIC" + "," + PERMISSION + " TEXT" + "," + AVAILABLE_GROUPS + " TEXT" + "," + AUTO_CLOSE_DAYS + " INTEGER" + " )";
    String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + UID + "," + NAME + "," + COLOR + "," + TEXT_COLOR + "," + SLUG + "," + TOPIC_COUNT + "," + TOPICS_WEEK + "," + TOPICS_MONTH + "," + TOPICS_YEAR + "," + DESCRIPTION + "," + DESCRIPTION_EXCERPT + "," + FEATURED_USER_IDS + "," + FEATURED_TOPICS_IDS + "," + TOPIC_URL + "," + HOTNESS + "," + READ_RESTRICTED + "," + PERMISSION + "," + AVAILABLE_GROUPS + "," + AUTO_CLOSE_DAYS + ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
}
