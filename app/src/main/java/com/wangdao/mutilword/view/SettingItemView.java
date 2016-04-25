package com.wangdao.mutilword.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangdao.mutilword.R;


/**
 * Created by MonkeyzZi on 2016/4/22.
 */
public class SettingItemView extends RelativeLayout {
    private TextView tv_title;
    private TextView tv_desc;
    private CheckBox cb_status;
    private String attributeName;
    private String attributeValue;
    private String NAMESPACE="http://schemas.android.com/apk/res-auto";
    private String mTitle;
    private String mDescOn;
    private String mDescOff;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTitle =attrs.getAttributeValue(NAMESPACE,"itemtitle");
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");


        initView();

    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    private void initView() {
        //将自定义好的布局文件设置给当前的SettingItemView
        View.inflate(getContext(), R.layout.view_setting_item, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        cb_status = (CheckBox) findViewById(R.id.cb_status);

        tv_title.setTextColor(Color.parseColor("#AEEEEE"));
        tv_desc.setTextColor(Color.parseColor("#AEEEEE"));
        cb_status.setTextColor(Color.parseColor("#AEEEEE"));

       // tv_title.setTextSize(DisplayUtils.dip2px());

        setTitle(mTitle);//设置标题
    }

    private void setTitle(String title) {
        tv_title.setText(title);
    }
    public void setDesc(String desc){
        tv_desc.setText(desc);
    }
    //返回勾选状态
    public boolean isChecked(){
        return cb_status.isChecked();
    }
    public void setChecked(boolean check){
        cb_status.setChecked(check);
        //根据选择的状态，更新文本描述
        if (check){
            setDesc(mDescOn);
        }else {
            setDesc(mDescOff);
        }

    }
}
