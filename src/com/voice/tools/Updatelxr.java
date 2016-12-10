package com.voice.tools;

import android.content.Context;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ContactManager.ContactListener;

public class Updatelxr {
	// 语音听写对象
	private SpeechRecognizer mIat;

	public void updatelxr(Context context) {
		ContactManager mgr = ContactManager.createManager(context,
				mContactListener);
		mgr.asyncQueryAllContactsName();
	}

	int ret = 0;// 函数调用返回值
	/**
	 * 获取联系人监听器。
	 */
	private ContactListener mContactListener = new ContactListener() {

		@Override
		public void onContactQueryFinish(final String contactInfos,
				boolean changeFlag) {
			// 注：实际应用中除第一次上传之外，之后应该通过changeFlag判断是否需要上传，否则会造成不必要的流量.
			// 每当联系人发生变化，该接口都将会被回调，可通过ContactManager.destroy()销毁对象，解除回调。
			if (changeFlag) {
				// 指定引擎类型
				Log.e("TAG", "正在上传联系人");
				mIat.setParameter(SpeechConstant.ENGINE_TYPE,
						SpeechConstant.TYPE_CLOUD);
				mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
				ret = mIat.updateLexicon("contact", contactInfos,
						mLexiconListener);
				if (ret != ErrorCode.SUCCESS) {
					Log.e("TAG", "上传联系人失败：" + ret);
				}
			}else{
				Log.e("TAG", "已上传过联系人");
			}
		}
	};
	/**
	 * 上传联系人/词表监听器。
	 */
	private LexiconListener mLexiconListener = new LexiconListener() {

		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			if (error != null) {
			} else {
				Log.e("TAG", "上传成功");
			}
		}
	};

}
