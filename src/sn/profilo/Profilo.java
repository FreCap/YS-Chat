package sn.profilo;

import it.uniroma3.mat.extendedset.intset.FastSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import sn.net.PresenceFutureListener;
import sn.net.PresenceHandler;
import sn.net.actions.ActionCall_Ring;
import sn.net.actions.ActionCall_Support;
import sn.net.actions.ActionCall_Wait;
import sn.net.actions.ActionChat_Close;
import sn.net.actions.ActionChat_NoActive;
import sn.net.actions.ActionChat_Open;
import sn.net.actions.ActionChat_With;
import sn.net.actions.ActionConnect;
import sn.store.Conversazione;
import sn.util.SecureHash;
import ys.db.Redis;
import ys.db.table.TableProfilo;
import ys.db.table.TableRelazioni;

import com.ibdknox.socket_io_netty.INSIOClient;
import sn.calls.Call;
import sn.net.actions.ActionCall;


public class Profilo extends ProfiloModel {
	
	ArrayList<String> channels_id_connected = new ArrayList<String>();
	
	final public int tipo = 1;
	
	String chat_key;

        final public ArrayList<String> call_opened = new ArrayList<String>();
	public String call_actived;
	
	FastSet chatTab_opened = new FastSet();
	public int chatTab_actived;
	
	public ArrayList<String> channel_id_connectedVoiceSupport = new ArrayList<String>();
	
	final Logger logger = LoggerFactory.getLogger(Profilo.class);
	
	//TODO to implement	
	int status;
	
	public boolean login_byChatKey(INSIOClient client, int profilo_id_from, String chatKeyEncrypted){
		
		if(chat_key == null){
			ResultSet SQL_profilo = TableProfilo.get_byId(profilo_id_from);
			
			//utente inesistente
			if(SQL_profilo == null){
				System.out.println("profilo_id " + profilo_id_from + " inesistente");
				//TODO error
				return false;
			}
			try {
				profilo_id = profilo_id_from;
				nickname = SQL_profilo.getString("nickname");
				chat_key = SQL_profilo.getString("chat_key");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		String Hash_fromDB = SecureHash.Md5(chat_key+ActionConnect.salts.get(client.getSessionID()));
		// chat key errata
		if(!(Hash_fromDB.equals(chatKeyEncrypted))){
			//TODO error
			return false;
		}
		
		//success
		channel_add(client);
		return true;
	}
	
	public void friend_checkList_fromDB(){
		
		ResultSet relazioni_SQL = TableRelazioni.get_byProfiloId(profilo_id);
		FastSet new_friends_online = new FastSet();
		try {
			int profilo_id2;
			while(relazioni_SQL.next()){
				int relazione_tipo = relazioni_SQL.getInt("tipo");
				if(relazione_tipo == TIPO_AMICI || relazione_tipo == TIPO_ISCRITTO){
					profilo_id2 = relazioni_SQL.getInt("profilo_id2");
					if(profili.containsKey(profilo_id2)){
						new_friends_online.add(profilo_id2);
						profili.get(profilo_id2).friend_add(profilo_id);
					}else{
						if(relazione_tipo == TIPO_ISCRITTO){
							// TODO crea gruppo
							Gruppo gruppo = new Gruppo();
							gruppo.crea(profilo_id2);
							
							new_friends_online.add(profilo_id2);
							profili.put(profilo_id2, gruppo);
							profili.get(profilo_id2).friend_add(profilo_id);
						}
					}
				}
			}
			
			Jedis DB = Redis.DBPool.getResource();
			
			// fa il load delle conversazioni solo se è stata aperta nelle chat opened
			Set<String> opened = DB.smembers(Conversazione.PROFILO_CHATOPENED + profilo_id);
			for (String profilo_id : opened) {
				int profilo_idOpened = Integer.valueOf(profilo_id);
				chatTab_opened.add(profilo_idOpened);
				if(profilo_idOpened > Party.PARTY_IDSTART){
					if(!profili.containsKey(profilo_idOpened)){
						Party party = new Party();
						party.load(profilo_idOpened);
						profili.put(profilo_idOpened, party);
					}
				}
			}
			
			Set<String> party = DB.smembers(Conversazione.PROFILO_PARTY + profilo_id);
			for (String party_idDB : party) {
				int party_idCheck = Integer.valueOf(party_idDB);
				if(profili.containsKey(party_idCheck)){
					profili.get(party_idCheck).friend_add(profilo_id);
					friend_add(party_idCheck);
				}
			}
			
			Redis.DBPool.returnResource(DB);
			
			friends_online = new_friends_online;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	

	//######### CALL SECTION #########
	
	public void call_notSupportedBy(int profilo_id){
		for(String channel_id:channels_id_connected){
			ActionCall_Support.write(PresenceHandler.clients.get(channel_id), profilo_id, false);
		}		
	}
	
	public boolean call_ring(int conv_id, int caller_id, String call_id){
		for(String channel_id:channels_id_connected){
			ActionCall_Ring.write(PresenceHandler.clients.get(channel_id), conv_id, caller_id, call_id);
		}
		return true;
	}
	
	public void call_wait(int conv_id, String call_id){
            for(String channel_id:channels_id_connected){
            	ActionCall_Wait.write(PresenceHandler.clients.get(channel_id), conv_id, call_id);
            }		
	}
        
        public void call_addOpened(String call_id){
            synchronized(call_opened){
                call_opened.add(call_id);
            }    
            call_actived = call_id;
        }
        
        public void call(Call call){
        
            call_addOpened(call.call_id);
            
            int conv_id;
            
            if(call.called_id == profilo_id){
                conv_id = call.caller_id;
            }else{
                conv_id = call.called_id;
            }
            
            int ts_port = call.get_TSPort();
            
            for(String channel_id:channel_id_connectedVoiceSupport){
		ActionCall.write(PresenceHandler.clients.get(channel_id), conv_id, call.call_id, call.call_password, 1 , call.server_id, ts_port);
          
            }
        
        }
        
	
	//######### MESSAGE SECTION #########
	
	public void message_send(int profilo_idTo, String message, INSIOClient client){
		String messages = ActionChat_With.convertToMessage(profilo_idTo, profilo_id, message);
		for(String channel_id:channels_id_connected){
			if(channel_id != client.getSessionID()){
				ActionChat_With.write(PresenceHandler.clients.get(channel_id), messages);
			}
		}		
	}
	
	public void message_receive(int profilo_idChat,int profilo_idFrom, String message, boolean already_stored){
		
		// in una conversazione profilo2profilo profilo_idChat diventa anche profilo_idFrom
		if(profilo_idChat == profilo_id){
			profilo_idChat = profilo_idFrom;
		}
		
		String messages = ActionChat_With.convertToMessage(profilo_idChat, profilo_idFrom, message);
		for(String channel_id:channels_id_connected){
			ActionChat_With.write(PresenceHandler.clients.get(channel_id),messages);
		}	
		
		chatTab_opened.add(profilo_idChat);
		
		if(already_stored == false){
			HashSet<Integer> profili_idsRed = new HashSet<Integer>(1);
			if(chatTab_actived == profilo_idChat){
				profili_idsRed.add(profilo_id);
			}
			Conversazione.write(message, profilo_id, profilo_idChat, tipo, profili_idsRed);
		}
	}
	
	public void chatTab_open(int profilo_idToOpen, INSIOClient client){
		chatTab_open(profilo_idToOpen, client, true);
	}

	public void chatTab_open(int profilo_idToOpen, INSIOClient client, boolean ignoreSameClient){
		chatTab_actived = profilo_idToOpen;
		if(profilo_idToOpen == profilo_id){
			Thread.dumpStack();
			System.out.println("ERRORE DOPPIO");
		}
		
		//TODO order not implemented, adesso è 1
		String ChatTab = ActionChat_Open.convertToMessage(profilo_idToOpen, 1);
		for(String channel_id:channels_id_connected){
			if((channel_id != client.getSessionID() && ignoreSameClient) || (!ignoreSameClient)){
				ActionChat_Open.write(PresenceHandler.clients.get(channel_id),chatTab_actived, ChatTab);
			}
		}
		
		if(chatTab_opened.add(profilo_idToOpen)){
			
			//TODO da fare in modo in futuro che solo i client attivi ricevano i msg.
			StringBuilder messages = new StringBuilder();
			boolean first = true;
			for (String message : Conversazione.conversazione_getStorico(
															Conversazione.get_id_conversazione(profilo_id, profilo_idToOpen, Profilo.profili.get(profilo_idToOpen).get_tipo()),
															10)) {
				if(first == false){
					messages.append(",");
				}else{
					first = false;
				}
				messages.append(message);
			}
			String text = ActionChat_With.convertToMessageList(profilo_idToOpen, messages.toString());
			
			if(first != true){
				for(String channel_id:channels_id_connected){
					ActionChat_With.write(PresenceHandler.clients.get(channel_id), text);
				}
			}
			
		}
	}
	
	public String ListchatTab_opened(){
		
		//TODO order not implemented, adesso è 1
		boolean first = true;
		
		StringBuilder ChatTab = new StringBuilder();
		if(!chatTab_opened.isEmpty()){
			for (int chat_id : chatTab_opened.toArray()) {
				if(first == false){
					ChatTab.append(",");
				}else{
					first = false;
				}
				ChatTab.append(ActionChat_Open.convertToMessage(chat_id, 1));
			}
		}
		return ChatTab.toString();
		
	}
	
	public void chatTab_close(int profilo_idToClose, INSIOClient client){
		chatTab_opened.remove(profilo_idToClose);
		for(String channel_id:channels_id_connected){
			if(channel_id != client.getSessionID()){
				ActionChat_Close.write(PresenceHandler.clients.get(channel_id),profilo_idToClose);
			}
		}
	}
	
	public void chatTab_noactive(INSIOClient client){
		chatTab_actived = 0;
		for(String channel_id:channels_id_connected){
			if(channel_id != client.getSessionID()){
				ActionChat_NoActive.write(PresenceHandler.clients.get(channel_id));
			}
		}
	}
	
	public void channel_voiceSupport_add(INSIOClient client){
		synchronized (channel_id_connectedVoiceSupport) {
			boolean exist = channel_id_connectedVoiceSupport.contains(client.getSessionID());		
			if(!exist){
				channel_id_connectedVoiceSupport.add(client.getSessionID());
			}
		}
	}
	
	public boolean channel_add(INSIOClient client){
		boolean result = false;
		synchronized(channels_id_connected) {
			boolean exist = channels_id_connected.contains(client.getSessionID());		
			if(exist == true){
				return false;
			}else{
				channels_id_connected.add(client.getSessionID());
				result = true;
			}
		}
		
		if(result){
			ProfiloModel.sessionID2profiloID.put(client.getSessionID(), profilo_id);
			PresenceHandler.clients.put(client.getSessionID(), client);
			PresenceHandler.addFutureListener(client, CHANNEL_DISCONNECT);
		}
		return result;

	}
	
	public void disconnect(){
		
		//segnalo che mi sono sconnesso
		for (int profilo_idCheck : friends_online.toArray()) {
			profili.get(profilo_idCheck).friend_remove(profilo_id);
		}
			
		//salvo le chat apert
		Jedis DB = Redis.DBPool.getResource();
		Pipeline p = DB.pipelined();
		
		p.del(Conversazione.PROFILO_CHATOPENED + ((Integer)profilo_id).toString());
		if(!chatTab_opened.isEmpty()){
			for (Integer chatTab_open : chatTab_opened.toArray()) {
				if(chatTab_open != 0){
					p.sadd(Conversazione.PROFILO_CHATOPENED + ((Integer)profilo_id).toString(), chatTab_open.toString());
				}
			}
		}
		p.sync();
		Redis.DBPool.returnResource(DB);

	}
	
	static PresenceFutureListener CHANNEL_DISCONNECT = new PresenceFutureListener() {
		 public void operationComplete(INSIOClient client) {
			int profilo_id = ProfiloModel.sessionID2profiloID.get(client.getSessionID());
			Profilo profilo = (Profilo) ProfiloModel.profili.get(profilo_id);
			synchronized(profilo.channels_id_connected) {
				 profilo.channels_id_connected.remove(client.getSessionID());
         	}
			synchronized(profilo.channel_id_connectedVoiceSupport) {
				 profilo.channel_id_connectedVoiceSupport.remove(client.getSessionID());
        	}
			ProfiloModel.sessionID2profiloID.remove(client.getSessionID());
			
            if(profilo.channels_id_connected.isEmpty()){
            	profilo.disconnect();
            	ProfiloModel.profili.remove(profilo_id);
            }
		 }
	 };
	

	public int get_tipo(){
		return tipo;
	}
}
