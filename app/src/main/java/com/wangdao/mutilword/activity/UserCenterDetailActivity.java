package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.UserInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserCenterDetailActivity extends Activity {

    private static final String TAG = "UserCenterDetail";
    private ListView lv_centerdetail_item;
    private List<UserItemInfo> userInfoItem;
    private int currentPosition;
    private UserInfoItemAdapter userInfoItemAdapter;
    private Button bu_modify_confirmmodify;
    private ImageView iv_itemhead_icon;
    private UserInfo userInfoModified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center_detail);

        lv_centerdetail_item = (ListView) findViewById(R.id.lv_centerdetail_item);
        bu_modify_confirmmodify = (Button) findViewById(R.id.bu_modify_confirmmodify);
        userInfoItem = new ArrayList<>();
        initViewData();

    }

    private void initViewData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String objectId = intent.getStringExtra("objectId");
        UserInfo userInfo = bundle.getParcelable("userInfo");
        Log.i(TAG,"userInfo:id"+userInfo.getObjectId());
        userInfoModified = userInfo;

        userInfoModified.setObjectId(objectId);

        Log.i(TAG,"userInfo:id"+userInfoModified.getObjectId());

        userInfoItem.add(new UserItemInfo("昵称", userInfo.getUsername()));
        userInfoItem.add(new UserItemInfo("个性签名", userInfo.getAutograph()));
        userInfoItem.add(new UserItemInfo("电话号码", userInfo.getPhone()));
        userInfoItem.add(new UserItemInfo("密码", userInfo.getPassword()));

        View inflate = View.inflate(this, R.layout.userinfo_item_head, null);
        iv_itemhead_icon = (ImageView) inflate.findViewById(R.id.iv_itemhead_icon);

        BitmapUtils bitmapUtils = new BitmapUtils(this);
        bitmapUtils.display(iv_itemhead_icon,userInfo.getUsericon());

        userInfoItemAdapter = new UserInfoItemAdapter();
        lv_centerdetail_item.setAdapter(userInfoItemAdapter);
        lv_centerdetail_item.addHeaderView(inflate);

        lv_centerdetail_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //当有一个item点击，表示用户想修改信息，让button看见
                bu_modify_confirmmodify.setVisibility(View.VISIBLE);

                Intent intent = new Intent();

                currentPosition = position;
                if (position==0){
                    intent.setAction("android.intent.action.PICK");
                    intent.setType("image/*");
                    startActivityForResult(intent,currentPosition); //position  请求码
                    overridePendingTransition(R.anim.myslide_in_right, R.anim.myslide_out_left);
                }
                else {
                    intent.setClass(UserCenterDetailActivity.this,ModifyUserInfoActivity.class);
                    currentPosition =position;
                    UserItemInfo userItemInfo = userInfoItem.get(currentPosition-1);
                    intent.putExtra("initdata",userItemInfo.getValue());
                    startActivityForResult(intent,currentPosition); //position  请求码
                    overridePendingTransition(R.anim.myslide_in_right, R.anim.myslide_out_left);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
            if (resultCode==RESULT_OK){
                Uri uri = data.getData();
                Log.i(TAG,"uri:"+uri);
                iv_itemhead_icon.setImageURI(uri);

                String[] imgs1 = {MediaStore.Images.Media.DATA};
                //将图片URI转换成存储路径
                Cursor cursor = this.managedQuery(uri, imgs1, null, null, null);
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String img_url = cursor.getString(index);

                final BmobFile icon = new BmobFile(new File(img_url));
                icon.upload(this, new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        userInfoModified.setIcon(icon);
                        userInfoModified.setUsericon(icon.getUrl());
                        Log.i(TAG,userInfoModified.toString());
                        userInfoModified.update(UserCenterDetailActivity.this, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(UserCenterDetailActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(int i, String s) {
                                Log.i(TAG,"修改失败error:   "+s);
                                Toast.makeText(UserCenterDetailActivity.this,"修改失败"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String ss) {
                        Log.i(TAG,"上传失败error:   "+ss);
                        Toast.makeText(UserCenterDetailActivity.this,"上传失败"+ss,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else {
            Log.i(TAG,"userInfoModified"+currentPosition);
            if (requestCode==currentPosition && resultCode == RESULT_OK){
                String returnValue = data.getStringExtra("value");
                String value = userInfoItem.get(currentPosition-1).getValue();
                if (returnValue!=null && value!=null){
                    if (!returnValue.equals(value)){
                        userInfoItem.get(currentPosition-1).setValue(returnValue);
                        userInfoItemAdapter.notifyDataSetChanged();
                        switch (currentPosition){
                            case 1:
                                userInfoModified.setUsername(data.getStringExtra("value"));
                                break;
                            case 2:
                                userInfoModified.setAutograph(data.getStringExtra("value"));
                                break;
                            case 3:
                                userInfoModified.setPhone(data.getStringExtra("value"));
                                break;
                            case 4:
                                userInfoModified.setPassword(data.getStringExtra("value"));
                                break;
                        }
                    }
                }
            }
        }
    }

    class UserInfoItemAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return userInfoItem.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UserItemInfo userItemInfo = userInfoItem.get(position);
            View inflate = View.inflate(UserCenterDetailActivity.this, R.layout.list_item_centerdetail, null);
            TextView tv_itemcneterdetail_key = (TextView) inflate.findViewById(R.id.tv_itemcneterdetail_key);
            TextView tv_itemcneterdetail_value = (TextView) inflate.findViewById(R.id.tv_itemcneterdetail_value);

            tv_itemcneterdetail_key.setText(userItemInfo.getKey());
            tv_itemcneterdetail_value.setText(userItemInfo.getValue());

            return inflate;
        }
    }


    class UserItemInfo{
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public UserItemInfo(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public void modify(View view){
        Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
        userInfoModified.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(UserCenterDetailActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserCenterDetailActivity.this,"修改失败，请检查网络",Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent();
        //Bundle bundle
        intent.putExtra("userInfoModified", (Parcelable) userInfoModified);
        setResult(103,intent);
    }

    public void back(View view){
        finish();
    }


}
