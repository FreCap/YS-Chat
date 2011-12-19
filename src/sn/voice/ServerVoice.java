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
	public int port;

	public int connected_clients;
	
	public static int get_server_withlessWorkLoad(){
		
		//TODO
		return 1;		
	}
	
	private  ClientTextToVoice.Client openClient(){
		
		ClientTextToVoice.Client client = null;
		
		try {
	     
			 TTransport transport;
		     
		     transport = new TSocket("localhost", 9090);
		     transport.open();
		     
		     TProtocol protocol = new  TBinaryProtocol(transport);
		     client = new ClientTextToVoice.Client(protocol);  
	     
		 } catch (TException x) {
		    	x.printStackTrace();
		 }
		 
		 return client;
	}
	
	private void closeClient(ClientTextToVoice.Client client){
	    
		client.getInputProtocol().getTransport().close();
		client.getOutputProtocol().getTransport().close(); 
	    
	}
	
	
}
