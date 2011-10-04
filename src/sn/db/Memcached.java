package sn.db;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class Memcached {

	protected static MemCachedClient MemCached = new MemCachedClient();

	final static boolean active = false;
	final static String server = "127.0.0.1";
	final static String port = "1111";
	
	
	
	@SuppressWarnings("unused")
	public static void init() {
		if(active == true){
			
			SockIOPool pool = SockIOPool.getInstance();
			
			// servers
			String[] servers = { server + ":" + port };
			
			// priorità
			Integer[] weights = { 3 };
		
			pool.setServers( servers );
			pool.setWeights( weights );
		
			
			pool.setInitConn( 5 );
			pool.setMinConn( 5 );
			pool.setMaxConn( 250 );
			
			// Idle di 24h
			pool.setMaxIdle( 1000 * 60 * 60 * 24 );
		
			// set the sleep for the maint thread
			// it will wake up every x seconds and
			// maintain the pool size
			pool.setMaintSleep( 30 );
		
			// disable nagle
			// timeout read 3 s
			// no connect timeout
			pool.setNagle( false );
			pool.setSocketTO( 3000 );
			pool.setSocketConnectTO( 0 );
		
			pool.initialize();
		}
	}
	
	public static String[] get_SQL(String key){
		return unserialize_row((String) MemCached.get(key));
	}
	
	public static String[] unserialize_row(String data){
		return data.split(Character.toChars(0).toString());
	}
	
	public static String serialize_row(String[] data){
		String serialized = "";
		for (String column : data) {
			if(serialized != ""){
				serialized.concat(Character.toChars(0).toString());
			}
			serialized.concat(column.replace(Character.toChars(0).toString(), "").replace(Character.toChars(7).toString(), ""));
		}
		
		return serialized;
		
	}
}
