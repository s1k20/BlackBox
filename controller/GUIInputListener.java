package controller;

public interface GUIInputListener {
    void onAtomPlaced(int col, int row);
    void onAtomRemoved(int col, int row);
    void onRaySent(int number);
    void onAtomGuess(int col, int row);
    void onAtomGuessRemoved(int col, int row);
    void onMainMenuToggle();
    void advanceGameState();
    void refreshBoard();
}
