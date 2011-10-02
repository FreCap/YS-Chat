package db.table;


public class TableProfilo extends Table {
		
	static String tableName = "profilo";
	static boolean Cache_Row = true;
	static String primaryColumn = "account_id";
	
	public static String get_chatKey_byAccountId(int account_id){
		
		return get_column_byId(account_id, "chat_key");
		
	}
	
}