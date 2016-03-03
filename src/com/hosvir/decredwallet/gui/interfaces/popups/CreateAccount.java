package com.hosvir.decredwallet.gui.interfaces.popups;

import java.awt.Color;
import java.awt.Graphics2D;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.InputBox;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class CreateAccount extends Interface {
	
	public void init() {
		selectedId = -1;
		InputBox passphrase = new InputBox("passphrase", (Engine.getWidth() / 2) - 250,(Engine.getHeight() / 2) + 40,500,30);
		passphrase.textHidden = true;
		
		this.components.add(new InputBox("account", (Engine.getWidth() / 2) - 250,Engine.getHeight() / 2,500,30));
		this.components.add(passphrase);
		this.components.add(new Button("cancel", Constants.cancelButtonText, 30, (Engine.getHeight() / 2) + 50, 100, 35, Constants.flatRed, Constants.flatRedHover));
		this.components.add(new Button("confirm", Constants.confirmButtonText, Engine.getWidth() - 130, (Engine.getHeight() / 2) + 50, 100, 35, Constants.flatBlue, Constants.flatBlueHover));
	}
	
	@Override
	public void update(long delta) {
		//Update
		super.update(delta);
		
		//For each component
		for(Component c : components) {
			if(c.containsMouse && c.enabled) Main.containsMouse = true;
					
			//Buttons
			if(c instanceof Button) {
				if(c.selectedId == 0){
					switch(c.name){
					case "cancel":
						Constants.blockInterfaces(false, this);
						getComponentByName("account").text = "";
						getComponentByName("account").selectedId = -1;
						getComponentByName("passphrase").text = "";
						getComponentByName("passphrase").selectedId = -1;
						this.selectedId = -1;
						break;
					case "confirm":
						Constants.setPrivatePassPhrase(getComponentByName("passphrase").text);
						Api.unlockWallet("30");
						Api.createNewAccount(getComponentByName("account").text);
						Constants.reloadAccounts();
						Constants.blockInterfaces(false, this);
						getComponentByName("account").text = "";
						getComponentByName("account").selectedId = -1;
						getComponentByName("passphrase").text = "";
						getComponentByName("passphrase").selectedId = -1;
						this.selectedId = -1;
						break;
					}
				}
					
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
		g.setColor(Constants.transparentBlack);
		g.fillRect(0, 0, Engine.getWidth(), Engine.getHeight());
		
		g.setColor(Color.WHITE);
		g.fillRect(0, (Engine.getHeight() / 2) - 100, Engine.getWidth(), 200);
		
		//Label
		g.setFont(Constants.labelFont);
		g.setColor(Constants.labelColor);
		
		g.drawString(Constants.addAccountMessage, (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(Constants.addAccountMessage) / 2), Engine.getHeight() / 2 - 50);
		
		
		//Render
		super.render(g);
	}
	
	@Override
	public boolean isActive() {
		return selectedId == 0;
	}
	
	@Override
	public void resize() {
		getComponentByName("account").x = (Engine.getWidth() / 2) - 250; 
		getComponentByName("account").y = Engine.getHeight() / 2 - 20; 
		getComponentByName("account").resize();
		
		getComponentByName("passphrase").x = (Engine.getWidth() / 2) - 250; 
		getComponentByName("passphrase").y = (Engine.getHeight() / 2) + 20;
		getComponentByName("passphrase").resize();
		
		getComponentByName("cancel").y = (Engine.getHeight() / 2) + 50;
		getComponentByName("confirm").y = (Engine.getHeight() / 2) + 50;
		getComponentByName("confirm").x = Engine.getWidth() - 130;
		
		getComponentByName("cancel").resize();
		getComponentByName("confirm").resize();
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
