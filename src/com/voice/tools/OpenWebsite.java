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
			mActivity.addlist("δ�ҵ�"+mName+"�����ڰٶ�...", "0");
			 Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);	
					intent.setData(Uri.parse("http://m.baidu.com/s?word="+mName)); 
					intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");//����Ϊϵͳ�Դ����������
			    mActivity.startActivity(intent);
		}
		else{
			mActivity.addlist("���ڴ�"+mUrl+"...", "0");
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);	
			intent.setData(Uri.parse(mUrl)); 
			intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");//����Ϊϵͳ�Դ����������
			mActivity.startActivity(intent);
		}
		
	}

}
