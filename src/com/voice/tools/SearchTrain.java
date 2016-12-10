package com.voice.tools;

import com.voice.act.MainActivity;

import android.content.Intent;
import android.net.Uri;



public class SearchTrain {
	 MainActivity mActivity;
	 String startLoc;
	 String endLoc;
	 //String searchEngine;
	
	 public  SearchTrain(String startloc,String endloc,MainActivity activity)
	  {
		startLoc = startloc;
		endLoc=endloc;
	    mActivity=activity;
	  }
	 
	 public void Search(){		 
		startWebSearch();	
	 }
	
	 private void startWebSearch()
	  {
		 mActivity.addlist("���ڲ�ѯ��"+startLoc+"��"+endLoc+"�Ļ�...", "0");
		 PinYinChange pinYinChange=new PinYinChange();
		 startLoc=pinYinChange.AllSpell(startLoc);
		 endLoc=pinYinChange.AllSpell(endLoc);
		 Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);	
			intent.setData(Uri.parse("http://m.ctrip.com/html5/trains/"+startLoc+"-"+endLoc+"/")); 
			intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");//����Ϊϵͳ�Դ����������
	    mActivity.startActivity(intent);
	  }
}
