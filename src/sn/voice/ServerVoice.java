package sn.voice;

import java.util.concurrent.ConcurrentHashMap;

public class ServerVoice {

	public static ConcurrentHashMap<Integer,ServerVoice> server = new ConcurrentHashMap<Integer,ServerVoice>();

	public int server_id;
	public int max_clients;
	public String DNS;
	public int port;

	public int connected_clients;
	
	
	
	
}
