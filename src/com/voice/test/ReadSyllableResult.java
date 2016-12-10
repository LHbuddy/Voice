package com.voice.test;



public class ReadSyllableResult extends Result {
	public ReadSyllableResult() {
		language = "cn";
		category = "read_syllable";
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[������]\n")
			.append("�������ݣ�" + content + "\n")
			.append("�ʶ�ʱ����" + time_len + "\n")
			.append("�ܷ֣�" + total_score + "\n\n")
			.append("[�ʶ�����]").append(ResultFormatUtil.formatDetails_CN(sentences));
		
		return buffer.toString();
	}
}
