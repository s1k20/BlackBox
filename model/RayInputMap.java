package model;

public class RayInputMap {
    int x;
    int y;
    int orientation;

    public boolean equals(RayInputMap r){
        return this.x == r.x && this.y == r.y && (this.orientation == r.orientation ||
                (this.orientation == r.orientation + 180) || (this.orientation == r.orientation - 180));
    }
}
