import java.util.ArrayList;

public class Atom {

    private int xCo_ord;
    private int yCo_ord;

    ArrayList<CircleOfInfluence> circleOfInfluence = new ArrayList<>();

    public Atom(int xCo_ord, int yCo_ord){

    //ignore, keeping in case needed in future and cba figuring it all out again
        //condition for atom which has full circle of influence
//        if(xCo_ord != 1 && xCo_ord != 9 && yCo_ord != 1 && yCo_ord != 9
//        && xCo_ord + yCo_ord != 14 && xCo_ord + yCo_ord != 6){

            circleOfInfluence.add(new CircleOfInfluence(xCo_ord - 1, yCo_ord, 90));
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord, yCo_ord - 1, 45));
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord + 1, yCo_ord - 1, 135));
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord + 1, yCo_ord, 90));
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord, yCo_ord + 1, 45));
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord - 1, yCo_ord + 1, 135));


    }

}
