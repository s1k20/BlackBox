package model;

public class RayOutputPoint {
    int x, y;
    boolean isFirst;

    public RayOutputPoint(int x, int y, boolean isFirst){
        this.x = x;
        this.y = y;
        this.isFirst = isFirst;
    }

    // method to return if two output points are the same
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RayOutputPoint myPoint = (RayOutputPoint) o;
        return x == myPoint.x && y == myPoint.y && isFirst == myPoint.isFirst;
    }

    // hash code created as object is stored in a hashmap
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + (isFirst ? 1 : 0);
        return result;
    }

}
