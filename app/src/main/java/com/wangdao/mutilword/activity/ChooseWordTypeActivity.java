package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.wangdao.mutilword.utils.ListViewSwipeGesture;

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
        rb_dialog_cet4 = (RadioButton) view.findViewById(R.id.rb_dialog_cet4);
        rb_dialog_cet6 = (RadioButton) view.findViewById(R.id.rb_dialog_cet6);
        rb_dialog_master = (RadioButton) view.findViewById(R.id.rb_dialog_master);

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
                new ListViewSwipeGesture(lv_chooseWordType_wordlist, swipeListener, this);
        listViewSwipeGesture.SwipeType = ListViewSwipeGesture.Double;
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
            daoRepeatWord.update(word_info.getId(), 0, -1);
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
        flag = true;
    }

    public void dialogShow(View v) {
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);

        switch (v.getId()) {
            case R.id.rotateleft:
                effect = Effectstype.RotateLeft;
                break;

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
        niftyDialogBuilder = dialogBuilder
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
        BmobQuery<Bmob_word_info> query = new BmobQuery<>();
        query.addWhereEqualTo("username", ApplicationInfo.userInfo.getUsername());
        query.setLimit(Integer.MAX_VALUE);

        //初始化弹框
        final ProgressDialog progressDialog = new ProgressDialog(ChooseWordTypeActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在与云端同步单词...");
        progressDialog.setMessage("下载进度....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        query.findObjects(this,  new FindListener<Bmob_word_info>() {

            public ArrayList<BmobObject> update_word_infos;
            public ArrayList<Word_info> word_infos_db;

            @Override
            public void onSuccess(List<Bmob_word_info> list) {
                //弹出进度框,避免用户误操作

                Toast.makeText(ChooseWordTypeActivity.this, "服务器端共有"+list.size()+"条数据,正在同步到本地数据库", Toast.LENGTH_SHORT).show();

                //获得所有背过单词的集合,该集合通过运算剩余的所有元素都是服务器没有的,用insert方法添加到服务器中
                word_infos_db = daoRepeatWord.getWord();

                //progressInfo保存总进度,当前进度,同步单词失败的个数
                final ProgressInfo progressInfo = new ProgressInfo();
                progressInfo.total=list.size();//总进度
                progressInfo. current=0;//当前进度
                progressInfo.failure=0;//同步单词失败的个数
                progressDialog.setMax(progressInfo.total);

                //遍历从云端获得的单词.若和本地数据库中的单词相同,则将本地数据库中的该单词用update的方法更新到云端
                for (final Bmob_word_info bmob_word_info:list){
                    //查询该单词是否存在本地数据库,存在返回本地数据库的单词信息,否则返回null
                    Word_info exist = daoRepeatWord.isExist(bmob_word_info.getWord());

                    if (exist!=null){
                        //单词在本地数据库存在,则从word_infos_db去掉,
                            word_infos_db.remove(exist);

                        //如果从云端获得的单词的最后背诵日期和存在本地单词的最后背诵日期相同,则没有同步的必要.
                        if (exist.getDate()==bmob_word_info.getDate()){
                            //更新进度条
                            refreshProgressDialog(progressInfo);
                            System.out.println("本地数据库和服务器端数据相同"+exist.getWord());
                            progressInfo.total--;
                            progressInfo.current--;
                            continue;
                        }else {
                            //更新该单词
                            bmob_word_info.setDate(exist.getDate());
                            bmob_word_info.update(ChooseWordTypeActivity.this, bmob_word_info.getObjectId(), new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    //更新进度条
                                    refreshProgressDialog(progressInfo);
                                    System.out.println("更新成功"+bmob_word_info.getWord());
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    refreshProgressDialog(progressInfo);
                                    //若更新失败,则failure++;
                                    progressInfo.failure++;
                                    System.out.println("更新成功"+bmob_word_info.getWord());
                                    System.out.println(bmob_word_info.getWord()+"更新失败:"+s);
                                }
                            });
                        }

                    }else {//单词在本地数据库不存在,则加入本地数据库
                        refreshProgressDialog(progressInfo);
                        daoRepeatWord.insert(new Word_info(bmob_word_info.getWord(),bmob_word_info.getTrans(),
                                bmob_word_info.getPhonetic(),bmob_word_info.getTags(),bmob_word_info.getRepeat(),-1,bmob_word_info.getDate()));
                    }
                }
                //更新进度条的最大进度
                progressInfo.total+=word_infos_db.size();
                progressDialog.setMax(progressInfo.total);

                //遍历所有还存在word_infos_db集合中的元素,这些元素代表着云端不存在而本地数据库存在的单词.将这些单词用insert方法加入到云端
                for(Word_info word_info:word_infos_db){
                    final Bmob_word_info bmob_word_info = new Bmob_word_info(word_info.getWord(), word_info.getTrans(), word_info.getPhonetic(),
                            word_info.getTags(), word_info.getRepeat(), word_info.getDate(), ApplicationInfo.userInfo.getUsername());

                    bmob_word_info.save(ChooseWordTypeActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            refreshProgressDialog(progressInfo);
                            System.out.println(bmob_word_info.getWord()+"插入成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            refreshProgressDialog(progressInfo);
                            progressInfo.failure++;
                            System.out.println(bmob_word_info.getWord()+"插入失败"+s);
                        }
                    });

                }

            }

            //更新进度框方法
            private void refreshProgressDialog(ProgressInfo progressInfo) {
                progressInfo.current++;
                progressDialog.setProgress(progressInfo.current);
                if(progressInfo.current==progressInfo.total){
                    if(progressInfo.failure!=0){
                        Toast.makeText(ChooseWordTypeActivity.this,"同步完成,还有"+progressInfo.failure+"个单词没同步成功...下次可一起同步",Toast.LENGTH_SHORT);
                    }else {
                        Toast.makeText(ChooseWordTypeActivity.this, "完全同步成功!", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(ChooseWordTypeActivity.this, "网络异常,请稍后更新", Toast.LENGTH_SHORT).show();
                System.out.println("从云端获取数据失败");
            }
        });


    }

}
