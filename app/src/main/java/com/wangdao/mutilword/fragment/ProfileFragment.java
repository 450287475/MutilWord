package com.wangdao.mutilword.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangdao.mutilword.R;


public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("~~~~~");
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    //需要处理点击事件的or有子控件如viewpager不需要触发侧边栏的功能，请参考HomeFragment中的代码

}
