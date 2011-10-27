package sn.net;

import com.ibdknox.socket_io_netty.NSIOServer;

import org.jboss.netty.channel.ChannelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starta il Server di Chat con Netty
 * @author NewAge
 */
public class PresenceServer {

    // --- Costanti & Variabili private ----------------------------------------

    private static Logger logger = LoggerFactory.getLogger(PresenceServer.class);

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
        
        try {
            bootstrap = new NSIOServer(new PresenceHandler(), serverPort);
            bootstrap.start();
        } catch (ChannelException ep) {
            logger.error("Error bootstraping server.");
            logger.error("The server reports: " + ep.getMessage());
            return false;
        } catch (RuntimeException ep) {
            logger.error("An unknown runtime  exception occured: " + ep.getMessage());
            return false;
        } catch (Exception ep) {
            logger.error("An unknown exception occured: " + ep.getMessage());
            return false;
        } 

        if (bootstrap.isRunning()) {
            if (logger.isInfoEnabled()) {
                logger.info("PresenceServer listening on 127.0.0.1:" + serverPort);
            }
            return true;
        } else {
            if (logger.isErrorEnabled()) {
                logger.error("Error bootstraping server.");
            }
            bootstrap.stop();
            bootstrap = null;
            return false;
        }
    }
    

    // --- Metodi protected ----------------------------------------------------

    // --- Metodi private ------------------------------------------------------

}
