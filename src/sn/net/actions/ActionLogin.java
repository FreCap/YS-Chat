package sn.net.actions;

import org.jboss.netty.channel.Channel;

public final class ActionLogin extends Action {

    // --- Constants & Variables -----------------------------------------------

    ActionsEnum MESSAGE_COMMAND = ActionsEnum.LOGIN;
    
    public static final String regex = "/^([0-9]{0,15}) ([0-9a-zA-Z]{10,50})$/";
	
	int account_id;
	int chat_key;
	
    // --- Constructors --------------------------------------------------------
        
	public static void login(String[] fields, String data, Channel channel) {
		
		boolean match = data.matches(regex);
		if(match){			
			
		}
		
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getChat_key() {
        return chat_key;
    }

    public void setChat_key(int chat_key) {
        this.chat_key = chat_key;
    }
    
    // --- Public functions ----------------------------------------------------
    
    // --- Protected functions -------------------------------------------------

    // --- Private functions ---------------------------------------------------

	
}
