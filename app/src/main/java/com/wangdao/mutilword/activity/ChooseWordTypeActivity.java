package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wangdao.mutilword.R;

public class ChooseWordTypeActivity extends Activity implements View.OnClickListener{

    public Button bt_choosewordtype_cet4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word_type);
        bt_choosewordtype_cet4 = (Button) findViewById(R.id._bt_choosewordtype_cet4);
        bt_choosewordtype_cet4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id._bt_choosewordtype_cet4:
                    startActivity(new Intent(this,ReciteWordActivity.class));
                break;
        }
    }

}
