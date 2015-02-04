package org.goodev.discourse.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.goodev.discourse.database.tables.TopicsTable;

/**
 * Generated model class for usage in your application, defined by classifiers in ecore diagram
 * <p/>
 * Generated Class. Do not modify!
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 */
public class Topics {

    private final ContentValues values = new ContentValues();
    private Long id;
    private long uid;
    private java.lang.String title;
    private java.lang.String fancy_title;
    private java.lang.String slug;
    private long posts_count;
    private long reply_count;
    private long highest_post_number;
    private java.lang.String image_url;
    private long created_at;
    private long last_posted_at;
    private boolean bumped;
    private long bumped_at;
    private boolean unseen;
    private boolean pinned;
    private boolean visible;
    private boolean closed;
    private boolean archived;
    private java.lang.String draft;
    private java.lang.String draft_key;
    private long draft_sequence;
    private long deleted_by;
    private long views;
    private boolean has_best_of;
    private java.lang.String archetype;
    private long category_id;
    private long deleted_at;
    private boolean is_featured;
    private long like_count;
    private boolean starred;

    public Topics() {
    }

    public Topics(final Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(TopicsTable.ID)));
        setUid(cursor.getLong(cursor.getColumnIndex(TopicsTable.UID)));
        setTitle(cursor.getString(cursor.getColumnIndex(TopicsTable.TITLE)));
        setFancy_title(cursor.getString(cursor.getColumnIndex(TopicsTable.FANCY_TITLE)));
        setSlug(cursor.getString(cursor.getColumnIndex(TopicsTable.SLUG)));
        setPosts_count(cursor.getLong(cursor.getColumnIndex(TopicsTable.POSTS_COUNT)));
        setReply_count(cursor.getLong(cursor.getColumnIndex(TopicsTable.REPLY_COUNT)));
        setHighest_post_number(cursor.getLong(cursor.getColumnIndex(TopicsTable.HIGHEST_POST_NUMBER)));
        setImage_url(cursor.getString(cursor.getColumnIndex(TopicsTable.IMAGE_URL)));
        setCreated_at(cursor.getLong(cursor.getColumnIndex(TopicsTable.CREATED_AT)));
        setLast_posted_at(cursor.getLong(cursor.getColumnIndex(TopicsTable.LAST_POSTED_AT)));
        setBumped(cursor.isNull(cursor.getColumnIndex(TopicsTable.BUMPED)) ? false : (cursor.getInt(cursor.getColumnIndex(TopicsTable.BUMPED)) != 0));
        setBumped_at(cursor.getLong(cursor.getColumnIndex(TopicsTable.BUMPED_AT)));
        setUnseen(cursor.isNull(cursor.getColumnIndex(TopicsTable.UNSEEN)) ? false : (cursor.getInt(cursor.getColumnIndex(TopicsTable.UNSEEN)) != 0));
        setPinned(cursor.isNull(cursor.getColumnIndex(TopicsTable.PINNED)) ? false : (cursor.getInt(cursor.getColumnIndex(TopicsTable.PINNED)) != 0));
        setVisible(cursor.isNull(cursor.getColumnIndex(TopicsTable.VISIBLE)) ? false : (cursor.getInt(cursor.getColumnIndex(TopicsTable.VISIBLE)) != 0));
        setClosed(cursor.isNull(cursor.getColumnIndex(TopicsTable.CLOSED)) ? false : (cursor.getInt(cursor.getColumnIndex(TopicsTable.CLOSED)) != 0));
        setArchived(cursor.isNull(cursor.getColumnIndex(TopicsTable.ARCHIVED)) ? false : (cursor.getInt(cursor.getColumnIndex(TopicsTable.ARCHIVED)) != 0));
        setDraft(cursor.getString(cursor.getColumnIndex(TopicsTable.DRAFT)));
        setDraft_key(cursor.getString(cursor.getColumnIndex(TopicsTable.DRAFT_KEY)));
        setDraft_sequence(cursor.getLong(cursor.getColumnIndex(TopicsTable.DRAFT_SEQUENCE)));
        setDeleted_by(cursor.getLong(cursor.getColumnIndex(TopicsTable.DELETED_BY)));
        setViews(cursor.getLong(cursor.getColumnIndex(TopicsTable.VIEWS)));
        setHas_best_of(cursor.isNull(cursor.getColumnIndex(TopicsTable.HAS_BEST_OF)) ? false : (cursor.getInt(cursor.getColumnIndex(TopicsTable.HAS_BEST_OF)) != 0));
        setArchetype(cursor.getString(cursor.getColumnIndex(TopicsTable.ARCHETYPE)));
        setCategory_id(cursor.getLong(cursor.getColumnIndex(TopicsTable.CATEGORY_ID)));
        setDeleted_at(cursor.getLong(cursor.getColumnIndex(TopicsTable.DELETED_AT)));
        setIs_featured(cursor.isNull(cursor.getColumnIndex(TopicsTable.IS_FEATURED)) ? false : (cursor.getInt(cursor.getColumnIndex(TopicsTable.IS_FEATURED)) != 0));
        setLike_count(cursor.getLong(cursor.getColumnIndex(TopicsTable.LIKE_COUNT)));
        setStarred(cursor.isNull(cursor.getColumnIndex(TopicsTable.STARRED)) ? false : (cursor.getInt(cursor.getColumnIndex(TopicsTable.STARRED)) != 0));

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
        this.values.put(TopicsTable.ID, id);
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
        this.values.put(TopicsTable.UID, uid);
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
        this.values.put(TopicsTable.TITLE, title);
    }

    /**
     * Get fancy_title
     *
     * @return fancy_title from type java.lang.String
     */
    public java.lang.String getFancy_title() {
        return this.fancy_title;
    }

    /**
     * Set fancy_title and set content value
     *
     * @param fancy_title from type java.lang.String
     */
    public void setFancy_title(final java.lang.String fancy_title) {
        this.fancy_title = fancy_title;
        this.values.put(TopicsTable.FANCY_TITLE, fancy_title);
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
        this.values.put(TopicsTable.SLUG, slug);
    }

    /**
     * Get posts_count
     *
     * @return posts_count from type long
     */
    public long getPosts_count() {
        return this.posts_count;
    }

    /**
     * Set posts_count and set content value
     *
     * @param posts_count from type long
     */
    public void setPosts_count(final long posts_count) {
        this.posts_count = posts_count;
        this.values.put(TopicsTable.POSTS_COUNT, posts_count);
    }

    /**
     * Get reply_count
     *
     * @return reply_count from type long
     */
    public long getReply_count() {
        return this.reply_count;
    }

    /**
     * Set reply_count and set content value
     *
     * @param reply_count from type long
     */
    public void setReply_count(final long reply_count) {
        this.reply_count = reply_count;
        this.values.put(TopicsTable.REPLY_COUNT, reply_count);
    }

    /**
     * Get highest_post_number
     *
     * @return highest_post_number from type long
     */
    public long getHighest_post_number() {
        return this.highest_post_number;
    }

    /**
     * Set highest_post_number and set content value
     *
     * @param highest_post_number from type long
     */
    public void setHighest_post_number(final long highest_post_number) {
        this.highest_post_number = highest_post_number;
        this.values.put(TopicsTable.HIGHEST_POST_NUMBER, highest_post_number);
    }

    /**
     * Get image_url
     *
     * @return image_url from type java.lang.String
     */
    public java.lang.String getImage_url() {
        return this.image_url;
    }

    /**
     * Set image_url and set content value
     *
     * @param image_url from type java.lang.String
     */
    public void setImage_url(final java.lang.String image_url) {
        this.image_url = image_url;
        this.values.put(TopicsTable.IMAGE_URL, image_url);
    }

    /**
     * Get created_at
     *
     * @return created_at from type long
     */
    public long getCreated_at() {
        return this.created_at;
    }

    /**
     * Set created_at and set content value
     *
     * @param created_at from type long
     */
    public void setCreated_at(final long created_at) {
        this.created_at = created_at;
        this.values.put(TopicsTable.CREATED_AT, created_at);
    }

    /**
     * Get last_posted_at
     *
     * @return last_posted_at from type long
     */
    public long getLast_posted_at() {
        return this.last_posted_at;
    }

    /**
     * Set last_posted_at and set content value
     *
     * @param last_posted_at from type long
     */
    public void setLast_posted_at(final long last_posted_at) {
        this.last_posted_at = last_posted_at;
        this.values.put(TopicsTable.LAST_POSTED_AT, last_posted_at);
    }

    /**
     * Get bumped
     *
     * @return bumped from type boolean
     */
    public boolean getBumped() {
        return this.bumped;
    }

    /**
     * Set bumped and set content value
     *
     * @param bumped from type boolean
     */
    public void setBumped(final boolean bumped) {
        this.bumped = bumped;
        this.values.put(TopicsTable.BUMPED, bumped);
    }

    /**
     * Get bumped_at
     *
     * @return bumped_at from type long
     */
    public long getBumped_at() {
        return this.bumped_at;
    }

    /**
     * Set bumped_at and set content value
     *
     * @param bumped_at from type long
     */
    public void setBumped_at(final long bumped_at) {
        this.bumped_at = bumped_at;
        this.values.put(TopicsTable.BUMPED_AT, bumped_at);
    }

    /**
     * Get unseen
     *
     * @return unseen from type boolean
     */
    public boolean getUnseen() {
        return this.unseen;
    }

    /**
     * Set unseen and set content value
     *
     * @param unseen from type boolean
     */
    public void setUnseen(final boolean unseen) {
        this.unseen = unseen;
        this.values.put(TopicsTable.UNSEEN, unseen);
    }

    /**
     * Get pinned
     *
     * @return pinned from type boolean
     */
    public boolean getPinned() {
        return this.pinned;
    }

    /**
     * Set pinned and set content value
     *
     * @param pinned from type boolean
     */
    public void setPinned(final boolean pinned) {
        this.pinned = pinned;
        this.values.put(TopicsTable.PINNED, pinned);
    }

    /**
     * Get visible
     *
     * @return visible from type boolean
     */
    public boolean getVisible() {
        return this.visible;
    }

    /**
     * Set visible and set content value
     *
     * @param visible from type boolean
     */
    public void setVisible(final boolean visible) {
        this.visible = visible;
        this.values.put(TopicsTable.VISIBLE, visible);
    }

    /**
     * Get closed
     *
     * @return closed from type boolean
     */
    public boolean getClosed() {
        return this.closed;
    }

    /**
     * Set closed and set content value
     *
     * @param closed from type boolean
     */
    public void setClosed(final boolean closed) {
        this.closed = closed;
        this.values.put(TopicsTable.CLOSED, closed);
    }

    /**
     * Get archived
     *
     * @return archived from type boolean
     */
    public boolean getArchived() {
        return this.archived;
    }

    /**
     * Set archived and set content value
     *
     * @param archived from type boolean
     */
    public void setArchived(final boolean archived) {
        this.archived = archived;
        this.values.put(TopicsTable.ARCHIVED, archived);
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
        this.values.put(TopicsTable.DRAFT, draft);
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
        this.values.put(TopicsTable.DRAFT_KEY, draft_key);
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
        this.values.put(TopicsTable.DRAFT_SEQUENCE, draft_sequence);
    }

    /**
     * Get deleted_by
     *
     * @return deleted_by from type long
     */
    public long getDeleted_by() {
        return this.deleted_by;
    }

    /**
     * Set deleted_by and set content value
     *
     * @param deleted_by from type long
     */
    public void setDeleted_by(final long deleted_by) {
        this.deleted_by = deleted_by;
        this.values.put(TopicsTable.DELETED_BY, deleted_by);
    }

    /**
     * Get views
     *
     * @return views from type long
     */
    public long getViews() {
        return this.views;
    }

    /**
     * Set views and set content value
     *
     * @param views from type long
     */
    public void setViews(final long views) {
        this.views = views;
        this.values.put(TopicsTable.VIEWS, views);
    }

    /**
     * Get has_best_of
     *
     * @return has_best_of from type boolean
     */
    public boolean getHas_best_of() {
        return this.has_best_of;
    }

    /**
     * Set has_best_of and set content value
     *
     * @param has_best_of from type boolean
     */
    public void setHas_best_of(final boolean has_best_of) {
        this.has_best_of = has_best_of;
        this.values.put(TopicsTable.HAS_BEST_OF, has_best_of);
    }

    /**
     * Get archetype
     *
     * @return archetype from type java.lang.String
     */
    public java.lang.String getArchetype() {
        return this.archetype;
    }

    /**
     * Set archetype and set content value
     *
     * @param archetype from type java.lang.String
     */
    public void setArchetype(final java.lang.String archetype) {
        this.archetype = archetype;
        this.values.put(TopicsTable.ARCHETYPE, archetype);
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
        this.values.put(TopicsTable.CATEGORY_ID, category_id);
    }

    /**
     * Get deleted_at
     *
     * @return deleted_at from type long
     */
    public long getDeleted_at() {
        return this.deleted_at;
    }

    /**
     * Set deleted_at and set content value
     *
     * @param deleted_at from type long
     */
    public void setDeleted_at(final long deleted_at) {
        this.deleted_at = deleted_at;
        this.values.put(TopicsTable.DELETED_AT, deleted_at);
    }

    /**
     * Get is_featured
     *
     * @return is_featured from type boolean
     */
    public boolean getIs_featured() {
        return this.is_featured;
    }

    /**
     * Set is_featured and set content value
     *
     * @param is_featured from type boolean
     */
    public void setIs_featured(final boolean is_featured) {
        this.is_featured = is_featured;
        this.values.put(TopicsTable.IS_FEATURED, is_featured);
    }

    /**
     * Get like_count
     *
     * @return like_count from type long
     */
    public long getLike_count() {
        return this.like_count;
    }

    /**
     * Set like_count and set content value
     *
     * @param like_count from type long
     */
    public void setLike_count(final long like_count) {
        this.like_count = like_count;
        this.values.put(TopicsTable.LIKE_COUNT, like_count);
    }

    /**
     * Get starred
     *
     * @return starred from type boolean
     */
    public boolean getStarred() {
        return this.starred;
    }

    /**
     * Set starred and set content value
     *
     * @param starred from type boolean
     */
    public void setStarred(final boolean starred) {
        this.starred = starred;
        this.values.put(TopicsTable.STARRED, starred);
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
