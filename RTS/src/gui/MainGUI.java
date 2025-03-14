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

import data.model.AIPlayer;
import data.model.Player;
import data.model.Race;
import config.GameConfiguration;
import data.map.Map;
import data.map.Position;
import data.map.Zone;
import data.mobile.Building;
import data.mobile.Slave;
import data.mobile.Unit;
import engine.process.AIManager;
import engine.process.GameBuilder;
import engine.process.MobileInterface;
import engine.process.MouseUtility;



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
	
	private BarracksBuildingMenuPanel barracksBuildingMenuPanel;
	
	private InfoPlayerPanel infoPlayerPanel;
	
	private Player mainPlayer;
	
	private AIPlayer enemyPlayer;
	private AIManager aiManager;
	
	private JButton buildingButton=new JButton("Building");
	
	private String placingBuilding="";
	
	private boolean placingUnit=false;
	
	private Container contentPane;
	
	private long warningTime = -1;
	
	private boolean showBuildingPreview = false;
	private String previewBuildingType = "";
	private Point mousePosition = new Point(0, 0);
	
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
		barracksBuildingMenuPanel=new BarracksBuildingMenuPanel(mainPlayer);
		
		infoPlayerPanel=new InfoPlayerPanel(mainPlayer);
		contentPane.add(infoPlayerPanel,BorderLayout.SOUTH);

		initAIPlayer();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		
		this.setLocationRelativeTo(null);
		setResizable(false);
	}
	
	public void initAIPlayer() {
		ArrayList<Position> enemyStarterPosition = new ArrayList<Position>();
	    for (int lineIndex = 6; lineIndex <= 8; lineIndex++) {
	        for (int columnIndex = 105; columnIndex <= 108; columnIndex++) {
	            Position position = new Position(lineIndex, columnIndex);
	            enemyStarterPosition.add(position);
	        }
	    }
	    
	    
	    String enemyRace ="elf";
	    enemyPlayer = new AIPlayer(550, 500, new Race(enemyRace), new Zone(enemyStarterPosition));
	    
	    manager.putBuilding(enemyPlayer.getStarterZone(), "base", enemyPlayer);
	    
	    aiManager = new AIManager(enemyPlayer, manager, map);
	    
	}
	
	public Player initMainPlayer(String raceMainPlayer) {
		ArrayList<Position> starterPositionBase=new ArrayList<Position>();
			
		for (int lineIndex = 50; lineIndex <= 52; lineIndex++) {
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
		barracksBuildingMenuPanel.getUnitsButton().addActionListener(new WarriorButton());
		barracksBuildingMenuPanel.getBackButton().addActionListener(new BackAction());
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(GameConfiguration.GAME_SPEED);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			aiManager.update();
			manager.updateConstruction();
			manager.checkCombat();
			dashboard.repaint();
			infoPlayerPanel.update();	
	        if (warningTime != -1 && System.currentTimeMillis() - warningTime >= 4000) {
	            infoPlayerPanel.setWarningLabel("");
	            warningTime = -1; 
	        }
			
			if (isDragging && selectionStart != null && selectionEnd != null) {
				dashboard.setSelectionRectangle(selectionStart, selectionEnd);
			} else {
				dashboard.setSelectionRectangle(null, null);
			}
			if(showBuildingPreview) {
				dashboard.setPreviewBuilding(previewBuildingType, mousePosition);
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
			mousePosition = e.getPoint();
		    if (showBuildingPreview) {
		        dashboard.repaint();
		    }
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
				System.out.println(clickedPosition);
				
				Unit clickedUnit = MouseUtility.findUnitAtPosition(manager.getAllUnits(),x, y,mainPlayer);
				
				Unit clickedAttack = MouseUtility.findUnitAtPosition(manager.getAllUnits(),x, y,enemyPlayer);
				
				if (clickedUnit != null && !clickedUnit.isUnderConstruction()) {
				      for (Unit unit : manager.getAllUnits()) {
				          unit.setSelected(false);
				      }
				      clickedUnit.setSelected(true);
				      placingUnit = true;
				 }
				
				else if (placingUnit && !manager.getSelectedUnits().isEmpty()) {
				    List<Unit> selectedUnits = manager.getSelectedUnits();
				    
				    String resourceType = manager.getResourceTypeAt(clickedPosition);
				    String buildingType = MouseUtility.checkBuilding(clickedPosition, mainPlayer);
				    
				    if (resourceType != null) {
				        for (Unit unit : selectedUnits) {
				            if (unit instanceof Slave) {
				                manager.startHarvesting((Slave) unit, clickedPosition,mainPlayer);
				            }
				        }
				    } else if ((!map.isfull(clickedPosition) && buildingType.isEmpty()) || map.isfullUnits(clickedPosition)) {
				        for (Unit unit : selectedUnits) {
				            unit.setTargetPosition(clickedPosition);
				            if(unit instanceof Slave) {
				            	((Slave) unit).setHarvesting(false);
				            	((Slave) unit).setReturning(false);
				            }
				        }
				    }

				    for (Unit unit : manager.getAllUnits()) {
				        unit.setSelected(false);
				    }
				}
				
				if (MouseUtility.checkBuilding(clickedPosition, mainPlayer)=="base") {
		            System.out.println("Clic sur la base du joueur principal !");
		            BorderLayout layout = (BorderLayout) contentPane.getLayout();
					Component eastComponent = layout.getLayoutComponent(BorderLayout.EAST);
					contentPane.remove(eastComponent);
					contentPane.add(baseBuildingMenuPanel,BorderLayout.EAST);
				    baseBuildingMenuPanel.revalidate();
				    baseBuildingMenuPanel.repaint();
				   
		        }		
				
				if(MouseUtility.checkBuilding(clickedPosition, mainPlayer)=="barracks") {
					Building barracks=manager.getBuildingsMainPlayer().get("barracks");
					if(!barracks.isUnderConstruction()) {
						System.out.println("Clic sur la barracks du joueur principal !");
			            BorderLayout layout = (BorderLayout) contentPane.getLayout();
						Component eastComponent = layout.getLayoutComponent(BorderLayout.EAST);
						contentPane.remove(eastComponent);
						contentPane.add(barracksBuildingMenuPanel,BorderLayout.EAST);
					    barracksBuildingMenuPanel.revalidate();
					    barracksBuildingMenuPanel.repaint();
					}
				}
				
				ArrayList<Position> listPosition= new ArrayList<Position>();
				listPosition.add(map.getBlock(y, x));
				listPosition.add(map.getBlock(y+1, x));
				listPosition.add(map.getBlock(y, x+1));
				listPosition.add(map.getBlock(y+1, x+1));

				Zone zone=new Zone(listPosition);		
			
				if(placingBuilding=="barracks" && !map.isfull(listPosition.get(0)) && (clickedPosition.getColumn()<=35 && clickedPosition.getLine()>=35)) {
					
						manager.putBuilding(zone,"barracks",mainPlayer);
						infoPlayerPanel.update();
						
						for(Position position : listPosition){
							System.out.println(position.getLine()+" "+position.getColumn());
						}
						System.out.println(x + " " + y);
						placingBuilding="";
						showBuildingPreview = false;
					    previewBuildingType = "";
					
				}
			}
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
				
				Rectangle selectionRect = MouseUtility.createSelectionRectangle(selectionStart, selectionEnd);
				
				placingUnit=MouseUtility.selectUnitsInRectangle(manager.getAllUnits(),selectionRect,mainPlayer);
				
				selectionStart = null;
				selectionEnd = null;
				isDragging = false;
				dashboard.setSelectionRectangle(null, null);
				dashboard.repaint();
			}
		}
	
		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
	}
	

	public void setPlacingUnit(boolean placingUnit) {
		this.placingUnit = placingUnit;
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
			if(mainPlayer.getWood()>=GameConfiguration.BARRACKS_COST) {
				placingBuilding="barracks";
				showBuildingPreview = true;
		        previewBuildingType = "barracks";
			}
			else {
				infoPlayerPanel.setWarningLabel("you don't have enough wood");
				warningTime = System.currentTimeMillis();
			}
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
			Building base = manager.getBuildingsMainPlayer().get("base");
			if(base.isUnderConstruction() && base!=null) {
				infoPlayerPanel.setWarningLabel("Base is still under construction");
	            warningTime = System.currentTimeMillis();
	            return;
			}
			else if(mainPlayer.getWood()>=GameConfiguration.SLAVE_COST) {
				Position unitPosition = new Position(
			            mainPlayer.getStarterZone().getPositions().get(0).getLine() + 3,
			            mainPlayer.getStarterZone().getPositions().get(0).getColumn() + manager.getAllUnits().size() %15 -5 
			        );
				System.out.println(unitPosition);
				manager.putSlave(unitPosition,mainPlayer);
				manager.selectMostRecentUnit();
				placingUnit=true;
				
			}else {
				infoPlayerPanel.setWarningLabel("you don't have enough wood");
				warningTime = System.currentTimeMillis();
			}
		}
	}
	
	private class WarriorButton implements ActionListener {
		public void actionPerformed(ActionEvent e){
			Building barracks = mainPlayer.getBuildings("barracks");
			if(barracks.isUnderConstruction() && barracks!=null) {
				infoPlayerPanel.setWarningLabel("Base is still under construction");
	            warningTime = System.currentTimeMillis();
	            return;
			}
			else if(mainPlayer.getWood()>=GameConfiguration.WARRIOR_COST) {
				Position unitPosition = new Position(
						barracks.getZone().getPositions().get(0).getLine()+3 ,
						barracks.getZone().getPositions().get(0).getColumn() + manager.getAllUnits().size()%15 -5 
			     
			        );
				System.out.println(unitPosition);
				manager.putWarrior(unitPosition,mainPlayer);
				manager.selectMostRecentUnit();
				placingUnit=true;
				
			}
			else {
				infoPlayerPanel.setWarningLabel("you don't have enough wood");
				warningTime = System.currentTimeMillis();
			}
		}
	}
	
}