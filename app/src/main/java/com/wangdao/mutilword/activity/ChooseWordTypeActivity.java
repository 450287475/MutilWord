package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.adapter.RepeatWordListAdapter;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.Bmob_word_info;
import com.wangdao.mutilword.bean.ProgressInfo;
import com.wangdao.mutilword.bean.Word_info;
import com.wangdao.mutilword.constant.Constant;
import com.wangdao.mutilword.dao.RepeatWordDao;
import com.wangdao.mutilword.dialog.Effectstype;
import com.wangdao.mutilword.dialog.NiftyDialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    public View view;
    public static boolean flag = false;
    public Button bt_chooseword_sync;


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
        bt_chooseword_sync = (Button) findViewById(R.id.bt_chooseword_sync);
        rg_dialog_choose = (RadioGroup) view.findViewById(R.id.rg_dialog_choose);
        rb_dialog_cet4 = (RadioButton) view.findViewById(R.id.rb_dialog_cet4);
        rb_dialog_cet6 = (RadioButton) view.findViewById(R.id.rb_dialog_cet6);
        rb_dialog_master = (RadioButton) view.findViewById(R.id.rb_dialog_master);


        //改变状态栏颜色
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

    private void initData() {


        //取出背过的单词本
        daoRepeatWord = new RepeatWordDao(this, "oldWord.db", 1);
        word_infos = daoRepeatWord.getWord();
        repeatWordListAdapter = new RepeatWordListAdapter(this, word_infos);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flag = true;
    }

    public void dialogShow(View v) {
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);

        switch (v.getId()) {
            case R.id.rotateleft:
                effect = Effectstype.RotateLeft;
                break;

        }

        //选择单词页
        niftyDialogBuilder = dialogBuilder
                .withTitle("选择要背的单词本")                                  //.withTitle(null)  no title
                .withTitleColor("#bfcce6")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("请问你想复习哪本书的单词?")                     //.withMessage(null)  no Msg
                .withMessageColor("#bfcce6")                                //def
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
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
                                intent.putExtra("dbURL", Constant.CET4_URL);
                                intent.putExtra("dbFilePath", getFilesDir() + "/CET_4.db");
                                startActivity(intent);
                                break;
                            case R.id.rb_dialog_cet6:
                                intent = new Intent(ChooseWordTypeActivity.this, ReciteWordActivity.class);
                                intent.putExtra("dbURL", Constant.CET6_URL);
                                intent.putExtra("dbFilePath", getFilesDir() + "/CET_6.db");
                                startActivity(intent);
                                break;
                            case R.id.rb_dialog_master:
                                intent = new Intent(ChooseWordTypeActivity.this, ReciteWordActivity.class);
                                intent.putExtra("dbURL", Constant.KAOYAN_URL);
                                intent.putExtra("dbFilePath", getFilesDir() + "/kaoyan.db");
                                startActivity(intent);
                                break;
                        }
                        flag = false;
                        niftyDialogBuilder.dismiss();

                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(v.getContext(), "i'm btn2", Toast.LENGTH_SHORT).show();
                        niftyDialogBuilder.dismiss();
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


    //同步功能
    public void sync(View view) {
        File oldDbFile = getDatabasePath("oldWord.db");

       /* //初始化弹框
        final ProgressDialog progressDialog = new ProgressDialog(ChooseWordTypeActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在与云端同步单词...");
        progressDialog.setMessage("下载进度....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();*/
        //开始同步前的初始化
        bt_chooseword_sync.setEnabled(false);
        bt_chooseword_sync.setTextColor(Color.GRAY);
        bt_chooseword_sync.setText("准备开始同步...");

        //查询
        BmobQuery<Bmob_word_info> query = new BmobQuery<>();
        query.addWhereEqualTo("username", ApplicationInfo.userInfo.getUsername());
        query.setLimit(Integer.MAX_VALUE);
        query.findObjects(this, new FindListener<Bmob_word_info>() {

            public ArrayList<BmobObject> update_word_infos;
            public ArrayList<Word_info> word_infos_db;

            @Override
            public void onSuccess(List<Bmob_word_info> list) {
                //弹出进度框,避免用户误操作

                Toast.makeText(ChooseWordTypeActivity.this, "服务器端共有" + list.size() + "条数据,正在同步到本地数据库", Toast.LENGTH_SHORT).show();

                //获得所有背过单词的集合,该集合通过运算剩余的所有元素都是服务器没有的,用insert方法添加到服务器中
                word_infos_db = daoRepeatWord.getWord();

                //progressInfo保存总进度,当前进度,同步单词失败的个数
                final ProgressInfo progressInfo = new ProgressInfo();
                progressInfo.total = list.size();//总进度
                progressInfo.current = 0;//当前进度
                progressInfo.failure = 0;//同步单词失败的个数
                bt_chooseword_sync.setText("正在同步..."+progressInfo.current+"/"+progressInfo.total);


                //遍历从云端获得的单词.若和本地数据库中的单词相同,则将本地数据库中的该单词用update的方法更新到云端
                for (final Bmob_word_info bmob_word_info : list) {
                    //查询该单词是否存在本地数据库,存在返回本地数据库的单词信息,否则返回null
                    Word_info exist = daoRepeatWord.isExist(bmob_word_info.getWord());

                    if (exist != null) {
                        //单词在本地数据库存在,则从word_infos_db去掉,
                        word_infos_db.remove(exist);

                        //如果从云端获得的单词的最后背诵日期和存在本地单词的最后背诵日期相同,则没有同步的必要.
                        if (exist.getDate() == bmob_word_info.getDate()) {
                            //更新进度条
                            refreshSync(progressInfo);
                            progressInfo.total--;
                            progressInfo.current--;
                            continue;
                        } else {
                            //更新该单词
                            bmob_word_info.setDate(exist.getDate());
                            bmob_word_info.update(ChooseWordTypeActivity.this, bmob_word_info.getObjectId(), new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    //更新进度条
                                    refreshSync(progressInfo);
                                    System.out.println("更新成功" + bmob_word_info.getWord());
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    refreshSync(progressInfo);
                                    //若更新失败,则failure++;
                                    progressInfo.failure++;
                                    System.out.println("更新成功" + bmob_word_info.getWord());
                                    System.out.println(bmob_word_info.getWord() + "更新失败:" + s);
                                }
                            });
                        }

                    } else {//单词在本地数据库不存在,则加入本地数据库
                        refreshSync(progressInfo);
                        daoRepeatWord.insert(new Word_info(bmob_word_info.getWord(), bmob_word_info.getTrans(),
                                bmob_word_info.getPhonetic(), bmob_word_info.getTags(), bmob_word_info.getRepeat(), -1, bmob_word_info.getDate()));
                    }
                }

                //更新进度条的最大进度
                progressInfo.total += word_infos_db.size();

                if (progressInfo.total==0){
                    Toast.makeText(ChooseWordTypeActivity.this, "单词库已经是最新的了,不需要同步", Toast.LENGTH_SHORT).show();
                    bt_chooseword_sync.setText("同步到云端");
                    bt_chooseword_sync.setTextColor(0xffbfcce6);
                    bt_chooseword_sync.setEnabled(true);
                    return;
                }
                bt_chooseword_sync.setText("正在同步..."+progressInfo.current+"/"+progressInfo.total);

                //遍历所有还存在word_infos_db集合中的元素,这些元素代表着云端不存在而本地数据库存在的单词.将这些单词用insert方法加入到云端
                for (Word_info word_info : word_infos_db) {
                    final Bmob_word_info bmob_word_info = new Bmob_word_info(word_info.getWord(), word_info.getTrans(), word_info.getPhonetic(),
                            word_info.getTags(), word_info.getRepeat(), word_info.getDate(), ApplicationInfo.userInfo.getUsername());

                    bmob_word_info.save(ChooseWordTypeActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            refreshSync(progressInfo);
                            System.out.println(bmob_word_info.getWord() + "插入成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            refreshSync(progressInfo);
                            progressInfo.failure++;
                            System.out.println(bmob_word_info.getWord() + "插入失败" + s);
                        }
                    });

                }

            }

            //更新进度框方法
            private void refreshSync(ProgressInfo progressInfo) {
                progressInfo.current++;
                bt_chooseword_sync.setText(progressInfo.current+"/"+progressInfo.total);
                if (progressInfo.current == progressInfo.total) {
                    if (progressInfo.failure != 0) {
                        Toast.makeText(ChooseWordTypeActivity.this, "同步完成,还有" + progressInfo.failure + "个单词没同步成功...下次可一起同步", Toast.LENGTH_SHORT);
                    } else {
                        Toast.makeText(ChooseWordTypeActivity.this, "完全同步成功!", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(ChooseWordTypeActivity.this, "已同步完成,失败"+progressInfo.failure+"个.", Toast.LENGTH_SHORT).show();
                    bt_chooseword_sync.setText("同步到云端");
                    bt_chooseword_sync.setTextColor(0xffbfcce6);
                    bt_chooseword_sync.setEnabled(true);
                }
            }

            @Override
            public void onError(int i, String s) {
                bt_chooseword_sync.setText("同步到云端");
                bt_chooseword_sync.setEnabled(true);
                bt_chooseword_sync.setTextColor(0xffbfcce6);
                Toast.makeText(ChooseWordTypeActivity.this, "网络异常,请稍后更新", Toast.LENGTH_SHORT).show();
                System.out.println("从云端获取数据失败");
            }
        });


    }

    //跳转到单词本页面
    public void goToWordBook(View view) {
        startActivity(new Intent(this,WordBookActivity.class));
    }
}
