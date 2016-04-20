package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.Word_info;
import com.wangdao.mutilword.dao.RepeatWordDao;
import com.wangdao.mutilword.dao.WordDao;
import com.wangdao.mutilword.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class ReciteWordActivity extends Activity {

    private static final String TAG ="ReciteWordActivity" ;
    public WordDao dao_cet4;
    public RepeatWordDao daoRepeatWord;
    public ArrayList<Word_info> word_infos;

    //默认是折叠状态
    private boolean mUnfold=false;

    private FoldingCell fc;
    public TextView tv_recite_trans;
    public TextView tv_recite_word;
    public int index;
    public int num;
    public String dbName;
    public TextView tv_recite_title;
    public TextView tv_recite_remainWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recite_word);
        //初始化db
        dbName = "CET_4.db";
        String oldDbName = "Old_CET_4.db";
        initAssets(dbName);
        num = 30;
        daoRepeatWord = new RepeatWordDao(this, oldDbName, 1);
        //往集合里增加num个新单词
        word_infos = WordDao.selectNoRepeatWord(this, dbName, num);
        ArrayList<Word_info> oldWord = daoRepeatWord.getOldWord();
        word_infos.addAll(oldWord);
        initView();
        initData();

    }
    //初始化控件
    private void initView() {
        index = 0;
        fc = (FoldingCell) findViewById(R.id.folding_cell);
        tv_recite_trans = (TextView) findViewById(R.id.tv_recite_trans);
        tv_recite_word = (TextView) findViewById(R.id.tv_recite_word);
        tv_recite_title = (TextView) findViewById(R.id.tv_recite_title);
        tv_recite_remainWord = (TextView) findViewById(R.id.tv_recite_remainWord);
    }

    //初始化控件的数字
    private void initData() {
        tv_recite_word.setText(word_infos.get(index).getWord());
        tv_recite_title.setText(word_infos.get(index).getWord());
        tv_recite_trans.setText(word_infos.get(index).getTrans());
        tv_recite_remainWord.setText("亲，你还剩"+word_infos.size()+"个单词，加油哦");

        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
                //点击之后改为打开
                mUnfold=!mUnfold;
            }
        });
    }

    //将文件从assets初始化到app中
    private void initAssets(String s) {
        AssetManager assets = getAssets();
        try {
            InputStream inputStream = assets.open(s);
            File file = new File(getFilesDir(), s);
            if (file.exists()) {
                Toast.makeText(ReciteWordActivity.this, "已存在", Toast.LENGTH_SHORT).show();
                return;
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            IOUtils.fisWriteToFos(inputStream, fileOutputStream);
            Toast.makeText(ReciteWordActivity.this, "复制完成", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //记得单词,切换到下一个
    public void remenber(View view) {
        //记住了，判断当前状态是否打开，如果打开要先关闭,再替换单词
        if (mUnfold){
            fc.toggle(false);
            remenberNextWord();
            //打开之后，改为打开状态
            mUnfold=false;
        }else {
            //如果当前是折叠状态，则直接替换单词
            remenberNextWord();
            //当前状态是折叠
            mUnfold=false;
        }
    }

    private void remenberNextWord() {
        if(index<word_infos.size()){
            Word_info word_info = word_infos.get(index);
            //如果repeat=0.说明没有加入背过的单词库,则让repeat次数=1,更新背诵的时间,加入数据库
            if (word_info.getRepeat()==0){
                word_info.setRepeat(1);
                word_info.setDate(new Date().getTime());
                daoRepeatWord.insert(word_info);
            }else {
                //如果repeat=1.说明加入背过的单词库,让repeat次数+1,更新背诵的时间,更新数据库
                int repeat = word_info.getRepeat() + 1;
                long time = new Date().getTime();
                daoRepeatWord.update(word_info.getId(),repeat,time);
            }
            //在未背过的数据库中减去,要背的单词集合 word_infos中减去
            WordDao.deleteWord(this,dbName,word_info.getId());
            word_infos.remove(index);
            //更新tv,因为上一句集合数减少了1,为了防止index越界,要重新判断
            if(index<word_infos.size()) {
                tv_recite_word.setText(word_infos.get(index).getWord());
                tv_recite_title.setText(word_infos.get(index).getWord());
                tv_recite_trans.setText(word_infos.get(index).getTrans());
            }else {
                if(index==0){
                    tv_recite_word.setText("恭喜你完成了此次背诵");
                    tv_recite_title.setText("恭喜你完成了此次背诵");
                    tv_recite_trans.setText("");
                }else {
                    index=0;
                    tv_recite_word.setText(word_infos.get(index).getWord());
                    tv_recite_title.setText(word_infos.get(index).getWord());
                    tv_recite_trans.setText(word_infos.get(index).getTrans());
                }
            }
        }else {//index>size
            //如果index=0;说明要背的单词集合word_infos的size为0,则完成背诵
            //否则,让index=0;
            if(index==0){
                tv_recite_word.setText("恭喜你完成了此次背诵");
                tv_recite_title.setText("恭喜你完成了此次背诵");
                tv_recite_trans.setText("");
            }else {
                index=0;
                tv_recite_word.setText(word_infos.get(index).getWord());
                tv_recite_title.setText(word_infos.get(index).getWord());
                tv_recite_trans.setText(word_infos.get(index).getTrans());
            }
        }
        System.out.println("remenber"+word_infos.size());
        tv_recite_remainWord.setText("亲，你还剩"+word_infos.size()+"个单词，加油哦");
    }

    //该单词未记得,显示下一个单词
    public void unRemenber(View view) {
        //只有打开状态才可以点击
        if (mUnfold){
            fc.toggle(false);
            unRemenberNextWord();
            //折叠后改为 false
            mUnfold=false;
        }else {
            Toast.makeText(ReciteWordActivity.this, "还没学习呢，学习要专心哦", Toast.LENGTH_SHORT).show();
            mUnfold=false;
        }

    }

    private void unRemenberNextWord() {
        if(index==0&&word_infos.size()==0){
            return;
        }
        index++;
        if(index>=word_infos.size()){
            index=0;
        }
        tv_recite_word.setText(word_infos.get(index).getWord());
        tv_recite_title.setText(word_infos.get(index).getWord());
        tv_recite_trans.setText(word_infos.get(index).getTrans());
        System.out.println("unRemenber"+word_infos.size());
    }
}
