package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class Receive extends Interface {
	private int headerThird;
	private String newAddress;
	private int newAddressId;
	
	public void init() {
		headerThird = (Engine.getWidth() - 200) / 4;
		
		this.components.add(new Button("new", Constants.getNewButtonText, Engine.getWidth() - 150, 200, 100, 35, Constants.flatBlue, Constants.flatBlueHover));
		this.components.add(new Dialog("newaddr", Constants.newAddressMessage));
	}

	public void update(long delta) {
		//Update
		super.update(delta);
		
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
			
			//Get new address
			if(getComponentByName("new").containsMouse) Main.containsMouse = true;
			
			if(getComponentByName("new").selectedId == 0 ){
				newAddress = Api.getNewAddress(Constants.accounts.get(selectedId).name).trim();
				Constants.setClipboardString(newAddress);
				newAddressId = selectedId;
				
				//Release button
				getComponentByName("new").selectedId = -1;
				
				//Show dialog
				this.blockInput = true;
				Constants.navbar.blockInput = true;
				getComponentByName("newaddr").selectedId = 0;
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
			g.drawString("" + Constants.accounts.get(selectedId).balance, (Engine.getWidth() + 150) / 2, 100);
			
			
			//Available, Pending and Locked
			g.setColor(Constants.labelColor);
			g.setFont(Constants.labelFont);
			g.drawString(Constants.availableLabel, Engine.getWidth() - (headerThird * 3), 125);
			g.drawString(Constants.pendingLabel, Engine.getWidth() - (headerThird * 2), 125);
			g.drawString(Constants.lockedLabel, Engine.getWidth() - (headerThird * 1), 125);
			
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
					Engine.getHeight() - 230,
					null);
			
			g.setColor(Color.WHITE);
			g.fillRect(330, 
					180, 
					Engine.getWidth() - 360, 
					Engine.getHeight() - 230);
			
			g.drawImage(Images.getInterfaces()[7], 
					Engine.getWidth() - 30, 
					180, 
					10,
					Engine.getHeight() - 230,
					null);
			
			g.drawImage(Images.getInterfaces()[19], 
					324, 
					Engine.getHeight() - 60, 
					Engine.getWidth() - 348,
					60,
					null);
			
			
			//Draw address		
			g.setColor(Constants.walletBalanceColor);
			g.setFont(Constants.addressFont);
			
			if(newAddress != null && selectedId == newAddressId)
				g.drawString(newAddress, 340, 220);
			else
				g.drawString(Constants.accounts.get(selectedId).addresses[0], 340, 220);
			
			
			//Render
			super.render(g);
		}
		
		
	}
	
	public void resize() {
		headerThird = (Engine.getWidth() - 200) / 4;
		
		getComponentByName("new").x = Engine.getWidth() - 150;
		getComponentByName("new").resize();
		getComponentByName("newaddr").resize();
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 2;
	}

}
