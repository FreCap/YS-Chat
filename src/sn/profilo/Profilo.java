package sn.profilo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import sn.net.PresenceHandler;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

import db.table.TableProfilo;

public class Profilo {
	/**
	 * TODO forse tutto dovrebbe essere dentro una ConcurrentHashMap, in modo da consentire l'accesso simultaneo
	 * e tutti i metodi, o quasi, dovrebbero essere static, con un argomento in più per l'id del profilo
	 */

	public static ConcurrentHashMap<Integer,Profilo> profili = new ConcurrentHashMap<Integer,Profilo>(); // dv int Ã¨ ovviamente l'profilo id
	IntegerArray channels_id = new IntegerArray(); // i channel appartenenti all'profilo
	int[] friends_online;
	int[] chat_opened;
	int chat_active;
	String nickname;
	String chat_key;
	int profilo_id;
	int status;
	
	public void login(Channel channel, int account_id, String chatKey){
		ResultSet SQL_profilo = TableProfilo.get_byId(account_id);
		
		//utente inesistente
		if(SQL_profilo == null){
			//TODO
		}
		try {
			// chat key errata
			if(SQL_profilo.getString("chat_key") != chatKey){
				//TODO
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//success
		channel_add(channel);
	}
	
	public boolean channel_add(Channel channel){
		int exist = channels_id.indexOf(channel.getId());		
		if(exist > 0){
			return false;
		}else{
			channels_id.addNew(channel.getId());
			channels_id.sort();
			
			PresenceHandler.channels.find(channel.getId()).getCloseFuture().addListener(new ChannelFutureListener() {
	            public void operationComplete(ChannelFuture future) {
	            	channels_id.pop(future.getChannel().getId());
	                if(channels_id.toIntArray().length == 0){
	                	//logout profilo
	                }
	            }
	        });
			
			return true;
		}
		
	}	
	
}
