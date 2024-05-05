package model;

public class CircleOfInfluence extends HexagonPosition {

    //variables to track a single piece of circle of influences locations and orientation,
    //orientation important for how it looks but also for when we incorporate rays
    private final int orientation;

    //constructor to create a new circle of influence
    public CircleOfInfluence(int xCo_ord, int yCo_ord, int orientation){
        super(xCo_ord, yCo_ord);
        this.orientation = orientation;
    }

    // returns the circle of influences orientation
    public int getOrientation(){
        return this.orientation;
    }
}
