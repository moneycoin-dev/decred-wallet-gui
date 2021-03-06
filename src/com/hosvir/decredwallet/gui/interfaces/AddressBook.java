package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class AddressBook extends Interface implements MouseWheelListener {
	private int scrollOffset = 0;
	private int scrollMinHeight = 130;
	private int scrollMaxHeight;
	private int scrollCurrentPosition = 130;
	
	@Override
	public void init() {		
		this.components.add(new Button("add", Constants.getLangValue("Add-Button-Text"),20,80,100,35,Constants.flatBlue,Constants.flatBlueHover));
		this.components.add(new Dialog("clipboard", Constants.getLangValue("Clipboard-Message")));
		Main.canvas.addMouseWheelListener(this);
		
		scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2);
	}
	
	@Override
	public void update(long delta) {
		//Update
		super.update(delta);
		//rectangles = null;
		
		if(!blockInput){
			//Set rectangles for remove buttons
			if(rectangles == null && Constants.contacts.size() > 0){
				rectangles = new Rectangle[Constants.contacts.size() * 4];
				
				for(int i = 0; i < (rectangles.length/4); i++){
					rectangles[i] = new Rectangle(Engine.getWidth() - 69, 150 + i*70 - scrollOffset, 50,50);
					rectangles[Constants.contacts.size()+i] = new Rectangle(30, 150 + i*70 - scrollOffset, 150,26);
					rectangles[(Constants.contacts.size()*2)+i] = new Rectangle(30, 176 + i*70 - scrollOffset, 150,22);
					rectangles[(Constants.contacts.size()*3)+i] = new Rectangle((Engine.getWidth() / 2) - (400 / 2) - 100, 150 + i*70 - scrollOffset, 400, 50);
				}
			}
			
			//Check for clicking on rectangle
			if(clickCount > 0){	
				if(selectedId > Constants.contacts.size()-1){
					if(selectedId < (Constants.contacts.size()*2)){
						Constants.setClipboardString(Constants.contacts.get(selectedId - Constants.contacts.size()).getName());
					}else if(selectedId < (Constants.contacts.size()*3)){
						Constants.setClipboardString(Constants.contacts.get(selectedId - (Constants.contacts.size()*2)).getEmail());
					}else{
						Constants.setClipboardString(Constants.contacts.get(selectedId - (Constants.contacts.size()*3)).getAddress());
					}
					
					getComponentByName("clipboard").text = Constants.getLangValue("Clipboard-Message") + ": " + Constants.getClipboardString();
					getComponentByName("clipboard").selectedId = 0;
				}else{
					Constants.removeContact(Constants.contacts.get(selectedId));
					rectangles = null;
				}
				
				clickCount = 0;
			}
			
			//For each component
			for(Component c : components) {
				if(c.containsMouse) Main.containsMouse = true;
				
				if(c instanceof Button){
					if(c.selectedId == 0 && c.enabled){
						blockInput = true;
						Constants.navbar.blockInput = true;
						Constants.guiInterfaces.get(Constants.guiInterfaces.size() -4).selectedId = 0;
						
						c.selectedId = -1;
					}
				}
				
				if(c instanceof Dialog){
					if(c.selectedId == 0){
						//Show dialog
						this.blockInput = true;
						Constants.navbar.blockInput = true;
						c.selectedId = 0;
					}
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g) {		
		
		//Render contacts
		for(int i = 0; i < Constants.contacts.size(); i++){
			if(150 + i*70 - scrollOffset < Engine.getHeight() && 150 + i*70 - scrollOffset > 80){
				g.drawImage(Images.getInterfaces()[6], 
						20, 
						150 + i*70 - scrollOffset, 
						10,
						50,
						null);
				
				g.setColor(Color.WHITE);
				g.fillRect(20, 
						150 + i*70 - scrollOffset, 
						Engine.getWidth() - 45,
						45);
				
				g.drawImage(Images.getInterfaces()[7], 
						Engine.getWidth() - 25, 
						150 + i*70 - scrollOffset, 
						10,
						50,
						null);
				
				g.drawImage(Images.getInterfaces()[19], 
						20, 
						150 + i*70 + 41 - scrollOffset, 
						Engine.getWidth() - 39,
						50,
						null);
				
				//Draw remove button
				if(hoverId == i) g.setColor(Constants.flatRedHover); else g.setColor(Constants.flatRed);
				g.fillRect(Engine.getWidth() - 69, 
						150 + i*70 - scrollOffset, 
						50,
						50);
				
				g.drawImage(Images.getRemoveIcon(),
						Engine.getWidth() - 69 + 9, 
						150 + i*70 + 9 - scrollOffset,
						Images.getRemoveIcon().getWidth(),
						Images.getRemoveIcon().getHeight(),
						null);
				
				//Draw name
				if(hoverId == Constants.contacts.size()+i) g.setColor(Constants.flatBlue); else g.setColor(Constants.walletBalanceColor);
				g.setFont(Constants.walletBalanceFont);
				g.drawString(Constants.contacts.get(i).getName(), 30, 173 + i*70 - scrollOffset);
				
				//Email
				if(hoverId == (Constants.contacts.size()*2)+i) g.setColor(Constants.flatBlue); else g.setColor(Constants.labelColor);
				g.setFont(Constants.transactionFont);
				g.drawString(Constants.contacts.get(i).getEmail(), 30, 190 + i*70 - scrollOffset);
				
				//Address
				if(hoverId == (Constants.contacts.size()*3)+i) g.setColor(Constants.flatBlue); else g.setColor(Constants.walletBalanceColor);
				g.setFont(Constants.addressFont);
				g.drawString(Constants.contacts.get(i).getAddress(), (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(Constants.contacts.get(i).getAddress()) / 2) - 100, 182 + i*70 - scrollOffset);
			}
		}
		
		//Scroll bar
		if(Constants.contacts.size() > 0) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(Engine.getWidth() - 10, 100, Engine.getWidth() - 10, Engine.getHeight());
			g.fillRect(Engine.getWidth() - 10, scrollCurrentPosition, 10, 60);
		}
		
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
		
		g.setColor(Constants.walletNameColor);
		g.setFont(Constants.totalBalanceFont);
		g.drawString(Constants.contacts.size() + "", (Engine.getWidth() / 2), 110);
		
		//Render
		super.render(g);
	}
	
	@Override
	public void resize() {
		rectangles = null;
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 2;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(isActive()){
			if(e.getUnitsToScroll() > 0){
				scrollOffset += 70;
				if(Constants.contacts.size() > 0)
				scrollCurrentPosition += (Engine.getHeight() - scrollMinHeight - 60) / (Constants.contacts.size() -1);
			}else{
				scrollOffset -= 70;
				if(Constants.contacts.size() > 0)
				scrollCurrentPosition -= (Engine.getHeight() - scrollMinHeight - 60) / (Constants.contacts.size() -1);
			}
			
			if(scrollOffset < 0) scrollOffset = 0;
			if(scrollOffset > (Constants.contacts.size()-1)*70) scrollOffset = (Constants.contacts.size()-1)*70;
			
			if(Constants.contacts.size() > 0){
				scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2);
				if(scrollCurrentPosition < scrollMinHeight) scrollCurrentPosition = scrollMinHeight;
				if(scrollCurrentPosition > scrollMaxHeight) scrollCurrentPosition = scrollMaxHeight;
			}
			
			rectangles = null;
		}
	}

}
