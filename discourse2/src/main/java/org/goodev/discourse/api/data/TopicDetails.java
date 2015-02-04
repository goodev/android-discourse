package org.goodev.discourse.api.data;

public class TopicDetails {
    public long auto_close_at;
    public long notification_level;
    public long notifications_reason_id;
    public boolean can_create_post;
    public boolean can_reply_as_new_topic;
    public boolean can_invite_to;
    public boolean can_edit;
    public User created_by;
    public User last_poster;
    public User[] participants;
    public Topic[] suggested_topics;
    public Links[] links;

    public TopicDetails() {
    }

    public TopicDetails(TopicDetails td) {
        this.auto_close_at = td.auto_close_at;
        this.notification_level = td.notification_level;
        this.notifications_reason_id = td.notifications_reason_id;
        this.can_create_post = td.can_create_post;
        this.can_reply_as_new_topic = td.can_reply_as_new_topic;
        this.created_by = td.created_by;
        this.last_poster = td.last_poster;
        this.participants = td.participants;
        this.suggested_topics = td.suggested_topics;
        this.links = td.links;
        this.can_edit = td.can_edit;
        this.can_invite_to = td.can_invite_to;
    }

}
