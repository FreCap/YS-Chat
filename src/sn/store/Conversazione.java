package sn.store;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Pipeline;
import ys.db.Redis;


public class Conversazione {
	
    public static final int TIPO_profilo2profilo = 1;
    public static final int TIPO_profilo2gruppo = 2;
    public static final int TIPO_profilo2party = 3;
    
    String id;
    
    public static final String CONV_MESSAGGI_LOST = "cnvlost"; // hash
    public static final String CONV_MESSAGGI = "cnvmsg"; // list
    public static final String CONV_PARTECIPANTI = "cnvp"; // set
    public static final String CONV_N_MSG = "cnvn";// increment

    public static final String PROFILO_CONVERSAZIONI = "pcnv";//scored set
    public static final String PROFILO_N_CNV_LOST = "pcnvl";//increment

    
    
	public static void write(String message, Integer sender, Integer receiver, Integer tipo, HashSet<Integer> profili_idsRed){
	
		String id_conversazione = get_id_conversazione(sender, receiver, tipo);
				
		String message_redis = "{m:\"" + message + "\",s:\"" + Integer.toString(sender) + "\"}";
				
		Redis.DB.rpush(CONV_MESSAGGI + id_conversazione, message_redis);
	
		//int n_messages = (int) Memcached.MemCached.addOrIncr("conversazioni:" + id_conversazione + ":n_messages");
		
		Integer n_messaggio = Redis.DB.incr(CONV_N_MSG + id_conversazione).intValue();
				
		if(tipo == TIPO_profilo2profilo){
					
			Redis.DB.hdel(CONV_MESSAGGI_LOST + id_conversazione, sender.toString()).intValue();
			boolean exist = Redis.DB.hexists(CONV_MESSAGGI_LOST + id_conversazione, receiver.toString());
			if(profili_idsRed.isEmpty() && exist == false){
				Redis.DB.hset(CONV_MESSAGGI_LOST + id_conversazione,receiver.toString(),n_messaggio.toString());
			}
					
		}else{

			Set<String> profili_ids = null;
			if(tipo == TIPO_profilo2gruppo){
				
				// TODO to implement get friends list
						
			}else if(tipo == TIPO_profilo2party){
						
				profili_ids = Redis.DB.smembers(CONV_PARTECIPANTI + id_conversazione);
						
			}
					
			profili_ids.removeAll(profili_idsRed);
			profili_ids.remove(sender.toString());
			Pipeline p = Redis.DB.pipelined();
				
			p.hdel(CONV_MESSAGGI_LOST + id_conversazione, sender.toString());
			int queries_done = 1;
			for (Integer profilo_id: profili_idsRed) {
				p.hdel(CONV_MESSAGGI_LOST + id_conversazione, profilo_id.toString());
				queries_done++;
			}
			for (String profilo_id : profili_ids) {
				p.hexists(CONV_MESSAGGI_LOST + id_conversazione, profilo_id);
			}
					
			List result = p.syncAndReturnAll();
					
			Integer time = 0;
					
			//se è 0 vuol dire che devo fare -1 ai msg persi di sender
			if((Long) result.get(0) == 1){
				p.decr(PROFILO_N_CNV_LOST + sender.toString());
			}
			
			for (String profilo_id : profili_ids) {
				//se non � ancora stato settato che � stato perso un msg
				if(false == (Boolean)result.get(queries_done)){
					p.hset(CONV_MESSAGGI_LOST + id_conversazione, profilo_id, n_messaggio.toString());
					p.incr(PROFILO_N_CNV_LOST + profilo_id);
				}
				queries_done++;
				p.zadd(PROFILO_CONVERSAZIONI + profilo_id, time, id_conversazione);
			}
			queries_done = 1;
			for (Integer profilo_id: profili_idsRed) {
				p.zadd(PROFILO_CONVERSAZIONI + profilo_id.toString(), time, id_conversazione);
				if(0 == (Long)result.get(queries_done)){
					p.decr(PROFILO_N_CNV_LOST + profilo_id.toString());
				}
				queries_done++;
			}
			p.zadd(PROFILO_CONVERSAZIONI + sender.toString(), time, id_conversazione);
							
			p.sync();
		}
	}
		
	public static void conversazione_letta(Integer sender, Integer receiver, Integer tipo){
		
		String id_conversazione = get_id_conversazione(sender, receiver, tipo);
		
		Pipeline p = Redis.DB.pipelined();
		
		p.hdel(CONV_MESSAGGI_LOST + id_conversazione, sender.toString());
		p.decr(PROFILO_N_CNV_LOST + sender.toString());
	
		p.sync();
	}
	
	public static String get_id_conversazione(Integer sender, Integer receiver, int tipo){
		switch (tipo) {
			case 1://profilo2profilo
				if(sender > receiver){
					return receiver.toString() + "_" + sender.toString();
				}else{
					return sender.toString() + "_" + receiver.toString();
				}
			case 2://profilo2gruppo
				return "g_" + receiver.toString();
			case 3://profilo2party
				return "p_" + receiver.toString();
			default:// in teoria nn ci dovrebbe essere...
				return "";
		}
		
	}
}
