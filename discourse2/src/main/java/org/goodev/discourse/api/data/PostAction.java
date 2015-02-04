package org.goodev.discourse.api.data;

/**
 * <pre>
 * id  2  like : can_act 是否可以喜欢；acted 是否喜欢了；can_undo喜欢了是否可以取消喜欢
 *
 * 获取喜欢的人列表： GET /post_actions/users?id=694&post_action_type_id=2
 *
 *
 * id 3: flag: 你报告它偏离主题. 撤销报告.  POST /post_actions 参数：id=701&post_action_type_id=3
 * id 4: flag: 你报告它为不当内容 id=701&post_action_type_id=4
 * id 8: flag :  你报告它为垃圾信息. 撤销报告. id=701&post_action_type_id=8
 * id 7: flag ： 你向版主报告了它. id=701&post_action_type_id=7&message=内容：
 * id 6: flag: 通知发帖人  id=701&post_action_type_id=6&message=内容
 *
 * 撤销Flag： DELETE /post_actions/701
 *
 * 添加书签：PUT /posts/701/bookmark 参数 bookmarked=true
 * 取消书签：PUT /posts/701/bookmark 参数 bookmarked=false
 *
 *
 *
 * </pre>
 */
public class PostAction {
    public Long id;
    public long count;
    public boolean hidden;
    public boolean can_act;
    public boolean can_undo;
    public boolean can_clear_flags;
    public boolean acted;
}
