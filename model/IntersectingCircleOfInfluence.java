package model;

import java.util.ArrayList;
import java.util.HashSet;

public class IntersectingCircleOfInfluence extends HexagonPosition {
    private final ArrayList<CircleOfInfluence> circleOfInfluences;
    private final HashSet<Integer> orientations; //hashset which includes circle of influences orientation for efficiency

    public IntersectingCircleOfInfluence(int xCo_ord, int yCo_ord) {
        super(xCo_ord, yCo_ord);
        this.circleOfInfluences = new ArrayList<>();
        this.orientations = new HashSet<>();
    }

    public CircleOfInfluence getCircleOfInfluence(int i){
        return circleOfInfluences.get(i);
    }

    public ArrayList<CircleOfInfluence> getCircleOfInfluences(){
        return this.circleOfInfluences;
    }

    public HashSet<Integer> getOrientations(){
        return this.orientations;
    }

    public void addPart(CircleOfInfluence c){
        circleOfInfluences.add(c);
        orientations.add(c.getOrientation());
    }

    public void removePart(int orientation) {
        circleOfInfluences.removeIf(c -> c.getOrientation() == orientation);
        orientations.remove(orientation);
    }

    // method to return if a horizontal reflection can take places by comparing orientations in hashset
    public boolean horizontalReflection(){
        for (Integer orientation : orientations) {
            if (orientations.contains(360 - orientation)) {
                return true;
            }
        }
        return false;
    }

    // method to return if a diagonal reflection can take place given whether certain
    // combinations orientations are present in hashset
    public boolean diagonalReflection(){
        return (orientations.contains(270) && (orientations.contains(300) || orientations.contains(60)))
                || (orientations.contains(90) && (orientations.contains(240) || orientations.contains(120)));
    }
}
