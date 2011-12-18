package sn.calls;

import it.uniroma3.mat.extendedset.intset.FastSet;

import java.util.concurrent.ConcurrentHashMap;

import sn.profilo.MultiModel;
import sn.profilo.Profilo;
import sn.profilo.ProfiloModel;
import sn.store.Conversazione;
import sn.util.RandomHash;
import sn.util.SecureHash;

public class Call {
	
	//l'id della map è fatto da Conversazioni.get_conversazione_id()
	public static ConcurrentHashMap<String,Call> calls = new ConcurrentHashMap<String,Call>();
	
	public String call_id;	
	public int caller_id;
	public int called_id;
	
	public FastSet partecipanti_attivi = new FastSet();
	
	public Call(ProfiloModel caller_model, ProfiloModel called_model){
		
		Profilo caller = (Profilo) caller_model;
		
		int tipo_called = called_model.get_tipo();
		
		if(tipo_called == Conversazione.TIPO_profilo2profilo){
			
			Profilo called = (Profilo) called_model;
			
			if(caller.friends_online.contains(called.profilo_id)){
			
				String random_string = "call_id" + (System.currentTimeMillis()/1000) + RandomHash.one();
				call_id = SecureHash.Md5(random_string);	
				
				if(!called.channel_id_connectedVoiceSupport.isEmpty()){
					
					caller.call_wait(caller.profilo_id, call_id);	
					
					caller_id = caller.profilo_id;
					called_id = called.profilo_id;
	
					calls.put(Conversazione.get_id_conversazione(caller_id, called_id, called.get_tipo()), this);
					
				}else{
					caller.call_notSupportedBy(called.profilo_id);
				}
	
				called.call_ring(caller.profilo_id, caller.profilo_id, call_id);
			
			}else{
				
				//TODO utente chiamato non online
				
			}
			
		}else{
			
			MultiModel called = (MultiModel) called_model;
			
			caller.call_wait(caller.profilo_id, call_id);	

			caller_id = caller.profilo_id;
			called_id = called.profilo_id;
			
			// se ci sono utenti connessi restituisce true
			if(called.call_ring(called_id, caller_id, call_id)){
				
				caller.call_wait(caller.profilo_id, call_id);	
				calls.put(Conversazione.get_id_conversazione(caller_id, called_id, called.get_tipo()), this);

			}else{
				
				//TODO nessun utente del gruppo/party online
				
			}
			
		}	
	}
	
	
	
	
}
