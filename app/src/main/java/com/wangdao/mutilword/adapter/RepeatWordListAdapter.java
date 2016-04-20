package com.wangdao.mutilword.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    public TextView tv_wordlist_name;
    public ViewHolder holder;
    public View view;

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
            holder = new ViewHolder();
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.manager_group_list_item_parent, parent, false);
            holder. tv_wordlist_name =  (TextView) view.findViewById(R.id.tv_wordlist_name);
            holder. tv_wordlist_trans =  (TextView) view.findViewById(R.id.tv_wordlist_trans);
            view.setTag(holder);
        }else {
            view=convertView;
            holder= (ViewHolder) view.getTag();
        }
        Word_info word_info = word_infos.get(position);
        holder.tv_wordlist_name.setText(word_info.getWord());
        holder.tv_wordlist_trans.setText(word_info.getTrans());

        return view;
    }

    class ViewHolder{
        TextView tv_wordlist_name;
        TextView tv_wordlist_trans;
    }
}
