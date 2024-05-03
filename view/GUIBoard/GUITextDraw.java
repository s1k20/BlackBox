package view.GUIBoard;

import controller.GameState;
import model.Board;

import java.awt.*;

public class GUITextDraw {

    GUIGameScreen guiGameScreen;

    public GUITextDraw(GUIGameScreen guiGameScreen) {
        this.guiGameScreen = guiGameScreen;
    }

    //TODO finish fixing this class
    protected void drawGameStateText(Graphics g, Board currentBoard) {
        FontMetrics metrics = g.getFontMetrics();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 20));

        if(guiGameScreen.getCurrentState() == GameState.SETTING_ATOMS || guiGameScreen.getCurrentState() == GameState.GUESSING_ATOMS){
            if(guiGameScreen.hoveredNumberArea != null && guiGameScreen.getCurrentState() == GameState.GUESSING_ATOMS){
                String displayString = "Send ray at: " + guiGameScreen.hoveredNumberArea.number;
                int textWidth = metrics.stringWidth(displayString);
                int x = ((guiGameScreen.getWidth() - metrics.stringWidth(displayString)) / 2) - 115;

                g.drawString(displayString.toUpperCase(), x, 60);

            }
            else{
                String playerName;
                String action;
                if (guiGameScreen.getCurrentState() == GameState.SETTING_ATOMS) {
                    playerName = guiGameScreen.getSetterName();
                    action = "PLACE";
                }
                else {
                    playerName = guiGameScreen.getExperimenterName();
                    action = "GUESS";
                }
                String displayString = playerName + " - " + action + " " + (6 - currentBoard.getNumAtomsPlaced())
                        + " more atoms";
                if (guiGameScreen.getCurrentState()== GameState.GUESSING_ATOMS) {
                    displayString += " or send a ray";
                }
                int textWidth = metrics.stringWidth(displayString);

                g.drawString(displayString, 180, 60);
            }

        }
        else if (guiGameScreen.getCurrentState() == GameState.AI_HAS_SENT_RAYS) {
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            String displayString = "Click 'Continue'";
            int textWidth = metrics.stringWidth(displayString);
            g.drawString(displayString.toUpperCase(), 300, 60);
        }
        else if (guiGameScreen.getCurrentState() == GameState.AI_GUESSING_ATOMS && currentBoard.getNumAtomsPlaced() == 6) {
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            String displayString = "Click 'Finish'";
            int textWidth = metrics.stringWidth(displayString);
            g.drawString(displayString.toUpperCase(), 300, 60);
        }
        else if (guiGameScreen.getCurrentState() != GameState.GAME_OVER){
            if(guiGameScreen.hoveredNumberArea != null){
                // Example hover effect: draw a highlighted border around the NumberArea
                String displayString = "Send ray at: " + guiGameScreen.hoveredNumberArea.number;
                int textWidth = metrics.stringWidth(displayString);
                int x = ((guiGameScreen.getWidth() - metrics.stringWidth(displayString)) / 2) - 115;

                g.drawString(displayString.toUpperCase(), x, 60);

            }
            else{
                g.setFont(new Font("Monospaced", Font.BOLD, 20));
                String displayString = guiGameScreen.getExperimenterName() + " - Click a number to send a ray";
                g.drawString(displayString.toUpperCase(), 195, 60);
            }
        }
        else {
            String displayString = "Full Game Picture";
            g.drawString(displayString.toUpperCase(), 300, 60);
        }
    }

    protected void drawCurrentRayInfo(Graphics g) {
        FontMetrics metrics = g.getFontMetrics();
        if(guiGameScreen.getCurrentRay().getInput() != -1 && guiGameScreen.getCurrentState() != GameState.GAME_OVER){
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            String displayString;

            if(guiGameScreen.getCurrentRay().getOutput() == -1){
                displayString = "Ray " + guiGameScreen.getNumRaysSent() + ": Entered at " + guiGameScreen.getCurrentRay().getInput() +
                        " -> Absorbed!";
            }
            else if (guiGameScreen.getCurrentRay().getDeflectionType() == 180) {
                displayString = "Ray " + guiGameScreen.getNumRaysSent() + ": Entered at " + guiGameScreen.getCurrentRay().getInput() +
                        " -> Reflected!";
            }
            else{
                displayString = "Ray " + guiGameScreen.getNumRaysSent() + ": Entered at " + guiGameScreen.getCurrentRay().getInput() +
                        " -> Exited at " + guiGameScreen.getCurrentRay().getOutput();
            }

            int textWidth = metrics.stringWidth(displayString);
            g.setColor(Color.WHITE);
            g.drawString(displayString.toUpperCase(), ((guiGameScreen.getWidth() - textWidth) / 2) - 60 , guiGameScreen.getHeight() - 40);
        }
    }

    protected void drawButtons(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int buttonWidth = 150;
        int buttonHeight = 30;
        int xB = guiGameScreen.getWidth() - buttonWidth - 5;
        int yB = guiGameScreen.getHeight() - buttonHeight - 30;
        guiGameScreen.printButtonBounds = new Rectangle(xB, yB, buttonWidth + 10 , buttonHeight);
        String text = "";

        if (guiGameScreen.canAdvanceState()) {
            text = "Advance";
        }

        if (!text.isEmpty()) printButton(guiGameScreen.printButtonBounds, text, g2);

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
}
