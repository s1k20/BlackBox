package model;

public class Ray {

    private final int input;
    private int output;
    private int currXCo_ord;
    private int currYCo_ord;
    private int orientation;
    private int deflectionType;

    public Ray(int input) {
        this.input = input;
        this.deflectionType = 0;
    }

    public int getInput() {
        return this.input;
    }

    public int getOutput(){
        return this.output;
    }

    public void setOutput(int output){
        this.output = output;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setCurrXCo_ord(int x) {
        this.currXCo_ord = x;
    }

    public int getCurrXCo_ord() {
        return this.currXCo_ord;
    }

    public void setCurrYCo_ord(int y) {
        this.currYCo_ord = y;
    }

    public int getCurrYCo_ord() {
        return this.currYCo_ord;
    }

    public int getDeflectionType() {
        return this.deflectionType;
    }

    public void setDeflectionType(int deflectionType) {
        this.deflectionType = deflectionType;
    }

    public void moveRay() {
        switch (getOrientation()) {
            case 0 -> move0();
            case 60 -> move60();
            case 120 -> move120();
            case 180 -> move180();
            case 240 -> move240();
            case 300 -> move300();
            default -> System.out.println("invalid ray movement");
        }
    }


    public void move0() {
        this.setCurrXCo_ord(this.getCurrXCo_ord() + 1);
    }

    public void move60() {
        this.setCurrXCo_ord(this.getCurrXCo_ord() + 1);
        this.setCurrYCo_ord(this.getCurrYCo_ord() - 1);
    }

    public void move120() {
        this.setCurrYCo_ord(this.getCurrYCo_ord() - 1);
    }

    public void move180() {
        this.setCurrXCo_ord(this.getCurrXCo_ord() - 1);
    }

    public void move240() {
        this.setCurrXCo_ord(this.getCurrXCo_ord() - 1);
        this.setCurrYCo_ord(this.getCurrYCo_ord() + 1);
    }

    public void move300() {
        this.setCurrYCo_ord(this.getCurrYCo_ord() + 1);
    }

    public void flipOrientation() {
        if (this.orientation > 180) this.orientation -= 180;
        else this.orientation += 180;

        if (this.orientation == 360) this.orientation = 0;
        this.setDeflectionType(180);
    }

    public void isReflection(int x, int y, int orientation) {
        if (currXCo_ord == x && currYCo_ord == y && orientation == Math.abs(this.orientation - 180)) {
            this.setDeflectionType(180);
            output = input;
        }
    }

    public boolean deflectionLogic_CircleOfInfluence(int coiOrientation){
        if(isAbsorption(coiOrientation)) return true;
        else if (isEdgeReflection(coiOrientation)) flipOrientation();
        else{
            if(coiOrientation != 270 && coiOrientation != 240 && coiOrientation != 120){
                if(this.getOrientation() == 0 && coiOrientation == 300){
                    this.setOrientation(360);
                }
                if(this.getOrientation() >= coiOrientation){
                    this.setOrientation(this.getOrientation() - 60);
                }
                else{
                    this.setOrientation(this.getOrientation() + 60);
                    if(this.getOrientation() == 360){
                        this.setOrientation(0);
                    }
                }
            }
            else if(coiOrientation == 240){
                if(this.getOrientation() == 60){
                    this.setOrientation(this.getOrientation() - 60);
                }
                else{
                    this.setOrientation(this.getOrientation() + 60);
                }
            }
            else if(coiOrientation == 120){
                if(this.getOrientation() == 180){
                    this.setOrientation(this.getOrientation() - 60);
                }
                else{
                    this.setOrientation(this.getOrientation() + 60);
                    if(this.getOrientation() == 360){
                        this.setOrientation(0);
                    }
                }
            }
            else{
                if(this.getOrientation() == 120){
                    this.setOrientation(this.getOrientation() - 60);
                }
                else{
                    this.setOrientation(this.getOrientation() + 60);
                }
            }
            if(this.getDeflectionType() != 120){
                this.setDeflectionType(60);
            }
        }
        return false;
    }

    private boolean isAbsorption(int coiOrientation) {
        return this.orientation - coiOrientation == 90 || this.orientation - coiOrientation == -90
                || this.orientation + coiOrientation == 360;
    }

    private boolean isEdgeReflection(int coiOrientation) {
        if (this.orientation == coiOrientation) return true;

        return switch (this.orientation) {
            case 0 -> coiOrientation == 120 || coiOrientation == 240;
            case 60, 300 -> coiOrientation == 270;
            case 120, 240 -> coiOrientation == 90;
            case 180 -> coiOrientation == 60 || coiOrientation == 300;
            default -> false;
        };
    }

    private boolean isEdgeReflection_IntersectingCircleOfInfluence(IntersectingCircleOfInfluence intersectingCircleOfInfluence) {
        for (CircleOfInfluence c : intersectingCircleOfInfluence.getCircleOfInfluences()) {
            if (isEdgeReflection(c.getOrientation())) return true;
        }
        return false;
    }

    public boolean deflectionLogic_IntersectingCircleOfInfluence(IntersectingCircleOfInfluence intersectingCircleOfInfluence) {
        if (isEdgeReflection_IntersectingCircleOfInfluence(intersectingCircleOfInfluence)) flipOrientation();

        //if ray is going straight across, conditions for reflecting is that it encounters intersecting
        //circle of influences which are like / and \
        else if (this.getOrientation() == 0 || this.getOrientation() == 180) {
            //horizontal reflection
            //function which checks to make sure conditions of horizontal reflection can take place
            //function just checks if two parts of circle of influence add to 360 which leads to horizontal reflection
            if (intersectingCircleOfInfluence.horizontalReflection()) flipOrientation();

            //horizontal 120
            else {
                if (this.getOrientation() == 180 && intersectingCircleOfInfluence.getOrientations().contains(120)) {
                    minus120();
                } else if (this.getOrientation() == 180 && intersectingCircleOfInfluence.getOrientations().contains(240)) {
                    plus120();
                } else if (this.getOrientation() == 0 && intersectingCircleOfInfluence.getOrientations().contains(60)) {
                    plus120();
                } else if (this.getOrientation() == 0 && intersectingCircleOfInfluence.getOrientations().contains(300)) {
                    minus120();
                }
                this.setDeflectionType(120);
            }
        }
        //if ray is going NOT straight across, conditions for reflecting is that it encounters intersecting
        //circle of influences which are like / and | or \ and |
        else {
            //diagonal reflection
            //just checking if either circle of influence is straight like "|"
            if (intersectingCircleOfInfluence.diagonalReflection()) flipOrientation();
            //diagonal 120
            else {
                if (!intersectingCircleOfInfluence.getOrientations().contains(90) && !intersectingCircleOfInfluence.getOrientations().contains(270)) {

                    if (this.getOrientation() == intersectingCircleOfInfluence.getCircleOfInfluence(0).getOrientation() + 180 ||
                            this.getOrientation() == intersectingCircleOfInfluence.getCircleOfInfluence(0).getOrientation() - 180) {
                        this.setOrientation(intersectingCircleOfInfluence.getCircleOfInfluence(1).getOrientation());
                    } else {
                        this.setOrientation(intersectingCircleOfInfluence.getCircleOfInfluence(0).getOrientation());
                    }

                } else {
                    if (this.getOrientation() == 120) {
                        minus120();
                    } else if (this.getOrientation() == 60) {
                        plus120();
                    } else if (this.getOrientation() == 300) {
                        minus120();
                    } else if (this.getOrientation() == 240) {
                        plus120();
                    }
                }
                this.setDeflectionType(120);
            }
        }
        return false;
    }

    private void minus120() {
        if (this.orientation < 120) {
            this.orientation += 360;
        }
        this.orientation -= 120;
    }

    private void plus120() {
        if (this.orientation >= 360 - 120) {
            this.orientation -= 360;
        }
        this.orientation += 120;
    }
}

