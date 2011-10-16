package sn.util;

import java.security.SecureRandom;

public final class RandomHash {

	//TODO, adesso è stata fatto molto alla sempliciotta, da rifare
	public static String one(){
		SecureRandom random = new SecureRandom();
		String random_string = random.generateSeed(40).toString();
		return SecureHash.Md5(random_string);		
	}
	
}
