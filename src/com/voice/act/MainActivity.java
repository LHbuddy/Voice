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

	// 语音听写对象
	private SpeechRecognizer mIat;
	// 动画
	private static final int ANIMATIONEACHOFFSET = 600; // 每个动画的播放时间间隔
	private AnimationSet aniSet, aniSet2, aniSet3;
	private ImageView btn, wave1, wave2, wave3;

	// 语音合成对象
	private SpeechSynthesizer mTts;

	// 默认云端发音人
	public static String voicerCloud = "xiaoyan";

	// 默认本地发音人
	public static String voicerLocal = "xiaoyan";

	// 缓冲进度
	private int mPercentForBuffering = 0;

	// 播放进度
	private int mPercentForPlaying = 0;

	// 云端/本地选择按钮
	private RadioGroup mRadioGroup;

	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	private static String TAG = "IatDemo";

	// 语义理解对象（语音到语义）。
	private SpeechUnderstander mSpeechUnderstander;
	private static String SAResult = "";// 语义识别结果
	// 用HashMap存储听写结果
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
	// 帮助
	private String[] helpstrs = { "您可以这样说：", "打电话→拨打某某某的电话。", "发送短信→发送短信给某某某，内容是***。", "打开应用→打开***。",
			"搜索应用→搜索***。", "浏览门户网站→网站名即可。", "关键词搜索→搜索***。", "火车票查询→**到**的火车票。",
			"天气查询→查询某地某天的天气。", "地图导航→某地到某地怎么走。", "计算器→10+20-10*2/4=25。",
			"闹钟日程→定闹钟、日程。", "智能问答→对于聊天类的消息智能回答。", "购买物品→购买***",
			"电视节目列表→cctv1某天的电视节目列表。", "语音评测→语音评测。" };
	// 本地无法回答
	private String[] dontlist = { "东风不与周郎便，搜索一下也来电。", "这个问题我不知，不如自挂东南枝。",
			"这个问题我不行，只好假装看风景。", "这个你都不知道？我也不知道hahahaha。",
			"我想起那天在夕阳下的奔跑，那是我没读书的青春。", "bibibibibibibi———好吧，我不会！",
			"长亭外，古道边，一行白鹭上青天，不会答，只能瞎编咯。", "我不会，怪我咯？", "反正我不会，你问我也白问。",
			"叮——您触发隐藏剧情：懵逼机器人，请将手机开启飞行模式，并将其向窗外掷出，即可完成任务。" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		aniSet = getNewAnimationSet();
		aniSet2 = getNewAnimationSet();
		aniSet3 = getNewAnimationSet();
		setContentView(R.layout.activity_main);
		// 透明状态栏
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
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
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(this, mInitListener);
		// 初始化语义识别对象
		mSpeechUnderstander = SpeechUnderstander.createUnderstander(this,
				mInitListener);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		myad = new myadapter();
		listView.setAdapter(myad);
		text = "您好，我是您的智能语音助手。目前包括的功能有：打电话，发送短信，打开应用，搜索应用，浏览门户网站，关键词搜索，火车票查询，天气查询，地图导航，计算器，闹钟日程，智能问答,购买商品,电视节目列表(版权原因，仅限cctv),语音评测等。语音功能由讯飞提供。";
		addlist(text, "0");
		// Updatelxr updatelxr=new Updatelxr();
		// updatelxr.updatelxr(MainActivity.this);
	}

	int ret = 0;// 函数调用返回值

	public void onClick(View view) {
		bdfh = 0;
		allcontent = "";
		allrobot = "";
		switch (view.getId()) {
		// 开始听写
		// 如何判断一次听写结束：OnResult isLast=true 或者 onError
		case R.id.btn:
			// 设置参数
			setParam();
			if (mSpeechUnderstander.isUnderstanding()) {// 开始前检查状态
				mSpeechUnderstander.stopUnderstanding();
				btn.setBackgroundResource(R.drawable.yy);
				wave1.setVisibility(View.GONE);
				wave2.setVisibility(View.GONE);
				wave3.setVisibility(View.GONE);
				media = MediaPlayer.create(MainActivity.this, R.drawable.end);
				media.start();
				cancalWaveAnimation();
				showTip("正在请求网络服务...");
			} else {
				ret = mSpeechUnderstander
						.startUnderstanding(speechUnderstandListener);
				if (ret != 0) {
					showTip("语义理解失败,错误码:" + ret);
				} else {
					btn.setBackgroundResource(R.drawable.yyls);
					wave1.setVisibility(View.VISIBLE);
					wave2.setVisibility(View.VISIBLE);
					wave3.setVisibility(View.VISIBLE);
					media = MediaPlayer.create(MainActivity.this,
							R.drawable.start);
					media.start();
					showWaveAnimation();
					// showTip("开始说话");
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败，错误码：" + code);
			}
		}
	};

	/**
	 * 语义识别回调。
	 */
	private SpeechUnderstanderListener speechUnderstandListener = new SpeechUnderstanderListener() {

		@Override
		public void onResult(final UnderstanderResult result) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (null != result) {
						// 显示
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
			// showTip("当前正在说话，音量大小：" + volume);
			// Log.d(TAG, data.length + "");
		}

		@Override
		public void onEndOfSpeech() {
			// 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
			// showTip("结束说话");
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
			// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
			// startlisten.setBackgroundResource(R.drawable.yyls);
			// showTip("开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
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
			// 设置参数
			setTtsparam();
			int code = mTts.startSpeaking(
					list.get(list.size() - 1).get("content"), mTtsListener);
			// /**
			// * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
			// * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
			// */
			// String path =
			// Environment.getExternalStorageDirectory()+"/tts.pcm";
			// int code = mTts.synthesizeToUri(text, path, mTtsListener);

			if (code != ErrorCode.SUCCESS) {
				showTip("语音合成失败,错误码: " + code);
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
						addlist(tvob.optString("time") + "播出："
								+ tvob.optString("pName"), "3");
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			if (msg.what == 0x445) {
				addlist("未查询到频道数据", "0");
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
				// 必须要初始化数组不然会有得不到任何结果的问题
				// 礼物
				giftname = result.getJSONObject(0).optString("name");
				if (service.equals("weather")) {
					// 天气
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
			// 导航
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
			addlist("解析数据出现问题", "0");
			e.printStackTrace();
		}
		Log.e("TAG", service);
		CheckService();
	}

	public void CheckService() {
		// speak("判断服务类型",false);
		// 购买物品
		Pattern pattern = Pattern.compile("买");
		Matcher m = pattern.matcher(talkstr);
		//打开应用
		Pattern pattern2 = Pattern.compile("打开");
		Matcher m2 = pattern2.matcher(talkstr);
		if (talkstr.equals("你的开发者。") || talkstr.equals("你的爸爸。")
				|| talkstr.equals("你的作者。")) {
			addlist("当然是15软件3班3组咯。", "0");
		} else if (m.find()) {// 购买物品
			SearchShop searchShop = new SearchShop(talkstr.substring(m.end(),
					talkstr.length() - 1), this);
			searchShop.Search();
		} else if (talkstr.equals("语音评测。")) {
			addlist("正在打开语音评测...", "0");
			startActivity(new Intent(MainActivity.this, IseDemo.class));
		} else if (service.equals("telephone")) {// 电话相关服务

			if (operation.equals("CALL")) { // 打电话
				// 必要条件【电话号码code】
				// 可选条件【人名name】【类型category】【号码归属地location】【运营商operator】【号段head_num】【尾号tail_num】
				// 可由多个可选条件确定必要条件
				// speak("name:"+name+"code:"+code,false);
				CallAction callAction = new CallAction(name, code, this);// 目前可根据名字或电话号码拨打电话
				callAction.start();
			}
			else if (operation.equals("VIEW")) { // 查看电话拨打记录
				int a = (int) (Math.random() * 10);
				addlist(dontlist[a], "0");
			}
		} else if (m2.find()) {// 打开应用
			OpenAppAction openApp = new OpenAppAction((talkstr.substring(
					m2.end(), talkstr.length() - 1).toLowerCase()),
					MainActivity.this);
			openApp.start();
		}

		else if (service.equals("app")) {// 应用相关服务

			if (m2.find()) {// 打开应用
				OpenAppAction openApp = new OpenAppAction((talkstr.substring(
						m2.end(), talkstr.length() - 1).toLowerCase()),
						MainActivity.this);
				openApp.start();
			}

			else if (operation.equals("QUERY")) {// 应用中心搜索应用
				SearchApp searchApp = new SearchApp(price, name, this);
				searchApp.start();
			}

		} else if (service.equals("message")) {// 短信相关服务

			if (operation.equals("SEND")) {// 1发送短信

				SendMessage sendMessage = new SendMessage(name, code, content,
						MainActivity.this);
				sendMessage.start();
			}

			else if (operation.equals("VIEW")) {// 2查看发送短信页面
				MessageView messageView = new MessageView(this);
				messageView.start();
			}

			else if (operation.equals("SENDCONTACTS")) {// 3发送名片,目前只能识别：名字发给名字
				SendContacts sendContacts = new SendContacts(name, receiver,
						this);
				sendContacts.start();
			}
		}

		else if (service.equals("website")) {// 网站相关服务

			if (operation.equals("OPEN")) {// 打开指定网址

				OpenWebsite openWebsite = new OpenWebsite(url, name, this);
				openWebsite.start();
			}

		}

		else if (service.equals("websearch")) {// 搜索相关服务

			if (operation.equals("QUERY")) {// 搜索

				SearchAction searchAction = new SearchAction(keywords,
						MainActivity.this);
				searchAction.Search();
			}

		}

		else if (service.equals("faq")) {// 社区问答相关服务

			if (operation.equals("ANSWER")) {// 社区问答
				OpenQA openQA = new OpenQA(text, this);
				openQA.start();
			}
		} else if (service.equals("chat")) {// 聊天相关服务

			if (operation.equals("ANSWER")) {// 聊天模式
				OpenQA openQA = new OpenQA(text, this);
				openQA.start();
			}
		}

		else if (service.equals("openQA")) {// 智能问答相关服务

			if (operation.equals("ANSWER")) {// 智能问答
				OpenQA openQA = new OpenQA(text, this);
				openQA.start();
			}
		}

		else if (service.equals("baike")) {// 百科知识相关服务

			if (operation.equals("ANSWER")) {// 百科

				OpenQA openQA = new OpenQA(text, this);
				openQA.start();

			}
		}

		else if (service.equals("weather")) {// 天气相关服务
			if (operation.equals("QUERY")) {// 查询天气

				SearchWeather searchWeather = new SearchWeather(date, dateOrig,
						city, sourceName, weatherDate, weather, tempRange,
						airQuality, wind, humidity, windLevel, this);
				searchWeather.start();
			}
		} else if (service.equals("schedule")) {// 闹钟日程
			if (operation.equals("CREATE")) {// 1创建日程/闹钟
				ScheduleCreate scheduleCreate = new ScheduleCreate(name,
						MainActivity.this);
				scheduleCreate.start();
				addlist("正在进入闹钟设置...", "0");
			} else if (operation.equals("VIEW")) {// 1查看闹钟/日历
				if (name.equals("clock")) {
					Intent alarmas = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
					startActivity(alarmas);
					addlist("正在进入闹钟...", "0");
				}
			}
		} else if (service.equals("datetime")) {// 日期服务
			addlist(text, "0");
		} else if (service.equals("train")) {// 火车服务
			SearchTrain searchAction = new SearchTrain(startloc, endloc,
					MainActivity.this);
			searchAction.Search();
		} else if (service.equals("tv")) {// 电视列表
			String regEx = "[^0-9]";
			Pattern p1 = Pattern.compile(regEx);
			Matcher m1 = p1.matcher(talkstr);
			gettvarr("cctv" + m1.replaceAll("").trim(), tvtime);
			Log.e("TAG", "cctv" + m1.replaceAll("").trim());
		} else if (service.equals("map")) {// 导航
			addlist("正在导航...", "0");
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
		} else if (service.equals("calc")) {// 礼品服务
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

	// 合成回调监听。
	private SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onSpeakBegin() {
			// showTip("开始播放");

		}

		@Override
		public void onSpeakPaused() {
			// showTip("暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			// showTip("继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			// 合成进度
			mPercentForBuffering = percent;

		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度
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
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}
	};

	// 参数设置
	private void setTtsparam() {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 设置合成
		if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			// 设置使用云端引擎
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_CLOUD);
			// 设置发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicerCloud);
		} else {
			// 设置使用本地引擎
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_LOCAL);
			// 设置发音人资源路径
			mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
			// 设置发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);
		}
		// 设置合成语速
		mTts.setParameter(SpeechConstant.SPEED,
				mSharedPreferences.getString("speed_preference", "50"));
		// 设置合成音调
		mTts.setParameter(SpeechConstant.PITCH,
				mSharedPreferences.getString("pitch_preference", "50"));
		// 设置合成音量
		mTts.setParameter(SpeechConstant.VOLUME,
				mSharedPreferences.getString("volume_preference", "50"));
		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE,
				mSharedPreferences.getString("stream_preference", "3"));

		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/tts.wav");
	}

	// 获取发音人资源路径
	private String getResourcePath() {
		StringBuffer tempBuffer = new StringBuffer();
		// 合成通用资源
		tempBuffer.append(ResourceUtil.generateResourcePath(this,
				RESOURCE_TYPE.assets, "tts/common.jet"));
		tempBuffer.append(";");
		// 发音人资源
		tempBuffer.append(ResourceUtil.generateResourcePath(this,
				RESOURCE_TYPE.assets, "tts/" + MainActivity.voicerLocal
						+ ".jet"));
		return tempBuffer.toString();
	}

	// 参数设置
	public void setParam() {
		String lag = mSharedPreferences.getString(
				"understander_language_preference", "mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, lag);
		}
		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("understander_vadbos_preference",
						"4000"));

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("understander_vadeos_preference",
						"1500"));

		// 设置标点符号，默认：1（有标点）
		mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("understander_punc_preference",
						"1"));

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/sud.wav");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时释放连接
		mSpeechUnderstander.cancel();
		mSpeechUnderstander.destroy();
		mTts.stopSpeaking();
		// 退出时释放连接
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
				// 补间动画
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

	// 电视台节目单列表
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
		sa.setRepeatCount(-1);// 设置循环
		AlphaAnimation aniAlp = new AlphaAnimation(1, 0.1f);
		aniAlp.setRepeatCount(-1);// 设置循环
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
			if (list.get(i).get("content").equals("您可以这样说：")) {
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

	// 双击返回事件
	long touchTime = 0;
	long waitTime = 2000;

	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
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
