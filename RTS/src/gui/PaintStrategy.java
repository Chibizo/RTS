package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import config.GameConfiguration;
import data.map.Map;
import data.map.Position;
import data.mobile.*;
import engine.process.GameBuilder;
import data.map.*;



public class PaintStrategy {
	

	
	public void paint(Map map, Graphics graphics, Graphics2D g2) {
		int blockSize=GameConfiguration.BLOCK_SIZE;
		Position[][] block=map.getBlock();
		
		for(int lineIndex=0; lineIndex<map.getLineCount(); lineIndex++) {
			for(int columnIndex=0 ;columnIndex<map.getColumnCount();columnIndex++) {
				int imageSize = GameConfiguration.BLOCK_SIZE*4;
				Position position=block[lineIndex][columnIndex];
				graphics.setColor(new Color(34, 139, 34));
				graphics.fillRect(position.getColumn() * blockSize, position.getLine() * blockSize, blockSize, blockSize);	

			}
		}
		for(int lineIndex=0; lineIndex<map.getLineCount(); lineIndex++) {
			for(int columnIndex=0 ;columnIndex<map.getColumnCount();columnIndex++) {
				int imageSize = GameConfiguration.BLOCK_SIZE*4;
				Position position=block[lineIndex][columnIndex];
				if(map.isOnBorder(position) && (lineIndex+columnIndex)%3==0) {
					g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalTile_48v2.png"),
							position.getColumn() * GameConfiguration.BLOCK_SIZE -(imageSize - GameConfiguration.BLOCK_SIZE)/2 ,
							position.getLine() * GameConfiguration.BLOCK_SIZE -(imageSize - GameConfiguration.BLOCK_SIZE)/2,   
							imageSize,
					        imageSize,
					        null);
				}
				/**
				 * ressource magic ore
				 */
				if((position.getLine() == 55 || position.getLine() == 56) && 
					    (position.getColumn() == 22 || position.getColumn() == 23)) {
	                g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalEnvironment_11.png"),
	                        position.getColumn() * GameConfiguration.BLOCK_SIZE -15,
	                        position.getLine() * GameConfiguration.BLOCK_SIZE -30,
	                        imageSize, imageSize, null);
				}
				/**
				 * ressource wood
				 */
				if((position.getLine() >= 44 && position.getLine() <= 46) && 
					    (position.getColumn() >= 6 && position.getColumn() <= 8)) {
					if ((position.getLine() + position.getColumn()) % 2 == 0) {
						 g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalEnvironment_01.png"),
			                        position.getColumn() * GameConfiguration.BLOCK_SIZE -15,
			                        position.getLine() * GameConfiguration.BLOCK_SIZE -30,
			                        imageSize, imageSize, null);
					}
					else {
							g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalEnvironment_02.png"),
	                        position.getColumn() * GameConfiguration.BLOCK_SIZE -15,
	                        position.getLine() * GameConfiguration.BLOCK_SIZE -30,
	                        imageSize, imageSize, null);
					}
				}
				
				
				
		
			}
		}
		
		
		
	}
	
	
	
	public void paint(Building building, Graphics2D g2) {
	    Position position = building.getZone().getPositions().get(0);
	    int imageSize = GameConfiguration.BLOCK_SIZE*4;
	    g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalStructure_02.png"),
	    	position.getColumn() * GameConfiguration.BLOCK_SIZE-20,   
	        position.getLine() * GameConfiguration.BLOCK_SIZE-35, 
	        imageSize,
	        imageSize,
	        null);
	    
	}
	
	public void paint(Unit unit,Graphics g2) {
		Position position = unit.getZone().getPositions().get(0);
	    int imageSize = GameConfiguration.BLOCK_SIZE*4;
	    g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalUnit_08.png"),
	    	position.getColumn() * GameConfiguration.BLOCK_SIZE-20,   
	        position.getLine() * GameConfiguration.BLOCK_SIZE-35, 
	        imageSize,
	        imageSize,
	        null);
	    
	    if (unit.isSelected()) {
	        g2.setColor(Color.GREEN);
	        ((Graphics2D) g2).setStroke(new BasicStroke(2));
	        g2.drawRect(
	            position.getColumn() * GameConfiguration.BLOCK_SIZE - 5,
	            position.getLine() * GameConfiguration.BLOCK_SIZE - 5,
	            GameConfiguration.BLOCK_SIZE + 10,
	            GameConfiguration.BLOCK_SIZE + 10
	        );
	    }
	    
	    if(((Slave)unit).isHarvesting()||((Slave)unit).isReturning() ) {
            int barWidth = GameConfiguration.BLOCK_SIZE;
            int barHeight = 5;
            int barX = position.getColumn() * GameConfiguration.BLOCK_SIZE;
            int barY = position.getLine() * GameConfiguration.BLOCK_SIZE + 10;
            
            g2.setColor(Color.GRAY);
            g2.fillRect(barX, barY, barWidth, barHeight);
            
            float progress = (float) ((Slave)unit).getResourceAmount() / 50.0f;
            if (((Slave)unit).getHarvestingResourceType().equals("wood")) {
            	g2.setColor(new Color(139, 69, 19));
            } else {
            	g2.setColor(new Color(0, 255, 255));
            }
            g2.fillRect(barX, barY, (int)(barWidth * progress), barHeight);
            
	    }
	}
	
	

	
	

}
