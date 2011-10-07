package sn.net.actions;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.net.PresenceHandler;
import sn.profilo.Profilo;

public final class ActionLoginChatKey extends Action {

    // --- Costanti & Variabili private -----------------------------------------------
	
	public static final int MESSAGE_ID = 3;
    
    //(int) command | (int) account_id (string) chat_key+salt MD5
    public static final String regex_clientToServer = "^([3]{1}) ([0-9]{0,15}) ([0-9a-zA-Z]{10,50})$";
	public static final String scheme_serverToClient = "%d %d %s";

	int profilo_id;
	String chat_key;
	
    // --- Constructors --------------------------------------------------------
        
	public static void login(String[] fields, String data, INSIOClient client) {
		
		boolean match = data.matches(regex_clientToServer);
		if(match){
			
			//se è dentro a channels se è già loggato
			if(PresenceHandler.clients.get(client.getSessionID()) == null){
				
				Profilo profilo = null;
				
				// se è un profilo già loggato da un altro client
				if(Profilo.profili.containsKey(fields[1])){
					profilo = Profilo.profili.get(fields[1]);
				}else{					
					profilo = new Profilo();
				}
				
				profilo.login_byChatKey(client, Integer.parseInt(fields[1]), fields[2]);
				
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
