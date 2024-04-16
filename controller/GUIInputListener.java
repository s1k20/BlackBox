package controller;

public interface GUIInputListener {
    void onAtomPlaced(int col, int row);
    void onAtomRemoved(int col, int row);
    void onRaySent(int number);
    void onFinishRays();
    void onFinishRound();
    void onAI_endRound();
    void onAtomGuess(int col, int row);
    void onAtomGuessRemoved(int col, int row);
    void onMainMenuToggle();
//    void onSinglePlayer();
//    void onTwoPlayer();
//    void onShowMenu();


}
