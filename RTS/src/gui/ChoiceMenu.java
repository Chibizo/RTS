package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import config.GameConfiguration;
import data.map.Map;
import data.map.Position;
import data.map.Zone;
import engine.process.GameBuilder;
import engine.process.MobileInterface;



public class ChoiceMenu extends JFrame{
	
	
	private Container contentPane;

	private JPanel racePanel ;
	private JPanel numberPlayerPanel;
	
	
	private JButton humanButton =new JButton("Human");
	private JButton elfButton=new JButton("Elf");
	private JButton dwarfButton=new JButton("Dwarf");
	
	private JButton duoButton=new JButton("2 Players");
	private JButton trioButton=new JButton("3 Players");
	private JButton quatorButton=new JButton("4 Players");
	
	private JButton backButton=new JButton("Back");
	
	private JLabel raceLabel=new JLabel("Select your Race");
	private JLabel numberPlayerLabel=new JLabel("select the number of players");
	
	private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);
	
	
	
	public ChoiceMenu(String title) {
		super(title);
		
	}
	
	public void init() {
		
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		racePanel=new JPanel(new FlowLayout());
		racePanel.add(humanButton);
		racePanel.add(elfButton);
		racePanel.add(dwarfButton);
		
		numberPlayerPanel=new JPanel(new GridLayout(2,2));
		numberPlayerPanel.add(duoButton);
		numberPlayerPanel.add(trioButton);
		numberPlayerPanel.add(quatorButton);
		numberPlayerPanel.add(backButton);
		
		contentPane.add(raceLabel,BorderLayout.NORTH);
		contentPane.add(racePanel,BorderLayout.CENTER);
		
		
		
		
	}
	
	
	public void initStyle() {
		
	}
	
	public void initAction() {
		
	}
}



