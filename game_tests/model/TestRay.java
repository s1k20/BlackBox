package game_tests.model;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import view.TUIBoard;

public class TestRay {

    @Test
    void testNoAtomDetected() {
        Board b = new Board();
        b.placeAtom(7,3);
        b.placeAtom(2,7);
        b.placeAtom(2,4);
        b.placeAtom(6,4);
        b.placeAtom(6,5);
        b.placeAtom(6,7);

        b.sendRay(2);
        b.sendRay(3);
        b.sendRay(36);
        b.sendRay(18);

        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 2);
        assertEquals(ray.getOutput(), 45);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 3);
        assertEquals(ray2.getOutput(), 26);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 36);
        assertEquals(ray3.getOutput(), 47);

        Ray ray4 = b.getSentRays().get(3);
        assertEquals(ray4.getInput(), 18);
        assertEquals(ray4.getOutput(), 29);
    }

    @Test
    void testNoAtomDetected2() {
        Board b2 = new Board();
        b2.placeAtom(8,2);
        b2.placeAtom(8,3);
        b2.placeAtom(8,4);
        b2.placeAtom(4,6);
        b2.placeAtom(5,9);
        b2.placeAtom(4,2);

        b2.sendRay(15);
        b2.sendRay(22);
        b2.sendRay(9);

        Ray ray5 = b2.getSentRays().get(0);
        assertEquals(ray5.getInput(), 15);
        assertEquals(ray5.getOutput(), 50);

        Ray ray6 = b2.getSentRays().get(1);
        assertEquals(ray6.getInput(), 22);
        assertEquals(ray6.getOutput(), 7);

        Ray ray7 = b2.getSentRays().get(2);
        assertEquals(ray7.getInput(), 9);
        assertEquals(ray7.getOutput(), 20);
    }

    @Test
    void testAbsorb() {
        Board b = new Board();
        b.placeAtom(7,1);
        b.placeAtom(5,9);

        b.sendRay(2);
        b.sendRay(15);
        b.sendRay(18);
        b.sendRay(1);

        Ray sentRay = b.getSentRays().get(0);
        assertEquals(sentRay.getInput(), 2);
        assertEquals(sentRay.getOutput(), -1);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 15);
        assertEquals(ray2.getOutput(), -1);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 18);
        assertEquals(ray3.getOutput(), -1);

        Ray ray4 = b.getSentRays().get(3);
        assertEquals(ray4.getInput(), 1);
        assertEquals(ray4.getOutput(), -1);
    }

    @Test
    void testAbsorb2() {
        Board b = new Board();
        b.placeAtom(8,5);
        b.placeAtom(5,8);
        b.placeAtom(5,5);


        b.sendRay(37);
        b.sendRay(28);
        b.sendRay(10);

        Ray sentRay = b.getSentRays().get(0);
        assertEquals(sentRay.getInput(), 37);
        assertEquals(sentRay.getOutput(), -1);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 28);
        assertEquals(ray2.getOutput(), -1);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 10);
        assertEquals(ray3.getOutput(), -1);
    }



    @Test
    void test60deflection() {
        Board b = new Board();
        b.placeAtom(6,4);

        b.sendRay(32);
        b.sendRay(17);
        b.sendRay(51);

        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 32);
        assertEquals(ray.getOutput(), 44);
        assertEquals(ray.getDeflectionType(),60);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 17);
        assertEquals(ray2.getOutput(), 1);
        assertEquals(ray2.getDeflectionType(),60);
    }

    @Test
    void test60deflection2() {
        Board b2 = new Board();
        b2.placeAtom(3,5);
        b2.sendRay(7);
        b2.sendRay(26);

        Ray ray3 = b2.getSentRays().get(0);
        assertEquals(ray3.getInput(), 7);
        assertEquals(ray3.getOutput(), 13);
        assertEquals(ray3.getDeflectionType(),60);

        Ray ray4 = b2.getSentRays().get(1);
        assertEquals(ray4.getInput(), 26);
        assertEquals(ray4.getOutput(), 48);
        assertEquals(ray4.getDeflectionType(),60);
    }

    @Test
    void testRayMarkerColour() {
        Board b = new Board();
        b.sendRay(2);
        String red = "\u001B[31m";
        RayMarker r = (RayMarker) b.getBoardPosition(4,1);
        assertEquals(red,r.getColour());

        b.placeAtom(3,8);
        b.sendRay(24);
        String green = "\u001B[32m";
        RayMarker r2 = (RayMarker) b.getBoardPosition(3,10);
        assertEquals(green,r2.getColour());

        Board b2 = new Board();
        b2.placeAtom(6,4);
        b2.sendRay(32);
        String yellow = "\u001B[34m";
        RayMarker r3 = (RayMarker) b2.getBoardPosition(10,1);
        assertEquals(yellow, r3.getColour());

        Board b3 = new Board();
        b3.placeAtom(4,5);
        b3.placeAtom(5,5);
        b3.sendRay(19);
        String pink = "\033[33m";
        RayMarker r4 = (RayMarker) b3.getBoardPosition(4,10);
        assertEquals(pink,r4.getColour());
    }

    @Test
    void testOrientation() {
        Board b = new Board();

        b.placeAtom(5,5);
        b.sendRay(10);
        b.sendRay(37);
        b.sendRay(28);
        b.sendRay(19);
        b.sendRay(46);
        b.sendRay(1);

        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 10);
        assertEquals(ray.getOutput(), -1);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 37);
        assertEquals(ray2.getOutput(), -1);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 28);
        assertEquals(ray3.getOutput(), -1);

        Ray ray4 = b.getSentRays().get(3);
        assertEquals(ray4.getInput(), 19);
        assertEquals(ray4.getOutput(), -1);

        Ray ray5 = b.getSentRays().get(4);
        assertEquals(ray5.getInput(), 46);
        assertEquals(ray5.getOutput(), -1);

        Ray ray6 = b.getSentRays().get(5);
        assertEquals(ray6.getInput(), 1);
        assertEquals(ray6.getOutput(), -1);
    }

    @Test
    void testOutput() {
        Board b = new Board();

        b.placeAtom(8,3);
        b.sendRay(24);
        b.sendRay(21);
        b.sendRay(5);
        b.sendRay(44);
        b.sendRay(29);
        b.sendRay(16);

        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 24);
        assertEquals(ray.getOutput(), 5);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 21);
        assertEquals(ray2.getOutput(), -1);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 5);
        assertEquals(ray3.getOutput(), 24);

        Ray ray4 = b.getSentRays().get(3);
        assertEquals(ray4.getInput(), 44);
        assertEquals(ray4.getOutput(), -1);

        Ray ray5 = b.getSentRays().get(4);
        assertEquals(ray5.getInput(), 29);
        assertEquals(ray5.getOutput(), 18);

        Ray ray6 = b.getSentRays().get(5);
        assertEquals(ray6.getInput(), 16);
        assertEquals(ray6.getOutput(), 31);
    }

    @Test
    void test120deflection() {
        Board b = new Board();
        TUIBoard view = new TUIBoard(b);

        b.placeAtom(3,5);
        b.placeAtom(4,5);
        b.placeAtom(5,4);
        b.placeAtom(6,4);
        b.placeAtom(6,6);
        b.placeAtom(7,6);

        b.sendRay(24);
        b.sendRay(53);
        b.sendRay(30);

        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 24);
        assertEquals(ray.getOutput(), 17);
        assertEquals(ray.getDeflectionType(),120);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 53);
        assertEquals(ray2.getOutput(), 48);
        assertEquals(ray2.getDeflectionType(),120);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 30);
        assertEquals(ray3.getOutput(), 25);
        assertEquals(ray3.getDeflectionType(),120);

        view.printEntireBoard();
    }

    @Test
    void test120deflection2() {
        Board b = new Board();
        TUIBoard view = new TUIBoard(b);

        b.placeAtom(6,3);
        b.placeAtom(5,5);
        b.placeAtom(5,4);
        b.placeAtom(6,4);

        b.sendRay(37);
        b.sendRay(17);
        b.sendRay(6);
        b.sendRay(46);

        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 37);
        assertEquals(ray.getOutput(), 30);
        assertEquals(ray.getDeflectionType(),120);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 17);
        assertEquals(ray2.getOutput(), 10);
        assertEquals(ray2.getDeflectionType(),120);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 6);
        assertEquals(ray3.getOutput(), 1);
        assertEquals(ray3.getDeflectionType(),120);

        Ray ray4 = b.getSentRays().get(3);
        assertEquals(ray4.getInput(), 46);
        assertEquals(ray4.getOutput(), 41);
        assertEquals(ray4.getDeflectionType(),120);

        view.printEntireBoard();
    }

    @Test
    void test180deflection() {
        Board b = new Board();
        TUIBoard view = new TUIBoard(b);

        b.placeAtom(9,1);
        b.placeAtom(5,2);
        b.placeAtom(6,5);
        b.placeAtom(4,4);
        b.placeAtom(1,9);
        b.placeAtom(5,7);

        b.sendRay(12);
        b.sendRay(6);
        b.sendRay(35);
        b.sendRay(41);

        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 12);
        assertEquals(ray.getOutput(), 12);
        assertEquals(ray.getDeflectionType(),180);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 6);
        assertEquals(ray2.getOutput(), 6);
        assertEquals(ray2.getDeflectionType(),180);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 35);
        assertEquals(ray3.getOutput(), 35);
        assertEquals(ray3.getDeflectionType(),180);

        Ray ray4 = b.getSentRays().get(3);
        assertEquals(ray4.getInput(), 41);
        assertEquals(ray4.getOutput(), 41);
        assertEquals(ray4.getDeflectionType(),180);

        view.printEntireBoard();
    }

    @Test
    void test180deflection2() {
        Board b = new Board();
        TUIBoard view = new TUIBoard(b);

        b.placeAtom(5,4);
        b.placeAtom(3,5);
        b.placeAtom(6,5);
        b.placeAtom(3,8);
        b.placeAtom(2,7);
        b.placeAtom(5,7);


        b.sendRay(19);
        b.sendRay(26);
        b.sendRay(46);
        b.sendRay(3);

        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 19);
        assertEquals(ray.getOutput(), 19);
        assertEquals(ray.getDeflectionType(),180);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 26);
        assertEquals(ray2.getOutput(), 26);
        assertEquals(ray2.getDeflectionType(),180);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 46);
        assertEquals(ray3.getOutput(), 46);
        assertEquals(ray3.getDeflectionType(),180);

        Ray ray4 = b.getSentRays().get(3);
        assertEquals(ray4.getInput(), 3);
        assertEquals(ray4.getOutput(), 3);
        assertEquals(ray4.getDeflectionType(),180);

        view.printEntireBoard();
    }

    @Test
    void testAdvancedRayPath() {
        Board b = new Board();
        TUIBoard view = new TUIBoard(b);

        b.placeAtom(5,3);
        b.placeAtom(7,3);
        b.placeAtom(6,5);
        b.placeAtom(7,5);
        b.placeAtom(2,8);
        b.placeAtom(4,9);

        b.sendRay(48);
        b.sendRay(14);
        b.sendRay(30);

        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 48);
        assertEquals(ray.getOutput(), 53);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 14);
        assertEquals(ray2.getOutput(), 14);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 30);
        assertEquals(ray3.getOutput(), -1);

        view.printEntireBoard();
    }

    @Test
    void testAdvancedRayPath2() {
        Board b = new Board();
        TUIBoard view = new TUIBoard(b);

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


        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 8);
        assertEquals(ray.getOutput(), 8);
        assertEquals(ray.getDeflectionType(),180);

        Ray ray2 = b.getSentRays().get(3);
        assertEquals(ray2.getInput(), 41);
        assertEquals(ray2.getOutput(), 41);
        assertEquals(ray2.getDeflectionType(),180);

        Ray ray3 = b.getSentRays().get(4);
        assertEquals(ray3.getInput(), 32);
        assertEquals(ray3.getOutput(), 44);
        assertEquals(ray3.getDeflectionType(),60);

        Ray ray4 = b.getSentRays().get(5);
        assertEquals(ray4.getInput(), 28);
        assertEquals(ray4.getOutput(), 28);
        assertEquals(ray4.getDeflectionType(),180);

        Ray ray5 = b.getSentRays().get(6);
        assertEquals(ray5.getInput(), 17);
        assertEquals(ray5.getOutput(), 24);
        assertEquals(ray5.getDeflectionType(),120);

        view.printEntireBoard();
    }

    @Test
    void testAtomEdge() {
        Board b = new Board();
        TUIBoard view = new TUIBoard(b);

        b.placeAtom(2,4);
        b.placeAtom(9,4);
        b.placeAtom(5,1);
        b.placeAtom(9,1);
        b.placeAtom(5,9);
        b.placeAtom(1,9);

        b.sendRay(6);
        b.sendRay(41);
        b.sendRay(3);
        b.sendRay(44);
        b.sendRay(26);
        b.sendRay(17);

        Ray ray = b.getSentRays().get(0);
        assertEquals(ray.getInput(), 6);
        assertEquals(ray.getOutput(), 6);

        Ray ray2 = b.getSentRays().get(1);
        assertEquals(ray2.getInput(), 41);
        assertEquals(ray2.getOutput(), 41);

        Ray ray3 = b.getSentRays().get(2);
        assertEquals(ray3.getInput(), 3);
        assertEquals(ray3.getOutput(), 3);

        Ray ray4 = b.getSentRays().get(3);
        assertEquals(ray4.getInput(), 44);
        assertEquals(ray4.getOutput(), 44);

        Ray ray5 = b.getSentRays().get(4);
        assertEquals(ray5.getInput(), 26);
        assertEquals(ray5.getOutput(), 26);

        Ray ray6 = b.getSentRays().get(5);
        assertEquals(ray6.getInput(), 17);
        assertEquals(ray6.getOutput(), 17);

        view.printEntireBoard();
    }

    //wrapper assertEquals to flip actual and expected arguments
    private static void assertEquals(int actual, int expected) {
        Assertions.assertEquals(expected, actual);
    }

    private static void assertEquals(String actual, String expected) {
        Assertions.assertEquals(expected, actual);
    }
}