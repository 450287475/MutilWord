package com.wangdao.mutilword.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.activity.ShowGoodDetailActivity;
import com.wangdao.mutilword.activity.ShowHadGoodDetailActivity;
import com.wangdao.mutilword.bean.GoodInfo;
import com.wangdao.mutilword.bean.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by haijun on 2016/4/22.
 */
public class HadAwardFragment extends Fragment {

    private static final String TAG = "HadAwardFragment";
    private GridView gv_awardhadmall_item;
    private List<ShoppingCart> goodcartInfoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_award_had, null);
        gv_awardhadmall_item = (GridView) inflate.findViewById(R.id.gv_awardhadmall_item);

        goodcartInfoList = new ArrayList<>();
        innitViewData();
        return inflate;

    }

    private void innitViewData() {
        //获取数据
        BmobQuery<ShoppingCart> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(getActivity(), new FindListener<ShoppingCart>() {
            @Override
            public void onSuccess(List<ShoppingCart> list) {
                goodcartInfoList = list;
                Toast.makeText(getActivity(),"获取数据成功:",Toast.LENGTH_SHORT).show();
                Log.i(TAG,"goodInfoList"+goodcartInfoList.size());

                //给LisetVIew添加adapter
                gv_awardhadmall_item.setAdapter(new AwardHadMallAdapter());

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(getActivity(),"失败:"+s,Toast.LENGTH_SHORT).show();
            }
        });

        gv_awardhadmall_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShoppingCart shoppingCart = goodcartInfoList.get(position);
                Intent intent = new Intent(getActivity(), ShowHadGoodDetailActivity.class);
                intent.putExtra("name",shoppingCart.getName());
                intent.putExtra("iconurl",shoppingCart.getIconurl());
                startActivity(intent);
            }
        });
    }

    class AwardHadMallAdapter extends BaseAdapter {

        private final BitmapUtils bitmapUtils;

        public AwardHadMallAdapter() {
            bitmapUtils = new BitmapUtils(getActivity());
        }

        @Override
        public int getCount() {
            return goodcartInfoList.size();
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
            ShoppingCart shoppingCart = goodcartInfoList.get(position);

            View inflate = View.inflate(getActivity(), R.layout.listitem_hadawardmall, null);
            ImageView iv_awardhadlist_icon = (ImageView) inflate.findViewById(R.id.iv_awardhadlist_icon);
            TextView tv_awardhadlist_name = (TextView) inflate.findViewById(R.id.tv_awardhadlist_name);
            bitmapUtils.display(iv_awardhadlist_icon,shoppingCart.getIconurl());
            tv_awardhadlist_name.setText(shoppingCart.getName());

            return inflate;
        }
    }
}
