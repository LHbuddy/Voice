package com.voice.test;

import java.util.ArrayList;



public class ResultFormatUtil {
	/**
	 * Ω´”¢”Ô∆¿≤‚œÍ«È∞¥∏Ò Ω ‰≥ˆ
	 * 
	 * @param sentences
	 * @return ”¢”Ô∆¿≤‚œÍ«È
	 */
	public static String formatDetails_EN(ArrayList<Sentence> sentences) {
		StringBuffer buffer = new StringBuffer();
		if (null == sentences) {
			return buffer.toString();
		}
		
		for (Sentence sentence: sentences ) {
			if ("‘Î“Ù".equals(ResultTranslateUtil.getContent(sentence.content)) 
					|| "æ≤“Ù".equals(ResultTranslateUtil.getContent(sentence.content))) {
				continue;
			}
			
			if (null == sentence.words) {
				continue;
			}
			for (Word word: sentence.words) {
				if ("‘Î“Ù".equals(ResultTranslateUtil.getContent(word.content)) 
						|| "æ≤“Ù".equals(ResultTranslateUtil.getContent(word.content))) {
					continue;
				}
				
				buffer.append("\nµ•¥ [" + ResultTranslateUtil.getContent(word.content) + "] ")
					.append("¿ ∂¡£∫" + ResultTranslateUtil.getDpMessageInfo(word.dp_message))
					.append(" µ√∑÷£∫" + word.total_score);
				if (null == word.sylls) {
					buffer.append("\n");
					continue;
				}
				
				for (Syll syll: word.sylls) {
					buffer.append("\n©∏“ÙΩ⁄[" + ResultTranslateUtil.getContent(syll.getStdSymbol()) + "] ");
					if (null == syll.phones) {
						continue;
					}
					
					for (Phone phone: syll.phones) {
						buffer.append("\n\t©∏“ÙÀÿ[" + ResultTranslateUtil.getContent(phone.getStdSymbol()) + "] ")
							.append(" ¿ ∂¡£∫" + ResultTranslateUtil.getDpMessageInfo(phone.dp_message));
					}
					
				}
				buffer.append("\n");
			}
		}
		
		return buffer.toString();
	}

	/**
	 * Ω´∫∫”Ô∆¿≤‚œÍ«È∞¥∏Ò Ω ‰≥ˆ
	 * 
	 * @param sentences
	 * @return ∫∫”Ô∆¿≤‚œÍ«È
	 */
	public static String formatDetails_CN(ArrayList<Sentence> sentences) {
		StringBuffer buffer = new StringBuffer();
		if (null == sentences) {
			return buffer.toString();
		}
		
		for (Sentence sentence: sentences ) {
			if (null == sentence.words) {
				continue;
			}
			
			for (Word word: sentence.words) {
				buffer.append("\n¥ ”Ô[" + ResultTranslateUtil.getContent(word.content) + "] " + word.symbol + "  ±≥§£∫" + word.time_len);
				if (null == word.sylls) {
					continue;
				}
				
				for (Syll syll: word.sylls) {
					if ("‘Î“Ù".equals(ResultTranslateUtil.getContent(syll.content)) 
							|| "æ≤“Ù".equals(ResultTranslateUtil.getContent(syll.content))) {
						continue;
					}
					
					buffer.append("\n©∏“ÙΩ⁄[" + ResultTranslateUtil.getContent(syll.content) + "] " + syll.symbol + "  ±≥§£∫" + syll.time_len);
					if (null == syll.phones) {
						continue;
					}
					
					for (Phone phone: syll.phones) {
						buffer.append("\n\t©∏“ÙÀÿ[" + ResultTranslateUtil.getContent(phone.content) + "] " + " ±≥§£∫" + phone.time_len)
							.append(" ¿ ∂¡£∫" + ResultTranslateUtil.getDpMessageInfo(phone.dp_message));
					}
					
				}
				buffer.append("\n");
			}
		}
		
		return buffer.toString();
	}
}
