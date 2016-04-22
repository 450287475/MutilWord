package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.wangdao.mutilword.R;

public class ModifyUserInfoActivity extends Activity {

    private EditText ed_modifyuser_vlaue;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);

        ed_modifyuser_vlaue = (EditText) findViewById(R.id.ed_modifyuser_vlaue);

        intent = getIntent();
        String initdata = intent.getStringExtra("initdata");
        ed_modifyuser_vlaue.setText(initdata);

    }

    public void modify(View view){
        String value = ed_modifyuser_vlaue.getText().toString();
        intent.putExtra("value",value);
        setResult(RESULT_OK,intent);  //RESULT_OK 为返回码
        finish();
    }

    public void back(View view){
        finish();
    }
}
