package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import controller.Game;
import model.Player;

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
                    background = ImageIO.read(Objects.requireNonNull(getClass().getResource("GUIBoard/images/background.jpg")));
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

    private void displayMultiplayerStats(JPanel panel, GridBagConstraints gbc) {
        Player player1 = game.getPlayerManager().getPlayer1();
        Player player2 = game.getPlayerManager().getPlayer2();
        Player winner = game.getPlayerManager().getWinner();

        JLabel titleLabel = new JLabel("Game Statistics", SwingConstants.CENTER);
        styleStatsLabel(titleLabel);

        // display each player's score
        String scores = "Scores - " + player1.getPlayerName() + ": " + player1.getScore() + " | " +
                player2.getPlayerName() + ": " + player2.getScore();
        JLabel scoreLabel = new JLabel(scores, SwingConstants.CENTER);
        styleStatsLabel(scoreLabel);

        // display a winner if there wasn't a draw
        String gameWinner = winner != null ? winner.getPlayerName() + " won!" : "Draw!";
        JLabel winnerLabel = new JLabel(gameWinner, SwingConstants.CENTER);
        styleStatsLabel(winnerLabel);

        // display player 1s game stats
        String p1StatString = player1.getPlayerName() + " - Rays Sent: " +
                player1.getNumSentRays() + ", Correct Atoms: " + player1.getNumCorrectAtoms();
        JLabel p1Stats = new JLabel(p1StatString, SwingConstants.CENTER);
        styleStatsLabel(p1Stats);

        // display player 2s game stats
        String p2StatString = player2.getPlayerName() + " - Rays Sent: " +
                player2.getNumSentRays() + ", Correct Atoms: " + player2.getNumCorrectAtoms();
        JLabel p2Stats = new JLabel(p2StatString, SwingConstants.CENTER);
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
