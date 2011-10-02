package sn.net.actions;

import org.jboss.netty.channel.Channel;

import sn.net.PresenceHandler;
import sn.profilo.Profilo;

public final class ActionLogin extends Action {

    // --- Constants & Variables -----------------------------------------------

    ActionsEnum MESSAGE_COMMAND = ActionsEnum.LOGIN;
    
    public static final String regex = "/^([0-9]{0,3}) ([0-9]{0,15}) ([0-9a-zA-Z]{10,50})$/";
	
	int profilo_id;
	String chat_key;
	
    // --- Constructors --------------------------------------------------------
        
	public static void login(String[] fields, String data, Channel channel) {
		
		boolean match = data.matches(regex);
		if(match){
			
			// è dentro a channels se è già loggato
			if(PresenceHandler.channels.find(channel.getId()) != null){
			
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
    
    // --- Public functions ----------------------------------------------------
    
    // --- Protected functions -------------------------------------------------

    // --- Private functions ---------------------------------------------------

	
}
