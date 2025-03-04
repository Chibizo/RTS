package gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.model.Player;

public class BaseBuildingMenuPanel extends JPanel{
		
	private JLabel baseLabel= new JLabel("Base Management", JLabel.CENTER);;
	private JButton unitsButton=new JButton("Slave"); ;
	private JButton  upgradeButton= new JButton("Upgrade Base");;
	private JButton backButton= new JButton("Back"); ;
	
	public BaseBuildingMenuPanel(Player mainPlayer) {
		setLayout(new GridLayout(4,1,10,10));
		initStyle();
		add(baseLabel);
        add(unitsButton);
        add(upgradeButton);
        add(backButton);
		
	}
	
	public void initStyle() {
		
		baseLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
        baseLabel.setForeground(Color.WHITE);
        baseLabel.setPreferredSize(new Dimension(100,100));
        
        unitsButton.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
        unitsButton.setBackground(new Color(50, 100, 150));
        unitsButton.setForeground(Color.WHITE);
        unitsButton.setPreferredSize(new Dimension(100,100));
        
        upgradeButton.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
        upgradeButton.setBackground(new Color(50, 100, 150));
        upgradeButton.setForeground(Color.WHITE);
        upgradeButton.setPreferredSize(new Dimension(100,100));
        
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
	
	
}
