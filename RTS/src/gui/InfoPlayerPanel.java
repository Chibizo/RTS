package gui;

import data.mobile.Building;
import data.model.Player;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPlayerPanel extends JPanel{
	
	private Player mainPlayer;
	private MainGUI mainGui;
	
	private JPanel grid;
	
	private JLabel raceNameLabel=new JLabel("Race :");
	private JLabel raceNameValue=new JLabel("");

	
	
	private JLabel woodLabel=new JLabel("Wood :");
	private JLabel woodValue=new JLabel("");
	
	private JLabel magicOreLabel=new JLabel("Magic Ore :");
	private JLabel magicOreValue=new JLabel("");
	
	private JLabel infoLabel=new JLabel("");
	private JLabel infoLabel2=new JLabel("");
	
	
	
	public InfoPlayerPanel (Player mainPlayer,MainGUI mainGui) {
		this.mainPlayer=mainPlayer;
		this.mainGui=mainGui;
		
		setLayout(new FlowLayout(FlowLayout.LEFT,100,10));
		grid=new JPanel(new GridLayout(2,4,10,10));
		
		raceNameValue.setText(mainPlayer.getRace().getName());
		woodValue.setText(String.valueOf(mainPlayer.getWood()));
		magicOreValue.setText(String.valueOf(mainPlayer.getMagicOre()));
		infoLabel.setPreferredSize(new Dimension(300,infoLabel.getPreferredSize().height));
		initStyle();
		grid.add(raceNameLabel);
		grid.add(woodLabel);
		grid.add(magicOreLabel);
		grid.add(infoLabel);
		grid.add(raceNameValue);
		grid.add(woodValue);
		grid.add(magicOreValue);
		grid.add(infoLabel2);
		add(grid); 
		
		
	}
	
	public void initStyle() {
		 raceNameLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		 raceNameLabel.setForeground(Color.WHITE);
		 
		 raceNameValue.setFont(new Font("Nimbus Sans", Font.BOLD, 20));
		 raceNameValue.setForeground(new Color(138, 43, 226));
		 
		 woodLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		 woodLabel.setForeground(Color.WHITE);
		 
		 infoLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 20));
		 infoLabel.setForeground(Color.WHITE);
		 
		 infoLabel2.setFont(new Font("Nimbus Sans", Font.BOLD, 20));
		 infoLabel2.setForeground(Color.WHITE);
		 
		 woodValue.setFont(new Font("Nimbus Sans", Font.BOLD, 20));
		 woodValue.setForeground(new Color(139, 69, 19));
		  
		 magicOreLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		 magicOreLabel.setForeground(Color.WHITE);

		 magicOreValue.setFont(new Font("Nimbus Sans", Font.BOLD, 20));
		 magicOreValue.setForeground(new Color(0, 255, 255));
		 
		 grid.setBackground(new Color(40, 40, 40));
		 setBackground(new Color(40, 40, 40));
		 
		 
		
	}
	
	public void update() {
		infoLabel2.setText("");
	    woodValue.setText(String.valueOf(mainPlayer.getWood()));
	    magicOreValue.setText(String.valueOf(mainPlayer.getMagicOre()));
	    if (mainGui.getValueInfo().equals("base")|| mainGui.getValueInfo().equals("barracks") || mainGui.getValueInfo().equals("runway")) {
	    	for(Building building : mainPlayer.getBuildings()) {
	    		if(building.getName().equals("base")&& mainGui.getValueInfo().equals("base")) {
	    			infoLabel.setText("PV : "+String.valueOf(building.getCurrentHealth()));
	    			infoLabel.setForeground(Color.GREEN);
	    			infoLabel2.setText("Slaves : "+String.valueOf(mainPlayer.getSlave())+"/"+String.valueOf(mainPlayer.getMaxSlaves()));
	    		}
	    		else if(building.getName().equals("barracks")&& mainGui.getValueInfo().equals("barracks")) {
	    			infoLabel.setText("PV : "+String.valueOf(building.getCurrentHealth()));
	    			infoLabel.setForeground(Color.GREEN);

	    		}else if(building.getName().equals("runway")&& mainGui.getValueInfo().equals("runway")) {
	    			infoLabel.setText("PV : "+String.valueOf(building.getCurrentHealth()));
	    			infoLabel.setForeground(Color.GREEN);
	    		}
	    	}
	    }else {
	    	infoLabel.setForeground(Color.WHITE);
	    }
	}


	public void setinfoLabel(String warning) {
		this.infoLabel.setText(warning);
	}
	
	
	
	

}
