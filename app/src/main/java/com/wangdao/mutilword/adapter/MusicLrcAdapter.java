package com.wangdao.mutilword.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.LrcLineContentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gourdboy on 2016/5/13.
 */
public class MusicLrcAdapter extends BaseAdapter
{
    private List<LrcLineContentBean> lrcbeanList;
    private Context mContext;
    private int indexOfCurrentSentence;
    private int mCurrentSize = 20;
    private int mNoCurrentSize = 15;
    public MusicLrcAdapter(Context context)
    {
        this.mContext = context;
        lrcbeanList = new ArrayList<>();
        indexOfCurrentSentence = 0;
    }
    public void setIndex(int indexOfCurrentSentence)
    {
        this.indexOfCurrentSentence = indexOfCurrentSentence;
    }
    public void setLrc(List<LrcLineContentBean> lrcbeanList)
    {
        if(lrcbeanList!=null)
        {
            this.lrcbeanList.clear();
            this.lrcbeanList.addAll(lrcbeanList);
        }
        indexOfCurrentSentence = 0;
    }
    //禁止点击
    @Override
    public boolean isEnabled(int position)
    {
        return false;
    }

    @Override
    public int getCount()
    {
        return lrcbeanList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return lrcbeanList.get(position).content;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = (LinearLayout) View.inflate(mContext, R.layout.lyric_line,null);
            holder.lyric_line = (TextView) convertView.findViewById(R.id.lyric_line_text);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position>=0&&position<lrcbeanList.size())
        {
            holder.lyric_line.setText(lrcbeanList.get(position).content);
        }
        if(indexOfCurrentSentence==position)
        {
            holder.lyric_line.setTextColor(Color.argb(210,251, 248, 29));
            holder.lyric_line.setTextSize(mCurrentSize);
        }
        else
        {
            holder.lyric_line.setTextColor(Color.argb(139, 255, 255, 255));
            holder.lyric_line.setTextSize(mNoCurrentSize);
        }
        holder.lyric_line.setGravity(Gravity.CENTER);
        return convertView;
    }
    private class ViewHolder
    {
        TextView lyric_line;
    }
}
