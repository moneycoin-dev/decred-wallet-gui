package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class Logs extends Interface implements MouseWheelListener {	
	private int daemonStartLine = 0;
	private int walletStartLine = 0;
	private int guiStartLine = 0;
	private int daemonEndLine = 27;
	private int walletEndLine = 27;
	private int guiEndLine = 27;
	private int maxLines = 27;
	
	@Override
	public void init() {
		rectangles = new Rectangle[3];
		for(int i = 0; i < rectangles.length; i++){
			rectangles[i] = new Rectangle(i*170,60,170,70);
		}
		
		maxLines = (Engine.getHeight() - 220) / 18;
		
		Main.canvas.addMouseWheelListener(this);
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
		g.drawString(Constants.daemonButtonText, 40, 105);
		g.drawString(Constants.walletButtonText, 215, 105);
		g.drawString(Constants.guiButtonText, 405, 105);
		
		
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
				if(Constants.getDaemonProcess() != null && Constants.getDaemonProcess().log != null)
				for(int i = daemonStartLine; i < daemonEndLine; i++){
					if(i < Constants.getDaemonProcess().log.size())
						g.drawString(Constants.getDaemonProcess().log.get(i), 40, 175 + (i - daemonStartLine)*18);
				}
				break;
			case 1:
				if(Constants.getWalletProcess() != null)
				for(int i = walletStartLine; i < walletEndLine; i++){
					if(i < Constants.getWalletProcess().log.size())
						g.drawString(Constants.getWalletProcess().log.get(i), 40, 175 + (i - walletStartLine)*18);
				}
				break;
			case 2:
				for(int i = guiStartLine; i < guiEndLine; i++){
					if(i < Constants.guiLog.size())
						g.drawString(Constants.guiLog.get(i), 40, 175 + (i - guiStartLine)*18);
				}
				break;
			}
		}
	}
	
	@Override
	public void resize() {
		if(Constants.getDaemonProcess() != null && Constants.getWalletProcess() != null){
			maxLines = (Engine.getHeight() - 220) / 18;
			
			daemonStartLine = Constants.getDaemonProcess().log.size() - maxLines;
			walletStartLine = Constants.getWalletProcess().log.size() - maxLines;
			guiStartLine = Constants.guiLog.size() - maxLines;
			daemonEndLine = daemonStartLine + maxLines;
			walletEndLine = walletStartLine + maxLines;
			guiEndLine = guiStartLine + maxLines;
			
			if(daemonStartLine < 0) daemonStartLine = 0;
			if(walletStartLine < 0) walletStartLine = 0;
			if(guiStartLine < 0) guiStartLine = 0;
			if(daemonEndLine > Constants.getDaemonProcess().log.size() | daemonEndLine < maxLines) daemonEndLine = Constants.getDaemonProcess().log.size();
			if(walletEndLine > Constants.getWalletProcess().log.size() | walletEndLine < maxLines) walletEndLine = Constants.getWalletProcess().log.size();
			if(guiEndLine > Constants.guiLog.size() | guiEndLine < maxLines) guiEndLine = Constants.guiLog.size();
		}
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 1;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(isActive()){
			switch(selectedId){
			case 0:
				if(e.getUnitsToScroll() > 0){
					daemonStartLine += e.getScrollAmount();
				}else{
					daemonStartLine -= e.getScrollAmount();
				}
				
				if(daemonStartLine > Constants.getDaemonProcess().log.size()) daemonStartLine = Constants.getDaemonProcess().log.size()-1;
				if(daemonStartLine < 0) daemonStartLine = 0;
				daemonEndLine = daemonStartLine + maxLines;
				if(daemonEndLine > Constants.getDaemonProcess().log.size()) daemonEndLine = Constants.getDaemonProcess().log.size();
				break;
			case 1:
				if(e.getUnitsToScroll() > 0){
					walletStartLine += e.getScrollAmount();
				}else{
					walletStartLine -= e.getScrollAmount();
				}
				
				if(walletStartLine > Constants.getWalletProcess().log.size()) walletStartLine = Constants.getWalletProcess().log.size()-1;
				if(walletStartLine < 0) walletStartLine = 0;
				walletEndLine = walletStartLine + maxLines;
				if(walletEndLine > Constants.getWalletProcess().log.size()) walletEndLine = Constants.getWalletProcess().log.size();
				break;
			case 2:
				if(e.getUnitsToScroll() > 0){
					guiStartLine += e.getScrollAmount();
				}else{
					guiStartLine -= e.getScrollAmount();
				}
				
				if(guiStartLine > Constants.guiLog.size()) guiStartLine = Constants.guiLog.size()-1;
				if(guiStartLine < 0) guiStartLine = 0;
				guiEndLine = guiStartLine + maxLines;
				if(guiEndLine > Constants.guiLog.size()) guiEndLine = Constants.guiLog.size();
				break;

			}
		}
	}

}
