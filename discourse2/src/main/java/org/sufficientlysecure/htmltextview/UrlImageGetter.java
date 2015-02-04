/*
 * Copyright (C) 2013 Antarix Tandon
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
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.goodev.discourse.App;
import org.goodev.discourse.R;
import org.goodev.discourse.span.ImageClickableSpan;
import org.goodev.discourse.utils.ImageLoader;
import org.goodev.discourse.utils.L;
import org.goodev.discourse.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class UrlImageGetter implements ImageGetter {
    public ArrayList<ImageContainer> mImageContainers = new ArrayList<ImageLoader.ImageContainer>();
    Context mCtx;
    TextView mTextView;
    Drawable mPlaseHolder;
    int mDefaultHeight;
    int mDefaultWidth;
    ImageLoader mImageLoader;

    /**
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     */
    public UrlImageGetter(TextView t, Context c, ImageLoader loader) {
        mCtx = c;
        t.setTag(R.id.poste_image_getter, this);
        mTextView = t;
        mImageLoader = loader;
        // TODO 帖子内容中的默认图片
        mPlaseHolder = c.getResources().getDrawable(R.drawable.ic_logo);
        mDefaultHeight = mPlaseHolder.getIntrinsicHeight();
        mDefaultWidth = mPlaseHolder.getIntrinsicWidth();
        mPlaseHolder.setBounds(0, 0, mDefaultWidth, mDefaultHeight);
    }

    @Override
    public Drawable getDrawable(String source) {
        source = checkUrl(source);
        UrlDrawable urlDrawable = new UrlDrawable();

        ImageContainer ic = mImageLoader.getForImageSpan(this, source, mTextView, new ImageCallback(mCtx.getResources(), urlDrawable));
        // TODO 当帖子滚动屏幕外的时候 如何和ImageView一样 取消前面没用的下载请求？？？
        if (ic != null && ic.getBitmap() != null) {
            BitmapDrawable drawable = new BitmapDrawable(mCtx.getResources(), ic.getBitmap());
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }
        if (ic != null) {
            mImageContainers.add(ic);
        }
        // get the actual source
        // ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);
        // asyncTask.execute(source);

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable;
    }

    private String checkUrl(String source) {
        if (source.startsWith(Utils.SLASH2)) {
            source = Utils.AVATAR_HTTP_PREFIX + source;
        } else if (source.startsWith(Utils.SLASH)) {
            // 用户上传的图片 地址为相对地址： /upload/ddd.
            source = App.getSiteUrl() + source.substring(1);
        }
        return source;
    }

    class ImageCallback implements com.android.volley.toolbox.ImageLoader.ImageListener {
        private final UrlDrawable mDrawable;
        private final Resources mRes;

        public ImageCallback(Resources res, UrlDrawable d) {
            mDrawable = d;
            mRes = res;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO 显示错误图片？
        }

        @Override
        public void onResponse(ImageContainer response, boolean isImmediate) {
            if (mTextView.getTag(R.id.poste_image_getter) == UrlImageGetter.this && response.getBitmap() != null) {
                BitmapDrawable drawable = new BitmapDrawable(mRes, response.getBitmap());
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                String src = response.getRequestUrl();
                // redraw the image by invalidating the container
                // 由于下面重新设置了ImageSpan，所以这里invalidate就不必要了吧
                // urlDrawable.invalidateSelf();
                // UrlImageGetter.this.mTextView.invalidate();

                // TODO 这种方式基本完美解决显示图片问题， blog？
                // 把提取到下面显示的图片在放出来？ 然后点击任何图片 进入到 帖子图片浏览界面。。？
                TextView v = UrlImageGetter.this.mTextView;
                CharSequence text = v.getText();
                if (!(text instanceof Spannable)) {
                    return;
                }
                Spannable spanText = (Spannable) text;
                @SuppressWarnings("unchecked") ArrayList<String> imgs = (ArrayList<String>) v.getTag(R.id.poste_image_data);
                ImageSpan[] imageSpans = spanText.getSpans(0, spanText.length(), ImageSpan.class);
                L.i("%b X Recycled %b src: %s", isImmediate, response.getBitmap().isRecycled(), src);
                for (ImageSpan imgSpan : imageSpans) {
                    int start = spanText.getSpanStart(imgSpan);
                    int end = spanText.getSpanEnd(imgSpan);
                    L.i("%d-%d :%s", start, end, imgSpan.getSource());
                    String url = imgSpan.getSource();
                    url = checkUrl(url);
                    if (src.equals(url)) {
                        spanText.removeSpan(imgSpan);
                        ImageSpan is = new ImageSpan(drawable, src, ImageSpan.ALIGN_BASELINE);
                        spanText.setSpan(is, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (imgs != null && imgs.contains(src)) {
                        ImageClickableSpan ics = new ImageClickableSpan(src);
                        spanText.setSpan(ics, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                }
                v.setText(spanText);
            }

        }

    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        private final UrlDrawable urlDrawable;
        private String src;

        public ImageGetterAsyncTask(UrlDrawable d) {
            urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            src = source;
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result == null) {
                // TODO 设置默认错误图片？
                return;
            }
            // set the correct bound according to the result from HTTP call
            int w = result.getIntrinsicWidth();
            int h = result.getIntrinsicHeight();
            urlDrawable.setBounds(0, 0, 0 + w, 0 + h);
            L.i("%d X %d", w, h);

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable.drawable = result;

            // redraw the image by invalidating the container
            // 由于下面重新设置了ImageSpan，所以这里invalidate就不必要了吧
            // urlDrawable.invalidateSelf();
            // UrlImageGetter.this.mTextView.invalidate();

            // TODO 这种方式基本完美解决显示图片问题， blog？
            // 把提取到下面显示的图片在放出来？ 然后点击任何图片 进入到 帖子图片浏览界面。。？
            TextView v = UrlImageGetter.this.mTextView;
            Spannable spanText = (Spannable) v.getText();
            @SuppressWarnings("unchecked") ArrayList<String> imgs = (ArrayList<String>) v.getTag(R.id.poste_image_data);
            ImageSpan[] imageSpans = spanText.getSpans(0, spanText.length(), ImageSpan.class);
            for (ImageSpan imgSpan : imageSpans) {
                int start = spanText.getSpanStart(imgSpan);
                int end = spanText.getSpanEnd(imgSpan);
                L.i("%d-%d :%s", start, end, imgSpan.getSource());
                if (src.equals(imgSpan.getSource())) {
                    spanText.removeSpan(imgSpan);
                    ImageSpan is = new ImageSpan(result, src, ImageSpan.ALIGN_BASELINE);
                    spanText.setSpan(is, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (imgs != null && imgs.contains(src)) {
                    ImageClickableSpan ics = new ImageClickableSpan(src);
                    spanText.setSpan(ics, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            }
            // For ICS
            // UrlImageGetter.this.container.setMinimumHeight(
            // (UrlImageGetter.this.container.getHeight() + result.getIntrinsicHeight()));
            // UrlImageGetter.this.container.requestLayout();
            // UrlImageGetter.this.container.invalidate();
        }

        /**
         * Get the Drawable from URL
         *
         * @param urlString
         * @return
         */
        public Drawable fetchDrawable(String urlString) {
            try {
                InputStream is = fetch(urlString);
                Drawable drawable = Drawable.createFromResourceStream(App.getContext().getResources(), null, is, "src");
                L.d("%d----------X------ %d--%s", drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), urlString);
                drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0 + drawable.getIntrinsicHeight());
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }

        private InputStream fetch(String urlString) throws MalformedURLException, IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }

    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        public UrlDrawable() {
            setBounds(0, 0, mDefaultWidth, mDefaultHeight);

        }        // the drawable that you need to set, you could set the initial drawing

        // with the loading image if you need to
        protected Drawable drawable = mPlaseHolder;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        @Override
        public int getIntrinsicWidth() {
            return drawable.getIntrinsicWidth();
        }

        @Override
        public int getIntrinsicHeight() {
            return drawable.getIntrinsicHeight();
        }


    }
}
