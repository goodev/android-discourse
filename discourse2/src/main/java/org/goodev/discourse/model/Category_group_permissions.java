package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.Category_group_permissionsTable;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class Category_group_permissions {

    private final ContentValues values = new ContentValues();
    private Long id;
    private long category_id;
    private long permission_type;
    private java.lang.String group_name;

    public Category_group_permissions() {
    }

    public Category_group_permissions(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(Category_group_permissionsTable.ID)));
        setCategory_id(cursor.getLong(cursor.getColumnIndex(Category_group_permissionsTable.CATEGORY_ID)));
        setPermission_type(cursor.getLong(cursor.getColumnIndex(Category_group_permissionsTable.PERMISSION_TYPE)));
        setGroup_name(cursor.getString(cursor.getColumnIndex(Category_group_permissionsTable.GROUP_NAME)));

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
        this.values.put(Category_group_permissionsTable.ID, id);
    }

    /**
     * Get category_id
     *
     * @return category_id from type long
     */
    public long getCategory_id() {
        return this.category_id;
    }

    /**
     * Set category_id and set content value
     *
     * @param category_id from type long
     */
    public void setCategory_id(final long category_id) {
        this.category_id = category_id;
        this.values.put(Category_group_permissionsTable.CATEGORY_ID, category_id);
    }

    /**
     * Get permission_type
     *
     * @return permission_type from type long
     */
    public long getPermission_type() {
        return this.permission_type;
    }

    /**
     * Set permission_type and set content value
     *
     * @param permission_type from type long
     */
    public void setPermission_type(final long permission_type) {
        this.permission_type = permission_type;
        this.values.put(Category_group_permissionsTable.PERMISSION_TYPE, permission_type);
    }

    /**
     * Get group_name
     *
     * @return group_name from type java.lang.String
     */
    public java.lang.String getGroup_name() {
        return this.group_name;
    }

    /**
     * Set group_name and set content value
     *
     * @param group_name from type java.lang.String
     */
    public void setGroup_name(final java.lang.String group_name) {
        this.group_name = group_name;
        this.values.put(Category_group_permissionsTable.GROUP_NAME, group_name);
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
