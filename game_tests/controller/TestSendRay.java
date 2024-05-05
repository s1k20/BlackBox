package game_tests.controller;

import controller.Game;
import controller.GameState;
import model.Ray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import view.GUIBoard.GUIGameScreen;

public class TestSendRay {
    @Test
    void testOnSendRay() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);
        g.getStateManager().setCurrentState(GameState.GUESSING_ATOMS);
        g.getPlayerManager().setupPlayer("Test P1", 1);
        g.getPlayerManager().setupPlayer("Test P2", 2);

        gb.getListener().onRaySent(10);
        gb.getListener().onRaySent(19);
        gb.getListener().onRaySent(45);


        Ray ray = g.getBoard().getSentRays().get(0);
        Ray ray2 = g.getBoard().getSentRays().get(1);
        Ray ray3 = g.getBoard().getSentRays().get(2);

        assertEquals(ray.getInput(), 10);
        assertEquals(ray.getOutput(), 37);

        assertEquals(ray2.getInput(), 19);
        assertEquals(ray2.getOutput(), 46);

        assertEquals(ray3.getInput(), 45);
        assertEquals(ray3.getOutput(), 2);
    }

    @Test
    void testOnSendRay2() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);
        g.getStateManager().setCurrentState(GameState.SETTING_ATOMS);
        gb.getListener().onAtomPlaced(6,4);

        g.getStateManager().setCurrentState(GameState.GUESSING_ATOMS);
        g.getPlayerManager().setupPlayer("Test P1", 1);
        g.getPlayerManager().setupPlayer("Test P2", 2);


        gb.getListener().onRaySent(32);
        gb.getListener().onRaySent(17);

        Ray ray = g.getBoard().getSentRays().get(0);
        Ray ray2 = g.getBoard().getSentRays().get(1);


        assertEquals(ray.getInput(), 32);
        assertEquals(ray.getOutput(), 44);
        assertEquals(ray.getDeflectionType(),60);

        assertEquals(ray2.getInput(), 17);
        assertEquals(ray2.getOutput(), 1);
        assertEquals(ray2.getDeflectionType(),60);
    }

    @Test
    void testOnSendRay3() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);
        g.getStateManager().setCurrentState(GameState.SETTING_ATOMS);

        gb.getListener().onAtomPlaced(3,5);
        gb.getListener().onAtomPlaced(4,5);
        gb.getListener().onAtomPlaced(5,4);
        gb.getListener().onAtomPlaced(6,4);
        gb.getListener().onAtomPlaced(6,6);
        gb.getListener().onAtomPlaced(7,6);

        g.getStateManager().setCurrentState(GameState.GUESSING_ATOMS);
        g.getPlayerManager().setupPlayer("Test P1", 1);
        g.getPlayerManager().setupPlayer("Test P2", 2);

        gb.getListener().onRaySent(24);
        gb.getListener().onRaySent(53);
        gb.getListener().onRaySent(30);

        Ray ray = g.getBoard().getSentRays().get(0);
        Ray ray2 = g.getBoard().getSentRays().get(1);
        Ray ray3 = g.getBoard().getSentRays().get(2);


        assertEquals(ray.getInput(), 24);
        assertEquals(ray.getOutput(), 17);
        assertEquals(ray.getDeflectionType(),120);

        assertEquals(ray2.getInput(), 53);
        assertEquals(ray2.getOutput(), 48);
        assertEquals(ray2.getDeflectionType(),120);

        assertEquals(ray3.getInput(), 30);
        assertEquals(ray3.getOutput(), 25);
        assertEquals(ray3.getDeflectionType(),120);
    }

    @Test
    void testOnSendRay4() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);
        g.getStateManager().setCurrentState(GameState.SETTING_ATOMS);

        gb.getListener().onAtomPlaced(9,1);
        gb.getListener().onAtomPlaced(5,2);
        gb.getListener().onAtomPlaced(6,5);
        gb.getListener().onAtomPlaced(4,4);
        gb.getListener().onAtomPlaced(1,9);
        gb.getListener().onAtomPlaced(5,7);

        g.getStateManager().setCurrentState(GameState.GUESSING_ATOMS);
        g.getPlayerManager().setupPlayer("Test P1", 1);
        g.getPlayerManager().setupPlayer("Test P2", 2);

        gb.getListener().onRaySent(12);
        gb.getListener().onRaySent(6);
        gb.getListener().onRaySent(35);
        gb.getListener().onRaySent(41);

        Ray ray = g.getBoard().getSentRays().get(0);
        Ray ray2 = g.getBoard().getSentRays().get(1);
        Ray ray3 = g.getBoard().getSentRays().get(2);
        Ray ray4 = g.getBoard().getSentRays().get(3);

        assertEquals(ray.getInput(), 12);
        assertEquals(ray.getOutput(), 12);
        assertEquals(ray.getDeflectionType(),180);

        assertEquals(ray2.getInput(), 6);
        assertEquals(ray2.getOutput(), 6);
        assertEquals(ray2.getDeflectionType(),180);

        assertEquals(ray3.getInput(), 35);
        assertEquals(ray3.getOutput(), 35);
        assertEquals(ray3.getDeflectionType(),180);

        assertEquals(ray4.getInput(), 41);
        assertEquals(ray4.getOutput(), 41);
        assertEquals(ray4.getDeflectionType(),180);
    }

    @Test
    void testOnSendRay5() {
        Game g = new Game();
        GUIGameScreen gb = new GUIGameScreen(g);
        g.getStateManager().setCurrentState(GameState.SETTING_ATOMS);

        gb.getListener().onAtomPlaced(5,3);
        gb.getListener().onAtomPlaced(7,3);
        gb.getListener().onAtomPlaced(6,5);
        gb.getListener().onAtomPlaced(7,5);
        gb.getListener().onAtomPlaced(2,8);
        gb.getListener().onAtomPlaced(4,9);

        g.getStateManager().setCurrentState(GameState.GUESSING_ATOMS);
        g.getPlayerManager().setupPlayer("Test P1", 1);
        g.getPlayerManager().setupPlayer("Test P2", 2);

        gb.getListener().onRaySent(48);
        gb.getListener().onRaySent(14);
        gb.getListener().onRaySent(30);

        Ray ray = g.getBoard().getSentRays().get(0);
        Ray ray2 = g.getBoard().getSentRays().get(1);
        Ray ray3 = g.getBoard().getSentRays().get(2);

        assertEquals(ray.getInput(), 48);
        assertEquals(ray.getOutput(), 53);

        assertEquals(ray2.getInput(), 14);
        assertEquals(ray2.getOutput(), 14);

        assertEquals(ray3.getInput(), 30);
        assertEquals(ray3.getOutput(), -1);
    }

    //wrapper assertEquals to flip actual and expected arguments
    private static void assertEquals(int actual, int expected) {
        Assertions.assertEquals(expected, actual);
    }
}
