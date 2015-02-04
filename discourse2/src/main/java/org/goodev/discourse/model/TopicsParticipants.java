package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.TopicsParticipantsTable;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class TopicsParticipants {

    private final ContentValues values = new ContentValues();
    private Long id;
    private long topic_id;
    private long uid;
    private java.lang.String username;
    private java.lang.String avatar_template;
    private long post_count;

    public TopicsParticipants() {
    }

    public TopicsParticipants(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(TopicsParticipantsTable.ID)));
        setTopic_id(cursor.getLong(cursor.getColumnIndex(TopicsParticipantsTable.TOPIC_ID)));
        setUid(cursor.getLong(cursor.getColumnIndex(TopicsParticipantsTable.UID)));
        setUsername(cursor.getString(cursor.getColumnIndex(TopicsParticipantsTable.USERNAME)));
        setAvatar_template(cursor.getString(cursor.getColumnIndex(TopicsParticipantsTable.AVATAR_TEMPLATE)));
        setPost_count(cursor.getLong(cursor.getColumnIndex(TopicsParticipantsTable.POST_COUNT)));

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
        this.values.put(TopicsParticipantsTable.ID, id);
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
        this.values.put(TopicsParticipantsTable.TOPIC_ID, topic_id);
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
        this.values.put(TopicsParticipantsTable.UID, uid);
    }

    /**
     * Get username
     *
     * @return username from type java.lang.String
     */
    public java.lang.String getUsername() {
        return this.username;
    }

    /**
     * Set username and set content value
     *
     * @param username from type java.lang.String
     */
    public void setUsername(final java.lang.String username) {
        this.username = username;
        this.values.put(TopicsParticipantsTable.USERNAME, username);
    }

    /**
     * Get avatar_template
     *
     * @return avatar_template from type java.lang.String
     */
    public java.lang.String getAvatar_template() {
        return this.avatar_template;
    }

    /**
     * Set avatar_template and set content value
     *
     * @param avatar_template from type java.lang.String
     */
    public void setAvatar_template(final java.lang.String avatar_template) {
        this.avatar_template = avatar_template;
        this.values.put(TopicsParticipantsTable.AVATAR_TEMPLATE, avatar_template);
    }

    /**
     * Get post_count
     *
     * @return post_count from type long
     */
    public long getPost_count() {
        return this.post_count;
    }

    /**
     * Set post_count and set content value
     *
     * @param post_count from type long
     */
    public void setPost_count(final long post_count) {
        this.post_count = post_count;
        this.values.put(TopicsParticipantsTable.POST_COUNT, post_count);
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
