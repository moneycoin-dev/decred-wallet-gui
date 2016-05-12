package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.DropdownBox;
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
	
	@Override
	public void init() {
		headerThird = Engine.getWidth() / 6;
		
		this.components.add(new Label("from", Constants.getLangValue("From-Label"), 40, 190));
		this.components.add(new Label("limit", Constants.getLangValue("Limit-Label"), 40, 240));
		this.components.add(new Label("address", Constants.getLangValue("Address-Label"), 40, 290));
		
		DropdownBox from = new DropdownBox("langSelect", 250, 170, Engine.getWidth() - 295, 30, Constants.accountNames.toArray(new String[Constants.accountNames.size()]));
		from.text = "default";
		from.enabled = false;
		
		this.components.add(from);
		this.components.add(new InputBox("limitInput", 250, 220, Engine.getWidth() - 295, 30));
		this.components.add(new InputBox("addressInput", 250, 270, Engine.getWidth() - 295, 30));
		
		this.components.add(new Button("cancel", Constants.getLangValue("Cancel-Button-Text"), 40, 320, 100, 35, Constants.flatRed, Constants.flatRedHover));
		
		Button confirmButton = new Button("confirm", Constants.getLangValue("Confirm-Button-Text"), Engine.getWidth() - 140, 320, 100, 35, Constants.flatBlue, Constants.flatBlueHover);
		confirmButton.enabled = false;
		
		this.components.add(confirmButton);
		
		this.components.add(new Dialog("errordiag", ""));
	}
	
	@Override
	public void update(long delta) {
		super.update(delta);
		
		//Check if accounts are populated
		if(DropdownBox.class.cast(this.getComponentByName("langSelect")).lineItems == null || DropdownBox.class.cast(this.getComponentByName("langSelect")).lineItems.length == 0){
			DropdownBox.class.cast(this.getComponentByName("langSelect")).lineItems = Constants.accountNames.toArray(new String[Constants.accountNames.size()]);
		}
		
		//For each component
		for(Component c : components) {
			if(c.containsMouse && c.enabled) Main.containsMouse = true;
			
			//Drop down
			if(c instanceof DropdownBox) {
				//c.selectedId = -1;
			}
			
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
							getComponentByName("errordiag").text = Constants.getLangValue("Insufficient-Funds-Error");
							
							//Show dialog
							this.blockInput = true;
							Constants.navbar.blockInput = true;
							getComponentByName("errordiag").selectedId = 0;
						}else{
							Constants.log("Stake result: " + result);
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
				if(c.clickCount > 0) Constants.unselectOtherInputs(components, c);
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
		g.drawString(Constants.getLangValue("Pool-Label"), headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Pool-Label")) / 2), 90);
		g.drawString(Constants.getLangValue("Price-Label"), (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Price-Label")) / 2), 90);
		g.drawString(Constants.getLangValue("Tickets-Label"), (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Tickets-Label")) / 2), 90);
		g.drawString(Constants.getLangValue("Voted-Label"), (headerThird * 4) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Voted-Label")) / 2), 90);
		g.drawString(Constants.getLangValue("Missed-Label"), (headerThird * 5) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Missed-Label")) / 2), 90);
		g.drawString(Constants.getLangValue("Revoked-Label"), (headerThird * 6) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Revoked-Label")) / 2), 90);
		
		if(Constants.globalCache.stakeInfo != null && Constants.globalCache.stakeInfo.size() > 0){
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

}
