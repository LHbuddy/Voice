package com.voice.tools;

import com.voice.act.MainActivity;

public class OpenQA {

	private String mText;
	MainActivity mActivity;
	
	public OpenQA(String text,MainActivity activity){
		mText=text;
		mActivity=activity;
	}
	
	public void start(){
		mActivity.addlist(mText, "0");
	}
	
}
