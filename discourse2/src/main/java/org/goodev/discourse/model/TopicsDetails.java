package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.TopicsDetailsTable;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class TopicsDetails {

    private final ContentValues values = new ContentValues();
    private Long id;
    private long auto_close_at;
    private long created_by_uid;
    private java.lang.String created_by_username;
    private java.lang.String created_by_avatar_template;
    private long last_poster_uid;
    private java.lang.String last_poster_username;
    private java.lang.String last_poster_avatar_template;
    private long topic_id;
    private java.lang.String suggested_topics;

    public TopicsDetails() {
    }

    public TopicsDetails(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(TopicsDetailsTable.ID)));
        setAuto_close_at(cursor.getLong(cursor.getColumnIndex(TopicsDetailsTable.AUTO_CLOSE_AT)));
        setCreated_by_uid(cursor.getLong(cursor.getColumnIndex(TopicsDetailsTable.CREATED_BY_UID)));
        setCreated_by_username(cursor.getString(cursor.getColumnIndex(TopicsDetailsTable.CREATED_BY_USERNAME)));
        setCreated_by_avatar_template(cursor.getString(cursor.getColumnIndex(TopicsDetailsTable.CREATED_BY_AVATAR_TEMPLATE)));
        setLast_poster_uid(cursor.getLong(cursor.getColumnIndex(TopicsDetailsTable.LAST_POSTER_UID)));
        setLast_poster_username(cursor.getString(cursor.getColumnIndex(TopicsDetailsTable.LAST_POSTER_USERNAME)));
        setLast_poster_avatar_template(cursor.getString(cursor.getColumnIndex(TopicsDetailsTable.LAST_POSTER_AVATAR_TEMPLATE)));
        setTopic_id(cursor.getLong(cursor.getColumnIndex(TopicsDetailsTable.TOPIC_ID)));
        setSuggested_topics(cursor.getString(cursor.getColumnIndex(TopicsDetailsTable.SUGGESTED_TOPICS)));

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
        this.values.put(TopicsDetailsTable.ID, id);
    }

    /**
     * Get auto_close_at
     *
     * @return auto_close_at from type long
     */
    public long getAuto_close_at() {
        return this.auto_close_at;
    }

    /**
     * Set auto_close_at and set content value
     *
     * @param auto_close_at from type long
     */
    public void setAuto_close_at(final long auto_close_at) {
        this.auto_close_at = auto_close_at;
        this.values.put(TopicsDetailsTable.AUTO_CLOSE_AT, auto_close_at);
    }

    /**
     * Get created_by_uid
     *
     * @return created_by_uid from type long
     */
    public long getCreated_by_uid() {
        return this.created_by_uid;
    }

    /**
     * Set created_by_uid and set content value
     *
     * @param created_by_uid from type long
     */
    public void setCreated_by_uid(final long created_by_uid) {
        this.created_by_uid = created_by_uid;
        this.values.put(TopicsDetailsTable.CREATED_BY_UID, created_by_uid);
    }

    /**
     * Get created_by_username
     *
     * @return created_by_username from type java.lang.String
     */
    public java.lang.String getCreated_by_username() {
        return this.created_by_username;
    }

    /**
     * Set created_by_username and set content value
     *
     * @param created_by_username from type java.lang.String
     */
    public void setCreated_by_username(final java.lang.String created_by_username) {
        this.created_by_username = created_by_username;
        this.values.put(TopicsDetailsTable.CREATED_BY_USERNAME, created_by_username);
    }

    /**
     * Get created_by_avatar_template
     *
     * @return created_by_avatar_template from type java.lang.String
     */
    public java.lang.String getCreated_by_avatar_template() {
        return this.created_by_avatar_template;
    }

    /**
     * Set created_by_avatar_template and set content value
     *
     * @param created_by_avatar_template from type java.lang.String
     */
    public void setCreated_by_avatar_template(final java.lang.String created_by_avatar_template) {
        this.created_by_avatar_template = created_by_avatar_template;
        this.values.put(TopicsDetailsTable.CREATED_BY_AVATAR_TEMPLATE, created_by_avatar_template);
    }

    /**
     * Get last_poster_uid
     *
     * @return last_poster_uid from type long
     */
    public long getLast_poster_uid() {
        return this.last_poster_uid;
    }

    /**
     * Set last_poster_uid and set content value
     *
     * @param last_poster_uid from type long
     */
    public void setLast_poster_uid(final long last_poster_uid) {
        this.last_poster_uid = last_poster_uid;
        this.values.put(TopicsDetailsTable.LAST_POSTER_UID, last_poster_uid);
    }

    /**
     * Get last_poster_username
     *
     * @return last_poster_username from type java.lang.String
     */
    public java.lang.String getLast_poster_username() {
        return this.last_poster_username;
    }

    /**
     * Set last_poster_username and set content value
     *
     * @param last_poster_username from type java.lang.String
     */
    public void setLast_poster_username(final java.lang.String last_poster_username) {
        this.last_poster_username = last_poster_username;
        this.values.put(TopicsDetailsTable.LAST_POSTER_USERNAME, last_poster_username);
    }

    /**
     * Get last_poster_avatar_template
     *
     * @return last_poster_avatar_template from type java.lang.String
     */
    public java.lang.String getLast_poster_avatar_template() {
        return this.last_poster_avatar_template;
    }

    /**
     * Set last_poster_avatar_template and set content value
     *
     * @param last_poster_avatar_template from type java.lang.String
     */
    public void setLast_poster_avatar_template(final java.lang.String last_poster_avatar_template) {
        this.last_poster_avatar_template = last_poster_avatar_template;
        this.values.put(TopicsDetailsTable.LAST_POSTER_AVATAR_TEMPLATE, last_poster_avatar_template);
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
        this.values.put(TopicsDetailsTable.TOPIC_ID, topic_id);
    }

    /**
     * Get suggested_topics
     *
     * @return suggested_topics from type java.lang.String
     */
    public java.lang.String getSuggested_topics() {
        return this.suggested_topics;
    }

    /**
     * Set suggested_topics and set content value
     *
     * @param suggested_topics from type java.lang.String
     */
    public void setSuggested_topics(final java.lang.String suggested_topics) {
        this.suggested_topics = suggested_topics;
        this.values.put(TopicsDetailsTable.SUGGESTED_TOPICS, suggested_topics);
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
