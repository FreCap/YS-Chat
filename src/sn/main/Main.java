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

	private static PresenceServer presenceServer = null;
	
    // --- Costruttori ---------------------------------------------------------
    
    // --- Main ----------------------------------------------------------------

    /**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting PresenceServer");
		
//		Mysql.init();
		Memcached.init();
		
		setPresenceServer(new PresenceServer());
	}
    
    // --- Getter & Setter -----------------------------------------------------

	public static void setPresenceServer(PresenceServer presenceServer) {
		Main.presenceServer = presenceServer;
	}

	public static PresenceServer getPresenceServer() {
		return presenceServer;
	}

    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
