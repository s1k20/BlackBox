package model;

public class Ray {

    private final int input;
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
}

