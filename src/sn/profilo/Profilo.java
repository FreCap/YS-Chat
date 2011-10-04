package sn.profilo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import sn.db.table.TableProfilo;
import sn.net.PresenceHandler;
import sn.net.actions.ActionConnect;
import sn.util.SecureHash;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;


public class Profilo {
	
	public static ConcurrentHashMap<Integer,Profilo> profili = new ConcurrentHashMap<Integer,Profilo>(); // dv int è ovviamente l'profilo id
	IntegerArray channels_id = new IntegerArray(); // i channel appartenenti all'profilo
	IntegerArray friends_online;
	IntegerArray chat_opened;
	int chat_active;
	String nickname;
	String chat_key;
	int profilo_id;
	int status;
	
	public boolean login_byChatKey(Channel channel, int account_id, String chatKeyEncrypted){
		ResultSet SQL_profilo = TableProfilo.get_byId(account_id);
		
		//utente inesistente
		if(SQL_profilo == null){
			//TODO error
		}
		try {
			String Hash_fromDB = SecureHash.Md5(SQL_profilo.getString("chat_key")+ActionConnect.salts.get(channel.getId()));
			
			// chat key errata
			if(Hash_fromDB != chatKeyEncrypted){
				//TODO error
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//success
		ActionConnect.salts.remove(channel.getId());
		channel_add(channel);
		return true;
	}
	
	public boolean channel_add(Channel channel){
		boolean result = false;
		synchronized(channels_id) {
			int exist = channels_id.indexOf(channel.getId());		
			if(exist > 0){
				return false;
			}else{
				channels_id.addNew(channel.getId());
				channels_id.sort();
				
				result = true;
				
			}
		}
		
		if(result){
			PresenceHandler.channels.find(channel.getId()).getCloseFuture().addListener(new ChannelFutureListener() {
	            public void operationComplete(ChannelFuture future) {
	            	synchronized(channels_id) {
	            		channels_id.pop(future.getChannel().getId());
	            	}
	                if(channels_id.toIntArray().length == 0){
	                	//logout profilo
	                }
	            }
	        });
		}
		
		return result;
		
	}	
	
}
