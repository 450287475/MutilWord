package com.wangdao.mutilword.explosionview;
package view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.activity.ChooseWordTypeActivity;
import com.wangdao.mutilword.activity.ExamActivity;
import com.wangdao.mutilword.activity.ReadActivity;

import java.util.ArrayList;

import Utils.HomePageUtils;

/**
 * Created by MonkeyzZi on 2016/4/19.
 */
public class ExplosionField extends View {
    private static final String TAG = "ExplosionField";
    private static final Canvas mCanvas = new Canvas();
    private ArrayList<ExplosionAnimator> explosionAnimators;
    private OnClickListener onClickListener;


    public ExplosionField(Context context) {
        super(context);
        init();
    }

    public ExplosionField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }
    private void init() {
        explosionAnimators = new ArrayList<ExplosionAnimator>();

        attach2Activity((Activity) getContext());
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (ExplosionAnimator animator : explosionAnimators) {
            animator.draw(canvas);
        }
    }



    //爆破图标
    public void explode(final View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect); //得到view相对于整个屏幕的坐标
        rect.offset(0, -HomePageUtils.dp2px(25)); //去掉状态栏高度

        final ExplosionAnimator animator = new ExplosionAnimator(this, createBitmapFromView(view), rect);
        explosionAnimators.add(animator);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.animate().alpha(0f).setDuration(150).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.animate().alpha(1f).setDuration(150).start();

                //动画结束时从动画集中移除
                explosionAnimators.remove(animation);
                JumpPage(view);
               // animation = null;
            }
        });
        animator.start();
    }


    private Bitmap createBitmapFromView(View view) {
        /*
         * 为什么屏蔽以下代码段？
         * 如果ImageView直接得到位图，那么当它设置背景（backgroud)时，不会读取到背景颜色
         */
//        if (view instanceof ImageView) {
//            Drawable drawable = ((ImageView)view).getDrawable();
//            if (drawable != null && drawable instanceof BitmapDrawable) {
//                return ((BitmapDrawable) drawable).getBitmap();
//            }
//        }

        //view.clearFocus(); //不同焦点状态显示的可能不同——（azz:不同就不同有什么关系？）

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        if (bitmap != null) {
            synchronized (mCanvas) {
                mCanvas.setBitmap(bitmap);
                view.draw(mCanvas);
                mCanvas.setBitmap(null); //清除引用
            }
        }
        return bitmap;
    }

    /**
     * 给Activity加上全屏覆盖的ExplosionField
     */
    private void attach2Activity(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.addView(this, lp);
    }


    /**
     * 希望谁有破碎效果，就给谁加Listener
     * @param view 可以是ViewGroup
     */
    public void addListener(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0 ; i < count; i++) {

                addListener(viewGroup.getChildAt(i));
                Log.e(viewGroup.getChildAt(i).toString(),"给第"+i+"个子view添加监听");

            }
        } else {
            view.setClickable(true);
            view.setOnClickListener(getOnClickListener());
            Log.e("原子控件","给view添加监听");
        }
    }


    private OnClickListener getOnClickListener() {
        if (null == onClickListener) {

            onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("override onclick","已经调用破碎方法");
//                    ExplosionField.this.explode(v);
                    //应该图和文字一起爆破，所以应该获取其父view
                    LinearLayout parent = (LinearLayout) v.getParent();
                    ExplosionField.this.explode(parent);




                    // v.setOnClickListener(null);  // 用过一次就不需要了
                }

            };
        }
        return onClickListener;

    }

    public void  JumpPage(View view){


        switch (view.getId()){
            case R.id.iv_word:
            case R.id.tv_word:
            case R.id.ll_word:
                //跳转背单词页面

                getContext().startActivity(new Intent(getContext(), ChooseWordTypeActivity.class));
                break;
            case R.id.iv_read:
            case R.id.tv_read:
            case R.id.ll_read:
                //跳转美文阅读页面

                getContext().startActivity(new Intent(getContext(), ReadActivity.class));
                break;
            case R.id.iv_exam:
            case R.id.tv_exam:
            case R.id.ll_exam:
                //跳转测试训练页面


                getContext().startActivity(new Intent(getContext(), ExamActivity.class));
                break;
            case R.id.iv_listen:
            case R.id.tv_listen:
            case R.id.ll_listen:
                //跳转听力页面


                getContext().startActivity(new Intent(getContext(), ReadActivity.class));
                break;

        }
    }



}
