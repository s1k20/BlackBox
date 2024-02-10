import java.util.ArrayList;

public class IntersectingCircleOfInfluence {

    //TODO change this so that it also contains atoms which might be intersected
    //TODO but will only show atom if atom is contained in array list
    //TODO  (more so for gui)
    private final ArrayList<CircleOfInfluence> circleOfInfluences;

    public IntersectingCircleOfInfluence() {
        this.circleOfInfluences = new ArrayList<>();
    }

    public void addPart(CircleOfInfluence c){
        circleOfInfluences.add(c);
    }

    public CircleOfInfluence getCircleOfInfluence(int i){
        return circleOfInfluences.get(i);
    }
}
