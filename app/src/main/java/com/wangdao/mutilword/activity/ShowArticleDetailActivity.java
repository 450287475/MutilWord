package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.ArticleList;

import cn.bmob.v3.listener.UpdateListener;

public class ShowArticleDetailActivity extends Activity {

    private static final String TAG = "ShowArticleActivity";
    private WebView wv_showarticle_artical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article_detail);

        wv_showarticle_artical = (WebView) findViewById(R.id.wv_showarticle_artical);

        showAtricel();
    }

    public void showAtricel(){
        Intent intent = getIntent();
        String articleurl = intent.getStringExtra("articleurl");
        wv_showarticle_artical.loadUrl(articleurl);

        int count = intent.getIntExtra("count",0);
        String objectId = intent.getStringExtra("objectId");
        Log.i(TAG,"objectId"+objectId);
        count++;
        ArticleList articleList = new ArticleList();
        articleList.setCount(count);

        //更新数据，每次阅读都将阅读量加1
        articleList.update(this, objectId, new UpdateListener() {
            @Override
            public void onSuccess() {
                //Toast.makeText(ShowArticleDetailActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                //Toast.makeText(ShowArticleDetailActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
            }
        });



    }
}
