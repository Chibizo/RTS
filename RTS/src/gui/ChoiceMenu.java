package gui;

import javax.swing.border.EmptyBorder;


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
	
	
	private String race;
	
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
		init();
		initStyle();
		initAction();
		this.setSize(800,400);
	    this.setLocationRelativeTo(null);


		
	}
	
	public void init() {
		
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		racePanel=new JPanel(new FlowLayout(FlowLayout.CENTER,50,10));
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
		
		
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    pack();
	    setVisible(true);

	}
	
	
	public void initStyle() {
	    
	    raceLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    raceLabel.setForeground(new java.awt.Color(255, 255, 255));
	    raceLabel.setHorizontalAlignment(JLabel.CENTER);
	    raceLabel.setBorder(new EmptyBorder(100, 0, 40, 0));

	    
	    humanButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    humanButton.setBackground(new java.awt.Color(45, 45, 45));  
	    humanButton.setForeground(new java.awt.Color(255, 255, 255));
	    humanButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	    
	    elfButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    elfButton.setBackground(new java.awt.Color(45, 45, 45));  
	    elfButton.setForeground(new java.awt.Color(255, 255, 255));
	    elfButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	    
	    dwarfButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    dwarfButton.setBackground(new java.awt.Color(45, 45, 45));  
	    dwarfButton.setForeground(new java.awt.Color(255, 255, 255));
	    dwarfButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	    
	    
	    
	    getContentPane().setBackground(new Color(33, 33, 33));
	    racePanel.setBackground(new Color(33, 33, 33));
	    numberPlayerPanel.setBackground(new Color(33, 33, 33));




	}
	
	public void initAction() {
		humanButton.addActionListener(new HumanAction(this));
		elfButton.addActionListener(new ElfAction(this));
		dwarfButton.addActionListener(new DwarfAction(this));

	}
	
	
	public class HumanAction implements ActionListener {
			
			private JFrame fenetre;
			public HumanAction(JFrame fenetre) {
				this.fenetre=fenetre;
			}
			
			public void actionPerformed(ActionEvent e) {
				race="human";
				fenetre.dispose();	
				
			}
		}
	
	
	public class ElfAction implements ActionListener {
		
		private JFrame fenetre;
		public ElfAction(JFrame fenetre) {
			this.fenetre=fenetre;
		}
		
		public void actionPerformed(ActionEvent e) {
			race="human";
			fenetre.dispose();	
			
		}
	}
	
	public class DwarfAction implements ActionListener {
		
		private JFrame fenetre;
		public DwarfAction(JFrame fenetre) {
			this.fenetre=fenetre;
		}
		
		public void actionPerformed(ActionEvent e) {
			race="human";
			fenetre.dispose();	
			
		}
	}
	
	
	
}



