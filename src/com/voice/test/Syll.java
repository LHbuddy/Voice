package com.voice.test;

import java.util.ArrayList;



public class Syll {
	/**
	 * ��ʼ֡λ�ã�ÿ֡�൱��10ms
	 */
	public int beg_pos;
	/**
	 * ����֡λ��
	 */
	public int end_pos;
	/**
	 * ��������
	 */
	public String content;
	/**
	 * ƴ����cn�������ִ���������5��ʾ��������fen1
	 */
	public String symbol;
	/**
	 * ��©����Ϣ��0����ȷ����16��©������32����������64���ض�����128���滻��
	 */
	public int dp_message;
	/**
	 * ʱ������λ��֡��ÿ֡�൱��10ms����cn��
	 */
	public int time_len;
	/**
	 * Syll����������
	 */
	public ArrayList<Phone> phones;
	
	/**
	 * ��ȡ���ڵı�׼���꣨en��
	 * 
	 * @return ��׼����
	 */
	public String getStdSymbol() {
		String stdSymbol = "";
		String[] symbols = content.split(" ");
		
		for (int i = 0; i < symbols.length; i++) {
			stdSymbol += Phone.getStdSymbol(symbols[i]);
		}
		
		return stdSymbol;
	}
}