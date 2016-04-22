package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.UserCollection;
import com.wangdao.mutilword.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class SeeCollectedArtivleActivity extends Activity {

    private static final String TAG = "SeeCollectedArtivle";
    private ListView lv_collectedarticel_articlelist;
    private List<UserCollection> articleListcollected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_collected_artivle);

        lv_collectedarticel_articlelist = (ListView) findViewById(R.id.lv_collectedarticel_articlelist);
        articleListcollected = new ArrayList<>();

        initarticleListcollected();

    }

    public void initarticleListcollected(){
        UserInfo userInfo = ApplicationInfo.userInfo;

        BmobQuery<UserCollection> bmobQuery = new BmobQuery<>(); //0003
        bmobQuery.addWhereEqualTo("userId",userInfo.getUserid());
        bmobQuery.findObjects(this, new FindListener<UserCollection>() {
            @Override
            public void onSuccess(List<UserCollection> list) {
                if (list!=null){
                    articleListcollected = list;
                    lv_collectedarticel_articlelist.setAdapter(new ArticlelistAdapter());
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(SeeCollectedArtivleActivity.this,"获取失败,请检查网络"+s,Toast.LENGTH_SHORT).show();
            }
        });

        lv_collectedarticel_articlelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserCollection article = articleListcollected.get(position);
                String articleurl = article.getArticleUrl();
                int count = 10;
                String objectId = article.getObjectId();
                Intent intent = new Intent(SeeCollectedArtivleActivity.this,ShowArticleDetailActivity.class);
                intent.putExtra("articleurl",articleurl);
                intent.putExtra("count",count);
                intent.putExtra("objectId",objectId);
                startActivity(intent);
            }
        });
    }


    class ArticlelistAdapter extends BaseAdapter {
        BitmapUtils bitmapUtils;
        public ArticlelistAdapter() {
            bitmapUtils= new BitmapUtils(SeeCollectedArtivleActivity.this);
        }

        @Override
        public int getCount() {
            return articleListcollected.size();
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
            UserCollection article = articleListcollected.get(position);
            View inflate = View.inflate(SeeCollectedArtivleActivity.this, R.layout.list_article_item_collected,null);
            TextView tv_collectedarticle_title = (TextView) inflate.findViewById(R.id.tv_collectedarticle_title);
            tv_collectedarticle_title.setText(article.getTitle());

            return inflate;
        }
    }

    public void back(View view){
        finish();
    }
}
