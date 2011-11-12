package sn.net.actions;

import java.util.HashSet;

import org.json.simple.JSONObject;

import com.ibdknox.socket_io_netty.INSIOClient;

import sn.profilo.Party;
import sn.profilo.Profilo;
import sn.profilo.ProfiloModel;
import sn.store.Conversazione;

public final class ActionChat_AddFriend extends Action {

    // --- Costanti & Variabili private -----------------------------------------------

    public static final int MESSAGE_ID = 9;
    
    @Deprecated
    public static final String regex_clientToServer = "^([1]{1})$";
	public static final String scheme_serverToClient = "{ \"op\":%d, \"profilo_id\":%d, \"conv_id\":%d }";
    // --- Constructors --------------------------------------------------------
        
	public static void chat_addfriend(JSONObject obj, String data, INSIOClient client) {
		// è dentro a channels se è già loggato
		int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());

		if(profilo_id > 0){
			
			int profilo_idToAdd = ((Long) obj.get("profilo_id")).intValue();
			int conv_id = ((Long) obj.get("conv_id")).intValue();
			
			boolean notExist = true;
			int party_id = 0;
			if(ProfiloModel.profili.containsKey(conv_id)){
				ProfiloModel profilo_got = ProfiloModel.profili.get(conv_id);
				System.out.println("profilo.tipo: " + profilo_got.get_tipo());
				if(profilo_got.get_tipo() == Conversazione.TIPO_profilo2profilo){
					HashSet<Integer> partecipantiSet = new HashSet<Integer>();
					partecipantiSet.add(profilo_id);
					partecipantiSet.add(profilo_idToAdd);
					partecipantiSet.add(conv_id);
					Party party = new Party(profilo_id, partecipantiSet);
					ProfiloModel.profili.put(party.profilo_id, party);
					party_id = party.profilo_id;
					notExist = false;
				}else if(profilo_got.tipo == Conversazione.TIPO_profilo2party){
					Party party = (Party) profilo_got;
					party.add_friend(profilo_id, profilo_idToAdd);
					party_id = party.profilo_id;
					notExist = false;
				}
			}else{
				Party party = new Party();
				if(conv_id > Party.PARTY_IDSTART){
					if(!party.load(conv_id)){
						party.add_friend(profilo_id, profilo_idToAdd);
						notExist = false;
					}
				}
				if(notExist == true){
					HashSet<Integer> partecipantiSet = new HashSet<Integer>();
					partecipantiSet.add(profilo_id);
					partecipantiSet.add(profilo_idToAdd);
					partecipantiSet.add(conv_id);
					party = new Party(profilo_id, partecipantiSet);
				}
				ProfiloModel.profili.put(party.profilo_id, party);
				party_id = party.profilo_id;
			}
			
			Profilo profilo = (Profilo)ProfiloModel.profili.get(profilo_id);
			System.out.println("party_id: " + party_id);
			profilo.chatTab_open(party_id, client, false);
				
		}
	}
	
	public static void write(INSIOClient client, int ChatTab_actived, String ChatTab){
		client.send(String.format(scheme_serverToClient, MESSAGE_ID, ChatTab_actived, ChatTab));
	}
    
    // --- Getter & Setter -----------------------------------------------------
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

	
}
