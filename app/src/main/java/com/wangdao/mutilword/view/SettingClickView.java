package com.wangdao.mutilword.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangdao.mutilword.R;


/**
 * Created by MonkeyzZi on 2016/4/22.
 */
public class SettingClickView extends RelativeLayout {

    private TextView tv_title ;
    private TextView tv_desc ;

    public SettingClickView(Context context) {
        super(context);
        initView();
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }
    //初始化布局
    private void initView() {
        //将自定义好的布局文件设置给当前的SettinglickView
        View.inflate(getContext(), R.layout.view_setting_click,this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_title.setTextColor(Color.parseColor("#AEEEEE"));
        tv_desc.setTextColor(Color.parseColor("#AEEEEE"));

    }
    public void setTitle(String title){
        tv_title.setText(title);
    }
    public void setDesc(String desc){
        tv_desc.setText(desc);
    }

}
