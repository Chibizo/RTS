package engine.process;

import data.model.Player;
import log.LoggerUtility;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import config.GameConfiguration;
import data.map.Map;

public class GameBuilder {
	
	private static Logger logger = LoggerUtility.getLogger(GameBuilder.class, "html");


	public static Map buildMap() {
		logger.info("Construction d'une nouvelle carte de jeu avec dimensions: " + 
		        GameConfiguration.LINE_COUNT + "x" + GameConfiguration.COLUMN_COUNT);
		return new Map(GameConfiguration.LINE_COUNT, GameConfiguration.COLUMN_COUNT);
	}
	
	public static MobileInterface buildInitMobile(Map map,Player mainPlayer,Player enemyPlayer) {
	    logger.info("Initialisation des éléments mobiles pour " + mainPlayer.getRace().getName());
		MobileInterface manager=new ElementManager(map,mainPlayer,enemyPlayer);
		manager.putBuilding(manager.getMainPlayer().getStarterZone(),"base",manager.getMainPlayer());
		return manager;
	}
	
	public static Image readImage(String filePath) {
		try {
	        /**logger.debug("Chargement de l'image: " + filePath);**/
			return ImageIO.read(new File(filePath));
		} catch (IOException e) {
	        logger.error("Impossible de lire le fichier image: " + filePath, e);
			System.err.println("-- Can not read the image file ! --");
			return null;
		}
	}
}
