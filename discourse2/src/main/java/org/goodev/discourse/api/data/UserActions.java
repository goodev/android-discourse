package org.goodev.discourse.api.data;

import android.text.TextUtils;

import org.goodev.discourse.App;

public class UserActions {
    public int action_type;
    public long created_at;
    public String excerpt;
    public String avatar_template;
    /**
     * 发起操作的人的头像 比如 点击赞的用户头像
     */
    public String acting_avatar_template;
    public String slug;
    public long topic_id;
    public long target_user_id;
    public String target_name;
    public String target_username;
    public long post_number;
    public long reply_to_post_number;
    public String username;
    public String name;
    public long user_id;
    public String acting_username;
    public String acting_name;
    public long acting_user_id;
    public String title;
    public boolean deleted;
    public boolean hidden;
    public boolean moderator_action;

    public boolean isYou() {
        if (TextUtils.isEmpty(username)) {
            return false;
        }
        return username.equals(App.getUsername());
    }

}
