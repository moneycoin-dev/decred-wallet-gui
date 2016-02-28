package com.hosvir.decredwallet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import com.hosvir.decredwallet.gui.BaseGui;
import com.hosvir.decredwallet.gui.Main;
import com.hosvir.decredwallet.gui.interfaces.Navbar;
import com.hosvir.decredwallet.utils.FileWriter;
import com.hosvir.decredwallet.utils.JsonObject;
import com.hosvir.decredwallet.utils.JsonObjects;

/**
 * 
 * Constant fields to be used throughout the program.
 * 
 * @author Troy
 * 
 */
public class Constants {
	private static String version;
	private static String buildDate;
	private static String userHome;
	private static String decredLocation;
	private static String daemonUsername;
	private static String daemonPassword;
	private static String publicPassPhrase;
	private static String privatePassPhrase;
	private static String windowsDaemonArguments;
	private static String windowsWalletArguments;
	private static String daemonCommand;
	private static String walletCommand;
	private static String dcrctlBaseCommand;
	private static File settingsFile;
	private static File dcrdCert;
	private static File walletCert;
	
	private static boolean enableOpenGL;
	private static boolean enableFps;
	
	private static ArrayList<String> allowedPasswordClasses;
	
	private static Throwable throwable; 
	private static StackTraceElement[] elements; 
	private static String callerClassName; 
	
	private static Date date;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy - hh:mm:ss a");
	private static SimpleDateFormat wsdf = new SimpleDateFormat("MMM dd:hh!mm!ss a");
	
	private static LocalProcess daemonProcess;
	private static LocalProcess walletProcess;
	private static boolean daemonReady, walletReady;
	private static Main mainGui;
	
	public static int doubleClickDelay;
	public static int fpsMax;
	public static int fpsMin;
	
	public static Color transparentBlack;
	public static Color walletNameColor;
	public static Color walletBalanceColor;
	public static Color labelColor;
	public static Color settingsSelectedColor;
	public static Color flatRed;
	public static Color flatYellow;
	public static Color flatOrange;
	public static Color flatBlue;
	public static Color flatGreen;
	public static Color flatRedHover;
	public static Color flatYellowHover;
	public static Color flatOrangeHover;
	public static Color flatBlueHover;
	public static Color flatGreenHover;
	public static Font walletNameFont;
	public static Font walletBalanceFont;
	public static Font dcrFont;
	public static Font totalBalanceFont;
	public static Font labelFont;
	public static Font addressFont;
	public static Font transactionFont;
	public static Font settingsFont;
	
	private static Clipboard clipboard;
	public static ArrayList<String> guiLog = new ArrayList<String>();
	public static ArrayList<Account> accounts = new ArrayList<Account>();
	public static ArrayList<BaseGui> guiInterfaces = new ArrayList<BaseGui>();
	public static Navbar navbar;
	public static GlobalCache globalCache;
	public static String accountToRename;
	
	/**
	 * Initialise constants.
	 */
	public static void initialise() {
		version = "0.0.1-beta";
		buildDate = "27/02/2016";
		guiLog = new ArrayList<String>();
		
		userHome = System.getProperty("user.home") + File.separator + "JDecredWallet" + File.separator;
		settingsFile = new File(Constants.userHome + "settings.conf");
		dcrdCert = new File(Constants.userHome + ".dcrd" + File.separator + "rpc.cert");
		walletCert = new File(Constants.userHome + ".dcrwallet" + File.separator + "rpc.cert");
		allowedPasswordClasses = new ArrayList<String>();
		allowedPasswordClasses.add("com.hosvir.decredwallet.DecredWallet");
		allowedPasswordClasses.add("com.hosvir.decredwallet.Api");
		allowedPasswordClasses.add("com.hosvir.decredwallet.gui.interfaces.Send");
		
		//OS is Windows... poor fella
		if(getOS().contains("Windows")){
			windowsDaemonArguments = " --notls ";
			windowsWalletArguments = " --noservertls --noclienttls ";
		}else{
			windowsDaemonArguments = "";
			windowsWalletArguments = "";
		}
		
		//Load settings
		if(!settingsFile.exists()) createDefaultProperties();
		
		try{
			Properties properties = new Properties();
			properties.load(new FileInputStream(settingsFile));
			
			decredLocation = properties.getProperty("Decred-Location");
			daemonUsername = properties.getProperty("Daemon-Username");
			daemonPassword = properties.getProperty("Daemon-Password");
			enableOpenGL = Boolean.valueOf(properties.getProperty("Enable-OpenGL"));
			enableFps = Boolean.valueOf(properties.getProperty("Enable-FPS"));
			doubleClickDelay = Integer.valueOf(properties.getProperty("Double-Click-Delay"));
			fpsMax = Integer.valueOf(properties.getProperty("FPS-Max"));
			fpsMin = Integer.valueOf(properties.getProperty("FPS-Min"));
		}catch(IOException e){
			System.out.println("Unable to find settings file.");
			e.printStackTrace();
		}
		
		if(getOS().contains("Windows")){
			daemonCommand = decredLocation + "dcrd.exe -u '" + 
							daemonUsername + "' -P '" + 
							daemonPassword + "'" + 
							windowsDaemonArguments;
			walletCommand = decredLocation + "dcrwallet.exe -u '" + 
							daemonUsername + "' -P '" + 
							daemonPassword + "'" + 
							windowsWalletArguments;
			dcrctlBaseCommand = decredLocation + "dcrctl.exe -u '" +
							daemonUsername + "' -P '" +
							daemonPassword + "'" +
							windowsWalletArguments;
		}else{
			daemonCommand = decredLocation + "dcrd -u '" + 
							daemonUsername + "' -P '" + 
							daemonPassword + "'" + 
							windowsDaemonArguments;
			walletCommand = decredLocation + "dcrwallet -u '" + 
							daemonUsername + "' -P '" + 
							daemonPassword + "'" + 
							windowsWalletArguments;
			dcrctlBaseCommand = decredLocation + "dcrctl -u '" +
							daemonUsername + "' -P '" +
							daemonPassword + "'" +
							windowsWalletArguments;
		}
		
		
		transparentBlack = new Color(0,0,0,0.7f);
		walletNameColor = new Color(54,54,54);
		walletBalanceColor = new Color(101,101,101);
		labelColor = new Color(144,144,144);
		settingsSelectedColor = new Color(228,228,228);
		flatRed = new Color(231,76,70);
		flatYellow = new Color(241,196,15);
		flatOrange = new Color(230,126,34);
		flatBlue = new Color(52,152,219);
		flatGreen = new Color(46,204,113);
		flatRedHover = new Color(192, 57, 43);
		flatYellowHover = new Color(243, 156, 18);
		flatOrangeHover = new Color(211, 84, 0);
		flatBlueHover = new Color(41, 128, 185);
		flatGreenHover = new Color(39, 174, 96);
		walletNameFont = new Font("Arial", 1, 25);
		walletBalanceFont = new Font("Arial", 1, 18);
		dcrFont = new Font("Arial", 0, 30);
		totalBalanceFont = new Font("Arial", 1, 40);
		labelFont = new Font("Arial", 0, 16);
		addressFont = new Font("Arial", 0, 18);
		transactionFont = new Font("Arial", 0, 14);
		settingsFont = new Font("Arial", 1, 20);
		
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		accounts = new ArrayList<Account>();
		guiInterfaces = new ArrayList<BaseGui>();
		
		globalCache = new GlobalCache();
	}

	/**
	 * Create default properties
	 */
	private static void createDefaultProperties(){
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "#Settings file for JDecredWallet.", false, false);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Decred-Location=/home/ubuntu/decred/", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Daemon-Username=username", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Daemon-Password=password", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "#GUI settings", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Double-Click-Delay=400", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "#Display settings", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Enable-OpenGL=true", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Enable-FPS=false", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "FPS-Max=30", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "FPS-Min=1", true, true);
	
		log("Default properties file has been created, edit preferences.conf and then restart the program.");
		System.exit(0);
	}
	
	/**
	 * Log a message
	 * 
	 * @param message
	 */
	public static void log(String message) {
		System.out.println(getDate() + ": " + message);
		guiLog.add(getDate() + ": " + message);
		
		if(guiInterfaces.size() > 5) guiInterfaces.get(5).resize();
	}
	
	/**
	 * Check if the calling class is allowed to access the username / password.
	 * @return
	 */
	private static boolean allowedPasswordClass() {
		throwable = new Throwable(); 
		elements = throwable.getStackTrace(); 
		callerClassName = elements[2].getClassName(); 

		return allowedPasswordClasses.contains(callerClassName) ? true : false;
	}
	
	/**
	 * Get the date.
	 * 
	 * @return String
	 */
	public static String getDate(){
		date = new Date();
		return sdf.format(date);
	}
	
	/**
	 * Get the date.
	 * 
	 * @return String
	 */
	public static String getWalletDate(Long timestamp){
		date = new Date();
		date.setTime(timestamp*1000);
		return wsdf.format(date);
	}
	
	/**
	 * Get the Operating System name, architecture and version.
	 * 
	 * @return String
	 */
	public static String getOS(){
		return System.getProperty("os.name") + ", " + System.getProperty("os.arch") + ", " + System.getProperty("os.version");
	}
	
	/**
	 * Get the current User.
	 * 
	 * @return String
	 */
	public static String getUser(){
		return System.getProperty("user.name");
	}
	
	/**
	 * Get the clipboard contents.
	 * 
	 * @return Clipboard string data
	 */
	public static String getClipboardString() {
		try{
			return (String) clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor);
		}catch(Exception e){
			return "";
		}
	}
	
	/**
	 * Set the clipboard contents.
	 * 
	 * @param text
	 */
	public static void setClipboardString(String text) {
		clipboard.setContents(new StringSelection(text), null);
	}
	
	public static void reloadAccounts() {
		for(Account a : accounts) a.setRunning(false);
		accounts.clear();
		
		for(JsonObject jo : Api.getAccounts())
			for(JsonObjects jos : jo.getJsonObjects())
				Constants.accounts.add(new Account(jos.getName()));
	}
	
	public static void blockInterfaces(boolean block, BaseGui gui) {
		for(BaseGui b : guiInterfaces)
			if(b != gui)
				b.blockInput = block;
	}

	public static String getVersion() {
		return version;
	}

	public static String getBuildDate() {
		return buildDate;
	}

	public static String getUserHome() {
		return userHome;
	}
	
	public static String getDecredLocation() {
		return decredLocation;
	}
	
	public static LocalProcess getDaemonProcess() {
		return daemonProcess;
	}

	public static void setDaemonProcess(LocalProcess daemonProcess) {
		Constants.daemonProcess = daemonProcess;
	}

	public static LocalProcess getWalletProcess() {
		return walletProcess;
	}

	public static void setWalletProcess(LocalProcess walletProcess) {
		Constants.walletProcess = walletProcess;
	}

	public static boolean isDaemonReady() {
		return daemonReady;
	}

	public static void setDaemonReady(boolean daemonReady) {
		Constants.daemonReady = daemonReady;
	}

	public static boolean isWalletReady() {
		return walletReady;
	}

	public static void setWalletReady(boolean walletReady) {
		Constants.walletReady = walletReady;
	}

	public static Main getMainGui() {
		return mainGui;
	}

	public static void setMainGui(Main mainGui) {
		Constants.mainGui = mainGui;
	}

	public static String getDaemonUsername() {
		return allowedPasswordClass() ? daemonUsername : null;
	}

	public static String getDaemonPassword() {
		return allowedPasswordClass() ? daemonPassword : null;
	}
	
	public static String getPublicPassPhrase() {
		return allowedPasswordClass() ? publicPassPhrase : null;
	}

	public static void setPublicPassPhrase(String publicPassPhrase) {
		Constants.publicPassPhrase = publicPassPhrase;
	}

	public static String getPrivatePassPhrase() {
		return allowedPasswordClass() ? privatePassPhrase : null;
	}

	public static void setPrivatePassPhrase(String privatePassPhrase) {
		Constants.privatePassPhrase = privatePassPhrase;
	}

	public static String getWindowsDaemonArguments() {
		return windowsDaemonArguments;
	}

	public static String getWindowsWalletArguments() {
		return windowsWalletArguments;
	}
	
	public static String getDaemonCommand() {
		return allowedPasswordClass() ? daemonCommand : null;
	}

	public static String getWalletCommand() {
		return allowedPasswordClass() ? walletCommand : null;
	}
	
	public static String getDcrctlBaseCommand() {
		return allowedPasswordClass() ? dcrctlBaseCommand : null;
	}

	public static File getDcrdCert() {
		return dcrdCert;
	}

	public static File getWalletCert() {
		return walletCert;
	}

	public static Clipboard getClipboard() {
		return clipboard;
	}

	public static boolean isEnableOpenGL() {
		return enableOpenGL;
	}

	public static boolean isEnableFps() {
		return enableFps;
	}
	
}