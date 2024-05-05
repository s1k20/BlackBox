package model;

import java.awt.*;

import static model.BoardConstants.*;
import static model.BoardConstants.ANSI_RED;

/**
 * Class which represents a ray marker on the board
 */
public class RayMarker extends HexagonPosition {
    private final int number;
    // contains both a gui and ansi colour for printing
    // to GUI and TUI (for testing purposes) respectively
    private String colour;
    private Color guiColour;

    public RayMarker(int x, int y, int number){
        super(x, y);
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public String getColour(){
        return this.colour;
    }

    public Color getGuiColour() {
        return this.guiColour;
    }

    // sets the rays colour given an int deflection type belonging to ray
    public void setColour(int deflectionType){
        // sets both ray markers guiColour and ANSI colour
        this.colour = getDeflectionANSI_Color(deflectionType);
        this.guiColour = getDeflectionColor(deflectionType);
    }

    private Color getDeflectionColor(int deflection) {
        if (deflection == -1) {
            return Color.green;
        }
        else if (deflection == 180) {
            return PINK;
        }
        else if(deflection == 120){
            return YELLOW;
        }
        else if(deflection == 60){
            return BLUE;
        }
        else {
            return RED;
        }
    }

    private String getDeflectionANSI_Color(int deflection) {
        if (deflection == -1) {
            return ANSI_GREEN;
        }
        else if (deflection == 180) {
            return ANSI_PINK;
        }
        else if(deflection == 120){
            return ANSI_YELLOW;
        }
        else if(deflection == 60){
            return ANSI_BLUE;
        }
        else {
            return ANSI_RED;
        }
    }

}
