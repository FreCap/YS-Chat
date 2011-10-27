package sn.main;

import sn.db.Memcached;
import sn.db.Mysql;
import sn.net.*;

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
