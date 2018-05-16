package com.sunnybear.library.voice;

/**
 * 语音识别回调
 * Created by chenkai.gu on 2018/5/16.
 */
public interface OnVoiceRecognitionCallback {

    void onVoiceRecognitionSuccess(String result);

    void onVoiceRecognitionFail(int errorCode, String errorMessage);
}
