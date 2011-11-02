package sn.main;

import sn.net.*;
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
