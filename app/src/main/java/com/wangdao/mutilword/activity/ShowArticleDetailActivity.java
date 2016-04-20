package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.wangdao.mutilword.R;

public class ShowArticleDetailActivity extends Activity {

    private WebView wv_showarticle_artical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article_detail);

        wv_showarticle_artical = (WebView) findViewById(R.id.wv_showarticle_artical);
        Intent intent = getIntent();
        String articleurl = intent.getStringExtra("articleurl");
        showAtricel(articleurl);
    }

    public void showAtricel(String articleurl){
        wv_showarticle_artical.loadUrl(articleurl);
    }
}
