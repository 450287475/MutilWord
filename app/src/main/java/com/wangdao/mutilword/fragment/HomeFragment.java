package com.wangdao.mutilword.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.special.ResideMenu.ResideMenu;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.explosionview.ExplosionField;

;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:33
 * Mail: specialcyci@gmail.com
 */
public class HomeFragment extends Fragment {

    public  View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home, container, false);
        //参考代码，哥几个别删：以下四行可以使添加进忽略的view不触发侧边栏滑动
        /*HomeActivity parentActivity = (HomeActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        RelativeLayout ignored_view = (RelativeLayout) parentView.findViewById(R.id.ignore_view);
        resideMenu.addIgnoredView(ignored_view);*/
        ExplosionField explosionField = new ExplosionField(getActivity());
        explosionField.addListener(parentView.findViewById(R.id.ll_root));
        return parentView;
    }

        //参考代码，别删：按照以下的方式添加点击事件不会出现事件被拦截的情况
        /*private void setUpViews() {
        HomeActivity parentActivity = (HomeActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        // add gesture operation's ignored views
        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
        resideMenu.addIgnoredView(ignored_view);
    }*/

}
