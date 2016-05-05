package com.hosvir.decredwallet;

import java.util.ArrayList;

import com.deadendgine.Updatable;
import com.deadendgine.utils.Timer;
import com.hosvir.decredwallet.utils.JsonObject;

/**
 * 
 * @author Troy
 *
 */
public class GlobalCache extends Thread implements Updatable {
	private boolean running;
	public boolean forceUpdate;
	public boolean forceUpdateInfo;
	public boolean forceUpdatePeers;
	private Timer updateTimer = new Timer(1000);
	private Timer infoTimer = new Timer(1000);
	private Timer peerTimer = new Timer(1000);
	public String walletFee = "0.00";
	public ArrayList<JsonObject> transactions;
	public ArrayList<JsonObject> info;
	public ArrayList<JsonObject> stakeInfo;
	public ArrayList<JsonObject> peers;
	
	public GlobalCache() {
		transactions = new ArrayList<JsonObject>();
		info = new ArrayList<JsonObject>();
		stakeInfo = new ArrayList<JsonObject>();
		peers = new ArrayList<JsonObject>();
		
		this.setName("Decred Wallet - Cache Thread");
		this.setPriority(NORM_PRIORITY);
		this.start();
	}
	
	public void run() {
		running = true;
		
		while(running){
			if(Constants.isWalletReady())
				update(0);
			
			try {
				Thread.sleep(1000);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void update(long delta) {
		if(updateTimer.isUp() || forceUpdate){
			walletFee = Api.getWalletFee();
			transactions = Api.getTransactions();
			
			if(updateTimer.timeLimit <= 100000) updateTimer.timeLimit = Constants.getRandomNumber(100000, 180000);
			updateTimer.reset();
			forceUpdate = false;
		}
		
		//Info update
		if(infoTimer.isUp() || forceUpdateInfo){
			info = Api.getInfo();
			stakeInfo = Api.getStakeInfo();
					
			if(infoTimer.timeLimit <= 10000) infoTimer.timeLimit = Constants.getRandomNumber(10000, 45000);
			infoTimer.reset();
			forceUpdateInfo = false;
		}
		
		//Peer update
		if(peerTimer.isUp() || forceUpdatePeers){
			Api.ping();
			peers = Api.getPeerInfo();
			Constants.guiInterfaces.get(6).resize();
			
			if(peerTimer.timeLimit <= 10000) peerTimer.timeLimit = Constants.getRandomNumber(10000, 45000);
			peerTimer.reset();
			forceUpdatePeers = false;
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
}
