package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.deadendgine.Engine;
import com.deadendgine.input.Mouse;
import com.hosvir.decredwallet.Api;
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
public class Settings extends Interface implements MouseWheelListener {
	private int headerThird;
	private int scrollOffset = 0;
	private Rectangle[] peerRectangles;
	public int peerHoverId = -1;
	
	public void init() {
		headerThird = Engine.getWidth() / 3;
		
		rectangles = new Rectangle[3];
		for(int i = 0; i < rectangles.length; i++){
			rectangles[i] = new Rectangle(i*170,60,170,70);
		}
		
		this.components.add(new Label("lang", Constants.languageLabel, 40, 190));
		
		DropdownBox dropbox = new DropdownBox("langSelect", 250, 170, Engine.getWidth() - 295, 30, Constants.getLangFiles().toArray(new String[Constants.getLangFiles().size()]));
		dropbox.text = Constants.langFile;
		
		this.components.add(dropbox);
		
		Main.canvas.addMouseWheelListener(this);
	}
	
	@Override
	public synchronized void update(long delta) {
		super.update(delta);
		
		//For each component
		for(Component c : components) {
			if(selectedId == 0 && c.containsMouse) Main.containsMouse = true;
							
			if(c instanceof DropdownBox) {
				if(c.text != Constants.langFile) {
					Constants.langFile = c.text.replace(".conf", "");
					Constants.reloadLanguage();
				}
			}
		}
		
		//Peer rectangles
		if(selectedId == 2){
			if(peerRectangles == null && Constants.globalCache.peers.size() > 0){
				peerRectangles = new Rectangle[Constants.globalCache.peers.size()];
				
				for(int i = 0; i < peerRectangles.length; i++){
					peerRectangles[i] = new Rectangle(Engine.getWidth() - 69, 225 + i*70 - scrollOffset, 50,50);
				}
			}
			
			if(peerRectangles != null){
				for(int i = 0; i < peerRectangles.length; i++){
					if(peerRectangles[i] != null && peerRectangles[i].contains(Mouse.point)){
						containsMouse = true;
						peerHoverId = i;
		
						if(Mouse.isMouseDown(MouseEvent.BUTTON1)){
							Api.disconnectPeer(Constants.globalCache.peers.get(i).getValueByName("id"));
							Mouse.release(MouseEvent.BUTTON1);
							Constants.globalCache.forceUpdatePeers = true;
						}
					}
				}
			}
			
			if(!containsMouse) peerHoverId = -1;
		}
	}

	@Override
	public synchronized void render(Graphics2D g) {
		switch(selectedId){
		case 0: //Main
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
			
			//Render
			super.render(g);
			break;
		case 1: //Security
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
			break;
		case 2: //Network	
			if(Constants.globalCache.peers != null)
			for(int i = 0; i < Constants.globalCache.peers.size(); i++) {
				if(150 + i*70 - scrollOffset < Engine.getHeight() && 150 + i*70 - scrollOffset > 80 && Constants.globalCache.peers.get(i).getValueByName("addr") != null){
					g.drawImage(Images.getInterfaces()[6], 
							20, 
							225 + i*70 - scrollOffset, 
							10,
							50,
							null);
					
					g.setColor(Color.WHITE);
					g.fillRect(25, 
							225 + i*70 - scrollOffset, 
							Engine.getWidth() - 45,
							45);
					
					g.drawImage(Images.getInterfaces()[7], 
							Engine.getWidth() - 25, 
							225 + i*70 - scrollOffset, 
							10,
							50,
							null);
					
					g.drawImage(Images.getInterfaces()[19], 
							25, 
							225 + i*70 + 41 - scrollOffset, 
							Engine.getWidth() - 49,
							50,
							null);
					
					//Draw remove button
					if(peerHoverId == i) g.setColor(Constants.flatRedHover); else g.setColor(Constants.flatRed);
					g.fillRect(Engine.getWidth() - 69, 
							225 + i*70 - scrollOffset, 
							50,
							50);
					
					g.drawImage(Images.getRemoveIcon(),
							Engine.getWidth() - 69 + 9, 
							225 + i*70 + 9 - scrollOffset,
							Images.getRemoveIcon().getWidth(),
							Images.getRemoveIcon().getHeight(),
							null);
					
					// TODO format bytes to getMod(), rectangles, disconnect peer, force globalCache update
										
					//Address
					g.setColor(Constants.walletBalanceColor);
					g.setFont(Constants.addressFont);
					g.drawString(Constants.globalCache.peers.get(i).getValueByName("addr"), 35, 257 + i*70 - scrollOffset);
					
					//Block height
					if(Constants.globalCache.peers.get(i).getValueByName("currentheight") != null)
					g.drawString(Constants.globalCache.peers.get(i).getValueByName("currentheight"), (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.peers.get(i).getValueByName("currentheight")) / 2), 257 + i*70 - scrollOffset);
					
					//Receive
					g.setColor(Constants.flatGreen);
					g.drawString(Engine.formatBytes(Long.parseLong(Constants.globalCache.peers.get(i).getValueByName("bytesrecv"))), Engine.getWidth() - 190, 247 + i*70 - scrollOffset);
					
					//Send
					g.setColor(Constants.flatRed);
					g.drawString(Engine.formatBytes(Long.parseLong(Constants.globalCache.peers.get(i).getValueByName("bytesrecv"))), Engine.getWidth() - 190, 267 + i*70 - scrollOffset);
					
					//Receive/Send last activity time
					g.setColor(Constants.labelColor);
					g.setFont(Constants.labelFont);
					g.drawString(Constants.formatDate(Long.parseLong(Constants.globalCache.peers.get(i).getValueByName("lastrecv"))), Engine.getWidth() - 400, 247 + i*70 - scrollOffset);
					g.drawString(Constants.formatDate(Long.parseLong(Constants.globalCache.peers.get(i).getValueByName("lastsend"))), Engine.getWidth() - 400, 267 + i*70 - scrollOffset);
				}
			}
			
			
			//Content box
			g.drawImage(Images.getInterfaces()[6], 
					10, 
					140, 
					10,
					65,
					null);
			
			g.setColor(Color.WHITE);
			g.fillRect(20, 
					140, 
					Engine.getWidth() - 40, 
					65);
			
			g.drawImage(Images.getInterfaces()[7], 
					Engine.getWidth() - 20, 
					140, 
					10,
					65,
					null);
			
			g.drawImage(Images.getInterfaces()[19], 
					14, 
					195, 
					Engine.getWidth() - 28,
					60,
					null);
			
			
			//Available, Pending and Locked
			g.setColor(Constants.labelColor);
			g.setFont(Constants.labelFont);
			g.drawString(Constants.blocksLabel, headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.blocksLabel) / 2), 170);
			g.drawString(Constants.difficultyLabel, (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.difficultyLabel) / 2), 170);
			g.drawString(Constants.peersLabel, (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.peersLabel) / 2), 170);
			
			g.setColor(Constants.walletBalanceColor);
			g.setFont(Constants.walletBalanceFont);
			g.drawString(Constants.globalCache.info.get(0).getValueByName("blocks"), headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.info.get(0).getValueByName("blocks")) / 2), 190);
			g.drawString(Constants.globalCache.info.get(0).getValueByName("difficulty"), (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.info.get(0).getValueByName("difficulty")) / 2), 190);
			g.drawString(Constants.globalCache.peers.size() + "", (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.peers.size() + "") / 2), 190);
			
			break;
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
	}
	
	@Override
	public void resize() {
		peerRectangles = null;
		headerThird = Engine.getWidth() / 3;
		
		getComponentByName("langSelect").width = Engine.getWidth() - 295;
		getComponentByName("langSelect").resize();
		
		super.resize();
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 0;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(isActive()){
			if(e.getUnitsToScroll() > 0){
				scrollOffset += Constants.scrollDistance;
			}else{
				scrollOffset -= Constants.scrollDistance;
			}
			
			if(scrollOffset < 0) scrollOffset = 0;
			if(scrollOffset > (Constants.globalCache.peers.size()-1)*70) scrollOffset = (Constants.globalCache.peers.size()-1)*70;
			
			peerRectangles = null;
		}
	}

}
