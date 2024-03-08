package model;

import java.util.ArrayList;
public class Atom {

    //atoms co-ordinates
    private final int xCo_ord;
    private final int yCo_ord;

    //array list of an atoms circle of influences
    //which are broken up into individual hexagon positions
    private final ArrayList<CircleOfInfluence> circleOfInfluence = new ArrayList<>();

    //atom constructor, automatically works out its circle of influence
    public Atom(int xCo_ord, int yCo_ord){

        //adding circle of influence to atom array list of circle of influences
        //creating new circle of influence object
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord, yCo_ord - 1, 60));
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord, yCo_ord + 1, 240));
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord + 1, yCo_ord, 270));
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord - 1, yCo_ord, 90));
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord - 1, yCo_ord + 1, 300));
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord + 1, yCo_ord - 1, 120));

        this.xCo_ord = xCo_ord;
        this.yCo_ord = yCo_ord;
    }

    //accessors for co-ords
    public int getXCo_ord(){
        return this.xCo_ord;
    }

    public int getYCo_ord(){
        return this.yCo_ord;
    }

    public ArrayList<CircleOfInfluence> getCircleOfInfluence(){
        return this.circleOfInfluence;
    }
}
