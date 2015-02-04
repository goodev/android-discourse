package org.goodev.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import org.goodev.discourse.utils.L;

public class MdEditText extends EditText {

    private OnSelectionChangeListener mSelectionListener;

    public MdEditText(Context context) {
        super(context);
    }

    public MdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MdEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnSelectionChangeListener(OnSelectionChangeListener l) {
        mSelectionListener = l;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        L.i("start: %d end: %d", selStart, selEnd);
        if (mSelectionListener != null) {
            mSelectionListener.onSelectionChanged(selStart, selEnd);
        }
    }

    public interface OnSelectionChangeListener {
        void onSelectionChanged(int start, int end);
    }

}
