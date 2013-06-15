package org.ictclas4j.segment;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.log4j.Logger;

import org.ictclas4j.bean.Atom;
import org.ictclas4j.bean.Dictionary;
import org.ictclas4j.bean.MidResult;
import org.ictclas4j.bean.SegNode;
import org.ictclas4j.bean.SegResult;
import org.ictclas4j.bean.Sentence;
import org.ictclas4j.utility.DebugUtil;
import org.ictclas4j.utility.POSTag;
import org.ictclas4j.utility.Utility;


public class Segment {
	private Dictionary coreDict;

	private Dictionary bigramDict;

	private PosTagger personTagger;

	private PosTagger transPersonTagger;

	private PosTagger placeTagger;

	private PosTagger lexTagger;

	private int segPathCount = 1;// �ִ�·������Ŀ
	
//	public int CAPACITY=100;
//	public int capacityIncrement=10;
	
	public ArrayList<String> word=new ArrayList<String>();     //���屣����������

//	static Logger logger = Logger.getLogger(Segment.class);

	public Segment(int segPathCount) {
		this.segPathCount = segPathCount;
	//	logger.info("Load coreDict  ...");
	//	coreDict = new Dictionary("data\\coreDictNew.dct", 2);
		coreDict = new Dictionary("data\\coreDict.dct");
	//	logger.info("Load bigramDict ...");
		bigramDict = new Dictionary("data\\bigramDict.dct");

	//	logger.info("Load tagger dict ...");
		personTagger = new PosTagger(Utility.TAG_TYPE.TT_PERSON, "data\\nr", coreDict);
		transPersonTagger = new PosTagger(Utility.TAG_TYPE.TT_TRANS_PERSON, "data\\tr", coreDict);
		placeTagger = new PosTagger(Utility.TAG_TYPE.TT_TRANS_PERSON, "data\\ns", coreDict);
		lexTagger = new PosTagger(Utility.TAG_TYPE.TT_NORMAL, "data\\lexical", coreDict);
	//	logger.info("Load dict is over");
	}

	public ArrayList<String> split(String src) 
	{
		word.clear();
	//	int k=0; //����word���±ꣻ
		SegResult sr = new SegResult(src);// �ִʽ��
		String finalResult = null;

		if (src != null) {
			finalResult = "";
			int index = 0;
			String midResult = null;
			sr.setRawContent(src);
			SentenceSeg ss = new SentenceSeg(src);
			ArrayList<Sentence> sens = ss.getSens();
			
			for (Sentence sen : sens) {
		//		logger.debug(sen);
		//		long start=System.currentTimeMillis();
				MidResult mr = new MidResult();
				mr.setIndex(index++);
				mr.setSource(sen.getContent());
				if (sen.isSeg()) {

					// ԭ�ӷִ�
					AtomSeg as = new AtomSeg(sen.getContent());
					ArrayList<Atom> atoms = as.getAtoms();
					mr.setAtoms(atoms); 
				//	System.err.println("[atom time]:"+(System.currentTimeMillis()-start));
				//	start=System.currentTimeMillis();
					
					// ���ɷִ�ͼ��,�Ƚ��г����ִʣ�Ȼ������Ż��������д��Ա��
					SegGraph segGraph = GraphGenerate.generate(atoms, coreDict);
					mr.setSegGraph(segGraph.getSnList());
					// ���ɶ���ִ�ͼ��
					SegGraph biSegGraph = GraphGenerate.biGenerate(segGraph, coreDict, bigramDict);
					mr.setBiSegGraph(biSegGraph.getSnList());
				//	System.err.println("[graph time]:"+(System.currentTimeMillis()-start));
				//	start=System.currentTimeMillis();
					
					// ��N���·��
					NShortPath nsp = new NShortPath(biSegGraph, segPathCount);
					ArrayList<ArrayList<Integer>> bipath = nsp.getPaths();
					mr.setBipath(bipath);
				//	System.err.println("[NSP time]:"+(System.currentTimeMillis()-start));
				//	start=System.currentTimeMillis();
					
					for (ArrayList<Integer> onePath : bipath) {
						// �õ����ηִ�·��
						ArrayList<SegNode> segPath = getSegPath(segGraph, onePath);
						ArrayList<SegNode> firstPath = AdjustSeg.firstAdjust(segPath);
						String firstResult = outputResult(firstPath);
						mr.addFirstResult(firstResult);
					//	System.err.println("[first time]:"+(System.currentTimeMillis()-start));
					//	start=System.currentTimeMillis();

						// ����δ��½�ʣ����Գ��ηִʽ�������Ż�
						SegGraph optSegGraph = new SegGraph(firstPath);
						ArrayList<SegNode> sns = clone(firstPath);
						personTagger.recognition(optSegGraph, sns);
						transPersonTagger.recognition(optSegGraph, sns);
						placeTagger.recognition(optSegGraph, sns);
						mr.setOptSegGraph(optSegGraph.getSnList());
						
					//	System.err.println("[unknown time]:"+(System.currentTimeMillis()-start));
						
					//	start=System.currentTimeMillis();

						// �����Ż���Ľ�������½������ɶ���ִ�ͼ��
						SegGraph optBiSegGraph = GraphGenerate.biGenerate(optSegGraph, coreDict, bigramDict);
						mr.setOptBiSegGraph(optBiSegGraph.getSnList());

						// ������ȡN�����·��
						NShortPath optNsp = new NShortPath(optBiSegGraph, segPathCount);
						ArrayList<ArrayList<Integer>> optBipath = optNsp.getPaths();
						mr.setOptBipath(optBipath);

						// �����Ż���ķִʽ�������Խ�����д��Ա�Ǻ������Ż���������
						ArrayList<SegNode> adjResult = null;
						for (ArrayList<Integer> optOnePath : optBipath) {
							ArrayList<SegNode> optSegPath = getSegPath(optSegGraph, optOnePath);
							for(int j=0;j<optSegPath.size();j++)
							{
//								System.out.println(optSegPath.get(j).getWord());
								String tempString=optSegPath.get(j).getWord();
//								if(k==CAPACITY)
//								{
//									CAPACITY=CAPACITY+capacityIncrement;
//									word.
//									
//								}
								if(this.isChinese(tempString))
								{
									word.add(tempString);
								}
								else if(this.isEnglish(tempString))
								{
									word.add(tempString);
									
								}
								else if(tempString.compareTo("δ##��")==0)
								{
									word.add(optSegPath.get(j).getSrcWord());
								}
										
							}
							
							lexTagger.recognition(optSegPath);
							String optResult = outputResult(optSegPath);
							mr.addOptResult(optResult);
							adjResult = AdjustSeg.finaAdjust(optSegPath, personTagger, placeTagger);
							String adjrs = outputResult(adjResult);
							
							//�ڴ˴�ӡ
						//	System.err.println("[last time]:"+(System.currentTimeMillis()-start));
						//	start=System.currentTimeMillis();
							if (midResult == null)
								midResult = adjrs;
							break;
						}
					}
					sr.addMidResult(mr);
				} else
					midResult = sen.getContent();
				finalResult += midResult;
				midResult = null;
			}

			sr.setFinalResult(finalResult);
		//	DebugUtil.output2html(sr);
		//	logger.info(finalResult);
		}

		return word;
	}

	private ArrayList<SegNode> clone(ArrayList<SegNode> sns) {
		ArrayList<SegNode> result = null;
		if (sns != null && sns.size() > 0) {
			result = new ArrayList<SegNode>();
			for (SegNode sn : sns)
				result.add(sn.clone());
		}
		return result;
	}

	// ���ݶ���ִ�·�����ɷִ�·��
	private ArrayList<SegNode> getSegPath(SegGraph sg, ArrayList<Integer> bipath) {

		ArrayList<SegNode> path = null;

		if (sg != null && bipath != null) {
			ArrayList<SegNode> sns = sg.getSnList();
			path = new ArrayList<SegNode>();

			for (int index : bipath)
				path.add(sns.get(index));

		}
		return path;
	}

	// ���ݷִ�·�����ɷִʽ��
	private String outputResult(ArrayList<SegNode> wrList) {
		String result = null;
		String temp=null;
		char[] pos = new char[2];
		if (wrList != null && wrList.size() > 0) {
			result = "";
			for (int i = 0; i < wrList.size(); i++) {
				SegNode sn = wrList.get(i);
				if (sn.getPos() != POSTag.SEN_BEGIN && sn.getPos() != POSTag.SEN_END) {
					int tag = Math.abs(sn.getPos());
					pos[0] = (char) (tag / 256);
					pos[1] = (char) (tag % 256);
					temp=""+pos[0];
					if(pos[1]>0)
						temp+=""+pos[1];
					result += sn.getSrcWord() + "/" + temp + " ";
				}
			}
		}

		return result;
	}

	public void setSegPathCount(int segPathCount) {
		this.segPathCount = segPathCount;
	}
	
	public boolean isChinese(String str)     //�ж�����
	{
		Pattern a=Pattern.compile("^[\u4e00-\u9fa5]+$");
		Matcher b=a.matcher(str); 
		return b.matches();
			
	}
	
	public boolean isEnglish(String str)  //�ж�Ӣ��
	{
		Pattern a=Pattern.compile("[\\w   ]*");
		Matcher b=a.matcher(str); 
		return b.matches();
			
		
	}

}
