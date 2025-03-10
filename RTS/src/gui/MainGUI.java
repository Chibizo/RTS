package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import data.model.Player;
import data.model.Race;
import config.GameConfiguration;
import data.map.Map;
import data.map.Position;
import data.map.Zone;
import data.mobile.Slave;
import data.mobile.Unit;
import engine.process.GameBuilder;
import engine.process.MobileInterface;



public class MainGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private Map map;

	private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);
	
	private GameDisplay dashboard;
	
	private MobileInterface manager;
	
	private String raceMainPlayer;
	
	private JPanel panelInteraction=new JPanel(new GridLayout(5,1)) ;
		
	private BuildingPanel buildingPanel=new BuildingPanel();
	
	private BaseBuildingMenuPanel baseBuildingMenuPanel;
	
	private InfoPlayerPanel infoPlayerPanel;
	
	private Player mainPlayer;
	
	private JButton buildingButton=new JButton("Building");
	
	private String placingBuilding="";
	
	private boolean placingUnit=false;
	
	private Container contentPane;
	
	private Point selectionStart = null;
	private Point selectionEnd = null;
	private boolean isDragging = false;

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
		
		mainPlayer=initMainPlayer(raceMainPlayer);
		
		manager= GameBuilder.buildInitMobile(map,mainPlayer);
		
		dashboard = new GameDisplay(map,manager);
		MouseControls mouseControls = new MouseControls();
		dashboard.addMouseListener(mouseControls);
		
		DragControls dragControls = new DragControls();
		dashboard.addMouseMotionListener(dragControls);
		
		dashboard.setPreferredSize(preferredSize);
		
		
		
		contentPane.add(dashboard,BorderLayout.CENTER);
		
		panelInteraction.add(buildingButton);	
		contentPane.add(panelInteraction,BorderLayout.EAST);
		
		baseBuildingMenuPanel=new BaseBuildingMenuPanel(mainPlayer);
		
		infoPlayerPanel=new InfoPlayerPanel(mainPlayer);
		contentPane.add(infoPlayerPanel,BorderLayout.SOUTH);


		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		
		this.setLocationRelativeTo(null);
		setResizable(false);
	}
	
	public Player initMainPlayer(String raceMainPlayer) {
		ArrayList<Position> starterPositionBase=new ArrayList<Position>();
			
		for (int lineIndex = 50; lineIndex <= 51; lineIndex++) {
	        for (int columnIndex = 11; columnIndex <= 14; columnIndex++) {
	            Position position = new Position(lineIndex,columnIndex);
	            starterPositionBase.add(position);
	        }
		}
		Player mainPlayer=new Player(550,500,new Race(raceMainPlayer),new Zone(starterPositionBase));
		return mainPlayer;
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
		buildingPanel.getRunWayBuilding().addActionListener(new PutBase());
		buildingPanel.getBarracksBuilding().addActionListener(new PutBarracks());
		baseBuildingMenuPanel.getBackButton().addActionListener(new BackAction());
		baseBuildingMenuPanel.getUnitsButton().addActionListener(new SlaveButton());
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
			infoPlayerPanel.update();
			
			// Pass selection rectangle to the dashboard for rendering
			if (isDragging && selectionStart != null && selectionEnd != null) {
				dashboard.setSelectionRectangle(selectionStart, selectionEnd);
			} else {
				dashboard.setSelectionRectangle(null, null);
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
	
	private class DragControls implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			if (selectionStart != null) {
				isDragging = true;
				selectionEnd = e.getPoint();
				dashboard.repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}
	
	private class MouseControls implements MouseListener {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX()/GameConfiguration.BLOCK_SIZE;
			int y = e.getY()/GameConfiguration.BLOCK_SIZE;
			
			if(y >= 2 && y < map.getLineCount()-2 && 
					x >= 0 && x < map.getColumnCount()-2) {
				
				Position clickedPosition = map.getBlock(y, x);
				
				Unit clickedUnit = findUnitAtPosition(x, y);
				
				
				if (clickedUnit != null) {
				      for (Unit unit : manager.getAllUnits()) {
				          unit.setSelected(false);
				      }
				      clickedUnit.setSelected(true);
				      placingUnit = true;
				 }
				
				else if (placingUnit && !manager.getSelectedUnits().isEmpty()) {
				    List<Unit> selectedUnits = manager.getSelectedUnits();
				    
				    String resourceType = manager.getResourceTypeAt(clickedPosition);
				    
				    if (resourceType != null) {
				        for (Unit unit : selectedUnits) {
				            if (unit instanceof Slave) {
				                manager.startHarvesting((Slave) unit, clickedPosition);
				            }
				        }
				    } else if (!map.isfull(clickedPosition)) {
				        for (Unit unit : selectedUnits) {
				            unit.setTargetPosition(clickedPosition);
				        }
				    }

				    for (Unit unit : manager.getAllUnits()) {
				        unit.setSelected(false);
				    }
				}
				
				if (isInBaseZone(x, y)) {
		            System.out.println("Clic sur la base du joueur principal !");
		            BorderLayout layout = (BorderLayout) contentPane.getLayout();
					Component eastComponent = layout.getLayoutComponent(BorderLayout.EAST);
					contentPane.remove(eastComponent);
					contentPane.add(baseBuildingMenuPanel,BorderLayout.EAST);
				    baseBuildingMenuPanel.revalidate();
				    baseBuildingMenuPanel.repaint();
				   
		        }		
				
				ArrayList<Position> listPosition= new ArrayList<Position>();
				listPosition.add(map.getBlock(y, x));
				listPosition.add(map.getBlock(y+1, x));
				listPosition.add(map.getBlock(y, x+1));
				listPosition.add(map.getBlock(y+1, x+1));

				Zone zone=new Zone(listPosition);		
			
				if(placingBuilding=="barracks" && !map.isfull(listPosition.get(0))) {
					
						manager.putBuilding(zone,"barracks");
						infoPlayerPanel.update();
						
						for(Position position : listPosition){
							System.out.println(position.getLine()+" "+position.getColumn());
						}
							System.out.println(x + " " + y);
						placingBuilding="";
					
				}
			}
		}
		
		private boolean isInBaseZone(int x, int y) {
		    Position clickedPosition = map.getBlock(y, x);
		    Zone baseZone = mainPlayer.getStarterZone();
		    
		    for (Position position : baseZone.getPositions()) {
		        if (position.equals(clickedPosition)) {
		            return true;
		        }
		    }
		    return false;
		}
		
		public Unit findUnitAtPosition(int x, int y) {
		    for (Unit unit : manager.getAllUnits()) {
		        Position unitPos = unit.getZone().getPositions().get(0);
		        if (unitPos.getColumn() == x && unitPos.getLine() == y) {
		            return unit;
		        }
		    }
		    return null;
		}


		@Override
		public void mousePressed(MouseEvent e) {
			selectionStart = e.getPoint();
			isDragging = false;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (isDragging && selectionStart != null) {
				selectionEnd = e.getPoint();
				
				Rectangle selectionRect = createSelectionRectangle(selectionStart, selectionEnd);
				
				selectUnitsInRectangle(selectionRect);
				
				selectionStart = null;
				selectionEnd = null;
				isDragging = false;
				dashboard.setSelectionRectangle(null, null);
				dashboard.repaint();
			}
		}
		
		public Rectangle createSelectionRectangle(Point start, Point end) {
			int x = Math.min(start.x, end.x);
			int y = Math.min(start.y, end.y);
			int width = Math.abs(end.x - start.x);
			int height = Math.abs(end.y - start.y);
			return new Rectangle(x, y, width, height);
		}
		
		private void selectUnitsInRectangle(Rectangle selectionRect) {
			for (Unit unit : manager.getAllUnits()) {
				unit.setSelected(false);
			}
			boolean unitsSelected = false;
			for (Unit unit : manager.getAllUnits()) {
				Position unitPos = unit.getZone().getPositions().get(0);
				int unitX = unitPos.getColumn() * GameConfiguration.BLOCK_SIZE;
				int unitY = unitPos.getLine() * GameConfiguration.BLOCK_SIZE;
				
				if (selectionRect.contains(unitX, unitY)) {
					unit.setSelected(true);
					unitsSelected = true;
				}
			}
			
			if (unitsSelected) {
				placingUnit = true;
			}
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
				/**placingBuilding=true;**/
		}
		
	}
	
	private class PutBarracks implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
				placingBuilding="barracks";
		}
		
	}
	
	private class SwapBuilding implements ActionListener {
		public void actionPerformed(ActionEvent e){
			contentPane.remove(panelInteraction);;
			contentPane.add(buildingPanel,BorderLayout.EAST);
		    buildingPanel.revalidate();
		    buildingPanel.repaint();
		}
	}
	
	private class BackAction implements ActionListener {
		public void actionPerformed(ActionEvent e){
			BorderLayout layout = (BorderLayout) contentPane.getLayout();
			Component eastComponent = layout.getLayoutComponent(BorderLayout.EAST);
			contentPane.remove(eastComponent);
			contentPane.add(panelInteraction,BorderLayout.EAST);
		    panelInteraction.revalidate();
		    panelInteraction.repaint();
		}
	}
	
	private class SlaveButton implements ActionListener {
		public void actionPerformed(ActionEvent e){
			Position unitPosition = new Position(
		            mainPlayer.getStarterZone().getPositions().get(0).getLine() + 3,
		            mainPlayer.getStarterZone().getPositions().get(0).getColumn() + manager.getAllUnits().size() %15 -5 
		        );
			System.out.println(unitPosition);
			manager.putSlave(unitPosition);
			manager.selectMostRecentUnit();
			placingUnit=true;
		}
	}
}