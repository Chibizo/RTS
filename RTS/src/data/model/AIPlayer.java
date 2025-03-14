package data.model;

import data.map.Zone;
import data.mobile.Building;

public class AIPlayer extends Player {
    

    private long lastDecisionTime; // pour contrôler la fréquence des décisions
    private int decisionInterval = 2000; // millisecondes entre les décisions
    
    public AIPlayer(int wood, int magicOre, Race race, Zone zone) {
        super(wood, magicOre, race, zone);
        this.lastDecisionTime = System.currentTimeMillis();
    }
    

    
    public boolean canMakeDecision() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDecisionTime >= decisionInterval) {
            lastDecisionTime = currentTime;
            return true;
        }
        return false;
    }
  
}