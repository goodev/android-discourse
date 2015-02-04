package org.goodev.discourse.api.data;

import java.io.Serializable;

public class Topic implements Serializable {
    public Long id;
    public java.lang.String fancy_title;
    public java.lang.String title;
    public java.lang.String slug;
    public long posts_count;
    public long reply_count;
    public long highest_post_number;
    public long last_read_post_number;
    public java.lang.String image_url;
    public long created_at;
    public long last_posted_at;
    public boolean bumped;
    public long bumped_at;
    public boolean unseen;
    public boolean pinned;
    public boolean visible;
    public boolean closed;
    public boolean archived;
    public long views;
    public long like_count;
    public boolean has_best_of;
    public java.lang.String archetype;
    public boolean starred;
    public boolean posted;
    public boolean can_edit;
    public String draft;
    public String draft_key;
    public long draft_sequence;
    public Long deleted_by;
    public long deleted_at;
    public long category_id;
    public Category category;

    public Topic() {

    }

    public Topic(Topic t) {
        super();
        this.id = t.id;
        this.fancy_title = t.fancy_title;
        this.title = t.title;
        this.slug = t.slug;
        this.posts_count = t.posts_count;
        this.reply_count = t.reply_count;
        this.highest_post_number = t.highest_post_number;
        this.last_read_post_number = t.last_read_post_number;
        this.image_url = t.image_url;
        this.created_at = t.created_at;
        this.last_posted_at = t.last_posted_at;
        this.bumped = t.bumped;
        this.bumped_at = t.bumped_at;
        this.unseen = t.unseen;
        this.pinned = t.pinned;
        this.visible = t.visible;
        this.closed = t.closed;
        this.archived = t.archived;
        this.views = t.views;
        this.like_count = t.like_count;
        this.has_best_of = t.has_best_of;
        this.archetype = t.archetype;
        this.category_id = t.category_id;
        this.starred = t.starred;
        this.posted = t.posted;
        this.draft = t.draft;
        this.draft_key = t.draft_key;
        this.draft_sequence = t.draft_sequence;
        this.deleted_by = t.deleted_by;
        this.deleted_at = t.deleted_at;
        this.category = t.category;
        this.can_edit = t.can_edit;
    }

    public long getCategoryId() {
        if (category != null) {
            return category.id;
        }
        return category_id;
    }

}
