package sn.net.actions;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;

import sn.net.PresenceHandler;

public final class ActionConnect extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 1;
    
    //(int) command | (string) salt
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "%d %s";
	
	public static ConcurrentHashMap<Integer,String> salts = new ConcurrentHashMap<Integer, String>();
	
    // --- Constructors --------------------------------------------------------
        
	public static void connect(String[] fields, String data, Channel channel) {
		System.out.println("PASSAGE:0");
		System.out.println(data);
		boolean match = data.matches(regex_clientToServer);
		if(match){
			System.out.println("PASSAGE:1");
			// è dentro a channels se è già loggato
			if(PresenceHandler.channels.find(channel.getId()) == null){
				System.out.println("PASSAGE:2");
				SecureRandom random = new SecureRandom();
				String random_string = random.generateSeed(40).toString();
				salts.put(channel.getId(), random_string);

				write(channel, random_string);
				
			}
			
		}
		
	}
	
	public static void write(Channel channel, String random_string){
		
		channel.write(String.format(scheme_serverToClient, MESSAGE_ID, random_string));
		
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
