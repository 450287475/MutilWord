package com.wangdao.mutilword.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.view.SettingClickView;
import com.wangdao.mutilword.view.SettingItemView;


public class SettingsFragment extends Fragment {

    private View parentView;
    private SharedPreferences mPrefs;
    private SettingItemView siv_update;
    private SettingItemView siv_info;
    private SettingClickView scv_words;
    private SettingClickView scv_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_settings, container, false);
        mPrefs = getActivity().getSharedPreferences("config", getActivity().MODE_PRIVATE);

        initUpdateView();
        initInfo();
        initWordsNum();
        initLogout();
        return parentView;
    }

    private void initLogout() {
        scv_logout =(SettingClickView)parentView.findViewById(R.id.scv_logout);
        scv_logout.setTitle("注销用户信息");
        scv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
                Toast.makeText(getActivity(), "注销成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //注销用户信息
    private void Logout() {

    }

    final String[] items = new String[]{"30个","40个","50个","60个"};
    private void initWordsNum() {
        scv_words =(SettingClickView)parentView.findViewById(R.id.scv_words);
        scv_words.setTitle("每天我要背的单词数");
        scv_words.setDesc("30个");
        scv_words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDialog();
            }
        });

    }

    private void showSingleChooseDialog() {
        int num = mPrefs.getInt("words_num",0);
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.bee)
                .setTitle("单词数模式")
                .setSingleChoiceItems(items, num, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //保存选择的单词数量
                        mPrefs.edit().putInt("words_num",which).commit();
                        dialog.dismiss();
                        //更新组合控件的描述信息
                        scv_words.setDesc(items[which]);

                    }
                }).setNegativeButton("取消",null).show();
    }

    //初始化推送设置开关
    private void initInfo() {
        siv_info =(SettingItemView)parentView.findViewById(R.id.siv_info);

        boolean autoUpdate = mPrefs.getBoolean("auto_update",true);
        if (autoUpdate){
            siv_info.setChecked(true);
        }else {
            siv_info.setChecked(false);
        }
        siv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断勾选状态
                if (siv_info.isChecked()){
                    //设置不勾选
                    siv_info.setChecked(false);
                    siv_info.setDesc("推送通知已关闭");
                    mPrefs.edit().putBoolean("auto_info",false).commit();
                }else {
                    siv_info.setChecked(true);
                    siv_info.setDesc("推送通知已开启");
                    mPrefs.edit().putBoolean("auto_info",true).commit();
                }
            }
        });
    }

    //初始化自动更新开关
    private void initUpdateView() {
        siv_update =(SettingItemView)parentView.findViewById(R.id.siv_update);

        boolean autoUpdate = mPrefs.getBoolean("auto_update",true);
        if (autoUpdate){
            siv_update.setChecked(true);
        }else {
            siv_update.setChecked(false);
        }
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断勾选状态
                if (siv_update.isChecked()){
                    //设置不勾选
                    siv_update.setChecked(false);
                    siv_update.setDesc("自动更新已关闭");
                    mPrefs.edit().putBoolean("auto_update",false).commit();
                }else {
                    siv_update.setChecked(true);
                    siv_update.setDesc("自动更新已开启");
                    mPrefs.edit().putBoolean("auto_update",true).commit();
                }
            }
        });
    }
}
