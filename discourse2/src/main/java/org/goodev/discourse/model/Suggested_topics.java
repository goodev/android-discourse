package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.Suggested_topicsTable;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class Suggested_topics {

    private final ContentValues values = new ContentValues();
    private Long id;
    private long topic_id;
    private long uid;

    public Suggested_topics() {
    }

    public Suggested_topics(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(Suggested_topicsTable.ID)));
        setTopic_id(cursor.getLong(cursor.getColumnIndex(Suggested_topicsTable.TOPIC_ID)));
        setUid(cursor.getLong(cursor.getColumnIndex(Suggested_topicsTable.UID)));

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
        this.values.put(Suggested_topicsTable.ID, id);
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
        this.values.put(Suggested_topicsTable.TOPIC_ID, topic_id);
    }

    /**
     * Get uid
     *
     * @return uid from type long
     */
    public long getUid() {
        return this.uid;
    }

    /**
     * Set uid and set content value
     *
     * @param uid from type long
     */
    public void setUid(final long uid) {
        this.uid = uid;
        this.values.put(Suggested_topicsTable.UID, uid);
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
