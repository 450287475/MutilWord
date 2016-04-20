package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.viewpagerindicator.CirclePageIndicator;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.ArticleList;
import com.wangdao.mutilword.bean.UserCollection;
import com.wangdao.mutilword.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;


public class ReadActivity extends Activity {

    private static final String TAG = "ReadActivity";
    private ViewPager vp_read_hotarticl;
    private List<ArticleList> articleListListTop;
    private List<ArticleList> articleListListContent;
    private List<UserCollection> userCollectionList;
    private TextView tv_read_imagetitle;
    private CirclePageIndicator ci_read_indicator;
    private ListView lv_read_articlelist;
    private ArticlelistAdapter articlelistAdapter;
    private UserInfo userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        View inflate = View.inflate(this, R.layout.article_toplist, null);

        vp_read_hotarticl = (ViewPager) inflate.findViewById(R.id.vp_read_hotarticl);
        tv_read_imagetitle = (TextView) inflate.findViewById(R.id.tv_read_imagetitle);


        lv_read_articlelist = (ListView) findViewById(R.id.lv_read_articlelist);

        lv_read_articlelist.addHeaderView(inflate);
        articleListListTop = new ArrayList<>();
        articleListListContent = new ArrayList<>();
        userCollectionList = new ArrayList<>();
        initarticleList();

        ci_read_indicator = (CirclePageIndicator)inflate.findViewById(R.id.ci_read_indicator);

    }

    class ReadPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return articleListListTop.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view==o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ArticleList article = articleListListTop.get(position);
            String imageurl = article.getImage();
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
                    int count = article.getCount();
                    String objectId = article.getObjectId();
                    intent.putExtra("articleurl",articleurl);
                    intent.putExtra("count",count);
                    intent.putExtra("objectId",objectId);

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
                for (int i=0;i<list.size();i++){
                    if (list.get(i).getLocation().equals("top")){
                        articleListListTop.add(list.get(i));
                    }
                    else if (list.get(i).getLocation().equals("content")){
                        articleListListContent.add(list.get(i));
                    }
                }

                //根据id去查找对应的用户收藏信息表
                userInfo = ApplicationInfo.userInfo;

                String userId = "";
                if (userInfo!=null){
                    userId = userInfo.getUserid();
                }

                BmobQuery<UserCollection> bmobQueryForCooect = new BmobQuery();
                bmobQueryForCooect.addWhereEqualTo("userId",userId);
                bmobQueryForCooect.findObjects(ReadActivity.this, new FindListener<UserCollection>() {
                    @Override
                    public void onSuccess(List<UserCollection> list) {


                        userCollectionList = list;
                        articlelistAdapter = new ArticlelistAdapter();
                        lv_read_articlelist.setAdapter(articlelistAdapter);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });

                vp_read_hotarticl.setAdapter(new ReadPageAdapter());

                ci_read_indicator.setViewPager(vp_read_hotarticl);
                //设置滑动监听，当页面滑动时改变页面上显示的文字   xx
                ci_read_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {
                        tv_read_imagetitle.setText(articleListListTop.get(i).getTitle());
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
                Toast.makeText(ReadActivity.this,"失败,请检查网络"+s,Toast.LENGTH_SHORT).show();
            }
        });



        lv_read_articlelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArticleList article = articleListListContent.get(position-1);
                String articleurl = article.getArticleurl();
                int count = article.getCount();
                String objectId = article.getObjectId();
                Log.i(TAG,"articleurl:"+articleurl);
                Intent intent = new Intent(ReadActivity.this,ShowArticleDetailActivity.class);
                intent.putExtra("articleurl",articleurl);
                intent.putExtra("count",count);
                intent.putExtra("objectId",objectId);
                startActivity(intent);
            }
        });




    }

    class ArticlelistAdapter extends BaseAdapter{
        BitmapUtils bitmapUtils;
        ImageView iv_readlist_collect;
        List<Boolean> isCollectedList;

        public ArticlelistAdapter() {
            bitmapUtils= new BitmapUtils(ReadActivity.this);
            isCollectedList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return articleListListContent.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ArticleList article = articleListListContent.get(position);
            View inflate = View.inflate(ReadActivity.this,R.layout.list_article_item,null);
            ImageView iv_readlist_artcileimage = (ImageView) inflate.findViewById(R.id.iv_readlist_artcileimage);
            TextView tv_readlist_title = (TextView) inflate.findViewById(R.id.tv_readlist_title);
            TextView tv_readlist_type = (TextView) inflate.findViewById(R.id.tv_readlist_type);
            TextView tv_readlist_count = (TextView) inflate.findViewById(R.id.tv_readlist_count);
            iv_readlist_collect = (ImageView) inflate.findViewById(R.id.iv_readlist_collect);

            String imageurl = article.getImage();
            bitmapUtils.display(iv_readlist_artcileimage,imageurl);

            tv_readlist_title.setText(article.getTitle());
            tv_readlist_type.setText("类型："+ article.getType());
            tv_readlist_count.setText("阅读量："+ article.getCount()+"");

            //如果要显示的文章列表和用户收藏的一样，则将这条item的图片设为已收藏
            if (userCollectionList!=null && userCollectionList.size()!=0){
                Boolean isCollected = false;
                for (int i=0;i<userCollectionList.size();i++){
                    String articleUrl = userCollectionList.get(i).getArticleUrl();
                    if (articleUrl.equals(article.getArticleurl())){
                        isCollected = true;
                    }
                }
                isCollectedList.add(isCollected);
            }
            else {
                isCollectedList.add(false);
            }

            Log.i(TAG,"isCollectedList:"+position+":"+isCollectedList);

            if (!isCollectedList.get(position)){
                iv_readlist_collect.setImageResource(R.drawable.collect);
            }
            else {
                iv_readlist_collect.setImageResource(R.drawable.collected);
            }

            //对收藏图标的点击事件，如果没有收藏，则点击图标，将这条信息收藏，并改变图标
            iv_readlist_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean isCollected = isCollectedList.get(position);
                    if (isCollected){
                        Toast.makeText(ReadActivity.this,"你已经收藏过了",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //将图标改为收藏
                        isCollectedList.set(position,true);
                        //iv_readlist_collect.setImageResource(R.drawable.collected);
                        //通知Adapter进行更新界面
                        articlelistAdapter.notifyDataSetChanged();
                        Toast.makeText(ReadActivity.this,"已添加到收藏夹",Toast.LENGTH_SHORT).show();

                        //保存新收藏的数据到服务器
                        UserCollection userCollection = new UserCollection();
                        if (userInfo==null){
                            return;
                        }
                        userCollection.setUserId(userInfo.getUserid());
                        userCollection.setType("文章_美文");
                        userCollection.setArticleUrl(article.getArticleurl());

                        Log.i(TAG,userCollection.toString());

                        // 插入到数据库
                        userCollection.save(ReadActivity.this);
                    }
               }
            });
            return inflate;
        }
    }

}
