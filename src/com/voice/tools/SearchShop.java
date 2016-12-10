package com.voice.tools;

import com.voice.act.MainActivity;

import android.content.Intent;
import android.net.Uri;

public class SearchShop {
	MainActivity mActivity;
	String name;
	String endLoc;

	// String searchEngine;

	public SearchShop(String name, MainActivity activity) {
		this.name = name;
		mActivity = activity;
	}

	public void Search() {
		startWebSearch();
	}

	private void startWebSearch() {
		mActivity.addlist("正在淘宝：" + name + "...", "0");
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://s.m.taobao.com/h5?event_submit_do_new_search_auction=1&_input_charset=utf-8&topSearch=1&atype=b&searchfrom=1&action=home%3Aredirect_app_action&from=1&sst=1&n=20&buying=buyitnow&q=" + name));
		intent.setClassName("com.android.browser",
				"com.android.browser.BrowserActivity");// 设置为系统自带浏览器启动
		mActivity.startActivity(intent);
	}
}