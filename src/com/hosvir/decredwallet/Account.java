package com.hosvir.decredwallet;

import com.deadendgine.Updatable;
import com.deadendgine.utils.Timer;

/**
 * 
 * @author Troy
 *
 */
public class Account extends Thread implements Updatable {
	private boolean running;
	public boolean forceUpdate;
	public String name;
	private Timer updateTimer = new Timer(1000);
	public double balance = 0.00;
	public double pendingBalance = 0.00;
	public double lockedBalance = 0.00;
	public double totalBalance = 0.00;
	public String[] addresses;
	//public ArrayList<JsonObject> transactions = new ArrayList<JsonObject>();
	
	/**
	 * Construct a new account.
	 * 
	 * @param name
	 */
	public Account(String name){
		this.name = name;
		this.setName("Decred Wallet - Account Thread");
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
			balance = Api.getBalance(name);
			lockedBalance = Api.getLockedBalance(name);
			totalBalance = Api.getBalanceAll(name);
			addresses = Api.getAddressesByAccount(name).split(",");
			//transactions = Api.getTransactions(name);
			
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
