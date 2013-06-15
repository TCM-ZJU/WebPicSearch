package pic.search;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import org.ictclas4j.segment.Segment;
import java.io.*;

public class QuerySeg {
	
	public static Segment seg;
	
	public String query;
	
	public ArrayList<String> words;
	
	public ArrayList<String> words_sorted;
	
//	public DBQuery DBquery;
	
	public HashSet<Picture> resultSet;
	
	public XMLPrinter printer;
	
	public String xmlpath = "C:\\apache-tomcat-6.0.14\\webapps\\DartSearch\\picture.xml";

//	static Logger logger = Logger.getLogger(Wordseg.class);

	public QuerySeg(String queryString) {
		query = queryString;
		words = new ArrayList<String>();
		words_sorted = new ArrayList<String>();
	//	DBquery = new DBQuery();
		resultSet = new HashSet<Picture>();
		printer = new XMLPrinter();
		getwords();
	//	searchPicture();
	}

	private void getwords() // 得到分词结果s
	{
		if (query.indexOf(" ") != -1) {
			String[] split = query.split("[\\s]+");
			for (String s: split)
				words.add(s);
		}
		else {
		//	System.out.println("seg");
			seg = new Segment(1);
			words = seg.split(query);
		}
	/*	
		for (String s: words)
		{
			if (!words_sorted.contains(s))
				words_sorted.add(s);
		}	
		Collections.sort(words_sorted, new LengthComparator());	*/
	}
	
	public ArrayList<Picture> searchPicture() {
		System.out.println("searching...");
		for (String seg: words) {
			ArrayList<Picture> midResult = Indexer.search(seg);
			resultSet.addAll(midResult);
		}
		ArrayList<Picture> list_sorted = new ArrayList<Picture>();
		list_sorted.addAll(resultSet);
		Collections.sort(list_sorted);
		printer.buildXML(xmlpath, list_sorted);
		
		return list_sorted;
	}
	
	public static void main(String[] args) throws IOException {
		boolean requery = true;
	//	while (requery) {
			System.err.print("input query keyword: ");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String query = in.readLine();
			long start = new Date().getTime();
			QuerySeg queryseg = new QuerySeg(query);
			ArrayList<Picture> list = queryseg.searchPicture();
			long end = new Date().getTime();
			System.err.println("Found " + queryseg.resultSet.size() + " document(s) in " + (end - start) + " milliseconds");
			for(Picture p : list)
			{
				System.out.println(p);
			}	
		//	System.err.println("Press y to continue, other to exit...");
		//	query = in.readLine();
		//	if (!query.equalsIgnoreCase("y"))
			//	requery = false;
		
	}
}

class LengthComparator implements Comparator<String> {
	public int compare(String s1, String s2) {
		if (s1.length() > s2.length())
			return -1;
		else return 1;
	}
}
