package sn.net;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * Starta il Server di Chat con Netty
 * @author NewAge
 */
public class PresenceServer {

    public final int DEFAULT_PORT = 9999;

    private int serverPort = DEFAULT_PORT;

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    
    public PresenceServer() {

        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new PresencePipelineFactory());

        try {
            bootstrap.bind(new InetSocketAddress(serverPort));
            System.out.println("PresenceServer listening on 127.0.0.1:" + serverPort);
        } catch(Exception e) {
            System.out.println("Cannot bind to 127.0.0.1:" + serverPort + ". Is there another server active?");
        }
        

        

    }
}
