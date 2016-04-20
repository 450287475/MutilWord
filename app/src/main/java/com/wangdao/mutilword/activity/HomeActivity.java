package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.os.Bundle;

import com.wangdao.mutilword.R;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //设置监听点击的图标
       /* ExplosionField explosionField = new ExplosionField(HomeActivity.this);

        explosionField.addListener(findViewById(R.id.ll_root));
*/
    }
}


}