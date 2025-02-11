package gui;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import engine.process.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

import config.GameConfiguration;
import engine.map.*;



public class MainGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private Map map;

	private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);
	
	private GameDisplay dashboard;
	
	private MobileInterface manager;
	
	private JPanel panelBuilding=new JPanel() ;
	private JButton baseBuilding=new JButton("Base");


	public MainGUI(String title) {
		super(title);
		init();
		initStyle();
		
	}

	private void init() {

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		KeyControls keyControls = new KeyControls();
		JTextField textField = new JTextField();
		this.addKeyListener(keyControls);
		
		
		map=GameBuilder.buildMap();
		
		manager= GameBuilder.buildInitMobile(map);
		
		dashboard = new GameDisplay(map,manager);
		
		MouseControls mouseControls = new MouseControls();
		dashboard.addMouseListener(mouseControls);
		
		dashboard.setPreferredSize(preferredSize);
		
		contentPane.add(dashboard,BorderLayout.CENTER);
		
		panelBuilding.setLayout(new GridLayout());
		panelBuilding.add(baseBuilding);
		contentPane.add(panelBuilding, BorderLayout.EAST);


		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		/*setExtendedState(JFrame.MAXIMIZED_BOTH);*/
		this.setLocationRelativeTo(null);
		setResizable(false);
	}
	
	private void initStyle() {
		  baseBuilding.setFont(new Font("Nimbus Sans", Font.BOLD, 15));
		  baseBuilding.setBackground(new java.awt.Color(45, 45, 45));  
		  baseBuilding.setForeground(new java.awt.Color(255, 255, 255));
		  baseBuilding.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		  baseBuilding.setPreferredSize(new Dimension(315, 50));
		  
		  panelBuilding.setBackground(new java.awt.Color(33, 33, 33));
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

			
			ArrayList<Position> listPosition= new ArrayList<Position>();
			listPosition.add(map.getBlock(x, y));
			listPosition.add(map.getBlock(x-1, y));
			listPosition.add(map.getBlock(x-1, y-1));
			listPosition.add(map.getBlock(x, y-1));

			Zone zone=new Zone(listPosition);		
			
			manager.putBuilding(zone);
			
			for(Position position : listPosition){
				System.out.println(position.getLine()+" "+position.getColumn());
			}
				System.out.println(x + " " + y);
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
}
