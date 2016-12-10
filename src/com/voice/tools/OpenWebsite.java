package com.voice.tools;

import com.voice.act.MainActivity;

import android.content.Intent;
import android.net.Uri;

public class OpenWebsite {
	
	private String mUrl=null,mName=null;
	MainActivity mActivity;
	
	public OpenWebsite(String url,String name,MainActivity activity){
		mUrl=url;
		mName=name;
		mActivity=activity;
	}
	
	public void start(){
		if(mUrl==null){
			mActivity.addlist("未找到"+mName+"，正在百度...", "0");
			 Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);	
					intent.setData(Uri.parse("http://m.baidu.com/s?word="+mName)); 
					intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");//设置为系统自带浏览器启动
			    mActivity.startActivity(intent);
		}
		else{
			mActivity.addlist("正在打开"+mUrl+"...", "0");
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);	
			intent.setData(Uri.parse(mUrl)); 
			intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");//设置为系统自带浏览器启动
			mActivity.startActivity(intent);
		}
		
	}

}
