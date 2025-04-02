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

public class RunwayBuildingMenuPanel extends JPanel{

	private MobileInterface manager;

	private JLabel runwayLabel= new JLabel("<html>Archery <br>tier : 1 </html>", JLabel.CENTER);;
	private JButton unitsButton=new JButton("Wizard"); ;
	private JButton  upgradeButton= new JButton("Upgrade Base");;
	private JButton backButton= new JButton("Back"); ;
	
	public RunwayBuildingMenuPanel(Player mainPlayer,MobileInterface manager) {
		this.manager=manager;
		setLayout(new GridLayout(4,1,10,10));
		initStyle();
		add(runwayLabel);
        add(unitsButton);
        add(upgradeButton);
        add(backButton);
		
	}
	
	public void updateRunwayTier() {
		runwayLabel.setText(getRunwayLabelText());
		upgradeButton.setText("airship");
	}
	
	private String getRunwayLabelText() {
		int tier = manager.getBuildingsMainPlayer().get("runway").getTier();
		return "<html>Runway <br>tier : "+tier+"</html>";
	}
	
	public void initStyle() {
		
		runwayLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
        runwayLabel.setForeground(Color.WHITE);
        runwayLabel.setPreferredSize(new Dimension(100,100));
        
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
