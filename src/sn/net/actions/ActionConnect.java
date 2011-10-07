package sn.net.actions;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.net.PresenceHandler;

public final class ActionConnect extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 1;
    
    //(int) command | (string) salt
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "%d %s";
	
	public static ConcurrentHashMap<String,String> salts = new ConcurrentHashMap<String, String>();
	
    // --- Constructors --------------------------------------------------------
        
	public static void connect(String[] fields, String data, INSIOClient client) {
		System.out.println("PASSAGE:0");
		System.out.println(data);
		boolean match = data.matches(regex_clientToServer);
		if(match){
			System.out.println("PASSAGE:1");
			// è dentro a channels se è già loggato
			if(PresenceHandler.clients.get(client.getSessionID()) == null){
				System.out.println("PASSAGE:2");
				SecureRandom random = new SecureRandom();
				String random_string = random.generateSeed(40).toString();
				salts.put(client.getSessionID(), random_string);

				write(client, random_string);
				
			}
			
		}
		
	}
	
	public static void write(INSIOClient client, String random_string){
		
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, random_string));
		
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
