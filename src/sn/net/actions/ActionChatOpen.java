/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.net.actions;

/**
 *
 * @author Alessandro Passerini <alessandro.passerini at xelia.it>
 */
public class ActionChatOpen extends Action 
{
    
    // --- Costanti & Variabili private ----------------------------------------
	
    public static final ActionsEnum MESSAGE_COMMAND = ActionsEnum.CHAT_OPEN;
    
    //(int) command | (int) account_id (string) chat_key+salt MD5
    public static final String regex_client = "/^([0-9]{0,3}) ([0-9]{0,15}) ([0-9a-zA-Z]{10,50})$/";
	public static final String scheme_server = "%d %d %s\n";

	int profilo_id;
	String chat_key;
	
    // --- Costruttori ---------------------------------------------------------
	
    public ActionChatOpen() {
    }
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
