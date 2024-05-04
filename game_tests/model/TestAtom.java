package game_tests.model;
import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestAtom {

    @Test
    void testAtomPlace(){
        Board b = new Board();
        b.placeAtom(5, 1);
        assertTrue(b.getBoardPosition(5, 1) instanceof Atom);

        b.placeAtom(3,4);
        assertTrue(b.getBoardPosition(3, 4) instanceof Atom);

        b.placeAtom(7,1);
        assertTrue(b.getBoardPosition(7, 1) instanceof Atom);

        b.placeAtom(1,9);
        assertTrue(b.getBoardPosition(1, 9) instanceof Atom);

        b.placeAtom(9,5);
        assertTrue(b.getBoardPosition(9, 5) instanceof Atom);

        b.placeAtom(1,6);
        assertTrue(b.getBoardPosition(1, 6) instanceof Atom);
    }

    @Test
    void testAtomPlace2(){
        Board b = new Board();
        b.placeAtom(2, 9);
        assertTrue(b.getBoardPosition(2, 9) instanceof Atom);

        b.placeAtom(3,9);
        assertTrue(b.getBoardPosition(3, 9) instanceof Atom);

        b.placeAtom(8,6);
        assertTrue(b.getBoardPosition(8, 6) instanceof Atom);

        b.placeAtom(8,4);
        assertTrue(b.getBoardPosition(8, 4) instanceof Atom);

        b.placeAtom(5,3);
        assertTrue(b.getBoardPosition(5, 3) instanceof Atom);

        b.placeAtom(3,7);
        assertTrue(b.getBoardPosition(3, 7) instanceof Atom);
    }

    @Test
    void testAtomPlace3(){
        Board b = new Board();
        b.placeAtom(1, 6);
        assertTrue(b.getBoardPosition(1, 6) instanceof Atom);

        b.placeAtom(9,3);
        assertTrue(b.getBoardPosition(9, 3) instanceof Atom);

        b.placeAtom(3,5);
        assertTrue(b.getBoardPosition(3, 5) instanceof Atom);

        b.placeAtom(6,8);
        assertTrue(b.getBoardPosition(6, 8) instanceof Atom);

        b.placeAtom(7,7);
        assertTrue(b.getBoardPosition(7, 7) instanceof Atom);

        b.placeAtom(9,2);
        assertTrue(b.getBoardPosition(9, 2) instanceof Atom);
    }

    @Test
    void testAtomPlace4(){
        Board b = new Board();

        b.placeAtom(5,9);
        assertTrue(b.getBoardPosition(5, 9) instanceof Atom);

        b.placeAtom(2,7);
        assertTrue(b.getBoardPosition(2, 7) instanceof Atom);

        b.placeAtom(4,2);
        assertTrue(b.getBoardPosition(4, 2) instanceof Atom);

        b.placeAtom(9,1);
        assertTrue(b.getBoardPosition(9, 1) instanceof Atom);

        b.placeAtom(7,6);
        assertTrue(b.getBoardPosition(7, 6) instanceof Atom);

        b.placeAtom(8,5);
        assertTrue(b.getBoardPosition(8, 5) instanceof Atom);
    }

    @Test
    void testAtomPlace5() {
        Board b = new Board();

        b.placeAtom(9,3);
        assertTrue(b.getBoardPosition(9, 3) instanceof Atom);

        b.placeAtom(6,6);
        assertTrue(b.getBoardPosition(6, 6) instanceof Atom);

        b.placeAtom(7,4);
        assertTrue(b.getBoardPosition(7, 4) instanceof Atom);
    }


    @Test
    void testInvalidPosition() {
        Board b = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(1, 1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(4, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(10, 10);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(2, 17);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(5, 0);
        });
    }

    @Test
    void testInvalidPosition2() {
        Board b = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(47734, 243);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(0, -0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(-1000, 2);
        });


        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(1, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(10, 6);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(31, 0);
        });
    }

    @Test
    void testInvalidPosition3() {
        Board b = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(7, 9);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(9, 7);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(10, 8);
        });


        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(10, 7);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(0, 4);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(0, 2);
        });
    }

    @Test
    void testCircleOfInfluenceList_Size(){
        Board b = new Board();
        b.placeAtom(4, 6);
        b.placeAtom(8, 2);
        b.placeAtom(1, 5);
        b.placeAtom(4,7);
        b.placeAtom(1,9);
        b.placeAtom(7,1);

        Atom a1 = (Atom) b.getBoardPosition(4, 6);
        assertEquals(6, a1.getCircleOfInfluence().size());

        Atom a2 = (Atom) b.getBoardPosition(8, 2);
        assertEquals(6, a2.getCircleOfInfluence().size());

        Atom a3 = (Atom) b.getBoardPosition(1, 5);
        assertEquals(6, a3.getCircleOfInfluence().size());

        Atom a4 = (Atom) b.getBoardPosition(4, 7);
        assertEquals(6, a4.getCircleOfInfluence().size());

        Atom a5 = (Atom) b.getBoardPosition(1, 9);
        assertEquals(6, a5.getCircleOfInfluence().size());

        Atom a6 = (Atom) b.getBoardPosition(7, 1);
        assertEquals(6, a6.getCircleOfInfluence().size());
    }

    @Test
    void testCircleOfInfluenceList_Size2(){
        Board b = new Board();
        b.placeAtom(1, 7);
        b.placeAtom(2, 8);
        b.placeAtom(6, 3);
        b.placeAtom(4,3);
        b.placeAtom(2,4);
        b.placeAtom(9,2);

        Atom a1 = (Atom) b.getBoardPosition(1, 7);
        assertEquals(6, a1.getCircleOfInfluence().size());

        Atom a2 = (Atom) b.getBoardPosition(2, 8);
        assertEquals(6, a2.getCircleOfInfluence().size());

        Atom a3 = (Atom) b.getBoardPosition(6, 3);
        assertEquals(6, a3.getCircleOfInfluence().size());

        Atom a4 = (Atom) b.getBoardPosition(4, 3);
        assertEquals(6, a4.getCircleOfInfluence().size());

        Atom a5 = (Atom) b.getBoardPosition(2, 4);
        assertEquals(6, a5.getCircleOfInfluence().size());

        Atom a6 = (Atom) b.getBoardPosition(9, 2);
        assertEquals(6, a6.getCircleOfInfluence().size());
    }

    @Test
    void testCircleOfInfluenceList_Size3(){
        Board b = new Board();
        b.placeAtom(9, 4);
        b.placeAtom(5, 9);
        b.placeAtom(6, 2);
        b.placeAtom(8,2);
        b.placeAtom(3,3);
        b.placeAtom(4,4);

        Atom a1 = (Atom) b.getBoardPosition(9, 4);
        assertEquals(6, a1.getCircleOfInfluence().size());

        Atom a2 = (Atom) b.getBoardPosition(5, 9);
        assertEquals(6, a2.getCircleOfInfluence().size());

        Atom a3 = (Atom) b.getBoardPosition(6, 2);
        assertEquals(6, a3.getCircleOfInfluence().size());

        Atom a4 = (Atom) b.getBoardPosition(8, 2);
        assertEquals(6, a4.getCircleOfInfluence().size());

        Atom a5 = (Atom) b.getBoardPosition(3, 3);
        assertEquals(6, a5.getCircleOfInfluence().size());

        Atom a6 = (Atom) b.getBoardPosition(4, 4);
        assertEquals(6, a6.getCircleOfInfluence().size());
    }
    
}