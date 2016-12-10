package com.voice.tools;

import com.voice.act.MainActivity;

public class SearchWeather {

	private String mCity = null, mSourceName = null, mdatetime = null,
			mdateorig = null;
	private String[] mWeatherDate = null, mWeather = null, mTempRange = null,
			mAirQuality = null, mWind = null, mHumidity = null,
			mWindLevel = null;
	private MainActivity mActivity = null;

	public SearchWeather(String datetime, String dateorig, String city,
			String sourceName, String[] weatherDate, String[] weather,
			String[] tempRange, String[] airQuality, String[] wind,
			String[] humidity, String[] windLevel, MainActivity activity) {
		mCity = city;
		mSourceName = sourceName;
		mWeatherDate = weatherDate;
		mWeather = weather;
		mTempRange = tempRange;
		mAirQuality = airQuality;
		mWind = wind;
		mHumidity = humidity;
		mWindLevel = windLevel;
		mActivity = activity;
		mdatetime = datetime;
		mdateorig = dateorig;
	}

	public void start() {
		if (mCity.equals("CURRENT_CITY")) {
			mActivity.addlist("��˵����Ҫ��ѯ�ĳ���", "0");
		} else {
			if (mdateorig.equals("����")) {
				mActivity.addlist("������" + mCity + "�����������������", "3");
				for (int i = 0; i < 6; i++) {
					mActivity.addlist(mWeatherDate[i] + " " + mWeather[i]
							+ " " + mTempRange[i] + " " + mAirQuality[i]
							+ " " + mWind[i], "0");
				}
			} else {
				mActivity.addlist("������" + mCity + mdateorig + "�����������", "0");
				if (mdateorig.equals("����")) {
					mActivity.addlist(mWeatherDate[0] + " " + mWeather[0] + " "
							+ mTempRange[0] + " " + mAirQuality[0] + " "
							+ mWind[0], "0");
				} else if (mdateorig.equals("����")) {
					mActivity.addlist(mWeatherDate[1] + " " + mWeather[1] + " "
							+ mTempRange[1] + " " + mAirQuality[1] + " "
							+ mWind[1], "0");
				} else if (mdateorig.equals("����")) {
					mActivity.addlist(mWeatherDate[2] + " " + mWeather[2] + " "
							+ mTempRange[2] + " " + mAirQuality[2] + " "
							+ mWind[2], "0");
				} else if (mdateorig.equals("�����")) {
					mActivity.addlist(mWeatherDate[3] + " " + mWeather[3] + " "
							+ mTempRange[3] + " " + mAirQuality[3] + " "
							+ mWind[3], "0");
				} else {
					for (int i = 0; i < 6; i++) {
						mActivity.addlist(mWeatherDate[i] + " " + mWeather[i]
								+ " " + mTempRange[i] + " " + mAirQuality[i]
								+ " " + mWind[i], "3");
					}
				}
			}
		}
	}

}
