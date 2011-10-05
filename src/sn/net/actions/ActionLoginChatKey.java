package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.net.PresenceHandler;
import sn.profilo.Profilo;

public final class ActionLoginChatKey extends Action {

    // --- Costanti & Variabili private -----------------------------------------------
	
	public static final int MESSAGE_ID = 3;
    
    //(int) command | (int) profilo_id (string) chat_key+salt MD5
    @Deprecated
	public static final String regex_clientToServer = "^([3]{1}) ([0-9]{0,15}) ([0-9a-zA-Z]{10,50})$";
	
	public static final String scheme_serverToClient = "{ \"op\":%d, \"result\":%s }";
	
    // --- Costruttori ---------------------------------------------------------
        
	public static void login(JSONObject obj, String data, INSIOClient client) {
		
		//se è dentro a channels se è già loggato
		if(!PresenceHandler.clients.containsKey(client.getSessionID())){
			
			Profilo profilo = null;
			// se è un profilo già loggato da un altro client
			Long profilo_id = (Long) obj.get("profilo_id");
			
			if(Profilo.profili.containsKey(profilo_id)){
				profilo = Profilo.profili.get(profilo_id);
			}else{					
				profilo = new Profilo();
			}
			
			boolean logged = profilo.login_byChatKey(client, profilo_id.intValue(),(String) obj.get("chat_key"));
			if(logged){
				Profilo.profili.put(profilo_id.intValue(), profilo);
			}
			write(client, logged);
		}
		
	}
	
	public static void write(INSIOClient client, boolean logged){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, new Boolean(logged).toString()));
	}
	
    // --- Getter & Setter -----------------------------------------------------

    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
