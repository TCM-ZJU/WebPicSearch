package org.ictclas4j.segment;

import java.util.ArrayList;


import org.ictclas4j.bean.Sentence;
import org.ictclas4j.utility.GFString;
import org.ictclas4j.utility.Utility;


public class SentenceSeg {
	private String src;
	private ArrayList<Sentence> sens;
	
	public SentenceSeg(String src){
		this.src=src;
		sens=split();
	}
	/**
	 * ���о��ӷָ�
	 * 
	 * @param src
	 * @return
	 */
	private ArrayList<Sentence> split( ) {
		ArrayList<Sentence> result = null;

		if (src != null) {
			result = new ArrayList<Sentence>();
			String s1 = Utility.SENTENCE_BEGIN;
			String[] ss = GFString.atomSplit(src);

			for (int i = 0; i < ss.length; i++) {
				// ����Ƿָ���������س�����/���ŵ�
				if (Utility.SEPERATOR_C_SENTENCE.indexOf(ss[i]) != -1
						|| Utility.SEPERATOR_LINK.indexOf(ss[i]) != -1
						|| Utility.SEPERATOR_C_SUB_SENTENCE.indexOf(ss[i]) != -1
						|| Utility.SEPERATOR_E_SUB_SENTENCE.indexOf(ss[i]) != -1) {
					// ������ǻس����кͿո�
					if (Utility.SEPERATOR_LINK.indexOf(ss[i]) == -1)
						s1 += ss[i];
					// �Ͼ�
					if (s1.length() > 0 && !Utility.SENTENCE_BEGIN.equals(s1)) {
						if (Utility.SEPERATOR_C_SUB_SENTENCE.indexOf(ss[i]) == -1
								&& Utility.SEPERATOR_E_SUB_SENTENCE
										.indexOf(ss[i]) == -1)
							s1 += Utility.SENTENCE_END;

						result.add(new Sentence(s1, true));
						s1 = "";
					}

					// �ǻس����з���ո�����Ҫ���з�������
					if (Utility.SEPERATOR_LINK.indexOf(ss[i]) != -1) {
					//	result.add(new Sentence(ss[i]));
						s1 = Utility.SENTENCE_BEGIN;

					} else if (Utility.SEPERATOR_C_SENTENCE.indexOf(ss[i]) != -1
							|| Utility.SEPERATOR_E_SENTENCE.indexOf(ss[i]) != -1)
						s1 = Utility.SENTENCE_BEGIN;
					else s1 = Utility.SENTENCE_BEGIN;
//						s1 = ss[i];

				} else
					s1 += ss[i];
			}

			if (s1.length() > 0 && !Utility.SENTENCE_BEGIN.equals(s1)) {
				s1 += Utility.SENTENCE_END;
				result.add(new Sentence(s1, true));
			}
		}
		return result;
	}
	public ArrayList<Sentence> getSens() {
		return sens;
	}
	
	
}
