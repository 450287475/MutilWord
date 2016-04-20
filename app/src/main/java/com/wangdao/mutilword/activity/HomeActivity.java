package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wangdao.mutilword.R;


public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void read(View view){
        startActivity(new Intent(this,ReadActivity.class));
    }


}
