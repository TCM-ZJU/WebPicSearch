package pic.search;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.regex.*;

public class Indexer {

	static File indexDir = new File("C:\\apache-tomcat-6.0.14\\webapps\\DartSearch\\Index");
	static File dataDir = new File("C:\\apache-tomcat-6.0.14\\webapps\\DartSearch\\File_Pic");
	static String picDir = "http://10.214.33.123/DartSearch/Picture/";
	
	public static int createIndex() {
		int numIndexed = 0;
		try {
			IndexWriter writer = new IndexWriter(indexDir, new StandardAnalyzer(), true, MaxFieldLength.LIMITED);
			writer.setUseCompoundFile(false);
			indexDirectory(writer, dataDir);
			numIndexed = writer.numDocs();
			writer.optimize();
			writer.close();
			
		}
		catch (Exception e) {
			System.err.println(e);
		}
		return numIndexed;
	}
	
	public static void indexDirectory(IndexWriter writer, File dir) throws IOException {
		File[] files = dir.listFiles();
		for (File f: files) {
			if (f.isDirectory()) {
				indexDirectory(writer, f);
			}
			else {
				indexFile(writer, f);
			}
		}
	}
	
	public static void indexFile(IndexWriter writer, File f) throws IOException {
		if (f.isHidden() || !f.exists() || !f.canRead()) {
			return;
		}
		System.out.println("Indexing " + f.getCanonicalPath());
		Document doc = new Document();
		/*if (f.getName().endsWith(".txt")) {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line, url;
			url = "";
			while ((line = in.readLine()) != null)
				url += line;
			doc.add(new Field("path", url, Field.Store.YES, Field.Index.NO));
			doc.add(new Field("tag", getChineseTag(f), Field.Store.YES, Field.Index.ANALYZED));
		}*/
		
		if (f.getName().endsWith(".jpg")) {
		//	doc.add(new Field("content", new FileReader(f), Field.Store.YES, Field.Index.NO));
		//    doc.add(new Field("path", f.getCanonicalPath(), Field.Store.YES, Field.Index.NO));	
		//	doc.add(new Field("path", f.getCanonicalPath(), Field.Store.YES, Field.Index.NO));	
			String name = f.getName();
			name = name.substring(name.indexOf("_") + 1);
			doc.add(new Field("path", picDir + name, Field.Store.YES, Field.Index.NO));	
			doc.add(new Field("tag", getChineseTag(f), Field.Store.YES, Field.Index.ANALYZED));
		}
		writer.addDocument(doc);
	}
	
	private static String getChineseTag(File f) {
		String tag = "";
		String filename = f.getName();
		Pattern p = Pattern.compile("([\u4e00-\u9fa5]+)");
		Matcher m = p.matcher(filename);
		if (m.find())
			tag = m.group(1);
		return tag;
	}
	
	public static ArrayList<Picture> search(String q) { 
		ArrayList<Picture> result = new ArrayList<Picture>();
		try {
			TopDocCollector collector = new TopDocCollector(100);
			Directory fsDir = FSDirectory.getDirectory(indexDir);
	        IndexSearcher is = new IndexSearcher(fsDir);
	        QueryParser parser = new QueryParser("tag", new StandardAnalyzer());
	        Query query = parser.parse(q);
	     //   long start = new Date().getTime();
	        is.search(query, collector);
	    //    long end = new Date().getTime();
	     //  long dur = end - start;
	        ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    //    System.err.println("Found " + hits.length + " document(s) in " + dur + " milliseconds");
	        for (ScoreDoc hit: hits){
	            Document doc = is.doc(hit.doc);
	            if (doc.get("path") != null)
	            {
	            	String path = doc.get("path");
	            	String tag = doc.get("tag");
	            //	System.out.println(tag + " " + filename);
	            	result.add(new Picture(tag, path));
	            }
	        }
		}
		catch (Exception e) {
			System.err.println(e);
		}
		return result;
	}
	
	public static void main(String[] args) {
		int num = Indexer.createIndex();
		System.out.println(num);
	//	search("É½é«");
	//	File f = new File("File_Pic\\×Øéµ×Ó_1269.jpg");
	//	System.out.println(getChineseTag(f));
	}
	
}
