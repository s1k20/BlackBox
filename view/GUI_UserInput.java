package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI_UserInput {

    public static String askForPlayerName(String title) {
        final String[] playerName = {null}; // To hold the player's name

        // Load the background image
        ImageIcon background = new ImageIcon("view/coolbackground.jpg");
        Image img = background.getImage();

        // Create the dialog
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(null); // Center on screen
        dialog.setResizable(false);

        // Create a panel with a background image
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        // Text field for name input
        JTextField textField = new JTextField(10);
        textField.setPreferredSize(new Dimension(250, 50)); // Make the input box bigger
        textField.setFont(new Font("Monospaced", Font.BOLD, 25)); // Increase font size
        textField.setBackground(Color.LIGHT_GRAY); // Set background of input box to grey

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Monospaced", Font.BOLD, 25)); // Match font size with input
        submitButton.setBackground(new Color(255, 215, 0)); // Gold color background
        submitButton.setForeground(Color.BLACK); // Text color
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName[0] = textField.getText().trim();
                dialog.dispose(); // Close dialog after submission
            }
        });

        // Panel for input field and button, using GridBagLayout for more control
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Add components to the panel with constraints
        inputPanel.add(textField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(submitButton, gbc);

        panel.add(inputPanel, BorderLayout.CENTER); // Add inputPanel to the center

        dialog.setContentPane(panel);
        dialog.setVisible(true); // Show dialog

        return playerName[0];
    }

    public static int getAIDifficulty() {
        ImageIcon icon = new ImageIcon("view/coolbackground.jpg"); // Ensure the image path is correct
        Image image = icon.getImage();

        // Create a modal JDialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Select Difficulty");
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(null); // Center on screen
        dialog.setModal(true); // Set modal to block user interaction with other windows
        dialog.setResizable(false);

        // Create a panel with an overridden paintComponent method to draw the background image
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Draw the background image
            }
        };

        JPanel comboBoxPanel = new JPanel(new GridBagLayout());
        comboBoxPanel.setOpaque(false); // Make the panel transparent

        String[] options = {"Easy", "Medium", "Hard"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setFont(new Font("Monospaced", Font.PLAIN, 20)); // Set font to Monospaced with size 20
        comboBox.setBackground(Color.GRAY); // Set a grey background
        Dimension comboBoxSize = new Dimension(200, 45); // Set preferred size
        comboBox.setPreferredSize(comboBoxSize); // Apply the preferred size

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Monospaced", Font.BOLD, 25));
        submitButton.setBackground(new Color(255, 215, 0)); // Gold color background
        submitButton.setForeground(Color.BLACK); // Text color
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Close the dialog on submit
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        comboBoxPanel.add(comboBox, gbc);
        comboBoxPanel.add(submitButton, gbc);

        panel.add(comboBoxPanel, BorderLayout.CENTER);
        dialog.add(panel);
        dialog.setVisible(true);

        // Process the selected item after dialog is disposed
        String selectedItem = (String) comboBox.getSelectedItem();
        if (selectedItem.equals("Hard")) return 1;
        else if (selectedItem.equals("Medium")) return 2;
        else return 3;
    }

}
