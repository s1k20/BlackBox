package game_tests.view;

import controller.Game;
import model.Board;
import view.TUIBoard;

public class TestGUIBoard {

    static void testGUIDisplay1() {
        Game g = new Game();
        Board b = g.getBoard();
        TUIBoard t = new TUIBoard(b);

        g.getPlayerManager().setupPlayer("Test1", 1);
        g.getPlayerManager().setupPlayer("Test2", 2);

        b.placeAtom(4,3);
        b.placeAtom(5,4);
        b.placeAtom(6,4);
        b.placeAtom(3,5);
        b.placeAtom(4,5);
        b.placeAtom(9,4);

        b.sendRay(8);
        b.sendRay(2);
        b.sendRay(10);
        b.sendRay(41);
        b.sendRay(32);
        b.sendRay(28);
        b.sendRay(17);

        g.guiView.showBoard("Test", "Test", 0);
        t.printEntireBoard();
    }

    static void testGUIDisplay2() {
        Game g = new Game();
        Board b = g.getBoard();
        TUIBoard t = new TUIBoard(b);

        g.getPlayerManager().setupPlayer("Test1", 1);
        g.getPlayerManager().setupPlayer("Test2", 2);

        b.placeAtom(5,3);
        b.placeAtom(7,3);
        b.placeAtom(6,5);
        b.placeAtom(7,5);
        b.placeAtom(2,8);
        b.placeAtom(4,9);

        b.sendRay(48);
        b.sendRay(14);
        b.sendRay(30);

        g.guiView.showBoard("Test", "Test", 0);
        t.printEntireBoard();
    }

    public static void main(String[] args) {
        testGUIDisplay1();
//        testGUIDisplay2();
    }
}
