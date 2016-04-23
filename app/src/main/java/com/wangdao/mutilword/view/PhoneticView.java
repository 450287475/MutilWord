package com.wangdao.mutilword.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Mumuseng on 2016/4/22.
 */
public class PhoneticView extends TextView {
    public PhoneticView(Context context) {
        super(context);
    }

    public PhoneticView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }
}
