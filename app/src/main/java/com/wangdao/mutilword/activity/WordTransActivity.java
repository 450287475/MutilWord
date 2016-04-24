package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wangdao.mutilword.R;

public class WordTransActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_trans);
        Intent intent = getIntent();

        TextView tv_wordtrans_title = (TextView) findViewById(R.id.tv_wordtrans_title);
        TextView tv_wordtrans_phonetic = (TextView) findViewById(R.id.tv_wordtrans_phonetic);
        TextView tv_wordtrans_trans = (TextView) findViewById(R.id.tv_wordtrans_trans);

        tv_wordtrans_title.setText(intent.getStringExtra("word"));
        tv_wordtrans_phonetic.setText(intent.getStringExtra("phonetic"));
        tv_wordtrans_trans.setText(intent.getStringExtra("trans"));

    }
}
