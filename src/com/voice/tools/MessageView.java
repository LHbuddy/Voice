package com.voice.tools;

import com.voice.act.MainActivity;

import android.content.Intent;

public class MessageView {
	MainActivity mActivity;
	
	public MessageView(MainActivity activity){
		mActivity=activity;
	}
	
	public void start(){
		Intent intent=new Intent();
		intent.setClassName("com.android.mms","com.android.mms.ui.ConversationList");
		mActivity.startActivity(intent);
	}
}
