package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.BaseGui;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.InputBox;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class PassphrasePopup extends BaseGui {
	private InputBox passphrase;
	private Button[] buttons;
	
	public void init() {
		selectedId = -1;
		passphrase = new InputBox((Engine.getWidth() / 2) - 250,Engine.getHeight() / 2,500,30,Constants.settingsSelectedColor,Constants.flatBlue);
		passphrase.password = true;
		
		buttons = new Button[2];
		buttons[0] = new Button("Cancel", 30, (Engine.getHeight() / 2) + 50, 100, 35, Constants.flatRed, Constants.flatRedHover);
		buttons[1] = new Button("Confirm", Engine.getWidth() - 130, (Engine.getHeight() / 2) + 50, 100, 35, Constants.flatBlue, Constants.flatBlueHover);
	}
	
	public void update(long delta) {
		super.update(delta);
		
		//Passphrase
		passphrase.update(delta);
		if(passphrase.containsMouse && passphrase.enabled) Main.containsMouse = true;
		
		//Buttons
		for(Button b : buttons) {
			b.update(delta);
			
			if(b.containsMouse) Main.containsMouse = true;
			
			if(b.selectedId == 0){
				switch(b.text){
				case "Cancel":
					Constants.navbar.blockInput = false;
					Constants.blockInterfaces(false, this);
					passphrase.text = "";
					passphrase.selectedId = -1;
					this.selectedId = -1;
					break;
				case "Confirm":
					Constants.setPrivatePassPhrase(passphrase.text);
					Constants.navbar.blockInput = false;
					Constants.blockInterfaces(false, this);
					passphrase.text = "";
					this.selectedId = -1;
					break;
				}
			}
			
			b.selectedId = -1;
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
		
		g.drawString("Enter your passphrase to continue", (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth("Enter your passphrase to continue") / 2), Engine.getHeight() / 2 - 30);
		
		
		//Input box and buttons
		passphrase.render(g);
		for(Button b : buttons) b.render(g);
	}
	
	@Override
	public boolean isActive() {
		return selectedId == 0;
	}
	
	@Override
	public void resize() {
		passphrase.x = (Engine.getWidth() / 2) - 250; 
		passphrase.y = Engine.getHeight() / 2; 
		passphrase.resize();
		
		buttons[0].y = (Engine.getHeight() / 2) + 50;
		buttons[1].y = (Engine.getHeight() / 2) + 50;
		buttons[1].x = Engine.getWidth() - 130;
		
		for(Button b : buttons) b.resize();
	}

}
