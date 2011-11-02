package sn.net.actions;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

import sn.profilo.Profilo;
import sn.profilo.ProfiloModel;

public final class ActionList extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 13;

    public static final int FRIENDS = 1;
    public static final int CHAT_OPENED = 2;
    public static final int ALL = 3;

    //(int) command | (string) salt
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d, \"list\":[%s] }";
	public static final String scheme_list = "{ \"profilo_id\":%d, \"nickname\":\"%s\" }";
		
    // --- Constructors --------------------------------------------------------
        
	public static void list(JSONObject obj, String data, INSIOClient client) {
		
		int tipo = ((Long) obj.get("tipo")).intValue();
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());
		if(profilo_id > 0){
			if(tipo == FRIENDS || tipo == ALL){
				// è dentro a channels se è già loggato
				String list_string = "";
				
				IntegerArray list = ProfiloModel.profili.get(profilo_id).friends_online;
				for (int profilo_id_friend : list.toIntArray()) {
					if(!list_string.isEmpty()){
						list_string = list_string.concat(",");
					}
					if(profilo_id_friend != 0){
						list_string = list_string.concat(String.format(scheme_list, profilo_id_friend, ProfiloModel.profili.get(profilo_id_friend).nickname));
					}
				}
			
				write(client, list_string);
			}
			
			if(tipo == FRIENDS || tipo == ALL){
				Profilo profilo = (Profilo) ProfiloModel.profili.get(profilo_id);
				String list = profilo.ListchatTab_opened();

				ActionChat_Open.write(client, profilo.chatTab_actived, list);

			}
		}
		
	}
	
	public static void write(INSIOClient client, String list){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, list));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
