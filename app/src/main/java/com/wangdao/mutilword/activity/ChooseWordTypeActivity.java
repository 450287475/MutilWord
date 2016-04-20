package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.adapter.RepeatWordListAdapter;
import com.wangdao.mutilword.bean.Word_info;
import com.wangdao.mutilword.dao.RepeatWordDao;

import java.util.ArrayList;

public class ChooseWordTypeActivity extends Activity implements View.OnClickListener{

    public Button bt_choosewordtype_cet4;
    public Button bt_choosewordtype_cet6;
    public Button bt_choosewordtype_master;
    public ListView lv_chooseWordType_wordlist;
    public RepeatWordDao daoRepeatWord;
    public ArrayList<Word_info> word_infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word_type);
        initView();
        initData();



    }



    private void initView() {
        bt_choosewordtype_cet4 = (Button) findViewById(R.id.bt_choosewordtype_cet4);
        bt_choosewordtype_cet6 = (Button) findViewById(R.id.bt_choosewordtype_cet6);
        bt_choosewordtype_master = (Button) findViewById(R.id.bt_choosewordtype_master);
        lv_chooseWordType_wordlist = (ListView) findViewById(R.id.lv_chooseWordType_wordlist);
    }

    private void initData() {

        //取出背过的单词本
        daoRepeatWord = new RepeatWordDao(this, "oldWord.db", 1);
        word_infos = daoRepeatWord.getWord();
        lv_chooseWordType_wordlist.setAdapter(new RepeatWordListAdapter(this,word_infos));

        bt_choosewordtype_cet4.setOnClickListener(this);
        bt_choosewordtype_cet6.setOnClickListener(this);
        bt_choosewordtype_master.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.bt_choosewordtype_cet4:
                 intent = new Intent(this, ReciteWordActivity.class);
                intent.putExtra("db","CET_4.db");
                intent.putExtra("oldDb","oldWord.db");
                startActivity(intent);
                break;
            case R.id.bt_choosewordtype_cet6:
                intent = new Intent(this, ReciteWordActivity.class);
                intent.putExtra("db","CET_6.db");
                intent.putExtra("oldDb","oldWord.db");
                startActivity(intent);
                break;
            case R.id.bt_choosewordtype_master:
                intent = new Intent(this, ReciteWordActivity.class);
                intent.putExtra("db","kaoyan.db");
                intent.putExtra("oldDb","oldWord.db");
                startActivity(intent);
                break;
        }
    }

}
