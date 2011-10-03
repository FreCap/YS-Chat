package sn.net.actions;

import org.jboss.netty.channel.Channel;

import sn.net.PresenceHandler;
import sn.profilo.Profilo;

public final class ActionLogin extends Action {

    // --- Costanti & Variabili private -----------------------------------------------
	
    ActionsEnum MESSAGE_COMMAND = ActionsEnum.LOGIN;
    
    //(int) command | (int) account_id (string) chat_key+salt MD5
    public static final String regex_client = "/^([0-9]{0,3}) ([0-9]{0,15}) ([0-9a-zA-Z]{10,50})$/";
	public static final String scheme_server = "%d %d %s\n";

	int profilo_id;
	String chat_key;
	
    // --- Constructors --------------------------------------------------------
        
	public static void login(String[] fields, String data, Channel channel) {
		
		boolean match = data.matches(regex_client);
		if(match){
			
			//se è dentro a channels se è già loggato
			if(PresenceHandler.channels.find(channel.getId()) == null){
			
				// se è un profilo già loggato da un altro client
				if(Profilo.profili.containsKey(fields[1])){
					
					Profilo profiloExistent = Profilo.profili.get(fields[1]);
					profiloExistent.channel_add(channel);
					
				}else{
					
					Profilo profilo = new Profilo();
					profilo.login(channel, Integer.parseInt(fields[1]), fields[2]);
					
				}
			}
			
		}
		
	}
	
    // --- Getter & Setter -----------------------------------------------------
    
    public int getprofilo_id() {
        return profilo_id;
    }

    public void setprofilo_id(int profilo_id) {
        this.profilo_id = profilo_id;
    }

    public String getChat_key() {
        return chat_key;
    }

    public void setChat_key(String chat_key) {
        this.chat_key = chat_key;
    }

    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
