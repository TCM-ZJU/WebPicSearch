package pic.search;

public class Picture implements Comparable<Picture> {

	private String tag;
	private String path;
	
	public Picture (String tag, String path) {
		this.tag = tag;
		this.path = path;
	}
	
	public String getTag() {
		return this.tag;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String toString() {
		return tag + " " + path;
	}
	
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Picture))
			return false;
		Picture p = (Picture)o;
		return this.path.equals(p.path);
	}
	
	public int hashCode() {
		return tag.hashCode() + path.hashCode();
	}
	
	public int compareTo(Picture p) {
		return this.tag.compareTo(p.tag);
	}
}
