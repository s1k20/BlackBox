package game_tests.controller;

import controller.Game;
import model.*;
import org.junit.jupiter.api.Test;
import view.GUIBoard.GUIGameScreen;

import static org.junit.jupiter.api.Assertions.*;


public class TestController {

    @Test
    void testOnAtomPlace() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);

        gb.getListener().onAtomPlaced(4, 5);

        assertTrue(g.getBoard().getBoardPosition(4,5) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(4,6) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(3,6) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(5,5) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(3,5) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(4,4) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(5,4) instanceof CircleOfInfluence);
    }

//    @Test
//    void testOnSendRay() {
//        Game g = new Game();
//        GUIGameBoard gb = new GUIGameBoard(g);
//
//        gb.listener.onRaySent(10);
//
//        Ray ray = g.getBoard().getSentRays().get(0);
//        assertEquals(ray.getInput(), 2);
//        assertEquals(ray.getOutput(), 45);
//    }
}
