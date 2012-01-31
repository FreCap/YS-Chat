package sn.net.actions;

import it.uniroma3.mat.extendedset.intset.FastSet;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.calls.Call;
import sn.profilo.Profilo;
import sn.profilo.ProfiloModel;
import sn.store.Conversazione;

public final class ActionCall_Ring extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 21;

	public static final String scheme_serverToClient = "{ \"op\":%d, \"conv_id\":%d, \"caller\":%d, \"call_id\":\"%s\" }";
	
    // --- Constructors --------------------------------------------------------
        
	public static void call_ring(JSONObject obj, String data, INSIOClient client) {
		
		int conv_id = ((Long) obj.get("conv_id")).intValue();
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());
		if(profilo_id > 0){
			Profilo profilo = (Profilo)ProfiloModel.profili.get(profilo_id);
			
			FastSet list = profilo.friends_online;
			for (int profilo_id_friend : list.toArray()) {			
				if(profilo_id_friend == conv_id){
					ProfiloModel profiloCalled = ProfiloModel.profili.get(conv_id);
					String conversazione_id = Conversazione.get_id_conversazione(profilo_id, profiloCalled.profilo_id, profiloCalled.get_tipo());

					if(Call.calls.containsKey(conversazione_id)){
						//TODO as I accepted z call
					}else{	
						new Call(profilo, profiloCalled);
					}
				}
			}								
			
		}else{
				
			// inesistente
				
		}
			
	}
		
	
	public static void write(INSIOClient client, int conv_id, int caller, String call_id){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, conv_id, caller, call_id));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
