package model;

public class RayOutputPoint{
    int x, y;
    boolean isFirst;

    public RayOutputPoint(int x, int y, boolean isFirst){
        this.x = x;
        this.y = y;
        this.isFirst = isFirst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RayOutputPoint myPoint = (RayOutputPoint) o;
        return x == myPoint.x && y == myPoint.y && isFirst == myPoint.isFirst;
    }

    @Override
    public int hashCode() {
        int result = 17; // Start with a non-zero constant prime number.
        result = 31 * result + x; // Multiply by a prime number before adding the next field.
        result = 31 * result + y;
        result = 31 * result + (isFirst ? 1 : 0); // Convert boolean to an int (1 for true, 0 for false).
        return result;
    }

}
