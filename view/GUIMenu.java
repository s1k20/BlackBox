package view;

import controller.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIMenu {

    private static JFrame mainFrame;
    private static JFrame initialMenuFrame;

    public static void showMenu() {
        SwingUtilities.invokeLater(() -> createInitialMenu());
    }

    private static void createAndShowGUI() {
        JFrame mainFrame = new JFrame("Black Box Plus - Main Menu");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 600);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("/Users/shlokpatel/Desktop/College/Java Projects/Software-Engineering-Project-2/view/coolbackground.jpg"); // Load the image
                g.drawImage(icon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        panel.setLayout(null);
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

        JButton singlePlayerButton = new JButton("Single Player");
        singlePlayerButton.setBounds(100, 50, 400, 100);
        singlePlayerButton.setBackground(Color.GRAY);
        singlePlayerButton.setOpaque(true);
        singlePlayerButton.setBorderPainted(false);
        singlePlayerButton.setFont(buttonFont);
        singlePlayerButton.setForeground(Color.WHITE);
        singlePlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                jFrame.dispose();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Game singlePlayerGame = new Game();
                        singlePlayerGame.playSinglePlayerGame();
                    }
                }).start();
            }
        });
        panel.add(singlePlayerButton);

        // Create and place the 2 player game button
        JButton twoPlayerButton = new JButton("Multiplayer");
        twoPlayerButton.setBounds(100, 250, 400, 100);
        twoPlayerButton.setBackground(DarkGrey);
        twoPlayerButton.setOpaque(true);
        twoPlayerButton.setBorderPainted(false);
        twoPlayerButton.setFont(buttonFont);
        twoPlayerButton.setForeground(Color.WHITE);
        twoPlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                jFrame.dispose();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Game twoPlayerGame = new Game();
                        twoPlayerGame.play2PlayerGame();
                    }
                }).start();
            }
        });
        panel.add(twoPlayerButton);

        // Create and place the quit button
        JButton quitButton = new JButton("Back");
        quitButton.setBounds(100, 450, 400, 100);
        quitButton.setBackground(Gold);
        quitButton.setOpaque(true);
        quitButton.setBorderPainted(false);
        quitButton.setFont(buttonFont);
        quitButton.setForeground(Color.WHITE);
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Code to quit the game
                jFrame.dispose();
                createInitialMenu();
            }
        });
        panel.add(quitButton);
    }

    private static void createInitialMenu() {
        initialMenuFrame = new JFrame("Welcome to Black Box Plus");
        initialMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialMenuFrame.setSize(800, 550);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("view/coolbackground.jpg");
                g.drawImage(icon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Logo next to the title
        ImageIcon logoIcon = new ImageIcon(new ImageIcon("view/Logo.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)); // Adjust the size as needed
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(50, 25, logoIcon.getIconWidth(), logoIcon.getIconHeight()); // Adjust position as needed
        panel.add(logoLabel);

        // Title Label
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
    private static void styleButton(JButton button) {
        // Common styling for buttons

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Monospaced", Font.BOLD, 40));
        button.setOpaque(true);
        button.setBorderPainted(false);
    }


}



