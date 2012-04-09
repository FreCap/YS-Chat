package sn.thrift;


import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sn.YSConfig;
import sn.calls.Call;
import sn.voice.ServerVoice;


public class ServerVoiceToText {

	

    public static class ClientVoiceToTextHandler implements ClientVoiceToText.Iface {
	
        final Logger logger = LoggerFactory.getLogger(ClientVoiceToTextHandler.class);
            
		public ClientVoiceToTextHandler() {

		}

		@Override
		public void info(int server_id, int max_clients, String DNS, int port_TS, int port_Thrift)
				throws TException {

					logger.info("Thrift <-Voice Info " + String.valueOf(server_id) + " " + String.valueOf(max_clients) + " " + String.valueOf(DNS) + " " + String.valueOf(port_TS) + " " + String.valueOf(port_Thrift) );

			if(ServerVoice.server.containsKey(server_id)){				

			}else{

				ServerVoice server = new ServerVoice();
				server.server_id = server_id;
				server.max_clients = max_clients;
				server.DNS = DNS;
				//TODO ADD thrift's port and TS's port
				server.port_TS = port_TS;
				server.port_Thrift = port_Thrift;

				ServerVoice.server.put(server_id, server);

			}
		}

	    public boolean onClientConnected(int server_id, int clientID, String clientNick) throws TException {
			if(Call.clientNameToProfiloId.containsKey(clientNick)){
				Call.clientIdToName.put(clientID, clientNick);
				return true;
			}else{
				return false;
			}
		}

		public boolean onClientDisconnected(int server_id, int clientID) throws TException {
			return true;
		}

		
		public boolean onClientJoined(int server_id, int clientID, int ChannelID) throws TException {
			if(Call.callsIdtoName.containsKey(ChannelID)){
				if(Call.calls.containsKey(Call.callsIdtoName.get(ChannelID))){
					Call currentCall = Call.calls.get(Call.callsIdtoName.get(ChannelID));

					int profilo_id = Call.clientNameToProfiloId.get(Call.clientIdToName.get(clientID));

					if(currentCall.has_partecipante(profilo_id)){
						return true;
					}else{
						return false;
					}

				}else{
					//delete
					return false;
				}
			}
			return false;
		}


		public boolean onClientLeft(int server_id, int clientID, int ChannelID) throws TException {
			//TODO
			return true;
		}


		public boolean onChannelCreated(int server_id, int channelID, String channelName) throws TException {
			if(Call.calls.containsKey(channelName)){
				Call.calls.get(channelName).call_idN = channelID;
				Call.callsIdtoName.put(channelID, channelName);
				return true;
			}else{
				//delete
				return false;
			}
		}
 

		public boolean onChannelDeleted(int server_id, int channelID) throws TException {
			//da sconnettere ttti quanti alla fine di qst
			return true;
		}
	
    }
	
	
    public static void start(){
		
        try {	
			
            ClientVoiceToTextHandler handler = new ClientVoiceToTextHandler();
            ClientVoiceToText.Processor<ClientVoiceToTextHandler> processor = new ClientVoiceToText.Processor<ClientVoiceToTextHandler>(handler);
            TServerTransport serverTransport = new TServerSocket(YSConfig.getInstance().ThriftPort);
            //TServer server = new TSimpleServer(processor, serverTransport);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor).protocolFactory(new TBinaryProtocol.Factory(true, true)));
			
            /*
            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(YSConfig.getInstance().ThriftPort);
            //TNonblockingServer.Args args = new TNonblockingServer.Args(serverTransport);
            THsHaServer.Args args = new THsHaServer.Args(serverTransport);
            args.processor(processor);
            args.transportFactory(new TTransportFactory());
            args.protocolFactory(new TBinaryProtocol.Factory(true, true));
            //TServer server = new TThreadPoolServer(args);
                        
            //TServer server = new TNonblockingServer(args);
               TServer server = new THsHaServer(args);
                        
            System.out.println("Initialing Thrift: ServerVoiceToText");		
            server.serve();
                        
            */
                        
            System.out.println("Thrift from Voice started");
            /* 
            TServerTransport serverTransport = new TServerSocket(YSConfig.getInstance().ThriftPort);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor).protocolFactory(new TBinaryProtocol.Factory(true, true)));
            */
                        
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