public class RayMarker {

    private final int xCo_ord;
    private final int yCo_ord;

    private final String colour;

    public RayMarker(int x, int y, String colour){
        this.xCo_ord = x;
        this.yCo_ord = y;
        this.colour = colour;
    }

    public int getXCo_ord(){
        return this.xCo_ord;
    }

    public int getYCo_ord(){
        return this.yCo_ord;
    }

    public String getColour(){
        return this.colour;
    }

}
