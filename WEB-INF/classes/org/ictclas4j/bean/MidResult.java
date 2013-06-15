package org.ictclas4j.bean;

import java.util.ArrayList;

/**
 * ��¼�ִ�ʱ�������м���
 * 
 * @author sinboy
 * @since 2007.5.24
 */
public class MidResult {
	private int index;// ���ӵ����б��

	private String source;// Դ����

	private ArrayList<Atom> atoms;// ԭ�ӷִʵĽ��

	private ArrayList<SegNode> segGraph;// �ִ�ͼ��

	private ArrayList<SegNode> biSegGraph;// ����ִ�ͼ��

	private ArrayList<ArrayList<Integer>> bipath;// ����ִ�·��
 

	private ArrayList<String> firstResult;// ���ηִʽ��

	private ArrayList<SegNode> optSegGraph;// �Ż���ķִ�ͼ��

	private ArrayList<SegNode> optBiSegGraph;// �Ż���Ķ���ִ�ͼ��

	private ArrayList<ArrayList<Integer>> optBipath;// �Ż���Ķ���ִ�·��

	private ArrayList<String> optResult;// �Ż���ķִʽ��
 

	public void setIndex(int index) {
		this.index = index;
	}

	public void setAtoms(ArrayList<Atom> atoms) {
		this.atoms = atoms;
	}

	public void setOptBiSegGraph(ArrayList<SegNode> biOptSegGraph) {
		this.optBiSegGraph = biOptSegGraph;
	}

	public void setBiSegGraph(ArrayList<SegNode> biSegGraph) {
		this.biSegGraph = biSegGraph;
	}
 

	public void setOptSegGraph(ArrayList<SegNode> optSegGraph) {
		this.optSegGraph = optSegGraph;
	}  

	public void setSegGraph(ArrayList<SegNode> segGraph) {
		this.segGraph = segGraph;
	}

	public void setBipath(ArrayList<ArrayList<Integer>> bipath) {
		this.bipath = bipath;
	}

	public void setOptBipath(ArrayList<ArrayList<Integer>> optBipath) {
		this.optBipath = optBipath;
	}
 

	public void addFirstResult(String result) {
		if(firstResult==null)
			firstResult=new ArrayList<String>();
		firstResult.add(result);
	}
	
	public void setFirstResult(ArrayList<String> resultList){
		this.firstResult=resultList;
	}
	
	public void addOptResult(String result) {
		if(optResult==null)
			optResult=new ArrayList<String>();
		optResult.add(result);
	}
	
	public void setOptResult(ArrayList<String> resultList){
		this.optResult=resultList;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String toHTML() {
		StringBuffer html = new StringBuffer();
		if (source != null) {
			// ��ʾ�ִ�ǰ��ԭʼ����
			html.append("<p>���о��ӷָ��Ľ����");
			html.append("<table border=\"1\" width=\"100%\">");
			html.append("<tr><td width=\"10%\">��" + index + "��</td>");
			html.append("<td width=\"90%\">" + source + "</td></tr></table>");

			// ��ʾ����ԭ�ӷִʺ�Ľ��
			if (atoms != null) {
				html.append("<p>����ԭ�ӷִʺ�Ľ����");
				html.append("<table border=\"1\" width=\"100%\">");
				html.append("<tr>");
				html.append("<td width=\"10%\" bgcolor=\"#99CCFF\">���</td>");
				html.append("<td width=\"40%\" bgcolor=\"#99CCFF\">ԭ��</td>");
				html.append("<td width=\"25%\" bgcolor=\"#99CCFF\">����(�ֽ�)</td>");
				html.append("<td width=\"25%\" bgcolor=\"#99CCFF\">pos</td>");
				html.append("</tr>");
				for (int i = 0; i < atoms.size(); i++) {
					Atom atom = atoms.get(i);
					html.append("<tr>");
					html.append("<td width=\"10%\">" + i + "</td>");
					html.append("<td width=\"40%\">" + atom.getWord() + "</td>");
					html.append("<td width=\"25%\">" + atom.getLen() + "</td>");
					html.append("<td width=\"25%\">" + atom.getPos() + "</td>");
					html.append("</tr>");
				}
				html.append("</table>");
			}

			// ��ʾ�������ɵķִ�ͼ��
			if (segGraph != null && segGraph.size() > 0) {
				html.append("<p>�������ɵķִ�ͼ��");
				html.append("<table border=\"1\" width=\"100%\">");
				html.append("<tr>");
				html.append("<td width=\"5%\"></td>");
				SegNode last = segGraph.get(segGraph.size() - 1);
				int width = last.getCol();
				int height = last.getRow();

				for (int i = 1; i <= width; i++)
					html.append("<td width=\"" + (95 / (double) width) + "%\" bgcolor=\"#99CCFF\">" + i + "</td>");
				html.append("</tr>");
				for (int i = 0; i <= height; i++) {
					html.append("<tr>");
					html.append("<td width=\"5%\">" + i + "</td>");
					for (int j = 1; j <= width; j++) {
						boolean flag = false;
						for (SegNode sn : segGraph) {
							if (i == sn.getRow() && j == sn.getCol()) {
								html.append("<td width=\"" + (95 / (double) width) + "%\"><a title=\"pos=" +sn.getPos()+" value="+sn.getValue()+"\">"+ sn.getWord() + "</a></td>");
								flag = true;
								break;
							}
						}
						if (!flag)
							html.append("<td width=\"" + (95 / (double) width) + "%\">&nbsp</td>");
					}
					html.append("</tr>");
				}
				html.append("</table>");
			}

			// ��ʾ�������ɵĶ���ִ�ͼ��
			if (biSegGraph != null && biSegGraph.size() > 0) {
				html.append("<p>�������ɵĶ���ִ�ͼ��");
				html.append("<table border=\"1\" width=\"100%\">");
				html.append("<tr>");
				html.append("<td width=\"5%\"></td>");
				SegNode last = biSegGraph.get(biSegGraph.size() - 1);
				int width = last.getCol();
				int height = last.getRow();

				for (int i = 1; i <= width; i++)
					html.append("<td width=\"" + (95 / (double) width) + "%\" bgcolor=\"#99CCFF\">" + i + "</td>");
				html.append("</tr>");
				for (int i = 0; i <= height; i++) {
					html.append("<tr>");
					html.append("<td width=\"5%\">" + i + "</td>");
					for (int j = 1; j <= width; j++) {
						boolean flag = false;
						for (SegNode sn : biSegGraph) {
							if (i == sn.getRow() && j == sn.getCol()) {
								html.append("<td width=\"" + (95 / (double) width) + "%\"><a title=\"pos=" +sn.getPos()+" value="+sn.getValue()+"\">"+ sn.getWord() + "</a></td>");
								flag = true;
								break;
							}
						}
						if (!flag)
							html.append("<td width=\"" + (95 / (double) width) + "%\">&nbsp</td>");
					}
					html.append("</tr>");
				}
				html.append("</table>");
			}

			// ��ʾ���ɵĶ���ִ�·��
			if (bipath != null && bipath.size() > 0) {
				html.append("<p>�������ɵĶ���ִ�·����");
				html.append("<table border=\"1\" width=\"100%\">");
				html.append("<tr>");
				html.append("<td width=\"10%\" bgcolor=\"#99CCFF\">���</td>");
				html.append("<td width=\"90%\" bgcolor=\"#99CCFF\">����ִ�·��</td>");
				html.append("</tr>");
				for (int i = 0; i < bipath.size(); i++) {
					html.append("<tr>");
					html.append("<td width=\"10%\">" + i + "</td>");
					html.append("<td width=\"90%\">");
					ArrayList<Integer> list = bipath.get(i);
					for (int index : list) {
						html.append(index + "&nbsp");
					}
					html.append("</td></tr>");
				}
				html.append("</table>");
			}
 

			// ��ʾ�������ɵķִʽ��
			if (firstResult != null && firstResult.size() > 0) {
				html.append("<p>�������ɵķִʽ����");
				html.append("<table border=\"1\" width=\"100%\">");
				html.append("<tr>");
				html.append("<td width=\"10%\" bgcolor=\"#99CCFF\">���</td>");
				html.append("<td width=\"90%\" bgcolor=\"#99CCFF\">�ִʽ��</td>");
				html.append("</tr>");

				for (int i=0;i< firstResult.size();i++) {
					html.append("<tr>");
					html.append("<td width=\"10%\">"+i+"</td>"); 
					html.append("<td width=\"90%\" ><font color=\"#FF0000\"><b>"+firstResult.get(i)+"</b></font</td>");
					html.append("</tr>");
				}
				html.append("</table>");
			}
			
			//��ʾ��������������ʶ���Ĵ�����
			if (optSegGraph != null && optSegGraph.size() > 0) {
				html.append("<p>��������������ʶ���ķִ�ͼ��");
				html.append("<table border=\"1\" width=\"100%\">");
				html.append("<tr>");
				html.append("<td width=\"5%\"></td>");
				SegNode last = optSegGraph.get(optSegGraph.size() - 1);
				int width = last.getCol();
				int height = last.getRow();

				for (int i = 1; i <= width; i++)
					html.append("<td width=\"" + (95 / (double) width) + "%\" bgcolor=\"#99CCFF\">" + i + "</td>");
				html.append("</tr>");
				for (int i = 0; i <= height; i++) {
					html.append("<tr>");
					html.append("<td width=\"5%\">" + i + "</td>");
					for (int j = 1; j <= width; j++) {
						boolean flag = false;
						for (SegNode sn : optSegGraph) {
							if (i == sn.getRow() && j == sn.getCol()) {
								html.append("<td width=\"" + (95 / (double) width) + "%\"><a title=\"pos=" +sn.getPos()+" value="+sn.getValue()+"\">"+ sn.getWord() + "</a></td>");
								flag = true;
								break;
							}
						}
						if (!flag)
							html.append("<td width=\"" + (95 / (double) width) + "%\">&nbsp</td>");
					}
					html.append("</tr>");
				}
				html.append("</table>");
			}
			
			//��ʾ����Ż���Ķ���ִ�·�� 
			if (optBiSegGraph != null && optBiSegGraph.size() > 0) {
				html.append("<p>�����Ż���Ķ���ִ�ͼ��");
				html.append("<table border=\"1\" width=\"100%\">");
				html.append("<tr>");
				html.append("<td width=\"5%\"></td>");
				SegNode last = optBiSegGraph.get(optBiSegGraph.size() - 1);
				int width = last.getCol();
				int height = last.getRow();

				for (int i = 1; i <= width; i++)
					html.append("<td width=\"" + (95 / (double) width) + "%\" bgcolor=\"#99CCFF\">" + i + "</td>");
				html.append("</tr>");
				for (int i = 0; i <= height; i++) {
					html.append("<tr>");
					html.append("<td width=\"5%\">" + i + "</td>");
					for (int j = 1; j <= width; j++) {
						boolean flag = false;
						for (SegNode sn : optBiSegGraph) {
							if (i == sn.getRow() && j == sn.getCol()) {
								html.append("<td width=\"" + (95 / (double) width) + "%\"><a title=\"pos=" +sn.getPos()+" value="+sn.getValue()+"\">"+ sn.getWord() + "</a></td>");
								flag = true;
								break;
							}
						}
						if (!flag)
							html.append("<td width=\"" + (95 / (double) width) + "%\">&nbsp</td>");
					}
					html.append("</tr>");
				}
				html.append("</table>");
			}
			
			//�����Ż���Ķ���ִ�·��
			if (optBipath != null && optBipath.size() > 0) {
				html.append("<p>�����Ż���Ķ���ִ�·����");
				html.append("<table border=\"1\" width=\"100%\">");
				html.append("<tr>");
				html.append("<td width=\"10%\" bgcolor=\"#99CCFF\">���</td>");
				html.append("<td width=\"90%\" bgcolor=\"#99CCFF\">����ִ�·��</td>");
				html.append("</tr>");
				for (int i = 0; i < optBipath.size(); i++) {
					html.append("<tr>");
					html.append("<td width=\"10%\">" + i + "</td>");
					html.append("<td width=\"90%\">");
					ArrayList<Integer> list = optBipath.get(i);
					for (int index : list) {
						html.append(index + "&nbsp");
					}
					html.append("</td></tr>");
				}
				html.append("</table>");
			}
			//��ʾ�����Ż���ķִʽ��
			if (optResult != null && optResult.size() > 0) {
				html.append("<p>�����Ż���ķִʽ����");
				html.append("<table border=\"1\" width=\"100%\">");
				html.append("<tr>");
				html.append("<td width=\"10%\" bgcolor=\"#99CCFF\">���</td>");
				html.append("<td width=\"90%\" bgcolor=\"#99CCFF\">�ִʽ��</td>");
				html.append("</tr>");

				for (int i=0;i< optResult.size();i++) {
					html.append("<tr>");
					html.append("<td width=\"10%\">"+i+"</td>"); 
					html.append("<td width=\"90%\" ><font color=\"#FF0000\"><b>"+optResult.get(i)+"</b></font</td>");
					html.append("</tr>");
				}
				html.append("</table>");
			}
			
			//��ʾ���յķִʽ��
		}
		return html == null ? null : html.toString();
	}
}
