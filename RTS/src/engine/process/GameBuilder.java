package engine.process;

import config.GameConfiguration;
import engine.map.Map;

public class GameBuilder {

	public static Map buildMap() {
		return new Map(GameConfiguration.LINE_COUNT, GameConfiguration.COLUMN_COUNT);
	}
	
	public static MobileInterface buildInitMobile(Map map) {
		MobileInterface manager=new ElementManager(map);
		
		return manager;
	}
	
}
