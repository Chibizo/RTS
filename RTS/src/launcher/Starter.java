package launcher;

import gui.ChoiceMenu;
import gui.MainGUI;

public class Starter {

	public static void main(String[] args) {
		/**
		 * 
		 
		MainGUI gameMainGUI = new MainGUI("RTS");

		Thread gameThread = new Thread(gameMainGUI);
		gameThread.start();
		**/
		new ChoiceMenu("Menu");
	}

}
