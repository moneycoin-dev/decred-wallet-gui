package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.InputBox;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Label;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class Staking extends Interface {
	private int headerThird;
	
	public void init() {
		headerThird = Engine.getWidth() / 6;
		
		this.components.add(new Label("from", Constants.fromLabel, 40, 190));
		this.components.add(new Label("limit", Constants.limitLabel, 40, 240));
		this.components.add(new Label("address", Constants.addressLabel, 40, 290));
		
		InputBox from = new InputBox("fromInput", 250, 170, Engine.getWidth() - 295, 30);
		from.text = "default";
		from.enabled = false;
		
		this.components.add(from);
		this.components.add(new InputBox("limitInput", 250, 220, Engine.getWidth() - 295, 30));
		this.components.add(new InputBox("addressInput", 250, 270, Engine.getWidth() - 295, 30));
		
		this.components.add(new Button("cancel", Constants.cancelButtonText, 40, 320, 100, 35, Constants.flatRed, Constants.flatRedHover));
		this.components.add(new Button("confirm", Constants.confirmButtonText, Engine.getWidth() - 140, 320, 100, 35, Constants.flatBlue, Constants.flatBlueHover));
		
		this.components.add(new Dialog("errordiag", ""));
	}
	
	@Override
	public void update(long delta) {
		super.update(delta);
		
		//For each component
		for(Component c : components) {
			if(c.containsMouse && c.enabled) Main.containsMouse = true;
			
			//Buttons
			if(c instanceof Button) {
				if(c.selectedId == 0 && c.enabled){
					switch(c.name){
					case "cancel":
						getComponentByName("limitInput").text = "";
						getComponentByName("limitInput").selectedId = -1;
						getComponentByName("addressInput").text = "";
						getComponentByName("addressInput").selectedId = -1;
						break;
					case "confirm":
						String result = Api.purchaseTicket(getComponentByName("fromInput").text, getComponentByName("limitInput").text, getComponentByName("addressInput").text);
						
						if(result.contains("not enough to purchase sstx")){
							Constants.log("Insufficient funds: " + result);
							getComponentByName("errordiag").text = Constants.insufficientFundsError;
							
							//Show dialog
							this.blockInput = true;
							Constants.navbar.blockInput = true;
							getComponentByName("errordiag").selectedId = 0;
						}else{
							Constants.globalCache.forceUpdateInfo = true;
						}
						
						getComponentByName("limitInput").text = "";
						getComponentByName("limitInput").selectedId = -1;
						getComponentByName("addressInput").text = "";
						getComponentByName("addressInput").selectedId = -1;
						
						break;
					}
				}
				
				//Release button
				c.selectedId = -1;
			}
			
			//Input boxes
			if(c instanceof InputBox) {
				if(c.clickCount > 0) unselectOtherInputs(c);
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		//Second nav
		g.setColor(Color.WHITE);
		g.fillRect(0, 
				60, 
				Engine.getWidth(), 
				60);
		
		g.drawImage(Images.getInterfaces()[19], 
				0, 
				120, 
				Engine.getWidth(),
				60,
				null);
		
		
		//Draw info
		g.setColor(Constants.labelColor);
		g.setFont(Constants.labelFont);
		g.drawString("Pool", headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth("Pool") / 2), 90);
		g.drawString("Price", (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth("Price") / 2), 90);
		g.drawString("Tickets", (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth("Tickets") / 2), 90);
		g.drawString("Voted", (headerThird * 4) - (headerThird / 2) - (g.getFontMetrics().stringWidth("Voted") / 2), 90);
		g.drawString("Missed", (headerThird * 5) - (headerThird / 2) - (g.getFontMetrics().stringWidth("Missed") / 2), 90);
		g.drawString("Revoked", (headerThird * 6) - (headerThird / 2) - (g.getFontMetrics().stringWidth("Revoked") / 2), 90);
		
		if(Constants.globalCache.stakeInfo != null){
			g.setColor(Constants.walletBalanceColor);
			g.setFont(Constants.walletBalanceFont);
			g.drawString(Constants.globalCache.stakeInfo.get(0).getValueByName("poolsize"), headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.stakeInfo.get(0).getValueByName("poolsize")) / 2), 110);
			g.drawString(Constants.globalCache.stakeInfo.get(0).getValueByName("difficulty"), (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.stakeInfo.get(0).getValueByName("difficulty")) / 2), 110);
			g.drawString(Constants.globalCache.stakeInfo.get(0).getValueByName("live"), (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.stakeInfo.get(0).getValueByName("live")) / 2), 110);
			g.drawString(Constants.globalCache.stakeInfo.get(0).getValueByName("voted"), (headerThird * 4) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.stakeInfo.get(0).getValueByName("voted")) / 2), 110);
			g.drawString(Constants.globalCache.stakeInfo.get(0).getValueByName("missed"), (headerThird * 5) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.stakeInfo.get(0).getValueByName("missed")) / 2), 110);
			g.drawString(Constants.globalCache.stakeInfo.get(0).getValueByName("revoked"), (headerThird * 6) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.stakeInfo.get(0).getValueByName("revoked")) / 2), 110);
		}
		
		//Content box
		g.setColor(Color.WHITE);
		g.fillRect(30, 
				150, 
				Engine.getWidth() - 60, 
				220);
		
		g.drawImage(Images.getInterfaces()[7], 
				Engine.getWidth() - 30, 
				150, 
				10,
				220,
				null);
		
		g.drawImage(Images.getInterfaces()[6], 
				20, 
				150, 
				10,
				220,
				null);
		
		g.drawImage(Images.getInterfaces()[19], 
				24, 
				358, 
				Engine.getWidth() - 48,
				60,
				null);
		
		
		//Render
		super.render(g);
	}
	
	@Override
	public void resize() {
		getComponentByName("fromInput").width = Engine.getWidth() - 295;
		getComponentByName("limitInput").width = Engine.getWidth() - 295;
		getComponentByName("addressInput").width = Engine.getWidth() - 295;
		getComponentByName("confirm").x = Engine.getWidth() - 140;

		getComponentByName("fromInput").resize();
		getComponentByName("limitInput").resize();
		getComponentByName("addressInput").resize();
		getComponentByName("confirm").resize();
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 4;
	}
	
	/**
	 * Unselect the input boxes.
	 * 
	 * @param ibb
	 */
	public void unselectOtherInputs(Component cc) {
		for(Component c : components)
			if(c instanceof InputBox)
				if(c != cc) c.selectedId = -1;
	}
	
	/**
	 * Unselect all input boxes.
	 * 
	 * @param ibb
	 */
	public void unselectAllInputs() {
		for(Component c : components)
			if(c instanceof InputBox)
				c.selectedId = -1;
	}

}
