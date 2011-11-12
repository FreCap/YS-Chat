package sn.profilo;

import it.uniroma3.mat.extendedset.intset.FastSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import sn.store.Conversazione;

import ys.db.table.TableProfilo;
import ys.db.table.TableRelazioni;


public class Gruppo extends ProfiloModel{
	
	final public int tipo = 2;
	
	public FastSet partecipanti = new FastSet();
	
	public void message_receive(int profilo_idChat, int profilo_idFrom, String message, boolean unused){
		Profilo ProfiloTo;
		
		HashSet<Integer> profili_idsRed = new HashSet<Integer>();
		for(int profilo_idTo:friends_online.toArray()){
			if(profilo_idTo != profilo_idFrom){
				ProfiloTo = (Profilo) ProfiloModel.profili.get(profilo_idTo);
				if(ProfiloTo.chatTab_actived == profilo_idChat){
					profili_idsRed.add(profilo_id);
				}
				ProfiloTo.message_receive(profilo_id, profilo_idTo, message, true);
			}
		}
		Conversazione.write(message, profilo_id, profilo_idChat, tipo, profili_idsRed);
	}
	
	public boolean crea(int gruppo_id){
		
		ResultSet SQL_profilo = TableProfilo.get_byId(gruppo_id);
		//utente inesistente
		if(SQL_profilo == null){
			System.out.println("profilo_id Gruppo " + gruppo_id + " inesistente");
			//TODO error
			return false;
		}
		try {
			profilo_id = gruppo_id;
			nickname = SQL_profilo.getString("nickname");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
		
	}

	public void set_partecipanti(){
		
		ResultSet relazioni_SQL = TableRelazioni.get_byProfiloId(profilo_id);
		try {
			while(relazioni_SQL.next()){
				int relazione_tipo = relazioni_SQL.getInt("tipo");
				if(relazione_tipo == TIPO_AMICI){
					partecipanti.add(relazioni_SQL.getInt("profilo_id2"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void friend_remove(int profilo_id){
		super.friend_remove(profilo_id);
		if(friends_online.toArray().length == 0){
			// se è 0 -> eliminare il gruppo
		}
	}
	
	public int get_tipo(){
		return tipo;
	}
	
}
