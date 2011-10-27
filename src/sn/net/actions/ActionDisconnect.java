/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.net.actions;

/**
 *
 * @author Alessandro Passerini <alessandro.passerini at xelia.it>
 */
public class ActionDisconnect extends Action
{
    
    // --- Costanti & Variabili private ----------------------------------------

    public static final int MESSAGE_ID = 1;
    
    //(int) command | (string) salt
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d, \"salt\":\"%s\" }";

	
    // --- Costruttori ---------------------------------------------------------
	
    public ActionDisconnect() {
    }

    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
