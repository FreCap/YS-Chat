package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.profilo.Profilo;
import sn.profilo.ProfiloModel;

public final class ActionCall_Support extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 22;

	public static final String scheme_serverToClient = "{ \"op\":%d, \"conv_id\":%d, \"support\":%s }";
	
    // --- Constructors --------------------------------------------------------
        
	public static void call_support(JSONObject obj, String data, INSIOClient client) {
		
		boolean support = ((Boolean) obj.get("support"));
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());
		if(profilo_id > 0){
			if(support){
				Profilo profilo = (Profilo)ProfiloModel.profili.get(profilo_id);
				profilo.channel_voiceSupport_add(client);
			}
		}
		
	}
	
	public static void write(INSIOClient client, int conv_id, boolean support){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, conv_id, ((Boolean) support).toString()));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
