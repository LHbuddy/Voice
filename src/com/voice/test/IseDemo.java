package com.voice.test;

import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.iflytek.sunflower.FlowerCollector;
import com.voice.act.R;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IseDemo extends Activity implements OnClickListener {
	private static String TAG = IseDemo.class.getSimpleName();

	private final static String PREFER_NAME = "ise_settings";
	private final static int REQUEST_CODE_SETTINGS = 1;

	private EditText mEvaTextEditText;
	private EditText mResultEditText;
	private Button mIseStartButton;
	private Toast mToast;

	// ��������
	private String language;
	// ��������
	private String category;
	// ����ȼ�
	private String result_level;

	private String mLastResult;
	private SpeechEvaluator mIse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ise_demo);
		mIse = SpeechEvaluator.createEvaluator(IseDemo.this, null);
		initUI();
		setEvaText();
	}

	// ��������ӿ�
	private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {

		public void onResult(EvaluatorResult result, boolean isLast) {
			Log.d(TAG, "evaluator result :" + isLast);

			if (isLast) {
				StringBuilder builder = new StringBuilder();
				builder.append(result.getResultString());

				if (!TextUtils.isEmpty(builder)) {
					mResultEditText.setText(builder.toString());
				}
				mIseStartButton.setEnabled(true);
				mLastResult = builder.toString();

				showTip("�������");
			}
		}

		public void onError(SpeechError error) {
			mIseStartButton.setEnabled(true);
			if (error != null) {
				showTip("error:" + error.getErrorCode() + ","
						+ error.getErrorDescription());
				mResultEditText.setText("");
				mResultEditText.setHint("��������ʼ���⡱��ť");
			} else {
				Log.d(TAG, "evaluator over");
			}
		}

		public void onBeginOfSpeech() {
			// �˻ص���ʾ��sdk�ڲ�¼�����Ѿ�׼�����ˣ��û����Կ�ʼ��������
			Log.d(TAG, "evaluator begin");
		}

		public void onEndOfSpeech() {
			// �˻ص���ʾ����⵽��������β�˵㣬�Ѿ�����ʶ����̣����ٽ�����������
			Log.d(TAG, "evaluator stoped");
		}

		public void onVolumeChanged(int volume, byte[] data) {
			showTip("��ǰ������" + volume);
			Log.d(TAG, "������Ƶ���ݣ�" + data.length);
		}

		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// ���´������ڻ�ȡ���ƶ˵ĻỰid����ҵ�����ʱ���Ựid�ṩ������֧����Ա�������ڲ�ѯ�Ự��־����λ����ԭ��
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}

	};

	private void initUI() {
		mEvaTextEditText = (EditText) findViewById(R.id.ise_eva_text);
		mResultEditText = (EditText) findViewById(R.id.ise_result_text);
		mIseStartButton = (Button) findViewById(R.id.ise_start);
		mIseStartButton.setOnClickListener(IseDemo.this);
		findViewById(R.id.ise_parse).setOnClickListener(IseDemo.this);
		findViewById(R.id.ise_stop).setOnClickListener(IseDemo.this);
		findViewById(R.id.ise_cancel).setOnClickListener(IseDemo.this);

		mToast = Toast.makeText(IseDemo.this, "", Toast.LENGTH_LONG);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.image_ise_set:
			Intent intent = new Intent(IseDemo.this, IseSettings.class);
			startActivityForResult(intent, REQUEST_CODE_SETTINGS);
			break;
		case R.id.ise_start:
			if (mIse == null) {
				return;
			}

			String evaText = mEvaTextEditText.getText().toString();
			mLastResult = null;
			mResultEditText.setText("");
			mResultEditText.setHint("���ʶ���������");
			mIseStartButton.setEnabled(false);

			setParams();
			mIse.startEvaluating(evaText, null, mEvaluatorListener);
			break;
		case R.id.ise_parse:
			// �������ս��

			if (!TextUtils.isEmpty(mLastResult)) {

				XmlResultParser resultParser = new XmlResultParser();
				// �˴������д��� ǿת�� result
				Result result = resultParser.parse(mLastResult);

				if (null != result) {
					mResultEditText.setText(result.toString());
				} else {
					showTip("�������Ϊ��");
				}
			}
			break;
		case R.id.ise_stop:

			if (mIse.isEvaluating()) {
				mResultEditText.setHint("������ֹͣ���ȴ������...");
				mIse.stopEvaluating();

			}
			break;
		case R.id.ise_cancel: {
			finish();
			break;
		}
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (REQUEST_CODE_SETTINGS == requestCode) {
			setEvaText();
		}
	}

	protected void onDestroy() {
		super.onDestroy();

		if (null != mIse) {
			mIse.destroy();
			mIse = null;
		}
	}

	// ������������
	private void setEvaText() {
		SharedPreferences pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
		language = pref.getString(SpeechConstant.LANGUAGE, "zh_cn");
		category = pref.getString(SpeechConstant.ISE_CATEGORY, "read_sentence");

		String text = "";
		if ("en_us".equals(language)) {
			if ("read_word".equals(category)) {
				text = getString(R.string.text_en_word);
			} else if ("read_sentence".equals(category)) {
				text = getString(R.string.text_en_sentence);
			}
		} else {
			// ��������
			if ("read_syllable".equals(category)) {
				text = getString(R.string.text_cn_syllable);
			} else if ("read_word".equals(category)) {
				text = getString(R.string.text_cn_word);
			} else if ("read_sentence".equals(category)) {
				text = getString(R.string.text_cn_sentence);
			}
		}

		mEvaTextEditText.setText(text);
		mResultEditText.setText("");
		mLastResult = null;
		mResultEditText.setHint("��������ʼ���⡱��ť");
	}

	private void showTip(String str) {
		if (!TextUtils.isEmpty(str)) {
			mToast.setText(str);
			mToast.show();
		}
	}

	private void setParams() {
		SharedPreferences pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
		// ������������
		language = pref.getString(SpeechConstant.LANGUAGE, "zh_cn");
		// ������Ҫ���������
		category = pref.getString(SpeechConstant.ISE_CATEGORY, "read_sentence");
		// ���ý���ȼ������Ľ�֧��complete��
		result_level = pref.getString(SpeechConstant.RESULT_LEVEL, "complete");
		// ��������ǰ�˵�:������ʱʱ�䣬���û��೤ʱ�䲻˵��������ʱ����
		String vad_bos = pref.getString(SpeechConstant.VAD_BOS, "5000");
		// ����������˵�:��˵㾲�����ʱ�䣬���û�ֹͣ˵���೤ʱ���ڼ���Ϊ�������룬 �Զ�ֹͣ¼��
		String vad_eos = pref.getString(SpeechConstant.VAD_EOS, "1800");
		// �������볬ʱʱ�䣬���û�����������˵�೤ʱ�䣻
		String speech_timeout = pref.getString(
				SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");

		mIse.setParameter(SpeechConstant.LANGUAGE, language);
		mIse.setParameter(SpeechConstant.ISE_CATEGORY, category);
		mIse.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
		mIse.setParameter(SpeechConstant.VAD_BOS, vad_bos);
		mIse.setParameter(SpeechConstant.VAD_EOS, vad_eos);
		mIse.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, speech_timeout);
		mIse.setParameter(SpeechConstant.RESULT_LEVEL, result_level);

		// ������Ƶ����·����������Ƶ��ʽ֧��pcm��wav������·��Ϊsd����ע��WRITE_EXTERNAL_STORAGEȨ��
		// ע��AUDIO_FORMAT���������Ҫ���°汾������Ч
		mIse.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/msc/ise.wav");
	}

	protected void onResume() {
		// ����ͳ�� �ƶ�����ͳ�Ʒ���
		FlowerCollector.onResume(IseDemo.this);
		FlowerCollector.onPageStart(TAG);
		super.onResume();
	}

	protected void onPause() {
		// ����ͳ�� �ƶ�����ͳ�Ʒ���
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(IseDemo.this);
		super.onPause();
	}

}
