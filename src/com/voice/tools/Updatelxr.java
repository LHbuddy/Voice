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
	// ������д����
	private SpeechRecognizer mIat;

	public void updatelxr(Context context) {
		ContactManager mgr = ContactManager.createManager(context,
				mContactListener);
		mgr.asyncQueryAllContactsName();
	}

	int ret = 0;// �������÷���ֵ
	/**
	 * ��ȡ��ϵ�˼�������
	 */
	private ContactListener mContactListener = new ContactListener() {

		@Override
		public void onContactQueryFinish(final String contactInfos,
				boolean changeFlag) {
			// ע��ʵ��Ӧ���г���һ���ϴ�֮�⣬֮��Ӧ��ͨ��changeFlag�ж��Ƿ���Ҫ�ϴ����������ɲ���Ҫ������.
			// ÿ����ϵ�˷����仯���ýӿڶ����ᱻ�ص�����ͨ��ContactManager.destroy()���ٶ��󣬽���ص���
			if (changeFlag) {
				// ָ����������
				Log.e("TAG", "�����ϴ���ϵ��");
				mIat.setParameter(SpeechConstant.ENGINE_TYPE,
						SpeechConstant.TYPE_CLOUD);
				mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
				ret = mIat.updateLexicon("contact", contactInfos,
						mLexiconListener);
				if (ret != ErrorCode.SUCCESS) {
					Log.e("TAG", "�ϴ���ϵ��ʧ�ܣ�" + ret);
				}
			}else{
				Log.e("TAG", "���ϴ�����ϵ��");
			}
		}
	};
	/**
	 * �ϴ���ϵ��/�ʱ��������
	 */
	private LexiconListener mLexiconListener = new LexiconListener() {

		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			if (error != null) {
			} else {
				Log.e("TAG", "�ϴ��ɹ�");
			}
		}
	};

}
