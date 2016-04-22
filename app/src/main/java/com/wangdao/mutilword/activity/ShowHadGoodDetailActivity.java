package com.wangdao.mutilword.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.wangdao.mutilword.R;

public class ShowHadGoodDetailActivity extends ActionBarActivity {

    private ImageView iv_showhadgood_image;
    private TextView tv_showhadgood_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_had_good_detail);

        iv_showhadgood_image = (ImageView) findViewById(R.id.iv_showhadgood_image);
        tv_showhadgood_name = (TextView) findViewById(R.id.tv_showhadgood_name);

        initViewData();

    }

    private void initViewData() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String iconurl = intent.getStringExtra("iconurl");

        tv_showhadgood_name.setText(name);
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        bitmapUtils.display(iv_showhadgood_image,iconurl);

    }
}
