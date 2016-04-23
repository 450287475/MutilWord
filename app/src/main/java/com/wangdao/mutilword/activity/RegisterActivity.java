package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity {

    private static final String TAG = "RegisterActivity";
    private EditText ed_register_phone;
    private EditText ed_register_password;
    private EditText ed_register_confirmpassword;
    private EditText ed_register_username;

    private EditText ed_register_verifycode;
    private String verifycode;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ed_register_phone = (EditText) findViewById(R.id.ed_register_phone);
        ed_register_password = (EditText) findViewById(R.id.ed_register_password);
        ed_register_confirmpassword = (EditText) findViewById(R.id.ed_register_confirmpassword);
        ed_register_username = (EditText) findViewById(R.id.ed_register_username);
        ed_register_verifycode = (EditText) findViewById(R.id.ed_register_verifycode);

    }

    //注册
    public void register(View view){
        String phone = ed_register_phone.getText().toString();
        String password = ed_register_password.getText().toString();
        String confirmpassword = ed_register_confirmpassword.getText().toString();
        String username = ed_register_username.getText().toString();
        String inputVerifycode = ed_register_verifycode.getText().toString();


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

        else if(inputVerifycode.isEmpty()){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
        }

        else {
            if(verifycode!=null){
                Log.i(TAG,"获取验证码"+ verifycode +"");
                Log.i(TAG,"输入验证码"+ inputVerifycode +"");
                if (verifycode.equals(inputVerifycode)){   //验证短信通过
                    UserInfo userInfo=new UserInfo();
                    userInfo.setPhone(phone);
                    userInfo.setPassword(password);
                    userInfo.setUsername(username);
                    insertToDB(userInfo);
                }
            }
            else {
                Toast.makeText(this,"未获取验证码",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //将数据插入到数据库
    private void insertToDB(final UserInfo userInfo) {
        showDialog("正在获取验证码");
        userInfo.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "注册成功：" + userInfo.getObjectId(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));

                //将登陆用户信息保存在ApplicationInfo类的UserInfo对象里面
                String objectId = userInfo.getObjectId();
                String userid = userInfo.getUserid();
                String username = userInfo.getUsername();
                String password = userInfo.getPassword();
                String autograph = userInfo.getAutograph();
                String phone = userInfo.getPhone();
                String usericon = userInfo.getUsericon();
                int collectedArticle = userInfo.getCollectedArticle();
                int collectedWord = userInfo.getCollectedWord();
                int exchangeAwarded = userInfo.getExchangeAwarded();
                int exchangeAward = userInfo.getExchangeAward();
                int articleCount = userInfo.getArticleCount();
                int wordCount = userInfo.getWordCount();
                int userpoints = userInfo.getUserpoints();
                int userrank = userInfo.getUserrank();

                ApplicationInfo.initUserInfo(objectId,userid, username, password, usericon, phone, autograph,
                        collectedArticle, collectedWord, exchangeAwarded, exchangeAward, articleCount, wordCount,
                        userrank, userpoints);
                Intent intent = getIntent();
                intent.putExtra("registOK",true);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(RegisterActivity.this,"注册失败：" + msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //请求短信验证码
    public void getVerifyCode(View view){
        String number = ed_register_phone.getText().toString();
        if(!TextUtils.isEmpty(number)){
            SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sendTime = format.format(new Date());

            //产生6位验证码
            int round = (int) Math.round(Math.random() * (999999 - 100000) + 100000);
            verifycode = String.valueOf(round);
            Log.i(TAG,"验证码"+ round +"");
            BmobSMS.requestSMS(this, number, "您的验证码为"+ verifycode +"，请及时验证！",sendTime,new RequestSMSCodeListener() {

                @Override
                public void done(Integer smsId,BmobException ex) {
                    hideDialog();
                    // TODO Auto-generated method stub
                    if(ex==null){//验证码发送成功
                        Toast.makeText(RegisterActivity.this,"验证码发送成功：",Toast.LENGTH_SHORT).show();
                        //toast("验证码发送成功，短信id："+smsId);//用于查询本次短信发送详情
                    }else{
                        Toast.makeText(RegisterActivity.this,"发送失败：errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        //toast("errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage());
                    }
                }
            });
        }else{
            hideDialog();
            Toast.makeText(RegisterActivity.this,"输入手机号",Toast.LENGTH_SHORT).show();
        }
    }
    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }
}
