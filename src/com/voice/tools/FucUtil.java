package com.voice.tools;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;

/**
 * 功能性函数扩展类
 */
public class FucUtil {
	/**
	 * 读取asset目录下文件�??
	 * @return content
	 */
	public static String readFile(Context mContext,String file,String code)
	{
		int len = 0;
		byte []buf = null;
		String result = "";
		try {
			InputStream in = mContext.getAssets().open(file);			
			len  = in.available();
			buf = new byte[len];
			in.read(buf, 0, len);
			
			result = new String(buf,code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取asset目录下音频文件�??
	 * 
	 * @return 二进制文件数�?
	 */
	public static byte[] readAudioFile(Context context, String filename) {
		try {
			InputStream ins = context.getAssets().open(filename);
			byte[] data = new byte[ins.available()];
			
			ins.read(data);
			ins.close();
			
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
