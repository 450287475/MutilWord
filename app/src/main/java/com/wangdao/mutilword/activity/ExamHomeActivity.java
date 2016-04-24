package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.ExamBean.CauseInfo;
import com.wangdao.mutilword.dao.examDao.AnswerColumns;
import com.wangdao.mutilword.dao.examDao.BaseColumns;
import com.wangdao.mutilword.dao.examDao.DBManager;
import com.wangdao.mutilword.utils.TimeUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yxd on 2016/4/20 for exam part.
 */
public class ExamHomeActivity extends Activity implements View.OnClickListener{


    private ProgressBar progressBar;
    private Thread mThread;
    private static final int MSG_SUCCESS = 0;//
    private static final int MSG_FAILURE = 1;//

    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {
                case MSG_SUCCESS:

                    Toast.makeText(ExamHomeActivity.this,"已加载本地题库", Toast.LENGTH_SHORT).show();
                    break;

                case MSG_FAILURE:
                    Toast.makeText(ExamHomeActivity.this,"加载本地题库失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TimeUtils.isNetworkAvailable(this)) {
            DBManager.getInstance(this).removeAll(AnswerColumns.TABLE_NAME);
        }
        setContentView(R.layout.activity_exam_homenew);
        //changecolor();
        TextView order = (TextView) findViewById(R.id.order);
        TextView simulate = (TextView) findViewById(R.id.simulate);
        TextView favorite = (TextView) findViewById(R.id.favorite);
        TextView wrong = (TextView) findViewById(R.id.wrong);
        TextView history = (TextView) findViewById(R.id.history);
        Button bt_examhome_back =  (Button) findViewById(R.id.bt_examhome_back);


        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        //解析题库
        parseQuestionBack("QuestionBank.json");

        order.setOnClickListener(this);
        simulate.setOnClickListener(this);
        favorite.setOnClickListener(this);
        wrong.setOnClickListener(this);
        history.setOnClickListener(this);
        bt_examhome_back.setOnClickListener(this);
    }

    /*private void changecolor() {
        Window window = this.getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.RED);
        }
    }*/
    //解析题库
    public void parseQuestionBack(String fileName){
        if(isFileExistInData(fileName)){
            Log.e("parseQuestionBack","json文件已存在");
            insertToCauseInfoFromString(fileToString(fileName));
        }
        else{
            asynctaskInstance(fileName);
        }

    }
    //判断文件是否存在
    public boolean isFileExistInData(String fileName){

        String dataPath = getFilesDir().getAbsolutePath();
        File file = new File(dataPath,fileName);
        Log.e(fileName+"路径",dataPath);
        return file.exists();
    }

    //将file下的文件读取并转化为字符串
    public String fileToString(String fileName){
        String result = null;
        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            byte[] bytes = new byte[1024];
            int leng = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while((leng=fileInputStream.read(bytes)) != -1){
                baos.write(bytes, 0, leng);
            }

            String string = new String(baos.toByteArray());

            result = string;
            return result;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //将读取json文件获取的string或者从网络获取的string解析为Causeinfo并插入表中
    public void insertToCauseInfoFromString(final String s){

        if(mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int code = jsonObject.getInt("code");
                        if (code == 1) {
                            String content = jsonObject.getString("content");
                            JSONArray array = new JSONArray(content);
                            for (int i = 0; i < content.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String timu_title = new JSONObject(object.getString("timu")).getString("title");
                                String timu_one = new JSONObject(object.getString("timu")).getString("one");
                                String timu_tow = new JSONObject(object.getString("timu")).getString("tow");
                                String timu_three = new JSONObject(object.getString("timu")).getString("three");
                                String timu_four = new JSONObject(object.getString("timu")).getString("four");
                                String daan_one = new JSONObject(object.getString("daan")).getString("daan_one");
                                String daan_tow = new JSONObject(object.getString("daan")).getString("daan_tow");
                                String daan_three = new JSONObject(object.getString("daan")).getString("daan_three");
                                String daan_four = new JSONObject(object.getString("daan")).getString("daan_four");
                                String types = new JSONObject(object.getString("types")).getString("types");
                                String detail = new JSONObject(object.getString("detail")).getString("detail");
                                int reply = BaseColumns.NULL;
                                CauseInfo myData = new CauseInfo(timu_title, timu_one, timu_tow, timu_three, timu_four, daan_one, daan_tow,
                                        daan_three, daan_four, detail, types, reply);
                                DBManager.getInstance(ExamHomeActivity.this).insert(AnswerColumns.TABLE_NAME, myData);

                            }
                            mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
                        } else {
                            mHandler.obtainMessage(MSG_FAILURE).sendToTarget();
                            Toast.makeText(ExamHomeActivity.this, "数据解析出现异常，请联系管理员", Toast.LENGTH_SHORT).show();
                        }
//            progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
//            progressBar.setVisibility(View.GONE);
                    }
                }
            });
            mThread.start();//线程启动
        }

    }

    //如果本地没有json题库文件，去从网络下载
    private void asynctaskInstance(final String fileName) {
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Contans.PATH_HOME, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    fileOutputStream.write(arg2);
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ExamHomeActivity.this, "待写入题库文件为找到", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ExamHomeActivity.this, "IO异常", Toast.LENGTH_SHORT).show();


                }
                String result = new String(arg2);
                insertToCauseInfoFromString(result);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ExamHomeActivity.this, "启禀小主：题库已更新", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ExamHomeActivity.this, "加载题库失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.order:
                startActivity(new Intent(this, OrderActivity.class));
                break;

            case R.id.simulate:
                View layout = getLayoutInflater().inflate(R.layout.enter_simulate, null);
                final Dialog dialog = new Dialog(this);
                dialog.setTitle("温馨提示");
                dialog.show();
                dialog.getWindow().setContentView(layout);
                final EditText et_name = (EditText) layout.findViewById(R.id.et_name);
                TextView confirm = (TextView) layout.findViewById(R.id.confirm);
                TextView cancel = (TextView) layout.findViewById(R.id.cancel);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                            Toast.makeText(ExamHomeActivity.this, "请输入测试者姓名", Toast.LENGTH_SHORT).show();
                        } else {
                            ExamActivity.intentToExamActivity(ExamHomeActivity.this, et_name.getText().toString().trim());
                            Toast.makeText(ExamHomeActivity.this, "考试开始", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.favorite:
                startActivity(new Intent(this, CollectActivity.class));
                break;

            case R.id.wrong:
                startActivity(new Intent(this, ErrorActivity.class));
                break;

            case R.id.history:
                startActivity(new Intent(this, HisResultActivity.class));
                break;
            case R.id.bt_examhome_back:
                finish();

            default:
                break;
        }
    }



}
