package com.voice.tools;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.AlarmClock;
import android.provider.CalendarContract;

import com.voice.act.MainActivity;

public class ScheduleView {

	private String mName;
	private MainActivity mActivity;

	public ScheduleView(String name, String time, String date, String content,
			MainActivity activity) {
		mName = name;
		mActivity = activity;
	}

	public void start() {
		if (mName.equals("clock")) {
			clockView();
		} else if (mName.equals("reminder")) {
			reminderView();
		}
	}

	private void clockView() {
		Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);
		mActivity.startActivity(alarmas);
	}

	@SuppressLint("NewApi")
	private void reminderView() {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setData(CalendarContract.Events.CONTENT_URI);
		mActivity.startActivity(intent);
	}
}
