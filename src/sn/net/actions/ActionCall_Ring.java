package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.profilo.Profilo;
import sn.profilo.ProfiloModel;

public final class ActionCall_Ring extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 21;

	public static final String scheme_serverToClient = "{ \"op\":%d, \"conv_id\":%d, \"caller\":%d, \"call_id\":\"%s\" }";
	
    // --- Constructors --------------------------------------------------------
        
	public static void call_ring(JSONObject obj, String data, INSIOClient client) {
		
		int support = ((Long) obj.get("conv_id")).intValue();
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());
		if(profilo_id > 0){
			Profilo profilo = (Profilo)ProfiloModel.profili.get(profilo_id);
			
			// TODO creazione della call
			
		}
		
	}
	
	public static void write(INSIOClient client, int conv_id, int caller, String call_id){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, conv_id, caller, call_id));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
