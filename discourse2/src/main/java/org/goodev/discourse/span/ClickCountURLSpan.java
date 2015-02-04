package org.goodev.discourse.span;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.URLSpan;

public class ClickCountURLSpan extends URLSpan {

    public ClickCountURLSpan(String url) {
        super(url);
    }

    public ClickCountURLSpan(Parcel src) {
        super(src);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(true);
    }
}
