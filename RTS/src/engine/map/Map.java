package engine.map;

import config.GameConfiguration;

public class Map {

	int height;
	int width; 
	
	public Map(int height,int width) {
		this.height=height;
		this.width=width;
		
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	
}
