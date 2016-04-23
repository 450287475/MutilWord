package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

public class RetrievePasswordActivity extends Activity {

    private static final String TAG = "RetrievePassword";
    private EditText ed_retrievePassword_phone;
    private EditText ed_retrievePassword_verifycode;
    private LinearLayout ll_retrievePasswor_password;
    private String verifycode;
    private TextView ed_retrievePasswor_password;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);

        ed_retrievePassword_phone = (EditText) findViewById(R.id.ed_retrievePassword_phone);
        ed_retrievePassword_verifycode = (EditText) findViewById(R.id.ed_retrievePassword_verifycode);
        ed_retrievePasswor_password = (TextView) findViewById(R.id.ed_retrievePasswor_password);
        ll_retrievePasswor_password = (LinearLayout) findViewById(R.id.ll_retrievePasswor_password);
    }

    public void retrievePasswor(View view){
        String phone = ed_retrievePassword_phone.getText().toString();

        String inputVerifycode = ed_retrievePassword_verifycode.getText().toString();


        if (phone.isEmpty()){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
        }
        else if(inputVerifycode.isEmpty()){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
        }

        else {
            if(verifycode!=null){
                Log.i(TAG,"获取验证码"+ verifycode +"");
                Log.i(TAG,"输入验证码"+ inputVerifycode +"");
                if (verifycode.equals(inputVerifycode)){   //验证短信通过
                    queryFromDB(phone);
                }
            }
            else {
                Toast.makeText(this,"未获取验证码",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void queryFromDB(String phone) {
        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("phone",phone);
        bmobQuery.findObjects(RetrievePasswordActivity.this, new FindListener<UserInfo>() {
            @Override
            public void onSuccess(List<UserInfo> list) {
                UserInfo userInfo = list.get(0);
                String password = userInfo.getPassword();
                Toast.makeText(RetrievePasswordActivity.this,"找回密码成功",Toast.LENGTH_SHORT).show();
                ll_retrievePasswor_password.setVisibility(View.VISIBLE);
                ed_retrievePasswor_password.setText(password);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //请求短信验证码
    public void getVerifyCode(View view){
        showDialog("正在获取验证码");
        String number = ed_retrievePassword_phone.getText().toString();
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
                    // TODO Auto-generated method stub
                    if(ex==null){//验证码发送成功
                        hideDialog();
                        Toast.makeText(RetrievePasswordActivity.this,"验证码发送成功：",Toast.LENGTH_SHORT).show();
                        //toast("验证码发送成功，短信id："+smsId);//用于查询本次短信发送详情
                    }else{
                        hideDialog();
                        Toast.makeText(RetrievePasswordActivity.this,"发送失败：errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        //toast("errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage());
                    }
                }
            });
        }else{
            hideDialog();
            Toast.makeText(RetrievePasswordActivity.this,"输入手机号",Toast.LENGTH_SHORT).show();
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
