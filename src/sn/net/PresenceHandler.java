package sn.net;

import java.util.concurrent.ConcurrentHashMap;

import com.ibdknox.socket_io_netty.INSIOClient;
import com.ibdknox.socket_io_netty.INSIOHandler;

import sn.net.actions.Action;

/*
 * PS: differenza fra Upstream e Downstream è sostanzialmente degli eventi che vengono applicati agli stesso, vedi sources:
 * http://docs.jboss.org/netty/3.2/xref/org/jboss/netty/channel/SimpleChannelUpstreamHandler.html // lettura
 * http://docs.jboss.org/netty/3.2/xref/org/jboss/netty/channel/SimpleChannelDownstreamHandler.html // scrittura
 */
public class PresenceHandler implements INSIOHandler  {

    public static final int MAX_UNITS = 25;
	public static ConcurrentHashMap<String,INSIOClient> clients = new ConcurrentHashMap<String,INSIOClient>(); // dv int è ovviamente l'profilo id

	@Override
	public void OnConnect(INSIOClient client) {
		System.out.println("A user connected :: " + client.getSessionID());	
	}

	@Override
	public void OnDisconnect(INSIOClient client) {
		System.out.println("A user disconnected :: " + client.getSessionID() + " :: hope it was fun");	
	}

	@Override
	public void OnMessage(INSIOClient client, String message) {
		 	     
	     System.out.println("<==" + message);
	   
	     Action.parseFromString(message, client);
	}

	@Override
	public void OnShutdown() {
		
	}
    
    
    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

    
}
