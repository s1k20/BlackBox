package model;

/**
 * Class which is inherited by other classes which are
 * objects which can be placed onto the board
 */
public class HexagonPosition {

    private final int xCo_ord;
    private final int yCo_ord;

    public HexagonPosition(int xCo_ord, int yCo_ord) {
        this.xCo_ord = xCo_ord;
        this.yCo_ord = yCo_ord;
    }

    public int getXCo_ord(){
        return this.xCo_ord;
    }

    public int getYCo_ord(){
        return this.yCo_ord;
    }

}
