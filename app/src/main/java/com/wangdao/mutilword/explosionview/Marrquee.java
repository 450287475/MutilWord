package com.wangdao.mutilword.explosionview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**跑马灯TextView 重写是为了防止当TextView失去焦点时跑马灯停止。
 * Created by MonkeyzZi on 2016/4/19.
 */
public class Marrquee extends TextView {
    public Marrquee(Context context) {
        super(context);
    }

    public Marrquee(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Marrquee(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {

    }
}
