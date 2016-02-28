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
	private Timer updateTimer = new Timer(1000);
	public double walletFee = 0.05;
	public ArrayList<JsonObject> transactions = new ArrayList<JsonObject>();
	
	public GlobalCache() {
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
			transactions = Api.getTransactions(null);
			
			if(updateTimer.timeLimit < 120000) updateTimer.timeLimit = 120000; 
			updateTimer.reset();
			forceUpdate = false;
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
}
