package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.UserInfo;
import com.wangdao.mutilword.utils.Md5Utils;

import java.util.HashMap;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private EditText ed_initpage_phone;
    private EditText ed_initpage_password;
    private CheckBox ed_initpage_savepassword;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_initpage_phone = (EditText) findViewById(R.id.ed_initpage_phone);
        ed_initpage_password = (EditText) findViewById(R.id.ed_initpage_password);
        ed_initpage_savepassword = (CheckBox) findViewById(R.id.ed_initpage_savepassword);
        //初始化 shareSDK
        ShareSDK.initSDK(this);

        Bmob.initialize(this, "d8aca0c0e17c711bfb65e82127887c2c");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this);

        judgIsLogin();
    }



    private void judgIsLogin() {
        //先判断是否登陆过
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        boolean isRememberInf = sharedPreferences.getBoolean("rememberInf", false);
        String objectId = sharedPreferences.getString("objectId", "");

        //没有登陆。显示登陆界面
        if (!isRememberInf){
            //什么都不做
        }
        //登陆过，跳到主页面
        else {
            startActivity(new Intent(this,HomeActivity.class));
            saveUserInfo(objectId);
        }
    }

    //用户记住密码时，进入软件，获取用户信息，供其他人使用
    private void saveUserInfo(final String objectId) {

        BmobQuery<UserInfo> bmobQuery = new BmobQuery();
        Log.i(TAG,"objectId"+objectId);

        bmobQuery.getObject(this, objectId, new GetListener<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                finish();
                Log.i(TAG,"onSuccess");
                //将登陆用户信息保存在ApplicationInfo类的UserInfo对象里面
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
                ApplicationInfo.editor.putInt("userpoints",userpoints);
                ApplicationInfo.editor.putString("username",username);
                ApplicationInfo.editor.commit();

                ApplicationInfo.initUserInfo(objectId,userid,username,password,usericon,phone,autograph,
                        collectedArticle,collectedWord,exchangeAwarded,exchangeAward,articleCount,wordCount,
                        userrank,userpoints);
            }

            @Override
            public void onFailure(int i, String s) {
                //登陆失败，初始化数据为空
                ApplicationInfo.initUserInfo(objectId,"","","","","","", 0,0,0,0,0,0, 0,0);
                Toast.makeText(MainActivity.this,"获取用户信息失败",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    //登陆按钮响应事件
    public void login(View view){

        //验证登陆，登陆成功，则跳到主页面
        String phone=ed_initpage_phone.getText().toString();
        String password=ed_initpage_password.getText().toString();

        //输入信息验证
        if (phone.isEmpty()||password.isEmpty()){
            Toast.makeText(this,"请输入用户名或密码",Toast.LENGTH_SHORT).show();
            return;
        }
        //登陆验证
        validate(phone,password);
    }

    //验证是否时注册用户
    private void validate(final String phone, final String password) {
        showDialog("正在登陆...");
        //查找UserInfo表里面id为  XXX  的数据
        BmobQuery<UserInfo> bmobQuery = new BmobQuery();

        bmobQuery.addWhereEqualTo("phone",phone);
        bmobQuery.findObjects(this, new FindListener<UserInfo>() {
            @Override
            public void onSuccess(List<UserInfo> list) {
                hideDialog();
                //查询到对应的信息，则注册过，跳到主页面
                if (list.size()==0){
                    //没有查询到对应的信息，则没注册过，提示该用户未注册
                    Toast.makeText(MainActivity.this,"还未注册，请先进行注册",Toast.LENGTH_SHORT).show();
                }
                else {
                    UserInfo userInfo = list.get(0);
                    if (!userInfo.getPassword().equals(password)){
                        Toast.makeText(MainActivity.this,"密码不正确",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //将登陆用户信息保存在ApplicationInfo类的UserInfo对象里面
                    String objectId = userInfo.getObjectId();
                    String userid = userInfo.getUserid();
                    String username = userInfo.getUsername();
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


                    ApplicationInfo.editor.putInt("userpoints",userpoints);
                    ApplicationInfo.editor.putString("username",username);
                    ApplicationInfo.editor.commit();
                    ApplicationInfo.initUserInfo(objectId,userid,username,password,usericon,phone,autograph,
                            collectedArticle,collectedWord,exchangeAwarded,exchangeAward,articleCount,wordCount,
                            userrank,userpoints);

                    Log.i(TAG,userInfo.toString());
                    Log.i(TAG,"userid:"+userid);

                    boolean checked = ed_initpage_savepassword.isChecked();
                    //选择记住密码，保存SharedPreferences里，下次不用再登陆
                    if (checked){
                        SharedPreferences.Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();
                        editor.putBoolean("rememberInf",true);
                        editor.putString("objectId",userInfo.getObjectId());
                        editor.commit();
                    }
                    Toast.makeText(MainActivity.this,"欢迎:"+username,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(int i, String s) {
                //登陆失败，初始化数据为空
                ApplicationInfo.initUserInfo("","","",password,"",phone,"", 0,0,0,0,0,0, 0,0);
                hideDialog();
                Toast.makeText(MainActivity.this,"登陆失败"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //注册
    public void register(View view){
        //跳到注册页面，注册成功，则跳到主页面
        startActivityForResult(new Intent(this,RegisterActivity.class),120);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册成功，页面销毁
        if (resultCode==RESULT_OK && requestCode==120){
            if (data.getBooleanExtra("registOK",false)) {
                finish();
            }
        }
    }

    //忘记密码,进行找回密码
    public void forgetPassword(View view){
        //跳到找回密码页面
        startActivity(new Intent(this,RetrievePasswordActivity.class));
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

    /*
     * 使用 sharesdk qq登录
     * @param view
     */
    public void qqLogin(View view) {
        final Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                System.out.println("授权成功");

                Log.i(TAG, "用户 getToken()"+qq.getDb().getToken());
                Log.i(TAG,  "用户 exportData("+ qq.getDb().exportData());
                Log.i(TAG, "用户ID getPlatformNname()："+qq.getDb().getPlatformNname());
                Log.i(TAG,  "用户getTokenSecret()："+qq.getDb().getTokenSecret());
                Log.i(TAG,  "用户性别"+qq.getDb().getUserGender());
                Log.i(TAG, "用户icon"+qq.getDb().getUserIcon());
                Log.i(TAG, "用户 getUserId()"+qq.getDb().getUserId());
                Log.i(TAG,  "用户 getUserName()"+qq.getDb().getUserName());
                Log.i(TAG,  "用户 getExpiresIn()"+qq.getDb().getExpiresIn() + "");
                Log.i(TAG,  "用户ID getExpiresTime()"+qq.getDb().getExpiresTime() + "");
                //ApplicationInfo.initNickname(qq.getDb().getUserName());

                String userName = qq.getDb().getUserName();
                //将第三方登陆的账号信息保存在服务器端数据库
                InitthirdLoginData(userName);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.i(TAG, "授权失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.i(TAG, "取消授权");
            }
        });
        qq.authorize();


    }

    /*
     * 使用 sharesdk 新浪微博 登录
     * @param view
     */
    public void weiboLogin(View view) {
        final Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        Log.i(TAG, weibo.toString());

        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            //授权成功
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.i(TAG, "onComplete");

                Log.d(TAG, weibo.getDb().getToken());
                Log.d(TAG, weibo.getDb().getToken());
                Log.d(TAG, weibo.getDb().exportData());
                Log.d(TAG, weibo.getDb().getPlatformNname());
                Log.d(TAG, weibo.getDb().getTokenSecret());
                Log.d(TAG, weibo.getDb().getUserGender());
                Log.d(TAG, weibo.getDb().getUserIcon());
                Log.d(TAG, weibo.getDb().getUserId());
                Log.d(TAG, weibo.getDb().getUserName());
                Log.d(TAG, weibo.getDb().getExpiresIn() + "");
                Log.d(TAG, weibo.getDb().getExpiresTime() + "");

                String userName = weibo.getDb().getUserName();
                //将第三方登陆的账号信息保存在服务器端数据库
                //InitthirdLoginData(userName);

                //Toast.makeText(MainActivity.this, "登陆失败", Toast.LENGTH_LONG).show();

            }

            @Override
            //授权失败
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG, "授权失败");
                Toast.makeText(MainActivity.this, "登陆失败", Toast.LENGTH_LONG).show();

            }

            @Override
            //授权取消
            public void onCancel(Platform platform, int i) {
                Log.i(TAG, "取消授权");
                Toast.makeText(MainActivity.this, "取消登陆", Toast.LENGTH_LONG).show();
            }
        });

        weibo.authorize();

        //移除授权
        //weibo.removeAccount(true);

    }

    private void InitthirdLoginData(final String userName) {
       //第三方数据保存数据库操作
        final UserInfo userInfo = new UserInfo();

        userInfo.setUsername(userName);
        //利用第三章用户账号的用户名生成对应的md5码，以作为用户id
        final String userid = Md5Utils.getMd5Message(userName);
        userInfo.setUserid(userid);

        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();

        bmobQuery.addWhereEqualTo("userid",userid);
        bmobQuery.findObjects(this, new FindListener<UserInfo>() {

            @Override
            public void onSuccess(List<UserInfo> list) {
                if (list!=null && list.size()!=0){
                    final UserInfo userInfo = list.get(0);
                    //存在该用户，第二次登陆
                    if (userInfo != null) {
                        //获取该用户在数据库里保存的信息
                        //保存用户信息，供应用程序访问
                        ApplicationInfo.editor.putInt("userpoints", userInfo.getUserpoints());
                        ApplicationInfo.editor.putString("username",userName);
                        ApplicationInfo.editor.commit();
                        ApplicationInfo.initUserInfo(userInfo.getObjectId(), userid, userName, userInfo.getPassword(),
                                userInfo.getUsericon(), userInfo.getPhone(), userInfo.getAutograph(),
                                userInfo.getCollectedArticle(), userInfo.getCollectedWord(), userInfo.getExchangeAwarded(),
                                userInfo.getExchangeAward(), userInfo.getArticleCount(), userInfo.getWordCount(),
                                userInfo.getUserrank(), userInfo.getUserpoints());
                        Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    }
                }
                else {
                    //不存在该用户，是第一次登陆
                    //保存用户信息到数据库
                    userInfo.save(MainActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_LONG).show();

                            //保存用户信息，供应用程序访问
                            ApplicationInfo.editor.putInt("userpoints", userInfo.getUserpoints());
                            ApplicationInfo.editor.putString("username",userName);
                            ApplicationInfo.editor.commit();
                            ApplicationInfo.initUserInfo(userInfo.getObjectId(), userid, userName, userInfo.getPassword(),
                                    userInfo.getUsericon(), userInfo.getPhone(), userInfo.getAutograph(),
                                    userInfo.getCollectedArticle(), userInfo.getCollectedWord(), userInfo.getExchangeAwarded(),
                                    userInfo.getExchangeAward(), userInfo.getArticleCount(), userInfo.getWordCount(),
                                    userInfo.getUserrank(), userInfo.getUserpoints());
                            finish();
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(MainActivity.this, "登陆失败", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this, "登陆失败，请检查网络", Toast.LENGTH_LONG).show();
            }
        });
    }
}