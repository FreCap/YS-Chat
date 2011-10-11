/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.net.actions;

/**
 *
 * @author Alessandro Passerini <alessandro.passerini at xelia.it>
 */
public class ActionChangeStatus extends Action 
{
    
    // --- Costanti & Variabili private ----------------------------------------
	
    public static final int MESSAGE_ID = 4;
    //public static final ActionsEnum MESSAGE_COMMAND = ActionsEnum.CHANGE_STATUS;
    
    //(int) command | (int) account_id (string) chat_key+salt MD5
    public static final String regex_client = "/^([0-9]{0,3}) ([0-9]{0,15}) ([0-9a-zA-Z]{10,50})$/";
	public static final String scheme_server = "%d %d %s\n";

	int profilo_id;
	String chat_key;
	
    // --- Costruttori ---------------------------------------------------------
	
    public ActionChangeStatus() {
    }
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
