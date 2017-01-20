package com.example.xfyun;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.io.File;

public class MainActivity extends UnityPlayerActivity {

    private static String TAG = "MainActivity";
    public SpeechRecognizer mspeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
           将此处的XXXXXXX更换为你自己的appid,同时也要更新以下两个库核心文件为
           你为你的app下载的库文件:
           app/src/main/jniLibs/armeabi-v7a/libmsc.so
           app/libs/Msc.jar
        */
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=587d7c53");
    }
    public void StartActivity0(String name)
    {
        startActivity(new Intent(this,MainActivity.class));
    }

    public void StartActivity1()
    {
        Toast.makeText(MainActivity.this, "Toast测试", Toast.LENGTH_SHORT).show();
    }

    public void initfunc(){
        mspeech = SpeechRecognizer.createRecognizer(getApplicationContext(), mInitListener);
        Log.d(TAG, "initfunc: init");
        Toast.makeText(MainActivity.this, "init", Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(this,MainActivity.class));
    }

    public void startRecognizer(){
        //开始识别
        Log.d(TAG, "startRecognizer: 1 ");
        mspeech.setParameter(SpeechConstant.DOMAIN, "iat");
        mspeech.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
        mspeech.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mspeech.setParameter(SpeechConstant.ACCENT, "mandarin");
        //监听对象
        Log.d(TAG, "startRecognizer: 2");
        mspeech.startListening(mRecognizerListener);
    }

    public InitListener mInitListener = new InitListener(){

        @Override
        public void onInit(int i) {
            Log.d(TAG, "onInit: " );
        }
    };

    public RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            Log.d(TAG, "onVolumeChanged: "+ i);
            Toast.makeText(MainActivity.this, i, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginOfSpeech() {
            Log.d(TAG, "onBeginOfSpeech: ");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech: ");
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            Log.d(TAG, recognizerResult.getResultString());
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.d(TAG, "onError: "+ speechError.getErrorCode());
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    public String ShowDialog(final String _title, final String _content){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(_title).setMessage(_content).setPositiveButton("Down", null);
                builder.show();
            }
        });

        return "Java return";
    }

    // 定义一个显示Toast的方法，在Unity中调用此方法
    public void ShowToast(final String mStr2Show){
        // 同样需要在UI线程下执行
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),mStr2Show, Toast.LENGTH_LONG).show();
            }
        });
    }


    //  定义一个手机振动的方法，在Unity中调用此方法
    public void SetVibrator(){
        Vibrator mVibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        mVibrator.vibrate(new long[]{200, 2000, 2000, 200, 200, 200}, -1); //-1：表示不重复 0：循环的震动
    }

    // 第一个参数是unity中的对象名字，记住是对象名字，不是脚本类名
    // 第二个参数是函数名
    // 第三个参数是传给函数的参数，目前只看到一个参数，并且是string的，自己传进去转吧
    public void callUnityFunc(String _objName , String _funcStr, String _content)
    {
        UnityPlayer.UnitySendMessage(_objName, _funcStr, "Come from:" + _content);
    }
}
