package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.profilo.Profilo;
import sn.profilo.ProfiloModel;

public final class ActionChat_NoActive extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 11;
    
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d }";
    // --- Constructors --------------------------------------------------------
        
	public static void chat_noactive(JSONObject obj, String data, INSIOClient client) {
		// è dentro a channels se è già loggato
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());

		if(profilo_id > 0){
			
			((Profilo)ProfiloModel.profili.get(profilo_id)).chatTab_noactive(client);
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
