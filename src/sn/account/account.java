package sn.account;

import java.util.concurrent.ConcurrentHashMap;

public class account {
	
	static ConcurrentHashMap<Integer,account> accounts = new ConcurrentHashMap<Integer,account>(); // dv int è ovviamente l'account id
	int[] channels_id; // i channel appartenenti all'account
	int[] friends_online;
	int[] chat_opened;
	int chat_active;
	String nickname;
	int account_id;
	int status;
	
	
	
	
}
