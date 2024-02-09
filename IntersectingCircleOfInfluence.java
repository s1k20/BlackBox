import java.util.ArrayList;

public class IntersectingCircleOfInfluence {
    private ArrayList<CircleOfInfluence> circleOfInfluences;

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
