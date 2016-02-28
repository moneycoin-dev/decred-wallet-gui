package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.gui.BaseGui;
import com.hosvir.decredwallet.gui.Images;

/**
 *   
 * @author Troy
 *
 */
public class Navbar extends BaseGui {
	private int a;
	
	public void init(){
		selectedId = 1;
		
		rectangles = new Rectangle[(Images.getIcons().length / 2)];
		
		for(int i = 0; i < rectangles.length; i++){
			rectangles[i] = new Rectangle(Engine.getWidth() - ((i+1) * 52),
					3,
					Images.getIcons()[0].getWidth(),
					Images.getIcons()[0].getHeight());
		}
	}

	@Override
	public void render(Graphics2D g) {		
		//Navbar
		g.drawImage(Images.getInterfaces()[0], 
				0, 
				0, 
				Engine.getWidth(),
				60,
				null);
		
		//Icon
		g.drawImage(Images.getIcon(), 
				5, 
				5, 
				Images.getIcon().getWidth(),
				Images.getIcon().getHeight(),
				null);
		
		//Draw icons
		for(int i = 0; i < rectangles.length; i++){
			if(selectedId == i){
				a = i; 
				
				g.drawImage(Images.getInterfaces()[1], 
						rectangles[i].x, 
						0, 
						52,
						60,
						null);
			}else{
				a = i + rectangles.length;
			}
			
			g.drawImage(Images.getInterfaces()[2], 
					rectangles[i].x + 52, 
					0, 
					2,
					60,
					null);
			
			g.drawImage(Images.getIcons()[a], 
					rectangles[i].x, 
					rectangles[i].y, 
					rectangles[i].width,
					rectangles[i].height,
					null);
			

		}
		
		
		g.setColor(Color.PINK);
		
		if(hoverId != -1 && hoverId != selectedId){
			g.drawImage(Images.getIcons()[hoverId], 
					rectangles[hoverId].x, 
					rectangles[hoverId].y, 
					rectangles[hoverId].width,
					rectangles[hoverId].height,
					null);
		}
	}
	
	/**
	 * Resize the bounds
	 */
	public void resize() {
		for(int i = 0; i < rectangles.length; i++){
			rectangles[i].x = Engine.getWidth() - ((i+1) * 52);
		}
	}

	@Override
	public boolean isActive() {
		return true;
	}

}