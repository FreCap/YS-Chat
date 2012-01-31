package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

public final class ActionCall_Wait extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 24;

	public static final String scheme_serverToClient = "{ \"op\":%d, \"conv_id\":%d, \"call_id\":\"%s\" }";
	
    // --- Constructors --------------------------------------------------------
        
	public static void call_wait(JSONObject obj, String data, INSIOClient client) {
		
		// should be empty
		
	}
	
	public static void write(INSIOClient client, int conv_id, String call_id){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, conv_id, call_id));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
