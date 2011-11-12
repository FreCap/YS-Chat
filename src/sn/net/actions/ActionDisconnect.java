/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.profilo.ProfiloModel;

/**
 *
 * @author Alessandro Passerini <alessandro.passerini at xelia.it>
 */
public class ActionDisconnect extends Action
{
    
    // --- Costanti & Variabili private ----------------------------------------

    public static final int MESSAGE_ID = 5;
    
	public static final String scheme_serverToClient = "{ \"op\":%d }";

	
    // --- Costruttori ---------------------------------------------------------
	
	public static void disconnect(JSONObject obj, String data, INSIOClient client) {
    	disconnect(client);
    }
    
	public static void disconnect(INSIOClient client) {
    	
    	// è dentro a channels se è già loggato
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());

		if(profilo_id > 0){
			
			System.out.println("disconnect");
			
		}
    	
    }

    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
