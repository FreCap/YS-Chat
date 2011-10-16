package sn.profilo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import sn.db.table.TableProfilo;
import sn.net.PresenceHandler;
import sn.net.actions.ActionConnect;
import sn.util.SecureHash;

import com.ibdknox.socket_io_netty.INSIOClient;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;


public class Profilo {
	
	public static ConcurrentHashMap<Integer,Profilo> profili = new ConcurrentHashMap<Integer,Profilo>(); // dv int è ovviamente l'profilo id
	ArrayList<String> channels_id = new ArrayList<String>();
	IntegerArray friends_online;
	IntegerArray chat_opened;
	int chat_active;
	String nickname;
	String chat_key;
	int profilo_id;
	int status;
	
	public boolean login_byChatKey(INSIOClient client, int account_id, String chatKeyEncrypted){
		ResultSet SQL_profilo = TableProfilo.get_byId(account_id);
		
		//utente inesistente
		if(SQL_profilo == null){
			//TODO error
			return false;
		}
		try {
			String Hash_fromDB = SecureHash.Md5(SQL_profilo.getString("chat_key")+ActionConnect.salts.get(client.getSessionID()));
			
			// chat key errata
			if(Hash_fromDB != chatKeyEncrypted){
				//TODO error
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
			
			/*client.getCloseFuture().addListener(new ChannelFutureListener() {//TODO, visto che adesso si usa socket.io, bisogna usare le sue api
	            public void operationComplete(ChannelFuture future) {
	            	synchronized(channels_id) {
	            		channels_id.pop(future.getChannel().getId());
	            	}
	                if(channels_id.toIntArray().length == 0){
	                	//logout profilo
	                }
	            }
	        });*/
		}
		
		return result;
		
	}	
	
}
