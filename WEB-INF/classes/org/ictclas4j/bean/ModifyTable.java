package org.ictclas4j.bean;

import java.util.ArrayList;


public class ModifyTable {
	// ͬһ��ͷ��������Ŀ���޸ı���
	private int count;

	// ��ԭ�ֵ����ɾ���Ĵ�������Ŀ
	private int delete;

	private  ArrayList<WordItem> words;
	
	public ModifyTable() {
		count = 0;
		delete = 0;
		words = new ArrayList<WordItem>();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getDelete() {
		return delete;
	}

	public void setDelete(int delete) {
		this.delete = delete;
	}

	public ArrayList<WordItem> getWords() {
		return words;
	}

	public void setWords(ArrayList<WordItem> words) {
		this.words = words;
	}
 
	 

}
