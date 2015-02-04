package org.goodev.discourse.ui;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.api.Api;
import org.goodev.discourse.api.TopicStream;
import org.goodev.discourse.api.data.Post;
import org.goodev.discourse.api.data.PostAction;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

public class PostRowViewBinder {
    private static OnLongClickListener popuplistener = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            showPopup(v);
            return true;
        }
    };

    public static void bindItemView(final Activity a, View view, Post data, ImageLoader imageLoader, TopicStream ts, int position, OnClickListener listener) {
        ViewHolder vh = (ViewHolder) view.getTag();
        if (vh == null) {
            vh = new ViewHolder();
            view.setTag(vh);

            // vh.mBookmark = (ImageView) view.findViewById(R.id.post_bookmark);
            // vh.mFlag = (ImageView) view.findViewById(R.id.post_flag);
            // vh.mImageHsv = (HorizontalScrollView) view.findViewById(R.id.post_image_hsv);
            // vh.mImageLayout = (LinearLayout) view.findViewById(R.id.post_images_layout);
            vh.mLikeContent = (TextView) view.findViewById(R.id.post_like_content);
            // vh.mLinkCount = (TextView) view.findViewById(R.id.post_link_count);
            vh.mModifyCount = (TextView) view.findViewById(R.id.modify_count);
            vh.mPostContent = (HtmlTextView) view.findViewById(R.id.post_content);
            vh.mPostContent.setImageLoader(imageLoader);
            vh.mPostCount = (TextView) view.findViewById(R.id.post_count);
            // vh.mPosterCount = (TextView) view.findViewById(R.id.topic_poster_count);
            vh.mPostTime = (TextView) view.findViewById(R.id.post_time);
            vh.mUserIcon = (ImageView) view.findViewById(R.id.user_icon);
            vh.mUserName = (TextView) view.findViewById(R.id.user_name);
            // vh.mUserTitle = (TextView) view.findViewById(R.id.user_title);
            vh.mViewCount = (TextView) view.findViewById(R.id.view_count);
            vh.mUserLayout = view.findViewById(R.id.post_user_info_layout);
            vh.mUserLayout.setOnClickListener(listener);

            vh.mShare = (ImageView) view.findViewById(R.id.post_share);
            vh.mLike = (ImageView) view.findViewById(R.id.post_like);
            vh.mEdit = (ImageView) view.findViewById(R.id.post_edit);
            vh.mRecover = (ImageView) view.findViewById(R.id.post_recover);
            vh.mReply = (ImageView) view.findViewById(R.id.post_replay);
            vh.mOverflowMenu = view.findViewById(R.id.overflow_menu);
            vh.mShare.setOnClickListener(listener);
            vh.mLike.setOnClickListener(listener);
            vh.mEdit.setOnClickListener(listener);
            vh.mRecover.setOnClickListener(listener);
            vh.mReply.setOnClickListener(listener);
            vh.mOverflowMenu.setOnClickListener(listener);
            vh.mShare.setOnLongClickListener(popuplistener);
            vh.mLike.setOnLongClickListener(popuplistener);
            vh.mEdit.setOnLongClickListener(popuplistener);
            vh.mRecover.setOnLongClickListener(popuplistener);
            vh.mReply.setOnLongClickListener(popuplistener);
            vh.mOverflowMenu.setOnLongClickListener(popuplistener);

        }

        vh.mEdit.setVisibility(data.can_edit ? View.VISIBLE : View.GONE);
        vh.mRecover.setVisibility(data.can_recover ? View.VISIBLE : View.GONE);
        boolean canReply = (!ts.mTopic.closed && App.isLogin());
        vh.mReply.setVisibility(canReply ? View.VISIBLE : View.GONE);

        final Context ctx = view.getContext();
        int size = Api.AVATAR_SIZE_BIG;
        if (!TextUtils.isEmpty(data.avatar_template)) {
            String iconUrl = Utils.getAvatarUrl(data.avatar_template, size);
            imageLoader.get(iconUrl, vh.mUserIcon);
        }
        String nameAndTitle;
        if (!TextUtils.isEmpty(data.user_title) && !Api.NULL.equals(data.user_title)) {
            nameAndTitle = ctx.getString(R.string.post_name_and_title, data.username, data.user_title);
        } else {
            nameAndTitle = data.username;
        }
        vh.mUserLayout.setTag(data.username);

        vh.mUserName.setText(nameAndTitle);
        // vh.mUserTitle.setText(title);
        vh.mPostTime.setText(Utils.formatPostTime(data.created_at));
        int version = (int) (data.version - 1);
        if (version > 0) {
            vh.mModifyCount.setText(String.valueOf(version));
            vh.mModifyCount.setVisibility(View.VISIBLE);
        } else {
            vh.mModifyCount.setVisibility(View.INVISIBLE);
        }
        if (position == 0) {
            vh.mPostCount.setText(String.valueOf(ts.mTopic.posts_count));
            vh.mViewCount.setText(String.valueOf(ts.mTopic.views));
            vh.mPostCount.setVisibility(View.VISIBLE);
            vh.mViewCount.setVisibility(View.VISIBLE);
        } else {
            vh.mPostCount.setVisibility(View.INVISIBLE);
            vh.mViewCount.setVisibility(View.INVISIBLE);
        }

        Document doc = Jsoup.parse(data.cooked);
        Elements elements = doc.select("div a.lightbox");
        ArrayList<String> images = new ArrayList<String>();
        for (Element element : elements) {
            String imgUrl = element.attr("href");
            images.add(imgUrl);
            // TODO 由于完美解决了图片显示问题 ，这里就不用提取图片了吧。。。
            // element.remove();
        }
        Elements videos = doc.select("p iframe");
        StringBuilder sb = new StringBuilder();
        for (Element element : videos) {
            sb.append("<br/>");
            String src = element.attr("src");
            if (src != null && src.startsWith(Utils.SLASH)) {
                sb.append(Utils.AVATAR_HTTP_PREFIX);
            }
            sb.append(element.attr("src"));
            element.remove();
        }
        String html = doc.body().html() + sb.toString();
        if (images.size() > 0) {
            vh.mPostContent.setTag(R.id.poste_image_data, images);
        }
        vh.mPostContent.setHtmlFromString(html);

        // vh.mImageLayout.removeAllViews();
        // if (images.size() > 0) {
        // String[] imgs = new String[images.size()];
        // imgs = images.toArray(imgs);
        // vh.mPostContent.setTag(R.id.poste_image_data, images);

        // size = ctx.getResources().getInteger(R.integer.post_item_img_size);
        // LayoutInflater inflater = LayoutInflater.from(ctx);
        // vh.mImageHsv.setVisibility(View.VISIBLE);
        // int index = 0;
        // for (String img : images) {
        // ImageView iv = (ImageView) inflater.inflate(
        // R.layout.post_image_item, vh.mImageLayout, false);
        // imageLoader.get(img, iv, placeHolder, size, size);
        // vh.mImageLayout.addView(iv);
        // iv.setTag(R.id.poste_image_data, imgs);
        // iv.setTag(R.id.poste_image_index, Integer.valueOf(index));
        // index++;
        // iv.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // final String[] img = (String[]) v.getTag(R.id.poste_image_data);
        // final Integer index = (Integer) v.getTag(R.id.poste_image_index);
        // ActivityUtils.openPhotosActivity(a, index, img);
        // }
        // });
        // }
        // } else {
        // vh.mImageHsv.setVisibility(View.GONE);
        // }

        PostAction like = data.getLikeAction();
        vh.mLikeContent.setVisibility(View.GONE);
        if (like == null || (!like.can_act && !like.can_undo)) {
            vh.mLike.setVisibility(View.GONE);
        } else {
            vh.mLike.setVisibility(View.VISIBLE);
            int resId = like.acted ? R.drawable.ic_heart_checked : R.drawable.ic_heart_black;
            vh.mLike.setImageResource(resId);
            if (like.count > 0) {
                vh.mLikeContent.setText(ctx.getString(R.string.like_content, like.count));
                vh.mLikeContent.setVisibility(View.VISIBLE);
            }
        }
        // int v = data.showFlag() ? View.VISIBLE : View.GONE;
        // vh.mFlag.setVisibility(v);
        //
        // int bookmarkId = data.bookmarked ? R.drawable.ic_bookmark_checked : R.drawable.ic_bookmark;
        // vh.mBookmark.setImageResource(bookmarkId);
        // int links = data.getLinksSize();
        // vh.mLinkCount.setVisibility(links > 0 ? View.VISIBLE : View.GONE);
        // vh.mLinkCount.setText(String.valueOf(links));
        //
        // if (position == 0) {
        // int posterSize = ts.getPosterSize();
        // vh.mPosterCount.setVisibility(View.VISIBLE);
        // vh.mPosterCount.setText(String.valueOf(posterSize));
        // } else {
        // vh.mPosterCount.setVisibility(View.GONE);
        // }

    }

    public static void showPopup(View v) {
        final Context c = v.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);
        TextView contentView = (TextView) inflater.inflate(R.layout.participant_popup_item, null);
        contentView.setText(v.getContentDescription());
        PopupWindow popup = new PopupWindow(c);
        popup.setContentView(contentView);
        popup.setOutsideTouchable(true);
        popup.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.poster_popup_bg));
        popup.showAsDropDown(v, 0, -v.getHeight() * 2);
    }

    private static class ViewHolder {
        private ImageView mUserIcon;
        private TextView mUserName;
        // private TextView mUserTitle;
        private TextView mPostTime;
        private TextView mModifyCount;
        private TextView mViewCount;
        private TextView mPostCount;
        private HtmlTextView mPostContent;
        // private HorizontalScrollView mImageHsv;
        // private LinearLayout mImageLayout;

        // private ImageView mFlag;
        private ImageView mLike;
        private ImageView mShare;
        private ImageView mEdit;
        private ImageView mRecover;
        private ImageView mReply;
        private View mOverflowMenu;
        // private ImageView mBookmark;
        // private TextView mLinkCount;
        // private TextView mPosterCount;
        private TextView mLikeContent;
        private View mUserLayout;
    }
}
