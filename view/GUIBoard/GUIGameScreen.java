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

public class GUIGameScreen extends JPanel {
    private final Game game;
    private final GUIInputListener listener;

    private final GUIBoardDraw boardDraw;
    private final GUITextDraw textDraw;

    private static JFrame frame = null;

    private final List<HexagonPath> hexagonPaths = new ArrayList<>();
    private final List<NumberArea> numberAreas = new ArrayList<>();

    private boolean isVisible;
    protected Rectangle printButtonBounds;
    protected Rectangle backToMenuButton;

    protected NumberArea hoveredNumberArea = null;

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

    protected boolean canAdvanceState() {
        return game.getStateManager().canAdvanceState();
    }

    protected String getExperimenterName() {
        return game.getPlayerManager().getExperimenterName();
    }

    protected String getSetterName() {
        return game.getPlayerManager().getSetterName();
    }

    protected Ray getCurrentRay() {
        return game.getBoard().currentRay;
    }

    protected int getNumRaysSent() {
        return game.getBoard().numRaysSent;
    }

    protected List<HexagonPath> getHexagonPaths() {
        return this.hexagonPaths;
    }

    protected List<NumberArea> getNumberAreas() {
        return this.numberAreas;
    }

    protected ArrayList<RayMarker> getRayMarkers() {
        return game.getBoard().getRayMarkers();
    }

    protected boolean isBoardVisible() {
        return this.isVisible;
    }

    protected GameState getCurrentState() {
        return game.getCurrentState();
    }

    public GUIInputListener getListener() {
        return this.listener;
    }

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

    private void initMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleBoardClick(e.getPoint());
            }
        });
    }

    private void initMouseHover() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleHover(e.getPoint());
            }
        });
    }

    private void handleBoardClick(Point clickedPoint) {
        if (backToMenuButton.contains(clickedPoint)) {
            backToMenu();
        }
        else if (canAdvanceState() && printButtonBounds.contains(clickedPoint)) {
            advanceState();
        }
        if(game.getCurrentState() == GameState.SETTING_ATOMS){
            handleAtomOnBoard(clickedPoint);
        }
        else if (game.getCurrentState() == GameState.GUESSING_ATOMS) {
            handleGuessing(clickedPoint); //method to which encapsulates both sending and guessing in guessing state
        }
    }

    private void backToMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.dispose();
        }
        listener.onMainMenuToggle();
    }

    private void advanceState() {
        listener.advanceGameState();
    }

    private void handleAtomOnBoard(Point clickedPoint) {
        hexagonPaths.forEach(hexPath -> {
            if (hexPath.path.contains(clickedPoint)) {
                if (game.getBoard().getBoardPosition(hexPath.col, hexPath.row) instanceof Atom) {
                    listener.onAtomRemoved(hexPath.col, hexPath.row);
                } else {
                    listener.onAtomPlaced(hexPath.col, hexPath.row);
                }
            }
        });
    }

    private void handleGuessing(Point clickedPoint) {
        boardVisible_ENABLE();
        handleAtomOnGuessingBoard(clickedPoint);
        handleSendRay(clickedPoint);
    }

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

    private void handleSendRay(Point clickedPoint) {
        for (NumberArea area : numberAreas) {
            if (area.contains(clickedPoint)) {
                listener.onRaySent(Integer.parseInt(area.number));
                repaint();
                break;
            }
        }
    }

    private void handleHover(Point hover) {
        if (isHovering(hover)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public boolean isHovering(Point hover) {
        return buttonHover(hover) || boardHover(hover) || numberHover(hover);
    }

    public boolean buttonHover(Point hover) {
        return (canAdvanceState() && printButtonBounds.contains(hover)) ||
                backToMenuButton.contains(hover);
    }

    public boolean boardHover(Point hover) {
        return (getCurrentState() == GameState.SETTING_ATOMS || getCurrentState() == GameState.GUESSING_ATOMS) &&
                hexagonPaths.stream().anyMatch(hexagonPath -> hexagonPath.contains(hover));
    }

    public boolean numberHover(Point hover) {
        return getCurrentState() == GameState.GUESSING_ATOMS &&
                numberAreas.stream().anyMatch(numberArea -> numberArea.contains(hover));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        isVisible = canBeVisible();
        drawBoardComponents(g);
    }

    private void drawBoardComponents(Graphics g) {
        Board currentBoard = determineCurrentBoard();
        Graphics2D g2 = (Graphics2D) g;

        //hexagon paths and number areas cleared as they are redrawn onto the board
        hexagonPaths.clear();
        numberAreas.clear();

        boardDraw.drawBackground(g2);
        boardDraw.drawBoard(g, currentBoard);
        textDraw.drawGameStateText(g, currentBoard);
        textDraw.drawCurrentRayInfo(g);
        textDraw.drawButtons(g);
    }

    private Board determineCurrentBoard() {
        if (game.getCurrentState() != GameState.GUESSING_ATOMS && game.getCurrentState() != GameState.AI_GUESSING_ATOMS) return game.getBoard();
        else return game.getGuessingBoard();
    }

    public void refreshBoard() {
        SwingUtilities.invokeLater(() -> {
            invalidate();
            validate();
            repaint();
        });
    }

    public void disposeBoard() {
        SwingUtilities.invokeLater(() -> {
            frame.dispose();
        });
    }

    public void boardVisible_ENABLE() {
        SwingUtilities.invokeLater(() -> {
            this.isVisible = true;
            this.refreshBoard();
        });
    }

    public void boardVisible_DISABLE() {
        SwingUtilities.invokeLater(() -> {
            this.isVisible = false;
            this.refreshBoard();
        });
    }

    public boolean canBeVisible() {
        return getCurrentState() != GameState.SENDING_RAYS &&
                getCurrentState() != GameState.AI_HAS_SENT_RAYS;
    }

    public void endRound() {
        this.disposeBoard();
        this.boardVisible_ENABLE();
    }

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


