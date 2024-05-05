package model;

import java.awt.*;

public class BoardConstants {
    //board is 9x9 but + 2 for allowing for ray marker positions
    public static final int WIDTH = 11;
    public static final int HEIGHT = 11;

    //ANSI colours
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PINK = "\u001B[38;5;206m";

    //Swing colours
    public static final Color PINK = new Color(191, 0, 255);
    public static final Color YELLOW = new Color(255, 234, 0);
    public static final Color BLUE = new Color(0, 13, 255);
    public static final Color RED = new Color(255, 0, 0);
}
