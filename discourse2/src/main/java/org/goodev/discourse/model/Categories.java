package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.CategoriesTable;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class Categories {

    private final ContentValues values = new ContentValues();
    private Long id;
    private long uid;
    private java.lang.String name;
    private java.lang.String color;
    private java.lang.String text_color;
    private java.lang.String slug;
    private long topic_count;
    private long topics_week;
    private long topics_month;
    private long topics_year;
    private java.lang.String description;
    private java.lang.String description_excerpt;
    private java.lang.String featured_user_ids;
    private java.lang.String featured_topics_ids;
    private java.lang.String topic_url;
    private long hotness;
    private boolean read_restricted;
    private java.lang.String permission;
    private java.lang.String available_groups;
    private long auto_close_days;

    public Categories() {
    }

    public Categories(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(CategoriesTable.ID)));
        setUid(cursor.getLong(cursor.getColumnIndex(CategoriesTable.UID)));
        setName(cursor.getString(cursor.getColumnIndex(CategoriesTable.NAME)));
        setColor(cursor.getString(cursor.getColumnIndex(CategoriesTable.COLOR)));
        setText_color(cursor.getString(cursor.getColumnIndex(CategoriesTable.TEXT_COLOR)));
        setSlug(cursor.getString(cursor.getColumnIndex(CategoriesTable.SLUG)));
        setTopic_count(cursor.getLong(cursor.getColumnIndex(CategoriesTable.TOPIC_COUNT)));
        setTopics_week(cursor.getLong(cursor.getColumnIndex(CategoriesTable.TOPICS_WEEK)));
        setTopics_month(cursor.getLong(cursor.getColumnIndex(CategoriesTable.TOPICS_MONTH)));
        setTopics_year(cursor.getLong(cursor.getColumnIndex(CategoriesTable.TOPICS_YEAR)));
        setDescription(cursor.getString(cursor.getColumnIndex(CategoriesTable.DESCRIPTION)));
        setDescription_excerpt(cursor.getString(cursor.getColumnIndex(CategoriesTable.DESCRIPTION_EXCERPT)));
        setFeatured_user_ids(cursor.getString(cursor.getColumnIndex(CategoriesTable.FEATURED_USER_IDS)));
        setFeatured_topics_ids(cursor.getString(cursor.getColumnIndex(CategoriesTable.FEATURED_TOPICS_IDS)));
        setTopic_url(cursor.getString(cursor.getColumnIndex(CategoriesTable.TOPIC_URL)));
        setHotness(cursor.getLong(cursor.getColumnIndex(CategoriesTable.HOTNESS)));
        setRead_restricted(cursor.isNull(cursor.getColumnIndex(CategoriesTable.READ_RESTRICTED)) ? false : (cursor.getInt(cursor.getColumnIndex(CategoriesTable.READ_RESTRICTED)) != 0));
        setPermission(cursor.getString(cursor.getColumnIndex(CategoriesTable.PERMISSION)));
        setAvailable_groups(cursor.getString(cursor.getColumnIndex(CategoriesTable.AVAILABLE_GROUPS)));
        setAuto_close_days(cursor.getLong(cursor.getColumnIndex(CategoriesTable.AUTO_CLOSE_DAYS)));

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
        this.values.put(CategoriesTable.ID, id);
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
        this.values.put(CategoriesTable.UID, uid);
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
        this.values.put(CategoriesTable.NAME, name);
    }

    /**
     * Get color
     *
     * @return color from type java.lang.String
     */
    public java.lang.String getColor() {
        return this.color;
    }

    /**
     * Set color and set content value
     *
     * @param color from type java.lang.String
     */
    public void setColor(final java.lang.String color) {
        this.color = color;
        this.values.put(CategoriesTable.COLOR, color);
    }

    /**
     * Get text_color
     *
     * @return text_color from type java.lang.String
     */
    public java.lang.String getText_color() {
        return this.text_color;
    }

    /**
     * Set text_color and set content value
     *
     * @param text_color from type java.lang.String
     */
    public void setText_color(final java.lang.String text_color) {
        this.text_color = text_color;
        this.values.put(CategoriesTable.TEXT_COLOR, text_color);
    }

    /**
     * Get slug
     *
     * @return slug from type java.lang.String
     */
    public java.lang.String getSlug() {
        return this.slug;
    }

    /**
     * Set slug and set content value
     *
     * @param slug from type java.lang.String
     */
    public void setSlug(final java.lang.String slug) {
        this.slug = slug;
        this.values.put(CategoriesTable.SLUG, slug);
    }

    /**
     * Get topic_count
     *
     * @return topic_count from type long
     */
    public long getTopic_count() {
        return this.topic_count;
    }

    /**
     * Set topic_count and set content value
     *
     * @param topic_count from type long
     */
    public void setTopic_count(final long topic_count) {
        this.topic_count = topic_count;
        this.values.put(CategoriesTable.TOPIC_COUNT, topic_count);
    }

    /**
     * Get topics_week
     *
     * @return topics_week from type long
     */
    public long getTopics_week() {
        return this.topics_week;
    }

    /**
     * Set topics_week and set content value
     *
     * @param topics_week from type long
     */
    public void setTopics_week(final long topics_week) {
        this.topics_week = topics_week;
        this.values.put(CategoriesTable.TOPICS_WEEK, topics_week);
    }

    /**
     * Get topics_month
     *
     * @return topics_month from type long
     */
    public long getTopics_month() {
        return this.topics_month;
    }

    /**
     * Set topics_month and set content value
     *
     * @param topics_month from type long
     */
    public void setTopics_month(final long topics_month) {
        this.topics_month = topics_month;
        this.values.put(CategoriesTable.TOPICS_MONTH, topics_month);
    }

    /**
     * Get topics_year
     *
     * @return topics_year from type long
     */
    public long getTopics_year() {
        return this.topics_year;
    }

    /**
     * Set topics_year and set content value
     *
     * @param topics_year from type long
     */
    public void setTopics_year(final long topics_year) {
        this.topics_year = topics_year;
        this.values.put(CategoriesTable.TOPICS_YEAR, topics_year);
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
        this.values.put(CategoriesTable.DESCRIPTION, description);
    }

    /**
     * Get description_excerpt
     *
     * @return description_excerpt from type java.lang.String
     */
    public java.lang.String getDescription_excerpt() {
        return this.description_excerpt;
    }

    /**
     * Set description_excerpt and set content value
     *
     * @param description_excerpt from type java.lang.String
     */
    public void setDescription_excerpt(final java.lang.String description_excerpt) {
        this.description_excerpt = description_excerpt;
        this.values.put(CategoriesTable.DESCRIPTION_EXCERPT, description_excerpt);
    }

    /**
     * Get featured_user_ids
     *
     * @return featured_user_ids from type java.lang.String
     */
    public java.lang.String getFeatured_user_ids() {
        return this.featured_user_ids;
    }

    /**
     * Set featured_user_ids and set content value
     *
     * @param featured_user_ids from type java.lang.String
     */
    public void setFeatured_user_ids(final java.lang.String featured_user_ids) {
        this.featured_user_ids = featured_user_ids;
        this.values.put(CategoriesTable.FEATURED_USER_IDS, featured_user_ids);
    }

    /**
     * Get featured_topics_ids
     *
     * @return featured_topics_ids from type java.lang.String
     */
    public java.lang.String getFeatured_topics_ids() {
        return this.featured_topics_ids;
    }

    /**
     * Set featured_topics_ids and set content value
     *
     * @param featured_topics_ids from type java.lang.String
     */
    public void setFeatured_topics_ids(final java.lang.String featured_topics_ids) {
        this.featured_topics_ids = featured_topics_ids;
        this.values.put(CategoriesTable.FEATURED_TOPICS_IDS, featured_topics_ids);
    }

    /**
     * Get topic_url
     *
     * @return topic_url from type java.lang.String
     */
    public java.lang.String getTopic_url() {
        return this.topic_url;
    }

    /**
     * Set topic_url and set content value
     *
     * @param topic_url from type java.lang.String
     */
    public void setTopic_url(final java.lang.String topic_url) {
        this.topic_url = topic_url;
        this.values.put(CategoriesTable.TOPIC_URL, topic_url);
    }

    /**
     * Get hotness
     *
     * @return hotness from type long
     */
    public long getHotness() {
        return this.hotness;
    }

    /**
     * Set hotness and set content value
     *
     * @param hotness from type long
     */
    public void setHotness(final long hotness) {
        this.hotness = hotness;
        this.values.put(CategoriesTable.HOTNESS, hotness);
    }

    /**
     * Get read_restricted
     *
     * @return read_restricted from type boolean
     */
    public boolean getRead_restricted() {
        return this.read_restricted;
    }

    /**
     * Set read_restricted and set content value
     *
     * @param read_restricted from type boolean
     */
    public void setRead_restricted(final boolean read_restricted) {
        this.read_restricted = read_restricted;
        this.values.put(CategoriesTable.READ_RESTRICTED, read_restricted);
    }

    /**
     * Get permission
     *
     * @return permission from type java.lang.String
     */
    public java.lang.String getPermission() {
        return this.permission;
    }

    /**
     * Set permission and set content value
     *
     * @param permission from type java.lang.String
     */
    public void setPermission(final java.lang.String permission) {
        this.permission = permission;
        this.values.put(CategoriesTable.PERMISSION, permission);
    }

    /**
     * Get available_groups
     *
     * @return available_groups from type java.lang.String
     */
    public java.lang.String getAvailable_groups() {
        return this.available_groups;
    }

    /**
     * Set available_groups and set content value
     *
     * @param available_groups from type java.lang.String
     */
    public void setAvailable_groups(final java.lang.String available_groups) {
        this.available_groups = available_groups;
        this.values.put(CategoriesTable.AVAILABLE_GROUPS, available_groups);
    }

    /**
     * Get auto_close_days
     *
     * @return auto_close_days from type long
     */
    public long getAuto_close_days() {
        return this.auto_close_days;
    }

    /**
     * Set auto_close_days and set content value
     *
     * @param auto_close_days from type long
     */
    public void setAuto_close_days(final long auto_close_days) {
        this.auto_close_days = auto_close_days;
        this.values.put(CategoriesTable.AUTO_CLOSE_DAYS, auto_close_days);
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
