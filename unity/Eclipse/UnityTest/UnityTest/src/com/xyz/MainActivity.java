package com.xyz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.unity3d.player.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;


import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends UnityPlayerActivity {

	String tag = "test";
	String result_ = "";
	String out = "";

	String regEx = "[\\u4e00-\\u9fa5]";
	Pattern p = Pattern.compile(regEx);

	public SpeechRecognizer mSpeechRecognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpeechUtility.createUtility(getApplicationContext(), "appid=5787398f");
    }

    public void initfunc(){
    	mSpeechRecognizer = SpeechRecognizer.createRecognizer(getApplicationContext(), mInitListener);
    	Log.d(tag, "init success");
    }
    
    public void startRecognizerfuc(){
    	
    	Log.d(tag, "startRecognizerfuc");

    	mSpeechRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
    	mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
    	mSpeechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin");
    	mSpeechRecognizer.setParameter(SpeechConstant.SAMPLE_RATE, "16000"); 
    	
    	out = "";//清空
    	
    	int ret = mSpeechRecognizer.startListening(mRecognizerListener);
    	Log.d(tag, "ret:"+ret);
    }
    
    public InitListener mInitListener = new InitListener() {
		
		@Override
		public void onInit(int arg0) {
			// TODO Auto-generated method stub
			Log.d(tag, "int code:"+arg0);
		}
	};
	
	public RecognizerListener mRecognizerListener = new RecognizerListener() {
		
    	
		@Override
		public void onVolumeChanged(int arg0, byte[] arg1) {
			// TODO Auto-generated method stub
			Log.d(tag, "音量"+arg0);
			String volume = Integer.toString(arg0);
			UnityPlayer.UnitySendMessage("Main Camera","volume",volume);
		}
		
		@Override
		public void onResult(RecognizerResult results, boolean arg1) {
			// TODO Auto-generated method stub
			Log.d(tag, results.getResultString());
			result_ = results.getResultString();
			
			try {
				JSONObject jsonObject = new JSONObject(result_);
				JSONArray jsonArray = jsonObject.getJSONArray("ws");
				
				for(int i=0;i<jsonArray.length();i++){//遍历
					
					JSONObject jsonObject1 = jsonArray.getJSONObject(i);
					String out0 = jsonObject1.getString("cw");
					
					Log.d(tag, "遍历:"+"i:"+i+out0+"--length:"+jsonArray.length());
					
					JSONArray jsonArray1 = jsonObject1.getJSONArray("cw");
					JSONObject json_result = jsonArray1.getJSONObject(0);
					
					String str = json_result.getString("w");
					//是否是汉字
					Matcher m = p.matcher(str);
					System.out.print("提取出来的中文有：");
					  
					while (m.find()) {
					   out += m.group();
					   Log.d(tag, "group:"+m.group());
					   Log.d(tag, "获取数据i:"+i+str);
					   
					}
					Log.d(tag, "获取数据str:i:"+i+str);
				
				}
				Log.d(tag,"out:" + out);
				UnityPlayer.UnitySendMessage("Main Camera","messgae",out);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onError(SpeechError arg0) {
			// TODO Auto-generated method stub
			Log.d(tag, "Error"+arg0.getErrorCode());
		}
		
		@Override
		public void onEndOfSpeech() {
			// TODO Auto-generated method stub
			Log.d(tag, "onEndOfSpeech:"+out);
			
		}
		
		@Override
		public void onBeginOfSpeech() {
			// TODO Auto-generated method stub
			Log.d(tag, "onBeginOfSpeech");
		}
	};
	
}
