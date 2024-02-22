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

        b.placeAtom(5,9);
        assertTrue(b.getBoardPosition(5, 9) instanceof Atom);

        b.placeAtom(2,7);
        assertTrue(b.getBoardPosition(2, 7) instanceof Atom);

        b.placeAtom(4,2);
        assertTrue(b.getBoardPosition(4, 2) instanceof Atom);
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

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(47734, 243);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(0, -0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(-1000, 2);
        });
    }

    @Test
    void testCircleOfInfluenceList_Size(){
        Board b = new Board();
        b.placeAtom(4, 6);
        b.placeAtom(8, 2);
        b.placeAtom(1, 5);

        Atom a1 = (Atom) b.getBoardPosition(4, 6);
        assertEquals(6, a1.getCircleOfInfluence().size());

        Atom a2 = (Atom) b.getBoardPosition(8, 2);
        assertEquals(6, a2.getCircleOfInfluence().size());

        Atom a3 = (Atom) b.getBoardPosition(1, 5);
        assertEquals(6, a3.getCircleOfInfluence().size());
    }


}