package sn.net;

import com.ibdknox.socket_io_netty.NSIOServer;

/**
 * Starta il Server di Chat con Netty
 * @author NewAge
 */
public class PresenceServer {

    // --- Costanti & Variabili private -----------------------------------------------

    public final int DEFAULT_PORT = 9999;

    private int serverPort = DEFAULT_PORT;
	
    // --- Costruttori ---------------------------------------------------------

    public PresenceServer() {
               
        NSIOServer bootstrap = new NSIOServer(new PresenceHandler(), serverPort);

        try {
        	bootstrap.start();
            System.out.println("PresenceServer listening on 127.0.0.1:" + serverPort);
        } catch(Exception e) {
            System.out.println("Cannot bind to 127.0.0.1:" + serverPort + ". Is there another server active?");
        }
    }

    // --- Getter & Setter -----------------------------------------------------
    
    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    // --- Metodi public -------------------------------------------------------
    
    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
