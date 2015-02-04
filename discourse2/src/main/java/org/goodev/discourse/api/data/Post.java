package org.goodev.discourse.api.data;

public class Post {
    private static final int LIKE = 2;
    private static final int FIVE = 5;
    private static final int OFF_TOPIC = 3;
    private static final int INAPPROPRIATE = 4;
    private static final int NOTIFY_POSTER = 6;
    private static final int NOTIFY_MODERATORS = 7;
    private static final int SPAM = 8;
    public Long id;
    public String name;
    public String username;
    public String avatar_template;
    public long created_at;
    /**
     * 文章 html 内容
     */
    public String cooked;
    public long post_number;
    public long post_type;
    public long updated_at;
    public long reply_count;
    public long reply_to_post_number;
    public long quote_count;
    public long avg_time;
    public long incoming_link_count;
    public long reads;
    public float score;
    public boolean yours;
    public String topic_slug;
    public Long topic_id;
    public String display_username;
    public long version;
    public boolean can_edit;
    public boolean can_delete;
    public boolean can_recover;
    public boolean read;
    public String raw;
    public String user_title;
    public boolean moderator;
    public boolean staff;
    public Long user_id;
    public boolean hidden;
    public long hidden_reason_id;
    public long trust_level;
    public long deleted_at;
    public boolean user_deleted;
    public boolean bookmarked;

    public Links[] link_counts;
    public PostAction[] actions_summary;

    public int getLinksSize() {
        if (link_counts == null) {
            return 0;
        }
        return link_counts.length;
    }

    public boolean isLikeCanAct() {
        PostAction like = getLikeAction();
        return like.can_act;
    }

    public boolean isLikeCanUndo() {
        PostAction like = getLikeAction();
        return like.can_undo;
    }

    public boolean isLikeActed() {
        PostAction like = getLikeAction();
        return like.acted;
    }

    /**
     * 更新喜欢状态，
     *
     * @param liked 是否已经喜欢成功
     */
    public void updateLikeAction(boolean liked) {
        PostAction like = getLikeAction();
        if (liked) {
            like.can_undo = true;
            like.acted = true;
        } else {
            like.can_undo = false;
            like.acted = false;
            like.can_act = true;
        }
    }

    public PostAction getLikeAction() {
        PostAction[] actions = actions_summary;
        for (PostAction action : actions) {
            if (action.id.intValue() == LIKE) {
                return action;
            }
        }
        return null;
    }

    public PostAction getAction(int type) {
        PostAction[] actions = actions_summary;
        for (PostAction action : actions) {
            if (action.id.intValue() == type) {
                return action;
            }
        }
        return null;
    }

    public boolean showFlag() {
        PostAction[] actions = actions_summary;
        for (PostAction action : actions) {
            int type = action.id.intValue();
            if (type != LIKE && type != FIVE) {
                if (action.can_act) {
                    return true;
                }
            }
        }
        return false;
    }
}
