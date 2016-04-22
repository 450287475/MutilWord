package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.fragment.AwardMallFragment;
import com.wangdao.mutilword.fragment.HadAwardFragment;

public class AwardActivity extends Activity {

    private TextView tv_award_city;
    private TextView tv_award_changed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);

        tv_award_city = (TextView) findViewById(R.id.tv_award_city);
        tv_award_changed = (TextView) findViewById(R.id.tv_award_changed);

        tv_award_city.setBackgroundColor(Color.parseColor("#CDAA7D"));
        tv_award_changed.setBackgroundColor(Color.parseColor("#FFFFFF"));

        FragmentManager fragmentManager = getFragmentManager();
        HadAwardFragment hadAwardFragment = (HadAwardFragment) fragmentManager.findFragmentById(R.id.fm_award_had);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(hadAwardFragment);
        fragmentTransaction.commit();
    }


    //转到AwardMallFragment
    public void toAwardMall(View view){
        tv_award_city.setBackgroundColor(Color.parseColor("#CDAA7D"));
        tv_award_changed.setBackgroundColor(Color.parseColor("#FFFFFF"));

        FragmentManager fragmentManager = getFragmentManager();
        AwardMallFragment awardMallFragment = (AwardMallFragment) fragmentManager.findFragmentById(R.id.fm_award_mall);
        HadAwardFragment hadAwardFragment = (HadAwardFragment) fragmentManager.findFragmentById(R.id.fm_award_had);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.fm_award_had,hadAwardFragment);  //替换Fragment
        fragmentTransaction.hide(hadAwardFragment);
        fragmentTransaction.show(awardMallFragment);
        fragmentTransaction.commit();

    }

    //转到HadAwardFragment
    public void toHadAward(View view){
        tv_award_city.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tv_award_changed.setBackgroundColor(Color.parseColor("#CDAA7D"));
        FragmentManager fragmentManager = getFragmentManager();
        AwardMallFragment awardMallFragment = (AwardMallFragment) fragmentManager.findFragmentById(R.id.fm_award_mall);
        HadAwardFragment hadAwardFragment = (HadAwardFragment) fragmentManager.findFragmentById(R.id.fm_award_had);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.fm_award_mall,awardMallFragment);  //替换Fragment
        fragmentTransaction.hide(awardMallFragment);
        fragmentTransaction.show(hadAwardFragment);
        fragmentTransaction.commit();
    }
}
