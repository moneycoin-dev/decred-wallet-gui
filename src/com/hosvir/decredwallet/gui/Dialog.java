package com.hosvir.decredwallet.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;

/**
 * 
 * @author Troy
 *
 */
public class Dialog extends Component {
	private Component okButton;
	
	/**
	 * Construct a new label.
	 * 
	 * @param name
	 * @param text
	 * @param x
	 * @param y
	 */
	public Dialog(String name, String text) {
		super(name, text, 4, 0, 0, 600, 150);
		this.textFont = Constants.labelFont;
		this.textColor = Constants.walletBalanceColor;
		this.selectedId = -1;
		
		this.okButton = new Button("ok", "Ok", (Engine.getWidth() / 2) + 185, (Engine.getHeight() / 2) + 25, 100, 35, Constants.flatBlue, Constants.flatBlueHover);
	}
	
	@Override
	public void update(long delta) {
		super.update(delta);
		
		if(isActive()){
			okButton.update(delta);
			
			if(okButton.containsMouse) Main.containsMouse = true;
			
			if(okButton.selectedId == 0){
				okButton.selectedId = -1;
				Constants.blockInterfaces(false, null);
				this.selectedId = -1;
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		if(isActive()){
			g.setColor(Constants.transparentBlack);
			g.fillRect(0, 0, Engine.getWidth(), Engine.getHeight());
			
			g.setColor(Color.WHITE);
			g.fillRoundRect((Engine.getWidth() / 2) - (width / 2), (Engine.getHeight() / 2) - (height / 2), width, height, 10, 10);
			
			g.setColor(textColor);
			g.setFont(textFont);
			g.drawString(text, (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(text) / 2), (Engine.getHeight() / 2));
			
			//Render
			okButton.render(g);
		}
	}

	@Override
	public boolean isActive() {
		return this.selectedId == 0;
	}
	
	@Override
	public void resize() {
		okButton.x = (Engine.getWidth() / 2) + 185;
		okButton.y = (Engine.getHeight() / 2) + 25;
		okButton.resize();
	}

}