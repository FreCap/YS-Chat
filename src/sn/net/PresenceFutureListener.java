package sn.net;

import java.util.EventListener;

import com.ibdknox.socket_io_netty.INSIOClient;

public interface PresenceFutureListener extends EventListener{

	 void operationComplete(INSIOClient client);
	
}
