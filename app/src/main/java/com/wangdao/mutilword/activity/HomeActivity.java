package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.fragment.HomeFragment;
import com.wangdao.mutilword.fragment.InterpretFragment;
import com.wangdao.mutilword.fragment.ProfileFragment;
import com.wangdao.mutilword.fragment.SettingsFragment;
import com.wangdao.mutilword.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeActivity extends FragmentActivity implements View.OnClickListener{
    //created by yxd for reside menu
    private ResideMenu resideMenu;
    private HomeActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemInterpret;
    private ResideMenuItem itemSettings;

    private int currentFragment = 0;
    private final int FRAGMENT_HOME = 0;
    private final int FRAGMENT_SETTINGS = 1;
    private final int FRAGMENT_PROFILE = 2;
    private final int FRAGMENT_INTERPRET = 3;
    private boolean hasPressedBack;

    private SharedPreferences mPrefs;
    private TextView tv_version;
    private TextView tv_progress;
    private String mVersionName;
    private int mVersionCode;
    private String mDesc;
    protected static final int CODE_UPDATE_DIALOG=0;
    protected static final int CODE_URL_ERROR=1;
    protected static final int CODE_NET_ERROR=2;
    protected static final int CODE_JSON_ERROR=3;
    protected static final int CODE_ENTER_HOME=4;


    private String mDownloadUrl;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CODE_UPDATE_DIALOG:
                    showUpdateDailog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(HomeActivity.this, "url错误", Toast.LENGTH_SHORT).show();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(HomeActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(HomeActivity.this, "json错误", Toast.LENGTH_SHORT).show();
                    break;
                case CODE_ENTER_HOME:
                    break;

                default:
                    break;


            }
            super.handleMessage(msg);
        }
    };
    public int setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting = getIntent().getIntExtra("setting",-1);

        setContentView(R.layout.activity_home);
        mPrefs = getSharedPreferences("config", MODE_PRIVATE);
        tv_version = (TextView)findViewById(R.id.tv_version);
        tv_version.setText("版本号:" + getVersionName());
        //默认隐藏
        tv_progress = (TextView)findViewById(R.id.tv_progress);
        //判断是否需要自动更新
        boolean autoUpdate = mPrefs.getBoolean("auto_update", true);
        if (autoUpdate){
            checkVersion();
        }else {
            myHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME,2000);//延时2s后发送消息
        }
        //changecolor();
        //created by yxd for reside menu
        mContext = this;
        setUpMenu();
        if( savedInstanceState == null )
            changeFragment(new HomeFragment());

        View homefragment = View.inflate(HomeActivity.this, R.layout.fragment_home, null);
    }
    private  String getVersionName(){
        PackageManager packageManager = getPackageManager();
        try {
            //获取包信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            Log.i("versionCode&versionName", versionCode + " " + versionName + " ");

            return versionName ;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";

    }private  int getVersionCode(){
        PackageManager packageManager = getPackageManager();
        try {
            //获取包信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode ;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;

    }
    /*从服务器获取版本信息进行校验
    *
    * */
    private void checkVersion(){
        final long startTime = System.currentTimeMillis();

        new Thread(){
            @Override
            public void run() {
                Message msg = Message.obtain();
                HttpURLConnection conn =null;
                try {
                    URL url = new URL("http://bmob-cdn-418.b0.upaiyun.com/2016/04/25/15df89894075826b80d1d6de83756b10.json");
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");//设置请求方法
                    conn.setConnectTimeout(5000);//设置连接超时
                    conn.setReadTimeout(5000);//设置响应超时，连接上了，但是服务器迟迟不给响应
                    conn.connect();//连接服务器

                    int responseCode = conn.getResponseCode();
                    if (responseCode==200){
                        InputStream inputStream = conn.getInputStream();
                        String result = StreamUtils.readFromStream(inputStream);
                        Log.i("网络返回:",result+"");

                        //解析json
                        JSONObject jo = new JSONObject(result);

                        mVersionName = jo.getString("versionName");
                        mVersionCode = jo.getInt("versionCode");
                        mDesc = jo.getString("description");
                        mDownloadUrl = jo.getString("downloadUrl");

                        Log.i("版本描述:",mDownloadUrl);
                        if (mVersionCode>getVersionCode()){
                            //判断是否有更新
                            //服务器的VersionCode大于本地的VersionCode
                            msg.what = CODE_UPDATE_DIALOG;
                        }else{
                            //没有版本更新
                            msg.what=CODE_ENTER_HOME;
                        }

                    }


                } catch (MalformedURLException e) {
                    //url错误的异常
                    msg.what= CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    //网络错误的异常
                    msg.what= CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    //json解析失败
                    msg.what= CODE_JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    long endTime = System.currentTimeMillis();
                    //访问网络花费的时间
                    long timeUsed= endTime-startTime;

                    if (timeUsed<5000){
                        //强制休眠一段时间，保证闪屏页显示5秒钟
                        try {
                            Thread.sleep(5000-timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    myHandler.sendMessage(msg);

                    if (conn!=null){
                        conn.disconnect();
                    }
                }

            }
        }.start();

    }
    protected void showUpdateDailog(){
        Activity activity = HomeActivity.this;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("发现新版本" + mVersionName)
                .setMessage(mDesc)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Tag", "立即更新");
                        download();
                    }
                })
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).show();
    }
    //下载apk文件
    protected void download(){
        //判断是否有sdcard

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {

            tv_progress.setVisibility(View.VISIBLE);//显示进度
            String target = Environment.getExternalStorageDirectory()+"/updateWords.apk";
            //XUtils
            HttpUtils utils = new HttpUtils();
            utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
                //下载文件的进度
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Log.i("下载进度:", current + "/" + total);
                    tv_progress.setText("下载进度:"+current*100/total+"%");
                }
                //下载成功
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.i("下载成功！！！","");
                    //跳转到系统下载页面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
                    // startActivity(intent);
                    startActivityForResult(intent,0);//如果用户取消安装的话，会返回结果

                }
                //下载失败
                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(HomeActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(HomeActivity.this, "您的手机没有SD卡", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onStart() {
        String username = ApplicationInfo.sp.getString("username", "");
        ApplicationInfo.userInfo.setUsername(username);
        int userpoints = ApplicationInfo.sp.getInt("userpoints", -1);
        ApplicationInfo.userInfo.setUserpoints(userpoints);
        if (setting==1){
            changeFragment(new ProfileFragment());
            setting=-1;
        }
        super.onStart();
    }

    /*private void changecolor() {
                Window window = this.getWindow();
                //设置透明状态栏,这样才能让 ContentView 向上
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏颜色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(Color.RED);
                }
            }*/
    public void goToReWord(View view) {
        startActivity(new Intent(this,ChooseWordTypeActivity.class));
    }

    //create by yxd for reside menu from 45-136
    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_homebackground_star);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     " 主 页");
        itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "个人中心");
        itemInterpret = new ResideMenuItem(this, R.drawable.ic_interpret, "查&译");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, " 设 置");

        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemInterpret.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemInterpret, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome){
            currentFragment = FRAGMENT_HOME;
            changeFragment(new HomeFragment());
        }else if (view == itemProfile){
            currentFragment = FRAGMENT_PROFILE;
            changeFragment(new ProfileFragment());
        }else if (view == itemInterpret){
            currentFragment = FRAGMENT_INTERPRET;
            changeFragment(new InterpretFragment());
        }else if (view == itemSettings){
            currentFragment = FRAGMENT_SETTINGS;
            changeFragment(new SettingsFragment());
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            //Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void closeMenu() {
            //Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();

        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){

        return resideMenu;
    }

    //解决在侧边栏opened状态下，按返回键退出应用的bug，现直接回到之前的页面
    //解决在侧边栏closed状态下，按返回键推出应用的bug
    //主页面按back键提示再按一次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && (!resideMenu.isOpened()) && currentFragment == FRAGMENT_HOME) {
            if (hasPressedBack) {
                finish();
                return true;
            }
            hasPressedBack = true;
            Toast.makeText(this, "再按一次退出MutiWords!", Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hasPressedBack = false;
                }
            }, 3000);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && (!resideMenu.isOpened()) && currentFragment != FRAGMENT_HOME) {
            switch (currentFragment) {

                case FRAGMENT_SETTINGS:
                    currentFragment = FRAGMENT_HOME;
                    changeFragment(new HomeFragment());
                    break;
                case FRAGMENT_INTERPRET:
                    currentFragment = FRAGMENT_HOME;
                    changeFragment(new HomeFragment());
                    break;
                case FRAGMENT_PROFILE:
                    currentFragment = FRAGMENT_HOME;
                    changeFragment(new HomeFragment());
                    break;
                default:
                    break;
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && resideMenu.isOpened()) {
            switch (currentFragment){
                case FRAGMENT_HOME:
                    changeFragment(new HomeFragment());
                    break;
                case FRAGMENT_SETTINGS:
                    changeFragment(new SettingsFragment());
                    break;
                case FRAGMENT_INTERPRET:
                    changeFragment(new InterpretFragment());
                    break;
                case FRAGMENT_PROFILE:
                    changeFragment(new ProfileFragment());
                    break;
                default:
                    break;
            }
            resideMenu.closeMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}