package com.voice.test;

import java.util.ArrayList;



public class Sentence {
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
	 * �ܵ÷�
	 */
	public float total_score;
	/**
	 * ʱ������λ��֡��ÿ֡�൱��10ms����cn��
	 */
	public int time_len;
	/**
	 * ���ӵ�������en��
	 */
	public int index;
	/**
	 * ��������en��
	 */
	public int word_count;
	/**
	 * sentence������word
	 */
	public ArrayList<Word> words;
}
