package sn.net;

import java.util.ArrayList;
import java.util.List;
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
	
	private static ConcurrentHashMap<String,List<PresenceFutureListener>> listeners = new ConcurrentHashMap<String, List<PresenceFutureListener>>(20000);
	
	@Override
	public void OnConnect(INSIOClient client) {
		System.out.println("A user connected :: " + client.getSessionID());	
	}

	@Override
	public void OnDisconnect(INSIOClient client) {
		System.out.println("A user disconnected :: " + client.getSessionID() + " :: hope it was fun");	
		notifyListeners(client);
	}

	@Override
	public void OnMessage(INSIOClient client, String message) {
	     System.out.println("<==" + message);
	     Action.parseFromString(message, client);
	}
	

	@Override
	public void OnShutdown() {
		
	}
	
	public static void addFutureListener(INSIOClient client, PresenceFutureListener futureListener) {
        if (futureListener == null) {
            throw new NullPointerException("listener");
        }       
        
        if (!listeners.contains(client.getSessionID())) {
        	ArrayList<PresenceFutureListener> listenersList = new ArrayList<PresenceFutureListener>(1);
        	listeners.put(client.getSessionID(), listenersList);
		}
		listeners.get(client.getSessionID()).add(futureListener);
	}

	 public static void notifyListeners(INSIOClient client) {
		if (listeners.containsKey(client.getSessionID())) {
			for (PresenceFutureListener l: listeners.get(client.getSessionID())) {
				l.operationComplete(client);
			}	
			listeners.remove(client.getSessionID());
		}
	}
	
	// --- Metodi public -------------------------------------------------------
	
	// --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

    
}
