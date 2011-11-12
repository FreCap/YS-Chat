package sn.net.actions;

import it.uniroma3.mat.extendedset.intset.FastSet;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.profilo.ProfiloModel;

public final class ActionChat_With extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 6;
    
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d, \"messages\":[%s] }";
	public static final String scheme_message = "{ \"conv_id\":%d, \"profilo_id\":%d, \"message\":\"%s\" }";
	public static final String scheme_messageList = "{ \"conv_id\":%d, \"messages\":[%s] }";
    // --- Constructors --------------------------------------------------------
        
	public static void chat_with(JSONObject obj, String data, INSIOClient client) {
		// è dentro a channels se è già loggato
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());

		if(profilo_id > 0){
			
			int profilo_idTo = ((Long) obj.get("profilo_id")).intValue();
			String message = (String) obj.get("message");
			
			ProfiloModel profilo = ProfiloModel.profili.get(profilo_id);
			FastSet list = profilo.friends_online;
			for (int profilo_id_friend : list.toArray()) {
				if(profilo_id_friend == profilo_idTo){
					ProfiloModel ProfiloTo = ProfiloModel.profili.get(profilo_idTo);
					ProfiloTo.message_receive(profilo_idTo, profilo_id, message, false);
					profilo.message_send(profilo_idTo, message, client);
					break;
				}
			}
		}
	}
	public static String convertToMessage(int conv_id, int profilo_id, String message){
		return String.format(scheme_message, conv_id, profilo_id, message);
	}
	public static String convertToMessageList(int conv_id, String messages){
		return String.format(scheme_messageList, conv_id, messages);
	}
	
	public static void write(INSIOClient client, String message){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, message));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
