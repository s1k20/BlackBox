package view;

import controller.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIMenu {


    public static void showMenu() {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
    public static void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Black Box Plus - Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);


        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("/Users/shlokpatel/Desktop/College/Java Projects/Software-Engineering-Project-2/view/coolbackground.jpg"); // Load the image
                // Draw the image as the background
                g.drawImage(icon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        panel.setLayout(null);
        frame.add(panel);
        placeComponents(panel, frame);

        frame.setVisible(true);
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
        JButton quitButton = new JButton("Quit");
        quitButton.setBounds(100, 450, 400, 100);
        quitButton.setBackground(Gold);
        quitButton.setOpaque(true);
        quitButton.setBorderPainted(false);
        quitButton.setFont(buttonFont);
        quitButton.setForeground(Color.WHITE);
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Code to quit the game
                System.exit(0);
            }
        });
        panel.add(quitButton);
    }


}



