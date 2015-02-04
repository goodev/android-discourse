/*
 * Copyright (C) 2011 Cyril Mottier (http://www.cyrilmottier.com)
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

package org.goodev.discourse.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;

/**
 * A special {@link CheckBox} that does not turn into the pressed state when when the parent is already pressed.
 *
 * @author Cyril Mottier
 */
public class DontPressWithParentCheckBox extends CheckBox {
    private static final int TOUCH_ADDITION = 20;
    private int mTouchAddition;

    public DontPressWithParentCheckBox(Context context) {
        super(context);
        init();
    }

    public DontPressWithParentCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DontPressWithParentCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        final float density = getContext().getResources().getDisplayMetrics().density;
        mTouchAddition = (int) (density * TOUCH_ADDITION + 0.5f);
    }

    @Override
    public void setPressed(boolean pressed) {
        // Make sure the parent is a View prior casting it to View
        if (pressed && getParent() instanceof View && ((View) getParent()).isPressed()) {
            return;
        }
        super.setPressed(pressed);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ViewParent vp = getParent();
        if (vp instanceof ViewGroup && getVisibility() == View.VISIBLE) {
            ViewGroup vg = (ViewGroup) vp;
            Rect bounds = new Rect(vg.getWidth() - w - 2 * mTouchAddition, 0, vg.getWidth(), h);
            TouchDelegate delegate = new TouchDelegate(bounds, this);
            vg.setTouchDelegate(delegate);

        }
    }

}
