package view.GUIBoard;

import controller.GameState;
import model.Board;

import java.awt.*;

public class GUITextDraw {

    private final GUIGameScreen guiGameScreen;

    public GUITextDraw(GUIGameScreen guiGameScreen) {
        this.guiGameScreen = guiGameScreen;
    }

    // method to draw information string to the top of the screen
    protected void drawGameStateText(Graphics g, Board currentBoard) {
        FontMetrics metrics = g.getFontMetrics();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 20));

        // create display string which gets correct text to display
        StringBuilder displayString = getInfoString(currentBoard.getNumAtomsPlaced());

        // Calculate the width of the text and determine the x coordinate for centering
        int textWidth = metrics.stringWidth(String.valueOf(displayString));

        int x = ((guiGameScreen.getWidth() - textWidth) / 2) - 25;
        if (guiGameScreen.getCurrentState() == GameState.GUESSING_ATOMS) x -= 25;
        int y = 60;  // Fixed vertical position

        g.drawString(displayString.toString().toUpperCase(), x, y);
    }

    // method to return correct information to be display depending on game state
    private StringBuilder getInfoString(int numAtomsPlaced) {
        StringBuilder displayString = new StringBuilder();
        // Determine what text to display based on the game state
        if (isPlacingOrGuessing()) {
            // if number is being hover over, display text asking user to send ray at that location
            if (guiGameScreen.hoveredNumberArea != null && guiGameScreen.getCurrentState() == GameState.GUESSING_ATOMS) {
                displayString.append("Send ray at: ").append(guiGameScreen.hoveredNumberArea.number);
            }
            // else display option for user of to put an atom onto the board or send a ray
            else {
                String playerName = getCurrentPlayer();
                String action = getCurrentAction();

                displayString.append(playerName).append(" - ").append(action).append(" ").append(6 - numAtomsPlaced).append(" more ");
                if (6 - numAtomsPlaced == 1) {
                    displayString.append("atom");
                }
                else {
                    displayString.append("atoms");
                }

                if (guiGameScreen.getCurrentState() == GameState.GUESSING_ATOMS) {
                    displayString.append(" or send a ray");
                }
            }
        } else if (canDisplayAdvance(numAtomsPlaced)) {
            displayString.append("Click 'Advance'");
        }
        else if (guiGameScreen.getCurrentState() == GameState.GAME_OVER) {
            displayString.append("Full Game Picture");
        } else {
            if (guiGameScreen.hoveredNumberArea != null) {
                displayString.append("Send ray at: ").append(guiGameScreen.hoveredNumberArea.number);
            } else {
                displayString.append(guiGameScreen.getExperimenterName()).append(" - Click a number to send a ray");
            }
        }
        return displayString;
    }

    // method to print to draw the current ray info (most recently sent ray)
    protected void drawCurrentRayInfo(Graphics g) {
        // check that a ray has been sent and if so print info
        if(isCurrentRay()){
            FontMetrics metrics = g.getFontMetrics();
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g.setColor(Color.WHITE);

            StringBuilder displayString = new StringBuilder();

            // create text to be printed given number of rays sent and current rays input
            displayString.append("Ray ").append(guiGameScreen.getNumRaysSent());
            displayString.append(" Entered at ").append(guiGameScreen.getCurrentRay().getInput());
            // find out suffix which will either be an output point, announcing ray was reflected or ray was absorbed
            displayString.append(getSuffix());

            // find location on screen to print info
            int textWidth = metrics.stringWidth(displayString.toString());
            int x = (guiGameScreen.getWidth() - textWidth) / 2;
            int y = guiGameScreen.getHeight() - 40;
            g.drawString(displayString.toString().toUpperCase(), x, y);
        }
    }

    // method which prints all buttons to the screen
    protected void drawButtons(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // get advance button outline ready to be printed
        int buttonWidth = 150;
        int buttonHeight = 30;
        int xB = guiGameScreen.getWidth() - buttonWidth - 5;
        int yB = guiGameScreen.getHeight() - buttonHeight - 30;
        guiGameScreen.advanceButton = new Rectangle(xB, yB, buttonWidth + 10 , buttonHeight);

        // check if game is ready to advance state i.e "Advance" button should be printed
        if (guiGameScreen.canAdvanceState()) {
            printButton(guiGameScreen.advanceButton, "Advance", g2);
        }

        // create and print back to menu button as it is always visible
        guiGameScreen.backToMenuButton = new Rectangle(0, yB, buttonWidth - 60, buttonHeight);
        printButton(guiGameScreen.backToMenuButton, "Menu", g2);
    }

    // method to print a button onto the screen given its rectangular outline
    private void printButton(Rectangle rectangle, String text, Graphics2D g2) {
        // set g2 for printing button
        Color gold = new Color(51, 51, 51);
        g2.setColor(gold);
        g2.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));

        int centre = 0;
        if (text.equals("Menu")) centre += 4; // add 4 to location of text to centralise it

        g2.drawString(text, rectangle.x + 16 + centre, rectangle.y + 22);
    }

    // return prefix of current ray info given ray output
    private String getSuffix() {
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

    // return true if a ray has been sent i.e a current ray exists
    private boolean isCurrentRay() {
        return guiGameScreen.getCurrentRay() != null && guiGameScreen.getCurrentState() != GameState.GAME_OVER;
    }

    private boolean isPlacingOrGuessing() {
        return guiGameScreen.getCurrentState() == GameState.SETTING_ATOMS ||
                guiGameScreen.getCurrentState() == GameState.GUESSING_ATOMS;
    }

    private boolean canDisplayAdvance(int numAtomsPlaced) {
        return guiGameScreen.getCurrentState() == GameState.AI_GUESSING_ATOMS && numAtomsPlaced == 6 ||
                guiGameScreen.getCurrentState() == GameState.AI_HAS_SENT_RAYS;
    }

    private String getCurrentPlayer() {
        return (guiGameScreen.getCurrentState() == GameState.SETTING_ATOMS)
                ? guiGameScreen.getSetterName() : guiGameScreen.getExperimenterName();
    }

    private String getCurrentAction() {
        return (guiGameScreen.getCurrentState() == GameState.SETTING_ATOMS) ? "PLACE" : "GUESS";
    }

}
