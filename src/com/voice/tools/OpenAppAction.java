package com.voice.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voice.act.MainActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

public class OpenAppAction {

	MainActivity mActivity;
	String mAppName;

	public OpenAppAction(String appname, MainActivity activity) {
		// Log.d("dd","here");
		mAppName = appname;
		mActivity = activity;
	}

	public void start() {
		if ((mAppName != null) && (mAppName.length() != 0)) {
			getAppByName();
		}
	}

	private void getAppByName() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		PackageManager pm = mActivity.getPackageManager();
		List<ResolveInfo> installAppList = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo info : installAppList) {
			String name = info.loadLabel(pm).toString();
			name=name.toLowerCase();
			Log.e("TAG", name);
			if (name.equals(mAppName)) {
				String pkgname = info.activityInfo.packageName;
				Log.e("TAG", pkgname);
				if ("com.android.contacts".equalsIgnoreCase(pkgname)) {
					Uri uri = Uri.parse("content://contacts/people");
					Intent i = new Intent("android.intent.action.VIEW", uri);
					mActivity.startActivity(i);
				} else {
					intent = pm.getLaunchIntentForPackage(pkgname);
					intent.addCategory("android.intent.category.LAUNCHER");
					mActivity.startActivity(intent);
				}
				mActivity.addlist("正在打开" + mAppName + "...", "0");
				return;
			}
		}
		mActivity.addlist("没有找到你所说的应用", "0");
	}

}
