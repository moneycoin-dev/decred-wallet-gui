package com.hosvir.decredwallet;

import java.util.ArrayList;

import com.hosvir.decredwallet.utils.Json;
import com.hosvir.decredwallet.utils.JsonObject;

/**
 * 
 * @author Troy
 *
 */
public class Api {
	private static LocalCommand command = new LocalCommand();
	
	public synchronized static double getBalance(String name) {
		return Double.parseDouble(command.execute(Constants.getDcrctlBaseCommand() + " --wallet getbalance '" + name + "' 0 spendable"));
	}
	
	public synchronized static double getLockedBalance(String name) {
		return Double.parseDouble(command.execute(Constants.getDcrctlBaseCommand() + " --wallet getbalance '" + name + "' 0 locked"));
	}
	
	public synchronized static double getBalanceAll(String name) {
		return Double.parseDouble(command.execute(Constants.getDcrctlBaseCommand() + " --wallet getbalance '" + name + "' 0 all"));
	}
	
	public synchronized static ArrayList<JsonObject> getTransactions(String name) {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " --wallet listtransactions"));
	}
	
	public synchronized static ArrayList<JsonObject> getAccounts() {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " --wallet listaccounts"));
	}
	
	public synchronized static double getWalletFee() {
		return Double.parseDouble(command.execute(Constants.getDcrctlBaseCommand() + " --wallet getwalletfee")) / 100000000;
	}
	
	public synchronized static double getStakeDifficulty() {
		return Double.parseDouble(command.execute(Constants.getDcrctlBaseCommand() + " --wallet getstakedifficulty"));
	}
	
	public synchronized static String getAddressesByAccount(String name) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getaddressesbyaccount '" + name + "'").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
	}
	
	public synchronized static String getNewAddress(String name) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getnewaddress '" + name + "'");
	}
	
	public synchronized static void unlockWallet(int timeout) {
		command.execute(Constants.getDcrctlBaseCommand() + " --wallet walletpassphrase '" + Constants.getPrivatePassPhrase() + "' " + timeout);
		Constants.setPrivatePassPhrase(null);
	}
	
	public synchronized static void renameAccount(String old, String name) {
		command.execute(Constants.getDcrctlBaseCommand() + " --wallet renameaccount '" + old + "' '" + name + "'");
	}
	
	public synchronized static boolean setTxFee(double fee) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet settxfee " + fee).startsWith("true");
	}

	public synchronized static String sendFrom(String name, String toAddress, String comment, double amount) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet sendfrom '" + name + "' " + toAddress + " " + amount);
	}

}
