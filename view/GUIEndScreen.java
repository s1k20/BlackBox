package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import controller.Game;  // Assuming Game and Player classes are defined appropriately
import model.Player;     // Make sure the Player class is in the right package

public class GUIEndScreen extends JFrame {
    private final Game game;

    public GUIEndScreen(Game game) {
        this.game = game;
        initializeUI();
    }


    private void initializeUI() {
        setTitle("Game Statistics");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false); // Make the panel transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;


        // Panel for the button, also needs to be non-opaque
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false); // Ensure the background image is visible

        // Create the content pane that will hold the background image
        setContentPane(new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage background;
                try {
                    background = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/background.jpg")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        });

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);


        // Button to go back to the main menu
        JButton backButton = new JButton("Menu");
        backButton.setBackground(Color.GRAY);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setFont(new Font("Monospaced", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            this.dispose(); // Close the current window
            game.onMainMenuToggle();
        });

        buttonPanel.add(backButton);

        displayMultiplayerStats(panel, gbc);

        setVisible(true);
    }
    private void displaySinglePlayerStats(JPanel panel, GridBagConstraints gbc) {
        Player player = game.getPlayer1(); // Ensure getPlayer1() method exists and returns a valid player

        JLabel titleLabel = new JLabel("Final Score and Statistics for " + player.getPlayerName(), SwingConstants.CENTER);
        styleStatsLabel(titleLabel);

        JLabel scoreLabel = new JLabel("Final Score: " + player.getScore(), SwingConstants.CENTER);
        styleStatsLabel(scoreLabel);

        JLabel statsLabel = new JLabel("Rays Sent: " + player.getNumSentRays() + ", Correct Atoms: " + player.getNumCorrectAtoms(), SwingConstants.CENTER);
        styleStatsLabel(statsLabel);

        panel.add(titleLabel, gbc);
        panel.add(scoreLabel, gbc);
        panel.add(statsLabel, gbc);
    }

    private void displayMultiplayerStats(JPanel panel, GridBagConstraints gbc) {
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();
        Player winner = game.getWinner();

        JLabel titleLabel = new JLabel("Game Statistics", SwingConstants.CENTER);
        styleStatsLabel(titleLabel);

        JLabel scoreLabel = new JLabel("Scores - " + player1.getPlayerName() + ": " + player1.getScore() + " | " + player2.getPlayerName() + ": " + player2.getScore(), SwingConstants.CENTER);
        styleStatsLabel(scoreLabel);

        JLabel winnerLabel = new JLabel(winner != null ? winner.getPlayerName() + " won!" : "Draw!", SwingConstants.CENTER);
        styleStatsLabel(winnerLabel);

        JLabel p1Stats = new JLabel(player1.getPlayerName() + " - Rays Sent: " + player1.getNumSentRays() + ", Correct Atoms: " + player1.getNumCorrectAtoms(), SwingConstants.CENTER);
        styleStatsLabel(p1Stats);

        JLabel p2Stats = new JLabel(player2.getPlayerName() + " - Rays Sent: " + player2.getNumSentRays() + ", Correct Atoms: " + player2.getNumCorrectAtoms(), SwingConstants.CENTER);
        styleStatsLabel(p2Stats);

        panel.add(titleLabel, gbc);
        panel.add(scoreLabel, gbc);
        panel.add(winnerLabel, gbc);
        panel.add(p1Stats, gbc);
        panel.add(p2Stats, gbc);
    }

    private void styleStatsLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, 16));
    }

    public void display() {
        setVisible(true);  // Make the GUI visible
    }

}
