package com.voice.tools;

import java.util.Calendar;

import com.voice.act.MainActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Intent;
import android.provider.AlarmClock;
import android.provider.CalendarContract;

public class ScheduleCreate {
	private String mName;
	private MainActivity mActivity;

	public ScheduleCreate(String name, MainActivity activity) {
		mName = name;
		mActivity = activity;
	}

	public void start() {
		if (mName.equals("clock")) {// 设置闹钟提醒
			setClock();
		} else if (mName.equals("reminder")) {// 设置日历提醒
			setCalendar();
		}
	}

	private void setClock() {
		Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);
		mActivity.startActivity(alarmas);
		/*
		 * AlarmManager aManager; Calendar currentTime=Calendar.getInstance();
		 * 获取闹钟管理的实例 aManager =
		 * (AlarmManager)mActivity.getSystemService(mActivity.ALARM_SERVICE);
		 * 设置闹钟
		 */
		// aManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
		// pendingIntent);
	}

	@SuppressLint("NewApi")
	private void setCalendar() {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setData(CalendarContract.Events.CONTENT_URI);
		mActivity.startActivity(intent);
	}
}
