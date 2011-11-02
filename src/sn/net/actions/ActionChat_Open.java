package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.profilo.Profilo;
import sn.profilo.ProfiloModel;

public final class ActionChat_Open extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 7;
    
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d, \"chatTab_active\":%d, \"chatTab\":[%s] }";
	public static final String scheme_message = "{ \"profilo_id\":%d, \"order\":%d }";
    // --- Constructors --------------------------------------------------------
        
	public static void chat_open(JSONObject obj, String data, INSIOClient client) {
		// è dentro a channels se è già loggato
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());

		if(profilo_id > 0){
			
			int profilo_idToOpen = ((Long) obj.get("profilo_id")).intValue();
			Profilo profilo = (Profilo)ProfiloModel.profili.get(profilo_id);
			
			profilo.chatTab_open(profilo_idToOpen, client);
				
		}
	}
	public static String convertToMessage(int profilo_id, int order){
		return String.format(scheme_message, profilo_id, order);
	}
	
	public static void write(INSIOClient client, int ChatTab_actived, String ChatTab){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, ChatTab_actived, ChatTab));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
