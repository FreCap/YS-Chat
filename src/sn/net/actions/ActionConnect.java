package sn.net.actions;

import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.net.PresenceFutureListener;
import sn.net.PresenceHandler;
import sn.util.RandomHash;

public final class ActionConnect extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 1;
    
    //(int) command | (string) salt
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d, \"salt\":\"%s\" }";
	
	public static ConcurrentHashMap<String,String> salts = new ConcurrentHashMap<String, String>();
	
    // --- Constructors --------------------------------------------------------
        
	public static void connect(JSONObject obj, String data, INSIOClient client) {
		// è dentro a channels se è già loggato
		if(!PresenceHandler.clients.containsKey(client.getSessionID())){
			String random_string = RandomHash.one();
			salts.put(client.getSessionID(), random_string);
			PresenceHandler.addFutureListener(client, REMOVE_SALT);
			write(client, random_string);
		}
	}
	
	public static void write(INSIOClient client, String random_string){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, random_string));
	}
	
	static PresenceFutureListener REMOVE_SALT = new PresenceFutureListener() {
		 public void operationComplete(INSIOClient client) {
			 ActionConnect.salts.remove(client.getSessionID());
		 }
	 };
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
