package com.voice.tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinChange {
	/**  
	    * ��ȡ���ִ�ƴ������ĸ��Ӣ���ַ�����  
	    *  
	    * @param chinese ���ִ�  
	    * @return ����ƴ������ĸ  
	    */   
	   public static String FirstSpell(String chinese) {   
	       StringBuffer pybf = new StringBuffer();   
	       char[] arr = chinese.toCharArray();   
	       HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();   
	       defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);   
	       defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);   
	       for (int i = 0; i < arr.length; i++) {   
	               if (arr[i] > 128) {   
	                       try {   
	                               String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);   
	                               if (_t != null) {   
	                                       pybf.append(_t[0].charAt(0));   
	                               }   
	                       } catch (BadHanyuPinyinOutputFormatCombination e) {   
	                               e.printStackTrace();   
	                       }   
	               } else {   
	                       pybf.append(arr[i]);   
	               }   
	       }   
	       return pybf.toString().replaceAll("\\W", "").trim().toUpperCase();   
	   }   
	   /**  
	    * ��ȡ���ִ�ƴ����Ӣ���ַ�����  
	    *  
	    * @param chinese ���ִ�  
	    * @return ����ƴ��  
	    */  
	   public static String AllSpell(String chinese) {   
	           StringBuffer pybf = new StringBuffer();   
	           char[] arr = chinese.toCharArray();   
	           HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();   
	           defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);   
	           defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);   
	           for (int i = 0; i < arr.length; i++) {   
	                   if (arr[i] > 128) {   
	                           try {   
	                                   pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);   
	                           } catch (BadHanyuPinyinOutputFormatCombination e) {   
	                                   e.printStackTrace();   
	                           }   
	                   } else {   
	                           pybf.append(arr[i]);   
	                   }   
	           }   
	           return pybf.toString();   
	   }   
	  
}
