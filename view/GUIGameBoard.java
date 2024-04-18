package view;

import controller.GUIInputListener;
import controller.Game;
import controller.GameObserver;
import controller.GameState;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GUIGameBoard extends JPanel implements GameObserver {
    private final int rows = 11; // Number of rows
    private final int cols = 11; // Number of columns
    private final int side = 40; // Side length of the hexagon

    Game game;
    GUIInputListener listener;

    private static JFrame frame = null;

    private final List<HexagonPath> hexagonPaths = new ArrayList<>(); // Store hexagon paths with their row and col
    private final ArrayList<NumberPath> numberPaths = new ArrayList<>();
    private final List<NumberArea> numberAreas = new ArrayList<>();

    boolean isVisible;
    private Rectangle printButtonBounds;
    private Rectangle backToMenuButton;

    private NumberArea hoveredNumberArea = null;

    private final GameImages images;


    public GUIGameBoard(Game game) {
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);

        isVisible = true;

        this.listener = game;
        this.game = game;

        images = new GameImages();
        initMouseListener();
        initMouseHover();

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

    private void handleBoardClick(Point clickedPoint) {
        if (backToMenuButton.contains(clickedPoint)) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.dispose();
            }
            listener.onMainMenuToggle();
        }
        if ((game.getCurrentState() == GameState.AI_HAS_SENT_RAYS || (game.getCurrentState() == GameState.SENDING_RAYS && !game.stateManager.isAiSending())) && printButtonBounds.contains(clickedPoint)) {
            listener.onFinishRays();
        }
        else if (game.getCurrentState() == GameState.AI_GUESSING_ATOMS && game.getGuessingBoard().getNumAtomsPlaced() >= game.NUM_ATOMS && printButtonBounds.contains(clickedPoint)) {
            listener.onAI_endRound();
        }
        else if (game.getCurrentState() == GameState.GAME_OVER && printButtonBounds.contains(clickedPoint)) {
            listener.onFinishRound();
        }

        if(game.getCurrentState() == GameState.SETTING_ATOMS && game.getBoard().getNumAtomsPlaced() < game.NUM_ATOMS){
            hexagonPaths.forEach(hexPath -> {
                if (hexPath.path.contains(clickedPoint)) {
                    if (game.getBoard().getBoardPosition(hexPath.col, hexPath.row) instanceof Atom) {
                        listener.onAtomRemoved(hexPath.col, hexPath.row);
                    } else {
                        listener.onAtomPlaced(hexPath.col, hexPath.row);
                    }
                    repaint();
                }
            });

            if (game.getBoard().getNumAtomsPlaced() == game.NUM_ATOMS) {
                boardVisible_DISABLE();
            }
        }
        else if (game.getCurrentState() == GameState.GUESSING_ATOMS && game.getGuessingBoard().getNumAtomsPlaced() < game.NUM_ATOMS) {
            boardVisible_ENABLE();
            hexagonPaths.forEach(hexPath -> {
                if (hexPath.path.contains(clickedPoint)) {
                    if (game.getGuessingBoard().getBoardPosition(hexPath.col, hexPath.row) instanceof Atom) {
                        listener.onAtomGuessRemoved(hexPath.col, hexPath.row);
                    } else {
                        listener.onAtomGuess(hexPath.col, hexPath.row);
                    }
                    repaint();
                }
            });
        }

        else{
            for (NumberArea area : numberAreas) {
                if (area.contains(clickedPoint)) {
                    listener.onRaySent(Integer.parseInt(area.number));
                    repaint();
                    break;
                }
            }
        }
    }

    private void handleHover(Point hover) {
        boolean onHover = hexagonPaths.stream().anyMatch(numberPath -> numberPath.path.contains(hover)) || backToMenuButton.contains(hover)
                || (printButtonBounds.contains(hover) && ((game.getCurrentState() == GameState.AI_HAS_SENT_RAYS || (game.getCurrentState() == GameState.SENDING_RAYS && !game.stateManager.isAiSending())) ||
                game.getCurrentState() == GameState.AI_GUESSING_ATOMS && game.getGuessingBoard().getNumAtomsPlaced() >= game.NUM_ATOMS
        || game.getCurrentState() == GameState.GAME_OVER));
        if (onHover) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
            if(game.getBoard().getNumAtomsPlaced() >= game.NUM_ATOMS) {

                boolean foundHover = false;
                for (NumberArea area : numberAreas) {
                    if (area.bounds.contains(hover)) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                        if (hoveredNumberArea != area) {
                            hoveredNumberArea = area;
                            repaint();
                        }
                        foundHover = true;
                        break;
                    }
                }
                if (!foundHover && hoveredNumberArea != null) {
                    hoveredNumberArea = null; // Clear hover state if not over a NumberArea
                    setCursor(Cursor.getDefaultCursor());
                    repaint(); // Repaint to remove hover effect
                }
            }
    }

    public void refreshBoard() {
        SwingUtilities.invokeLater(() -> {
            invalidate();  // Invalidate the layout
            validate();    // Force the layout manager to reset the layout
            repaint();     // Request a repaint
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
            this.repaint();
        });
    }

    public void boardVisible_DISABLE() {
        SwingUtilities.invokeLater(() -> {
            this.isVisible = false;
            this.repaint();
        });
    }


    private void drawHexagon(Graphics2D g2, double x, double y, int side, int row, int col) {
//        double height = (int) (Math.sin(Math.PI / 3) * side);
        double width = 2 * side;
        Path2D path = new Path2D.Double();

        double startX = 100 + x + width / 2;
        double startY = 100 + y;


        g2.setColor(new Color(255, 196, 0));
        g2.setStroke(new BasicStroke(4));


        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI / 6 * (i + 0.5);
            int xOff = (int) (Math.cos(angle) * side);
            int yOff = (int) (Math.sin(angle) * side);
            if (i == 0) {
                path.moveTo(startX + xOff, startY + yOff);
            } else {
                path.lineTo(startX + xOff, startY + yOff);
            }
        }
        path.closePath();
        g2.draw(path);

        // Store the hexagon's path with its row and column for click detection
        hexagonPaths.add(new HexagonPath(path, row, col));
    }

    @Override
    public void update() {
        refreshBoard();
    }

    static class NumberArea {
        Rectangle bounds; // This holds the x, y, width, and height.
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


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Board currentBoard;
        if (game.getCurrentState() != GameState.GUESSING_ATOMS && game.getCurrentState() != GameState.AI_GUESSING_ATOMS) currentBoard = game.getBoard();
        else currentBoard = game.getGuessingBoard();

        Graphics2D g2 = (Graphics2D) g;
        hexagonPaths.clear();
        int counter = 1;

        if (game.getCurrentState() == GameState.SENDING_RAYS || game.getCurrentState() == GameState.AI_HAS_SENT_RAYS) isVisible = false;
//        System.out.println(isVisible);

        Image scaledBackground = images.background.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
        g2.drawImage(scaledBackground, 0, 0, this);

        FontMetrics metrics = g.getFontMetrics();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 40));



        if(currentBoard.getNumAtomsPlaced() < 6){
            String playerName;
            String action;
            if (game.getCurrentState() == GameState.SETTING_ATOMS) {
                playerName = game.getSetter().getPlayerName();
                action = "PLACE";
            }
            else {
                playerName = game.getExperimenter().getPlayerName();
                action = "GUESS";
            }
            String displayString = playerName + " - " + action + " " + (6 - currentBoard.getNumAtomsPlaced())
                    + " more atoms";
            int textWidth = metrics.stringWidth(displayString);

            g.drawString(displayString, 30, 75);
        }
        else if (game.getCurrentState() != GameState.GAME_OVER){
            if(hoveredNumberArea != null){
                // Example hover effect: draw a highlighted border around the NumberArea
                String displayString = "Send ray at: " + hoveredNumberArea.number;
                int textWidth = metrics.stringWidth(displayString);
                int x = ((getWidth() - metrics.stringWidth(displayString)) / 2) - 115;

                g.drawString(displayString.toUpperCase(), x, 60);

            }
            else{
                g.setFont(new Font("Monospaced", Font.BOLD, 20));
                String displayString = game.getExperimenter().getPlayerName() + " - Click a number to send a ray";
                int textWidth = metrics.stringWidth(displayString);
                g.drawString(displayString.toUpperCase(), ((this.getWidth() - textWidth) / 2) - 215, 60);
            }
        }
        else {
            // Example hover effect: draw a highlighted border around the NumberArea
            String displayString = "Full Game Picture";
            int textWidth = metrics.stringWidth(displayString);
            int x = ((getWidth() - metrics.stringWidth(displayString)) / 2) - 115;

            g.drawString(displayString.toUpperCase(), x, 60);
        }


        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (currentBoard.getBoardPosition(col, row) instanceof Board.NullHex) {
                    continue;
                }

                // Calculate x and y positions based on your original logic
                double x = ((col * 1.155) * 2 * side * 3) / 4;
                x += (row * (2 * side * 0.8)) - (row * 30);
                x -= 250;

                double y = ((row * 1.16) * Math.sqrt(3) * side * 3) / 4;

                if(currentBoard.getBoardPosition(col, row) instanceof Board.EmptyMarker || currentBoard.getBoardPosition(col, row) instanceof RayMarker){
                    g.setFont(new Font("Roboto", Font.BOLD, 16));
                    g.setColor(Color.white);


                    RayOutputPoint p1 = new RayOutputPoint(col, row, true);
                    RayOutputPoint p2 = new RayOutputPoint(col, row, false);

                    String num1 = String.valueOf(currentBoard.numberOut.get(new RayOutputPoint(col, row, true)));
                    String num2 = String.valueOf(currentBoard.numberOut.get(new RayOutputPoint(col, row, false)));

                    metrics = g.getFontMetrics();

                    int textHeight = metrics.getHeight();
                    Rectangle bounds1;
                    Rectangle bounds2;

                    try {
                        int number1 = Integer.parseInt(num1);
                        int textWidth1 = metrics.stringWidth(num1);
                        if(number1 < 10){
                            g.drawString(num1, (int) x + 155, (int) y + 103);
                            bounds1 = new Rectangle((int) x - (textWidth1 / 2) + 160 - 5, (int) y - (textHeight / 2) + 98, textWidth1 + 10, textHeight);
                        }
                        else if(number1 < 19){
                            g.drawString(num1, (int) x + 140, (int) y + 90);
                            bounds1 = new Rectangle((int) x - (textWidth1 / 2) + 150, (int) y - (textHeight / 2) + 85, textWidth1, textHeight);

                        }
                        else if(number1 <= 28){
                            g.drawString(num1, (int) x + 120, (int) y + 90);
                            bounds1 = new Rectangle((int) x - (textWidth1 / 2) + 130, (int) y - (textHeight / 2) + 85, textWidth1, textHeight);

                        }
                        else if(number1 < 37){
                            g.drawString(num1, (int) x + 111, (int) y + 105);
                            bounds1 = new Rectangle((int) x - (textWidth1 / 2) + 122, (int) y - (textHeight / 2) + 100, textWidth1, textHeight);

                        }
                        else if(number1 < 46){
                            g.drawString(num1, (int) x + 125, (int) y + 125);
                            bounds1 = new Rectangle((int) x - (textWidth1 / 2) + 137, (int) y - (textHeight / 2) + 120, textWidth1, textHeight);

                        }
                        else{
                            g.drawString(num1, (int) x + 140, (int) y + 125);
                            bounds1 = new Rectangle((int) x - (textWidth1 / 2) + 153, (int) y - (textHeight / 2) + 120, textWidth1, textHeight);
                        }

                        numberAreas.add(new NumberArea(bounds1, num1, row, col));
                    } catch (NumberFormatException e) {
                        // If parsing fails, do not draw the strings
                        // Optionally, handle the error or log it
                    }
                    try {
                        int number1 = Integer.parseInt(num2);
                        int textWidth2 = metrics.stringWidth(num2);


                        if(number1 < 10){
                            g.drawString(num2, (int) x + 147, (int) y + 125);
                            bounds2 = new Rectangle((int) x - (textWidth2 / 2) + 152 - 5, (int) y - (textHeight / 2) + 120, textWidth2 + 10, textHeight);
                        }
                        else if(number1 < 19){
                            g.drawString(num2, (int) x + 150, (int) y + 110);
                            bounds2 = new Rectangle((int) x - (textWidth2 / 2) + 160, (int) y - (textHeight / 2) + 107, textWidth2, textHeight);
                        }
                        else if(number1 <= 28){
                            g.drawString(num2, (int) x + 143, (int) y + 90);
                            bounds2 = new Rectangle((int) x - (textWidth2 / 2) + 154, (int) y - (textHeight / 2) + 85, textWidth2, textHeight);
                        }
                        else if(number1 < 37){
                            g.drawString(num2, (int) x + 125, (int) y + 85);
                            bounds2 = new Rectangle((int) x - (textWidth2 / 2) + 135, (int) y - (textHeight / 2) + 79, textWidth2, textHeight);
                        }
                        else if(number1 < 46){
                            g.drawString(num2, (int) x + 110, (int) y + 102);
                            bounds2 = new Rectangle((int) x - (textWidth2 / 2) + 122, (int) y - (textHeight / 2) + 95, textWidth2, textHeight);
                        }
                        else{
                            g.drawString(num2, (int) x + 120, (int) y + 125);
                            bounds2 = new Rectangle((int) x - (textWidth2 / 2) + 130, (int) y - (textHeight / 2) + 120, textWidth2, textHeight);
                        }


                        numberAreas.add(new NumberArea(bounds2, num2, row, col));
                    } catch (NumberFormatException e) {
                        // If parsing fails, do not draw the strings
                        // Optionally, handle the error or log it
                    }



                    double height = (int) (Math.sin(Math.PI / 3) * side);
                    double width = 2 * side;
                    Path2D path = new Path2D.Double();

                    double startX = 100 + x + width / 2;
                    double startY = 100 + y;


                    g2.setColor(new Color(255, 196, 0));
                    g2.setStroke(new BasicStroke(4));


                    for (int i = 0; i < 6; i++) {
                        double angle = 2 * Math.PI / 6 * (i + 0.5);
                        int xOff = (int) (Math.cos(angle) * side);
                        int yOff = (int) (Math.sin(angle) * side);
                        if (i == 0) {
                            path.moveTo(startX + xOff, startY + yOff);
                        } else {
                            path.lineTo(startX + xOff, startY + yOff);
                        }
                    }

//                    g2.draw(path);
                    path.closePath();

                }


                else{
                    drawHexagon(g2, x, y, side, row, col); // Pass row and col to drawHexagon

                    double centerX = 100 + x + (double) 2 * side / 2;
                    double centerY = 100 + y + Math.sqrt(3) * side / 2;
                    centerY -= 40;
                    centerX -= 10;
                    int circleRadius = side / 4; // Adjust the radius as needed

                    // Your existing logic for drawing atoms remains unchanged
                    if (currentBoard.getBoardPosition(col, row) instanceof Atom && isVisible) {

//                    g2.setColor(Color.RED); // Set color for the circle
//                    g2.fillOval((int) (centerX - circleRadius), (int) (centerY - circleRadius), 4 * circleRadius, 4 * circleRadius);

                        double scale = 0.049;

                        int imageWidth = (int) (images.atomImage.getWidth() * scale);
                        int imageHeight = (int) (images.atomImage.getHeight() * scale);

                        // Adjust the image position so that it's centered within the hexagon
                        int imageX = (int) (centerX - imageWidth / 2);
                        int imageY = (int) (centerY - imageHeight / 2);

                        // Draw the scaled image
                        Image scaledImage = images.atomImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);

                        // Draw the image
                        g2.drawImage(scaledImage, imageX + 10, imageY + 7, this);
                    }
                    else if(currentBoard.getBoardPosition(col, row) instanceof Board.RayTrails rt && isVisible){
                        for (Board.RayTrail r : rt.getRayTrails()) {
                            double scale = 0.63;

                            if(r.getOrientation() == 60 || r.getOrientation() == 240){
                                int imageWidth = (int) (images.ray60.getWidth() * scale);
                                int imageHeight = (int) (images.ray60.getHeight() * scale);
                                int imageX = (int) (centerX - imageWidth / 2);
                                int imageY = (int) (centerY - imageHeight / 2);

                                Image scaledImage = images.ray60.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                                // Draw the image
                                g2.drawImage(scaledImage, imageX - 17, imageY + 62, this);


                            }
                            else if(r.getOrientation() == 0 || r.getOrientation() == 180){
                                scale = 0.25;
                                int imageWidth = (int) (images.ray0.getWidth() * scale);
                                int imageHeight = (int) (images.ray0.getHeight() * scale);
                                int imageX = (int) (centerX - imageWidth / 2);
                                int imageY = (int) (centerY - imageHeight / 2);

                                Image scaledImage = images.ray0.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                                // Draw the image
                                g2.drawImage(scaledImage, imageX + 10, imageY + 7, this);

                            }
                            else{
                                int imageWidth = (int) (images.ray120.getWidth() * scale);
                                int imageHeight = (int) (images.ray120.getHeight() * scale);
                                int imageX = (int) (centerX - imageWidth / 2);
                                int imageY = (int) (centerY - imageHeight / 2);

                                Image scaledImage = images.ray120.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                                // Draw the image
                                g2.drawImage(scaledImage, imageX + 45, imageY + 60, this);

                            }
                        }

                    }
                    else if(currentBoard.getBoardPosition(col, row) instanceof CircleOfInfluence c && isVisible){
                        printCircleOfInfluence(c, g2, x, y);
                    }
                    else if (currentBoard.getBoardPosition(col, row) instanceof IntersectingCircleOfInfluence is && isVisible){
                        for(int i = 0; i < is.getCircleOfInfluences().size(); i++){
                            printCircleOfInfluence(is.getCircleOfInfluence(i), g2, x, y);
                        }
                    }
                }
            }
        }

        //testing hitbox
//        for (NumberArea area : numberAreas) {
//
//                g2.setColor(Color.RED); // Set a high-contrast color
//                g2.fill(area.bounds);
//
//
//            // Draw the bounds for visual inspection
//        }

        for (NumberArea n : numberAreas) {
            int num = Integer.parseInt(n.number);
            if (game.getBoard().getRayMarkers() != null) {
                for (RayMarker r : game.getBoard().getRayMarkers()) {
                    if (r.getNumber() == num) {
                        g2.setColor(Color.WHITE);
                        g2.fillRect(n.bounds.x - 3, n.bounds.y - 3, n.bounds.width + 6, n.bounds.height + 6);
                        g2.setColor(r.getGuiColour());
                        g2.fill(n.bounds);
                    }
                }
            }
        }

        if(game.getBoard().currentRay.getInput() != -1 && game.getCurrentState() != GameState.GUESSING_ATOMS && game.getCurrentState() != GameState.GAME_OVER){
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            String displayString;

            if(game.getBoard().currentRay.getOutput() == -1){
                displayString = "Ray " + game.getBoard().numRaysSent + ": Entered at " + game.getBoard().currentRay.getInput() +
                        " -> Absorbed!";
            }
            else if (game.getBoard().currentRay.getOutput() == game.getBoard().currentRay.getInput()) {
                displayString = "Ray " + game.getBoard().numRaysSent + ": Entered at " + game.getBoard().currentRay.getInput() +
                        " -> Reflected!";
            }
            else{
                displayString = "Ray " + game.getBoard().numRaysSent + ": Entered at " + game.getBoard().currentRay.getInput() +
                        " -> Exited at " + game.getBoard().currentRay.getOutput();
            }

            int textWidth = metrics.stringWidth(displayString);
            g.setColor(Color.WHITE);
            g.drawString(displayString.toUpperCase(), ((this.getWidth() - textWidth) / 2) - 60 , getHeight() - 40);
        }

        int buttonWidth = 150;
        int buttonHeight = 30;
        int xB = getWidth() - buttonWidth - 5; // Adjusted for the current width
        int yB = getHeight() - buttonHeight - 30; // Adjusted for the current height
        printButtonBounds = new Rectangle(xB, yB, buttonWidth + 10 , buttonHeight);
        Color Gold = new Color(51, 51, 51);

        // Now draw the button with updated bounds
        if (game.getCurrentState() == GameState.SENDING_RAYS && !game.stateManager.isAiSending()) {
            g2.setColor(Gold); // Use a more visible color
            g2.fillRect(printButtonBounds.x, printButtonBounds.y, printButtonBounds.width, printButtonBounds.height);
            g2.setColor(Color.WHITE); // Ensure text is visible
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g2.drawString("Finish Rays", printButtonBounds.x + 16, printButtonBounds.y + 22);
        }
        else if (game.getCurrentState() == GameState.AI_HAS_SENT_RAYS && !game.stateManager.isAiSending()) {
            g2.setColor(Gold); // Use a more visible color
            g2.fillRect(printButtonBounds.x, printButtonBounds.y, printButtonBounds.width, printButtonBounds.height);
            g2.setColor(Color.WHITE); // Ensure text is visible
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g2.drawString("Continue", printButtonBounds.x + 16, printButtonBounds.y + 22);
        }
        else if (game.getCurrentState() == GameState.AI_GUESSING_ATOMS && game.getGuessingBoard().getNumAtomsPlaced() >= game.NUM_ATOMS) {
            g2.setColor(Gold); // Use a more visible color
            g2.fillRect(printButtonBounds.x, printButtonBounds.y, printButtonBounds.width, printButtonBounds.height);
            g2.setColor(Color.WHITE); // Ensure text is visible
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g2.drawString("Finish", printButtonBounds.x + 16, printButtonBounds.y + 22);
        }
        else if (game.getCurrentState() == GameState.GAME_OVER) {
            g2.setColor(Gold); // Use a more visible color
            g2.fillRect(printButtonBounds.x, printButtonBounds.y, printButtonBounds.width, printButtonBounds.height);
            g2.setColor(Color.WHITE); // Ensure text is visible
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g2.drawString("Next", printButtonBounds.x + 16, printButtonBounds.y + 22);
        }


        backToMenuButton = new Rectangle(0, yB, buttonWidth - 60, buttonHeight);


        g2.setColor(Gold); // Use a more visible color
        g2.fillRect(backToMenuButton.x, backToMenuButton.y, backToMenuButton.width, backToMenuButton.height);
        g2.setColor(Color.WHITE); // Ensure text is visible
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g2.drawString("Menu", backToMenuButton.x + 20, backToMenuButton.y + 22);

    }

    // Inner class to store hexagon path and its row and column
    private static class HexagonPath {
        Path2D path;
        int row, col;

        HexagonPath(Path2D path, int row, int col) {
            this.path = path;
            this.row = row;
            this.col = col;
        }
    }

    private static class NumberPath {
        Path2D path;
        int num;

        NumberPath(Path2D path, int num) {
            this.path = path;
            this.num = num;
        }
    }

    public void circleOfInfluenceImage(BufferedImage img, Graphics2D g2, double centreX, double centreY, double scale){
        int imageWidth = (int) (img.getWidth() * scale);
        int imageHeight = (int) (img.getHeight() * scale);

        // Adjust the image position so that it's centered within the hexagon
        int imageX = (int) (centreX - imageWidth / 2);
        int imageY = (int) (centreY - imageHeight / 2);

        // Draw the scaled image
        Image scaledImage = img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        g2.drawImage(scaledImage, imageX + 10, imageY + 7, this);
    }

    public void printCircleOfInfluence(CircleOfInfluence c, Graphics2D g2, double x, double y){
        double centerX = 100 + x + (double) 2 * side / 2;
        double centerY = 100 + y + Math.sqrt(3) * side / 2;
        centerY -= 40;
        centerX -= 10;
        int circleRadius = side / 4; // Adjust the radius as needed
//                    g2.setColor(Color.RED); // Set color for the circle
//                    g2.fillOval((int) (centerX - circleRadius), (int) (centerY - circleRadius), 4 * circleRadius, 4 * circleRadius);

        double scale = 0.3;

        if(c.getOrientation() == 60){
            circleOfInfluenceImage(images.circle60, g2, centerX, centerY, scale);
        }
        else if(c.getOrientation() == 120){
            circleOfInfluenceImage(images.circle120, g2, centerX, centerY, scale);
        }
        else if(c.getOrientation() == 240){
            circleOfInfluenceImage(images.circle240, g2, centerX, centerY, scale);
        }
        else if(c.getOrientation() == 300){
            circleOfInfluenceImage(images.circle300, g2, centerX, centerY, scale);
        }
        else if(c.getOrientation() == 90){
            circleOfInfluenceImage(images.circle90, g2, centerX + 4, centerY, scale * 1.1);
        }
        else if(c.getOrientation() == 270){
            circleOfInfluenceImage(images.circle270, g2, centerX - 7, centerY, scale);
        }
    }

}


