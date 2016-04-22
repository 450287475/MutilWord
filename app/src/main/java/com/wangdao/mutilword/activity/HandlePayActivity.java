package com.wangdao.mutilword.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.GoodInfo;
import com.wangdao.mutilword.bean.GoodsOrder;
import com.wangdao.mutilword.bean.ShoppingCart;
import com.wangdao.mutilword.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class HandlePayActivity extends ActionBarActivity {

    private static final String TAG = "HandlePayActivity";
    private ProgressDialog dialog;
    private TextView tv_handlepay_goodsname;
    private TextView tv_handlepay_goodpointsormoney;
    private EditText ev_handlepay_address;
    private EditText ev_handlepay_phone;
    private EditText ev_handlepay_usersname;
    private ShoppingCart shoppingCart;
    private GoodsOrder goodsOrder;
    private GoodInfo goodInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_pay);

        tv_handlepay_goodsname = (TextView) findViewById(R.id.tv_handlepay_goodsname);
        tv_handlepay_goodpointsormoney = (TextView) findViewById(R.id.tv_handlepay_goodpointsormoney);
        ev_handlepay_address = (EditText) findViewById(R.id.ev_handlepay_address);
        ev_handlepay_phone = (EditText) findViewById(R.id.ev_handlepay_phone);
        ev_handlepay_usersname = (EditText) findViewById(R.id.ev_handlepay_usersname);

        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");
        Bundle bundle = intent.getBundleExtra("bundle");
        goodInfo = bundle.getParcelable("goodInfo");
        goodInfo.setObjectId(objectId);


        tv_handlepay_goodsname.setText(goodInfo.getName());
        tv_handlepay_goodpointsormoney.setText(goodInfo.getPoints()+"");
    }

    public void submitOrder(View view) {

        Log.i(TAG,goodInfo.toString());

        final int points = goodInfo.getPoints();  //商品需要的积分数
        int userpoints = ApplicationInfo.userInfo.getUserpoints(); //用户现有的积分数



        String address = ev_handlepay_address.getText().toString();
        String phone = ev_handlepay_phone.getText().toString();
        String usersname = ev_handlepay_usersname.getText().toString();


        goodsOrder = new GoodsOrder();
        goodsOrder.setUserAddress(address);
        goodsOrder.setUserPhone(phone);
        goodsOrder.setUsername(usersname);
        goodsOrder.setUserid(ApplicationInfo.userInfo.getUserid());
        goodsOrder.setGoodId(goodInfo.getGoodId());
        goodsOrder.setGoodsname(goodInfo.getName());

        shoppingCart = new ShoppingCart();

        shoppingCart.setGoodId(goodInfo.getGoodId());
        shoppingCart.setUserid(ApplicationInfo.userInfo.getUserid());
        shoppingCart.setIconurl(goodInfo.getIconurl());
        shoppingCart.setName(goodInfo.getName());

        long goodid = System.currentTimeMillis();
        goodsOrder.setOrderId(goodid+"");

        //自己现有的积分不够,调用祝福吧接口进行付款
        if (points > userpoints) {
            //得到需要支付多少钱
            final float money = goodInfo.getMoney();
            goodsOrder.setMoney(money);
            //调用Bmob的支付几口进行支付

            new AlertDialog.Builder(this)
                    .setTitle("积分不足啦")
                    .setMessage("您的积分不足以兑换奖品，请按确认进行支付宝支付吧")
                    .setPositiveButton("支付宝付款", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            payByAli(money);
                        }
                    })
                    .setNegativeButton("退出支付",null)
                    .show();
        }
        //有足够积分
        else {
            Log.i(TAG, goodsOrder.toString());
            goodsOrder.setPoints(points);
            showDialog("正在提交订单");
            goodInfo.update(HandlePayActivity.this, new UpdateListener() {
                @Override
                public void onSuccess() {
                    hideDialog();
                    //showDialog("正在兑换");
                    UserInfo userInfo = ApplicationInfo.userInfo;
                    //购买成功，积分减少
                    int newPoints = userInfo.getUserpoints() - points;
                    userInfo.setUserpoints(newPoints);
                    userInfo.setExchangeAwarded(userInfo.getExchangeAwarded()+1);
                    userInfo.update(HandlePayActivity.this, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            //保存订单信息
                            //Bmob批量处理数据，同时向goodsOrder和shoppingCart里添加数据
                            List<BmobObject> userBeans = new ArrayList<BmobObject>();
                            userBeans.add(goodsOrder);
                            userBeans.add(shoppingCart);
                            BmobObject bmobObject = new BmobObject();
                            bmobObject.insertBatch(HandlePayActivity.this, userBeans, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(HandlePayActivity.this,"兑换成功",Toast.LENGTH_SHORT).show();
                                    finish();  //退出当前提交订单页面
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(HandlePayActivity.this,"提交订单失败"+s,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            //hideDialog();
                            Toast.makeText(HandlePayActivity.this,"领取失败"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {
                    hideDialog();
                    Toast.makeText(HandlePayActivity.this,"用户id不存在"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 调用支付宝支付，price为支付金额
    void payByAli( double price) {
        showDialog("正在检查信息...");

        final String username = "";
        String housinfo="";

        BP.pay(this, username, housinfo, price, true, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(HandlePayActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT).show();
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(HandlePayActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                hideDialog();
                //保存订单信息
                //Bmob批量处理数据，同时向goodsOrder和shoppingCart里添加数据
                List<BmobObject> userBeans = new ArrayList<BmobObject>();
                userBeans.add(goodsOrder);
                userBeans.add(shoppingCart);
                BmobObject bmobObject = new BmobObject();
                bmobObject.insertBatch(HandlePayActivity.this, userBeans, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(HandlePayActivity.this,"兑换成功",Toast.LENGTH_SHORT).show();
                        finish();  //退出当前提交订单页面
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(HandlePayActivity.this,"提交订单失败"+s,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                Toast.makeText(HandlePayActivity.this, "支付中断!", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    public void back(View view){
        finish();
    }

}
