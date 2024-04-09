package view;

import controller.Game;

public class GUIView {
    public GUIGameBoard boardScreen;

    public GUIView(Game game) {
        boardScreen = new GUIGameBoard(game);
    }

}
