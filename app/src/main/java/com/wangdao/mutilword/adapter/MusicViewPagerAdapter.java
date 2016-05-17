package com.wangdao.mutilword.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by gourdboy on 2016/5/13.
 */
public class MusicViewPagerAdapter extends PagerAdapter
{
    private List<ListView> lvlist;
    private Context mContext;
    public MusicViewPagerAdapter(Context context, List<ListView> lvlist)
    {
        super();
        this.mContext = context;
        this.lvlist = lvlist;
    }
    @Override
    public int getCount()
    {
        return lvlist.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        ListView lv = lvlist.get(position);
        container.addView(lv);
        return lv;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ListView lv = (ListView) object;
        container.removeView(lv);
    }
}
