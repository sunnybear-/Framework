package com.sunnybear.library.voice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.baidu.android.voicedemo.control.MyRecognizer;
import com.baidu.android.voicedemo.recognization.ChainRecogListener;
import com.baidu.android.voicedemo.recognization.MessageStatusRecogListener;
import com.baidu.android.voicedemo.recognization.online.OnlineRecogParams;
import com.baidu.android.voicedemo.util.Logger;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DigitalDialogInput;
import com.baidu.voicerecognition.android.ui.SimpleTransApplication;

import java.util.Map;

/**
 * 百度语音识别工具
 * Created by chenkai.gu on 2018/5/16.
 */
public class VoiceTools {

    private Context mContext;
    private Handler mHandler;

    private ChainRecogListener mChainRecogListener;
    private MyRecognizer mRecognizer;
    //在线识别参数
    private OnlineRecogParams mOnlineRecogParams;
    //对话框界面的输入参数
    private DigitalDialogInput mInput;

    private OnVoiceRecognitionCallback mOnVoiceRecognitionCallback;

    public VoiceTools(Context context, OnVoiceRecognitionCallback onVoiceRecognitionCallback) {
        mContext = context;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                Logger.info("MyRecognizer", "识别结果:" + result);
            }
        };
        mOnVoiceRecognitionCallback = onVoiceRecognitionCallback;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mChainRecogListener = new ChainRecogListener();
        mChainRecogListener.addListener(new MessageStatusRecogListener(mHandler, mOnVoiceRecognitionCallback));
        // 可以传入IRecogListener的实现类,也可以如SDK,传入EventListener实现类
        // 如果传入IRecogListener类,在RecogEventAdapter为您做了大多数的json解析
        mRecognizer = new MyRecognizer(mContext, mChainRecogListener);
        mOnlineRecogParams = new OnlineRecogParams((Activity) mContext);
    }

    public void start() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        Map<String, Object> params = mOnlineRecogParams.fetch(sp);  // params可以手动填入

        // BaiduASRDigitalDialog的输入参数
        mInput = new DigitalDialogInput(mRecognizer, mChainRecogListener, params);
        ((SimpleTransApplication) mContext.getApplicationContext()).setDigitalDialogInput(mInput);
        Intent intent = new Intent(mContext, BaiduASRDigitalDialog.class);
        mContext.startActivity(intent);
//        Map<String, Object> params = new ConcurrentHashMap<>();
//        params.put("accept-audio-data", false);
//        params.put("disable-punctuation", false);
//        params.put("accept-audio-volume", false);
//        params.put("pid", 1536);
//        mRecognizer.start(params);
    }

    /**
     * 释放VoiceTools资源
     */
    public void release() {
        mRecognizer.release();
    }
}
