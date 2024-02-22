package game_tests.model;
import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestCircleOfInfluences {

    @Test
    void testCircleOfInfluencePlace() {
        Board b = new Board();

        b.placeAtom(4,5);
        assertTrue(b.getBoardPosition(4,6) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(3,6) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(5,5) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(3,5) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(4,4) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(5,4) instanceof CircleOfInfluence);

        b.placeAtom(5,1);
        assertTrue(b.getBoardPosition(6,1) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(4,1) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(4,2) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(5,0) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(5,2) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(6,0) instanceof CircleOfInfluence);

        b.placeAtom(5,9);
        assertTrue(b.getBoardPosition(5,8) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(6,9) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(4,10) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(6,8) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(4,9) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(5,10) instanceof CircleOfInfluence);


        Board b2 = new Board();

        b2.placeAtom(6,4);
        assertTrue(b2.getBoardPosition(6,3) instanceof CircleOfInfluence);
        assertTrue(b2.getBoardPosition(6,5) instanceof CircleOfInfluence);
        assertTrue(b2.getBoardPosition(5,4) instanceof CircleOfInfluence);
        assertTrue(b2.getBoardPosition(7,4) instanceof CircleOfInfluence);
        assertTrue(b2.getBoardPosition(5,5) instanceof CircleOfInfluence);
        assertTrue(b2.getBoardPosition(7,3) instanceof CircleOfInfluence);

        b2.placeAtom(9,5);
        assertTrue(b2.getBoardPosition(9,4) instanceof CircleOfInfluence);
        assertTrue(b2.getBoardPosition(8,5) instanceof CircleOfInfluence);
        assertTrue(b2.getBoardPosition(8,6) instanceof CircleOfInfluence);
        assertFalse(b2.getBoardPosition(9,6) instanceof CircleOfInfluence);
        assertFalse(b2.getBoardPosition(10,4) instanceof CircleOfInfluence);
        assertFalse(b2.getBoardPosition(10,5) instanceof CircleOfInfluence);

        b2.placeAtom(1,9);
        assertTrue(b2.getBoardPosition(1,8) instanceof CircleOfInfluence);
        assertTrue(b2.getBoardPosition(2,8) instanceof CircleOfInfluence);
        assertTrue(b2.getBoardPosition(2,9) instanceof CircleOfInfluence);
        assertFalse(b2.getBoardPosition(0,9) instanceof CircleOfInfluence);
        assertFalse(b2.getBoardPosition(0,10) instanceof CircleOfInfluence);
        assertFalse(b2.getBoardPosition(1,10) instanceof CircleOfInfluence);

    }

    @Test
    void testOrientation() {
        Board b = new Board();
        b.placeAtom(5, 5);

        CircleOfInfluence c = (CircleOfInfluence) b.getBoardPosition(4, 6);
        assertEquals(300, c.getOrientation());

        CircleOfInfluence c2 = (CircleOfInfluence) b.getBoardPosition(5, 6);
        assertEquals(240, c2.getOrientation());

        CircleOfInfluence c3 = (CircleOfInfluence) b.getBoardPosition(5, 4);
        assertEquals(60, c3.getOrientation());

        CircleOfInfluence c4 = (CircleOfInfluence) b.getBoardPosition(6, 4);
        assertEquals(120, c4.getOrientation());

        CircleOfInfluence c5 = (CircleOfInfluence) b.getBoardPosition(4, 5);
        assertEquals(90, c5.getOrientation());

        CircleOfInfluence c6 = (CircleOfInfluence) b.getBoardPosition(6, 5);
        assertEquals(270, c6.getOrientation());



        b.placeAtom(8, 2);

        CircleOfInfluence C = (CircleOfInfluence) b.getBoardPosition(9, 2);
        assertEquals(270, C.getOrientation());

        CircleOfInfluence C2 = (CircleOfInfluence) b.getBoardPosition(7, 2);
        assertEquals(90, C2.getOrientation());

        CircleOfInfluence C3 = (CircleOfInfluence) b.getBoardPosition(7, 3);
        assertEquals(300, C3.getOrientation());

        CircleOfInfluence C4 = (CircleOfInfluence) b.getBoardPosition(9, 1);
        assertEquals(120, C4.getOrientation());

        CircleOfInfluence C5 = (CircleOfInfluence) b.getBoardPosition(8, 1);
        assertEquals(60, C5.getOrientation());

        CircleOfInfluence C6 = (CircleOfInfluence) b.getBoardPosition(8, 3);
        assertEquals(240, C6.getOrientation());

    }

    @Test
    void testIntersectingCircleOfInfluence(){

        Board b = new Board();
        b.placeAtom(5, 5);
        b.placeAtom(6, 4);

        assertTrue(b.getBoardPosition(6,5) instanceof IntersectingCircleOfInfluence);
        assertTrue(b.getBoardPosition(5,4) instanceof IntersectingCircleOfInfluence);

        IntersectingCircleOfInfluence c1 = (IntersectingCircleOfInfluence) b.getBoardPosition(6, 5);
        IntersectingCircleOfInfluence c2 = (IntersectingCircleOfInfluence) b.getBoardPosition(5, 4);

        assertEquals(2, c1.getCircleOfInfluences().size());
        assertEquals(2, c2.getCircleOfInfluences().size());

        b.placeAtom(5, 3);
        assertEquals(3, c2.getCircleOfInfluences().size());
    }

}
