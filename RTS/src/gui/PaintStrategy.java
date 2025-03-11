package gui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.math.*;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

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
	
	
	public void paint(Map map,String previewBuildingType,Point previewMousePosition,Graphics2D g2) {
        int x = previewMousePosition.x / GameConfiguration.BLOCK_SIZE;
        int y = previewMousePosition.y / GameConfiguration.BLOCK_SIZE;
        
        ArrayList<Position> previewPositions = new ArrayList<>();
        previewPositions.add(new Position(y, x));
        previewPositions.add(new Position(y+1, x));
        previewPositions.add(new Position(y, x+1));
        previewPositions.add(new Position(y+1, x+1));
        Zone previewZone = new Zone(previewPositions);
        
        boolean validPosition = true;
        for (Position pos : previewPositions) {
        	if(map.isOnBorder(pos) || map.isfull(pos) || map.isfullUnits(pos)) {
        		validPosition=false;
        		break;
        	}
        }
        AlphaComposite alphaComposite;
        alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        
        
        Composite originalComposite = g2.getComposite();
        g2.setComposite(alphaComposite);
        
        if (previewBuildingType.equals("barracks")) {
            if (validPosition) {
                g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalStructure_05.png"),
                    x * GameConfiguration.BLOCK_SIZE - 6,
                    y * GameConfiguration.BLOCK_SIZE,
                    imageSize,imageSize, null);
            }
        }
        
        g2.setComposite(originalComposite);
        
	}
	
	
	
	public void paint(Building building, String type, Graphics2D g2) {
	    Position position = building.getZone().getPositions().get(0);
	    
	    if (building.isUnderConstruction()) {
	        float progress = building.getConstructionProgress();
	        
	        
	        int barWidth = GameConfiguration.BLOCK_SIZE * 2;
	        int barHeight = 8;
	        int barX = position.getColumn() * GameConfiguration.BLOCK_SIZE;
	        int barY = position.getLine() * GameConfiguration.BLOCK_SIZE - 15;
	        
	        g2.setColor(Color.GRAY);
	        g2.fillRect(barX, barY, barWidth, barHeight);
	        
	        g2.setColor(Color.BLUE);
	        g2.fillRect(barX, barY, (int)(barWidth * progress), barHeight);
	        
	        int remainingTime = building.getRemainingConstructionTime();
	        String timeText = remainingTime + "s";
	        g2.setColor(Color.WHITE);
	        g2.setFont(new Font("Arial",Font.BOLD,12));
	        g2.drawString(timeText, barX + barWidth/2 - 10, barY - 5);
	        
	        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	        Composite originalComposite = g2.getComposite();
	        g2.setComposite(alphaComposite);
	        
	        if (type == "base") {
	            g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalStructure_02.png"),
	                position.getColumn() * GameConfiguration.BLOCK_SIZE + 5,
	                position.getLine() * GameConfiguration.BLOCK_SIZE - 22,
	                imageSize, imageSize, null);
	        } else if (type == "barracks") {
	            g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalStructure_05.png"),
	                position.getColumn() * GameConfiguration.BLOCK_SIZE - 6,
	                position.getLine() * GameConfiguration.BLOCK_SIZE,
	                imageSize, imageSize, null);
	        }
	        
	        g2.setComposite(originalComposite);
	    } else {
	        if (type == "base") {

	            g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalStructure_02.png"),
	                position.getColumn() * GameConfiguration.BLOCK_SIZE + 5,
	                position.getLine() * GameConfiguration.BLOCK_SIZE - 22,
	                imageSize, imageSize, null);
	            
	        } else if (type == "barracks") {
	            g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalStructure_05.png"),
	                position.getColumn() * GameConfiguration.BLOCK_SIZE - 6,
	                position.getLine() * GameConfiguration.BLOCK_SIZE,
	                imageSize, imageSize, null);
	            /**
	            for (Position pos : building.getZone().getPositions()) {
	                g2.setColor(Color.GREEN);
	                ((Graphics2D) g2).setStroke(new BasicStroke(2));
	                g2.drawRect(
	                    pos.getColumn() * GameConfiguration.BLOCK_SIZE,
	                    pos.getLine() * GameConfiguration.BLOCK_SIZE,
	                    GameConfiguration.BLOCK_SIZE + 10,
	                    GameConfiguration.BLOCK_SIZE + 10);
	            }
	            **/
	        }
	    }
	}
	
	public void paint(Unit unit,String name, Graphics2D g2) {
		 Position position = unit.getZone().getPositions().get(0);
		    
		    if (unit.isUnderConstruction()) {
		        float progress = unit.getConstructionProgress();
		        
		        int barWidth = GameConfiguration.BLOCK_SIZE;
		        int barHeight = 8;
		        int barX = position.getColumn() * GameConfiguration.BLOCK_SIZE;
		        int barY = position.getLine() * GameConfiguration.BLOCK_SIZE - 15;
		        
		        g2.setColor(Color.GRAY);
		        g2.fillRect(barX, barY, barWidth, barHeight);
		        
		        g2.setColor(Color.BLUE);
		        g2.fillRect(barX, barY, (int)(barWidth * progress), barHeight);
		        
		        int remainingTime = unit.getRemainingConstructionTime();
		        String timeText = remainingTime + "s";
		        g2.setColor(Color.WHITE);
		        g2.setFont(new Font("Arial", Font.BOLD, 12));
		        g2.drawString(timeText, barX + barWidth/2 - 10, barY - 5);
		        
		        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		        Composite originalComposite = g2.getComposite();
		        g2.setComposite(alphaComposite);
		        
		        if(name=="warrior") {
			        g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalUnit_08.png"),
			            position.getColumn() * GameConfiguration.BLOCK_SIZE - 20,
			            position.getLine() * GameConfiguration.BLOCK_SIZE - 20,
			            imageSize, imageSize, null);
		    	}
		        g2.setComposite(originalComposite);
		    } else {
		    	if(name=="warrior") {
			        g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalUnit_08.png"),
			            position.getColumn() * GameConfiguration.BLOCK_SIZE - 20,
			            position.getLine() * GameConfiguration.BLOCK_SIZE - 20,
			            imageSize, imageSize, null);
		    	}
			    
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
		        
		        float progress = (float) unit.getCurrentHealth() / 200.0f;
		        g2.setColor(Color.GREEN);
		        g2.fillRect(barX, barY, (int)(barWidth * progress), barHeight);
		    }
			 
		
	}
	
	public void paint(Slave unit, Graphics2D g2) {
	    Position position = unit.getZone().getPositions().get(0);
	    
	    if (unit.isUnderConstruction()) {
	        float progress = unit.getConstructionProgress();
	        
	        int barWidth = GameConfiguration.BLOCK_SIZE;
	        int barHeight = 8;
	        int barX = position.getColumn() * GameConfiguration.BLOCK_SIZE;
	        int barY = position.getLine() * GameConfiguration.BLOCK_SIZE - 15;
	        
	        g2.setColor(Color.GRAY);
	        g2.fillRect(barX, barY, barWidth, barHeight);
	        
	        g2.setColor(Color.BLUE);
	        g2.fillRect(barX, barY, (int)(barWidth * progress), barHeight);
	        
	        int remainingTime = unit.getRemainingConstructionTime();
	        String timeText = remainingTime + "s";
	        g2.setColor(Color.WHITE);
	        g2.setFont(new Font("Arial", Font.BOLD, 12));
	        g2.drawString(timeText, barX + barWidth/2 - 10, barY - 5);
	        
	        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	        Composite originalComposite = g2.getComposite();
	        g2.setComposite(alphaComposite);
	        
	        g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalUnit_06.png"),
	            position.getColumn() * GameConfiguration.BLOCK_SIZE - 20,
	            position.getLine() * GameConfiguration.BLOCK_SIZE - 20,
	            imageSize, imageSize, null);
	        
	        g2.setComposite(originalComposite);
	    } else {
	    	
	     
		    g2.drawImage(GameBuilder.readImage("RTS/src/images/medievalUnit_06.png"),
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
	        g2.setColor(Color.GREEN);
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
    public void drawSelectionRectangle(Point selectionStart,Point selectionEnd,Graphics2D g2) {
		int x = Math.min(selectionStart.x, selectionEnd.x);
		int y = Math.min(selectionStart.y, selectionEnd.y);
		int width = Math.abs(selectionEnd.x - selectionStart.x);
		int height = Math.abs(selectionEnd.y - selectionStart.y);
		
		g2.setColor(new Color(0, 255, 0, 80)); 
		g2.fillRect(x, y, width, height);
		
		g2.setColor(Color.GREEN);
		g2.setStroke(new BasicStroke(2));
		g2.drawRect(x, y, width, height);
	}
	
	

	
	

}
