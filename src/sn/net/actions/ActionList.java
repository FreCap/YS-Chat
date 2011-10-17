package sn.net.actions;

import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

import sn.profilo.Profilo;

public final class ActionList extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 9;
    
    //(int) command | (string) salt
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d, \"list\":[%s] }";
	public static final String scheme_list = "{ \"profilo_id\":%d, \"nickname\":\"%s\" }";
		
    // --- Constructors --------------------------------------------------------
        
	public static void list(JSONObject obj, String data, INSIOClient client) {
		// è dentro a channels se è già loggato
		int profilo_id = Profilo.sessionID2profiloID.get(client.getSessionID());
		String list_string = "";
		if(profilo_id > 0){
			IntegerArray list = Profilo.profili.get(profilo_id).friend_getList();
			for (int profilo_id_friend : list.toIntArray()) {
				if(!list_string.isEmpty()){
					list_string.concat(",");
				}
				if(profilo_id_friend != 0){
					list_string.concat(String.format(scheme_list, profilo_id_friend, Profilo.profili.get(profilo_id_friend).nickname));
				}
			}
		}
		write(client, list_string);
	}
	
	public static void write(INSIOClient client, String list){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, list));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
