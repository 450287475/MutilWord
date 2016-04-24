package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.Word_info;
import com.wangdao.mutilword.dao.RepeatWordDao;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuyidong on 16/1/23.
 */
public class WordBookActivity extends Activity implements SlideAndDragListView.OnListItemLongClickListener,
        SlideAndDragListView.OnDragListener, SlideAndDragListView.OnSlideListener,
        SlideAndDragListView.OnListItemClickListener, SlideAndDragListView.OnMenuItemClickListener,
        SlideAndDragListView.OnItemDeleteListener {
    private static final String TAG = WordBookActivity.class.getSimpleName();

    private List<Menu> mMenuList;
    private List<Word_info> word_infos;
    private SlideAndDragListView<Word_info> mListView;
    public RepeatWordDao repeatWordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workbook);
        initData();
        initMenu();
        initUiAndListener();
    }

    public void initData() {
        repeatWordDao = new RepeatWordDao(this, "oldWord.db", 1);
        word_infos = repeatWordDao.getWord();
    }

    public void initMenu() {
        mMenuList = new ArrayList<>(1);
        Menu menu0 = new Menu(false, true, 0);
        menu0.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn2_width))
                .setBackground(new ColorDrawable(Color.RED))
                .setText("删除")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.WHITE)
                .setTextSize((int) getResources().getDimension(R.dimen.txt_word))
                .build());
        menu0.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width_img))
                .setBackground(new ColorDrawable(0xfffcbe32))
                .setText("标为生词")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.WHITE)
                .setTextSize((int) getResources().getDimension(R.dimen.txt_word))
                .build());
        menu0.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn2_width))
                .setBackground(new ColorDrawable(Color.GRAY))
                .setText("返回")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.WHITE)
                .setTextSize((int) getResources().getDimension(R.dimen.txt_word))
                .build());

        mMenuList.add(menu0);
    }

    public void initUiAndListener() {
        mListView = (SlideAndDragListView) findViewById(R.id.lv_edit);
        mListView.setMenu(mMenuList);
        mListView.setAdapter(mAdapter);
        mListView.setOnListItemLongClickListener(this);
        mListView.setOnDragListener(this, word_infos);
        mListView.setOnListItemClickListener(this);
        mListView.setOnSlideListener(this);
        mListView.setOnMenuItemClickListener(this);
        mListView.setOnItemDeleteListener(this);
        mListView.setDivider(new ColorDrawable(Color.GRAY));
        mListView.setDividerHeight(1);
    }

    private BaseAdapter mAdapter = new BaseAdapter() {
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
            CustomViewHolder holder = null;
            if (convertView == null) {
                holder = new CustomViewHolder();
                convertView = LayoutInflater.from(WordBookActivity.this).inflate(R.layout.item_wordbook_list, null);
                holder. tv_wordbook_title = (TextView) convertView.findViewById(R.id.tv_wordbook_title);
                holder. tv_wordbook_content = (TextView) convertView.findViewById(R.id.tv_wordbook_content);
                convertView.setTag(holder);
            } else {
                holder = (CustomViewHolder) convertView.getTag();
            }
            Word_info item = (Word_info) this.getItem(position);
            holder.tv_wordbook_title.setText(item.getWord());
            holder.tv_wordbook_content.setText("已背次数:"+item.getRepeat()+"      "+item.getTrans());
            return convertView;
        }

        class CustomViewHolder {
            TextView tv_wordbook_title;
            TextView tv_wordbook_content;
        }

    };

    @Override
    public void onListItemLongClick(View view, int position) {

    }

    @Override
    public void onDragViewStart(int position) {

    }

    @Override
    public void onDragViewMoving(int position) {

    }

    @Override
    public void onDragViewDown(int position) {

    }

    @Override
    public void onListItemClick(View v, int position) {
        Intent intent = new Intent(this, WordTransActivity.class);
        Word_info word_info = word_infos.get(position);
        intent.putExtra("word",word_info.getWord());
        intent.putExtra("trans",word_info.getTrans());
        intent.putExtra("phonetic",word_info.getPhonetic());
        startActivity(intent);
    }

    @Override
    public void onSlideOpen(View view, View parentView, int position, int direction) {

    }

    @Override
    public void onSlideClose(View view, View parentView, int position, int direction) {

    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        Log.i(TAG, "onMenuItemClick   " + itemPosition + "   " + buttonPosition + "   " + direction);
        Word_info word_info = word_infos.get(itemPosition);
        String word = word_info.getWord();
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition){
                    case 0:
                        repeatWordDao.delete(word);
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                    case 1:
                        repeatWordDao.update(word,0,new Date().getTime());
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                    case 2:
                        return Menu.ITEM_SCROLL_BACK;
                }
            default:
                return Menu.ITEM_NOTHING;
        }
    }



    @Override
    public void onItemDelete(View view, int position) {
        word_infos.remove(position - mListView.getHeaderViewsCount());
        mAdapter.notifyDataSetChanged();
    }
}
