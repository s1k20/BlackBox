package model;

import java.util.ArrayList;
import java.util.HashSet;

public class IntersectingCircleOfInfluence extends HexagonPosition {

    private final ArrayList<CircleOfInfluence> circleOfInfluences;
    private final HashSet<Integer> orientations;

    public IntersectingCircleOfInfluence(int xCo_ord, int yCo_ord) {
        super(xCo_ord, yCo_ord);
        this.circleOfInfluences = new ArrayList<>();
        this.orientations = new HashSet<>();
    }

    public void addPart(CircleOfInfluence c){
        circleOfInfluences.add(c);
        orientations.add(c.getOrientation());
    }

    public void removePart(int orientation) {
        circleOfInfluences.removeIf(c -> c.getOrientation() == orientation);
        orientations.remove(orientation);
    }

    public HashSet<Integer> getOrientations(){
        return this.orientations;
    }

    public boolean horizontalReflection(){
        for (Integer orientation : orientations) {
            if (orientations.contains(360 - orientation)) {
                return true;
            }
        }
        return false;
    }

    public boolean diagonalReflection(){
        return (orientations.contains(270) && (orientations.contains(300) || orientations.contains(60)))
                || (orientations.contains(90) && (orientations.contains(240) || orientations.contains(120)));
    }

    public CircleOfInfluence getCircleOfInfluence(int i){
        return circleOfInfluences.get(i);
    }

    public ArrayList<CircleOfInfluence> getCircleOfInfluences(){
        return this.circleOfInfluences;
    }

}
