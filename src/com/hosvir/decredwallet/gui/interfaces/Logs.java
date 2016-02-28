package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.BaseGui;
import com.hosvir.decredwallet.gui.Images;

/**
 * 
 * @author Troy
 *
 */
public class Logs extends BaseGui {	
	private int daemonStartLine = 0;
	private int walletStartLine = 0;
	private int guiStartLine = 0;
	private int maxLines = 27;
	
	public void init() {
		rectangles = new Rectangle[3];
		for(int i = 0; i < rectangles.length; i++){
			rectangles[i] = new Rectangle(i*170,60,170,70);
		}
		
		maxLines = (Engine.getHeight() - 220) / 18;
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
		g.drawString("DAEMON", 40, 105);
		g.drawString("WALLET", 215, 105);
		g.drawString("GUI", 405, 105);
		
		
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
		g.setFont(Constants.labelFont);
		g.setColor(Constants.labelColor);
		
		if(daemonStartLine > -1){
			switch(selectedId){
			case 0:
				for(int i = daemonStartLine; i < Constants.getDaemonProcess().log.size(); i++){
					g.drawString(Constants.getDaemonProcess().log.get(i), 40, 175 + (i - daemonStartLine)*18);
				}
				break;
			case 1:
				for(int i = walletStartLine; i < Constants.getWalletProcess().log.size(); i++){
					g.drawString(Constants.getWalletProcess().log.get(i), 40, 175 + (i - walletStartLine)*18);
				}
				break;
			case 2:
				for(int i = guiStartLine; i < Constants.guiLog.size(); i++){
					g.drawString(Constants.guiLog.get(i), 40, 175 + (i - guiStartLine)*18);
				}
				break;
			}
		}
	}
	
	public void resize() {
		if(Constants.getDaemonProcess() != null && Constants.getWalletProcess() != null){
			maxLines = (Engine.getHeight() - 220) / 18;
			daemonStartLine = Constants.getDaemonProcess().log.size() - maxLines;
			walletStartLine = Constants.getWalletProcess().log.size() - maxLines;
			guiStartLine = Constants.guiLog.size() - maxLines;
			
			if(daemonStartLine < 0) daemonStartLine = 0;
			if(walletStartLine < 0) walletStartLine = 0;
			if(guiStartLine < 0) guiStartLine = 0;
		}
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 1;
	}

}
