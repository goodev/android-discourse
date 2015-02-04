package org.goodev.discourse.span;

import android.content.Context;
import android.os.Parcel;
import android.text.style.ClickableSpan;
import android.view.View;

import org.goodev.discourse.ActivityUtils;
import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.Utils;

import java.util.ArrayList;

public class DiscourseURLSpan extends ClickableSpan {
    private final String mURL;

    public DiscourseURLSpan(String url) {
        mURL = url;
    }

    public DiscourseURLSpan(Parcel src) {
        mURL = src.readString();
    }

    public String getURL() {
        return mURL;
    }

    @Override
    public void onClick(View widget) {
        // TODO 处理 帖子内跳转事件
        String url = getURL();

        ArrayList<String> imgs = (ArrayList<String>) widget.getTag(R.id.poste_image_data);
        if (imgs != null && imgs.contains(url)) {
            Context a = widget.getContext();
            final int index = imgs.indexOf(url);
            String[] img = new String[imgs.size()];
            img = imgs.toArray(img);
            ActivityUtils.openPhotosActivity(a, index, img);
            return;
        }

        L.i("%s ", url);
        if (url.startsWith(Utils.SLASH) || url.startsWith(App.getSiteUrl())) {
            handleInAppClicked(widget);
        } else {
            openUrl(widget);
        }
    }

    private void handleInAppClicked(View widget) {
        String url = getURL();
        Context ctx = widget.getContext();

        ActivityUtils.openDiscourseLinks(ctx, url);
    }

    public void openUrl(View widget) {
        Context context = widget.getContext();
        ActivityUtils.openUrl(context, getURL());
    }
}
