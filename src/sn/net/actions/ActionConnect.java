package sn.net.actions;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;

import sn.net.PresenceHandler;
import sun.security.provider.MD5;

public final class ActionConnect extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final ActionsEnum MESSAGE_COMMAND = ActionsEnum.LOGIN;
    
    //(int) command | (string) salt
    public static final String regex_client = "/^([0-9]{0,3})([0-9a-zA-Z]{10,50})$/";
	public static final String scheme_server = "%d %s\n";
	
	public static ConcurrentHashMap<Integer,String> salts = new ConcurrentHashMap<Integer, String>();
	
    // --- Constructors --------------------------------------------------------
        
	public static void connect(String[] fields, String data, Channel channel) {
		
		boolean match = data.matches(regex_client);
		if(match){
			
			// è dentro a channels se è già loggato
			if(PresenceHandler.channels.find(channel.getId()) == null){
				
				SecureRandom random = new SecureRandom();
				String random_string = random.generateSeed(40).toString();
				salts.put(channel.getId(), random_string);
				
				write(channel, random_string);
				
			}
			
		}
		
	}
	
	public static void write(Channel channel, String random_string){
		
		channel.write(String.format(scheme_server, MESSAGE_COMMAND.ordinal(), random_string));
		
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
