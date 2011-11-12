package sn.profilo;

import it.uniroma3.mat.extendedset.intset.FastSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import sn.store.Conversazione;
import ys.db.Redis;
import ys.db.table.TableRelazioni;


public class Party extends ProfiloModel{
	
	final public static int PARTY_IDSTART = 50000000;
	
	final public int tipo = 3;
	
	public String nickname = "";
	
	public FastSet partecipanti = new FastSet();

	public Party(int profilo_idCreatore, HashSet<Integer> profili_ids) {
		crea(profilo_idCreatore, profili_ids);
	}
	
	public Party() {

	}
	
	public boolean load(int party_id){
		
		Jedis DB = Redis.DBPool.getResource();
		
		Pipeline p = DB.pipelined();

		profilo_id = party_id;
		
		String conversazione_id = Conversazione.get_id_conversazione(party_id, party_id, tipo);
		
		p.get(Conversazione.CONV_NOME + conversazione_id);
		p.smembers(Conversazione.CONV_PARTECIPANTI + conversazione_id);
		
		List responso = p.syncAndReturnAll();
		
		Redis.DBPool.returnResource(DB);
		
		if(responso.get(0) != null){
			nickname = (String) responso.get(0);
		}
		boolean listEmpty = true;
		for (String profilo_idString : (List<String>) responso.get(1)) {
			partecipanti.add(Integer.valueOf(profilo_idString));
			profili.get(Integer.valueOf(profilo_idString)).friend_add(profilo_id);
			friend_add(Integer.valueOf(profilo_idString));
			listEmpty = false;
		}
		
		return listEmpty;
		
	}
	
	public void crea(int profilo_idCreatore, HashSet<Integer> profili_ids){
		
		Jedis DB = Redis.DBPool.getResource();
		
		profilo_id = ((Long)DB.incr(Conversazione.CONV_INCREMENT)).intValue();
		
		add_friend(profilo_idCreatore, profili_ids, true);		
				
		Redis.DBPool.returnResource(DB);
		
	}

	public void add_friend(Integer profilo_idAdder, HashSet<Integer> profili_idToAdd, boolean isNew){
		Jedis DB = Redis.DBPool.getResource();
		Pipeline p = DB.pipelined();
		
		if(isNew == true){
			profili.get(profilo_idAdder).friend_add(profilo_id);
			friend_add(profilo_idAdder);
			partecipante_add(profilo_idAdder);
			p.sadd(Conversazione.CONV_PARTECIPANTI + ((Integer)profilo_id).toString(), profilo_idAdder.toString());
			p.sadd(Conversazione.PROFILO_PARTY + profilo_idAdder.toString(), ((Integer)profilo_id).toString());
		}
		
		ResultSet relazioni_SQL = TableRelazioni.get_byProfiloId(profilo_idAdder);
		try {
			int profilo_id2;
			while(relazioni_SQL.next()){
				int relazione_tipo = relazioni_SQL.getInt("tipo");
				if(relazione_tipo == TIPO_AMICI){
					profilo_id2 = relazioni_SQL.getInt("profilo_id2"); 
					if(profili_idToAdd.contains(relazioni_SQL.getInt("profilo_id2"))){
						if(profili.containsKey(profilo_id2)){
							profili.get(profilo_id2).friend_add(profilo_id);
							friend_add(profilo_id2);
						}
						partecipante_add(profilo_id2);
						p.sadd(Conversazione.CONV_PARTECIPANTI + ((Integer)profilo_id).toString(), relazioni_SQL.getString("profilo_id2"));
						p.sadd(Conversazione.PROFILO_PARTY + relazioni_SQL.getString("profilo_id2"), ((Integer)profilo_id).toString());
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Redis.DBPool.returnResource(DB);
	}

	
	public void add_friend(int profilo_idAdder, int profilo_idToAdd){
		
		HashSet<Integer> profili_id = new HashSet<Integer>();
		profili_id.add(profilo_idToAdd);
		add_friend(profilo_idAdder, profili_id, false);
		
	}
	
	public void partecipante_add(int profilo_id){
		synchronized (partecipanti) {
			partecipanti.add(profilo_id);
		}
	}
	
	public void partecipante_remove(int profilo_id){
		synchronized (partecipanti) {
			partecipanti.remove(profilo_id);
		}
	}
	
	public void message_receive(int profilo_idChat, int profilo_idFrom, String message, boolean unused){
		Profilo ProfiloTo;
		
		HashSet<Integer> profili_idsRed = new HashSet<Integer>();
		for(int profilo_idTo:friends_online.toArray()){ System.out.println("parteciapnti: "+profilo_idTo);
			if(profilo_idTo != profilo_idFrom){
				ProfiloTo = (Profilo) ProfiloModel.profili.get(profilo_idTo);
				if(ProfiloTo.chatTab_actived == profilo_idChat){
					profili_idsRed.add(profilo_id);
				}
				ProfiloTo.message_receive(profilo_idChat, profilo_idFrom, message, true);
			}
		}
		Conversazione.write(message, profilo_idChat, profilo_id, tipo, profili_idsRed);
	}
	

	public int get_tipo(){
		return tipo;
	}
	
}
