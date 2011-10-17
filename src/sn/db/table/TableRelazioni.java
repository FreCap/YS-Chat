package sn.db.table;

import java.sql.ResultSet;

import sn.db.Mysql;

/**
 * 
 * @author Alessandro Passerini <alessandro.passerini at xelia.it>
 */
public class TableRelazioni extends Table {
    
    // --- Costanti & Variabili private ----------------------------------------
	
    static String tableName = "relazioni";
	static boolean Cache_Row = true;
	static String primaryColumn = "profilo_id";

	public static ResultSet get_byProfiloId(int item_id){
		ResultSet rs = Mysql.query("SELECT * FROM " + tableName + " WHERE " + primaryColumn + " = " + item_id);
		return rs;
	}
	
    // --- Getter & Setter -----------------------------------------------------

    // --- Metodi public ------------------------------------------------------
    
    // --- Metodi protected ---------------------------------------------------

    // --- Metodi private -----------------------------------------------------

}