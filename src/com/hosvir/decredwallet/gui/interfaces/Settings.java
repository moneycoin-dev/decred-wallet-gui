package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.DropdownBox;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Label;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class Settings extends Interface {
	
	public void init() {
		rectangles = new Rectangle[3];
		for(int i = 0; i < rectangles.length; i++){
			rectangles[i] = new Rectangle(i*170,60,170,70);
		}
		
		this.components.add(new Label("lang", Constants.languageLabel, 40, 190));
		
		DropdownBox dropbox = new DropdownBox("langSelect", 250, 170, Engine.getWidth() - 295, 30, Constants.getLangFiles().toArray(new String[Constants.getLangFiles().size()]));
		dropbox.text = Constants.langFile;
		
		this.components.add(dropbox);
	}
	
	@Override
	public void update(long delta) {
		super.update(delta);
		
		//For each component
		for(Component c : components) {
			if(c.containsMouse) Main.containsMouse = true;
							
			if(c instanceof DropdownBox) {
				if(c.text != Constants.langFile) {
					Constants.langFile = c.text.replace(".conf", "");
					Constants.reloadLanguage();
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		//getComponentByName("langSelect").selectedId = -1;
		
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
		
		for(int i = 0; i < rectangles.length; i++){
			g.setColor(Constants.settingsSelectedColor);
			
			if(selectedId == i || hoverId == i){
				g.fillRect(rectangles[i].x, 
						rectangles[i].y, 
						rectangles[i].width, 
						rectangles[i].height);
			}

			g.fillRect(rectangles[i].x + 170, 
					rectangles[i].y, 
					2, 
					rectangles[i].height);
		}
		
		g.setFont(Constants.settingsFont);
		g.setColor(Constants.walletBalanceColor);
		g.drawString(Constants.mainButtonText, 60, 105);
		g.drawString(Constants.securityButtonText, 210, 105);
		g.drawString(Constants.networkButtonText, 375, 105);
		
		
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
		
		
		if(selectedId != 0){
			//Draw log data
			g.setFont(Constants.walletNameFont);
			g.setColor(Constants.labelColor);
		
			g.drawString("Under construction.", Engine.getWidth() / 2 - (g.getFontMetrics().stringWidth("Under construction.") / 2), Engine.getHeight() / 2);
		}else{
			super.render(g);
		}
	}
	
	@Override
	public void resize() {
		getComponentByName("langSelect").width = Engine.getWidth() - 295;
		getComponentByName("langSelect").resize();
		
		super.resize();
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 0;
	}

}
