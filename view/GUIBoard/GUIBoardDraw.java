package view.GUIBoard;

import model.*;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import static model.BoardConstants.*;

/**
 * Class which handles drawing everything regarding
 * drawing to the hexagonal board itself
 */
public class GUIBoardDraw {
    private final int SIDE = 40; //hexagon side length

    private final GUIGameScreen guiGameScreen;
    private final GameImages images;

    private Graphics g;
    private Graphics2D g2;

    /**
     * Main constructor for class to set main screen and
     * also load images to the class
     * @param guiGameScreen Main game screen which this class prints to
     */
    public GUIBoardDraw(GUIGameScreen guiGameScreen) {
        this.guiGameScreen = guiGameScreen;
        this.images = new GameImages();
    }

    /**
     * Method which will draw the board and call methods to also
     * draw the contents of the board to display current state of game
     *
     * @param g Graphics used to print
     * @param currentBoard The board which is being printed
     */
    protected void drawBoard(Graphics g, Board currentBoard) {
        this.g = g;
        this.g2 = (Graphics2D) g;

        // Iterate through board data structure and print each indexes contents
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (!(currentBoard.getBoardPosition(col, row) instanceof Board.NullHex)) {
                    //if position is a ray marker position, print numbers associated to that position
                    if(isRayMarkerPosition(currentBoard, col, row)){
                        drawInputNumbers(col, row, currentBoard);
                    }
                    else {
                        //drawing outline hexagonal shape
                        drawHexagon(row, col);
                        if (guiGameScreen.isBoardVisible()) {
                            drawHexagonContents(currentBoard.getBoardPosition(col, row), col, row);
                        }
                    }
                }
            }
        }
        drawRayMarkers();
    }

    /**
     * Method to determine if a given index on a board is a position for a RayMarker
     * @param currentBoard Board which is getting check
     * @param col index which is being checked
     * @param row index which is being checked
     * @return true if is a RayMarker position false otherwise
     */
    private boolean isRayMarkerPosition(Board currentBoard, int col, int row) {
        return currentBoard.getBoardPosition(col, row) instanceof Board.EmptyMarker ||
                currentBoard.getBoardPosition(col, row) instanceof RayMarker;
    }

    /**
     * Method to print the outline number which surround the hexagon board for ray inputs
     * Method uses indexes on the board data structure to determine what set of 2 numbers
     * make up that positions input numbers, or in some cases only 1 number is used for a given position
     * @param col index for a given RayMarker/Input position
     * @param row index for a given RayMarker/Input position
     * @param currentBoard The board being displayed
     */
    private void drawInputNumbers(int col, int row, Board currentBoard) {
        g.setFont(new Font("Roboto", Font.BOLD, 16));
        g.setColor(Color.white);

        // isFirst refers to which number comes first in a given hexagon.
        // Hexagon contains 2, 3 then 2 isFirst == true or hexagon contains 35, 36 then 35 isFirst == true
        String num2 = String.valueOf(currentBoard.getNumberOut().get(new RayOutputPoint(col, row, false)));
        String num1 = String.valueOf(currentBoard.getNumberOut().get(new RayOutputPoint(col, row, true)));

        // draw the second number, isFirst is passed as false as this number will always be printed
        // i.e. for the position 2, 3, - 3 will be printed first and given the fact that 3 is not
        // one of the special numbers which do not have an accompanying number in the same board position
        // 2 will also be printed but in the case of 1, it is printed but as it is a special number, a second
        // number is not printed as 1 is the only input number in that given position on the board data structure
        drawInputNumber(g, num2, col, row, false);

        //when num1 doesnt exist
        if (!isSingleRayMarker(num2)) {
            drawInputNumber(g, num1, col, row, true);
        }
    }

    /**
     * draws a given number to the screen in a given position by finding out
     * the position on the screen given col and row indexes
     * @param g Graphics which number will be printed to
     * @param num Number getting displayed
     * @param col Column index on board
     * @param row Row index on board
     * @param isFirst boolean flag to correctly print each number for a given position as one index includes 2 numbers in most cases
     */
    private void drawInputNumber(Graphics g, String num, int col, int row, boolean isFirst) {
        // finding corresponding location on screen given row and col numbers
        double[] location = findScreenLocation(col, row);
        double x = location[0];
        double y = location[1];

        FontMetrics metrics = g.getFontMetrics();
        Rectangle bounds;

        // number to be printed, parsed as it is compared to certain bounds to make sure each
        // number is printed in the correct position on the screen
        int number = Integer.parseInt(num);
        int textHeight = metrics.getHeight();
        int textWidth1 = metrics.stringWidth(num);

        // increasing bounds for numbers less than 10 as they are smaller
        if (number < 10) textWidth1 += 10;
        int xBound1 = (int) x - (textWidth1 / 2) + getXScreenLocation(number, isFirst);
        if (!(number < 10)) xBound1 += 10;
        int yBound1 = (int) y - (textHeight / 2) + getYScreenLocation(number, isFirst) - 5;

        g.drawString(num, (int) x + getXScreenLocation(number, isFirst), (int) y + getYScreenLocation(number, isFirst));
        bounds = new Rectangle(xBound1, yBound1, textWidth1, textHeight);
        guiGameScreen.getNumberAreas().add(new GUIGameScreen.NumberArea(bounds, num, row, col));
    }

    /**
     * Method to print the contents of a hexagon, either atom, circles of influence etc.
     * @param content Object which is the content of the hexagon
     * @param col Index of content in board
     * @param row Index of content in board
     */
    private void drawHexagonContents(Object content, int col, int row) {
        // finding corresponding location on the screen for given row and col
        double[] location = findScreenLocation(col, row);
        double x = location[0];
        double y = location[1];

        // finding the centre of the atom given the position on the screen
        double[] centre = findHexCentre(x, y);
        double centreX = centre[0];
        double centreY = centre[1];

        if (content instanceof Atom) {
            drawAtom(centreX, centreY);
        }
        else if (content instanceof Board.RayTrails rayTrails) {
            drawRayTrails(rayTrails, centreX, centreY);
        }
        else if (content instanceof CircleOfInfluence c) {
            drawCircleOfInfluence(c, centreX, centreY);
        }
        else if (content instanceof IntersectingCircleOfInfluence is) {
            for(int i = 0; i < is.getCircleOfInfluences().size(); i++){
                drawCircleOfInfluence(is.getCircleOfInfluence(i), centreX, centreY);
            }
        }
    }

    /**
     * Method to trace out hexagon graphic to the screen
     * Also uses paths to handle registering clicks to the screen
     * @param row Row index of hexagon
     * @param col Column index of hexagon
     */
    private void drawHexagon(int row, int col) {
        // find relative location on the screen for the hexagon
        double[] location = findScreenLocation(col, row);
        double x = location[0];
        double y = location[1];

        Path2D path = new Path2D.Double();

        double width = 2 * SIDE;
        double startX = 100 + x + width / 2;
        double startY = 100 + y;

        g2.setColor(new Color(255, 196, 0));
        g2.setStroke(new BasicStroke(4));

        // creating 6 sides of the hexagon
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI / 6 * (i + 0.5);
            int xOff = (int) (Math.cos(angle) * SIDE);
            int yOff = (int) (Math.sin(angle) * SIDE);
            if (i == 0) {
                path.moveTo(startX + xOff, startY + yOff);
            } else {
                path.lineTo(startX + xOff, startY + yOff);
            }
        }
        path.closePath();
        g2.draw(path);

        // add drawn path for given hexagon to hexagonPaths with its corresponding indexes
        // relative to the actual board data structure
        guiGameScreen.getHexagonPaths().add(new GUIGameScreen.HexagonPath(path, row, col));
    }

    /**
     * Method handles scaling and position the atom correctly on the screen
     * given the position in which it should be printed
     * @param centerX The centre of the hexagon X coordinate the atom is being printed in
     * @param centerY The centre of the hexagon Y coordinate the atom is being printed in
     */
    private void drawAtom(double centerX, double centerY) {
        double scale = 0.049;

        int imageWidth = (int) (images.atomImage.getWidth() * scale);
        int imageHeight = (int) (images.atomImage.getHeight() * scale);

        int imageX = (int) (centerX - imageWidth / 2);
        int imageY = (int) (centerY - imageHeight / 2);

        Image scaledImage = images.atomImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);

        g2.drawImage(scaledImage, imageX + 10, imageY + 7, guiGameScreen);
    }

    /**
     * Method which cycles through the boards placed ray markers and draws them all to the screen
     * by finding the corresponding area in which it should be printed to
     */
    private void drawRayMarkers() {
        for (GUIGameScreen.NumberArea n : guiGameScreen.getNumberAreas()) {
            int num = Integer.parseInt(n.number);
            if (guiGameScreen.getRayMarkers() != null) {
                for (RayMarker r : guiGameScreen.getRayMarkers()) {
                    if (r.getNumber() == num) {
                        // draw actual rectangle and draw white boarder around the edge
                        g2.setColor(Color.WHITE);
                        g2.fillRect(n.bounds.x - 3, n.bounds.y - 3, n.bounds.width + 6, n.bounds.height + 6);
                        g2.setColor(r.getGuiColour());
                        g2.fill(n.bounds);
                    }
                }
            }
        }
    }

    /**
     * Method to draw a given circle of influence by calling another method to actually draw the
     * image depending on its orientation
     * @param c Circle of influence being printed
     * @param centreX Centre X coordinate of hexagon being printed to
     * @param centreY Centre Y coordinate of hexagon being printed to
     */
    private void drawCircleOfInfluence(CircleOfInfluence c, double centreX, double centreY){
        // determine orientation of circle of influence and call method to
        // print its corresponding image
        if(c.getOrientation() == 60){
            drawCircleOfInfluenceImage(images.circle60, centreX, centreY);
        }
        else if(c.getOrientation() == 120){
            drawCircleOfInfluenceImage(images.circle120, centreX, centreY);
        }
        else if(c.getOrientation() == 240){
            drawCircleOfInfluenceImage(images.circle240, centreX, centreY);
        }
        else if(c.getOrientation() == 300){
            drawCircleOfInfluenceImage(images.circle300, centreX, centreY);
        }
        else if(c.getOrientation() == 90){
            drawCircleOfInfluenceImage(images.circle90, centreX + 4, centreY);
        }
        else if(c.getOrientation() == 270){
            drawCircleOfInfluenceImage(images.circle270, centreX - 7, centreY);
        }
    }

    /**
     * Drawing of actual circle of influence which uses the passed in image to print
     * @param img Image of specific circle of influence
     * @param centreX Centre X coordinate of hexagon being printed to
     * @param centreY Centre Y coordinate of hexagon being printed to
     */
    private void drawCircleOfInfluenceImage(BufferedImage img, double centreX, double centreY){
        double scale = 0.3;
        if (img == images.circle90) scale *= 1.1;

        int imageWidth = (int) (img.getWidth() * scale);
        int imageHeight = (int) (img.getHeight() * scale);

        int imageX = (int) (centreX - imageWidth / 2);
        int imageY = (int) (centreY - imageHeight / 2);

        Image scaledImage = img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        g2.drawImage(scaledImage, imageX + 10, imageY + 7, guiGameScreen);
    }

    /**
     * Method which will draw all ray trails which passed through a certain hexagon
     * @param rayTrails An object containing a list of singular ray trails
     * @param centreX Centre X coordinate of hexagon being printed to
     * @param centreY Centre Y coordinate of hexagon being printed to
     */
    private void drawRayTrails(Board.RayTrails rayTrails, double centreX, double centreY) {
        for (Board.RayTrail r : rayTrails.getRayTrails()) {
            drawRayTrail(r.getOrientation(), centreX, centreY);
        }
    }

    /**
     * Drawing a specific ray trail graphic to the screen by used of calling a method
     * and passing correct image to the method
     * @param orientation orientation of the ray when it passed through a given hexagon
     * @param centreX Centre X coordinate of hexagon being printed to
     * @param centreY Centre Y coordinate of hexagon being printed to
     */
    private void drawRayTrail(int orientation, double centreX, double centreY) {
        if(orientation == 60 || orientation == 240) {
            drawRayTrailImage(images.ray60, centreX, centreY);
        }
        else if(orientation == 0 || orientation == 180) {
            drawRayTrailImage(images.ray0, centreX, centreY);
        }
        else {
            drawRayTrailImage(images.ray120, centreX, centreY);
        }
    }

    /**
     * Method to actually draw the ray trail onto the screen
     * @param img Image being printed, specific ray trail
     * @param centreX Centre X coordinate of hexagon being printed to
     * @param centreY Centre Y coordinate of hexagon being printed to
     */
    private void drawRayTrailImage(BufferedImage img, double centreX, double centreY) {
        double scale = 0.63;
        if (img == images.ray0) scale = 0.25;

        int imageWidth = (int) (images.ray120.getWidth() * scale);
        int imageHeight = (int) (images.ray120.getHeight() * scale);

        int imageX = (int) (centreX - imageWidth / 2);
        int imageY = (int) (centreY - imageHeight / 2);

        // change x and y coordinate on the screen accordingly to
        // account for different image formats
        if (img == images.ray60)  {
            imageX -= 17;
            imageY += 62;
        }
        else if (img == images.ray0) {
            imageX += 10;
            imageY += 7;
        }
        else {
            imageX += 45;
            imageY += 60;
        }

        Image scaledImage = img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        g2.drawImage(scaledImage, imageX, imageY, guiGameScreen);
    }

    /**
     * Method to just draw main background to the screen
     * @param g2 Graphics the background is printed to
     */
    protected void drawBackground(Graphics2D g2) {
        Image scaledBackground = images.background.getScaledInstance(guiGameScreen.getWidth(), guiGameScreen.getHeight(), Image.SCALE_SMOOTH);
        g2.drawImage(scaledBackground, 0, 0, guiGameScreen);
    }

    /**
     * Helper method to translate row and col variables to positions on the screen
     * @param col Column index
     * @param row Row index
     * @return an array consisting of 2 values, an x and y coordinate
     */
    private double[] findScreenLocation(int col, int row) {
        double x = ((((col * 1.155) * 2 * SIDE * 3) / 4) + (row * (2 * SIDE * 0.8)) - (row * 30)) - 250;
        double y = ((row * 1.16) * Math.sqrt(3) * SIDE * 3) / 4;
        return new double[]{x, y};
    }

    /**
     * Help method to find the centre of the hexagon given a position on the screen
     * @param x X coordinate on the screen
     * @param y Y coordinate on the screen
     * @return an array consisting of 2 values, an x and y coordinate corresponding to centre of hexagon
     */
    private double[] findHexCentre(double x, double y) {
        double centerX = (100 + x + (double) 2 * SIDE / 2) - 10;
        double centerY = (100 + y + Math.sqrt(3) * SIDE / 2) - 40;
        return new double[]{centerX, centerY};
    }

    /**
     * Method to return whether a given number which is printed around the outside of
     * the board is the special case of only that number being printed into
     * the corresponding position on board
     * @param num2 Number being compared
     * @return true is number is the only number to be printed in a given hexagon position
     */
    private boolean isSingleRayMarker(String num2) {
        return (num2.equals("1") || num2.equals("10") || num2.equals("19") ||
                num2.equals("28") || num2.equals("37") || num2.equals("46"));
    }

    /**
     * Returns the number need to be added to the screen position X to
     * correctly print that number to the screen
     * @param number Number to be printed
     * @param isFirst whether the number is printed first or second in a given hexagon
     * @return the integer to be added to X value of screen location
     */
    private int getXScreenLocation(int number, boolean isFirst) {
        if (isFirst) {
            if (number < 10) return 155;
            else if (number < 19) return 140;
            else if (number < 28) return 120;
            else if (number < 37) return 111;
            else if (number < 46) return 125;
            else return 140;
        }
        else {
            if (number < 10) return 147;
            else if (number < 19) return 150;
            else if (number < 28) return 143;
            else if (number < 37) return 125;
            else if (number < 46) return 110;
            else return 120;
        }
    }

    /**
     * Returns the number need to be added to the screen position Y to
     * correctly print that number to the screen
     * @param number Number to be printed
     * @param isFirst whether the number is printed first or second in a given hexagon
     * @return the integer to be added to Y value of screen location
     */
    private int getYScreenLocation(int number, boolean isFirst) {
        if (isFirst) {
            if (number < 10) return 103;
            else if (number < 19) return 90;
            else if (number < 28) return 90;
            else if (number < 37) return 105;
            else if (number < 46) return 125;
            else return 125;
        }
        else {
            if (number < 10) return 125;
            else if (number < 19) return 110;
            else if (number < 28) return 90;
            else if (number < 37) return 85;
            else if (number < 46) return 102;
            else return 125;
        }
    }
}
