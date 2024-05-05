package view;

import controller.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GUIMenu {

    private static JFrame initialMenuFrame;

    public static void showMenu() {
        SwingUtilities.invokeLater(GUIMenu::createInitialMenu);
    }

    private static void createAndShowGUI() {
        JFrame mainFrame = new JFrame("Black Box Plus - Main Menu");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 600);
        mainFrame.setResizable(false);

        JPanel panel = getPanel();
        mainFrame.add(panel);

        placeComponents(panel, mainFrame);

        // Centralize the JFrame on the screen
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private static void placeComponents(JPanel panel, JFrame jFrame) {
        panel.setLayout(null);
        Font buttonFont = new Font("Monospaced", Font.BOLD, 40);
        Color Gold = new Color(255, 204, 51);
        Color DarkGrey = new Color( 51,51,51);

        JButton singlePlayerButton = getButton(jFrame, buttonFont);
        panel.add(singlePlayerButton);

        // Create and place the 2 player game button
        JButton twoPlayerButton = getButton(jFrame, DarkGrey, buttonFont);
        panel.add(twoPlayerButton);

        // Create and place the quit button
        JButton quitButton = new JButton("Back");
        quitButton.setBounds(100, 450, 400, 100);
        quitButton.setBackground(Gold);
        quitButton.setOpaque(true);
        quitButton.setBorderPainted(false);
        quitButton.setFont(buttonFont);
        quitButton.setForeground(Color.WHITE);
        quitButton.addActionListener(e -> {
            // Code to quit the game
            jFrame.dispose();
            createInitialMenu();
        });
        panel.add(quitButton);
    }

    private static JButton getButton(JFrame jFrame, Color DarkGrey, Font buttonFont) {
        JButton twoPlayerButton = new JButton("Multiplayer");
        twoPlayerButton.setBounds(100, 250, 400, 100);
        twoPlayerButton.setBackground(DarkGrey);
        twoPlayerButton.setOpaque(true);
        twoPlayerButton.setBorderPainted(false);
        twoPlayerButton.setFont(buttonFont);
        twoPlayerButton.setForeground(Color.WHITE);
        twoPlayerButton.addActionListener(e -> {
            jFrame.dispose();
            new Thread(() -> {
                Game twoPlayerGame = new Game();
                twoPlayerGame.play2PlayerGame();
            }).start();
        });
        return twoPlayerButton;
    }

    private static JButton getButton(JFrame jFrame, Font buttonFont) {
        JButton singlePlayerButton = new JButton("Single Player");
        singlePlayerButton.setBounds(100, 50, 400, 100);
        singlePlayerButton.setBackground(Color.GRAY);
        singlePlayerButton.setOpaque(true);
        singlePlayerButton.setBorderPainted(false);
        singlePlayerButton.setFont(buttonFont);
        singlePlayerButton.setForeground(Color.WHITE);
        singlePlayerButton.addActionListener(e -> {

            jFrame.dispose();
            new Thread(() -> {
                Game singlePlayerGame = new Game();
                singlePlayerGame.playSinglePlayerGame();
            }).start();
        });
        return singlePlayerButton;
    }

    private static void createInitialMenu() {
        initialMenuFrame = new JFrame("Welcome to Black Box Plus");
        initialMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialMenuFrame.setSize(800, 550);
        initialMenuFrame.setResizable(false);

        JPanel panel = getPanel();

        JLabel titleLabel = new JLabel("Black Box Plus");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(160, 25, 600, 100); // Adjust position as needed
        panel.add(titleLabel);

        JButton playButton = new JButton("Play");
        playButton.setBounds(200, 150, 400, 100);
        playButton.setFont(new Font("Monospaced", Font.BOLD, 40));
        playButton.setBackground(new Color(51, 51, 51)); // Dark grey
        styleButton(playButton);
        playButton.addActionListener(e -> {
            initialMenuFrame.dispose();
            createAndShowGUI();
        });
        panel.add(playButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(200, 300, 400, 100);
        exitButton.setFont(new Font("Monospaced", Font.BOLD, 40));
        exitButton.setBackground(new Color(255, 204, 51)); // Gold color
        styleButton(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton);

        initialMenuFrame.add(panel);
        initialMenuFrame.setLocationRelativeTo(null);
        initialMenuFrame.setVisible(true);
    }

    private static JPanel getPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage background;
                try {
                    background = ImageIO.read(Objects.requireNonNull(getClass().getResource("GUIBoard/images/background.jpg")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        panel.setLayout(null);
        return panel;
    }

    private static void styleButton(JButton button) {
        // Common styling for buttons
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Monospaced", Font.BOLD, 40));
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

}



