package model;

import java.awt.*;

public class RayMarker {

    //ray marker co-ords on board
    private final int xCo_ord;
    private final int yCo_ord;

    //string which will hold ansi code for text-based interface to show colour
    //TODO will be developed when working on gui
    private String colour;
    private Color guiColour;

    //constructor to create ray marker
    public RayMarker(int x, int y, String colour, Color guiColour){
        this.xCo_ord = x;
        this.yCo_ord = y;
        this.colour = colour;
        this.guiColour = guiColour;
    }

    //accessors to get private variables
    public int getXCo_ord(){
        return this.xCo_ord;
    }

    public int getYCo_ord(){
        return this.yCo_ord;
    }

    public String getColour(){
        return this.colour;
    }

    public Color getGuiColour() {
        return this.guiColour;
    }

    public void setColour(String colour){
        this.colour = colour;
    }

    public void setGuiColour(Color colour) {
        this.guiColour = colour;
    }

}
