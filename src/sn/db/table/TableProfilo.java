package sn.db.table;

import java.sql.ResultSet;

/**
 * 
 * @author Alessandro Passerini <alessandro.passerini at xelia.it>
 */
public class TableProfilo extends Table {
    
    // --- Costanti & Variabili private ----------------------------------------
	
    static String tableName = "profilo";
	static boolean Cache_Row = true;
	static String primaryColumn = "profilo_id";

	public static String get_column_byId(int item_id, String column){
		return Table.get_column_byId(item_id, column, tableName, primaryColumn);
	}
	
	public static ResultSet get_byId(int item_id){
		return Table.get_byId(item_id, tableName, primaryColumn);
	}
	
	public static String[] get_column_byId(int item_id, boolean toArray){
		return Table.get_byId(item_id, toArray, tableName, primaryColumn);
	}
    // --- Getter & Setter -----------------------------------------------------

    // --- Metodi public ------------------------------------------------------
    
    // --- Metodi protected ---------------------------------------------------

    // --- Metodi private -----------------------------------------------------

}