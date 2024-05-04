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

    @Test
    void testOnAtomPlace2() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);

        gb.getListener().onAtomPlaced(8, 3);

        assertTrue(g.getBoard().getBoardPosition(8,3) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(7,4) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(7,3) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(8,2) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(8,4) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(9,2) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(9,3) instanceof CircleOfInfluence);
    }

    @Test
    void testOnAtomPlace3() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);

        gb.getListener().onAtomPlaced(2, 5);

        assertTrue(g.getBoard().getBoardPosition(2,5) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(3,4) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(3,5) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(2,4) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(2,6) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(1,5) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(1,6) instanceof CircleOfInfluence);
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
