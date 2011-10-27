package sn.net;

import com.ibdknox.socket_io_netty.NSIOServer;
import org.jboss.netty.channel.ChannelException;

/**
 * Starta il Server di Chat con Netty
 * @author NewAge
 */
public class PresenceServer {

    // --- Costanti & Variabili private ----------------------------------------

    public final int DEFAULT_PORT = 9999;

    private int serverPort = DEFAULT_PORT;
	
    private NSIOServer bootstrap =null;
    
    // --- Costruttori ---------------------------------------------------------

    public PresenceServer() {
        


    }

    // --- Getter & Setter -----------------------------------------------------
    
    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    // --- Metodi public -------------------------------------------------------
    
    public boolean start() {
        bootstrap = new NSIOServer(new PresenceHandler(), serverPort);
        try {
            bootstrap.start();
        } catch (Exception ep) {
            System.out.println("Error bootstraping server.");
            System.out.println("The server reports: " + ep.getMessage());
            return false;
        } 

        System.out.println("PresenceServer listening on 127.0.0.1:" + serverPort);
        return true;
    }
    

    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
