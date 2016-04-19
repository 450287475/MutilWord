package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.UserInfo;

import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity {

    private EditText ed_register_phone;
    private EditText ed_register_password;
    private EditText ed_register_confirmpassword;
    private EditText ed_register_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ed_register_phone = (EditText) findViewById(R.id.ed_register_phone);
        ed_register_password = (EditText) findViewById(R.id.ed_register_password);
        ed_register_confirmpassword = (EditText) findViewById(R.id.ed_register_confirmpassword);
        ed_register_username = (EditText) findViewById(R.id.ed_register_username);



    }

    //注册
    public void register(View view){
        String phone = ed_register_phone.getText().toString();
        String password = ed_register_password.getText().toString();
        String confirmpassword = ed_register_confirmpassword.getText().toString();
        String username = ed_register_username.getText().toString();

        if (phone.isEmpty()){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty()){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
        }
        else if(!password.isEmpty() && !confirmpassword.isEmpty() && !password.equals(confirmpassword)){
            Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT).show();
        }
        else if(username.isEmpty()){
            Toast.makeText(this,"请输入昵称",Toast.LENGTH_SHORT).show();
        }
        else {
            UserInfo userInfo=new UserInfo();
            userInfo.setPhone(phone);
            userInfo.setPassword(password);
            userInfo.setUsername(username);
            insertToDB(userInfo);
        }



    }

    //将数据插入到数据库
    private void insertToDB(final UserInfo userInfo) {
        userInfo.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this,"注册成功："+userInfo.getObjectId(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(RegisterActivity.this,"注册失败：" + msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
