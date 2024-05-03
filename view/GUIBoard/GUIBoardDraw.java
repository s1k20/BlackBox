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

    public GUIBoardDraw(GUIGameScreen guiGameScreen) {
        this.guiGameScreen = guiGameScreen;
        this.images = new GameImages();
    }

    protected void drawBoard(Graphics g, Board currentBoard) {
        Graphics2D g2 = (Graphics2D) g;
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (!(currentBoard.getBoardPosition(col, row) instanceof Board.NullHex)) {
                    if(currentBoard.getBoardPosition(col, row) instanceof Board.EmptyMarker || currentBoard.getBoardPosition(col, row) instanceof RayMarker){
                        drawInputNumbers(g, col, row, currentBoard);
                    }
                    else {
                        drawHexagon(g2, row, col);
                        if (guiGameScreen.isBoardVisible()) drawHexagonContents(g2, currentBoard.getBoardPosition(col, row), col, row);
                    }
                }
            }
        }
        drawRayMarkers(g2);
    }

    private void drawInputNumbers(Graphics g, int col, int row, Board currentBoard) {
        g.setFont(new Font("Roboto", Font.BOLD, 16));
        g.setColor(Color.white);

        // isFirst refers to which number comes first in a given hexagon.
        // Hexagon contains 2, 3 then 2 isFirst == true or hexagon contains 35, 36 then 35 isFirst == true
        String num2 = String.valueOf(currentBoard.numberOut.get(new RayOutputPoint(col, row, false)));
        String num1 = String.valueOf(currentBoard.numberOut.get(new RayOutputPoint(col, row, true)));

        drawInputNumber(g, num2, col, row, false);

        //when num1 doesnt exist
        if (!isSingleRayMarker(num2)) {
            drawInputNumber(g, num1, col, row, true);
        }
    }

    private void drawInputNumber(Graphics g, String num1, int col, int row, boolean isFirst) {
        double[] location = findScreenLocation(col, row);
        double x = location[0];
        double y = location[1];

        FontMetrics metrics = g.getFontMetrics();
        Rectangle bounds;

        int number1 = Integer.parseInt(num1);
        int textHeight = metrics.getHeight();
        int textWidth1 = metrics.stringWidth(num1);

        if (number1 < 10) textWidth1 += 10;
        int xBound1 = (int) x - (textWidth1 / 2) + getXScreenLocation(number1, isFirst);

        if (!(number1 < 10)) xBound1 += 10;
        int yBound1 = (int) y - (textHeight / 2) + getYScreenLocation(number1, isFirst) - 5;

        g.drawString(num1, (int) x + getXScreenLocation(number1, isFirst), (int) y + getYScreenLocation(number1, isFirst));
        bounds = new Rectangle(xBound1, yBound1, textWidth1, textHeight);
        guiGameScreen.getNumberAreas().add(new GUIGameScreen.NumberArea(bounds, num1, row, col));
    }

    private void drawHexagonContents(Graphics2D g2, Object content, int col, int row) {
        double[] location = findScreenLocation(col, row);
        double x = location[0];
        double y = location[1];

        double[] centre = findHexCentre(x, y);
        double centreX = centre[0];
        double centreY = centre[1];

        if (content instanceof Atom) {
            drawAtom(g2, centreX, centreY);
        }
        else if (content instanceof Board.RayTrails rayTrails) {
            drawRayTrails(g2, rayTrails, centreX, centreY);
        }
        else if (content instanceof CircleOfInfluence c) {
            drawCircleOfInfluence(c, g2, centreX, centreY);
        }
        else if (content instanceof IntersectingCircleOfInfluence is) {
            for(int i = 0; i < is.getCircleOfInfluences().size(); i++){
                drawCircleOfInfluence(is.getCircleOfInfluence(i), g2, centreX, centreY);
            }
        }
    }

    private void drawHexagon(Graphics2D g2, int row, int col) {
        double[] location = findScreenLocation(col, row);
        double x = location[0];
        double y = location[1];

        double width = 2 * SIDE;
        Path2D path = new Path2D.Double();

        double startX = 100 + x + width / 2;
        double startY = 100 + y;

        g2.setColor(new Color(255, 196, 0));
        g2.setStroke(new BasicStroke(4));

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

        guiGameScreen.getHexagonPaths().add(new GUIGameScreen.HexagonPath(path, row, col));
    }

    private void drawAtom(Graphics2D g2, double centerX, double centerY) {
        double scale = 0.049;

        int imageWidth = (int) (images.atomImage.getWidth() * scale);
        int imageHeight = (int) (images.atomImage.getHeight() * scale);

        int imageX = (int) (centerX - imageWidth / 2);
        int imageY = (int) (centerY - imageHeight / 2);

        Image scaledImage = images.atomImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);

        g2.drawImage(scaledImage, imageX + 10, imageY + 7, guiGameScreen);
    }

    private void drawRayMarkers(Graphics2D g2) {
        for (GUIGameScreen.NumberArea n : guiGameScreen.getNumberAreas()) {
            int num = Integer.parseInt(n.number);
            if (guiGameScreen.getRayMarkers() != null) {
                for (RayMarker r : guiGameScreen.getRayMarkers()) {
                    if (r.getNumber() == num) {
                        g2.setColor(Color.WHITE);
                        g2.fillRect(n.bounds.x - 3, n.bounds.y - 3, n.bounds.width + 6, n.bounds.height + 6);
                        g2.setColor(r.getGuiColour());
                        g2.fill(n.bounds);
                    }
                }
            }
        }
    }

    private void drawCircleOfInfluence(CircleOfInfluence c, Graphics2D g2, double centreX, double centreY){
        if(c.getOrientation() == 60){
            drawCircleOfInfluenceImage(images.circle60, g2, centreX, centreY);
        }
        else if(c.getOrientation() == 120){
            drawCircleOfInfluenceImage(images.circle120, g2, centreX, centreY);
        }
        else if(c.getOrientation() == 240){
            drawCircleOfInfluenceImage(images.circle240, g2, centreX, centreY);
        }
        else if(c.getOrientation() == 300){
            drawCircleOfInfluenceImage(images.circle300, g2, centreX, centreY);
        }
        else if(c.getOrientation() == 90){
            drawCircleOfInfluenceImage(images.circle90, g2, centreX + 4, centreY);
        }
        else if(c.getOrientation() == 270){
            drawCircleOfInfluenceImage(images.circle270, g2, centreX - 7, centreY);
        }
    }

    private void drawCircleOfInfluenceImage(BufferedImage img, Graphics2D g2, double centreX, double centreY){
        double scale = 0.3;
        if (img == images.circle90) scale *= 1.1;

        int imageWidth = (int) (img.getWidth() * scale);
        int imageHeight = (int) (img.getHeight() * scale);

        int imageX = (int) (centreX - imageWidth / 2);
        int imageY = (int) (centreY - imageHeight / 2);

        Image scaledImage = img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        g2.drawImage(scaledImage, imageX + 10, imageY + 7, guiGameScreen);
    }

    protected void drawRayTrails(Graphics2D g2, Board.RayTrails rayTrails, double centerX, double centerY) {
        for (Board.RayTrail r : rayTrails.getRayTrails()) {
            drawRayTrail(g2, r.getOrientation(), centerX, centerY);
        }
    }

    private void drawRayTrail(Graphics2D g2, int orientation, double centreX, double centreY) {
        if(orientation == 60 || orientation == 240) {
            drawRayTrailImage(images.ray60, g2, centreX, centreY);
        }
        else if(orientation == 0 || orientation == 180) {
            drawRayTrailImage(images.ray0, g2, centreX, centreY);
        }
        else {
            drawRayTrailImage(images.ray120, g2, centreX, centreY);
        }
    }

    private void drawRayTrailImage(BufferedImage img, Graphics2D g2, double centreX, double centreY) {
        double scale = 0.63;
        if (img == images.ray0) scale = 0.25;

        int imageWidth = (int) (images.ray120.getWidth() * scale);
        int imageHeight = (int) (images.ray120.getHeight() * scale);

        int imageX = (int) (centreX - imageWidth / 2);
        int imageY = (int) (centreY - imageHeight / 2);

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

    protected void drawBackground(Graphics2D g2) {
        Image scaledBackground = images.background.getScaledInstance(guiGameScreen.getWidth(), guiGameScreen.getHeight(), Image.SCALE_SMOOTH);
        g2.drawImage(scaledBackground, 0, 0, guiGameScreen);
    }

    private double[] findScreenLocation(int col, int row) {
        double x = ((((col * 1.155) * 2 * SIDE * 3) / 4) + (row * (2 * SIDE * 0.8)) - (row * 30)) - 250;
        double y = ((row * 1.16) * Math.sqrt(3) * SIDE * 3) / 4;
        return new double[]{x, y};
    }

    private double[] findHexCentre(double x, double y) {
        double centerX = (100 + x + (double) 2 * SIDE / 2) - 10;
        double centerY = (100 + y + Math.sqrt(3) * SIDE / 2) - 40;
        return new double[]{centerX, centerY};
    }

    private boolean isSingleRayMarker(String num2) {
        return (num2.equals("1") || num2.equals("10") || num2.equals("19") ||
                num2.equals("28") || num2.equals("37") || num2.equals("46"));
    }

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
