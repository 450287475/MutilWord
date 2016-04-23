package com.wangdao.mutilword.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.Word_info;
import com.wangdao.mutilword.dao.RepeatWordDao;

import java.util.ArrayList;

public class WordBookActivity extends ActionBarActivity {

    public ListView lv_wordbook_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_book);
        lv_wordbook_item = (ListView) findViewById(R.id.lv_wordbook_item);
        RepeatWordDao daoRepeatWord = new RepeatWordDao(this, "oldWord.db", 1);
        ArrayList<Word_info> oldWord = daoRepeatWord.getOldWord();
       // lv_wordbook_item.setAdapter();

    }

    //class WordBookAdapter
}
