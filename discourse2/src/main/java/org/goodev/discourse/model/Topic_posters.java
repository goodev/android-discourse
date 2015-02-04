package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.Topic_postersTable;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class Topic_posters {

    private final ContentValues values = new ContentValues();
    private Long id;
    private java.lang.String extras;
    private java.lang.String description;
    private long user_id;
    private long topic_id;

    public Topic_posters() {
    }

    public Topic_posters(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(Topic_postersTable.ID)));
        setExtras(cursor.getString(cursor.getColumnIndex(Topic_postersTable.EXTRAS)));
        setDescription(cursor.getString(cursor.getColumnIndex(Topic_postersTable.DESCRIPTION)));
        setUser_id(cursor.getLong(cursor.getColumnIndex(Topic_postersTable.USER_ID)));
        setTopic_id(cursor.getLong(cursor.getColumnIndex(Topic_postersTable.TOPIC_ID)));

    }

    /**
     * Get id
     *
     * @return id from type java.lang.Long
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Set id
     *
     * @param id from type java.lang.Long
     */
    public void setId(final Long id) {
        this.id = id;
        this.values.put(Topic_postersTable.ID, id);
    }

    /**
     * Get extras
     *
     * @return extras from type java.lang.String
     */
    public java.lang.String getExtras() {
        return this.extras;
    }

    /**
     * Set extras and set content value
     *
     * @param extras from type java.lang.String
     */
    public void setExtras(final java.lang.String extras) {
        this.extras = extras;
        this.values.put(Topic_postersTable.EXTRAS, extras);
    }

    /**
     * Get description
     *
     * @return description from type java.lang.String
     */
    public java.lang.String getDescription() {
        return this.description;
    }

    /**
     * Set description and set content value
     *
     * @param description from type java.lang.String
     */
    public void setDescription(final java.lang.String description) {
        this.description = description;
        this.values.put(Topic_postersTable.DESCRIPTION, description);
    }

    /**
     * Get user_id
     *
     * @return user_id from type long
     */
    public long getUser_id() {
        return this.user_id;
    }

    /**
     * Set user_id and set content value
     *
     * @param user_id from type long
     */
    public void setUser_id(final long user_id) {
        this.user_id = user_id;
        this.values.put(Topic_postersTable.USER_ID, user_id);
    }

    /**
     * Get topic_id
     *
     * @return topic_id from type long
     */
    public long getTopic_id() {
        return this.topic_id;
    }

    /**
     * Set topic_id and set content value
     *
     * @param topic_id from type long
     */
    public void setTopic_id(final long topic_id) {
        this.topic_id = topic_id;
        this.values.put(Topic_postersTable.TOPIC_ID, topic_id);
    }

    /**
     * Get ContentValues
     *
     * @return id from type android.content.ContentValues with the values of this object
     */
    public ContentValues getContentValues() {
        return this.values;
    }
}
