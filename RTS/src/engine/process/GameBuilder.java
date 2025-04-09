package engine.process;

import data.model.Player;
import log.LoggerUtility;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
	
	public static MobileInterface buildInitMobile(Map map,Player mainPlayer,Player enemyPlayer,Player enemyPlayer2) {
	    logger.info("Initialisation des éléments mobiles pour " + mainPlayer.getRace().getName());
		MobileInterface manager=new ElementManager(map,mainPlayer,enemyPlayer,enemyPlayer2);
		manager.putBuilding(manager.getMainPlayer().getStarterZone(),"base",manager.getMainPlayer().getRace().getName());
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
	
	public static String getRandomRace(String excludedRace1, String excludedRace2) {
	    String[] races = {"human", "elf", "dwarf"};
	    ArrayList<String> availableRaces = new ArrayList<>();
	    
	    for (String race : races) {
	        if (!race.equals(excludedRace1) && !race.equals(excludedRace2)) {
	            availableRaces.add(race);
	        }
	    }
	    
	    if (availableRaces.size() == 1) {
	        return availableRaces.get(0);
	    }
	    
	    int randomIndex = (int) (Math.random() * availableRaces.size());
	    return availableRaces.get(randomIndex);
	}
}
