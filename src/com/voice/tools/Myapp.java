package com.voice.tools;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ContactManager.ContactListener;

public class Myapp extends Application {
	public final static String key = "A3430665290BDD60286867CAD5093C28CA7496B4";
	BMapManager manager;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=574693c9");
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
	}
	
}
