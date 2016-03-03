package com.hosvir.decredwallet.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.deadendgine.BaseGame;
import com.deadendgine.Engine;
import com.deadendgine.GameFrame;
import com.deadendgine.GameState;
import com.deadendgine.graphics.GameCanvas;
import com.deadendgine.input.Keyboard;
import com.deadendgine.input.Mouse;
import com.deadendgine.utils.Timer;
import com.hosvir.decredwallet.Account;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.Processes;
import com.hosvir.decredwallet.gui.interfaces.AddressBook;
import com.hosvir.decredwallet.gui.interfaces.Logs;
import com.hosvir.decredwallet.gui.interfaces.Navbar;
import com.hosvir.decredwallet.gui.interfaces.Receive;
import com.hosvir.decredwallet.gui.interfaces.Send;
import com.hosvir.decredwallet.gui.interfaces.Settings;
import com.hosvir.decredwallet.gui.interfaces.Staking;
import com.hosvir.decredwallet.gui.interfaces.Wallet;
import com.hosvir.decredwallet.gui.interfaces.popups.AddContact;
import com.hosvir.decredwallet.gui.interfaces.popups.CreateAccount;
import com.hosvir.decredwallet.gui.interfaces.popups.Passphrase;
import com.hosvir.decredwallet.gui.interfaces.popups.RenameAccount;
import com.hosvir.decredwallet.utils.JsonObject;
import com.hosvir.decredwallet.utils.JsonObjects;

/**
 * 
 * @author Troy
 *
 */
public class Main extends BaseGame {
	//---------------------Thread variables-----------------
	private Thread gameThread;
	public static int UPDATE_RATE = 30;
	private long UPDATE_PERIOD = 1000L / UPDATE_RATE;
	public static long beginTime, timeTaken, timeLeft, lastLoopTime, delta;
	//------------------------------------------------------
				
	//----------------------FPS / DELTA---------------------
	public int FPS = 0;
	private int FRAMES = 0;
	private long totalTime = 0;
	private long curTime = System.currentTimeMillis();
	private long lastTime = curTime;
	//------------------------------------------------------
				
	//-------------------Canvas / Frame---------------------
	public static GameFrame frame;
	public static GameCanvas canvas;
	//------------------------------------------------------
			
	//-------------------Variables--------------------------
	public GameState state = GameState.INITILISING;
	public static boolean fullscreen = false;
					
	//Keyboard and Mouse
	public static Keyboard keyboard = null;
	public static Mouse mouse = null;
	
	//Volume
	public static float volume = 1.0f;
	public static boolean sound = true;
	
	private Timer loadingAnimationTimer;
	private int loadingAnimationCount;
	private String loadingAnimationString = "";
	public static boolean containsMouse = false;
	
	
	/**
	 * Constructor
	 */
	public Main() {
		init();
		
		//Create a new thread to run the game in
		gameThread = new Thread(){
			
			/**
			 * Default run method for the thread.
			 * 
			 * Call loop then interrupt the loop then quit
			 * the game.
			 */
			@Override
			public void run(){
				loop();
				interrupt();
				quit();
			}
			
		};
			
		//Start the thread
		gameThread.setPriority(Thread.MAX_PRIORITY);
		gameThread.start();
	}

	@Override
	public void init() {
		Engine.setOpenGL(Constants.isEnableOpenGL());
		Engine.init();
		
		//Let the engine know this class is the game
		Engine.setGame(this);
			
		//Set the width and height to be used for window
		Engine.setPreferedWidth(1200);
		Engine.setPreferedHeight(700);
		Engine.setWidth(1200);
		Engine.setHeight(700);
		Engine.setMinimumWidth(1100);
		Engine.setMinimumHeight(600);
			
		//Create a new canvas
		canvas = new GameCanvas();
		//Create a frame with our canvas
		frame = new GameFrame("Decred Wallet", canvas);
		frame.setResizable(true);
		frame.setMinimumSize(new Dimension(Engine.getMinimumWidth(),Engine.getMinimumHeight()));
		canvas.setBackground(new Color(241,241,241));
		
		
		//Initialise the canvas
		canvas.init();
			
		//Create a new keyboard and mouse
		keyboard = new Keyboard();
		mouse = new Mouse();
		
		//Add the listeners to the canvas
		canvas.addKeyListener(keyboard);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		
		//Load images
		Images.init();
		
		Constants.navbar = new Navbar();
		Constants.guiInterfaces.add(new Wallet());
		Constants.guiInterfaces.add(new AddressBook());
		Constants.guiInterfaces.add(new Staking());
		Constants.guiInterfaces.add(new Send());
		Constants.guiInterfaces.add(new Receive());
		Constants.guiInterfaces.add(new Logs());
		Constants.guiInterfaces.add(new Settings());
		Constants.guiInterfaces.add(new AddContact());
		Constants.guiInterfaces.add(new CreateAccount());
		Constants.guiInterfaces.add(new RenameAccount());
		Constants.guiInterfaces.add(new Passphrase());
		
		
		Constants.navbar.init();
		for(BaseGui bg : Constants.guiInterfaces) bg.init();
		
		loadingAnimationTimer = new Timer(1000);
	}

	@Override
	public void loop() {
		state = GameState.MAIN_MENU;
		lastLoopTime = System.currentTimeMillis();
		
		while(true){
			try{					
				//Calculate FPS and time
				lastTime = curTime;
				curTime = System.currentTimeMillis();
				totalTime += curTime - lastTime;
					
				if(totalTime > 1000){
					totalTime -= 1000;
					FPS = FRAMES;
					FRAMES = 0;
				}
					
				//Get FPS
				FRAMES++;
				beginTime = System.currentTimeMillis();
				delta = beginTime - lastLoopTime;
				lastLoopTime = beginTime;
					
				//Update
				update(delta);

				//Draw the graphics
				canvas.render();
					
				//Sleep
				timeTaken = System.currentTimeMillis() - beginTime;
				timeLeft = (UPDATE_PERIOD - timeTaken);
					
				//Sleep for remaining time
				try{
					if(timeLeft > -1)
						Thread.sleep(timeLeft);
				}catch(InterruptedException ex){break;}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}


	@Override
	public synchronized void update(long delta) {
		containsMouse = false;
		
		//Adjust FPS to reduce load when not in use
		if(!canvas.hasFocus() && UPDATE_RATE != Constants.fpsMin){
			UPDATE_RATE = Constants.fpsMin;
			UPDATE_PERIOD = 1000L / UPDATE_RATE;
		}else if(UPDATE_RATE != Constants.fpsMax) {
			UPDATE_RATE = Constants.fpsMax;
			UPDATE_PERIOD = 1000L / UPDATE_RATE;
		}
		
		
		
		if(!Constants.isDaemonReady() || !Constants.isWalletReady()){
			if(loadingAnimationTimer.isUp()){
				loadingAnimationCount++;
				loadingAnimationString = "";
				loadingAnimationTimer.reset();
				
				if(loadingAnimationCount > 3)
					loadingAnimationCount = 0;
				
				for(int i = 0; i < loadingAnimationCount; i++){
					loadingAnimationString += ".";
				}
			}
		}else{
			if(Constants.accounts.size() == 0){
				for(JsonObject jo : Api.getAccounts())
					for(JsonObjects jos : jo.getJsonObjects())
						Constants.accounts.add(new Account(jos.getName()));
				
				Constants.globalCache.forceUpdateInfo = true;
				Constants.globalCache.forceUpdatePeers = true;
			}
			
			//Update nav
			Constants.navbar.update(delta);
			if(Constants.navbar.containsMouse) containsMouse = true;
			
			//Update interfaces
			for(BaseGui bg : Constants.guiInterfaces) {
				if(bg.isActive()){
					bg.update(delta);
					
					if(bg.containsMouse && !bg.blockInput) containsMouse = true;
				}
			}
			
			//Restore cursor
			if(!containsMouse){
				Main.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}else{
				Main.frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			
			
			//Cleanup logs, one log per update
			if(Constants.guiLog.size() > Constants.maxLogLines) {
				for(int i = 0; i < 100; i++)
					Constants.guiLog.remove(i);
			}else if(Constants.getDaemonProcess().log.size() > Constants.maxLogLines) {
				for(int i = 0; i < 100; i++)
					Constants.guiLog.remove(i);
			}else if(Constants.getWalletProcess().log.size() > Constants.maxLogLines) {
				for(int i = 0; i < 100; i++)
					Constants.guiLog.remove(i);
			}
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		switch(state){
		case INITILISING:
			g.drawString("Loading...", (Engine.getWidth() / 2) - 30, (Engine.getHeight() / 2) - 30);
			break;
		case MAIN_MENU:
			Constants.navbar.render(g);
			
			for(BaseGui bg : Constants.guiInterfaces) {
				if(bg.isActive())
					bg.render(g);
			}
			
			//Loading message
			if(!Constants.isDaemonReady() || !Constants.isWalletReady()){				
				g.setColor(Constants.transparentBlack);
				g.fillRect(0, 0, Engine.getWidth(), Engine.getHeight());
				
				g.setColor(Color.WHITE);
				g.fillRect(0, (Engine.getHeight() / 2) - 100, Engine.getWidth(), 200);
				
				g.setColor(Constants.walletNameColor);
				g.setFont(Constants.dcrFont);
				g.drawString("Loading" + loadingAnimationString, (Engine.getWidth() / 2) - 60, (Engine.getHeight() / 2));
				
				if(!Constants.isDaemonReady()){
					g.setColor(Constants.labelColor);
					g.setFont(Constants.labelFont);
					g.drawString("Daemon starting", (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth("Daemon starting") / 2), (Engine.getHeight() / 2) + 40);
				}else{
					g.setColor(Constants.labelColor);
					g.setFont(Constants.labelFont);
					g.drawString("Wallet starting", (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth("Wallet starting") / 2), (Engine.getHeight() / 2) + 40);
				}
					
			}
			
			break;
		case OPTIONS_MENU:		
			break;
		case PAUSE_MENU:		
			break;
		case PLAYING:
			break;
		case GAME_OVER:
			break;
		case CREDITS:
			break;
		default:
			break;
		}
		
		if(Constants.isEnableFps()){
			g.setColor(Color.WHITE);
			g.setFont(Constants.labelFont);
			g.drawString("FPS: " + FPS, 80, 35);
		}
	}
	
	@Override
	public void resize() {
		canvas.resize();
		
		if(Constants.navbar != null) Constants.navbar.resize();
		for(BaseGui bg : Constants.guiInterfaces) bg.resize();
	}
	
	@Override
	public void quit() {		
		Processes.killByName("dcrd");
		Processes.killByName("dcrwallet");
	}

}
