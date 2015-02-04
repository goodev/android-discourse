package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.UserInfoTable;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.MCrypt;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class UserInfo {

    private final ContentValues values = new ContentValues();
    private Long id;
    private java.lang.String name;
    private java.lang.String password;
    private long siteid;
    private java.lang.String siteurl;

    public UserInfo() {
    }

    public UserInfo(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(UserInfoTable.ID)));
        setName(cursor.getString(cursor.getColumnIndex(UserInfoTable.NAME)));
        String pwd = cursor.getString(cursor.getColumnIndex(UserInfoTable.PASSWORD));
        String cleartext;
        try {
            cleartext = new String(new MCrypt().decrypt(pwd)).trim();
            L.i("p: %s  c: %s", cleartext, pwd);
        } catch (Exception e) {
            L.e(pwd);
            L.e(e);
            cleartext = pwd;
        }
        setPassword(cleartext);
        setSiteId(cursor.getLong(cursor.getColumnIndex(UserInfoTable.SITEID)));
        setSiteUrl(cursor.getString(cursor.getColumnIndex(UserInfoTable.SITEURL)));

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
        this.values.put(UserInfoTable.ID, id);
    }

    /**
     * Get name
     *
     * @return name from type java.lang.String
     */
    public java.lang.String getName() {
        return this.name;
    }

    /**
     * Set name and set content value
     *
     * @param name from type java.lang.String
     */
    public void setName(final java.lang.String name) {
        this.name = name;
        this.values.put(UserInfoTable.NAME, name);
    }

    /**
     * Get password
     *
     * @return password from type java.lang.String
     */
    public java.lang.String getPassword() {
        return this.password;
    }

    /**
     * Set password and set content value
     *
     * @param password from type java.lang.String
     */
    public void setPassword(final java.lang.String password) {
        this.password = password;
        this.values.put(UserInfoTable.PASSWORD, password);
    }

    /**
     * Get siteid
     *
     * @return siteid from type long
     */
    public long getSiteId() {
        return this.siteid;
    }

    /**
     * Set siteid and set content value
     *
     * @param siteid from type long
     */
    public void setSiteId(final long siteid) {
        this.siteid = siteid;
        this.values.put(UserInfoTable.SITEID, siteid);
    }

    /**
     * Get siteurl
     *
     * @return siteurl from type java.lang.String
     */
    public java.lang.String getSiteUrl() {
        return this.siteurl;
    }

    /**
     * Set siteurl and set content value
     *
     * @param siteurl from type java.lang.String
     */
    public void setSiteUrl(final java.lang.String siteurl) {
        this.siteurl = siteurl;
        this.values.put(UserInfoTable.SITEURL, siteurl);
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
