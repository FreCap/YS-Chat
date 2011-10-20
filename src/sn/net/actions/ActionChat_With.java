package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

import sn.profilo.Profilo;

public final class ActionChat_With extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 6;
    
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d, \"actived\":%d, \"messages\":[$s] }";
	public static final String scheme_message = "{ \"me\":%d, \"profilo_id\":%d, \"message\":[%s] }";
    // --- Constructors --------------------------------------------------------
        
	public static void chat_with(JSONObject obj, String data, INSIOClient client) {
		// è dentro a channels se è già loggato
		int profilo_id = Profilo.sessionID2profiloID.get(client.getSessionID());
		String list_string = "";
		if(profilo_id > 0){
			
			Long profilo_idTo = (Long) obj.get("profilo_id");
			String message = (String) obj.get("message");
			
			Profilo profilo = Profilo.profili.get(profilo_id);
			IntegerArray list = profilo.friends_online;
			for (int profilo_id_friend : list.toIntArray()) {
				if(profilo_id_friend == profilo_idTo){
					Profilo.profili.get(profilo_idTo).message_receive(profilo_id, message);
					profilo.message_send(profilo_idTo.intValue(), message, client);
					break;
				}
			}
			
			//TODO scrittura su mysql
			
		}
	}
	public static String convertToMessage(int profilo_id, String message, int me){
		return String.format(scheme_message, me, profilo_id, message);
	}
	
	public static void write(INSIOClient client, String message){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, message));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
