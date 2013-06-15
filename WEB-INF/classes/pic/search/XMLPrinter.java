package pic.search;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.util.List;
import java.io.FileOutputStream;

public class XMLPrinter {

	public void buildXML(String file, List<Picture> list) {
		Element root, picture, name, path;
		try {
			root = new Element("picture-list");
			
			Document doc = new Document(root);
			for (Picture pic: list) {
				picture = new Element("picture");
				name = new Element("name");
				path = new Element("path");
				String pic_tag = pic.getTag();
				String pic_path = pic.getPath();
				name.setText(pic_tag);
				path.setText(pic_path);
				picture.addContent(name);
				picture.addContent(path);
				root.addContent(picture);
			}
			Format format = Format.getCompactFormat();
			XMLOutputter XMLOut = new XMLOutputter(format);
			XMLOut.output(doc, new FileOutputStream(file));  
			System.out.println("printing...");
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
}
