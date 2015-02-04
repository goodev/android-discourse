package org.goodev.discourse.api.data;

import android.database.Cursor;

import org.goodev.discourse.database.tables.CategoriesTable;

import java.io.Serializable;

public class Category implements Serializable {
    public Long id;
    public java.lang.String name;
    public java.lang.String color;
    public java.lang.String text_color;
    public java.lang.String slug;
    public long topic_count;
    public java.lang.String topic_url;
    public java.lang.String description;
    public long hotness;
    public boolean secure;

    public long topics_week;
    public long topics_month;
    public long topics_year;
    public java.lang.String description_excerpt;
    public long[] featured_user_ids;
    public boolean isFooter;

    public Category() {
    }

    public Category(final Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(CategoriesTable.UID));
        name = cursor.getString(cursor.getColumnIndex(CategoriesTable.NAME));
        color = cursor.getString(cursor.getColumnIndex(CategoriesTable.COLOR));
        text_color = cursor.getString(cursor.getColumnIndex(CategoriesTable.TEXT_COLOR));
        slug = cursor.getString(cursor.getColumnIndex(CategoriesTable.SLUG));
        topic_count = cursor.getLong(cursor.getColumnIndex(CategoriesTable.TOPIC_COUNT));
        topics_week = cursor.getLong(cursor.getColumnIndex(CategoriesTable.TOPICS_WEEK));
        topics_month = cursor.getLong(cursor.getColumnIndex(CategoriesTable.TOPICS_MONTH));
        topics_year = cursor.getLong(cursor.getColumnIndex(CategoriesTable.TOPICS_YEAR));
        description = cursor.getString(cursor.getColumnIndex(CategoriesTable.DESCRIPTION));
        description_excerpt = cursor.getString(cursor.getColumnIndex(CategoriesTable.DESCRIPTION_EXCERPT));
        topic_url = cursor.getString(cursor.getColumnIndex(CategoriesTable.TOPIC_URL));
        hotness = cursor.getLong(cursor.getColumnIndex(CategoriesTable.HOTNESS));

    }

    public Category(Category c, boolean isFooter) {
        this.id = c.id;
        this.name = c.name;
        this.color = c.color;
        this.text_color = c.text_color;
        this.slug = c.slug;
        this.topic_count = c.topic_count;
        this.topic_url = c.topic_url;
        this.hotness = c.hotness;
        this.secure = c.secure;
        this.topics_week = c.topics_week;
        this.topics_month = c.topics_month;
        this.topics_year = c.topics_year;
        this.description = c.description;
        this.description_excerpt = c.description_excerpt;
        this.featured_user_ids = c.featured_user_ids;
        this.isFooter = isFooter;
    }
}
