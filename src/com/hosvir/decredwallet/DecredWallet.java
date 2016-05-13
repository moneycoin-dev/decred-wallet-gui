package com.hosvir.decredwallet;

import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * This is the entry class.
 * 
 * @author Troy
 *
 */
public class DecredWallet {
	private long start, end;
	
	public static void main(String[] args){
		new DecredWallet();
	}
	
	/**
	 * Constructor
	 */
	public DecredWallet() {
		//Setup constants
		Constants.initialise();
		
		// TODO look into changing this to use log files rather than killing process
		if(Processes.getClosestProcess("dcrd") != -1){ 
			Processes.killByName("dcrd");
			Constants.log("Killed existing Daemon, we need to control the process.");
		}
		
		if(Processes.getClosestProcess("dcrwallet") != -1) {
			Processes.killByName("dcrwallet");
			Constants.log("Killed existing Wallet, we need to control the process.");
		}
		
		//Launch GUI
		Constants.setMainGui(new Main());
		
		Constants.log("Starting Daemon.");
	
		start = System.currentTimeMillis();
			
		//Start Daemon
		Constants.setDaemonProcess(new LocalProcess(Constants.getDaemonCommand()));
			
		//Check to see if the daemon is ready
		while(!Constants.isDaemonReady()){
			for(String s : Constants.getDaemonProcess().log)
				if(s.contains("RPC server listening"))
					Constants.setDaemonReady(true);
				
			//Sleep
			sleep(100);
		}

		Constants.log("Starting Wallet.");
			
		//Start Wallet
		Constants.setWalletProcess(new LocalProcess(Constants.getWalletCommand()));
	
		//Check to see if the daemon is ready
		while(!Constants.isWalletReady()){
			for(String s : Constants.getWalletProcess().log)
				if(s.contains("Opened wallet"))
					Constants.setWalletReady(true);
				else if(s.contains("invalid passphrase for master public key"))
					Constants.log("Need public key, edit settings.conf");
				
			//Sleep
			sleep(100);
		}
		
		end = System.currentTimeMillis() - start; 
		Constants.log("Loaded Daemon and Wallet in " + (end / 1000) + " seconds.");
		
		//Update Global cache
		Constants.globalCache.forceUpdate = true;
		Constants.globalCache.forceUpdateInfo = true;
		Constants.globalCache.forceUpdatePeers = true;
	}
	
	/**
	 * Sleep the thread
	 * 
	 * @param time
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

}
