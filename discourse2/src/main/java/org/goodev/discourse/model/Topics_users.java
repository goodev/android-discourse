package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.Topics_usersTable;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class Topics_users {

    private final ContentValues values = new ContentValues();
    private Long id;
    private long uid;
    private java.lang.String username;
    private java.lang.String avatar_template;

    public Topics_users() {
    }

    public Topics_users(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(Topics_usersTable.ID)));
        setUid(cursor.getLong(cursor.getColumnIndex(Topics_usersTable.UID)));
        setUsername(cursor.getString(cursor.getColumnIndex(Topics_usersTable.USERNAME)));
        setAvatar_template(cursor.getString(cursor.getColumnIndex(Topics_usersTable.AVATAR_TEMPLATE)));

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
        this.values.put(Topics_usersTable.ID, id);
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
        this.values.put(Topics_usersTable.UID, uid);
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
        this.values.put(Topics_usersTable.USERNAME, username);
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
        this.values.put(Topics_usersTable.AVATAR_TEMPLATE, avatar_template);
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
