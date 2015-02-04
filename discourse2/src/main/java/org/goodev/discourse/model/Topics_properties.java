package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.Topics_propertiesTable;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class Topics_properties {

    private final ContentValues values = new ContentValues();
    private Long id;
    private boolean can_create_topic;
    private java.lang.String more_topics_url;
    private java.lang.String draft;
    private java.lang.String draft_key;
    private long draft_sequence;

    public Topics_properties() {
    }

    public Topics_properties(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(Topics_propertiesTable.ID)));
        setCan_create_topic(cursor.isNull(cursor.getColumnIndex(Topics_propertiesTable.CAN_CREATE_TOPIC)) ? false : (cursor.getInt(cursor.getColumnIndex(Topics_propertiesTable.CAN_CREATE_TOPIC)) != 0));
        setMore_topics_url(cursor.getString(cursor.getColumnIndex(Topics_propertiesTable.MORE_TOPICS_URL)));
        setDraft(cursor.getString(cursor.getColumnIndex(Topics_propertiesTable.DRAFT)));
        setDraft_key(cursor.getString(cursor.getColumnIndex(Topics_propertiesTable.DRAFT_KEY)));
        setDraft_sequence(cursor.getLong(cursor.getColumnIndex(Topics_propertiesTable.DRAFT_SEQUENCE)));

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
        this.values.put(Topics_propertiesTable.ID, id);
    }

    /**
     * Get can_create_topic
     *
     * @return can_create_topic from type boolean
     */
    public boolean getCan_create_topic() {
        return this.can_create_topic;
    }

    /**
     * Set can_create_topic and set content value
     *
     * @param can_create_topic from type boolean
     */
    public void setCan_create_topic(final boolean can_create_topic) {
        this.can_create_topic = can_create_topic;
        this.values.put(Topics_propertiesTable.CAN_CREATE_TOPIC, can_create_topic);
    }

    /**
     * Get more_topics_url
     *
     * @return more_topics_url from type java.lang.String
     */
    public java.lang.String getMore_topics_url() {
        return this.more_topics_url;
    }

    /**
     * Set more_topics_url and set content value
     *
     * @param more_topics_url from type java.lang.String
     */
    public void setMore_topics_url(final java.lang.String more_topics_url) {
        this.more_topics_url = more_topics_url;
        this.values.put(Topics_propertiesTable.MORE_TOPICS_URL, more_topics_url);
    }

    /**
     * Get draft
     *
     * @return draft from type java.lang.String
     */
    public java.lang.String getDraft() {
        return this.draft;
    }

    /**
     * Set draft and set content value
     *
     * @param draft from type java.lang.String
     */
    public void setDraft(final java.lang.String draft) {
        this.draft = draft;
        this.values.put(Topics_propertiesTable.DRAFT, draft);
    }

    /**
     * Get draft_key
     *
     * @return draft_key from type java.lang.String
     */
    public java.lang.String getDraft_key() {
        return this.draft_key;
    }

    /**
     * Set draft_key and set content value
     *
     * @param draft_key from type java.lang.String
     */
    public void setDraft_key(final java.lang.String draft_key) {
        this.draft_key = draft_key;
        this.values.put(Topics_propertiesTable.DRAFT_KEY, draft_key);
    }

    /**
     * Get draft_sequence
     *
     * @return draft_sequence from type long
     */
    public long getDraft_sequence() {
        return this.draft_sequence;
    }

    /**
     * Set draft_sequence and set content value
     *
     * @param draft_sequence from type long
     */
    public void setDraft_sequence(final long draft_sequence) {
        this.draft_sequence = draft_sequence;
        this.values.put(Topics_propertiesTable.DRAFT_SEQUENCE, draft_sequence);
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
