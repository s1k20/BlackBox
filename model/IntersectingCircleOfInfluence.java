package model;

import java.util.ArrayList;

public class IntersectingCircleOfInfluence {

    //TODO change this so that it also contains atoms which might be intersected
    //TODO but will only show atom if atom is contained in array list
    //TODO  (more so for gui)

    //array list of pieces of circle of influence which intersect each other
    //all put into one array list so that we can track what parts are intersect each other
    private final ArrayList<CircleOfInfluence> circleOfInfluences;

    //constructor to just create arraylist object
    public IntersectingCircleOfInfluence() {
        this.circleOfInfluences = new ArrayList<>();
    }

    //mutator which will add a new circle of influence to the array list
    public void addPart(CircleOfInfluence c){
        circleOfInfluences.add(c);
    }

    //accessor which can return individual piece of intersecting circle of influences
    public CircleOfInfluence getCircleOfInfluence(int i){
        return circleOfInfluences.get(i);
    }

    public ArrayList<CircleOfInfluence> getCircleOfInfluences(){
        return this.circleOfInfluences;
    }
}
