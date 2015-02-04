package org.goodev.discourse.api.data;

import org.goodev.discourse.utils.SerializableSparseIntArray;

import java.io.Serializable;

public class UserDetails implements Serializable {
    public Long id;
    public String username;
    public String name;
    public String avatar_template;
    public String email;
    public String bio_raw;
    public String bio_cooked;
    public String website;
    public boolean can_edit;
    public boolean can_edit_username;
    public boolean can_edit_email;
    public boolean can_send_private_message_to_user;
    public boolean moderator;
    public boolean admin;
    public String title;
    public String bio_excerpt;
    public int trust_level;
    public String invited_by;
    public long last_posted_at;
    public long last_seen_at;
    public long created_at;
    public SerializableSparseIntArray stats = new SerializableSparseIntArray(13);

}
