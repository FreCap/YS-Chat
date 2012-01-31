package sn.voice;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import sn.thrift.ClientTextToVoice;

public class ServerVoice {

	public static ConcurrentHashMap<Integer,ServerVoice> server = new ConcurrentHashMap<Integer,ServerVoice>();

	public int server_id;
	public int max_clients;
	public String DNS;
	public int port_TS;
        public int port_Thrift;        

	public int connected_clients;
	
	public static int get_server_withlessWorkLoad(){
		
		//TODO
		return 1;		
	}
	
	private  ClientTextToVoice.Client openClient() throws TException{
		
            ClientTextToVoice.Client client = null;
		
            TTransport transport;
		     
            transport = new TSocket(DNS, port_Thrift);
            transport.open();
		     
            TProtocol protocol = new  TBinaryProtocol(transport);
            client = new ClientTextToVoice.Client(protocol);  
	
            return client;
	
        }
	
	private void closeClient(ClientTextToVoice.Client client){
	    
            client.getInputProtocol().getTransport().close();
            client.getOutputProtocol().getTransport().close(); 
	    
	}
        
        public boolean new_channel(String call_id, String channel_pasword){
            
            try {
                
                ClientTextToVoice.Client client = openClient();
            
				
				int channel_id = client.channelCreate(server_id, call_id, channel_pasword);
                if(channel_id > 0){
                    //TODO
                    closeClient(client);
                    return true;
                    
                }else{
                    
                    closeClient(client);
                    return false;
                
                }
                            
            } catch (TException x) {
	   	x.printStackTrace();
            }     
            return false;
        }
        
         /**
         * una volta che un client si connette, il calback del server chiede se quel client si può attaccare
         * a quel determinato channel e il java risponde con un bool
         * @deprecated
         **/
         public void add_client(String channel_name, String client_name){
            
            try {
                
                ClientTextToVoice.Client client = openClient();
            
                //old client.add_client(server_id, channel_name, client_name);
                
                closeClient(client);
            
            } catch (TException x) {
	   	x.printStackTrace();
            }     
            
        }
        
	
	
}
