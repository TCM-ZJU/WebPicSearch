package org.ictclas4j.segment;

import java.util.ArrayList;


import org.ictclas4j.bean.Atom;
import org.ictclas4j.utility.GFString;
import org.ictclas4j.utility.Utility;

/**
 * ԭ�ӷִ�
 * @author sinboy
 * @since 2007.6.1
 */
public class AtomSeg {

	private String str;

	private ArrayList<Atom> atoms;

	public AtomSeg(String src) {
		this.str = src;
		atoms = atomSplit();
	}

	/**
	 * <pre>
	 *                 ԭ�ӷִ�,�ֳ�һ�����Ķ����ַ�,��ʼ��־�ͽ�����־��һ��ԭ�ӵ�λ����.
	 *                 ����:ʼ##ʼ��˵��ȷʵ����ĩ##ĩ--&gt;ʼ##ʼ �� ˵ �� ȷ ʵ �� �� ĩ##ĩ
	 * </pre>
	 * 
	 * @param str
	 *            Դ�ַ���
	 * @return
	 */
	private ArrayList<Atom> atomSplit() {
		ArrayList<Atom> result = null;

		if (str != null && str.length() > 0) {
			String sAtom = "";
			result = new ArrayList<Atom>();
			String[] ss = GFString.atomSplit(str);

			int index = str.indexOf(Utility.SENTENCE_BEGIN);
			if (index == 0) {
				Atom atom = new Atom();
				atom.setWord(Utility.SENTENCE_BEGIN);
				atom.setLen(Utility.SENTENCE_BEGIN.length());
				atom.setPos(Utility.CT_SENTENCE_BEGIN);
				result.add(atom);
				index += Utility.SENTENCE_BEGIN.length();
			}

			if (index == -1)
				index = 0;
			for (int i = index; i < ss.length; i++) {
				if (Utility.SENTENCE_END.equals(str.substring(i))) {
					Atom atom = new Atom();
					atom.setWord(Utility.SENTENCE_END);
					atom.setLen(Utility.SENTENCE_END.length());
					atom.setPos(Utility.CT_SENTENCE_END);
					result.add(atom);
					break;
				}

				String s = ss[i];
				sAtom += s;
				int curType = Utility.charType(s);
				if (".".equals(s)
						&& (i + 1 < ss.length && (Utility.charType(ss[i + 1]) == Utility.CT_NUM || GFString
								.isNumeric(ss[i+1]))))
					curType = Utility.CT_NUM;

				// ����Ǻ��֡��ָ�����
				if (curType == Utility.CT_CHINESE || curType == Utility.CT_INDEX || curType == Utility.CT_DELIMITER
						|| curType == Utility.CT_OTHER) {

					Atom atom = new Atom();
					atom.setWord(s);
					atom.setLen(s.length());
					atom.setPos(curType);
					result.add(atom);
					sAtom = "";
				}
				// ��������֡���ĸ�����ֽڷ��ţ�������ڵ���Щ��Ϊһ��ԭ�ӡ����磺����SHX-123�ͺŵ��ֻ��������е�SHX-123����һ��ԭ��
				else {
					int nextType = 255;// ��һ���ַ�������
					if (i < ss.length - 1)
						nextType = Utility.charType(ss[i + 1]);
					if (nextType != curType || i == ss.length - 1) {
						Atom atom = new Atom();
						atom.setWord(sAtom);
						atom.setLen(sAtom.length());
						atom.setPos(curType);
						result.add(atom);
						sAtom = "";
					}
				}
			}

		}
		return result;
	}

	public ArrayList<Atom> getAtoms() {
		return atoms;
	}

}
