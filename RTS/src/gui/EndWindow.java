package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import engine.process.MobileInterface;
import data.model.Player;

public class EndWindow extends JFrame {
	
	private MobileInterface manager;
	private Player winner;
	private Player loser;

	private JLabel titleLabel;
	private JTextArea statsArea;
	private JButton quitButton;

	public EndWindow(String title, MobileInterface manager, Player winner, Player loser) {
		super(title);
		this.manager = manager;
		this.winner = winner;
		this.loser = loser;
		
		init();
		initStyle();
		initAction();
	}
	
	public void init() {
		// Configuration du conteneur
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(10, 10));
		
		// Titre
		titleLabel = new JLabel("Game Over!", SwingConstants.CENTER);
		contentPane.add(titleLabel, BorderLayout.NORTH);
		
		// Zone de statistiques
		statsArea = new JTextArea();
		statsArea.setEditable(false);
		statsArea.setLineWrap(true);
		statsArea.setWrapStyleWord(true);
		statsArea.setText(generateGameStats());
		
		JScrollPane scrollPane = new JScrollPane(statsArea);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		// Bouton de sortie
		quitButton = new JButton("Quit Game");
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (manager != null) {
					manager.terminateGame();
				}
				dispose();
				System.exit(0);
			}
		});
		contentPane.add(quitButton, BorderLayout.SOUTH);
		
		// Configuration de la fenêtre
		setSize(500, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); // Assurez-vous que cette ligne est présente
	}
	
	public void initStyle() {
		// Style du titre
		titleLabel.setFont(new Font("Nimbus Sans", Font.BOLD, 30));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
		
		// Style de la zone de statistiques
		statsArea.setBackground(new Color(50, 50, 50));
		statsArea.setForeground(Color.WHITE);
		statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		
		// Style du bouton
		quitButton.setBackground(new Color(70, 70, 70));
		quitButton.setForeground(Color.WHITE);
		quitButton.setFont(new Font("Nimbus Sans", Font.BOLD, 16));
		quitButton.setBorder(new EmptyBorder(10, 20, 10, 20));
		
		// Couleur de fond globale
		getContentPane().setBackground(new Color(33, 33, 33));
	}
	
	public void initAction() {
		// Déjà géré dans la méthode init() avec l'ajout de l'ActionListener
	}
	
	private String generateGameStats() {
		StringBuilder stats = new StringBuilder();
		
		stats.append("GAME RESULT\n");
		stats.append("====================\n\n");
		
		stats.append("Winner: ").append(winner.getRace().getName()).append("\n");
		stats.append("Loser: ").append(loser.getRace().getName()).append("\n\n");
		
		stats.append("Winner's Resources:\n");
		stats.append("Wood: ").append(winner.getWood()).append("\n");
		stats.append("Magic Ore: ").append(winner.getMagicOre()).append("\n\n");
		
		stats.append("Units Remaining:\n");
		stats.append("Slaves: ").append(winner.getSlave()).append("\n");
		
		return stats.toString();
	}
}