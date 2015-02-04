package org.goodev.discourse.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.LatestTopics;
import org.goodev.discourse.api.data.Category;
import org.goodev.discourse.api.data.Topic;
import org.goodev.discourse.api.data.TopicPoster;
import org.goodev.discourse.api.data.User;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.Utils;

import java.util.ArrayList;

public class TopicRowViewBinder {

    public static void bindItemView(View view, Topic t, ImageLoader imageLoader, boolean b, LatestTopics lt, OnCheckedChangeListener listener) {

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            view.setTag(holder);

            holder.mStatusView = (TextView) view.findViewById(R.id.topic_status_view);
            holder.mCategoryTV = (TextView) view.findViewById(R.id.topic_category);
            // holder.mFirstTimeTV = (TextView) view.findViewById(R.id.topic_first_time);
            holder.mLastTimeTV = (TextView) view.findViewById(R.id.topic_last_time);
            holder.mLikesNumberTV = (TextView) view.findViewById(R.id.topic_likes_number);
            holder.mParticipantsLayout = (LinearLayout) view.findViewById(R.id.participants);
            holder.mPostsNumberTV = (TextView) view.findViewById(R.id.topic_posts_number);
            holder.mStarCB = (CheckBox) view.findViewById(R.id.topic_star);
            holder.mTitleTV = (TextView) view.findViewById(R.id.topic_title);
            holder.mViewsNumberTV = (TextView) view.findViewById(R.id.topic_views_number);
        }
        holder.mStarCB.setOnCheckedChangeListener(null);
        holder.mStarCB.setVisibility(App.isLogin() ? View.VISIBLE : View.INVISIBLE);
        holder.mStarCB.setTag(t);
        int left = 0;
        int right = 0;
        if (t.pinned) {
            right = R.drawable.ic_pin;
        }
        if (t.closed) {
            left = R.drawable.ic_locked;
        }
        holder.mStatusView.setCompoundDrawablesWithIntrinsicBounds(left, 0, right, 0);

        // holder.mFirstTimeTV.setText(Utils.formatPostTime(t.created_at));
        long lastPostTime = t.last_posted_at;
        if (lastPostTime == 0) {
            lastPostTime = t.created_at;
        }
        holder.mLastTimeTV.setText(Utils.formatPostTime(lastPostTime));
        holder.mStarCB.setChecked(t.starred);
        // 设置完默认值后 在设置 listener
        holder.mStarCB.setOnCheckedChangeListener(listener);
        holder.mStarCB.setVisibility(listener == null ? View.GONE : View.VISIBLE);
        Category cat = App.getCategory(t.getCategoryId());
        Utils.setCategoryView(cat, holder.mCategoryTV);

        holder.mLikesNumberTV.setText(String.valueOf(t.like_count));
        holder.mPostsNumberTV.setText(String.valueOf(t.posts_count));
        holder.mViewsNumberTV.setText(Utils.formatTopicViews(t.views));
        if (t.unseen) {
            holder.mTitleTV.setText(Utils.getNewTitleSpan(t.title));
        } else {
            holder.mTitleTV.setText(t.title);
        }

        // 设置参与者
        if (lt == null) {
            return;
        }
        final LinearLayout layout = holder.mParticipantsLayout;
        layout.removeAllViews();
        ArrayList<TopicPoster> posters = lt.getPosters(t.id);
        if (posters != null) {
            final Context ctx = view.getContext();
            int maxSize = Api.AVATAR_SIZE_SMALL;
            String iconUrl = null;
            LayoutInflater inflater = LayoutInflater.from(ctx);
            for (TopicPoster poster : posters) {
                User u = lt.getUser(poster.user_id);
                if (u != null) {
                    ImageView iv = (ImageView) inflater.inflate(R.layout.topic_participant_icon, layout, false);
                    iconUrl = Utils.getAvatarUrl(u.avatar_template, maxSize);
                    imageLoader.get(iconUrl, iv);
                    layout.addView(iv);
                    iv.setTag(R.id.poster_des, poster.description);
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
        contentView.setText(c.getString(R.string.participant_popup_text, v.getTag(R.id.poster_name), v.getTag(R.id.poster_des)));
        PopupWindow popup = new PopupWindow(c);
        popup.setContentView(contentView);
        popup.setOutsideTouchable(true);
        popup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.poster_popup_bg));
        popup.showAsDropDown(v);
    }

    private static class ViewHolder {
        // private TextView mFirstTimeTV;
        private TextView mLastTimeTV;
        private CheckBox mStarCB;
        private TextView mTitleTV;
        private TextView mCategoryTV;
        private LinearLayout mParticipantsLayout;
        private TextView mPostsNumberTV;
        private TextView mLikesNumberTV;
        private TextView mViewsNumberTV;
        private TextView mStatusView;
    }

}
