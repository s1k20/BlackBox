public class CircleOfInfluence {

    private final int xCo_ord;
    private final int yCo_ord;
    private final int orientation;

    public CircleOfInfluence(int xCo_ord, int yCo_ord, int orientation){
        this.xCo_ord = xCo_ord;
        this.yCo_ord = yCo_ord;
        this.orientation = orientation;
    }

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
