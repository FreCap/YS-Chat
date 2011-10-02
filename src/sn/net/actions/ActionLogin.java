package sn.net.actions;

import org.jboss.netty.channel.Channel;

import sn.account.Account;
import sn.net.PresenceHandler;

public final class ActionLogin extends Action {

    // --- Constants & Variables -----------------------------------------------

    ActionsEnum MESSAGE_COMMAND = ActionsEnum.LOGIN;
    
    public static final String regex = "/^([0-9]{0,3}) ([0-9]{0,15}) ([0-9a-zA-Z]{10,50})$/";
	
	int account_id;
	String chat_key;
	
    // --- Constructors --------------------------------------------------------
        
	public static void login(String[] fields, String data, Channel channel) {
		
		boolean match = data.matches(regex);
		if(match){
			
			// è dentro a channels se è già loggato
			if(PresenceHandler.channels.find(channel.getId()) != null){
			
				// se è un account già loggato da un altro client
				if(Account.accounts.containsKey(fields[1])){
					
					Account account = Account.accounts.get(fields[1]);
					account.channel_add(channel);
					
				}else{
					
					Account account = new Account();
					account.login(channel);
					
				}
			}
			
		}
		
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
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
