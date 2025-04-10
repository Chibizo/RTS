package gui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.math.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


import config.GameConfiguration;
import data.map.Map;
import data.map.Position;
import data.mobile.*;
import data.model.Player;
import engine.process.GameBuilder;
import data.map.*;



public class PaintStrategy {
	
	
	private final  int imageSize = GameConfiguration.BLOCK_SIZE*4;
	private HashMap<String,Image> images=new HashMap<String,Image>();
	
	
	
	public void paint(Map map, Graphics graphics, Graphics2D g2) {
	    Zone magicOreLocations = map.getMagicOreLocations();
	    Zone woodLocations = map.getWoodLocations();
	    
	    // Chargement initial des images si nécessaire
	    
	    int blockSize = GameConfiguration.BLOCK_SIZE;
	    Position[][] block = map.getBlock();
	    
	    // Dessiner le terrain de base
	    for(int lineIndex=0; lineIndex<map.getLineCount(); lineIndex++) {
	        for(int columnIndex=0; columnIndex<map.getColumnCount(); columnIndex++) {
	            Position position = block[lineIndex][columnIndex];
	            graphics.setColor(new Color(34, 139, 34));
	            graphics.fillRect(position.getColumn() * blockSize, position.getLine() * blockSize, blockSize, blockSize);    
	        }
	    }
	    
	    // Dessiner les bordures et ressources
	    for(int lineIndex=0; lineIndex<map.getLineCount(); lineIndex++) {
	        for(int columnIndex=0; columnIndex<map.getColumnCount(); columnIndex++) {
	            Position position = block[lineIndex][columnIndex];
	            if(map.isOnBorder(position) && (lineIndex+columnIndex)%3==0) {
	                drawResourceImage("border", position, g2);
	            }
	            
	            // Ressource magic ore
	            if(((position.getLine() == 55 || position.getLine() == 56) && 
	                (position.getColumn() == 23 || position.getColumn() == 24)) ||
	               ((position.getLine() == 5 || position.getLine() == 6) && 
	                (position.getColumn() == 92 || position.getColumn() == 93)) ||
	               ((position.getLine() == 39 || position.getLine() == 40) && 
	                (position.getColumn() == 110 || position.getColumn() == 111))) {
	                drawResourceImage("magic_ore", position, g2);
	            }
	            
	            // Ressource wood
	            if(((position.getLine() >= 44 && position.getLine() <= 46) && 
	                (position.getColumn() >= 6 && position.getColumn() <= 8)) ||
	               ((position.getLine() >= 15 && position.getLine() <= 17) && 
	                (position.getColumn() >= 112 && position.getColumn() <= 114)) ||
	               ((position.getLine() >= 53 && position.getLine() <= 55) && 
	                (position.getColumn() >= 94 && position.getColumn() <= 96))) {
	                if ((position.getLine() + position.getColumn()) % 2 == 0) {
	                    drawResourceImage("wood_1", position, g2);
	                } else {
	                    drawResourceImage("wood_2", position, g2);
	                }
	            }
	        }
	    }
	}
	
	public void drawResourceImage(String resourceType, Position position, Graphics2D g2) {
	    if (!images.containsKey("magic_ore") && resourceType.equals("magic_ore")) {
	        images.put("magic_ore", GameBuilder.readImage("RTS/src/images/medievalEnvironment_11.png"));
	    }
	    if (!images.containsKey("wood_1") && resourceType.equals("wood_1")) {
	        images.put("wood_1", GameBuilder.readImage("RTS/src/images/medievalEnvironment_01.png"));
	    }
	    if (!images.containsKey("wood_2") && resourceType.equals("wood_2")) {
	        images.put("wood_2", GameBuilder.readImage("RTS/src/images/medievalEnvironment_02.png"));
	    }
	    if (!images.containsKey("border") && resourceType.equals("border")) {
	        images.put("border", GameBuilder.readImage("RTS/src/images/medievalTile_48v2.png"));
	    }
	    
	    int x = position.getColumn() * GameConfiguration.BLOCK_SIZE;
	    int y = position.getLine() * GameConfiguration.BLOCK_SIZE;
	    
	    if (resourceType.equals("magic_ore") && images.containsKey("magic_ore")) {
	        g2.drawImage(images.get("magic_ore"), x - 15, y - 30, imageSize, imageSize, null);
	    } else if (resourceType.equals("wood_1") && images.containsKey("wood_1")) {
	        g2.drawImage(images.get("wood_1"), x - 15, y - 30, imageSize, imageSize, null);
	    } else if (resourceType.equals("wood_2") && images.containsKey("wood_2")) {
	        g2.drawImage(images.get("wood_2"), x - 15, y - 30, imageSize, imageSize, null);
	    } else if (resourceType.equals("border") && images.containsKey("border")) {
	        g2.drawImage(images.get("border"), x - (imageSize - GameConfiguration.BLOCK_SIZE)/2, 
	                     y - (imageSize - GameConfiguration.BLOCK_SIZE)/2, imageSize, imageSize, null);
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
                g2.drawImage(GameBuilder.readImage("RTS/src/images/barracks_hum.png"),
                    x * GameConfiguration.BLOCK_SIZE - 6,
                    y * GameConfiguration.BLOCK_SIZE,
                    imageSize,imageSize, null);
            }
        }else if(previewBuildingType.equals("runway")) {
        	if(validPosition) {
        		 g2.drawImage(GameBuilder.readImage("RTS/src/images/runway_human.png"),
                         x * GameConfiguration.BLOCK_SIZE - 10,
                         y * GameConfiguration.BLOCK_SIZE -15,
                         imageSize,imageSize, null);
        	}
        }else if(previewBuildingType.equals("archery")) {
        	if(validPosition) {
       		 g2.drawImage(GameBuilder.readImage("RTS/src/images/tent.png"),
                        x * GameConfiguration.BLOCK_SIZE - 10,
                        y * GameConfiguration.BLOCK_SIZE -15,
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
	        
	        drawProgressBar(barX, barY, barWidth, barHeight, progress, Color.BLUE, g2);
	        drawTimeRemaining(barX + barWidth/2, barY, building.getRemainingConstructionTime(), g2);
	        
	        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	        Composite originalComposite = g2.getComposite();
	        g2.setComposite(alphaComposite);
	        
	        drawBuildingImage(type, position, g2);
	        
	        g2.setComposite(originalComposite);
	    } else {
	        drawBuildingImage(type, position, g2);
	        
	        if (building.isTargeted()) {
	            g2.setColor(Color.RED);
	            ((Graphics2D) g2).setStroke(new BasicStroke(2));
	            for (Position pos : building.getZone().getPositions()) {
	                g2.drawRect(
	                    pos.getColumn() * GameConfiguration.BLOCK_SIZE,
	                    pos.getLine() * GameConfiguration.BLOCK_SIZE,
	                    GameConfiguration.BLOCK_SIZE,
	                    GameConfiguration.BLOCK_SIZE
	                );
	            }
	        }
	    }
	}
	
	
	
	public void drawBuildingImage(String type, Position position, Graphics2D g2) {
	    if (!images.containsKey("base") && type.equals("base")) {
	        images.put("base", GameBuilder.readImage("RTS/src/images/castle_hum.png"));
	    }
	    if (!images.containsKey("barracks") && type.equals("barracks")) {
	        images.put("barracks", GameBuilder.readImage("RTS/src/images/barracks_hum.png"));
	    }
	    if (!images.containsKey("runway") && type.equals("runway")) {
	        images.put("runway", GameBuilder.readImage("RTS/src/images/runway_human.png"));
	    }
	    if (!images.containsKey("archery") && type.equals("archery")) {
	        images.put("archery", GameBuilder.readImage("RTS/src/images/tent.png"));
	    }
	    
	    int x = position.getColumn() * GameConfiguration.BLOCK_SIZE;
	    int y = position.getLine() * GameConfiguration.BLOCK_SIZE;
	    
	    if (type.equals("base") && images.containsKey("base")) {
	        g2.drawImage(images.get("base"), x + 5, y - 22, 100, 100, null);
	    } else if (type.equals("barracks") && images.containsKey("barracks")) {
	        g2.drawImage(images.get("barracks"), x - 6, y, 75, 75, null);
	    } else if (type.equals("runway") && images.containsKey("runway")) {
	        g2.drawImage(images.get("runway"), x - 10, y - 15, 75, 75, null);
	    } else if (type.equals("archery") && images.containsKey("archery")) {
	        g2.drawImage(images.get("archery"), x - 10, y - 15, 75, 75, null);
	    }
	}
	
	
	public void paint(Unit unit, String name, Graphics2D g2, Player mainPlayer) {


	    Position position = unit.getZone().getPositions().get(0);

	    if (unit.isUnderConstruction()) {
	        float progress = unit.getConstructionProgress();

	        int barWidth = GameConfiguration.BLOCK_SIZE;
	        int barHeight = 8;
	        int barX = position.getColumn() * GameConfiguration.BLOCK_SIZE;
	        int barY = position.getLine() * GameConfiguration.BLOCK_SIZE - 15;

	        drawProgressBar(barX, barY, barWidth, barHeight, progress, Color.BLUE, g2);
	        drawTimeRemaining(barX + barWidth/2, barY, unit.getRemainingConstructionTime(), g2);

	        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	        Composite originalComposite = g2.getComposite();
	        g2.setComposite(alphaComposite);

	        drawUnitImage(name, position, g2,unit.getRace().getName());

	        g2.setComposite(originalComposite);
	    } else {
	        drawUnitImage(name, position, g2,unit.getRace().getName());

	        if (unit.isSelected()) {
	            g2.setColor(Color.GREEN);
	            g2.setStroke(new BasicStroke(2));
	            g2.drawRect(
	                position.getColumn() * GameConfiguration.BLOCK_SIZE,
	                position.getLine() * GameConfiguration.BLOCK_SIZE,
	                GameConfiguration.BLOCK_SIZE + 10,
	                GameConfiguration.BLOCK_SIZE + 10
	            );
	        }

	        if (unit.isTargeted()) {
	            g2.setColor(Color.RED);
	            g2.setStroke(new BasicStroke(2));
	            g2.drawRect(
	                position.getColumn() * GameConfiguration.BLOCK_SIZE,
	                position.getLine() * GameConfiguration.BLOCK_SIZE,
	                GameConfiguration.BLOCK_SIZE + 10,
	                GameConfiguration.BLOCK_SIZE + 10
	            );
	        }

	        int barWidth = GameConfiguration.BLOCK_SIZE;
	        int barHeight = 5;
	        int barX = position.getColumn() * GameConfiguration.BLOCK_SIZE + 5;
	        int barY = position.getLine() * GameConfiguration.BLOCK_SIZE - 11;

	        g2.setColor(Color.GRAY);
	        g2.fillRect(barX, barY, barWidth, barHeight);

	        float progress = (float) unit.getCurrentHealth() / 200.0f;
	        g2.setColor(mainPlayer != null ? Color.GREEN : Color.RED);
	        g2.fillRect(barX, barY, (int) (barWidth * progress), barHeight);
	    }
	}

	public void drawUnitImage(String name, Position position, Graphics2D g2,String race) {
		
		String imageName=name+"_"+race;
		
	    if (!images.containsKey(imageName)) {
	    	String imagePath="RTS/src/images/"+imageName+".png";
	        images.put(imageName, GameBuilder.readImage(imagePath));
	    }

	    
	    
	    int x = position.getColumn() * GameConfiguration.BLOCK_SIZE;
	    int y = position.getLine() * GameConfiguration.BLOCK_SIZE;
	    
	    if (images.containsKey(imageName)) {
	        if (name.equals("warrior")) {
	            g2.drawImage(images.get(imageName), x, y, 30, 30, null);
	        } else if (name.equals("plane")) {
	            g2.drawImage(images.get(imageName), x, y, 50, 50, null);
	        } else if (name.equals("bowman")) {
	            g2.drawImage(images.get(imageName), x, y, 30, 30, null);
	        } else if (name.equals("knight")) {
	            g2.drawImage(images.get(imageName), x, y, 35, 35, null);
	        } else if (name.equals("mosketeer")) {
	            g2.drawImage(images.get(imageName), x, y, 30, 30, null);
	        } else if (name.equals("airship")) {
	            g2.drawImage(images.get(imageName), x, y, 50, 50, null);
	        } else {
	            g2.drawImage(images.get(imageName), x, y, 30, 30, null);
	        }
	    }
	}
	
	public void paint(Slave unit, Graphics2D g2,Player mainPlayer) {
		
	    Position position = unit.getZone().getPositions().get(0);
	    
	    String imageName = "slave_" + unit.getRace().getName();
	    
	    if (!images.containsKey(imageName)) {
	        String imagePath = "RTS/src/images/" + imageName + ".png";
	        images.put(imageName, GameBuilder.readImage(imagePath));
	    }
	    
	    if (unit.isUnderConstruction()) {
	        float progress = unit.getConstructionProgress();
	        
	        int barWidth = GameConfiguration.BLOCK_SIZE;
	        int barHeight = 8;
	        int barX = position.getColumn() * GameConfiguration.BLOCK_SIZE;
	        int barY = position.getLine() * GameConfiguration.BLOCK_SIZE - 15;
	        
	        drawProgressBar(barX, barY, barWidth, barHeight, progress, Color.BLUE, g2);
	        drawTimeRemaining(barX + barWidth/2, barY, unit.getRemainingConstructionTime(), g2);
	        
	        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	        Composite originalComposite = g2.getComposite();
	        g2.setComposite(alphaComposite);
	        
	        g2.drawImage(images.get(imageName),
	                position.getColumn() * GameConfiguration.BLOCK_SIZE,
	                position.getLine() * GameConfiguration.BLOCK_SIZE,
	                30, 30, null);
	        
	        g2.setComposite(originalComposite);
	    } else {
	    	
	     
	    	g2.drawImage(images.get(imageName),
	                position.getColumn() * GameConfiguration.BLOCK_SIZE,
	                position.getLine() * GameConfiguration.BLOCK_SIZE,
	                30, 30, null);
		    
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
		    if (unit.isTargeted()) {
	            g2.setColor(Color.RED);
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
	        int barX = position.getColumn() * GameConfiguration.BLOCK_SIZE+5;
	        int barY = position.getLine() * GameConfiguration.BLOCK_SIZE -11;
	        
	        g2.setColor(Color.GRAY);
	        g2.fillRect(barX, barY, barWidth, barHeight);
	        
	        float progress = (float) unit.getCurrentHealth() / 100.0f;
	        if(mainPlayer!=null) {
	        	g2.setColor(Color.GREEN);
	        }else {
	        	g2.setColor(Color.RED);
	        }
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
    
    public void drawProgressBar(int x, int y, int width, int height, float progress, Color fillColor, Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.fillRect(x, y, width, height);
        
        g2.setColor(fillColor);
        g2.fillRect(x, y, (int)(width * progress), height);
    }

    public void drawTimeRemaining(int x, int y, int remainingTime, Graphics2D g2) {
        String timeText = remainingTime + "s";
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString(timeText, x - 10, y - 5);
    }
	
	

	
	

}
