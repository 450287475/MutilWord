package com.wangdao.mutilword.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.fragment.HomeFragment;
import com.wangdao.mutilword.fragment.InterpretFragment;
import com.wangdao.mutilword.fragment.ProfileFragment;
import com.wangdao.mutilword.fragment.SettingsFragment;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*ExplosionField explosionField = new ExplosionField(HomeActivity.this);
        explosionField.addListener(findViewById(R.id.ll_root));
*/

        //created by yxd for reside menu
        mContext = this;
        setUpMenu();
        if( savedInstanceState == null )
            changeFragment(new HomeFragment());

        View homefragment = View.inflate(HomeActivity.this, R.layout.fragment_home, null);
    }

    public void goToReWord(View view) {
        startActivity(new Intent(this,ChooseWordTypeActivity.class));
    }

    //create by yxd for reside menu from 45-136
    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     " 主 页");
        itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "个人中心");
        itemInterpret = new ResideMenuItem(this, R.drawable.icon_interpret, "查&译");
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