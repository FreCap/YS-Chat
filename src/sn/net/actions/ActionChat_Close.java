package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.profilo.Profilo;
import sn.profilo.ProfiloModel;

public final class ActionChat_Close extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 8;
    
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d, \"profilo_id\":%d }";
    // --- Constructors --------------------------------------------------------
        
	public static void chat_close(JSONObject obj, String data, INSIOClient client) {
		// è dentro a channels se è già loggato
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());

		if(profilo_id > 0){
			
			int profilo_idToOpen = ((Long) obj.get("profilo_id")).intValue();
			
			((Profilo) ProfiloModel.profili.get(profilo_id)).chatTab_close(profilo_idToOpen, client);
				
		}
	}
	
	public static void write(INSIOClient client, int profilo_id){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, profilo_id));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
