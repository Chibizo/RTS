package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.model.Player;
import engine.process.MobileInterface;

public class BarracksBuildingMenuPanel extends JPanel{
	
	private MobileInterface manager;
	
	private JLabel barracksLabel= new JLabel("<html>Barracks <br>tier : 1 </html>", JLabel.CENTER);;
	private JButton unitsButton=new JButton("Warrior"); ;
	private JButton  upgradeButton= new JButton("Upgrade Base");
	private JButton heavyUnitsButton= new JButton("Knight"); 
	private JButton backButton= new JButton("Back"); 
	
	public BarracksBuildingMenuPanel(Player mainPlayer,MobileInterface manager) {
		this.manager=manager;
		setLayout(new GridLayout(4,1,10,10));
		initStyle();
		add(barracksLabel);
        add(unitsButton);
        add(upgradeButton);
        add(backButton);
		
	}
	public void updateBarracksTier() {
		barracksLabel.setText(getBarracksLabelText());
		remove(upgradeButton);
		remove(backButton);
		add(heavyUnitsButton);
		add(backButton);
		
		
	}
	
	private String getBarracksLabelText() {
		int tier = manager.getBuildingsMainPlayer().get("barracks").getTier();
		return "<html>Barracks <br>tier : "+tier+"</html>";
	}
	
	public void initStyle() {
		
		barracksLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
        barracksLabel.setForeground(Color.WHITE);
        barracksLabel.setPreferredSize(new Dimension(100,100));
        
        unitsButton.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
        unitsButton.setBackground(new Color(50, 100, 150));
        unitsButton.setForeground(Color.WHITE);
        unitsButton.setPreferredSize(new Dimension(100,100));
        
        upgradeButton.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
        upgradeButton.setBackground(new Color(50, 100, 150));
        upgradeButton.setForeground(Color.WHITE);
        upgradeButton.setPreferredSize(new Dimension(100,100));
        
        heavyUnitsButton.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
        heavyUnitsButton.setBackground(new Color(50, 100, 150));
        heavyUnitsButton.setForeground(Color.WHITE);
        heavyUnitsButton.setPreferredSize(new Dimension(100,100));
        
        backButton.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
        backButton.setBackground(new Color(205, 92, 92));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(100,100));
        
		setBackground(new Color(40, 40, 40));

	}

	public JButton getUnitsButton() {
		return unitsButton;
	}

	public JButton getUpgradeButton() {
		return upgradeButton;
	}

	public JButton getBackButton() {
		return backButton;
	}
	
	public JButton getHeavyUnitsButton() {
		return heavyUnitsButton;
	}
	
}
