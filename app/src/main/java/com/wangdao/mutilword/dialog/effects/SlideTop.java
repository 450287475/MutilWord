package com.wangdao.mutilword.dialog.effects;

import android.animation.ObjectAnimator;
import android.view.View;



/**
 * Created by lee on 2014/7/31.
 */
public class SlideTop extends BaseEffects{

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "translationY", -300, 0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)

        );
    }
}
