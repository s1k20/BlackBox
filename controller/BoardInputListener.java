package controller;

public interface BoardInputListener {
    void onAtomPlaced(int col, int row);
    void onAtomRemoved(int col, int row);
    void onRaySent(int number);
    void onFinishRays();
    void onAtomGuess(int col, int row);

}
