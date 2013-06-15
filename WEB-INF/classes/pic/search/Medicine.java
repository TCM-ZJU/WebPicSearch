package pic.search;

import java.sql.Blob;

public class Medicine {
	private String name;
	private Blob pic;
	private String url;
	
	public Medicine(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public Medicine(String name, Blob pic, String url) {
		this.name = name;
		this.pic = pic;
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	
	public Blob getPic() {
		return pic;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPic(Blob pic) {
		this.pic = pic;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
