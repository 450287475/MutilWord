package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.ArticleList;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class ReadActivity extends Activity {

    private static final String TAG = "ReadActivity";
    private ViewPager vp_read_hotarticl;
    private List<ArticleList> articleListList;
    private TextView tv_read_imagetitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        vp_read_hotarticl = (ViewPager) findViewById(R.id.vp_read_hotarticl);
        tv_read_imagetitle = (TextView) findViewById(R.id.tv_read_imagetitle);


        articleListList = new ArrayList<>();
        initarticleList();
        //initItemOnClickData();


    }

    /*private void initItemOnClickData() {
        vp_read_hotarticl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = vp_read_hotarticl.getCurrentItem();
                Log.i(TAG,"currentItem:"+currentItem);
                String articleurl = articleListList.get(currentItem).getArticleurl();
                Log.i(TAG,"articleurl:"+articleurl);
                Intent intent = new Intent(ReadActivity.this,ShowArticleDetailActivity.class);
                intent.putExtra("articleurl",articleurl);
                startActivity(intent);
            }
        });
    }*/


    class ReadPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return articleListList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view==o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ArticleList article = articleListList.get(position);
            String imageurl = article.getImage();
            Log.i(TAG,"imageurl:"+imageurl);
            BitmapUtils bitmapUtils = new BitmapUtils(ReadActivity.this);

            ImageView imageView = new ImageView(ReadActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            bitmapUtils.display(imageView,imageurl);
            container.addView(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String articleurl = article.getArticleurl();
                    Log.i(TAG,"articleurl:"+articleurl);
                    Intent intent = new Intent(ReadActivity.this,ShowArticleDetailActivity.class);
                    intent.putExtra("articleurl",articleurl);
                    startActivity(intent);
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void initarticleList(){
        BmobQuery<ArticleList> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(this, new FindListener<ArticleList>() {
            @Override
            public void onSuccess(List<ArticleList> list) {
                articleListList = list;
                Log.i(TAG,"list"+articleListList.size());
                vp_read_hotarticl.setAdapter(new ReadPageAdapter());

                //设置滑动监听，当页面滑动时改变页面上显示的文字
                vp_read_hotarticl.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {
                        tv_read_imagetitle.setText(articleListList.get(i).getTitle());
                    }

                    @Override
                    public void onPageSelected(int i) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(ReadActivity.this,"失败"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }





}
