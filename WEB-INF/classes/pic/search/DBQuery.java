package pic.search;

import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Blob;
import java.io.*;
import java.util.Date;

public class DBQuery {
	String ip;
	String port;
	String dbname;
	String user;
	String password;
	Connection conn;
	Statement stmt;
	ArrayList<Medicine> resource;
	
	public DBQuery() {
		ip = "10.214.33.111";
		port = "1521";
		dbname = "tcm";
		user = "tcm";
		password = "tcm";
		resource = new ArrayList<Medicine>();
		Connect();
	}
	
	public DBQuery(String ip, String port, String dbname, String user, String password) {
		this.ip = ip;
		this.port = port;
		this.dbname = dbname;
		this.user = user;
		this.password = password;
		resource = new ArrayList<Medicine>();
		Connect();
	}
	
	public void Connect() {
		String cmd = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbname;
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(cmd, user, password);
			stmt = conn.createStatement();
		}
		catch (Exception e) {
			System.out.println("Connection error!");
		}
	}
	
	public void loadData() {
		try {
			String sql1 = "select t1.ym ym, t2.pic pic from " +
					"zcy t1, zcy_pic t2 where t1.id = t2.id and t1.ym like '%°×ÜÆ%'";
			String sql2 = "select t1.ym ym, t2.pic pic from " +
			"zcy t1, zcy_pic t2 where t1.id = t2.id";
			long start = new Date().getTime();
			ResultSet rs = stmt.executeQuery(sql2);
			long end = new Date().getTime();
	        System.out.println(end - start);
	        
			int count = 1;
			while (rs.next()) {
			//	if (count++ >= 1000)
				//	break;
			//	resource.add(new Medicine(rs.getString(1), rs.getBlob(2), ""));
				System.out.println(rs.getString(1) + " " + count++);
			} 
		}
		catch (SQLException e) {
			System.err.println(e);
		}
	//	for (Medicine med: resource) {
		//	System.out.println(med.getName());
	//	}
	}
	
	public void saveData() {
		FileOutputStream out;
		byte[] buffer = new byte[4096*10];
		int size = 0;
		int count = 0;
		try {
			for (Medicine med: resource) {
				count++;
				String name = med.getName();
				Blob blob = med.getPic();
				//File file = new File();
				out = new FileOutputStream("File_Pic\\" + name + "_" + count + ".jpg");
				InputStream in = blob.getBinaryStream();
				while ((size = in.read(buffer)) != -1) {
					out.write(buffer, 0, size);
				}
				out.flush();
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) throws SQLException {
		DBQuery db = new DBQuery();
		db.loadData();
		db.saveData();
	}
}
