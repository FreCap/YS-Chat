package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.profilo.Profilo;

public final class ActionChat_NoActive extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 9;
    
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d }";
    // --- Constructors --------------------------------------------------------
        
	public static void chat_noactive(JSONObject obj, String data, INSIOClient client) {
		// è dentro a channels se è già loggato
		int profilo_id = Profilo.sessionID2profiloID.get(client.getSessionID());

		if(profilo_id > 0){
			
			Long profilo_idToOpen = (Long) obj.get("profilo_id");			
			Profilo.profili.get(profilo_id).chatTab_close(profilo_idToOpen.intValue(), client);
		}
	}
	
	public static void write(INSIOClient client){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
