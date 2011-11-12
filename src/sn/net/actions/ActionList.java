package sn.net.actions;

import it.uniroma3.mat.extendedset.intset.FastSet;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

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
	public static final String scheme_list = "{\"profilo_id\":%d,\"tipo\":%d,\"nickname\":\"%s\"}";
		
    // --- Constructors --------------------------------------------------------
        
	public static void list(JSONObject obj, String data, INSIOClient client) {
		
		int tipo = ((Long) obj.get("tipo")).intValue();
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());
		if(profilo_id > 0){
			if(tipo == FRIENDS || tipo == ALL){
				// è dentro a channels se è già loggato
				StringBuilder list_string = new StringBuilder();
				ProfiloModel profilo;
				FastSet list = ProfiloModel.profili.get(profilo_id).friends_online;
				if(!list.isEmpty()){
					for (int profilo_id_friend : list.toArray()) {
						if(!(list_string.length() == 0)){
							list_string.append(",");
						}
						if(profilo_id_friend != 0){
							profilo = ProfiloModel.profili.get(profilo_id_friend);
							list_string.append(String.format(scheme_list, profilo_id_friend, profilo.tipo, profilo.nickname));
						}
					}
				}
				write(client, list_string.toString());
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
