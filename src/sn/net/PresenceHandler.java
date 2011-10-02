package sn.net;

import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.*;

import sn.net.actions.Action;

/*
 * PS: differenza fra Upstream e Downstream è sostanzialmente degli eventi che vengono applicati agli stesso, vedi sources:
 * http://docs.jboss.org/netty/3.2/xref/org/jboss/netty/channel/SimpleChannelUpstreamHandler.html // lettura
 * http://docs.jboss.org/netty/3.2/xref/org/jboss/netty/channel/SimpleChannelDownstreamHandler.html // scrittura
 */
public class PresenceHandler extends SimpleChannelUpstreamHandler {

    public static final int MAX_UNITS = 25;
    public static final ChannelGroup channels = new DefaultChannelGroup();

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        System.out.println("Connect " + e.getChannel().getId());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
            ChannelStateEvent e) throws Exception {
        System.out.println("Disconnect " + e.getChannel().getId());
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        super.messageReceived(ctx, e);

        String messageText = (String) e.getMessage();
        Channel ch = e.getChannel();
        System.out.println("<==" + ch.write(e.getMessage()));

        Action action = Action.parseFromString(messageText, e.getChannel());

//        String[] splitted = messageText.split(" ", 2);
//
//        int azione = Integer.parseInt(splitted[0]);
//        String value = splitted[1];
//
//        switch (azione) {
//            case ChatMessageLogin.TAG:
//                account account = new account();
//                data = value.split(" ");
//                account.login(e.getChannel(), data[0], data[1]);
//
//                Channel ch = e.getChannel();
//                ch.write(e.getMessage());
//
//                break;
//
//        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
    }
    
    // --- Private methods -----------------------------------------------------
    
    
}
