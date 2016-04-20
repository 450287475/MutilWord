package com.wangdao.mutilword.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.wangdao.mutilword.R;

/**
 * Created by haijun on 2016/4/20.
 */
public class CollectImageView extends ImageView {
    public CollectImageView(Context context) {
        super(context);
    }

    public CollectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setImageResource(R.drawable.collected);
        return super.onKeyDown(keyCode, event);
    }

}
