package com.voice.tools;

import android.os.Handler;

import com.voice.act.MainActivity;

public class Translation {
	private String mTarget, mSource, mContent, mResult;
	private MainActivity mActivity;
	private Handler handler = new Handler();

	public Translation(String target, String source, String content,
			MainActivity activity) {
		mTarget = target;
		mSource = source;
		mContent = content;
		mActivity = activity;
	}

	public void start() {// 开始翻译
		mTarget = changeLanguageCode(mTarget);
		mSource = changeLanguageCode(mSource);
		new Thread(r).start();
	}

	private String changeLanguageCode(String originalCode) {// 改变语言编码方式
		// TODO Auto-generated method stub
		if (originalCode.equals("ja")) {
			return "jp";
		} else if (originalCode.equals("ko")) {
			return "kor";
		} else if (originalCode.equals("es")) {
			return "spa";
		} else if (originalCode.equals("fr")) {
			return "fra";
		} else {
			return originalCode;
		}
	}

	Runnable r = new Runnable() {// 调用百度翻译API
		@Override
		public void run() {
			mResult = Utils.upLoad(mContent, mSource, mTarget);
			handler.post(new Runnable() {
				@Override
				public void run() {
					mActivity.addlist(mResult, "0");
				}
			});

		}
	};
}
