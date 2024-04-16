package view;

import controller.GUIInputListener;
import controller.Game;
import controller.GameObserver;
import controller.GameState;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GUIGameBoard extends JPanel implements GameObserver {
    private final int rows = 11; // Number of rows
    private final int cols = 11; // Number of columns
    private final int side = 40; // Side length of the hexagon

    Game game;
    GUIInputListener listener;

    private static JFrame frame = null;

    private List<HexagonPath> hexagonPaths = new ArrayList<>(); // Store hexagon paths with their row and col
    private ArrayList<NumberPath> numberPaths = new ArrayList<>();
    List<NumberArea> numberAreas = new ArrayList<>();

    boolean isVisible;
    private Rectangle printButtonBounds;
    private Rectangle BacktoMenuButton;

    private NumberArea hoveredNumberArea = null;

    BufferedImage atomImage;
    BufferedImage background;
    BufferedImage circle60;
    BufferedImage circle120;
    BufferedImage circle240;
    BufferedImage circle300;
    BufferedImage circle90;
    BufferedImage circle270;
    BufferedImage ray60;
    BufferedImage ray120;
    BufferedImage ray0;


    public GUIGameBoard(Game game) {
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);

        isVisible = true;

        this.listener = game;
        this.game = game;

        loadImages();
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
            if (frame == null || !frame.isDisplayable()) {  // Check if frame is disposed
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
        if (BacktoMenuButton.contains(clickedPoint)) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.dispose();
            }
            listener.onMainMenuToggle();
        }
        if ((game.getCurrentState() == GameState.SENDING_RAYS || game.getCurrentState() == GameState.AI_SENDING_RAYS) && printButtonBounds.contains(clickedPoint)) {
            listener.onFinishRays();
        }
        if (game.getCurrentState() == GameState.AI_GUESSING_ATOMS && printButtonBounds.contains(clickedPoint)) {
            listener.onAI_endRound();
        }
        if (game.getCurrentState() == GameState.GAME_OVER && printButtonBounds.contains(clickedPoint)) {
            listener.onFinishRound();
        }

        if(game.getCurrentState() == GameState.SETTING_ATOMS && game.getBoard().getNumAtomsPlaced() < game.NUM_ATOMS){
            hexagonPaths.forEach(hexPath -> {
                if (hexPath.path.contains(clickedPoint)) {
                    System.out.println("Hexagon clicked at row " + hexPath.row + " and col " + hexPath.col);
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
                    System.out.println("Hexagon clicked at row " + hexPath.row + " and col " + hexPath.col);
                    if (game.getGuessingBoard().getBoardPosition(hexPath.col, hexPath.row) instanceof Atom) {
                        //TODO change this so it does it to guessing board
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
                    // Clicked within the bounds of a number
                    System.out.println("Clicked number: " + area.number + " at row " + area.row + " and col " + area.col);
//                    game.getBoard().sendRay(Integer.parseInt(area.number));
                    listener.onRaySent(Integer.parseInt(area.number));
                    repaint();
                    // Execute any associated action here
                    break; // Assuming only one number can be clicked at a time
                }
            }
        }
    }

    private void handleHover(Point hover) {
        boolean onHexagon = hexagonPaths.stream().anyMatch(numberPath -> numberPath.path.contains(hover)) || printButtonBounds.contains(hover);
        if (onHexagon) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }


                if(game.getBoard().getNumAtomsPlaced() >= game.NUM_ATOMS) {

                    boolean foundHover = false;
                    for (NumberArea area : numberAreas) {
                        if (area.bounds.contains(hover)) {
                            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            // Mouse is hovering over this NumberArea
                            if (hoveredNumberArea != area) {
                                hoveredNumberArea = area;
                                repaint(); // Only repaint if hover state changes
                            }
                            foundHover = true;
                            break; // Exit loop since we found the hover target
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


    private void loadImages() {
        try {
            atomImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("atom.svg.png")));
            background = ImageIO.read(Objects.requireNonNull(getClass().getResource("coolbackground.jpg")));
            circle60 = ImageIO.read(Objects.requireNonNull(getClass().getResource("60CircleOfInfluence.png")));
            circle120 = ImageIO.read(Objects.requireNonNull(getClass().getResource("120CircleOfInfluence.png")));
            circle90 = ImageIO.read(Objects.requireNonNull(getClass().getResource("90CircleOfInfluence.png")));
            circle270 = ImageIO.read(Objects.requireNonNull(getClass().getResource("270CircleOfInfluence.png")));
            circle300 = ImageIO.read(Objects.requireNonNull(getClass().getResource("300CircleOfInfluence.png")));
            circle240 = ImageIO.read(Objects.requireNonNull(getClass().getResource("240CircleOfInfluence.png")));
            ray0 = ImageIO.read(Objects.requireNonNull(getClass().getResource("straight_line_thick.png")));
            ray60 = ImageIO.read(Objects.requireNonNull(getClass().getResource("60_degree_line_thick.png")));
            ray120 = ImageIO.read(Objects.requireNonNull(getClass().getResource("120_degree_line_thick.png")));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawHexagon(Graphics2D g2, double x, double y, int side, int row, int col) {
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
        path.closePath();
        g2.draw(path);

        // Store the hexagon's path with its row and column for click detection
        hexagonPaths.add(new HexagonPath(path, row, col));
    }

    @Override
    public void update() {
        refreshBoard();
    }

    class NumberArea {
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

        if (game.getCurrentState() == GameState.SENDING_RAYS || game.getCurrentState() == GameState.AI_SENDING_RAYS) isVisible = false;

        Image scaledBackground = background.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
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
                double x = (double) ((col * 1.155) * 2 * side * 3) / 4;
                x += (row * (2 * side * 0.8)) - (row * 30);
                x -= 250;

                double y = (double) ((row * 1.16) * Math.sqrt(3) * side * 3) / 4;

//                if(currentBoard.getBoardPosition(col, row) instanceof RayMarker r){
////                    g2.setColor(Color.WHITE);
////                    g2.fillOval((int) x + 113, (int) y + 73, 55, 55);
////                    g2.setColor(r.getGuiColour());
////                    g2.fillOval((int) x + 115, (int) y + 75, 50, 50);
//                    continue;
//                }
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
                    double centerY = 100 + y + (double) Math.sqrt(3) * side / 2;
                    centerY -= 40;
                    centerX -= 10;
                    int circleRadius = side / 4; // Adjust the radius as needed

                    // Your existing logic for drawing atoms remains unchanged
                    if (currentBoard.getBoardPosition(col, row) instanceof Atom && isVisible) {

//                    g2.setColor(Color.RED); // Set color for the circle
//                    g2.fillOval((int) (centerX - circleRadius), (int) (centerY - circleRadius), 4 * circleRadius, 4 * circleRadius);

                        double scale = 0.049;

                        int imageWidth = (int) (atomImage.getWidth() * scale);
                        int imageHeight = (int) (atomImage.getHeight() * scale);

                        // Adjust the image position so that it's centered within the hexagon
                        int imageX = (int) (centerX - imageWidth / 2);
                        int imageY = (int) (centerY - imageHeight / 2);

                        // Draw the scaled image
                        Image scaledImage = atomImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);

                        // Draw the image
                        g2.drawImage(scaledImage, imageX + 10, imageY + 7, this);
                    }
                    else if(currentBoard.getBoardPosition(col, row) instanceof Board.RayTrails rt && isVisible){
                        for (Board.RayTrail r : rt.getRayTrails()) {
                            double scale = 0.63;

                            if(r.getOrientation() == 60 || r.getOrientation() == 240){
                                int imageWidth = (int) (ray60.getWidth() * scale);
                                int imageHeight = (int) (ray60.getHeight() * scale);
                                int imageX = (int) (centerX - imageWidth / 2);
                                int imageY = (int) (centerY - imageHeight / 2);

                                Image scaledImage = ray60.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                                // Draw the image
                                g2.drawImage(scaledImage, imageX - 17, imageY + 62, this);


                            }
                            else if(r.getOrientation() == 0 || r.getOrientation() == 180){
                                scale = 0.25;
                                int imageWidth = (int) (ray0.getWidth() * scale);
                                int imageHeight = (int) (ray0.getHeight() * scale);
                                int imageX = (int) (centerX - imageWidth / 2);
                                int imageY = (int) (centerY - imageHeight / 2);

                                Image scaledImage = ray0.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                                // Draw the image
                                g2.drawImage(scaledImage, imageX + 10, imageY + 7, this);

                            }
                            else{
                                int imageWidth = (int) (ray120.getWidth() * scale);
                                int imageHeight = (int) (ray120.getHeight() * scale);
                                int imageX = (int) (centerX - imageWidth / 2);
                                int imageY = (int) (centerY - imageHeight / 2);

                                Image scaledImage = ray120.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                                // Draw the image
                                g2.drawImage(scaledImage, imageX + 45, imageY + 63, this);

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
        if (game.getCurrentState() == GameState.SENDING_RAYS) {
            g2.setColor(Gold); // Use a more visible color
            g2.fillRect(printButtonBounds.x, printButtonBounds.y, printButtonBounds.width, printButtonBounds.height);
            g2.setColor(Color.WHITE); // Ensure text is visible
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g2.drawString("Finish Rays", printButtonBounds.x + 16, printButtonBounds.y + 22);
        }
        else if (game.getCurrentState() == GameState.AI_SENDING_RAYS) {
            g2.setColor(Gold); // Use a more visible color
            g2.fillRect(printButtonBounds.x, printButtonBounds.y, printButtonBounds.width, printButtonBounds.height);
            g2.setColor(Color.WHITE); // Ensure text is visible
            g.setFont(new Font("Monospaced", Font.BOLD, 20));
            g2.drawString("Continue", printButtonBounds.x + 16, printButtonBounds.y + 22);
        }
        else if (game.getCurrentState() == GameState.AI_GUESSING_ATOMS) {
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


        BacktoMenuButton = new Rectangle(0, yB, buttonWidth - 60, buttonHeight);


        g2.setColor(Gold); // Use a more visible color
        g2.fillRect(BacktoMenuButton.x, BacktoMenuButton.y, BacktoMenuButton.width, BacktoMenuButton.height);
        g2.setColor(Color.WHITE); // Ensure text is visible
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g2.drawString("Menu", BacktoMenuButton.x + 20, BacktoMenuButton.y + 22);

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
        double centerY = 100 + y + (double) Math.sqrt(3) * side / 2;
        centerY -= 40;
        centerX -= 10;
        int circleRadius = side / 4; // Adjust the radius as needed
//                    g2.setColor(Color.RED); // Set color for the circle
//                    g2.fillOval((int) (centerX - circleRadius), (int) (centerY - circleRadius), 4 * circleRadius, 4 * circleRadius);

        double scale = 0.3;
//        double scale = 0.05;

        if(c.getOrientation() == 60){
            circleOfInfluenceImage(circle60, g2, centerX, centerY, scale);
        }
        else if(c.getOrientation() == 120){
            circleOfInfluenceImage(circle120, g2, centerX, centerY, scale);
        }
        else if(c.getOrientation() == 240){
            circleOfInfluenceImage(circle240, g2, centerX, centerY, scale);
        }
        else if(c.getOrientation() == 300){
            circleOfInfluenceImage(circle300, g2, centerX, centerY, scale);
        }
        else if(c.getOrientation() == 90){
            circleOfInfluenceImage(circle90, g2, centerX + 4, centerY, scale * 1.1);
        }
        else if(c.getOrientation() == 270){
            circleOfInfluenceImage(circle270, g2, centerX - 7, centerY, scale);
        }
    }

    private void paintRayMarker(Graphics g2, double x, double y) {
        // Define the dimensions of the square
        int width = 100; // Width of the square
        int height = 100; // Height of the square

        // Draw the outline of a square
        g2.setColor(Color.BLACK);
        g2.fillOval((int)x, (int)y, width, height);
        // Use g2.fillRect((int)x, (int)y, width, height) if you want to draw a filled square.
    }


}


