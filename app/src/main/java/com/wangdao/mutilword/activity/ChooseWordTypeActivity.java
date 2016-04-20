package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.adapter.RepeatWordListAdapter;
import com.wangdao.mutilword.bean.Word_info;
import com.wangdao.mutilword.dao.RepeatWordDao;
import com.wangdao.mutilword.dialog.Effectstype;
import com.wangdao.mutilword.dialog.NiftyDialogBuilder;
import com.wangdao.mutilword.utils.ListViewSwipeGesture;

import java.util.ArrayList;

public class ChooseWordTypeActivity extends Activity {

    public ListView lv_chooseWordType_wordlist;
    public RepeatWordDao daoRepeatWord;
    public ArrayList<Word_info> word_infos;
    public RepeatWordListAdapter repeatWordListAdapter;

    private Effectstype effect;
    public RadioGroup rg_dialog_choose;
    public RadioButton rb_dialog_cet4;
    public RadioButton rb_dialog_cet6;
    public RadioButton rb_dialog_master;
    public NiftyDialogBuilder niftyDialogBuilder;
    public  View view;
    public static boolean flag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word_type);
        initView();
        initData();



    }



    private void initView() {
        //初始化dialog的view
        view = View.inflate(this, R.layout.custom_view, null);
        rg_dialog_choose = (RadioGroup) view.findViewById(R.id.rg_dialog_choose);
        rb_dialog_cet4 = (RadioButton)view. findViewById(R.id.rb_dialog_cet4);
        rb_dialog_cet6 = (RadioButton)view. findViewById(R.id.rb_dialog_cet6);
        rb_dialog_master = (RadioButton)view. findViewById(R.id.rb_dialog_master);

        //初始化要背单词的列表
        lv_chooseWordType_wordlist = (ListView) findViewById(R.id.lv_chooseWordType_wordlist);
    }

    private void initData() {

        //取出背过的单词本
        daoRepeatWord = new RepeatWordDao(this, "oldWord.db", 1);
        word_infos = daoRepeatWord.getWord();
        repeatWordListAdapter = new RepeatWordListAdapter(this, word_infos);
        lv_chooseWordType_wordlist.setAdapter(repeatWordListAdapter);
        ListViewSwipeGesture listViewSwipeGesture =
                new ListViewSwipeGesture(lv_chooseWordType_wordlist,swipeListener,this);
        listViewSwipeGesture.SwipeType= ListViewSwipeGesture.Double;
        lv_chooseWordType_wordlist.setOnTouchListener(listViewSwipeGesture);
    }


    ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {
        @Override
        public void FullSwipeListView(int position) {
            Toast.makeText(ChooseWordTypeActivity.this, "Action_2", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void HalfSwipeListView(int position) {
            Word_info word_info = word_infos.remove(position);
            repeatWordListAdapter.notifyDataSetChanged();
            daoRepeatWord.update(word_info.getId(),0,-1);
            Toast.makeText(ChooseWordTypeActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnClickListView(int position) {

        }

        @Override
        public void LoadDataForScroll(int count) {

        }

        @Override
        public void onDismiss(ListView listView, int[] reverseSortedPositions) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flag=true;
    }

    public void dialogShow(View v){
        NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(this);

        switch (v.getId()){
            case R.id.rotateleft:effect=Effectstype.RotateLeft;break;

        }

        //.withTitle(null)  no title
        //def
        //def
        //.withMessage(null)  no Msg
        //def
        //def    | isCancelable(true)
        //def
        //def Effectstype.Slidetop
        //def gone
        //def gone
        //.setCustomView(View or ResId,context)
        //  Toast.makeText(v.getContext(), "i'm btn2", Toast.LENGTH_SHORT).show();
        niftyDialogBuilder =  dialogBuilder
                .withTitle("选择要背的单词本")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("请问你想复习哪本书的单词?")                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFF")                                //def
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(effect)                                         //def Effectstype.Slidetop
                .withButton1Text("OK")                                      //def gone
                .withButton2Text("Cancel")                                  //def gone
                .setCustomView(view, v.getContext())         //.setCustomView(View or ResId,context)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        switch (rg_dialog_choose.getCheckedRadioButtonId()) {
                            case R.id.rb_dialog_cet4:
                                intent = new Intent(ChooseWordTypeActivity.this, ReciteWordActivity.class);
                                intent.putExtra("db","CET_4.db");
                                intent.putExtra("oldDb","oldWord.db");
                                startActivity(intent);
                                break;
                            case R.id.rb_dialog_cet6:
                                intent = new Intent(ChooseWordTypeActivity.this, ReciteWordActivity.class);
                                intent.putExtra("db","CET_6.db");
                                intent.putExtra("oldDb","oldWord.db");
                                startActivity(intent);
                                break;
                            case R.id.rb_dialog_master:
                                intent = new Intent(ChooseWordTypeActivity.this, ReciteWordActivity.class);
                                intent.putExtra("db","kaoyan.db");
                                intent.putExtra("oldDb","oldWord.db");
                                startActivity(intent);
                                break;
                        }
                        flag=false;

                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  Toast.makeText(v.getContext(), "i'm btn2", Toast.LENGTH_SHORT).show();
                       niftyDialogBuilder. dismiss();
                    }
                });
        niftyDialogBuilder.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    //    niftyDialogBuilder.dismiss();
    }
}
