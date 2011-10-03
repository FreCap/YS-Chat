package sn.main;

import db.Memcached;
import db.Mysql;
import sn.net.*;

/**
 *
 * @author Alessandro Passerini <alessandro.passerini at xelia.it>
 */
public class Main {

    // --- Costanti & Variabili private ----------------------------------------

	private static PresenceServer presenceServer = null;
	
    // --- Costruttori ---------------------------------------------------------
    
    // --- Getter & Setter -----------------------------------------------------

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting PresenceServer");
		
		Mysql.init();
		Memcached.init();
		
		setPresenceServer(new PresenceServer());
	}

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
