package org.ictclas4j.segment;

import java.util.ArrayList;


import org.ictclas4j.bean.SegNode;
import org.ictclas4j.utility.NumUtil;
import org.ictclas4j.utility.POSTag;
import org.ictclas4j.utility.Utility;


/**
 * �ִʵ���
 * 
 * @author sinboy
 * @since 2007.6.1
 */
public class AdjustSeg {
	/**
	 * �Գ��ηִʽ�����е�������Ҫ�Ƕ�ʱ�䡢���ڡ����ֵȽ��кϲ�����
	 * 
	 * @return
	 */
	public static ArrayList<SegNode> firstAdjust(ArrayList<SegNode> sgs) {

		ArrayList<SegNode> wordResult = null;
		int index = 0;
		int j = 0;
		int pos = 0;

		if (sgs != null) {
			wordResult = new ArrayList<SegNode>();

			for (int i = 0; i < sgs.size(); i++, index++) {
				SegNode sn = sgs.get(i);
				String srcWord = null;
				String curWord = sn.getSrcWord();
				SegNode newsn = new SegNode();
				pos = sn.getPos();

				boolean isNum = false;
				if ((Utility.isAllNum(curWord) || Utility.isAllChineseNum(curWord))) {
					isNum = true;
					for (j = i + 1; j < sgs.size() - 1; j++) {
						String temp = sgs.get(j).getSrcWord();
						// ������ڵļ����ַ��������֣�������ǽ��кϲ�
						if (Utility.isAllNum(temp) || Utility.isAllChineseNum(temp)) {
							isNum = true;
							index = j;
							curWord += temp;
						} else
							break;

					}
				}

				// ����������֣����ǿ��Ժ�ǰ������ֹ������ڣ�����������ǰһ���ڵ�
				// ����ֱ�ӰѸýڵ���ӵ��������
				if (!isNum) {
					SegNode prevsn = null;
					if (wordResult.size() > 0)
						prevsn = wordResult.get(wordResult.size() - 1);
					if (Utility.isDelimiter(curWord)) {
						// �����һ���ַ�Ҳ�Ƿָ���������кϲ�
						if (prevsn != null && Utility.isDelimiter(prevsn.getWord())) {
							prevsn.setCol(sn.getCol());
							prevsn.appendWord(curWord);
							continue;
						} else
							// 'w'*256;Set the POS with 'w'
							pos = POSTag.PUNC;
					} else if (curWord.length() == 1 && "����ʱ����".indexOf(curWord) != -1 || "�·�".equals(curWord)) {
						if (prevsn != null && prevsn.getPos() == -POSTag.NUM) {
							prevsn.setCol(sn.getCol());
							prevsn.setWord(Utility.UNKNOWN_TIME);
							prevsn.setSrcWord(prevsn.getSrcWord() + curWord);
							prevsn.setPos(-POSTag.TIME);
							continue;
						}
					} else if ("��".equals(curWord)) {
						if (prevsn != null && Utility.isYearTime(prevsn.getSrcWord())) {
							prevsn.setCol(sn.getCol());
							prevsn.setWord(Utility.UNKNOWN_TIME);
							prevsn.setSrcWord(prevsn.getSrcWord() + curWord);
							prevsn.setPos(-POSTag.TIME);
							continue;
						}
					}
				} else {

					// �����ǰ�ַ����������������ַ���ɵĶ�����һ�����֣��������Ӧ��ԭʼ�ڵ���ϢҲ��ӵ��������
					if (NumUtil.isNumStrNotNum(curWord)) {
						for (int k = i; k <= index; k++)
							wordResult.add(sgs.get(k));
						continue;
					}
					// ��һ������
					else {
						// �����������������ʽ��
						// 3-4�£�����ǰԪ����һ�����֣�ǰһ���Ƿָ�����ǰǰһ��Ҳ�����֣���ǰԪ��Ӧ��������
						boolean flag = false;
						int size = wordResult.size();
						if (wordResult.size() > 1) {
							SegNode prevPrevsn = wordResult.get(size - 2);
							SegNode prevsn = wordResult.get(size - 1);
							if (NumUtil.isNumDelimiter(prevPrevsn.getPos(), prevsn.getWord())) {
								pos = POSTag.NUM;
								flag = true;
							}
						}
						if (!flag) {
							if (curWord.indexOf("��") == curWord.length() - 1) {
								pos = -POSTag.TIME;
								srcWord = curWord;
								curWord = Utility.UNKNOWN_TIME;
							} else if (curWord.length() > 1) {
								String last = curWord.substring(curWord.length() - 1);
								// �����ǰ�ʵ����һ���ַ��������¼����������˵������һ�����֡��������һ���ַ�����һ����㣬�������������
								if ("�á�����./".indexOf(last) == -1) {
									pos = -POSTag.NUM;
									srcWord = curWord;
									curWord = Utility.UNKNOWN_NUM;

								} else {
									if (".".equals(last) || "/".equals(last)) {
										pos = -POSTag.NUM;
										srcWord = curWord.substring(0, curWord.length() - 1);
										curWord = Utility.UNKNOWN_NUM;
										index--;
									} else if (curWord.length() > 2) {
										pos = -POSTag.NUM;
										srcWord = curWord.substring(0, curWord.length() - 2);
										curWord = Utility.UNKNOWN_NUM;
										index -= 2;
									}
								}
							}
						}
					}

				}

				int col = index > i ? sgs.get(index).getCol() : sn.getCol();
				newsn.setCol(col);
				newsn.setRow(sn.getRow());
				newsn.setWord(curWord);
				newsn.setPos(pos);
				newsn.setValue(sn.getValue());
				if (srcWord != null)
					newsn.setSrcWord(srcWord);
				wordResult.add(newsn);
				i = index;
			}
		}

		return wordResult;
	}

	/**
	 * �Էִʽ�������յĵ�������Ҫ�������Ĳ�ֻ��ص��ʵĺϲ�
	 * 
	 * @param optSegPath
	 * @param personTagger
	 * @param placeTagger
	 * @return
	 */
	public static ArrayList<SegNode> finaAdjust(ArrayList<SegNode> optSegPath, PosTagger personTagger,
			PosTagger placeTagger) {
		ArrayList<SegNode> result = null;
		SegNode wr = null;

		if (optSegPath != null && optSegPath.size() > 0 && personTagger != null && placeTagger != null) {

			result = new ArrayList<SegNode>();
			for (int i = 0; i < optSegPath.size(); i++) {
				boolean isBeProcess = false;
				wr = optSegPath.get(i);
				// if (wr.getPos() == POSTag.NOUN_PERSON
				// && (pname = Utility.chineseNameSplit(wr.getSrcWord(),
				// personTagger)) != null
				// && !"Ҷ����".equals(wr.getSrcWord())) {
				// if (pname.getFirstName() != null) {
				// SegNode wr2 = new SegNode();
				// wr2.setWord(pname.getFirstName());
				// wr2.setPos(POSTag.NOUN_PERSON);
				// result.add(wr2);
				// }
				//
				// if (pname.getMidName() != null) {
				// SegNode wr2 = new SegNode();
				// wr2.setWord(pname.getMidName());
				// wr2.setPos(POSTag.NOUN_PERSON);
				// result.add(wr2);
				// }
				//
				// if (pname.getLastName() != null) {
				// SegNode wr2 = new SegNode();
				// wr2.setWord(pname.getLastName());
				// wr2.setPos(POSTag.NOUN_PERSON);
				// result.add(wr2);
				// }
				//
				// isBeProcess = true;
				// }
				// Rule2 for overlap words ABB һ�ζΡ�һƬƬ
				if (wr.getPos() == POSTag.NUM && i + 2 < optSegPath.size() && optSegPath.get(i + 1).getLen() == 2
						&& optSegPath.get(i + 1).getSrcWord().equals(optSegPath.get(i + 2).getSrcWord())) {
					SegNode wr2 = new SegNode();
					wr2.setWord(wr.getSrcWord() + optSegPath.get(i + 1).getSrcWord()
							+ optSegPath.get(i + 2).getSrcWord());
					wr2.setPos(POSTag.NUM);
					result.add(wr2);
					i += 2;
					isBeProcess = true;
				}
				// Rule3 for overlap words AA
				else if (wr.getLen() == 2 && i + 1 < optSegPath.size()
						&& wr.getSrcWord().equals(optSegPath.get(i + 1).getSrcWord())) {
					SegNode wr2 = new SegNode();
					wr2.setWord(wr.getSrcWord() + optSegPath.get(i + 1).getSrcWord());
					wr2.setPos(POSTag.ADJ);
					if (wr.getPos() == POSTag.VERB || optSegPath.get(i + 1).getPos() == POSTag.VERB)// 30208='v'8256
						wr2.setPos(POSTag.VERB);

					if (wr.getPos() == POSTag.NOUN || optSegPath.get(i + 1).getPos() == POSTag.NOUN)// 30208='v'8256
						wr2.setPos(POSTag.NOUN);

					i += 1;
					if (optSegPath.get(i + 1).getLen() == 2) {// AAB:ϴ/ϴ/����������
						if ((wr2.getPos() == POSTag.VERB && optSegPath.get(i + 1).getPos() == POSTag.NOUN)
								|| (wr2.getPos() == POSTag.ADJ && optSegPath.get(i + 1).getPos() == POSTag.ADJ)) {
							wr2.setWord(wr2.getWord() + optSegPath.get(i + 1).getSrcWord());
							i += 1;
						}
					}
					isBeProcess = true;
					result.add(wr2);
				}
				// Rule 4: AAB ϴ/ϴ��
				else if (wr.getLen() == 2 && i + 1 < optSegPath.size()
						&& (wr.getPos() == POSTag.VERB || wr.getPos() == POSTag.ADJ)
						&& optSegPath.get(i + 1).getLen() == 4
						&& optSegPath.get(i + 1).getSrcWord().indexOf(wr.getSrcWord()) == 0) {
					SegNode wr2 = new SegNode();
					wr2.setWord(wr.getWord() + optSegPath.get(i + 1).getSrcWord());
					wr2.setPos(POSTag.ADJ); // 24832=='a'*256

					if (wr.getPos() == POSTag.VERB || optSegPath.get(i + 1).getPos() == POSTag.VERB)// 30208='v'8256
						wr2.setPos(POSTag.VERB);

					i += 1;
					isBeProcess = true;
					result.add(wr2);
				} else if (wr.getPos() / 256 == 'u' && wr.getPos() % 256 != 0)// uj,ud,uv,uz,ul,ug->u
					wr.setPos('u' * 256);
				// AABB,��������
				else if (wr.getLen() == 2 && i + 2 < optSegPath.size() && optSegPath.get(i + 1).getLen() == 4
						&& optSegPath.get(i + 1).getWord().indexOf(wr.getWord()) == 0
						&& optSegPath.get(i + 1).getWord().indexOf(optSegPath.get(i + 2).getWord()) == 0) {
					SegNode wr2 = new SegNode();
					wr2.setWord(wr.getWord() + optSegPath.get(i + 1).getWord() + optSegPath.get(i + 2).getWord());
					wr2.setPos(optSegPath.get(i + 1).getPos());
					i += 2;
					isBeProcess = true;
					result.add(wr2);
				}
				// 28275=='n'*256+'s' ����+X
				else if (wr.getPos() == POSTag.NOUN_SPACE && i + 1 < optSegPath.size())// PostFix
				{
					SegNode next = optSegPath.get(i + 1);
					if (placeTagger.getUnknownDict().isExist(next.getSrcWord(), 4)) {
						SegNode wr2 = new SegNode();
						wr2.setWord(wr.getSrcWord() + next.getSrcWord());
						wr2.setPos(POSTag.NOUN_SPACE);
						i += 1;
						isBeProcess = true;
						result.add(wr2);
					} else if ("��".equals(next.getSrcWord())) {
						SegNode wr2 = new SegNode();
						wr2.setWord(wr.getSrcWord() + next.getSrcWord());
						wr2.setPos(POSTag.NOUN_ORG);
						i += 1;
						isBeProcess = true;
						result.add(wr2);
					} else if (optSegPath.get(i + 1).getLen() == 2 && "�����ֱ�".indexOf(next.getSrcWord()) != -1) {
						SegNode wr2 = new SegNode();
						wr2.setWord(wr.getSrcWord() + next.getSrcWord());
						wr2.setPos(POSTag.NOUN_ZHUAN);
						i += 1;
						isBeProcess = true;
						result.add(wr2);
					} else if ("��".equals(next.getSrcWord())) {
						SegNode wr2 = new SegNode();
						wr2.setWord(wr.getSrcWord() + next.getSrcWord());
						wr2.setPos(POSTag.NOUN);
						i += 1;
						isBeProcess = true;
						result.add(wr2);
					}
				} else if (wr.getPos() == POSTag.VERB  || wr.getPos() == POSTag.VERB_NOUN  ||wr.getPos() == POSTag.NOUN)// v
				{
					if (i + 1 < optSegPath.size() && "Ա".equals(optSegPath.get(i + 1).getSrcWord())) {
						SegNode wr2 = new SegNode();
						wr2.setWord(wr.getSrcWord() + optSegPath.get(i + 1).getSrcWord());
						wr2.setPos(POSTag.NOUN);
						i += 1;
						isBeProcess = true;
						result.add(wr2);
					}
				}
				// www/nx ./w sina/nx;
				// �ţɣ�/nx -������/m
				// �ӣȣ�/nx ��/w ������/m
				// 28280=='n'*256+'r'
				// 27904=='m'*256
				else if (wr.getPos() == POSTag.NOUN_LETTER && i + 1 < optSegPath.size()) {
					SegNode wr2 = new SegNode();
					wr2.setWord(wr.getSrcWord());
					wr2.setPos(POSTag.NOUN_LETTER);
					while (true) {
						SegNode nextSN = optSegPath.get(i + 1);
						if (nextSN.getPos() == POSTag.NOUN_LETTER || ".��-��".indexOf(nextSN.getSrcWord()) != -1
								|| (nextSN.getPos() == POSTag.NUM && Utility.isAllNum(nextSN.getSrcWord()))) {
							wr2.setWord(wr2.getSrcWord() + nextSN.getSrcWord());
							i++;
						} else
							break;
					}
					isBeProcess = true;
					result.add(wr2);
				}
				// If not processed,that's mean: not need to adjust;
				// just copy to the final result
				if (!isBeProcess) {
					SegNode wr2 = new SegNode();
					wr2.setWord(wr.getSrcWord());
					wr2.setPos(wr.getPos());
					result.add(wr2);

				}
			}
		}

		return result;
	}

}
