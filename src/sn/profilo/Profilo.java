package sn.profilo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import sn.db.table.TableProfilo;
import sn.db.table.TableRelazioni;
import sn.net.PresenceFutureListener;
import sn.net.PresenceHandler;
import sn.net.actions.ActionChat_Close;
import sn.net.actions.ActionChat_NoActive;
import sn.net.actions.ActionChat_Open;
import sn.net.actions.ActionChat_With;
import sn.net.actions.ActionConnect;
import sn.util.SecureHash;

import com.ibdknox.socket_io_netty.INSIOClient;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;


public class Profilo {
	
	public static ConcurrentHashMap<Integer,Profilo> profili = new ConcurrentHashMap<Integer,Profilo>(); // dv int è ovviamente l'profilo id
	public static ConcurrentHashMap<String,Integer> sessionID2profiloID = new ConcurrentHashMap<String, Integer>();
	ArrayList<String> channels_id_connected = new ArrayList<String>();
	
	int profilo_id;
	public String nickname;
	String chat_key;
	public IntegerArray friends_online = new IntegerArray();
	
	IntegerArray chatTab_opened = new IntegerArray();
	int chatTab_actived;
	
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
		friend_checkList_fromDB();
		return true;
	}
	
	public void friend_checkList_fromDB(){
		
		ResultSet relazioni_SQL = TableRelazioni.get_byProfiloId(profilo_id);
		IntegerArray new_friends_online = new IntegerArray();
		try {
			int profilo_id2;
			while(relazioni_SQL.next()){
				profilo_id2 = relazioni_SQL.getInt("profilo_id2");
				if(profili.containsKey(profilo_id2)){
					//TODO da mettere l'esclusione ai tipi di account non possibili da contattare, come quelli bloccati..
					new_friends_online.add(profilo_id2);
					profili.get(profilo_id2).friend_add(profilo_id);
				}
			}
			friends_online = new_friends_online;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	public void message_send(int profilo_idTo, String message, INSIOClient client){
		String messages = ActionChat_With.convertToMessage(profilo_idTo, message, 1);
		for(String channel_id:channels_id_connected){
			if(channel_id != client.getSessionID()){
				ActionChat_With.write(PresenceHandler.clients.get(channel_id), messages);
			}
		}
	}
	
	public void message_receive(int profilo_idFrom, String message){
		String messages = ActionChat_With.convertToMessage(profilo_idFrom, message, 0);
		for(String channel_id:channels_id_connected){
			ActionChat_With.write(PresenceHandler.clients.get(channel_id),messages);
		}
	}
	
	public void chatTab_open(int profilo_idToOpen, INSIOClient client){
		chatTab_actived = profilo_idToOpen;
		chatTab_opened.addNew(profilo_idToOpen);
		//TODO order not implemented, adesso è 1
		String ChatTab = ActionChat_Open.convertToMessage(profilo_idToOpen, 1);
		for(String channel_id:channels_id_connected){
			if(channel_id != client.getSessionID()){
				ActionChat_Open.write(PresenceHandler.clients.get(channel_id),chatTab_actived, ChatTab);
			}
		}
	}
	
	public void chatTab_close(int profilo_idToClose, INSIOClient client){
		chatTab_opened.pop(profilo_idToClose);
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
	
	public void friend_add(int profilo_id){
		synchronized (friends_online) {
			friends_online.addNew(profilo_id);
		}
	}
	
	public void friend_remove(int profilo_id){
		synchronized (friends_online) {
			friends_online.pop(profilo_id);
		}
	}
	
	public boolean channel_add(INSIOClient client){
		boolean result = false;
		synchronized(channels_id_connected) {
			boolean exist = channels_id_connected.contains(client.getSessionID());		
			if(exist == true){
				return false;
			}else{
				sessionID2profiloID.put(client.getSessionID(), profilo_id);
				channels_id_connected.add(client.getSessionID());
				result = true;
			}
		}
		
		if(result){
			PresenceHandler.clients.put(client.getSessionID(), client);
			
			PresenceHandler.addFutureListener(client, CHANNEL_DISCONNECT);
		}
		return result;

	}
	
	static PresenceFutureListener CHANNEL_DISCONNECT = new PresenceFutureListener() {
		 public void operationComplete(INSIOClient client) {
			int profilo_id = Profilo.sessionID2profiloID.get(client.getSessionID());
			Profilo profilo = Profilo.profili.get(profilo_id);
			synchronized(profilo.channels_id_connected) {
				 profilo.channels_id_connected.remove(client.getSessionID());
         	}
			sessionID2profiloID.remove(client.getSessionID());
            if(profilo.channels_id_connected.size() == 0){
            	//TODO logout profilo
            }
		 }
	 };
	
}
