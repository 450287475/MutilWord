package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.utils.DisplayUtils;
import com.wangdao.mutilword.utils.PrefUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity {
    private static final int[] mImageIds = new int[]{R.drawable.guide3,R.drawable.guide2,R.drawable.guide1,R.drawable.guide4};
    private ViewPager vp_guide ;
    private ArrayList<ImageView> mImageViewsList ;

    private Button bt_start ;//开始体验
    private LinearLayout ll_point_group ; //引导圆点的父控件
    private View view_red_point ;//小红点
    private int mPointWidth ;//圆点间的距离
    private int dip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        vp_guide = (ViewPager) findViewById(R.id.vp_giude);
        bt_start = (Button) findViewById(R.id.bt_start);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        view_red_point =findViewById(R.id.view_red_point);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp，表示已经展示了新手引导
                PrefUtils.setBoolean(GuideActivity.this,
                        "is_user_guide_showed", true);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
        initView();
        vp_guide.setAdapter(new GuideAdapter());
        vp_guide.setOnPageChangeListener(new GuidePageListener());
    }
    //初始化引导页的三个界面
    private void initView() {
        mImageViewsList = new ArrayList<>();
        for (int i=0 ;i<mImageIds.length;i++){
            ImageView image = new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);//设置引导页背景
            mImageViewsList.add(image);

        }//初始化引导页的小圆点
        for (int i=0;i<mImageIds.length;i++ ){
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);
            dip = DisplayUtils.dip2px(this, 7);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip,dip);
            if (i>0){
                //设置圆点间隔
                params.leftMargin = dip ;

            }
            //设置圆点大小
            point.setLayoutParams(params);
            //将圆点添加给线性布局
            ll_point_group.addView(point);
        }
        //获取视图树对layout结束事件进行监听
        ll_point_group.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当layout执行结束后执行此方法
            @Override
            public void onGlobalLayout() {
                System.out.println("layout结束了");
                ll_point_group.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //measure(测量大小) layout(界面位置) ondraw(如何画)
                mPointWidth = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
                System.out.println("圆点距离:"+mPointWidth);
            }
        });

    }
    //ViewPager适配器
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewsList.get(position));
            return mImageViewsList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
    //ViewPager的滑动监听
    class GuidePageListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int len = (int) (mPointWidth * positionOffset) + position*mPointWidth ;
            //获取当前红点的布局参数
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_red_point
                    .getLayoutParams();
            //设置左边距
            params.leftMargin = len ;

            //重新给小蓝点设置布局参数
            view_red_point.setLayoutParams(params);

        }

        @Override
        public void onPageSelected(int position) {
            if (position == mImageIds.length -1){
                bt_start.setVisibility(View.VISIBLE);
            }else {
                bt_start.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
