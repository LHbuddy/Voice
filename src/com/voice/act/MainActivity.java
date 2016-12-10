package com.voice.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.r;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.cloud.util.ContactManager.ContactListener;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;
import com.iflytek.thirdparty.bt;
import com.voice.test.IseDemo;
import com.voice.tools.CallAction;
import com.voice.tools.CallView;
import com.voice.tools.HttpUtil;
import com.voice.tools.JsonParser;
import com.voice.tools.MessageView;
import com.voice.tools.MyFlashView;
import com.voice.tools.OpenAppAction;
import com.voice.tools.SearchShop;
import com.voice.tools.OpenQA;
import com.voice.tools.OpenWebsite;
import com.voice.tools.ScheduleCreate;
import com.voice.tools.ScheduleView;
import com.voice.tools.SearchAction;
import com.voice.tools.SearchApp;
import com.voice.tools.SearchTrain;
import com.voice.tools.SearchWeather;
import com.voice.tools.SendContacts;
import com.voice.tools.SendMessage;
import com.voice.tools.Translation;
import com.voice.tools.Updatelxr;

@SuppressLint("InlinedApi")
public class MainActivity extends Activity implements OnClickListener {

	// ������д����
	private SpeechRecognizer mIat;
	// ����
	private static final int ANIMATIONEACHOFFSET = 600; // ÿ�������Ĳ���ʱ����
	private AnimationSet aniSet, aniSet2, aniSet3;
	private ImageView btn, wave1, wave2, wave3;

	// �����ϳɶ���
	private SpeechSynthesizer mTts;

	// Ĭ���ƶ˷�����
	public static String voicerCloud = "xiaoyan";

	// Ĭ�ϱ��ط�����
	public static String voicerLocal = "xiaoyan";

	// �������
	private int mPercentForBuffering = 0;

	// ���Ž���
	private int mPercentForPlaying = 0;

	// �ƶ�/����ѡ��ť
	private RadioGroup mRadioGroup;

	// ��������
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	private static String TAG = "IatDemo";

	// �������������������壩��
	private SpeechUnderstander mSpeechUnderstander;
	private static String SAResult = "";// ����ʶ����
	// ��HashMap�洢��д���
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	public String talkstr = "";
	public static JSONObject semantic = null, slots = null, answer = null,
			datetime = null, location = null, data = null,
			trainJsonObject = null, webpage = null, starttime = null;
	public static String operation = null, service = null, tvtime = null;
	public static JSONArray result = null, tvArray = null;
	public static String receiver = null, name = null, price = null,
			code = null, song = null, keywords = null, content = null,
			url = null, gifturl = null, giftname = null, text = null,
			time = null, date = null, dateOrig = null, city = null,
			sourceName = null, target = null, source = null, endloc = null,
			startloc = null, areaend = null, areastart = null, poiend = null,
			poistart = null, tvname = null;
	public static String[] weatherDate = null, weather = null,
			tempRange = null, airQuality = null, wind = null, humidity = null,
			windLevel = null;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private ListView listView;
	private myadapter myad;
	private String allcontent = "", allrobot = "";
	private int bdfh = 0;
	private MediaPlayer media;
	// ����
	private String[] helpstrs = { "����������˵��", "��绰������ĳĳĳ�ĵ绰��", "���Ͷ��š����Ͷ��Ÿ�ĳĳĳ��������***��", "��Ӧ�á���***��",
			"����Ӧ�á�����***��", "����Ż���վ����վ�����ɡ�", "�ؼ�������������***��", "��Ʊ��ѯ��**��**�Ļ�Ʊ��",
			"������ѯ����ѯĳ��ĳ���������", "��ͼ������ĳ�ص�ĳ����ô�ߡ�", "��������10+20-10*2/4=25��",
			"�����ճ̡������ӡ��ճ̡�", "�����ʴ���������������Ϣ���ܻش�", "������Ʒ������***",
			"���ӽ�Ŀ�б��cctv1ĳ��ĵ��ӽ�Ŀ�б�", "����������������⡣" };
	// �����޷��ش�
	private String[] dontlist = { "���粻�����ɱ㣬����һ��Ҳ���硣", "��������Ҳ�֪�������ԹҶ���֦��",
			"��������Ҳ��У�ֻ�ü�װ���羰��", "����㶼��֪������Ҳ��֪��hahahaha��",
			"������������Ϧ���µı��ܣ�������û������ഺ��", "bibibibibibibi�������ðɣ��Ҳ��ᣡ",
			"��ͤ�⣬�ŵ��ߣ�һ�а��������죬�����ֻ��Ϲ�࿩��", "�Ҳ��ᣬ���ҿ���", "�����Ҳ��ᣬ������Ҳ���ʡ�",
			"���������������ؾ��飺�±ƻ����ˣ��뽫�ֻ���������ģʽ�����������������������������" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		aniSet = getNewAnimationSet();
		aniSet2 = getNewAnimationSet();
		aniSet3 = getNewAnimationSet();
		setContentView(R.layout.activity_main);
		// ͸��״̬��
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// ͸��������
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		btn = (ImageView) findViewById(R.id.btn);
		wave1 = (ImageView) findViewById(R.id.wave1);
		wave2 = (ImageView) findViewById(R.id.wave2);
		wave3 = (ImageView) findViewById(R.id.wave3);
		wave1.setVisibility(View.GONE);
		wave2.setVisibility(View.GONE);
		wave3.setVisibility(View.GONE);
		btn.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.conlist);
		myadapter ad = new myadapter();
		listView.setAdapter(ad);
		mSharedPreferences = getSharedPreferences("com.iflytek.setting",
				Activity.MODE_PRIVATE);
		// ��ʼ���ϳɶ���
		mTts = SpeechSynthesizer.createSynthesizer(this, mInitListener);
		// ��ʼ������ʶ�����
		mSpeechUnderstander = SpeechUnderstander.createUnderstander(this,
				mInitListener);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		myad = new myadapter();
		listView.setAdapter(myad);
		text = "���ã��������������������֡�Ŀǰ�����Ĺ����У���绰�����Ͷ��ţ���Ӧ�ã�����Ӧ�ã�����Ż���վ���ؼ�����������Ʊ��ѯ��������ѯ����ͼ�������������������ճ̣������ʴ�,������Ʒ,���ӽ�Ŀ�б�(��Ȩԭ�򣬽���cctv),��������ȡ�����������Ѷ���ṩ��";
		addlist(text, "0");
		// Updatelxr updatelxr=new Updatelxr();
		// updatelxr.updatelxr(MainActivity.this);
	}

	int ret = 0;// �������÷���ֵ

	public void onClick(View view) {
		bdfh = 0;
		allcontent = "";
		allrobot = "";
		switch (view.getId()) {
		// ��ʼ��д
		// ����ж�һ����д������OnResult isLast=true ���� onError
		case R.id.btn:
			// ���ò���
			setParam();
			if (mSpeechUnderstander.isUnderstanding()) {// ��ʼǰ���״̬
				mSpeechUnderstander.stopUnderstanding();
				btn.setBackgroundResource(R.drawable.yy);
				wave1.setVisibility(View.GONE);
				wave2.setVisibility(View.GONE);
				wave3.setVisibility(View.GONE);
				media = MediaPlayer.create(MainActivity.this, R.drawable.end);
				media.start();
				cancalWaveAnimation();
				showTip("���������������...");
			} else {
				ret = mSpeechUnderstander
						.startUnderstanding(speechUnderstandListener);
				if (ret != 0) {
					showTip("�������ʧ��,������:" + ret);
				} else {
					btn.setBackgroundResource(R.drawable.yyls);
					wave1.setVisibility(View.VISIBLE);
					wave2.setVisibility(View.VISIBLE);
					wave3.setVisibility(View.VISIBLE);
					media = MediaPlayer.create(MainActivity.this,
							R.drawable.start);
					media.start();
					showWaveAnimation();
					// showTip("��ʼ˵��");
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * ��ʼ����������
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("��ʼ��ʧ�ܣ������룺" + code);
			}
		}
	};

	/**
	 * ����ʶ��ص���
	 */
	private SpeechUnderstanderListener speechUnderstandListener = new SpeechUnderstanderListener() {

		@Override
		public void onResult(final UnderstanderResult result) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (null != result) {
						// ��ʾ
						SAResult = result.getResultString();
						Log.e("TAG", SAResult);
						if (!TextUtils.isEmpty(SAResult)) {
							try {
								JSONObject object = new JSONObject(SAResult);
								talkstr = object.getString("text");
								addlist(object.getString("text"), "1");
								getJsonData();
							} catch (Exception e) {
								// TODO: handle exception
							}

						}
					} else {
						showTip(getString(R.string.text_begin));
					}
				}
			});
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			// showTip("��ǰ����˵����������С��" + volume);
			// Log.d(TAG, data.length + "");
		}

		@Override
		public void onEndOfSpeech() {
			// �˻ص���ʾ����⵽��������β�˵㣬�Ѿ�����ʶ����̣����ٽ�����������
			// showTip("����˵��");
			btn.setBackgroundResource(R.drawable.yy);
			wave1.setVisibility(View.GONE);
			wave2.setVisibility(View.GONE);
			wave3.setVisibility(View.GONE);
			media = MediaPlayer.create(MainActivity.this, R.drawable.end);
			media.start();
			cancalWaveAnimation();
		}

		@Override
		public void onBeginOfSpeech() {
			// �˻ص���ʾ��sdk�ڲ�¼�����Ѿ�׼�����ˣ��û����Կ�ʼ��������
			// startlisten.setBackgroundResource(R.drawable.yyls);
			// showTip("��ʼ˵��");
		}

		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// ���´������ڻ�ȡ���ƶ˵ĻỰid����ҵ�����ʱ���Ựid�ṩ������֧����Ա�������ڲ�ѯ�Ự��־����λ����ԭ��
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}
	};

	public void addlist(String content, String type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("content", content);
		map.put("type", type);
		list.add(map);
		myad.notifyDataSetChanged();
		mTts.stopSpeaking();
		if (type.equals("0")) {
			// ���ò���
			setTtsparam();
			int code = mTts.startSpeaking(
					list.get(list.size() - 1).get("content"), mTtsListener);
			// /**
			// * ֻ������Ƶ�����в��Žӿ�,���ô˽ӿ���ע��startSpeaking�ӿ�
			// * text:Ҫ�ϳɵ��ı���uri:��Ҫ�������Ƶȫ·����listener:�ص��ӿ�
			// */
			// String path =
			// Environment.getExternalStorageDirectory()+"/tts.pcm";
			// int code = mTts.synthesizeToUri(text, path, mTtsListener);

			if (code != ErrorCode.SUCCESS) {
				showTip("�����ϳ�ʧ��,������: " + code);
			}
		}
	}

	public void calltel(JSONObject jsonObject) {
		MainActivity.this.getPackageName();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x221) {
				myad.notifyDataSetChanged();
			}
			if (msg.what == 0x222) {
				wave2.startAnimation(aniSet2);
			}
			if (msg.what == 0x333) {
				wave3.startAnimation(aniSet3);
			}
			if (msg.what == 0x444) {
				for (int i = 0; i < tvArray.length(); i++) {
					try {
						JSONObject tvob = tvArray.getJSONObject(i);
						addlist(tvob.optString("time") + "������"
								+ tvob.optString("pName"), "3");
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			if (msg.what == 0x445) {
				addlist("δ��ѯ��Ƶ������", "0");
			}

		};
	};

	public void getJsonData() {
		// speak("here",false);
		resallstr();
		try {
			JSONObject SAResultJson = new JSONObject(SAResult);
			operation = SAResultJson.optString("operation");
			service = SAResultJson.optString("service");
			semantic = SAResultJson.optJSONObject("semantic");
			answer = SAResultJson.optJSONObject("answer");
			data = SAResultJson.optJSONObject("data");
			webpage = SAResultJson.optJSONObject("webPage");
			if (data == null) {
			} else {
				result = data.optJSONArray("result");
			}
			if (webpage == null) {
			} else {
				gifturl = webpage.optString("url");
			}
			if (result == null) {
			} else {
				// ����Ҫ��ʼ�����鲻Ȼ���еò����κν��������
				// ����
				giftname = result.getJSONObject(0).optString("name");
				if (service.equals("weather")) {
					// ����
					airQuality = new String[10];
					weatherDate = new String[10];
					wind = new String[10];
					humidity = new String[10];
					windLevel = new String[10];
					weather = new String[10];
					tempRange = new String[10];
					for (int i = 1; i < 7; i++) {
						airQuality[i - 1] = result.getJSONObject(i).optString(
								"airQuality");
						weatherDate[i - 1] = result.getJSONObject(i)
								.optString("date").substring(0, 10);
						wind[i - 1] = result.getJSONObject(i).optString("wind");
						humidity[i - 1] = result.getJSONObject(i).optString(
								"humidity");
						windLevel[i - 1] = result.getJSONObject(i).optString(
								"windLevel");
						weather[i - 1] = result.getJSONObject(i).optString(
								"weather");
						tempRange[i - 1] = result.getJSONObject(i).optString(
								"tempRange");
						sourceName = result.getJSONObject(i).optString(
								"sourceName");
					}
				}
			}

			if (answer == null) {
			} else
				text = answer.optString("text");

			if (semantic == null) {
			} else
				slots = semantic.optJSONObject("slots");

			if (slots == null) {
			} else {
				starttime = slots.optJSONObject("startTime");
				datetime = slots.optJSONObject("datetime");
				tvname = slots.optString("tvName");
				receiver = slots.optString("receiver");
				location = slots.optJSONObject("location");
				name = slots.optString("name");
				price = slots.optString("price");
				code = slots.optString("code");
				song = slots.optString("song");
				keywords = slots.optString("keywords");
				content = slots.optString("content");
				url = slots.optString("url");
				target = slots.optString("target");
				source = slots.optString("source");
				trainJsonObject = slots.optJSONObject("endLoc");
			}
			// tv
			if (starttime == null) {
			} else {
				tvtime = starttime.optString("date");
			}
			// ����
			if (trainJsonObject == null) {
			} else {
				endloc = slots.optJSONObject("endLoc").optString("cityAddr");
				startloc = slots.optJSONObject("startLoc")
						.optString("cityAddr");
				areastart = slots.optJSONObject("startLoc").optString(
						"areaAddr");
				areaend = slots.optJSONObject("endLoc").optString("areaAddr");
				poistart = slots.optJSONObject("startLoc").optString("poi");
				poiend = slots.optJSONObject("endLoc").optString("poi");
			}

			if (location == null) {
			} else {
				city = location.optString("city");
			}
			if (datetime == null) {
			} else {
				date = datetime.optString("date");
				dateOrig = datetime.optString("dateOrig");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			addlist("�������ݳ�������", "0");
			e.printStackTrace();
		}
		Log.e("TAG", service);
		CheckService();
	}

	public void CheckService() {
		// speak("�жϷ�������",false);
		// ������Ʒ
		Pattern pattern = Pattern.compile("��");
		Matcher m = pattern.matcher(talkstr);
		//��Ӧ��
		Pattern pattern2 = Pattern.compile("��");
		Matcher m2 = pattern2.matcher(talkstr);
		if (talkstr.equals("��Ŀ����ߡ�") || talkstr.equals("��İְ֡�")
				|| talkstr.equals("������ߡ�")) {
			addlist("��Ȼ��15���3��3�鿩��", "0");
		} else if (m.find()) {// ������Ʒ
			SearchShop searchShop = new SearchShop(talkstr.substring(m.end(),
					talkstr.length() - 1), this);
			searchShop.Search();
		} else if (talkstr.equals("�������⡣")) {
			addlist("���ڴ���������...", "0");
			startActivity(new Intent(MainActivity.this, IseDemo.class));
		} else if (service.equals("telephone")) {// �绰��ط���

			if (operation.equals("CALL")) { // ��绰
				// ��Ҫ�������绰����code��
				// ��ѡ����������name��������category�������������location������Ӫ��operator�����Ŷ�head_num����β��tail_num��
				// ���ɶ����ѡ����ȷ����Ҫ����
				// speak("name:"+name+"code:"+code,false);
				CallAction callAction = new CallAction(name, code, this);// Ŀǰ�ɸ������ֻ�绰���벦��绰
				callAction.start();
			}
			else if (operation.equals("VIEW")) { // �鿴�绰�����¼
				int a = (int) (Math.random() * 10);
				addlist(dontlist[a], "0");
			}
		} else if (m2.find()) {// ��Ӧ��
			OpenAppAction openApp = new OpenAppAction((talkstr.substring(
					m2.end(), talkstr.length() - 1).toLowerCase()),
					MainActivity.this);
			openApp.start();
		}

		else if (service.equals("app")) {// Ӧ����ط���

			if (m2.find()) {// ��Ӧ��
				OpenAppAction openApp = new OpenAppAction((talkstr.substring(
						m2.end(), talkstr.length() - 1).toLowerCase()),
						MainActivity.this);
				openApp.start();
			}

			else if (operation.equals("QUERY")) {// Ӧ����������Ӧ��
				SearchApp searchApp = new SearchApp(price, name, this);
				searchApp.start();
			}

		} else if (service.equals("message")) {// ������ط���

			if (operation.equals("SEND")) {// 1���Ͷ���

				SendMessage sendMessage = new SendMessage(name, code, content,
						MainActivity.this);
				sendMessage.start();
			}

			else if (operation.equals("VIEW")) {// 2�鿴���Ͷ���ҳ��
				MessageView messageView = new MessageView(this);
				messageView.start();
			}

			else if (operation.equals("SENDCONTACTS")) {// 3������Ƭ,Ŀǰֻ��ʶ�����ַ�������
				SendContacts sendContacts = new SendContacts(name, receiver,
						this);
				sendContacts.start();
			}
		}

		else if (service.equals("website")) {// ��վ��ط���

			if (operation.equals("OPEN")) {// ��ָ����ַ

				OpenWebsite openWebsite = new OpenWebsite(url, name, this);
				openWebsite.start();
			}

		}

		else if (service.equals("websearch")) {// ������ط���

			if (operation.equals("QUERY")) {// ����

				SearchAction searchAction = new SearchAction(keywords,
						MainActivity.this);
				searchAction.Search();
			}

		}

		else if (service.equals("faq")) {// �����ʴ���ط���

			if (operation.equals("ANSWER")) {// �����ʴ�
				OpenQA openQA = new OpenQA(text, this);
				openQA.start();
			}
		} else if (service.equals("chat")) {// ������ط���

			if (operation.equals("ANSWER")) {// ����ģʽ
				OpenQA openQA = new OpenQA(text, this);
				openQA.start();
			}
		}

		else if (service.equals("openQA")) {// �����ʴ���ط���

			if (operation.equals("ANSWER")) {// �����ʴ�
				OpenQA openQA = new OpenQA(text, this);
				openQA.start();
			}
		}

		else if (service.equals("baike")) {// �ٿ�֪ʶ��ط���

			if (operation.equals("ANSWER")) {// �ٿ�

				OpenQA openQA = new OpenQA(text, this);
				openQA.start();

			}
		}

		else if (service.equals("weather")) {// ������ط���
			if (operation.equals("QUERY")) {// ��ѯ����

				SearchWeather searchWeather = new SearchWeather(date, dateOrig,
						city, sourceName, weatherDate, weather, tempRange,
						airQuality, wind, humidity, windLevel, this);
				searchWeather.start();
			}
		} else if (service.equals("schedule")) {// �����ճ�
			if (operation.equals("CREATE")) {// 1�����ճ�/����
				ScheduleCreate scheduleCreate = new ScheduleCreate(name,
						MainActivity.this);
				scheduleCreate.start();
				addlist("���ڽ�����������...", "0");
			} else if (operation.equals("VIEW")) {// 1�鿴����/����
				if (name.equals("clock")) {
					Intent alarmas = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
					startActivity(alarmas);
					addlist("���ڽ�������...", "0");
				}
			}
		} else if (service.equals("datetime")) {// ���ڷ���
			addlist(text, "0");
		} else if (service.equals("train")) {// �𳵷���
			SearchTrain searchAction = new SearchTrain(startloc, endloc,
					MainActivity.this);
			searchAction.Search();
		} else if (service.equals("tv")) {// �����б�
			String regEx = "[^0-9]";
			Pattern p1 = Pattern.compile(regEx);
			Matcher m1 = p1.matcher(talkstr);
			gettvarr("cctv" + m1.replaceAll("").trim(), tvtime);
			Log.e("TAG", "cctv" + m1.replaceAll("").trim());
		} else if (service.equals("map")) {// ����
			addlist("���ڵ���...", "0");
			Intent intent = new Intent(MainActivity.this, RoutePlanDemo.class);
			if (areastart.equals("") && poistart.equals("")) {
				intent.putExtra("start", startloc);
			} else if (poistart.equals("") && startloc.equals("")) {
				intent.putExtra("start", areastart);
			} else {
				intent.putExtra("start", poistart);
			}
			if (areaend.equals("") && poiend.equals("")) {
				intent.putExtra("end", endloc);
			} else if (poiend.equals("") && endloc.equals("")) {
				intent.putExtra("end", areaend);
			} else {
				intent.putExtra("end", poiend);
			}
			// 39483
			startActivity(intent);
		} else if (service.equals("calc")) {// ��Ʒ����
			addlist(text, "0");
		} else {
			int a = (int) (Math.random() * 10);
			addlist(dontlist[a], "0");
		}

	}

	private void showTip(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.setText(str);
				mToast.show();
			}
		});
	}

	// �ϳɻص�������
	private SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onSpeakBegin() {
			// showTip("��ʼ����");

		}

		@Override
		public void onSpeakPaused() {
			// showTip("��ͣ����");
		}

		@Override
		public void onSpeakResumed() {
			// showTip("��������");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			// �ϳɽ���
			mPercentForBuffering = percent;

		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// ���Ž���
			mPercentForPlaying = percent;
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
			} else if (error != null) {
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// ���´������ڻ�ȡ���ƶ˵ĻỰid����ҵ�����ʱ���Ựid�ṩ������֧����Ա�������ڲ�ѯ�Ự��־����λ����ԭ��
			// ��ʹ�ñ����������ỰidΪnull
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}
	};

	// ��������
	private void setTtsparam() {
		// ��ղ���
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// ���úϳ�
		if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			// ����ʹ���ƶ�����
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_CLOUD);
			// ���÷�����
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicerCloud);
		} else {
			// ����ʹ�ñ�������
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_LOCAL);
			// ���÷�������Դ·��
			mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
			// ���÷�����
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);
		}
		// ���úϳ�����
		mTts.setParameter(SpeechConstant.SPEED,
				mSharedPreferences.getString("speed_preference", "50"));
		// ���úϳ�����
		mTts.setParameter(SpeechConstant.PITCH,
				mSharedPreferences.getString("pitch_preference", "50"));
		// ���úϳ�����
		mTts.setParameter(SpeechConstant.VOLUME,
				mSharedPreferences.getString("volume_preference", "50"));
		// ���ò�������Ƶ������
		mTts.setParameter(SpeechConstant.STREAM_TYPE,
				mSharedPreferences.getString("stream_preference", "3"));

		// ���ò��źϳ���Ƶ������ֲ��ţ�Ĭ��Ϊtrue
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

		// ������Ƶ����·����������Ƶ��ʽ֧��pcm��wav������·��Ϊsd����ע��WRITE_EXTERNAL_STORAGEȨ��
		// ע��AUDIO_FORMAT���������Ҫ���°汾������Ч
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/tts.wav");
	}

	// ��ȡ��������Դ·��
	private String getResourcePath() {
		StringBuffer tempBuffer = new StringBuffer();
		// �ϳ�ͨ����Դ
		tempBuffer.append(ResourceUtil.generateResourcePath(this,
				RESOURCE_TYPE.assets, "tts/common.jet"));
		tempBuffer.append(";");
		// ��������Դ
		tempBuffer.append(ResourceUtil.generateResourcePath(this,
				RESOURCE_TYPE.assets, "tts/" + MainActivity.voicerLocal
						+ ".jet"));
		return tempBuffer.toString();
	}

	// ��������
	public void setParam() {
		String lag = mSharedPreferences.getString(
				"understander_language_preference", "mandarin");
		if (lag.equals("en_us")) {
			// ��������
			mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// ��������
			mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// ������������
			mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, lag);
		}
		// ��������ǰ�˵�:������ʱʱ�䣬���û��೤ʱ�䲻˵��������ʱ����
		mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("understander_vadbos_preference",
						"4000"));

		// ����������˵�:��˵㾲�����ʱ�䣬���û�ֹͣ˵���೤ʱ���ڼ���Ϊ�������룬 �Զ�ֹͣ¼��
		mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("understander_vadeos_preference",
						"1500"));

		// ���ñ����ţ�Ĭ�ϣ�1���б�㣩
		mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("understander_punc_preference",
						"1"));

		// ������Ƶ����·����������Ƶ��ʽ֧��pcm��wav������·��Ϊsd����ע��WRITE_EXTERNAL_STORAGEȨ��
		// ע��AUDIO_FORMAT���������Ҫ���°汾������Ч
		mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/sud.wav");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// �˳�ʱ�ͷ�����
		mSpeechUnderstander.cancel();
		mSpeechUnderstander.destroy();
		mTts.stopSpeaking();
		// �˳�ʱ�ͷ�����
		mTts.destroy();
	}

	private class myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View v, ViewGroup arg2) {
			// TODO Auto-generated method stub
			v = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.con_list_item, null);
			TextView left = (TextView) v.findViewById(R.id.lefttext);
			TextView right = (TextView) v.findViewById(R.id.righttext);
			left.setText(list.get(arg0).get("content"));
			right.setText(list.get(arg0).get("content"));
			if (list.get(arg0).get("type").equals("0")
					|| list.get(arg0).get("type").equals("3")) {
				// ���䶯��
				if (list.size() - 1 == arg0) {
					Animation animation = AnimationUtils.loadAnimation(
							MainActivity.this, R.anim.animation61);
					left.startAnimation(animation);
				}
				right.setVisibility(View.GONE);
			} else {
				left.setVisibility(View.GONE);
			}

			return v;
		}
	}

	// ����̨��Ŀ���б�
	public void gettvarr(String code, String time) {
		String urlString = "http://japi.juhe.cn/tv/getProgram?";
		String codeString = "code=" + code;
		String timeString = "date=" + time;
		final String url = urlString + codeString + "&" + timeString
				+ "&key=bb33b678b14e83be28273cd95bdbf7fb";
		Log.e("TAG", url);
		new Thread() {
			public void run() {
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("aaa", "aaa");
					String string = HttpUtil.doPost(url, map);
					JSONObject jsonObject = new JSONObject(string);
					if (!(jsonObject.getString("error_code").equals("0"))) {
						handler.sendEmptyMessage(0x445);
					} else {
						tvArray = jsonObject.getJSONArray("result");
						handler.sendEmptyMessage(0x444);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("TAG", "catch");
				}

			};
		}.start();

	}

	private AnimationSet getNewAnimationSet() {
		AnimationSet as = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1f, 2.3f, 1f, 2.3f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(ANIMATIONEACHOFFSET * 3);
		sa.setRepeatCount(-1);// ����ѭ��
		AlphaAnimation aniAlp = new AlphaAnimation(1, 0.1f);
		aniAlp.setRepeatCount(-1);// ����ѭ��
		as.setDuration(ANIMATIONEACHOFFSET * 3);
		as.addAnimation(sa);
		as.addAnimation(aniAlp);
		return as;
	}

	private void showWaveAnimation() {
		wave1.startAnimation(aniSet);
		handler.sendEmptyMessageDelayed(0x222, ANIMATIONEACHOFFSET);
		handler.sendEmptyMessageDelayed(0x333, ANIMATIONEACHOFFSET * 2);
	}

	private void cancalWaveAnimation() {
		wave1.clearAnimation();
		wave2.clearAnimation();
		wave3.clearAnimation();
	}

	public void helponclick(View v) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get("content").equals("����������˵��")) {
				for (int j = 0; j < helpstrs.length; j++) {
					Log.e("TAG", "i" + i + "j" + j);
					list.remove(i);
				}
			}
		}
		for (int i = 0; i < helpstrs.length; i++) {
			addlist(helpstrs[i], "3");
		}
	}

	// ˫�������¼�
	long touchTime = 0;
	long waitTime = 2000;

	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, "�ٰ�һ���˳�", Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			finish();
		}
	}

	public void resallstr() {
		starttime = null;
		tvtime = null;
		semantic = null;
		slots = null;
		answer = null;
		datetime = null;
		location = null;
		data = null;
		trainJsonObject = null;
		webpage = null;
		operation = null;
		service = null;
		result = null;
		tvArray = null;
		receiver = null;
		name = null;
		price = null;
		code = null;
		song = null;
		keywords = null;
		content = null;
		url = null;
		gifturl = null;
		giftname = null;
		text = null;
		time = null;
		date = null;
		city = null;
		sourceName = null;
		target = null;
		source = null;
		endloc = null;
		startloc = null;
		areaend = null;
		areastart = null;
		poiend = null;
		poistart = null;
		tvname = null;
		weatherDate = null;
		weather = null;
		tempRange = null;
		airQuality = null;
		wind = null;
		humidity = null;
		windLevel = null;
	}
}
