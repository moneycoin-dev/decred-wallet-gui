package com.hosvir.decredwallet;

import java.math.BigDecimal;
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
	
	public synchronized static String getBalance(String name) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getbalance '" + name + "' 0 spendable");
	}
	
	public synchronized static String getLockedBalance(String name) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getbalance '" + name + "' 0 locked");
	}
	
	public synchronized static String getBalanceAll(String name) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getbalance '" + name + "' 0 all");
	}
	
	public synchronized static ArrayList<JsonObject> getTransactions(String name) {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " --wallet listtransactions"));
	}
	
	public synchronized static ArrayList<JsonObject> getAccounts() {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " --wallet listaccounts"));
	}
	
	public synchronized static String getWalletFee() {
		return String.valueOf(BigDecimal.valueOf(Double.valueOf(command.execute(Constants.getDcrctlBaseCommand() + " --wallet getwalletfee").trim()) / 100000000));
	}
	
	public synchronized static String getStakeDifficulty() {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getstakedifficulty");
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
	
	public synchronized static void createNewAccount(String name) {
		command.execute(Constants.getDcrctlBaseCommand() + " --wallet createnewaccount '" + name + "'");
	}
	
	public synchronized static boolean setTxFee(String fee) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet settxfee " + fee).startsWith("true");
	}

	public synchronized static String sendFrom(String name, String toAddress, String comment, String amount) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet sendfrom '" + name + "' " + toAddress + " " + amount);
	}

}
