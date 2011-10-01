package sn.main;

import sn.net.*;

public class Main {

	private static PresenceServer presenceServer = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting PresenceServer");
		setPresenceServer(new PresenceServer());
	}

	public static void setPresenceServer(PresenceServer presenceServer) {
		Main.presenceServer = presenceServer;
	}

	public static PresenceServer getPresenceServer() {
		return presenceServer;
	}

}
