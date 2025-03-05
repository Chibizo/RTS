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
	
	private final  int imageSize = GameConfiguration.BLOCK_SIZE*4;
	
	public void paint(Map map, Graphics graphics, Graphics2D g2) {
		int blockSize=GameConfiguration.BLOCK_SIZE;
		Position[][] block=map.getBlock();
		
		for(int lineIndex=0; lineIndex<map.getLineCount(); lineIndex++) {
			for(int columnIndex=0 ;columnIndex<map.getColumnCount();columnIndex++) {
				Position position=block[lineIndex][columnIndex];
				graphics.setColor(new Color(34, 139, 34));
				graphics.fillRect(position.getColumn() * blockSize, position.getLine() * blockSize, blockSize, blockSize);	

			}
		}
		for(int lineIndex=0; lineIndex<map.getLineCount(); lineIndex++) {
			for(int columnIndex=0 ;columnIndex<map.getColumnCount();columnIndex++) {
				Position position=block[lineIndex][columnIndex];
				if(map.isOnBorder(position) && (lineIndex+columnIndex)%3==0) {
					g2.drawImage(GameBuilder.readImage("src/images/medievalTile_48v2.png"),
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
	                g2.drawImage(GameBuilder.readImage("src/images/medievalEnvironment_11.png"),
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
						 g2.drawImage(GameBuilder.readImage("src/images/medievalEnvironment_01.png"),
			                        position.getColumn() * GameConfiguration.BLOCK_SIZE -15,
			                        position.getLine() * GameConfiguration.BLOCK_SIZE -30,
			                        imageSize, imageSize, null);
					}
					else {
							g2.drawImage(GameBuilder.readImage("src/images/medievalEnvironment_02.png"),
	                        position.getColumn() * GameConfiguration.BLOCK_SIZE -15,
	                        position.getLine() * GameConfiguration.BLOCK_SIZE -30,
	                        imageSize, imageSize, null);
					}
				}
				
				
				
		
			}
		}
		
		
		
	}
	
	
	
	public void paint(Building building,String type, Graphics2D g2) {
		if(type=="base") {
		    Position position = building.getZone().getPositions().get(0);
		    g2.drawImage(GameBuilder.readImage("src/images/medievalStructure_02.png"),
		    	position.getColumn() * GameConfiguration.BLOCK_SIZE-20,   
		        position.getLine() * GameConfiguration.BLOCK_SIZE-35, 
		        imageSize,
		        imageSize,
		        null);
		    
		    for (Position pos : building.getZone().getPositions()) {
			    g2.setColor(Color.GREEN);
		        ((Graphics2D) g2).setStroke(new BasicStroke(2));
		        g2.drawRect(
		            pos.getColumn() * GameConfiguration.BLOCK_SIZE,
		            pos.getLine() * GameConfiguration.BLOCK_SIZE,
		            GameConfiguration.BLOCK_SIZE + 10,
		            GameConfiguration.BLOCK_SIZE + 10);
		    }
		}
		else if (type=="barracks") {
		    Position position = building.getZone().getPositions().get(0);
		    g2.drawImage(GameBuilder.readImage("src/images/medievalStructure_05.png"),
			    	position.getColumn() * GameConfiguration.BLOCK_SIZE-20,   
			        position.getLine() * GameConfiguration.BLOCK_SIZE-35, 
			        imageSize,
			        imageSize,
			        null);

		}
	    
	}
	
	public void paint(Slave unit,Graphics g2) {
		Position position = unit.getZone().getPositions().get(0);
	    g2.drawImage(GameBuilder.readImage("src/images/medievalUnit_06.png"),
	    	position.getColumn() * GameConfiguration.BLOCK_SIZE-20,   
	        position.getLine() * GameConfiguration.BLOCK_SIZE-20, 
	        imageSize,
	        imageSize,
	        null);
	    
	    if (unit.isSelected()) {
	        g2.setColor(Color.GREEN);
	        ((Graphics2D) g2).setStroke(new BasicStroke(2));
	        g2.drawRect(
	            position.getColumn() * GameConfiguration.BLOCK_SIZE,
	            position.getLine() * GameConfiguration.BLOCK_SIZE,
	            GameConfiguration.BLOCK_SIZE + 10,
	            GameConfiguration.BLOCK_SIZE + 10
	        );
	    }
	    
	    int barWidth = GameConfiguration.BLOCK_SIZE;
        int barHeight = 5;
        int barX = position.getColumn() * GameConfiguration.BLOCK_SIZE+2;
        int barY = position.getLine() * GameConfiguration.BLOCK_SIZE -11;
        
        g2.setColor(Color.GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);
        
        float progress = (float) unit.getCurrentHealth() / 100.0f;
        g2.setColor(Color.red);
        g2.fillRect(barX, barY, (int)(barWidth * progress), barHeight);
	    
	    if(((Slave)unit).isHarvesting()||((Slave)unit).isReturning() ) {
            int barWidth2 = GameConfiguration.BLOCK_SIZE;
            int barHeight2 = 5;
            int barX2 = position.getColumn() * GameConfiguration.BLOCK_SIZE + 2;
            int barY2 = position.getLine() * GameConfiguration.BLOCK_SIZE + 20;
            
            g2.setColor(Color.GRAY);
            g2.fillRect(barX2, barY2, barWidth2, barHeight2);
            
            float progress2 = (float) ((Slave)unit).getResourceAmount() / 50.0f;
            if (((Slave)unit).getHarvestingResourceType().equals("wood")) {
            	g2.setColor(new Color(139, 69, 19));
            } else {
            	g2.setColor(new Color(0, 255, 255));
            }
            g2.fillRect(barX2, barY2, (int)(barWidth2 * progress2), barHeight2);
            
	    }
	}
	
	

	
	

}
