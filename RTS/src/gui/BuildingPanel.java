package gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class BuildingPanel extends JPanel{
	
	private JLabel sectionName=new JLabel("Buildings");
	private Button runwayBuilding=new Button("runway") ;
	private Button barracksBuilding=new Button("Barracks");
	private Button archeryBuilding=new Button("Archery");
	private Button backButton=new Button("Back");

	public  BuildingPanel() {
		setLayout(new GridLayout(5,1,10,10));
		initStyle();
		add(sectionName);
		add(barracksBuilding);
		add(archeryBuilding);
		add(runwayBuilding);
		add(backButton);
		
	}
	
	
	private void initStyle() {
			runwayBuilding.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
			runwayBuilding.setBackground(new Color(50, 100, 150));
			runwayBuilding.setForeground(Color.WHITE);
			runwayBuilding.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			runwayBuilding.setPreferredSize(new Dimension(100,100));
		  
		  barracksBuilding.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		  barracksBuilding.setBackground(new Color(50, 100, 150));
		  barracksBuilding.setForeground(Color.WHITE);
		  barracksBuilding.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		  barracksBuilding.setPreferredSize(new Dimension(100,100));
		  
		  archeryBuilding.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		  archeryBuilding.setBackground(new Color(50, 100, 150));
		  archeryBuilding.setForeground(Color.WHITE);
		  archeryBuilding.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		  archeryBuilding.setPreferredSize(new Dimension(100,100));
		  
		  sectionName.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		  sectionName.setForeground(Color.WHITE);

		  
		  backButton.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		  backButton.setBackground(new Color(205, 92, 92));
		  backButton.setForeground(Color.WHITE);
		  backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		  backButton.setPreferredSize(new Dimension(100,100));
		  
		  setBackground(new Color(40, 40, 40));
		  
	}


	public JLabel getSectionName() {
		return sectionName;
	}


	public Button getRunWayBuilding() {
		return runwayBuilding;
	}


	public Button getBarracksBuilding() {
		return barracksBuilding;
	}
	
	public Button getArcheryBuilding() {
		return archeryBuilding;
	}


	public Button getBackButton() {
		return backButton;
	}
	
	
	
}
