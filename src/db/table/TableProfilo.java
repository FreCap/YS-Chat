package db.table;

/**
 * 
 * @author Alessandro Passerini <alessandro.passerini at xelia.it>
 */
public class TableProfilo extends Table {
    
    // --- Costanti & Variabili private ----------------------------------------
	
    static String tableName = "profilo";
	static boolean Cache_Row = true;
	static String primaryColumn = "account_id";
	
    // --- Constructors --------------------------------------------------------

	public static String get_chatKey_byAccountId(int account_id){
		
		return get_column_byId(account_id, "chat_key");
		
	}
	
    // --- Getter & Setter -----------------------------------------------------

    // --- Metodi public ------------------------------------------------------
    
    // --- Metodi protected ---------------------------------------------------

    // --- Metodi private -----------------------------------------------------

}