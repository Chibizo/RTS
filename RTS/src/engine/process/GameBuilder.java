package engine.process;

import data.model.Player;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import config.GameConfiguration;
import data.map.Map;

public class GameBuilder {

	public static Map buildMap() {
		return new Map(GameConfiguration.LINE_COUNT, GameConfiguration.COLUMN_COUNT);
	}
	
	public static MobileInterface buildInitMobile(Map map,Player mainPlayer) {
		MobileInterface manager=new ElementManager(map,mainPlayer);
		manager.putBuilding(manager.getMainPlayer().getStarterZone(),"base",manager.getMainPlayer());
		return manager;
	}
	
	public static Image readImage(String filePath) {
		try {
			return ImageIO.read(new File(filePath));
		} catch (IOException e) {
			System.err.println("-- Can not read the image file ! --");
			return null;
		}
	}
}
