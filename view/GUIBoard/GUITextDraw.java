package view.GUIBoard;

import controller.GameState;
import model.Board;

import java.awt.*;

public class GUITextDraw {

    GUIGameScreen guiGameScreen;

    public GUITextDraw(GUIGameScreen guiGameScreen) {
        this.guiGameScreen = guiGameScreen;
    }

    protected void drawGameStateText(Graphics g, Board currentBoard) {
        FontMetrics metrics = g.getFontMetrics();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 20));

        StringBuilder displayString = new StringBuilder(); // Initialize the display string

        // Determine what text to display based on the game state
        if (guiGameScreen.getCurrentState() == GameState.SETTING_ATOMS || guiGameScreen.getCurrentState() == GameState.GUESSING_ATOMS) {
            if (guiGameScreen.hoveredNumberArea != null && guiGameScreen.getCurrentState() == GameState.GUESSING_ATOMS) {
                displayString.append("Send ray at: ").append(guiGameScreen.hoveredNumberArea.number);
            } else {
                String playerName = (guiGameScreen.getCurrentState() == GameState.SETTING_ATOMS) ? guiGameScreen.getSetterName() : guiGameScreen.getExperimenterName();
                String action = (guiGameScreen.getCurrentState() == GameState.SETTING_ATOMS) ? "PLACE" : "GUESS";
                displayString.append(playerName).append(" - ").append(action).append(" ").append(6 - currentBoard.getNumAtomsPlaced()).append(" more atoms");
                if (guiGameScreen.getCurrentState() == GameState.GUESSING_ATOMS) {
                    displayString.append(" or send a ray");
                }
            }
        } else if (guiGameScreen.getCurrentState() == GameState.AI_HAS_SENT_RAYS) {
            displayString.append("Click 'Continue'");
        } else if (guiGameScreen.getCurrentState() == GameState.AI_GUESSING_ATOMS && currentBoard.getNumAtomsPlaced() == 6) {
            displayString.append("Click 'Finish'");
        } else if (guiGameScreen.getCurrentState() == GameState.GAME_OVER) {
            displayString.append("Full Game Picture");
        } else {
            if (guiGameScreen.hoveredNumberArea != null) {
                displayString.append("Send ray at: ").append(guiGameScreen.hoveredNumberArea.number);
            } else {
                displayString.append(guiGameScreen.getExperimenterName()).append(" - Click a number to send a ray");
            }
        }

        // Calculate the width of the text and determine the x coordinate for centering
        int textWidth = metrics.stringWidth(String.valueOf(displayString));
        int x = ((guiGameScreen.getWidth() - textWidth) / 2) - 25;
        if (guiGameScreen.getCurrentState() == GameState.GUESSING_ATOMS) x -= 25;
        int y = 60;  // Fixed vertical position; adjust if necessary

        // Draw the string using the calculated x coordinate and fixed y coordinate
        g.drawString(displayString.toString().toUpperCase(), x, y);
    }


    protected void drawCurrentRayInfo(Graphics g) {
        if(isCurrentRay()){
            FontMetrics metrics = g.getFontMetrics();
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g.setColor(Color.WHITE);

            StringBuilder displayString = new StringBuilder();

            displayString.append("Ray ").append(guiGameScreen.getNumRaysSent()).append(" Entered at ").append(guiGameScreen.getCurrentRay().getInput());
            displayString.append(getPrefix());

            int textWidth = metrics.stringWidth(displayString.toString());
            int x = (guiGameScreen.getWidth() - textWidth) / 2;
            int y = guiGameScreen.getHeight() - 40;
            g.drawString(displayString.toString().toUpperCase(), x, y);
        }
    }

    protected void drawButtons(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int buttonWidth = 150;
        int buttonHeight = 30;
        int xB = guiGameScreen.getWidth() - buttonWidth - 5;
        int yB = guiGameScreen.getHeight() - buttonHeight - 30;
        guiGameScreen.advanceButton = new Rectangle(xB, yB, buttonWidth + 10 , buttonHeight);
        String text = "";

        if (guiGameScreen.canAdvanceState()) {
            text = "Advance";
        }

        if (!text.isEmpty()) printButton(guiGameScreen.advanceButton, text, g2);

        guiGameScreen.backToMenuButton = new Rectangle(0, yB, buttonWidth - 60, buttonHeight);

        printButton(guiGameScreen.backToMenuButton, "Menu", g2);
    }

    private void printButton(Rectangle rectangle, String text, Graphics2D g2) {
        Color gold = new Color(51, 51, 51);

        g2.setColor(gold);
        g2.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));

        int centre = 0;
        if (text.equals("Menu")) centre += 4;

        g2.drawString(text, rectangle.x + 16 + centre, rectangle.y + 22);
    }

    private String getPrefix() {
        if(guiGameScreen.getCurrentRay().getOutput() == -1){
            return " -> Absorbed!";
        }
        else if (guiGameScreen.getCurrentRay().getDeflectionType() == 180) {
            return " -> Reflected!";
        }
        else{
            return " -> Exited at " + guiGameScreen.getCurrentRay().getOutput();
        }
    }

    public boolean isCurrentRay() {
        return guiGameScreen.getCurrentRay() != null && guiGameScreen.getCurrentState() != GameState.GAME_OVER;
    }
}
