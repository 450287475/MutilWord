package com.wangdao.mutilword.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.bean.Word_info;
import com.wangdao.mutilword.bean.interpretBean.Dict;
import com.wangdao.mutilword.bean.interpretBean.Sent;
import com.wangdao.mutilword.dao.RepeatWordDao;
import com.wangdao.mutilword.utils.MyAdapter;
import com.wangdao.mutilword.utils.SearchWords;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;


public class InterpretFragment extends Fragment {

    private View parentView;
    private ListView listView;
    private ImageButton btn_voice; // 语音服务
    private EditText edit_search; // 编辑单词
    private Button btn_search; // 搜索单词
    private TextView text_word;
    private TextView text_pron;
    private TextView btn_aduio; //单词发音
    private TextView btn_add; // 添加单词
    private TextView text_def;
    private ListView didaListview;
    private MyAdapter adapter;
    private String aduioPath;
    private AudioManager audioManager;//音量管理者
    private int maxVolume;// 最大音量
    private RepeatWordDao dao;

    private static Context context;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static StringBuffer sb = new StringBuffer();
    private ProgressDialog mProgressDialog;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressDialog.dismiss();
            Dict dict = (Dict) msg.obj;
            System.out.println("dict from obj----------------------"+dict.toString());
            if (dict != null && !"".equals(dict)) {
                if (dict.getKey() == null || "".equals(dict.getKey())) {
                    text_word.setText(edit_search.getText());
                } else {
                    text_word.setText(dict.getKey());

                }
                String ps = dict.getPs();
                if (ps == null || "".equals(ps)) {
                    // 中文翻译成英文
                    btn_aduio.setVisibility(View.INVISIBLE);
                    btn_add.setVisibility(View.VISIBLE);
                    text_def.setText(dict.getAcceptation());
                    if (dict.getSents() != null) {
                        adapter = new MyAdapter(getActivity().getApplicationContext());
                        adapter.setSents(dict.getSents());
                        didaListview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    //英文翻译成中文
                    System.out.println("-----------------------------------------------------");
                    text_pron.setText("[" + ps + "]");
                    aduioPath = dict.getPron();
                    btn_aduio.setVisibility(View.VISIBLE);
                    btn_add.setVisibility(View.VISIBLE);
                    text_def.setText(dict.getAcceptation());
                    System.out.println(dict.getAcceptation() + "------------------------------");
                    if (dict.getSents() != null) {
                        System.out.println(dict.getSents().toString());
                        adapter = new MyAdapter(getActivity().getApplicationContext());
                        adapter.setSents(dict.getSents());
                        System.out.println(dict.getSents().get(0).getTrans() + "-------------sents----------");
                        didaListview.setAdapter(adapter);
                    }
                    for (int i = 0; i < dict.getSents().size(); i++) {
                        Sent sent = dict.getSents().get(i);
                        sb.append(sent.getOrig()).append(":").append(sent.getTrans()).append(",");
                    }
                }
            }
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_interpret, container, false);
        listView   = (ListView) parentView.findViewById(R.id.didaListview);
        context = getActivity();
        //initView();
        init();
        //需要处理点击事件的or有子控件如viewpager不需要触发侧边栏的功能，请参考HomeFragment中的代码
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获得最大音量
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                maxVolume - 2, AudioManager.FLAG_ALLOW_RINGER_MODES);
        return parentView;
    }

    private void init() {

        dao = new RepeatWordDao(getActivity(), "oldWord.db", 1);

        btn_voice = (ImageButton) parentView.findViewById(R.id.btn_voice);
        edit_search = (EditText) parentView.findViewById(R.id.edit_search);
        btn_search = (Button) parentView.findViewById(R.id.btn_search);
        text_word = (TextView) parentView.findViewById(R.id.text_word);
        // edit_search.setText("%C4%E3%BA%C3");
        text_pron = (TextView) parentView.findViewById(R.id.text_pron);
        btn_aduio = (TextView) parentView.findViewById(R.id.btn_aduio);
        btn_add = (TextView) parentView.findViewById(R.id.btn_add);
        text_def = (TextView) parentView.findViewById(R.id.text_def);
        didaListview = (ListView) parentView.findViewById(R.id.didaListview);
        //语音服务
        btn_voice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                voice();
            }
        });
        //搜索单词
        btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CheckNet()) {
                    final String word = edit_search.getText().toString().trim();
                    try {
                        final String words = URLEncoder.encode(word, "GBK");
                        if (words != null && !words.equals("")) {
                            mProgressDialog = ProgressDialog.show(
                                    getActivity(), null, "正在查询 . . .");
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    System.out.println("--------------" + words);
                                    Dict dict = SearchWords.transWord(words);
                                    System.out.println(dict.toString() + "------------dict add by lan----------------");
                                    Message msg = handler.obtainMessage();
                                    msg.obj = dict;
                                    handler.sendMessage(msg);
                                }
                            }).start();
                        }
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "网络无法连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //单词发音
        btn_aduio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                playAudio(aduioPath);

            }
        });
        // 添加单词
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Word_info word_info = new Word_info(text_word.getText() + "", text_def.getText() + "", text_pron.getText() + "", "custom", 0, -1, new Date().getTime());
                dao.insert(word_info);

                Toast.makeText(getActivity(), "该单词已成功添加至单词本~", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static void playAudio(String path) {
        MediaPlayer mp = null;
        try {
            mp = MediaPlayer.create(context, Uri.parse(path));
            mp.start();
        } finally {
            mp = null;
        }
    }
    private void voice(){
        try{
            //通过Intent传递语音识别的模式，开启语音
            Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            //语言模式和自由模式的语音识别
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            //提示语音开始
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
            //开始语音识别
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "找不到语音设备", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //回调获取从谷歌得到的数据
        if(requestCode==VOICE_RECOGNITION_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            //取得语音的字符
            ArrayList<String> results=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String resultString="";
            StringBuffer sb=new StringBuffer();
            for(int i=0;i<results.size();i++){

                sb.append(results.get(i)).append(",");

            }
            String str=sb.substring(0, sb.lastIndexOf(","));
            final String[] items=str.split(",");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("请选择");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                    edit_search.setText(items[item]);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

            //Toast.makeText(this, items.toString(), 1).show();
        }

    }


   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mProgressDialog!=null){
                mProgressDialog.dismiss();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getActivity());
            builder.setIcon(R.drawable.bee);
            builder.setTitle("你确定退出吗？");
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            getActivity().finish();
                            android.os.Process.killProcess(android.os.Process
                                    .myPid());
                            android.os.Process.killProcess(android.os.Process
                                    .myTid());
                            android.os.Process.killProcess(android.os.Process
                                    .myUid());
                        }
                    });
            builder.setNegativeButton("返回",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.cancel();
                        }
                    });
            builder.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }*/
    /**
     * 查询网络是否连接
     *
     * @return
     */
    private Boolean CheckNet() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }








/*
    private void initView(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getCalendarData());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<String> getCalendarData(){
        ArrayList<String> calendarList = new ArrayList<String>();
        calendarList.add("New Year's Day");
        calendarList.add("St. Valentine's Day");
        calendarList.add("Easter Day");
        calendarList.add("April Fool's Day");
        calendarList.add("Mother's Day");
        calendarList.add("Memorial Day");
        calendarList.add("National Flag Day");
        calendarList.add("Father's Day");
        calendarList.add("Independence Day");
        calendarList.add("Labor Day");
        calendarList.add("Columbus Day");
        calendarList.add("Halloween");
        calendarList.add("All Soul's Day");
        calendarList.add("Veterans Day");
        calendarList.add("Thanksgiving Day");
        calendarList.add("Election Day");
        calendarList.add("Forefather's Day");
        calendarList.add("Christmas Day");
        return calendarList;
    }*/
}
