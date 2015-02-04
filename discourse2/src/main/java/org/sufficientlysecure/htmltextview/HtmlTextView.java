/*
 * Copyright (C) 2013 Dominik Schürmann <dominik@dominikschuermann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sufficientlysecure.htmltextview;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;

import org.goodev.discourse.span.DiscourseURLSpan;
import org.goodev.discourse.utils.ImageLoader;

import java.io.InputStream;

public class HtmlTextView extends JellyBeanSpanFixTextView {

    public static final String TAG = "HtmlTextView";
    private ImageLoader mImageLoader;

    public HtmlTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HtmlTextView(Context context) {
        super(context);
    }

    /**
     * http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
     *
     * @param is
     * @return
     */
    static private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Loads HTML from a raw resource, i.e., a HTML file in res/raw/. This allows translatable resource (e.g., res/raw-de/ for german). The
     * containing HTML is parsed to Android's Spannable format and then displayed.
     *
     * @param context
     * @param id      for example: R.raw.help
     */
    public void setHtmlFromRawResource(Context context, int id) {
        // load html from html file from /res/raw
        InputStream inputStreamText = context.getResources().openRawResource(id);

        setHtmlFromString(convertStreamToString(inputStreamText));
    }

    public void setImageLoader(ImageLoader loader) {
        mImageLoader = loader;
    }

    /**
     * Parses String containing HTML to Android's Spannable format and displays it in this TextView.
     *
     * @param html String containing HTML, for example: "<b>Hello world!</b>"
     */
    public void setHtmlFromString(String html) {
        SpannableStringBuilder spanText = (SpannableStringBuilder) Html.fromHtml(html, new UrlImageGetter(this, getContext(), mImageLoader), new HtmlTagHandler());

        URLSpan[] spans = spanText.getSpans(0, spanText.length(), URLSpan.class);
        // TODO URLSpan 必需用自定义的 ClickableSpan （DiscourseURLSpan） 替换， 否则 URLSpan不可点击， 显示也没有下划线。 WHY?
        for (URLSpan span : spans) {
            int start = spanText.getSpanStart(span);
            int end = spanText.getSpanEnd(span);
            String url = span.getURL();
            if (url != null) {
                spanText.removeSpan(span);
                ClickableSpan span1 = new DiscourseURLSpan(span.getURL());
                spanText.setSpan(span1, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        // ImageSpan[] images = spanText.getSpans(0, spanText.length(), ImageSpan.class);
        // for (ImageSpan image : images) {
        // int start = spanText.getSpanStart(image);
        // int end = spanText.getSpanEnd(image);
        // L.i("%d-%d :%s", start, end, image.getSource());
        // spanText.removeSpan(image);
        // DynamicDrawableSpan is = new DiscourseImageSpan(getContext(), image.getDrawable());
        // spanText.setSpan(is, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //
        // }
        setText(spanText);

        // make links work
        setMovementMethod(LinkMovementMethod.getInstance());

        // no flickering when clicking textview for Android < 4
        // text.setTextColor(getResources().getColor(android.R.color.secondary_text_dark_nodisable));
    }
}
