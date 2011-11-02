package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

import sn.profilo.ProfiloModel;

public final class ActionChat_With extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 6;
    
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d, \"messages\":[%s] }";
	public static final String scheme_message = "{ \"me\":%d, \"conv_id\", \"profilo_id\":%d, \"message\":\"%s\" }";
    // --- Constructors --------------------------------------------------------
        
	public static void chat_with(JSONObject obj, String data, INSIOClient client) {
		// è dentro a channels se è già loggato
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());

		if(profilo_id > 0){
			
			int profilo_idTo = ((Long) obj.get("profilo_id")).intValue();
			String message = (String) obj.get("message");
			
			ProfiloModel profilo = ProfiloModel.profili.get(profilo_id);
			IntegerArray list = profilo.friends_online;
			for (int profilo_id_friend : list.toIntArray()) {
				if(profilo_id_friend == profilo_idTo){
					ProfiloModel ProfiloTo = ProfiloModel.profili.get(profilo_idTo);
					ProfiloTo.message_receive(profilo_id, profilo_id_friend, message);
					profilo.message_send(profilo_idTo, message, client);
					break;
				}
			}
			
			//TODO scrittura su redis tramite class Conversazioni
			//prima da definire il tipo di conv ( p2GRUPPO p2p p2party) 
			
		}
	}
	public static String convertToMessage(int conv_id, int profilo_id, String message){
		return String.format(scheme_message, conv_id, profilo_id, message);
	}
	
	public static void write(INSIOClient client, String message){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, message));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
