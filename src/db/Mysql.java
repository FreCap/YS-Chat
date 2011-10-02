package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

public class Mysql {

	public static Connection connection = null;
	
	public static final String url = "localhost";
	public static final String db = "social";
	public static final String user = "ascent";
	public static final String pass = "";
	
	
	public static void init(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();            
		} catch (Exception e) {
				            
		}
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + url + "/" + db + "?user=" + user + "&password=" + pass);
		} catch (SQLException e) {
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("Error: " + e.getErrorCode());
		}
	}
	
	public static Hashtable<Integer, String[]> query_toHashtable(String query){
		ResultSet rs = query(query);
		
		Hashtable<Integer, String[]> data  = new Hashtable<Integer, String[]>();
		String[] row = null;
		
		try {
			int colonne = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				row = new String[colonne];
				for (int i = 1; i < colonne; i++) {
					row[i-1] = rs.getString(i);
				}
				data.put(rs.getInt(1), row);
			}
		} catch (SQLException e) {
				
			e.printStackTrace();
		}
		return data;
			
	}
	
	public static ResultSet query(String query){
		
		Statement st = null;
		ResultSet rs = null;
		try {
			st = connection.createStatement();
			st.execute(query);
			rs = st.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return rs;	
		
	}
	
}
