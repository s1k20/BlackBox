package view.GUIBoard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class GameImages {

    // all images used in the game
    protected BufferedImage atomImage;
    protected BufferedImage background;
    protected BufferedImage circle60;
    protected BufferedImage circle120;
    protected BufferedImage circle240;
    protected BufferedImage circle300;
    protected BufferedImage circle90;
    protected BufferedImage circle270;
    protected BufferedImage ray60;
    protected BufferedImage ray120;
    protected BufferedImage ray0;

    public GameImages() {
        loadImages();
    }

    private void loadImages() {
        // attempt to load all images to set instance variables
        try {
            atomImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/atom.svg.png")));
            background = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/background.jpg")));
            circle60 = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/60CircleOfInfluence.png")));
            circle120 = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/120CircleOfInfluence.png")));
            circle90 = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/90CircleOfInfluence.png")));
            circle270 = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/270CircleOfInfluence.png")));
            circle300 = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/300CircleOfInfluence.png")));
            circle240 = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/240CircleOfInfluence.png")));
            ray0 = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/straight_line_thick.png")));
            ray60 = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/60_degree_line_thick.png")));
            ray120 = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/120_degree_line_thick.png")));

        }catch (Exception e) {
            System.err.println("Failed to load images: " + e.getMessage()); // log error
        }
    }
}
