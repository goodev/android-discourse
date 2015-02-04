package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.SiteTable;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class Site {

    private final ContentValues values = new ContentValues();
    private Long id;
    private java.lang.String url;
    private java.lang.String title;
    private long updatetime;

    public Site() {
    }

    public Site(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(SiteTable.ID)));
        setUrl(cursor.getString(cursor.getColumnIndex(SiteTable.URL)));
        setTitle(cursor.getString(cursor.getColumnIndex(SiteTable.TITLE)));
        setUpdateTime(cursor.getLong(cursor.getColumnIndex(SiteTable.UPDATETIME)));

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
        this.values.put(SiteTable.ID, id);
    }

    /**
     * Get url
     *
     * @return url from type java.lang.String
     */
    public java.lang.String getUrl() {
        return this.url;
    }

    /**
     * Set url and set content value
     *
     * @param url from type java.lang.String
     */
    public void setUrl(final java.lang.String url) {
        this.url = url;
        this.values.put(SiteTable.URL, url);
    }

    /**
     * Get title
     *
     * @return title from type java.lang.String
     */
    public java.lang.String getTitle() {
        return this.title;
    }

    /**
     * Set title and set content value
     *
     * @param title from type java.lang.String
     */
    public void setTitle(final java.lang.String title) {
        this.title = title;
        this.values.put(SiteTable.TITLE, title);
    }

    /**
     * Get updatetime
     *
     * @return updatetime from type long
     */
    public long getUpdateTime() {
        return this.updatetime;
    }

    /**
     * Set updatetime and set content value
     *
     * @param updatetime from type long
     */
    public void setUpdateTime(final long updatetime) {
        this.updatetime = updatetime;
        this.values.put(SiteTable.UPDATETIME, updatetime);
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
