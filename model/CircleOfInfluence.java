package model;

public class CircleOfInfluence {

    //variables to track a single piece of circle of influences locations and orientation,
    //orientation important for how it looks but also for when we incorporate rays
    private final int xCo_ord;
    private final int yCo_ord;
    private final int orientation;

    //constructor to create a new circle of influence
    public CircleOfInfluence(int xCo_ord, int yCo_ord, int orientation){
        this.xCo_ord = xCo_ord;
        this.yCo_ord = yCo_ord;
        this.orientation = orientation;
    }

    //accessors for circle of influence attributes
    public int getXCo_ord(){
        return this.xCo_ord;
    }

    public int getYCo_ord(){
        return this.yCo_ord;
    }

    public int getOrientation(){
        return this.orientation;
    }
}
