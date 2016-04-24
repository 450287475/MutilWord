package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wangdao.mutilword.BuildConfig;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.utils.DisplayUtils;
import com.wangdao.mutilword.utils.StreamUtils;
import com.wangdao.mutilword.view.spalshview.MainView;
import com.wangdao.mutilword.view.spalshview.SplashView;

import org.json.JSONException;
import org.json.JSONObject;


public class SpalshActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final boolean DO_XML = false;

    private ViewGroup mMainView;
    private SplashView mSplashView;
    private Handler mHandler = new Handler();
    private SharedPreferences mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences("config", MODE_PRIVATE);

        // change the DO_XML variable to switch between code and xml
        if(DO_XML){
            // inflate the view from XML and then get a reference to it
            setContentView(R.layout.activity_main);
            mMainView = (ViewGroup) findViewById(R.id.main_view);
            mSplashView = (SplashView) findViewById(R.id.splash_view);
        } else {
            // create the main view and it will handle the rest
            mMainView = new MainView(getApplicationContext());
            mSplashView = ((MainView) mMainView).getSplashView();
            setContentView(mMainView);
        }

        // pretend like we are loading data
        startLoadingData();


    }


    private void startLoadingData(){
        // finish "loading data" in a random time between 1 and 3 seconds
        Random random = new Random();
        mHandler.postDelayed(new Runnable(){
            @Override
            public void run(){
                onLoadingDataEnded();
            }
        }, 3000);  //1000为延时的秒数
    }

    private void onLoadingDataEnded(){
        Context context = getApplicationContext();
        // start the splash animation
        mSplashView.splashAndDisappear(new SplashView.ISplashListener(){
            @Override
            public void onStart(){
                // log the animation start event
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash started");
                }
            }

            @Override
            public void onUpdate(float completionFraction){
                // log animation update events
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash at " + String.format("%.2f", (completionFraction * 100)) + "%");
                }
            }

            @Override
            public void onEnd(){
               /* startActivity(new Intent(SpalshActivity.this,GuideActivity.class));
                finish();*/
                // log the animation end event
                if(BuildConfig.DEBUG){
                    Log.d(TAG, "splash ended");
                    boolean userGuide = mPrefs.getBoolean("is_user_guide_showed", false);

                    if (!userGuide){
                        startActivity(new Intent(SpalshActivity.this,GuideActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SpalshActivity.this, MainActivity.class));

                    }
                    finish();
                }
                // free the view so that it turns into garbage
                mSplashView = null;
                if(!DO_XML){
                    // if inflating from code we will also have to free the reference in MainView as well
                    // otherwise we will leak the View, this could be done better but so far it will suffice
                    ((MainView) mMainView).unsetSplashView();
                }
            }
        });
    }
}
