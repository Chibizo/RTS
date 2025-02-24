package gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPlayerPanel extends JPanel{
	
	private JPanel grid;
	
	private JLabel raceNameLabel=new JLabel("Race :");
	private JLabel raceNameValue=new JLabel("");

	
	
	private JLabel woodLabel=new JLabel("Wood :");
	private JLabel woodValue=new JLabel("");
	
	private JLabel magicOreLabel=new JLabel("Magic Ore :");
	private JLabel magicOreValue=new JLabel("");
	
	
	
	public InfoPlayerPanel (String raceName) {
		setLayout(new FlowLayout(FlowLayout.LEFT,100,10));
		grid=new JPanel(new GridLayout(3,2,10,10));
		
		raceNameValue.setText(raceName);
		woodValue.setText("500");
		magicOreValue.setText("500");
		initStyle();
		grid.add(raceNameLabel);
		grid.add(raceNameValue);
		grid.add(woodLabel);
		grid.add(woodValue);
		grid.add(magicOreLabel);
		grid.add(magicOreValue);
		add(grid);
		
	}
	
	public void initStyle() {
		 raceNameLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		 raceNameLabel.setForeground(Color.WHITE);
		 
		 raceNameValue.setFont(new Font("Nimbus Sans", Font.BOLD, 20));
		 raceNameValue.setForeground(new Color(138, 43, 226));
		 
		 woodLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		 woodLabel.setForeground(Color.WHITE);
		 
		 woodValue.setFont(new Font("Nimbus Sans", Font.BOLD, 20));
		 woodValue.setForeground(new Color(139, 69, 19));
		 
		 magicOreLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		 magicOreLabel.setForeground(Color.WHITE);

		 magicOreValue.setFont(new Font("Nimbus Sans", Font.BOLD, 20));
		 magicOreValue.setForeground(new Color(0, 255, 255));
		 
		 grid.setBackground(new Color(40, 40, 40));
		 setBackground(new Color(40, 40, 40));
		 
		 
		
	}
	

}
