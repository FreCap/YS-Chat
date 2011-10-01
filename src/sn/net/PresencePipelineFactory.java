package sn.net;

//serve per la funzione pipeline()
import static org.jboss.netty.channel.Channels.*;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.string.*;
import org.jboss.netty.handler.codec.frame.*;


public class PresencePipelineFactory implements ChannelPipelineFactory {

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		
		ChannelPipeline pipeline = pipeline();
		
		// si occupa della gestione del buffer e di spezzare il messaggio quando incontra il determinato carattere o super gli 8KB
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
		                   8192, Delimiters.lineDelimiter()));
		
		// Finché non si stabilisce se e come creare il protocollo per la nostra app, usiamo semplici stinghe. PS: sarebbe meglio un codice binario, tanto sono pochi tipi di informazione
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());
		 
		// si occupa degli eventi dei socket 
		pipeline.addLast("handler", new PresenceHandler());
		   
		return pipeline;
	}

}
