package com.android.volley.toolbox;

import android.content.Context;
import android.util.AttributeSet;

/**
 * An image view which always remains square with respect to its width.
 */
final class SquaredImageView extends NetworkImageView {
    public SquaredImageView(Context context) {
        super(context);
    }

    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
