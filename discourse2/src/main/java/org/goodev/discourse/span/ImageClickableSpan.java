package org.goodev.discourse.span;

import android.content.Context;
import android.os.Parcel;
import android.text.style.ClickableSpan;
import android.view.View;

import org.goodev.discourse.ActivityUtils;
import org.goodev.discourse.R;

import java.util.ArrayList;

public class ImageClickableSpan extends ClickableSpan {

    private final String mURL;

    public ImageClickableSpan(String url) {
        mURL = url;
    }

    public ImageClickableSpan(Parcel src) {
        mURL = src.readString();
    }

    public String getURL() {
        return mURL;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
        ArrayList<String> imgs = (ArrayList<String>) v.getTag(R.id.poste_image_data);
        final Integer index = imgs.indexOf(mURL);
        Context a = v.getContext();
        String[] img = new String[imgs.size()];
        img = imgs.toArray(img);
        ActivityUtils.openPhotosActivity(a, index, img);
    }

}
