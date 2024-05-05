package model;

import java.util.ArrayList;
public class Atom extends HexagonPosition {
    //array list of an atoms circle of influences
    //which are broken up into individual hexagon positions
    private final ArrayList<CircleOfInfluence> circleOfInfluence = new ArrayList<>();

    public Atom(int xCo_ord, int yCo_ord){
        super(xCo_ord, yCo_ord);
        addCircleOfInfluence(xCo_ord, yCo_ord);
    }

    private void addCircleOfInfluence(int xCo_ord, int yCo_ord) {
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord, yCo_ord - 1, 60));
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord, yCo_ord + 1, 240));
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord + 1, yCo_ord, 270));
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord - 1, yCo_ord, 90));
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord - 1, yCo_ord + 1, 300));
        circleOfInfluence.add(new CircleOfInfluence(xCo_ord + 1, yCo_ord - 1, 120));
    }

    public ArrayList<CircleOfInfluence> getCircleOfInfluence(){
        return this.circleOfInfluence;
    }
}
