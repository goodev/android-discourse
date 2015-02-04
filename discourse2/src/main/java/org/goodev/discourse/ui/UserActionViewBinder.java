package org.goodev.discourse.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.data.UserActions;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.Utils;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class UserActionViewBinder {

    public static void bindActivityView(View view, UserActions action, ImageLoader imageLoader, boolean b) {
        Holder h = (Holder) view.getTag();
        if (h == null) {
            h = new Holder();
            view.setTag(h);

            h.mContent = (HtmlTextView) view.findViewById(R.id.post_content);
            h.mContent.setImageLoader(imageLoader);
            h.mPostTime = (TextView) view.findViewById(R.id.post_time);
            h.mText1 = (TextView) view.findViewById(R.id.text1);
            h.mText2 = (TextView) view.findViewById(R.id.text2);
            h.mText3 = (TextView) view.findViewById(R.id.text3);
            h.mTopicTitle = (TextView) view.findViewById(R.id.topic_title);
            h.mUserIcon = (ImageView) view.findViewById(R.id.user_icon);
        }

        h.mTopicTitle.setText(action.title);
        final Context ctx = view.getContext();
        int size = Api.AVATAR_SIZE_BIG;
        if (!TextUtils.isEmpty(action.avatar_template)) {
            String iconUrl = Utils.getAvatarUrl(action.avatar_template, size);
            imageLoader.get(iconUrl, h.mUserIcon);
        }
        h.mContent.setHtmlFromString(action.excerpt);
        h.mPostTime.setText(Utils.formatPostTime(action.created_at));
        switch (action.action_type) {
            case UserActionsFragment.TYPE_REPLY:
            case UserActionsFragment.TYPE_QUOTES:
            case UserActionsFragment.TYPE_MENTIONS:
                if (action.isYou()) {
                    h.mText1.setText(R.string.user_actions_you);
                } else {
                    h.mText1.setText(action.name);
                }
                h.mText2.setText(R.string.uc_reply);
                if (action.reply_to_post_number == 0) {
                    h.mText3.setText(R.string.uc_the_topic);
                } else {
                    h.mText3.setText(ctx.getString(R.string.uc_reply_number, action.reply_to_post_number));
                }
                break;

            default:
                if (action.isYou()) {
                    h.mText1.setText(R.string.user_actions_you);
                    h.mText2.setText(R.string.uc_posted);
                    h.mText3.setText(R.string.uc_the_topic);
                } else {
                    h.mText1.setText(R.string.uc_posted_by);
                    h.mText2.setText(action.name);
                }
                break;
        }
    }

    private static class Holder {
        private ImageView mUserIcon;
        private TextView mPostTime;
        private TextView mTopicTitle;
        private TextView mText1;
        private TextView mText2;
        private TextView mText3;
        private HtmlTextView mContent;
    }

}
