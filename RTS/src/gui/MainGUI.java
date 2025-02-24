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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


import data.model.Player;
import config.GameConfiguration;
import data.map.Map;
import data.map.Position;
import data.map.Zone;
import engine.process.GameBuilder;
import engine.process.MobileInterface;



public class MainGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private Map map;

	private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);
	
	private GameDisplay dashboard;
	
	private MobileInterface manager;
	
	private String  raceMainPlayer;
	
	private JPanel panelInteraction=new JPanel(new GridLayout(5,1)) ;
		
	private BuildingPanel buildingPanel=new BuildingPanel();
	
	private InfoPlayerPanel infoPlayerPanel;
	
	private JButton buildingButton=new JButton("Building");
	
	private boolean placingBuilding= false;
	
	private boolean placingUnit=false;
	
	private Container contentPane;
	


	public MainGUI(String title,String race) {
		super(title);
		raceMainPlayer=race;
		init();
		initStyle();
		initAction();
	}

	private void init() {
		
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		KeyControls keyControls = new KeyControls();
		JTextField textField = new JTextField();
		this.addKeyListener(keyControls);
		
		
		map=GameBuilder.buildMap();
		
		manager= GameBuilder.buildInitMobile(map,raceMainPlayer);
		
		dashboard = new GameDisplay(map,manager);
		
		MouseControls mouseControls = new MouseControls();
		dashboard.addMouseListener(mouseControls);
		
		dashboard.setPreferredSize(preferredSize);
		
		contentPane.add(dashboard,BorderLayout.CENTER);
		
		panelInteraction.add(buildingButton);	
		contentPane.add(panelInteraction,BorderLayout.EAST);
		infoPlayerPanel=new InfoPlayerPanel(raceMainPlayer);
		contentPane.add(infoPlayerPanel,BorderLayout.SOUTH);


		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		/*setExtendedState(JFrame.MAXIMIZED_BOTH);*/
		this.setLocationRelativeTo(null);
		setResizable(false);
	}
	
	private void initStyle() {
		  buildingButton.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		  buildingButton.setBackground(new Color(50, 100, 150));
		  buildingButton.setForeground(Color.WHITE);
		  buildingButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		  buildingButton.setPreferredSize(new Dimension(100,100));
		  
		  panelInteraction.setBackground(new Color(40, 40, 40));
		  
	}
	
	private void initAction() {
		buildingButton.addActionListener(new SwapBuilding());
		buildingPanel.getBackButton().addActionListener(new BackAction());
		buildingPanel.getBaseBuilding().addActionListener(new PutBase());
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(GameConfiguration.GAME_SPEED);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			dashboard.repaint();
			if(placingUnit) {
				manager.moveUnit();
			}
		}
	}

	private class KeyControls implements KeyListener {

		@Override
		public void keyPressed(KeyEvent event) {
			char keyChar = event.getKeyChar();
			switch (keyChar) {

			case 'q':
				break;
			case 'd':
				break;
			case 'm':
				break;
			default:
				break;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}
	private class MouseControls implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX()/GameConfiguration.BLOCK_SIZE;
			int y = e.getY()/GameConfiguration.BLOCK_SIZE;
			
			if(y >= 0 && y < map.getLineCount()-1 && 
					x >= 0 && x < map.getColumnCount()-1) {
				ArrayList<Position> listPosition= new ArrayList<Position>();
				listPosition.add(map.getBlock(y, x));
				listPosition.add(map.getBlock(y, x+1));
				listPosition.add(map.getBlock(y+1, x));
				listPosition.add(map.getBlock(y+1, x+1));
				
				Zone zone=new Zone(listPosition);		
			
				if(placingBuilding) {
					
						manager.putBuilding(zone);
						
						for(Position position : listPosition){
							System.out.println(position.getLine()+" "+position.getColumn());
						}
							System.out.println(x + " " + y);
						placingBuilding=false;
					
				}
				else {
					manager.putUnit(zone);	
					placingUnit=true;

				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
	}
	private class PutBase implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
				placingBuilding=true;
		}
		
	}
	
	private class SwapBuilding implements ActionListener {
		public void actionPerformed(ActionEvent e){
			contentPane.remove(panelInteraction);;
			contentPane.add(buildingPanel,BorderLayout.EAST);
		    buildingPanel.revalidate();
		}
	}
	
	private class BackAction implements ActionListener {
		public void actionPerformed(ActionEvent e){
			contentPane.remove(buildingPanel);
			contentPane.add(panelInteraction,BorderLayout.EAST);
		    buildingPanel.revalidate();
		}
	}
	
	
}
