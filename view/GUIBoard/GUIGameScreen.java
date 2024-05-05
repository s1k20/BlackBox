package view.GUIBoard;

import controller.GUIInputListener;
import controller.Game;
import controller.GameState;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * GUIGameScreen is the main JPanel that displays the game's graphical user interface.
 * It handles user interactions and draws game elements such as the board and its components.
 */
public class GUIGameScreen extends JPanel {
    private final Game game; // Reference to the game controller, managing game logic and state.
    private final GUIInputListener listener; // Listener for handling clicks which call methods to properly execute tasks

    // Classes which handle the displaying of the main game picture
    private final GUIBoardDraw boardDraw;
    private final GUITextDraw textDraw;

    private static JFrame frame = null;

    // Lists of both drawn hexagon paths and valid number areas
    // In order to handle click interaction
    private final List<HexagonPath> hexagonPaths = new ArrayList<>();
    private final List<NumberArea> numberAreas = new ArrayList<>();

    private boolean isVisible; // Flag for handling what is displayed to the board and what is not

    //Rectangle areas present on the screen in which buttons are placed
    protected Rectangle advanceButton;
    protected Rectangle backToMenuButton;

    protected NumberArea hoveredNumberArea = null; // Current hovered on number for displaying correct text


    /**
     * Constructs a game screen, initialising components and mouse handling
     * and creates instances of classes
     *
     * @param game the game which the screen will display
     */
    public GUIGameScreen(Game game) {
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);

        isVisible = true;
        this.listener = game;
        this.game = game;

        initMouseListener();
        initMouseHover();

        boardDraw = new GUIBoardDraw(this);
        textDraw = new GUITextDraw(this);
    }

    /**
     * Method returns whether the game state manager says the current state can advance
     * @return true if state can advance false otherwise
     */
    protected boolean canAdvanceState() {
        return game.getStateManager().canAdvanceState();
    }

    /**
     * Method returns the current experimenters name
     * @return String name
     */
    protected String getExperimenterName() {
        return game.getPlayerManager().getExperimenterName();
    }

    /**
     * Method returns the current setters name
     * @return String name
     */
    protected String getSetterName() {
        return game.getPlayerManager().getSetterName();
    }

    /**
     * Returns the currently active ray on the board
     * @return most recently sent Ray
     */

    protected Ray getCurrentRay() {
        return game.getBoard().getCurrentRay();
    }

    /**
     * Returns the number of rays that have been sent on the game board.
     * @return integer representing the count of rays sent so far.
     */
    protected int getNumRaysSent() {
        return game.getBoard().getNumRaysSent();
    }

    /**
     * Accessor for the list of hexagon paths involved in the game.
     * @return List of HexagonPath objects representing the paths on the board.
     */
    protected List<HexagonPath> getHexagonPaths() {
        return this.hexagonPaths;
    }

    /**
     * Retrieves the list of number areas from the game.
     * @return a List of NumberArea objects, each representing a numbered section of the game board.
     */
    protected List<NumberArea> getNumberAreas() {
        return this.numberAreas;
    }

    /**
     * Fetches the list of ray markers from the current game board.
     * @return ArrayList of RayMarker objects indicating positions of rays on the board.
     */
    protected ArrayList<RayMarker> getRayMarkers() {
        return game.getBoard().getRayMarkers();
    }

    /**
     * Checks if the game board is currently visible to the player.
     * @return true if the board is visible, false otherwise.
     */
    protected boolean isBoardVisible() {
        return this.isVisible;
    }

    /**
     * Obtains the current state of the game.
     * @return the current GameState of the game.
     */
    protected GameState getCurrentState() {
        return game.getCurrentState();
    }

    /**
     * Accesses the GUI input listener associated with this component.
     * @return the GUIInputListener currently active.
     */
    public GUIInputListener getListener() {
        return this.listener;
    }

    /**
     * Method which displays the actual screen window
     * sets all information to properly initialise window
     *
     * @param setter Setter name for the current round
     * @param experimenter Experimenter name for the current round
     * @param round the number round the game is currently on
     */
    public void showBoard(String setter, String experimenter, int round) {
        SwingUtilities.invokeLater(() -> {
            if (frame == null || !frame.isDisplayable()) {
                frame = new JFrame("Round " + round + " | Setter: " + setter + " | Experimenter: " + experimenter);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(this);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setResizable(false);
            } else {
                frame.repaint();
                frame.revalidate();
            }
        });
    }

    /**
     * Initialises mouse clicks through mouse listener which passed any clicks
     * to the appropriate handleBoardClick method to determine what should be done
     */
    private void initMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleBoardClick(e.getPoint());
            }
        });
    }

    /**
     * Initialises mouse hovers through mouse motion listener which passed any motion
     * to the appropriate handleHover method to determine what should be done if
     * hovering over a clickable area to
     */
    private void initMouseHover() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleHover(e.getPoint());
            }
        });
    }

    /**
     * Method takes a point on the screen and will cycle through possibilities
     * of whether the click is a clickable area and what that area was
     *
     * @param clickedPoint Point on the screen which has been clicked
     */
    private void handleBoardClick(Point clickedPoint) {
        if (backToMenuButton.contains(clickedPoint)) {
            backToMenu();
        }
        else if (canAdvanceState() && advanceButton.contains(clickedPoint)) {
            advanceState();
        }
        if(game.getCurrentState() == GameState.SETTING_ATOMS){
            handleAtomOnBoard(clickedPoint);
        }
        else if (game.getCurrentState() == GameState.GUESSING_ATOMS) {
            handleGuessing(clickedPoint); //method to which encapsulates both sending and guessing in guessing state
        }
    }

    /**
     * Method which is executed if the user wants to return to the menu
     * Discards the current game picture and notifies listener of desire to return
     * to menu
     */
    private void backToMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.dispose();
        }
        listener.onMainMenuToggle();
    }

    /**
     * Method which makes use of implemented listener method to
     * move the game state to the next state
     */
    private void advanceState() {
        listener.advanceGameState();
    }

    /**
     * Method which checks if a hexagon has been clicked and if so will either
     * call listener to tell game controller to place an atom into that position
     * or call the listener to remove an atom if an atom is in that position
     *
     * @param clickedPoint Point on the screen clicked
     */
    private void handleAtomOnBoard(Point clickedPoint) {
        hexagonPaths.forEach(hexPath -> {
            if (hexPath.path.contains(clickedPoint)) {
                //makes use of each hexagons path corresponding coordinates on our board data structure
                if (game.getBoard().getBoardPosition(hexPath.col, hexPath.row) instanceof Atom) {
                    listener.onAtomRemoved(hexPath.col, hexPath.row);
                } else {
                    listener.onAtomPlaced(hexPath.col, hexPath.row);
                }
            }
        });
    }

    /**
     * Method called when game is in guessing state, allowing user to
     * either send a ray or guess an atom
     *
     * @param clickedPoint Point clicked by user
     */
    private void handleGuessing(Point clickedPoint) {
        boardVisible_ENABLE();
        handleAtomOnGuessingBoard(clickedPoint);
        handleSendRay(clickedPoint);
    }

    /**
     * Method which checks if a hexagon on the guessing board has been clicked and if so will either
     * call listener to tell game controller to guess an atom into that position of the guessing board
     * or call the listener to remove an atom guess if an atom is in that position
     *
     * @param clickedPoint Point clicked on the screen
     */
    private void handleAtomOnGuessingBoard(Point clickedPoint) {
        hexagonPaths.forEach(hexPath -> {
            if (hexPath.contains(clickedPoint)) {
                if (game.getGuessingBoard().getBoardPosition(hexPath.col, hexPath.row) instanceof Atom) {
                    listener.onAtomGuessRemoved(hexPath.col, hexPath.row);
                } else {
                    listener.onAtomGuess(hexPath.col, hexPath.row);
                }
                repaint();
            }
        });
    }

    /**
     * Method cycles through all valid locations on the screen in which is clicked
     * a ray will be sent from that location and compares to see if point is contained
     *
     * @param clickedPoint Point clicked on the screen
     */
    private void handleSendRay(Point clickedPoint) {
        for (NumberArea area : numberAreas) {
            if (area.contains(clickedPoint)) {
                listener.onRaySent(Integer.parseInt(area.number));
                repaint();
                break;
            }
        }
    }

    /**
     * Method which sets mouse to a pointer or reverts back depending
     * on the location in which it is at on the screen
     *
     * @param hover Point on the screen where the mouse is
     */
    private void handleHover(Point hover) {
        if (isHovering(hover)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Method to return whether the mouse is over a clickable area
     *
     * @param hover Point on the screen of which the mouse is
     * @return true if mouse is on a clickable area false otherwise
     */
    public boolean isHovering(Point hover) {
        return buttonHover(hover) || boardHover(hover) || numberHover(hover);
    }

    /**
     * Method to return if mouse is over position of advance button and
     * that the game can be advanced - leading to the button appearing on the screen
     *
     * @param hover Point where mouse is on the screen
     * @return true if mouse is over advance button and button is present on screen
     */
    public boolean buttonHover(Point hover) {
        return canAdvanceState() && advanceButton.contains(hover) ||
                backToMenuButton.contains(hover);
    }

    /**
     * Method to return whether mouse is hovering over the board and that
     * the board can be clicked to place/remove and atom
     *
     * @param hover Point where mouse is on the screen
     * @return true if hovering over board false otherwise
     */
    public boolean boardHover(Point hover) {
        return (getCurrentState() == GameState.SETTING_ATOMS || getCurrentState() == GameState.GUESSING_ATOMS) &&
                hexagonPaths.stream().anyMatch(hexagonPath -> hexagonPath.contains(hover));
    }

    /**
     * Method to return whether mouse is hovering over a number which surrounds
     * the board and that the game is in correct state to send a ray
     *
     * @param hover Point where mouse is on the screen
     * @return true if mouse is over a number false otherwise
     */
    public boolean numberHover(Point hover) {
        return getCurrentState() == GameState.GUESSING_ATOMS &&
                numberAreas.stream().anyMatch(numberArea -> numberArea.contains(hover));
    }

    /**
     * Main method for displaying board and text to the screen
     * Makes use of other methods to correctly draw game screen
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        isVisible = canBeVisible();
        drawBoardComponents(g);
    }

    /**
     * Method to call all methods to draw to the screen to include both board,
     * buttons and also any text to be displayed
     *
     * @param g Graphics to allow for drawing
     */
    private void drawBoardComponents(Graphics g) {
        Board currentBoard = determineCurrentBoard();
        Graphics2D g2 = (Graphics2D) g;

        //hexagon paths and number areas cleared as they are redrawn onto the board
        clearLists();

        boardDraw.drawBackground(g2);
        boardDraw.drawBoard(g, currentBoard);
        textDraw.drawGameStateText(g, currentBoard);
        textDraw.drawCurrentRayInfo(g);
        textDraw.drawButtons(g);
    }

    /**
     * Based on current state will return the board which
     * should be passed to draw to correctly display board given current state
     *
     * @return Board object, either the game board or guessing board
     */
    private Board determineCurrentBoard() {
        if (game.getCurrentState() != GameState.GUESSING_ATOMS && game.getCurrentState() != GameState.AI_GUESSING_ATOMS) return game.getBoard();
        else return game.getGuessingBoard();
    }

    /**
     * Method which encapsulates a series of methods to be called when
     * attempting to refresh the board screen
     */
    public void refreshBoard() {
        SwingUtilities.invokeLater(() -> {
            invalidate();
            validate();
            repaint();
        });
    }

    /**
     * Method to properly encapsulate Swings' disposing window
     */
    public void disposeBoard() {
        SwingUtilities.invokeLater(() -> frame.dispose());
    }

    /**
     * Method to make board visible through changing flag
     * and also calling a refresh
     */
    public void boardVisible_ENABLE() {
        SwingUtilities.invokeLater(() -> {
            this.isVisible = true;
            this.refreshBoard();
        });
    }

    /**
     * Method to make the board not visible by
     * changing flag and calling a refresh
     */
    public void boardVisible_DISABLE() {
        SwingUtilities.invokeLater(() -> {
            this.isVisible = false;
            this.refreshBoard();
        });
    }

    /**
     * Determines whether the board should be visible
     * i.e. displaying atoms, ray paths etc.
     *
     * @return true if game state should allow board to be visible
     */
    public boolean canBeVisible() {
        return getCurrentState() != GameState.SENDING_RAYS &&
                getCurrentState() != GameState.AI_HAS_SENT_RAYS;
    }

    /**
     * Method to end a game round through making board
     * visible and getting rid of frame
     */
    public void endRound() {
        this.disposeBoard();
        this.boardVisible_ENABLE();
    }

    /**
     * Method which clears clickable areas which are
     * redrawn on a board repaint()
     */
    private void clearLists() {
        hexagonPaths.clear();
        numberAreas.clear();
    }

    /**
     * Class which holds a numbers corresponding location on the game screen
     */
    protected static class NumberArea {
        Rectangle bounds;
        String number;
        int row;
        int col;

        public NumberArea(Rectangle bounds, String number, int row, int col) {
            this.bounds = bounds;
            this.number = number;
            this.row = row;
            this.col = col;
        }

        public boolean contains(Point p) {
            return bounds.contains(p);
        }
    }

    /**
     * Class to hold a hexagon path and its corresponding row, col index
     * on the main board 2d object array
     */
    protected static class HexagonPath {
        Path2D path;
        int row, col;

        HexagonPath(Path2D path, int row, int col) {
            this.path = path;
            this.row = row;
            this.col = col;
        }

        public boolean contains(Point p) {
            return path.contains(p);
        }
    }
}


