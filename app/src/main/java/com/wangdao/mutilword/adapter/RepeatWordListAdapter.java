package com.wangdao.mutilword.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.activity.ChooseWordTypeActivity;
import com.wangdao.mutilword.bean.Word_info;

import java.util.ArrayList;

/**
 * Created by Mumuseng on 2016/4/20.
 */
public class RepeatWordListAdapter extends BaseAdapter {
    ChooseWordTypeActivity activity;
    ArrayList<Word_info> word_infos;
    public RepeatWordListAdapter(ChooseWordTypeActivity activity, ArrayList<Word_info> word_infos) {
        this.activity=activity;
        this.word_infos=word_infos;
    }

    @Override
    public int getCount() {
        return word_infos.size();
    }

    @Override
    public Object getItem(int position) {
        return word_infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.manager_group_list_item_parent, parent, false);
            //view.findViewById(R.id.)
        }

        return null;
    }
}
