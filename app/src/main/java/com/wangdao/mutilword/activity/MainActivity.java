package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.UserInfo;


import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private EditText ed_initpage_phone;
    private EditText ed_initpage_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "d8aca0c0e17c711bfb65e82127887c2c");

        ed_initpage_phone = (EditText) findViewById(R.id.ed_initpage_phone);
        ed_initpage_password = (EditText) findViewById(R.id.ed_initpage_password);

    }

    //登陆
    public void login(View view){
        //验证登陆，登陆成功，则跳到主页面
        String phone=ed_initpage_phone.getText().toString();
        String password=ed_initpage_password.getText().toString();

        //输入信息验证
        if (phone.isEmpty()||password.isEmpty()){
            Toast.makeText(this,"请输入用户名或密码",Toast.LENGTH_SHORT).show();
            return;
        }
        validate(phone);


    }

    //验证是否时注册用户
    private void validate(String phone) {
        //查找UserInfo表里面id为  XXX  的数据
        BmobQuery<UserInfo> bmobQuery = new BmobQuery();

        bmobQuery.addWhereEqualTo("phone",phone);
        bmobQuery.findObjects(this, new FindListener<UserInfo>() {
            @Override
            public void onSuccess(List<UserInfo> list) {
                //查询到对应的信息，则注册过，跳到主页面
                if (list.size()==0){
                    //没有查询到对应的信息，则没注册过，提示该用户未注册
                    Toast.makeText(MainActivity.this,"还未注册，请先进行注册",Toast.LENGTH_SHORT).show();
                }
                else {
                    String username = list.get(0).getUsername();
                    Log.i(TAG,"查询Username:"+username);
                    Toast.makeText(MainActivity.this,"欢迎:"+username,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //注册
    public void register(View view){
        //跳到注册页面，注册成功，则跳到主页面
        startActivity(new Intent(this,RegisterActivity.class));
    }

    //忘记密码,进行找回密码
    public void forgetPassword(View view){
        //跳到找回密码页面
        startActivity(new Intent(this,RetrievePasswordActivity.class));
    }

}
