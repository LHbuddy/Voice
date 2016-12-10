package com.voice.test;

import java.util.ArrayList;



public class Sentence {
	/**
	 * 开始帧位置，每帧相当于10ms
	 */
	public int beg_pos;
	/**
	 * 结束帧位置
	 */
	public int end_pos;
	/**
	 * 句子内容
	 */
	public String content;
	/**
	 * 总得分
	 */
	public float total_score;
	/**
	 * 时长（单位：帧，每帧相当于10ms）（cn）
	 */
	public int time_len;
	/**
	 * 句子的索引（en）
	 */
	public int index;
	/**
	 * 单词数（en）
	 */
	public int word_count;
	/**
	 * sentence包括的word
	 */
	public ArrayList<Word> words;
}
