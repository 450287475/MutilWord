package com.wangdao.mutilword.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.activity.AwardActivity;
import com.wangdao.mutilword.activity.SeeCollectedArtivleActivity;
import com.wangdao.mutilword.activity.SignActivity;
import com.wangdao.mutilword.activity.UserCenterDetailActivity;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.UserInfo;
import com.wangdao.mutilword.dao.SignDao;


public class ProfileFragment extends Fragment {
    private boolean isFirstCome = false;
    private static final String TAG = "ProfileFragment";
    private RelativeLayout rl_personcenter_seeinf;
    private ImageView iv_personcenter_useriocn;
    private TextView tv_personcenter_username;
    private TextView tv_personcenter_autograph;
    private TextView tv_personcenter_ariiclecount;
    private TextView tv_personcenter_collectedarticlecount;
    private TextView tv_personcenter_userpoints;
    private TextView tv_personcenter_userrank;
    private LinearLayout rl_personcenter_collected;
    private LinearLayout tv_personcenter_award;
    public LinearLayout ll_personcenter_sign;
    public TextView tv_personcenter_sign;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_personal_center, container, false);

        rl_personcenter_seeinf = (RelativeLayout) inflate.findViewById(R.id.rl_personcenter_seeinf);
        rl_personcenter_collected = (LinearLayout) inflate.findViewById(R.id.rl_personcenter_collected);
        tv_personcenter_award = (LinearLayout) inflate.findViewById(R.id.tv_personcenter_award);


        iv_personcenter_useriocn = (ImageView) inflate.findViewById(R.id.iv_personcenter_useriocn);
        tv_personcenter_username = (TextView) inflate.findViewById(R.id.tv_personcenter_username);
        tv_personcenter_autograph = (TextView) inflate.findViewById(R.id.tv_personcenter_autograph);
        tv_personcenter_ariiclecount = (TextView) inflate.findViewById(R.id.tv_personcenter_ariiclecount);
        tv_personcenter_collectedarticlecount = (TextView) inflate.findViewById(R.id.tv_personcenter_collectedarticlecount);
        tv_personcenter_userpoints = (TextView) inflate.findViewById(R.id.tv_personcenter_userpoints);
        tv_personcenter_userrank = (TextView) inflate.findViewById(R.id.tv_personcenter_userrank);
        ll_personcenter_sign = (LinearLayout) inflate.findViewById(R.id.ll_personcenter_sign);
        tv_personcenter_sign = (TextView) inflate.findViewById(R.id.tv_personcenter_sign);

        //获取ApplicationInfo里保存的用户信息
        UserInfo userInfo = ApplicationInfo.userInfo;

        String username = userInfo.getUsername();
        String autograph = userInfo.getAutograph();
        String icon = userInfo.getUsericon();
        int collectedArticle = userInfo.getCollectedArticle();
        int collectedWord = userInfo.getCollectedWord();
        int exchangeAwarded = userInfo.getExchangeAwarded();
        int exchangeAward = userInfo.getExchangeAward();
        int articleCount = userInfo.getArticleCount();
        int wordCount = userInfo.getWordCount();
        int userpoints = userInfo.getUserpoints();
        int userrank = userInfo.getUserrank();

        //将用户信息显示在个人中心界面
        BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
        bitmapUtils.display(iv_personcenter_useriocn,icon);

        tv_personcenter_username.setText(username);
        tv_personcenter_autograph.setText(autograph);
        tv_personcenter_ariiclecount.setText(articleCount+"");
        tv_personcenter_collectedarticlecount.setText(collectedArticle+"");
        tv_personcenter_userpoints.setText(userpoints+"");
        tv_personcenter_userrank.setText(userrank+"");


        HandleOnclick(userInfo);

        return inflate;

    }

    @Override
    public void onStart() {
        super.onStart();
        SignDao signDao = new SignDao(getActivity());
        tv_personcenter_sign.setText(signDao.query().size()+"");
    }

    private void HandleOnclick(final UserInfo userInfo) {
        rl_personcenter_seeinf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserCenterDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("userInfo", userInfo);
                intent.putExtra("bundle", bundle);
                intent.putExtra("objectId", userInfo.getObjectId());
                Log.i(TAG,"ProfileFragment"+userInfo.toString());
                Log.i(TAG,"ProfileFragment"+userInfo.getObjectId());
                startActivityForResult(intent,100);
            }
        });

        rl_personcenter_collected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳到查看用户收藏文章页面
                startActivity(new Intent(getActivity(), SeeCollectedArtivleActivity.class));
            }
        });

        tv_personcenter_award.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AwardActivity.class);
                startActivityForResult(intent,110);
            }
        });

        ll_personcenter_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SignActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==103 && resultCode==100){
            UserInfo userInfoModified = data.getParcelableExtra("userInfoModified");

            //将用户信息显示在个人中心界面
            BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
            bitmapUtils.display(iv_personcenter_useriocn,userInfoModified.getUsericon());

            tv_personcenter_username.setText(userInfoModified.getUsername());
            tv_personcenter_autograph.setText(userInfoModified.getAutograph());
            tv_personcenter_ariiclecount.setText(userInfoModified.getArticleCount()+"");
            tv_personcenter_collectedarticlecount.setText(userInfoModified.getCollectedArticle()+"");
            tv_personcenter_userpoints.setText(userInfoModified.getUserpoints()+"");
            tv_personcenter_userrank.setText(userInfoModified.getUserrank()+"");
        }


    }


    //需要处理点击事件的or有子控件如viewpager不需要触发侧边栏的功能，请参考HomeFragment中的代码

}
