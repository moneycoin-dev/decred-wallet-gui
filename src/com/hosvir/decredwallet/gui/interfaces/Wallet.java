package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class Wallet extends Interface {
	private int headerThird;
	private String[] dateString;
	
	public void init() {
		headerThird = (Engine.getWidth() - 200) / 4;
		
		this.components.add(new Button("add", Constants.addButtonText, 20, Engine.getHeight() - 80, 255, 35, Constants.flatBlue, Constants.flatBlueHover));
	}

	public void update(long delta) {
		if(!blockInput){
			if(rectangles == null && Constants.accounts.size() > 0){
				rectangles = new Rectangle[Constants.accounts.size()];
				
				for(int i = 0; i < rectangles.length; i++){
					rectangles[i] = new Rectangle(0,
							60 + i*60,
							295,
							60);
				}
			}
			
			//Update
			super.update(delta);
			
			//For each component
			for(Component c : components) {
				if(c.containsMouse && c.enabled) Main.containsMouse = true;
				
				if(c instanceof Button) {
					if(c.selectedId == 0 && c.enabled){
						switch(c.name){
						case "add":
							blockInput = true;
							Constants.navbar.blockInput = true;
							Constants.guiInterfaces.get(Constants.guiInterfaces.size() -3).selectedId = 0;
							break;
						}
						
						//Release button
						c.selectedId = -1;
					}
				}
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
			g.drawString(Constants.dcrLabel, Engine.getWidth() / 2, 100);
			
			g.setColor(Constants.walletNameColor);
			g.setFont(Constants.totalBalanceFont);
			g.drawString(Constants.accounts.get(selectedId).balance, (Engine.getWidth() + 150) / 2, 100);
			
			
			//Available, Pending and Locked
			g.setColor(Constants.labelColor);
			g.setFont(Constants.labelFont);
			g.drawString(Constants.availableLabel, Engine.getWidth() - (headerThird * 3), 125);
			g.drawString(Constants.pendingLabel, Engine.getWidth() - (headerThird * 2), 125);
			g.drawString(Constants.lockedLabel, Engine.getWidth() - (headerThird * 1), 125);
			
			g.setColor(Constants.walletBalanceColor);
			g.setFont(Constants.walletBalanceFont);
			g.drawString(Constants.accounts.get(selectedId).balance, Engine.getWidth() - (headerThird * 3) + 30 - (g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).balance) / 2), 145);
			g.drawString(Constants.accounts.get(selectedId).pendingBalance, Engine.getWidth() - (headerThird * 2) + 30 - (g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).pendingBalance) / 2), 145);
			g.drawString(Constants.accounts.get(selectedId).lockedBalance, Engine.getWidth() - (headerThird * 1) + 30 - (g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).lockedBalance) / 2), 145);
			
			
			//Transactions
			if(selectedId == 0){
			for(int i = 0; i < Constants.globalCache.transactions.size(); i++){
				if(180 + i*70 < Engine.getHeight()){
					g.drawImage(Images.getInterfaces()[6], 
							310, 
							180 + i*70, 
							10,
							50,
							null);
					
					g.setColor(Color.WHITE);
					g.fillRect(320, 
							180 + i*70, 
							Engine.getWidth() - 345,
							45);
					
					g.drawImage(Images.getInterfaces()[7], 
							Engine.getWidth() - 25, 
							180 + i*70, 
							10,
							50,
							null);
					
					g.drawImage(Images.getInterfaces()[19], 
							315, 
							180 + i*70 + 41, 
							Engine.getWidth() - 334,
							50,
							null);
					
					switch(Constants.globalCache.transactions.get(i).getValueByName("category")){
					case "send":
						g.setColor(Constants.flatRed);
						break;
					case "receive":
						g.setColor(Constants.flatGreen);
						break;
					case "generate":
						g.setColor(Constants.flatYellow);
						break;
					}
					
					g.fillRect(314, 
							180 + i*70, 
							5,
							50);
					
					
					//Draw date
					dateString = Constants.getWalletDate(Long.parseLong(Constants.globalCache.transactions.get(i).getValueByName("timereceived"))).split(":");
					g.setColor(Constants.walletBalanceColor);
					g.setFont(Constants.walletBalanceFont);
					g.drawString(dateString[0], 330, 212 + i*70);
					
					g.setColor(Constants.labelColor);
					g.setFont(Constants.transactionFont);
					g.drawString(dateString[1].replaceAll("!", ":"), 322, 226 + i*70);
					
					//Draw address
					g.setColor(Constants.walletBalanceColor);
					g.setFont(Constants.addressFont);
					g.drawString(Constants.globalCache.transactions.get(i).getValueByName("address"), 
							(Engine.getWidth() - (g.getFontMetrics().stringWidth(Constants.globalCache.transactions.get(i).getValueByName("address")) / 2)) / 2, 
							204 + i*70);
					
					//Draw transaction
					g.setColor(Constants.labelColor);
					g.setFont(Constants.transactionFont);
					g.drawString(Constants.globalCache.transactions.get(i).getValueByName("txid"), 
							(Engine.getWidth() - (g.getFontMetrics().stringWidth(Constants.globalCache.transactions.get(i).getValueByName("txid")) / 2)) / 2, 
							226 + i*70);
					
					//Draw amount
					g.setColor(Constants.walletBalanceColor);
					g.setFont(Constants.walletBalanceFont);
					g.drawString(Constants.globalCache.transactions.get(i).getValueByName("amount").replace("-", "- "), (Engine.getWidth() - 30 - g.getFontMetrics().stringWidth(Constants.globalCache.transactions.get(i).getValueByName("amount").replace("-", "- "))), 212 + i*70);
					
					//Draw tx fee
					if(Constants.globalCache.transactions.get(i).getValueByName("fee") != null){
						g.setColor(Constants.labelColor);
						g.setFont(Constants.transactionFont);
						g.drawString(Constants.globalCache.transactions.get(i).getValueByName("fee").replace("-", "- "), (Engine.getWidth() - 30 - g.getFontMetrics().stringWidth(Constants.globalCache.transactions.get(i).getValueByName("fee").replace("-", "- "))), 226 + i*70);
					}
				}
			}
			}
		}
		
		//Render
		super.render(g);
	}
	
	public void resize() {
		headerThird = (Engine.getWidth() - 200) / 4;
		
		getComponentByName("add").y = Engine.getHeight() - 80;
		getComponentByName("add").resize();
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 6;
	}

}
