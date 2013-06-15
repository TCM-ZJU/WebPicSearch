package pic.search;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.ictclas4j.bean.Dictionary;

public class AddNewWord {

	Dictionary dic;
	ArrayList<String> addList;
	
	public AddNewWord() {
		dic = new Dictionary("data\\coreDict.dct");
		addList = new ArrayList<String>();
	}
	
	public void setAddList(String filename) {
		BufferedReader in;
		String line;
		try {
			in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				//System.out.println(line);
				addList.add(line);
			}
		}
		catch (IOException e) {
			System.err.println("read file error!");
		}
	}
	
	public void add() {
		int count = 1;
		for (String s: addList) {
			dic.addItem(s, 28160, 3000);
			System.out.println(s + " " + count++);
		}
		dic.save("data\\coreDictNew.dct");
	}
	
	public static void main(String[] args) { 
		AddNewWord anw = new AddNewWord();
		anw.setAddList("name.txt");
		anw.add();
	//	ArrayList<String> list = new ArrayList<String>(10);
		
	//		System.out.println(list.size());
	//	System.out.println(list.get(0));
	}
}
