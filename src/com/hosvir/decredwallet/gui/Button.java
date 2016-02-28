package com.hosvir.decredwallet.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.hosvir.decredwallet.Constants;

/**
 * 
 * @author Troy
 *
 */
public class Button extends BaseGui {
	public String text;
	public int x;
	public int y;
	public int width;
	public int height;
	public Color bgColor;
	public Color hoverColor;
	public boolean enabled;
	
	/**
	 * Construct a new button.
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param bgColor
	 * @param hoverColor
	 */
	public Button(String text, int x, int y, int width, int height, Color bgColor, Color hoverColor) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bgColor = bgColor;
		this.hoverColor = hoverColor;
		
		this.rectangles = new Rectangle[1];
		this.rectangles[0] = new Rectangle(x,y,width,height);
		
		this.selectedId = -1;
		this.enabled = true;
	}

	@Override
	public void render(Graphics2D g) {
		if(hoverId != -1) g.setColor(hoverColor); else g.setColor(bgColor);
		if(!enabled) g.setColor(Constants.settingsSelectedColor);
		
		g.fillRoundRect(x, y, width, height, 10, 10);
		
		g.setColor(Color.WHITE);
		g.setFont(Constants.settingsFont);
		g.drawString(text, x + (width / 2) - (g.getFontMetrics().stringWidth(text) / 2), y + (height / 2) + 8);
	}
	
	public void resize() {
		for(Rectangle r : rectangles){
			r.x = x;
			r.y = y;
		}
	}

	@Override
	public boolean isActive() {
		return this.selectedId == 0;
	}


}
