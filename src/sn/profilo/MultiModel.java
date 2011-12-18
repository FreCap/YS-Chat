package sn.profilo;

import java.util.HashSet;

import sn.store.Conversazione;
import it.uniroma3.mat.extendedset.intset.FastSet;

public abstract class MultiModel extends ProfiloModel {

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
				ProfiloTo.message_receive(profilo_idChat, profilo_idFrom, message, true);
			}
		}
		Conversazione.write(message, profilo_idChat, profilo_id, tipo, profili_idsRed);
	}
	
	
	public boolean call_ring(int conv_id, int caller_id, String call_id){
		// REMEMBER conv_id è ovviamente uguale a profilo_id
		Profilo ProfiloTo;
		
		if(friends_online.isEmpty()){
			return false;
		}
		for(int profilo_idTo:friends_online.toArray()){ 
			if(profilo_idTo != caller_id){
				ProfiloTo = (Profilo) ProfiloModel.profili.get(profilo_idTo);
				ProfiloTo.call_ring(conv_id, caller_id, call_id);
			}
		}
		
		return true;
		
	}
	
}
