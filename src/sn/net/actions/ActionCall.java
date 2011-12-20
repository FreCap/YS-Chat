package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;


public final class ActionCall extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 25;

	public static final String scheme_serverToClient = "{ \"op\":%d, \"conv_id\":%d, \"call_id\":\"%s\", \"call_password\":\"%s\", \"call_version\":%d,\"server\":%d,\"port\":%d }";
	
    // --- Constructors --------------------------------------------------------
        
	public static void call(JSONObject obj, String data, INSIOClient client) {
		
		//SHOULD BE EMPTY
			
	}
		
	
	public static void write(INSIOClient client, int conv_id, String call_id, String call_password, int call_version, int server, int port){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, conv_id, call_id, call_password, call_version, server, port));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
