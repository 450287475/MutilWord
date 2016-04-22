package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.GoodInfo;
import com.wangdao.mutilword.bean.UserInfo;

import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.listener.UpdateListener;

public class ShowGoodDetailActivity extends Activity {

    private static final String TAG = "ShowGoodDetailActivity";
    private ImageView iv_showgood_image;
    private TextView tv_showgood_changedcount;
    private TextView tv_showgood_leftcount;
    private TextView tv_showgood_name;

    private Intent intent;
    private GoodInfo goodInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_good_detail);

        String APPID = "d8aca0c0e17c711bfb65e82127887c2c";
        BP.init(this, APPID);

        iv_showgood_image = (ImageView) findViewById(R.id.iv_showgood_image);
        tv_showgood_changedcount = (TextView) findViewById(R.id.tv_showgood_changedcount);
        tv_showgood_name = (TextView) findViewById(R.id.tv_showgood_name);
        tv_showgood_leftcount = (TextView) findViewById(R.id.tv_showgood_leftcount);
        showGood();
    }

    public void showGood(){
        intent = getIntent();
        String iconurl = intent.getStringExtra("iconurl");
        String goodId = intent.getStringExtra("goodId");
        String name = intent.getStringExtra("name");
        String objectId = intent.getStringExtra("objectId");
        int changedcount = intent.getIntExtra("changedcount", 0);
        int leftcount = intent.getIntExtra("leftcount", 0);
        int points = intent.getIntExtra("points", 0);
        float money = intent.getFloatExtra("money",0);

        BitmapUtils bitmapUtils = new BitmapUtils(this);
        bitmapUtils.display(iv_showgood_image,iconurl);

        tv_showgood_name.setText(name);
        tv_showgood_changedcount.setText(changedcount +"个");
        tv_showgood_leftcount.setText(leftcount +"个");

        goodInfo = new GoodInfo();
        goodInfo.setGoodId(goodId);
        goodInfo.setObjectId(objectId);
        goodInfo.setName(name);
        goodInfo.setChangedcount(changedcount);
        goodInfo.setLeftcount(leftcount);
        goodInfo.setPoints(points);
        goodInfo.setMoney(money);


    }

    private void updateGoodInfo(final GoodInfo goodInfo) {
        new AlertDialog.Builder(this)
                .setTitle("兑换")
                .setMessage("确认兑换吗？需要"+goodInfo.getPoints()+"个积分")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳到填写填写订单详情页
                        Intent intent = new Intent(ShowGoodDetailActivity.this,HandlePayActivity.class);
                        intent.putExtra("objectId",goodInfo.getObjectId());

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("goodInfo",goodInfo);
                        intent.putExtra("bundle",bundle);

                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    //点击按钮，进行兑换
    public void startChanged(View view){
        int changedcount = goodInfo.getChangedcount();
        int leftcount = goodInfo.getLeftcount();

        //兑换时间库存量进行更新
        goodInfo.setChangedcount(++changedcount);
        goodInfo.setLeftcount(--leftcount);
        updateGoodInfo(goodInfo);
    }


}


