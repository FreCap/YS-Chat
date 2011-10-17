package sn.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import sn.db.Mysql;


public abstract class Table {

	static String tableName = null;
	static boolean Cache_Row = false;
	static String primaryColumn;
	
	public static String get_column_byId(int item_id, String column, String tableName, String primaryColumn){
		
		ResultSet rs = Mysql.query("SELECT " + column + " FROM " + tableName + " WHERE " + primaryColumn + " = " + item_id + " LIMIT 1");
		
		try {
			if(rs.next()){
				return rs.getString(column);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ResultSet get_byId(int item_id, String tableName, String primaryColumn){
		
		ResultSet rs = Mysql.query("SELECT * FROM " + tableName + " WHERE " + primaryColumn + " = " + item_id + " LIMIT 1");
		try {
			rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	
	}

	public static String[] get_byId(int item_id, boolean toArray, String tableName, String primaryColumn){
		if(toArray){
			ResultSet rs = get_byId(item_id, tableName, primaryColumn);
			return Mysql.ResultSetRow_to_StringArray(rs);
		}
		return null;
	}	
	
	

    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
