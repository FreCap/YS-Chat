package db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.Mysql;

public abstract class Table {

	static String tableName = null;
	static boolean Cache_Row = false;
	static String primaryColumn = null;
	
	public static String get_column_byId(int item_id, String column){
		
		ResultSet rs = Mysql.query("SELECT " + column + " FROM " + tableName + " WHERE " + primaryColumn + " = " + item_id + "LIMIT 1");
		
		try {
			if(rs.next()){
				return rs.getString(column);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ResultSet get_byId(int item_id){
		
		ResultSet rs = Mysql.query("SELECT * FROM " + tableName + " WHERE " + primaryColumn + " = " + item_id + "LIMIT 1");
		try {
			rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	
	}

	public static String[] get_byId(int item_id, boolean toArray){
		if(toArray){
			ResultSet rs = get_byId(item_id);
			return Mysql.ResultSetRow_to_StringArray(rs);
		}
		return null;
	}	
	
	

    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
