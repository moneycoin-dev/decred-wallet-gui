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
	
	public synchronized static ArrayList<JsonObject> getInfo() {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " getinfo"));
	}
	
	public synchronized static ArrayList<JsonObject> getPeerInfo() {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " getpeerinfo"));
	}
	
	public synchronized static void disconnectPeer(String id) {
		command.execute(Constants.getDcrctlBaseCommand() + " node disconnect " + id + " temp");
	}
	
	public synchronized static void ping() {
		command.execute(Constants.getDcrctlBaseCommand() + " ping");
	}
	
	
	
	public synchronized static String getBalance(String name) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getbalance " + 
				Constants.getOsQuote() + name + Constants.getOsQuote() + 
				" 0 spendable");
	}
	
	public synchronized static String getLockedBalance(String name) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getbalance " + 
				Constants.getOsQuote() + name + Constants.getOsQuote() + 
				" 0 locked");
	}
	
	public synchronized static String getBalanceAll(String name) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getbalance " + 
				Constants.getOsQuote() + name + Constants.getOsQuote() + 
				" 0 all");
	}
	
	public synchronized static ArrayList<JsonObject> getTransactions() {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " --wallet listtransactions"));
	}
	
	public synchronized static ArrayList<JsonObject> getTransactions(String name) {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " --wallet listtransactions " + Constants.getOsQuote() + name + Constants.getOsQuote()));
	}
	
	public synchronized static ArrayList<JsonObject> getAccounts() {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " --wallet listaccounts"));
	}
	
	public synchronized static String getWalletFee() {
		return String.valueOf(Double.valueOf(command.execute(Constants.getDcrctlBaseCommand() + " --wallet getwalletfee").trim()));
	}
	
	public synchronized static String getStakeDifficulty() {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " --wallet getstakedifficulty")).get(0).getValueByName("current");
	}
	
	public synchronized static String getAddressesByAccount(String name) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getaddressesbyaccount " + 
				Constants.getOsQuote() + name + Constants.getOsQuote()).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
	}
	
	public synchronized static String getNewAddress(String name) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getnewaddress " + 
				Constants.getOsQuote() + name + Constants.getOsQuote());
	}
	
	public synchronized static String unlockWallet(String timeout) {
		String result = command.execute(Constants.getDcrctlBaseCommand() + " --wallet walletpassphrase " + 
				Constants.getOsQuote() + Constants.getPrivatePassPhrase() + Constants.getOsQuote() + " " + timeout);
		
		Constants.setPrivatePassPhrase(null);
		return result;
	}
	
	public synchronized static void renameAccount(String old, String name) {
		command.execute(Constants.getDcrctlBaseCommand() + " --wallet renameaccount " + 
				Constants.getOsQuote() + old + Constants.getOsQuote() + " " + 
				Constants.getOsQuote() + name + Constants.getOsQuote());
	}
	
	public synchronized static void createNewAccount(String name) {
		command.execute(Constants.getDcrctlBaseCommand() + " --wallet createnewaccount " + 
				Constants.getOsQuote() + name + Constants.getOsQuote());
	}
	
	public synchronized static ArrayList<JsonObject> getStakeInfo() {
		return Json.parseJson(command.execute(Constants.getDcrctlBaseCommand() + " --wallet getstakeinfo"));
	}
	
	public synchronized static boolean setTxFee(String fee) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet settxfee " + fee).startsWith("true");
	}

	public synchronized static String sendFrom(String name, String toAddress, String comment, String amount) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet sendfrom " + 
				Constants.getOsQuote() + name + Constants.getOsQuote() + " " + 
				Constants.getOsQuote() + toAddress + Constants.getOsQuote() + " " + amount);
	}
	
	public synchronized static String purchaseTicket(String name, String spendLimit, String address) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet purchaseticket " + 
				Constants.getOsQuote() + name + Constants.getOsQuote() + " " + spendLimit + " 1 " + 
				Constants.getOsQuote() + address + Constants.getOsQuote());
	}
	
	public synchronized static String dumpPrivKey(String address) {
		String result = command.execute(Constants.getDcrctlBaseCommand() + " --wallet dumpprivkey " + 
				Constants.getOsQuote() + address + Constants.getOsQuote());
		
		Constants.setPrivatePassPhrase(null);
		
		return result;
	}
	
	public synchronized static String wallPassphraseChange(String oldpassphrase, String newpassphrase) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet walletpassphrasechange " + 
				Constants.getOsQuote() + oldpassphrase + Constants.getOsQuote() + " " + 
				Constants.getOsQuote() + newpassphrase + Constants.getOsQuote());
	}
	
	public synchronized static String getMasterPubkey() {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet getmasterpubkey");
	}
	
	public synchronized static String dumpWallet(String filename) {
		return command.execute(Constants.getDcrctlBaseCommand() + " --wallet dumpwallet" + 
				Constants.getOsQuote() + filename + Constants.getOsQuote());
	}

}
