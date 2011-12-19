package sn.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import sn.voice.ServerVoice;

public class ServerVoiceToText {

	
	public static class ClientVoiceToTextHandler implements ClientVoiceToText.Iface {
	
		public ClientVoiceToTextHandler() {
		
		}
	
		@Override
		public void info(int server_id, int max_clients, String DNS, int port)
				throws TException {
			
			if(ServerVoice.server.containsKey(server_id)){				
				
			}else{
				
				ServerVoice server = new ServerVoice();
				server.server_id = server_id;
				server.max_clients = max_clients;
				server.DNS = DNS;
				//TODO ADD thrift's port and TS's port
				server.port = port;				
				ServerVoice.server.put(server_id, server);
				
			}
		}
	
	}
	
	
	public static void start(){
		
		try {	
			
			ClientVoiceToTextHandler handler = new ClientVoiceToTextHandler();
			ClientVoiceToText.Processor<ClientVoiceToTextHandler> processor = new ClientVoiceToText.Processor<ClientVoiceToTextHandler>(handler);
			TServerTransport serverTransport = new TServerSocket(9090);
			//TServer server = new TSimpleServer(processor, serverTransport);
			
			TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
			
			System.out.println("Initialing Thrift: ServerVoiceToText");		
			server.serve();
			
		} catch (Exception x) {
			x.printStackTrace();
		}
		
	}
	
	public static void init() {
		
		Runnable s = new Runnable() {
			public void run() {
				start();
			}
		};      
		     
		new Thread(s).start();
		
	}

}