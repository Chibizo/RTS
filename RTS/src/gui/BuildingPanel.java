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
	private Button baseBuilding=new Button("Base") ;
	private Button barracksBuilding=new Button("Barracks");
	private Button backButton=new Button("Back");

	public  BuildingPanel() {
		setLayout(new GridLayout(4,1,10,10));
		initStyle();
		add(sectionName);
		add(baseBuilding);
		add(barracksBuilding);
		add(backButton);
		
	}
	
	
	private void initStyle() {
		  baseBuilding.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		  baseBuilding.setBackground(new Color(50, 100, 150));
		  baseBuilding.setForeground(Color.WHITE);
		  baseBuilding.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		  baseBuilding.setPreferredSize(new Dimension(100,100));
		  
		  barracksBuilding.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		  barracksBuilding.setBackground(new Color(50, 100, 150));
		  barracksBuilding.setForeground(Color.WHITE);
		  barracksBuilding.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		  barracksBuilding.setPreferredSize(new Dimension(100,100));
		  
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


	public Button getBaseBuilding() {
		return baseBuilding;
	}


	public Button getBarracksBuilding() {
		return barracksBuilding;
	}


	public Button getBackButton() {
		return backButton;
	}
	
	
	
}
