package sn.profilo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import sn.db.table.TableProfilo;
import sn.net.PresenceFutureListener;
import sn.net.PresenceHandler;
import sn.net.actions.ActionConnect;
import sn.util.SecureHash;

import com.ibdknox.socket_io_netty.INSIOClient;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;


public class Profilo {
	
	public static ConcurrentHashMap<Integer,Profilo> profili = new ConcurrentHashMap<Integer,Profilo>(); // dv int è ovviamente l'profilo id
	ArrayList<String> channels_id = new ArrayList<String>();
	
	int profilo_id;
	String nickname;
	String chat_key;
	
	//TODO to implement
	IntegerArray friends_online;
	IntegerArray chat_opened;
	int chat_active;
	int status;
	
	public boolean login_byChatKey(INSIOClient client, int profilo_id_from, String chatKeyEncrypted){
		
		if(chat_key.isEmpty()){
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
	
	public boolean channel_add(INSIOClient client){
		boolean result = false;
		synchronized(channels_id) {
			boolean exist = channels_id.contains(client.getSessionID());		
			if(exist == true){
				return false;
			}else{
				channels_id.add(client.getSessionID());
				result = true;
			}
		}
		
		if(result){
			PresenceHandler.clients.put(client.getSessionID(), client);
			
			PresenceHandler.addFutureListener(client, new PresenceFutureListener() {
				@Override
				public void operationComplete(INSIOClient client) {
					synchronized(channels_id) {
	            		channels_id.remove(client.getSessionID());
	            	}
	                if(channels_id.size() == 0){
	                	//TODO logout profilo
	                }
					
				}
	        });
		}
		return result;

	}
	
}
