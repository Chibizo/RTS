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
	private int numberPlayer;
	
	private Container contentPane;

	private JPanel racePanel ;
	private JPanel flowNumberPlayerPanel;
	private JPanel gridNumberPlayerPanel;
	
	
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
		
		gridNumberPlayerPanel=new JPanel(new GridLayout(2,2,25,25));
		gridNumberPlayerPanel.add(duoButton);
		gridNumberPlayerPanel.add(trioButton);
		gridNumberPlayerPanel.add(quatorButton);
		gridNumberPlayerPanel.add(backButton);
		
		flowNumberPlayerPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,50,10));
		flowNumberPlayerPanel.add(gridNumberPlayerPanel);
		
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
	    
	    numberPlayerLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    numberPlayerLabel.setForeground(new java.awt.Color(255, 255, 255));
	    numberPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
	    numberPlayerLabel.setBorder(new EmptyBorder(100, 0, 40, 0));

	    
	    humanButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    humanButton.setBackground(new Color(50, 100, 150));
	    humanButton.setForeground(new java.awt.Color(255, 255, 255));
	    humanButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	    
	    elfButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    elfButton.setBackground(new Color(50, 100, 150));
	    elfButton.setForeground(new java.awt.Color(255, 255, 255));
	    elfButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	    
	    dwarfButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    dwarfButton.setBackground(new Color(50, 100, 150)); 
	    dwarfButton.setForeground(new java.awt.Color(255, 255, 255));
	    dwarfButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	    
	    
	    
	    getContentPane().setBackground(new Color(33, 33, 33));
	    racePanel.setBackground(new Color(33, 33, 33));
	    flowNumberPlayerPanel.setBackground(new Color(33, 33, 33));
	    gridNumberPlayerPanel.setBackground(new Color(33, 33, 33));

	    
	    
	    duoButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    duoButton.setBackground(new Color(50, 100, 150));
	    duoButton.setForeground(new java.awt.Color(255, 255, 255));
	    duoButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

	    trioButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    trioButton.setBackground(new Color(50, 100, 150));
	    trioButton.setForeground(new java.awt.Color(255, 255, 255));
	    trioButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	    
	    quatorButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    quatorButton.setBackground(new Color(50, 100, 150));
	    quatorButton.setForeground(new java.awt.Color(255, 255, 255));
	    quatorButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	    
	    backButton.setFont(new Font("Nimbus Sans", Font.BOLD, 25));
	    backButton.setBackground(new Color(205, 92, 92)); 
	    backButton.setForeground(new java.awt.Color(255, 255, 255));
	    backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));


	}
	
	public void initAction() {
		humanButton.addActionListener(new HumanAction());
		elfButton.addActionListener(new ElfAction());
		dwarfButton.addActionListener(new DwarfAction());
		
		duoButton.addActionListener(new DuoAction(this));
		trioButton.addActionListener(new TrioAction(this));
		quatorButton.addActionListener(new QuatorAction(this));
		
		backButton.addActionListener(new BackAction());
		

	}
	
	
	public class HumanAction implements ActionListener {
			
			public void actionPerformed(ActionEvent e) {
				race="human";
				contentPane.removeAll();
				contentPane.revalidate();
				contentPane.repaint();
				contentPane.add(numberPlayerLabel,BorderLayout.NORTH);
				contentPane.add(flowNumberPlayerPanel,BorderLayout.CENTER);
				
			}
		}
	
	
	public class ElfAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			race="elf";	
			contentPane.removeAll();
			contentPane.revalidate();
			contentPane.repaint();
			contentPane.add(numberPlayerLabel,BorderLayout.NORTH);
			contentPane.add(flowNumberPlayerPanel,BorderLayout.CENTER);
			
		}
	}
	
	public class DwarfAction implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			race="dwarf";	
			contentPane.removeAll();
			contentPane.revalidate();
			contentPane.repaint();
			contentPane.add(numberPlayerLabel,BorderLayout.NORTH);
			contentPane.add(flowNumberPlayerPanel,BorderLayout.CENTER);
			
		}
	}
	
	public class BackAction implements ActionListener {
			
			public void actionPerformed(ActionEvent e) {
				
				contentPane.removeAll();
				contentPane.revalidate();
				contentPane.repaint();
				contentPane.add(raceLabel,BorderLayout.NORTH);
				contentPane.add(racePanel,BorderLayout.CENTER);
				
			}
		}
	
	
	public class DuoAction implements ActionListener {
			
		private JFrame fenetre;
		public DuoAction(JFrame fenetre) {
			this.fenetre=fenetre;
		}
		
		public void actionPerformed(ActionEvent e) {
			numberPlayer=2;
			fenetre.dispose();	
			MainGUI gameMainGUI = new MainGUI("RTS",race,numberPlayer);

			Thread gameThread = new Thread(gameMainGUI);
			gameThread.start();
		}
	}
	
	public class TrioAction implements ActionListener {
		
		private JFrame fenetre;
		public TrioAction(JFrame fenetre) {
			this.fenetre=fenetre;
		}
		
		public void actionPerformed(ActionEvent e) {
			numberPlayer=3;
			fenetre.dispose();	
			MainGUI gameMainGUI = new MainGUI("RTS",race,numberPlayer);

			Thread gameThread = new Thread(gameMainGUI);
			gameThread.start();
		}
	}
	
	
	public class QuatorAction implements ActionListener {
		
		private JFrame fenetre;
		public QuatorAction(JFrame fenetre) {
			this.fenetre=fenetre;
		}
		
		public void actionPerformed(ActionEvent e) {
			numberPlayer=4;
			fenetre.dispose();	
			MainGUI gameMainGUI = new MainGUI("RTS",race,numberPlayer);

			Thread gameThread = new Thread(gameMainGUI);
			gameThread.start();
		}
	}
	
	
	
}



