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
import com.wangdao.mutilword.bean.GoodInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by haijun on 2016/4/22.
 */
public class AwardMallFragment extends Fragment {

    private static final String TAG = "AwardMallFragment";
    private GridView gv_awardmall_item;
    private List<GoodInfo> goodInfoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_ward_mall, null);

        gv_awardmall_item = (GridView) inflate.findViewById(R.id.gv_awardmall_item);
        goodInfoList = new ArrayList<>();
        innitViewData();

        return inflate;

    }

    private void innitViewData() {
        //获取数据
        BmobQuery<GoodInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(getActivity(), new FindListener<GoodInfo>() {
            @Override
            public void onSuccess(List<GoodInfo> list) {
                goodInfoList = list;
                Toast.makeText(getActivity(),"获取数据成功:",Toast.LENGTH_SHORT).show();
                Log.i(TAG,"goodInfoList"+goodInfoList.size());

                //给LisetVIew添加adapter
                gv_awardmall_item.setAdapter(new AwardmallAdapter());

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(getActivity(),"失败:"+s,Toast.LENGTH_SHORT).show();
            }
        });

        gv_awardmall_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodInfo goodInfo = goodInfoList.get(position);
                Intent intent = new Intent(getActivity(), ShowGoodDetailActivity.class);
                intent.putExtra("objectId",goodInfo.getObjectId());
                intent.putExtra("goodId",goodInfo.getGoodId());
                intent.putExtra("iconurl",goodInfo.getIconurl());
                intent.putExtra("name",goodInfo.getName());
                intent.putExtra("changedcount",goodInfo.getChangedcount());
                intent.putExtra("leftcount",goodInfo.getLeftcount());
                intent.putExtra("points",goodInfo.getPoints());
                intent.putExtra("money",goodInfo.getMoney());
                startActivity(intent);
            }
        });
    }


    class AwardmallAdapter extends BaseAdapter{

        private final BitmapUtils bitmapUtils;

        public AwardmallAdapter() {
            bitmapUtils = new BitmapUtils(getActivity());
        }

        @Override
        public int getCount() {
            return goodInfoList.size();
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
            GoodInfo goodInfo1 = goodInfoList.get(position);

            View inflate = View.inflate(getActivity(), R.layout.listitem_awardmall, null);

            ImageView iv_awardlist_icon1 = (ImageView) inflate.findViewById(R.id.iv_awardlist_icon1);
            TextView tv_awardlist_name1 = (TextView) inflate.findViewById(R.id.tv_awardlist_name1);
            TextView tv_awardlist_point1 = (TextView) inflate.findViewById(R.id.tv_awardlist_point1);

            bitmapUtils.display(iv_awardlist_icon1,goodInfo1.getIconurl());
            tv_awardlist_name1.setText(goodInfo1.getName());
            tv_awardlist_point1.setText(goodInfo1.getPoints()+"积分");


            return inflate;
        }
    }
}
