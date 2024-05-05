package game_tests.controller;

import controller.Game;
import model.*;
import org.junit.jupiter.api.Test;
import view.GUIBoard.GUIGameScreen;

import static org.junit.jupiter.api.Assertions.*;


public class TestAtomPlace {

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
    @Test
    void testAtomRemove() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);

        gb.getListener().onAtomPlaced(2, 5);
        gb.getListener().onAtomPlaced(7, 4);
        gb.getListener().onAtomPlaced(3, 9);
        gb.getListener().onAtomPlaced(1, 5);
        gb.getListener().onAtomPlaced(8, 1);
        gb.getListener().onAtomPlaced(7, 3);

        assertTrue(g.getBoard().getBoardPosition(2,5) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(7,4) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(3,9) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(1,5) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(8,1) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(7,3) instanceof Atom);

        gb.getListener().onAtomRemoved(2,5);
        gb.getListener().onAtomRemoved(7,4);
        gb.getListener().onAtomRemoved(3,9);
        gb.getListener().onAtomRemoved(1,5);
        gb.getListener().onAtomRemoved(8,1);
        gb.getListener().onAtomRemoved(7,3);


        assertFalse(g.getBoard().getBoardPosition(2,5) instanceof Atom);
        assertFalse(g.getBoard().getBoardPosition(7,4) instanceof Atom);
        assertFalse(g.getBoard().getBoardPosition(3,9) instanceof Atom);
        assertFalse(g.getBoard().getBoardPosition(1,5) instanceof Atom);
        assertFalse(g.getBoard().getBoardPosition(8,1) instanceof Atom);
        assertFalse(g.getBoard().getBoardPosition(7,3) instanceof Atom);
    }

    @Test
    void testAtomRemove2() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);

        gb.getListener().onAtomPlaced(6, 4);
        gb.getListener().onAtomPlaced(8, 5);

        assertTrue(g.getBoard().getBoardPosition(6,4) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(8,5) instanceof Atom);

        assertTrue(g.getBoard().getBoardPosition(6,5) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(5,4) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(7,3) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(7,6) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(9,4) instanceof CircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(9,5) instanceof CircleOfInfluence);

        gb.getListener().onAtomRemoved(6,4);
        gb.getListener().onAtomRemoved(8,5);

        assertFalse(g.getBoard().getBoardPosition(6,5) instanceof CircleOfInfluence);
        assertFalse(g.getBoard().getBoardPosition(5,4) instanceof CircleOfInfluence);
        assertFalse(g.getBoard().getBoardPosition(7,3) instanceof CircleOfInfluence);
        assertFalse(g.getBoard().getBoardPosition(7,6) instanceof CircleOfInfluence);
        assertFalse(g.getBoard().getBoardPosition(9,4) instanceof CircleOfInfluence);
        assertFalse(g.getBoard().getBoardPosition(9,5) instanceof CircleOfInfluence);
    }

    @Test
    void testAtomRemove3() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);

        gb.getListener().onAtomPlaced(8, 1);
        gb.getListener().onAtomPlaced(8, 3);
        gb.getListener().onAtomPlaced(8, 5);

        assertTrue(g.getBoard().getBoardPosition(8,1) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(8,3) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(8,5) instanceof Atom);

        assertTrue(g.getBoard().getBoardPosition(8,2) instanceof IntersectingCircleOfInfluence);
        assertTrue(g.getBoard().getBoardPosition(8,4) instanceof IntersectingCircleOfInfluence);

        gb.getListener().onAtomRemoved(8,3);

        assertFalse(g.getBoard().getBoardPosition(8,2) instanceof IntersectingCircleOfInfluence);
        assertFalse(g.getBoard().getBoardPosition(8,4) instanceof IntersectingCircleOfInfluence);
    }

    @Test
    void testAtomRemove4() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);

        gb.getListener().onAtomPlaced(2, 8);
        gb.getListener().onAtomPlaced(4, 8);


        assertTrue(g.getBoard().getBoardPosition(2,8) instanceof Atom);
        assertTrue(g.getBoard().getBoardPosition(4,8) instanceof Atom);


        assertTrue(g.getBoard().getBoardPosition(3,8) instanceof IntersectingCircleOfInfluence);

        gb.getListener().onAtomRemoved(4,8);

        assertTrue(g.getBoard().getBoardPosition(3,8) instanceof CircleOfInfluence);
    }
}
