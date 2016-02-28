package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Account;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.BaseGui;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.InputBox;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class Send extends BaseGui {
	private int headerThird;
	private Button[] buttons;
	private InputBox[] inputs;
	private boolean readyToSend;
	
	public void init() {
		headerThird = (Engine.getWidth() - 200) / 4;
		buttons = new Button[2];
		buttons[0] = new Button("Cancel", 350, 500, 100, 35, Constants.flatRed, Constants.flatRedHover);
		buttons[1] = new Button("Send", Engine.getWidth() - 150, 500, 100, 35, Constants.flatBlue, Constants.flatBlueHover);
		buttons[1].enabled = false;
		
		inputs = new InputBox[5];
		inputs[0] = new InputBox(500,200,Engine.getWidth() - 545,30,Constants.settingsSelectedColor,Constants.flatBlue);
		inputs[1] = new InputBox(500,250,Engine.getWidth() - 545,30,Constants.settingsSelectedColor,Constants.flatBlue);
		inputs[2] = new InputBox(500,300,Engine.getWidth() - 545,80,Constants.settingsSelectedColor,Constants.flatBlue);
		inputs[3] = new InputBox(500,400,Engine.getWidth() - 545,30,Constants.settingsSelectedColor,Constants.flatBlue);
		inputs[4] = new InputBox(500,450,Engine.getWidth() - 545,30,Constants.settingsSelectedColor,Constants.flatBlue);
		
		inputs[0].enabled = false;
		inputs[3].text = "" + Constants.globalCache.walletFee;
	}

	public void update(long delta) {
		if(!blockInput){
			buttons[0].y = 500;
			if(rectangles == null && Constants.accounts.size() > 0){
				rectangles = new Rectangle[Constants.accounts.size()];
				
				for(int i = 0; i < rectangles.length; i++){
					rectangles[i] = new Rectangle(0,
							60 + i*60,
							295,
							60);
				}
			}
			
			super.update(delta);
			
			//Assign name to input box
			if(inputs[0].text != Constants.accounts.get(selectedId).name)
				inputs[0].text = Constants.accounts.get(selectedId).name;
			
			//Set fee
			if(inputs[3].text == "0.00") inputs[3].text = "" + Constants.globalCache.walletFee;
			
			//Enable send
			if(inputs[1].text != "" && inputs[3].text != "" && inputs[4].text != "") buttons[1].enabled = true; else buttons[1].enabled = false;
			
			//Check for sending
			if(Constants.getPrivatePassPhrase() != null && inputs[1].text != "") {
				if(inputs[3].text != String.valueOf(Api.getWalletFee())){
					if(Api.setTxFee(Double.valueOf(inputs[3].text))){
						readyToSend = true;
					}else{
						readyToSend = false;
					}
				}else{
					readyToSend = true;
				}
				
				if(readyToSend){
					Api.unlockWallet(30);
					
					String txId = Api.sendFrom(inputs[0].text, inputs[1].text, inputs[2].text, Double.parseDouble(inputs[4].text));
					if(txId == ""){
						Constants.log("Unable to send DCR. " + txId);
					}else{
						Constants.log("Sucess, transaction id: " + txId);
					}
				}else{
					Constants.log("Unable to set Wallet fee, sending cancelled.");
				}
				
				//Force update accounts
				for(Account a : Constants.accounts) a.forceUpdate = true;
				
				Constants.setPrivatePassPhrase(null);
				resetForm();
			}
		
			for(Button b : buttons) {
				b.update(delta);
				
				if(b.containsMouse) Main.containsMouse = true;
				
				if(b.selectedId == 0 && b.enabled){
					switch(b.text){
					case "Cancel":
						resetForm();
						break;
					case "Send":
						blockInput = true;
						Constants.navbar.blockInput = true;
						unselectAllInputs();
						Constants.guiInterfaces.get(Constants.guiInterfaces.size() -1).selectedId = 0;
						break;
					}			
					
					//Release button
					b.selectedId = -1;
				}
			}
			
			for(InputBox ib : inputs) {
				ib.update(delta);
				
				if(ib.containsMouse && ib.enabled) Main.containsMouse = true;
				
				//Unselect other inputs
				if(ib.selectedId == 0) unselectOtherInputs(ib);
			}
		
			
			//Rename account
			if(doubleClicked && !Constants.accounts.get(selectedId).name.startsWith("imported")){
				Constants.accountToRename = Constants.accounts.get(selectedId).name;
				blockInput = true;
				Constants.navbar.blockInput = true;
				Constants.guiInterfaces.get(Constants.guiInterfaces.size() - 2).selectedId = 0;
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		//Header
		g.setColor(Color.WHITE);
		g.fillRect(0, 
				60, 
				Engine.getWidth(),
				95);
		
		g.drawImage(Images.getInterfaces()[19], 
				0, 
				150, 
				Engine.getWidth(),
				60,
				null);
		
		//Sidebar
		g.fillRect(0, 
				60, 
				290,
				Engine.getHeight());
		
		g.drawImage(Images.getInterfaces()[5], 
				290, 
				60, 
				10,
				Engine.getHeight(),
				null);
		
		
		if(rectangles != null){
			for(int i = 0; i < rectangles.length; i++){				
				//Selected wallet
				g.setColor(Constants.settingsSelectedColor);
				if(i == selectedId || i == hoverId){
					g.fillRect(rectangles[i].x, 
							rectangles[i].y, 
							rectangles[i].width,
							rectangles[i].height);
				}
				
				g.fillRect(rectangles[i].x, 
						rectangles[i].y + 60, 
						rectangles[i].width,
						1);
				
				//Wallet Labels
				g.setColor(Constants.walletNameColor);
				g.setFont(Constants.walletNameFont);
				g.drawString(Constants.accounts.get(i).name, 6, 98 + i*60);
				
				
				//Wallet Balance
				g.setColor(Constants.walletBalanceColor);
				g.setFont(Constants.walletBalanceFont);
				g.drawString("" + Constants.accounts.get(i).balance, 285 - g.getFontMetrics().stringWidth("" + Constants.accounts.get(i).balance), 98 + i*60);
			}
		}
		
		if(Constants.accounts.size() > 0){
			//DCR and Balance
			g.setColor(Constants.walletBalanceColor);
			g.setFont(Constants.dcrFont);
			g.drawString("DCR", Engine.getWidth() / 2, 100);
			
			g.setColor(Constants.walletNameColor);
			g.setFont(Constants.totalBalanceFont);
			g.drawString("" + Constants.accounts.get(selectedId).balance, (Engine.getWidth() + 150) / 2, 100);
			
			
			//Available, Pending and Locked
			g.setColor(Constants.labelColor);
			g.setFont(Constants.labelFont);
			g.drawString("Available", Engine.getWidth() - (headerThird * 3), 125);
			g.drawString("Pending", Engine.getWidth() - (headerThird * 2), 125);
			g.drawString("Locked", Engine.getWidth() - (headerThird * 1), 125);
			
			g.setColor(Constants.walletBalanceColor);
			g.setFont(Constants.walletBalanceFont);
			g.drawString("" + Constants.accounts.get(selectedId).balance, Engine.getWidth() - (headerThird * 3) + 30 - (g.getFontMetrics().stringWidth("" + Constants.accounts.get(selectedId).balance) / 2), 145);
			g.drawString("" + Constants.accounts.get(selectedId).pendingBalance, Engine.getWidth() - (headerThird * 2) + 30 - (g.getFontMetrics().stringWidth("" + Constants.accounts.get(selectedId).pendingBalance) / 2), 145);
			g.drawString("" + Constants.accounts.get(selectedId).lockedBalance, Engine.getWidth() - (headerThird * 1) + 30 - (g.getFontMetrics().stringWidth("" + Constants.accounts.get(selectedId).lockedBalance) / 2), 145);
		
			
			//Content box
			g.drawImage(Images.getInterfaces()[6], 
					320, 
					180, 
					10,
					370,
					null);
			
			g.setColor(Color.WHITE);
			g.fillRect(330, 
					180, 
					Engine.getWidth() - 360, 
					370);
			
			g.drawImage(Images.getInterfaces()[7], 
					Engine.getWidth() - 30, 
					180, 
					10,
					370,
					null);
			
			g.drawImage(Images.getInterfaces()[19], 
					324, 
					540, 
					Engine.getWidth() - 348,
					60,
					null);
			
			
			//Draw log data
			g.setFont(Constants.labelFont);
			g.setColor(Constants.labelColor);
			
			g.drawString("From", 350, 220);
			g.drawString("To", 350, 270);
			g.drawString("Description", 350, 345);
			g.drawString("Fee", 350, 425);
			g.drawString("Amount", 350, 475);
			
			for(Button b : buttons) b.render(g);
			for(InputBox ib : inputs) ib.render(g);
		}
		
		
	}
	
	public void resize() {
		headerThird = (Engine.getWidth() - 200) / 4;
		buttons[1].x = Engine.getWidth() - 150;
		
		for(Button b : buttons) b.resize();
		for(InputBox ib : inputs) {
			ib.width = Engine.getWidth() - 545; 
			ib.resize();
		}
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 3;
	}
	
	/**
	 * Unselect the input boxes.
	 * 
	 * @param ibb
	 */
	public void unselectOtherInputs(InputBox ibb) {
		for(InputBox ib : inputs)
			if(ib != ibb) ib.selectedId = -1;
	}
	
	/**
	 * Unselect all input boxes.
	 * 
	 * @param ibb
	 */
	public void unselectAllInputs() {
		for(InputBox ib : inputs)
			ib.selectedId = -1;
	}
	
	/**
	 * Reset all the fields on the form.
	 */
	public void resetForm() {
		for(int i = 1; i < inputs.length; i++)
			inputs[i].text = "";
		
		inputs[3].text = "" + Constants.globalCache.walletFee;
	}

}
