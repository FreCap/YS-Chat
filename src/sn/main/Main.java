package sn.main;

import redis.clients.jedis.Jedis;
import sn.net.*;
import sn.profilo.Party;
import sn.store.Conversazione;
import ys.db.Memcached;
import ys.db.Mysql;
import ys.db.Redis;

/**
 * @author Francesco Capponi <capponi.francesco at gmail.com>
 * @author Alessandro Passerini <alessandro.passerini at xelia.it>
 */
public class Main {

    // --- Costanti & Variabili private ----------------------------------------
	
    // --- Costruttori ---------------------------------------------------------
    
    // --- Main ----------------------------------------------------------------

    /**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println("Starting PresenceServer");
		
        PresenceServer presenceServer = null;
		
		Mysql.init();
		Memcached.init();
        Redis.init();
		

      Jedis DB = Redis.DBPool.getResource();
		
      DB.set(Conversazione.CONV_INCREMENT, ((Integer)Party.PARTY_IDSTART).toString());
		
      
      Redis.DBPool.returnResource(DB);
      
      presenceServer = new PresenceServer();
        if (!presenceServer.start()) {
            // Qualcosa è andato storto.. forzo la chiusura.
            Runtime.getRuntime().exit(1);
        }
	}
    
    // --- Getter & Setter -----------------------------------------------------

    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
