package sn.profilo;
import it.uniroma3.mat.extendedset.intset.FastSet;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibdknox.socket_io_netty.INSIOClient;

public abstract class ProfiloModel {
	
	public static ConcurrentHashMap<String,Integer> sessionID2profiloID = new ConcurrentHashMap<String, Integer>();
	public static ConcurrentHashMap<Integer,ProfiloModel> profili = new ConcurrentHashMap<Integer,ProfiloModel>(); // dv int è ovviamente l'profilo id
	
	public int profilo_id;
	public String nickname;
	public int tipo;
	public FastSet friends_online = new FastSet();
	
	final Logger logger = LoggerFactory.getLogger(ProfiloModel.class);
	
	//TODO to implement	
	int status;
	
	public static final int TIPO_AMICI = 1;
	public static final int TIPO_ISCRITTO = 3;
	
	public void message_send(int profilo_idTo, String message, INSIOClient client){
		
	}
	
	public void message_receive(int profilo_idChat, int profilo_idFrom, String message, boolean already_stored){
		
	}
	
	public void friend_add(int profilo_id){
		synchronized (friends_online) {
			friends_online.add(profilo_id);
		}
	}
	
	public void friend_remove(int profilo_id){
		synchronized (friends_online) {
			friends_online.remove(profilo_id);
		}
	}
	
	public int get_tipo(){
		return tipo;
	}
	
}
