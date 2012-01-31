package sn.calls;

import it.uniroma3.mat.extendedset.intset.FastSet;

import java.util.concurrent.ConcurrentHashMap;

import sn.profilo.MultiModel;
import sn.profilo.Profilo;
import sn.profilo.ProfiloModel;
import sn.store.Conversazione;
import sn.util.RandomHash;
import sn.util.SecureHash;
import sn.voice.ServerVoice;

public class Call {
	
	//l'id della map è fatto da Conversazioni.get_conversazione_id()
	public static ConcurrentHashMap<String,Call> calls = new ConcurrentHashMap<String,Call>();
	
	public String call_id;	
	public String call_password;
	public int caller_id;
	public int called_id;
	
        
	public FastSet partecipanti_attivi = new FastSet();
	
        public int server_id;
	public int iniziata_time;
	
        public int get_tipoCalled(){
            
            return ProfiloModel.profili.get(called_id).get_tipo();
            
        }
        
	public Call(ProfiloModel caller_model, ProfiloModel called_model){
		
		Profilo caller = (Profilo) caller_model;
		
		int tipo_called = called_model.get_tipo();
		
		if(tipo_called == Conversazione.TIPO_profilo2profilo){
			
			Profilo called = (Profilo) called_model;
			for(int a:called.friends_online.toArray()){
                            System.out.println(a);
                            
                        }
			if(caller.friends_online.contains(called.profilo_id)){
			
				String random_string = "call_id" + (System.currentTimeMillis()/1000) + RandomHash.one();
				call_id = SecureHash.Md5(random_string);	                               
                                
				if(caller.channel_id_connectedVoiceSupport.isEmpty()){
                                    
                                    caller.call_notSupportedBy(caller.profilo_id);
                                
                                }else if(called.channel_id_connectedVoiceSupport.isEmpty()){
                                    
                                    caller.call_notSupportedBy(called.profilo_id);
                                
                                }else{
					caller.call_wait(caller.profilo_id, call_id);	
					
					caller_id = caller.profilo_id;
					called_id = called.profilo_id;
	
					calls.put(Conversazione.get_id_conversazione(caller_id, called_id, called.get_tipo()), this);
					
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
			
                        if(!caller.channel_id_connectedVoiceSupport.isEmpty()){
                       
                            // se ci sono utenti connessi restituisce true
                            if(called.call_ring(called_id, caller_id, call_id)){

                                    caller.call_wait(caller.profilo_id, call_id);	
                                    calls.put(Conversazione.get_id_conversazione(caller_id, called_id, called.get_tipo()), this);

                            }else{

                                    //TODO nessun utente del gruppo/party online

                            }
			
                        }else{
                            
                             caller.call_notSupportedBy(caller.profilo_id);
                        
                        }
		}	
	}
        
	public void init_TS(){
		
		if(!(iniziata_time>100)){
                   
                    iniziata_time = ((Long) (System.currentTimeMillis()/1000)).intValue();
                    
                    server_id = ServerVoice.get_server_withlessWorkLoad();
                    
                    ServerVoice.server.get(server_id).new_channel(call_id, call_password);
                  
                    call_accepted(caller_id);
                    
		}	
		
	}
	
	public void call_accepted(int profilo_id){
		
            init_TS();
            
            Profilo profilo = (Profilo) ProfiloModel.profili.get(profilo_id);
            
            ServerVoice.server.get(server_id).add_client(call_id, get_clientName_byProfilo(profilo));
            
            profilo.call(this);
            
	}
	
        
        public static String clientNameSalt = "7ahFA8((2ABfp";
        
        public String get_clientName_byProfilo (Profilo profilo){
            
            return SecureHash.Md5(profilo.profilo_id + clientNameSalt);
            
        }
        
        public int get_TSPort(){
            
           return ServerVoice.server.get(server_id).port_TS;
 
        }
	
}
