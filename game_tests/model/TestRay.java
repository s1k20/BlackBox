package game_tests.model;
import model.*;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class TestRay {

    @Test
    void testPlaceRayMarker() {
        Board b = new Board();

        assertThrows(IllegalArgumentException.class, () -> b.placeRayMarker(4, 0));

        assertThrows(IllegalArgumentException.class, () -> b.placeRayMarker(6, 4));

        assertThrows(IllegalArgumentException.class, () -> b.placeRayMarker(3, 1));

        assertThrows(IllegalArgumentException.class, () -> b.placeRayMarker(2, 9));

        assertThrows(IllegalArgumentException.class, () -> b.placeRayMarker(10, 6));

        assertThrows(IllegalArgumentException.class, () -> b.placeRayMarker(1, 3));

        assertThrows(IllegalArgumentException.class, () -> b.placeRayMarker(2, 0));

        assertThrows(IllegalArgumentException.class, () -> b.placeRayMarker(7, 10));
    }


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
        RayMarker a = (RayMarker) b.getBoardPosition(4,1);
        assertEquals(red,a.getColour());

        b.placeAtom(3,8);
        b.sendRay(24);
        String green = "\u001B[32m";
       // RayMarker c2 = new RayMarker(3,10,green);
        RayMarker a2 = (RayMarker) b.getBoardPosition(3,10);
        assertEquals(green,a2.getColour());

        Board b2 = new Board();
        b2.placeAtom(6,4);
        b2.sendRay(32);
        String blue = "\u001B[33m";
        RayMarker a3 = (RayMarker) b2.getBoardPosition(10,1);
        assertEquals(blue,a3.getColour());
    }


}