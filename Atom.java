import java.util.ArrayList;
public class Atom {

    private final int xCo_ord;
    private final int yCo_ord;

    ArrayList<CircleOfInfluence> circleOfInfluence = new ArrayList<>();

    public Atom(int xCo_ord, int yCo_ord){

        validateInput();

    //ignore, keeping in case needed in future and cba figuring it all out again
        //condition for atom which has full circle of influence
//        if(xCo_ord != 1 && xCo_ord != 9 && yCo_ord != 1 && yCo_ord != 9
//        && xCo_ord + yCo_ord != 14 && xCo_ord + yCo_ord != 6){

            //adding circle of influence to atom array list of circle of influences
            //creating new circle of influence object
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord, yCo_ord - 1, 45));
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord, yCo_ord + 1, 45));
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord + 1, yCo_ord, 90));
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord - 1, yCo_ord, 90));
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord - 1, yCo_ord + 1, 135));
            circleOfInfluence.add(new CircleOfInfluence(xCo_ord + 1, yCo_ord - 1, 135));

            this.xCo_ord = xCo_ord;
            this.yCo_ord = yCo_ord;
    }

    public int getXCo_ord(){
        return this.xCo_ord;
    }

    public int getYCo_ord(){
        return this.yCo_ord;
    }

    public boolean validateInput() {
        if((xCo_ord == 1 && (xCo_ord < 5||xCo_ord >10))  || (yCo_ord==2&&(xCo_ord<4||xCo_ord>9))
                || (yCo_ord==3&&(xCo_ord<3||xCo_ord>9))   || (yCo_ord==4&&(xCo_ord<2||xCo_ord>9))
                || (yCo_ord==5&&(xCo_ord<1||xCo_ord>9))   || (yCo_ord==6&&(xCo_ord<1||xCo_ord>8))
                || (yCo_ord==7&&(xCo_ord<1||xCo_ord>7))   || (yCo_ord==8&&(xCo_ord<1||xCo_ord>6))
                || (yCo_ord==9&&(xCo_ord<1||xCo_ord>5))) {
            return true;
        }
        else return false;
    }

}
