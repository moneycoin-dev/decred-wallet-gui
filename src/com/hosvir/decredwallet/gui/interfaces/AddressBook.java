package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;

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
public class AddressBook extends Interface {
	
	public void init() {
		this.components.add(new Button("add", Constants.addButtonText,20,80,100,35,Constants.flatBlue,Constants.flatBlueHover));
	}
	
	@Override
	public void update(long delta) {
		super.update(delta);
		
		//For each component
		for(Component c : components) {
			if(c.containsMouse) Main.containsMouse = true;
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
		
		
		//Content box
		g.drawImage(Images.getInterfaces()[6], 
				20, 
				150, 
				10,
				Engine.getHeight() - 200,
				null);
		
		g.setColor(Color.WHITE);
		g.fillRect(30, 
				150, 
				Engine.getWidth() - 60, 
				Engine.getHeight() - 200);
		
		g.drawImage(Images.getInterfaces()[7], 
				Engine.getWidth() - 30, 
				150, 
				10,
				Engine.getHeight() - 200,
				null);
		
		g.drawImage(Images.getInterfaces()[19], 
				24, 
				Engine.getHeight() - 60, 
				Engine.getWidth() - 48,
				60,
				null);
		
		
		//Draw log data
		g.setFont(Constants.walletNameFont);
		g.setColor(Constants.labelColor);
		
		g.drawString("Under construction.", Engine.getWidth() / 2 - (g.getFontMetrics().stringWidth("Under construction.") / 2), Engine.getHeight() / 2);
		
		
		//Render
		super.render(g);
	}
	
	@Override
	public void resize() {
		
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 5;
	}

}
