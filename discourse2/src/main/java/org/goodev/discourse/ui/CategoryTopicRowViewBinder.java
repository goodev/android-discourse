package org.goodev.discourse.ui;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.Categories;
import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.api.data.User;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.Utils;

public class CategoryTopicRowViewBinder {

    public static void bindTopicItemView(View view, Topic t) {
        TopicViewHolder h = (TopicViewHolder) view.getTag();
        if (h == null) {
            h = new TopicViewHolder();
            view.setTag(h);

            h.mFirstTime = (TextView) view.findViewById(R.id.topic_first_time);
            h.mPostsCount = (TextView) view.findViewById(R.id.topic_posts_count);
            h.mTitle = (TextView) view.findViewById(R.id.topic_title);
        }

        h.mFirstTime.setText(Utils.formatPostTime(t.created_at));
        h.mPostsCount.setText(t.posts_count + "");
        h.mTitle.setText(t.title);
    }

    public static void bindCategoryFooterView(View view, Category c) {
        CategoryFooterViewHolder h = (CategoryFooterViewHolder) view.getTag();
        if (h == null) {
            h = new CategoryFooterViewHolder();
            view.setTag(h);

            h.mTopicsCount = (TextView) view.findViewById(R.id.category_topics_count);
            h.mViewAllTopics = (TextView) view.findViewById(R.id.category_view_all_topics);
        }
        final Context ctx = view.getContext();
        if (ctx == null) {
            return;
        }
        String count = ctx.getString(R.string.category_topics_count, c.topics_year, c.topics_month, c.topics_week);
        h.mTopicsCount.setText(count);
        h.mViewAllTopics.setText(ctx.getString(R.string.category_view_all, c.topic_count));

    }

    public static void bindCategoryHeaderView(ImageLoader imageLoader, View view, Category c, Categories categories) {
        CategoryHeaderViewHolder h = (CategoryHeaderViewHolder) view.getTag();
        if (h == null) {
            h = new CategoryHeaderViewHolder();
            view.setTag(h);

            h.mName = (TextView) view.findViewById(R.id.category_name);
            h.mDes = (TextView) view.findViewById(R.id.category_des);
            h.mUsersLayout = (LinearLayout) view.findViewById(R.id.participants);
        }

        h.mName.setText(c.name);
        int color = Utils.parseColor(c.color);
        int tc = Utils.parseColor(c.text_color);
        if (color == tc) {
            tc = Color.WHITE;
        }
        h.mName.setBackgroundColor(color);
        h.mName.setTextColor(tc);

        if (TextUtils.isEmpty(c.description_excerpt) || "null".equals(c.description_excerpt)) {
            h.mDes.setText("");
        } else {
            h.mDes.setText(c.description_excerpt);
        }

        long[] featured_user_ids = c.featured_user_ids;
        final LinearLayout layout = h.mUsersLayout;
        layout.removeAllViews();
        if (featured_user_ids != null && featured_user_ids.length > 0) {
            final Context ctx = view.getContext();
            LayoutInflater inflater = LayoutInflater.from(ctx);

            for (int i = 0; i < featured_user_ids.length; i++) {
                User u = categories.getUser(featured_user_ids[i]);
                int maxSize = Api.AVATAR_SIZE_SMALL;
                String iconUrl = null;
                if (u != null) {
                    ImageView iv = (ImageView) inflater.inflate(R.layout.topic_participant_icon, layout, false);
                    iconUrl = Utils.getAvatarUrl(u.avatar_template, maxSize);
                    imageLoader.get(iconUrl, iv);
                    layout.addView(iv);
                    iv.setTag(R.id.poster_name, u.username);
                    iv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopup(v);
                        }
                    });
                }
            }
        }
    }

    public static void showPopup(View v) {
        final Context c = v.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);
        TextView contentView = (TextView) inflater.inflate(R.layout.participant_popup_item, null);
        contentView.setText(v.getTag(R.id.poster_name).toString());
        PopupWindow popup = new PopupWindow(c);
        popup.setContentView(contentView);
        popup.setOutsideTouchable(true);
        popup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.poster_popup_bg));
        popup.showAsDropDown(v);
    }

    private static class TopicViewHolder {
        TextView mFirstTime;
        TextView mPostsCount;
        TextView mTitle;
    }

    private static class CategoryFooterViewHolder {
        TextView mTopicsCount;
        TextView mViewAllTopics;
    }

    private static class CategoryHeaderViewHolder {
        TextView mName;
        LinearLayout mUsersLayout;
        TextView mDes;
    }

}
