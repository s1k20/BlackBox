package model;

import java.util.ArrayList;

public class IntersectingCircleOfInfluence {

    //TODO change this so that it also contains atoms which might be intersected
    //TODO but will only show atom if atom is contained in array list
    //TODO  (more so for gui)
    private final ArrayList<CircleOfInfluence> circleOfInfluences;
    private final ArrayList<Integer> orientations;

    public IntersectingCircleOfInfluence() {
        this.circleOfInfluences = new ArrayList<>();
        this.orientations = new ArrayList<>();
    }

    public void addPart(CircleOfInfluence c){
        circleOfInfluences.add(c);
        orientations.add(c.getOrientation());
    }

    public void removePart(int orientation) {
        circleOfInfluences.removeIf(c -> c.getOrientation() == orientation);
    }

    public ArrayList<Integer> getOrientations(){
        return this.orientations;
    }

    private boolean containsOrientation(int orientation){
        for(CircleOfInfluence c : circleOfInfluences){
            if(c.getOrientation() == orientation){
                return true;
            }
        }
        return false;
    }

    public boolean horizontalReflection(){
        for(int i = 0; i < circleOfInfluences.size() - 1; i++){
            for(int j = i + 1; j < circleOfInfluences.size(); j++){
                if(circleOfInfluences.get(i).getOrientation() + circleOfInfluences.get(j).getOrientation() == 360){
                    return true;
                }
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
