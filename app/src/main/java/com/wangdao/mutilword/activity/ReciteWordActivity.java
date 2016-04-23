package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.ramotion.foldingcell.FoldingCell;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.Word_info;
import com.wangdao.mutilword.constant.Constant;
import com.wangdao.mutilword.dao.RepeatWordDao;
import com.wangdao.mutilword.dao.WordDao;

import java.io.File;
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

    //最先进来的时候的单词总量
    private int WORD_TOTAL_NUMBER;
    private ImageButton bt_reciteword_imagebutton;
    public ProgressDialog dialog;
    public String dbFilePath;
    public String dbURL;
    public TextView tv_recite_phonetic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recite_word);
        //改变状态栏的颜色
        changecolor();
        initView();

        //初始化db
        Intent intent = getIntent();
        dbURL = intent.getStringExtra("dbURL");
        dbFilePath = intent.getStringExtra("dbFilePath");
        File file = new File(dbFilePath);
        dbName= dbFilePath.substring(dbFilePath.lastIndexOf("/")+1);
        if(!file.exists()){
            //显示下载弹窗
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(true);
            dialog = progressDialog.show(this,null, "下载单词本中...");
            //从网络上下载单词本
            downLoadDb();
        }else {
            initData();
        }


    }


    //
    //从网络上下载单词本
    private void downLoadDb() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download(dbURL, //下载地址
                dbFilePath,//保存路径
                true, //true表示可以断点重连
                new RequestCallBack<File>() {

            @Override
            public void onStart() {
                super.onStart();
                System.out.println("onStart");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                System.out.println(current+":"+current+":"+isUploading);
                dialog.setMax((int) total);
                dialog.setProgress((int) current);
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                dialog.dismiss();
                initData();
                Toast.makeText(ReciteWordActivity.this, "下载完成:"+responseInfo.result.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("onFailure"+s+e.getExceptionCode());
                dialog.dismiss();
                Toast.makeText(ReciteWordActivity.this,"onFailure", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //改变状态栏的颜色
    private void changecolor() {
        Window window = this.getWindow();
       //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
       //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.RED);
        }
    }

    //初始化控件
    private void initView() {
        index = 0;
        fc = (FoldingCell) findViewById(R.id.folding_cell);
        tv_recite_trans = (TextView) findViewById(R.id.tv_recite_trans);
        tv_recite_word = (TextView) findViewById(R.id.tv_recite_word);
        tv_recite_phonetic = (TextView) findViewById(R.id.tv_recite_phonetic);
        tv_recite_title = (TextView) findViewById(R.id.tv_recite_title);
        tv_recite_remainWord = (TextView) findViewById(R.id.tv_recite_remainWord);
        bt_reciteword_imagebutton = (ImageButton) findViewById(R.id.bt_reciteword_imagebutton);
    }

    //初始化控件的数字
    private void initData() {
        // 初始化参数
        num = ApplicationInfo.sp.getInt("wordNum", 30);
        daoRepeatWord = new RepeatWordDao(this, "oldWord.db", 1);
        word_infos=new ArrayList<>();
        //如果离上次背单词已经过去一天了,往集合里增加num个新单词
        long date = ApplicationInfo.sp.getLong("date", -1);
        long time = new Date().getTime();
        long interval = time - date;
        if(interval>Constant.oneDay){
            ApplicationInfo.editor.putLong("date",time).commit();
            word_infos = WordDao.selectNoRepeatWord(this, dbName, num);
            for(Word_info word_info:word_infos){
                //加入到已背的单词库中
                daoRepeatWord.insert(word_info);
                //在未背过的数据库中减去
                WordDao.deleteWord(this,dbName,word_info.getId());
            }
        }

        ArrayList<Word_info> oldWord = daoRepeatWord.getOldWord();
        word_infos=oldWord;
        WORD_TOTAL_NUMBER=word_infos.size();
        if(word_infos.size()>0) {
            refreshText();
        }else {
            doneRefreshText();
        }


        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
                //点击之后改为打开
                mUnfold=!mUnfold;
            }
        });

        //点击小箭头，关闭当前页面
        bt_reciteword_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //完成背诵单词时更新页面
    private void doneRefreshText() {
        tv_recite_word.setText("今天的单词量已经完成");
        tv_recite_title.setText("今天的单词量已经完成");
        tv_recite_trans.setText("");
        tv_recite_phonetic.setText("");
        tv_recite_remainWord.setText("");
    }

    //更新文本
    private void refreshText() {
        tv_recite_word.setText(word_infos.get(index).getWord());
        tv_recite_title.setText(word_infos.get(index).getWord());
        tv_recite_trans.setText(word_infos.get(index).getTrans());
        tv_recite_phonetic.setText("音标:"+word_infos.get(index).getPhonetic());
        tv_recite_remainWord.setText(word_infos.size()+"/"+WORD_TOTAL_NUMBER);
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
            int repeat = word_info.getRepeat() + 1;
            long time = new Date().getTime();
            daoRepeatWord.update(word_info.getId(),repeat,time);
           //要背的单词集合 word_infos中减去
            word_infos.remove(index);
            //更新tv,因为上一句集合数减少了1,为了防止index越界,要重新判断
            if(index<word_infos.size()) {
              refreshText();
            }else {
                if(index==0){
                  doneRefreshText();
                }else {
                    index=0;
                    refreshText();
                }
            }
        }else {//index>size
            //如果index=0;说明要背的单词集合word_infos的size为0,则完成背诵
            //否则,让index=0;
            if(index==0){
                doneRefreshText();
            }else {
                index=0;
                 refreshText();
            }
        }
        System.out.println("remenber"+word_infos.size());
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
           // Toast.makeText(ReciteWordActivity.this, "还没学习呢，学习要专心哦", Toast.LENGTH_SHORT).show();
            unRemenberNextWord();
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
          refreshText();
        System.out.println("unRemenber"+word_infos.size());
    }
}
