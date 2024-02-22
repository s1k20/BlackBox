package test.model;
import model.*;
import controller.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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


    /*@Test
    //method is private, need other way to test for incorrect atom
    void testInvalidPosition() {
        Board b = new Board();
        PlayerController playerController = new PlayerController();

        assertTrue(playerController.checkInvalidInput(3,2));
        assertTrue(playerController.checkInvalidInput(9,0));
        assertTrue(playerController.checkInvalidInput(100,2));
        assertTrue(playerController.checkInvalidInput(0,7));
        assertTrue(playerController.checkInvalidInput(9,9));
        assertTrue(playerController.checkInvalidInput(1,10));
        assertTrue(playerController.checkInvalidInput(4,1));
        assertTrue(playerController.checkInvalidInput(5,0));
        assertTrue(playerController.checkInvalidInput(6,0));
        assertTrue(playerController.checkInvalidInput(10,2));

    }*/


    @Test
    void testCircleOfInfluencePlace() {
        Board b = new Board();

        b.placeAtom(5,1);
        assertTrue(b.getBoardPosition(6,1) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(4,1) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(4,2) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(5,0) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(5,2) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(6,0) instanceof CircleOfInfluence);

        b.placeAtom(4,5);
        assertTrue(b.getBoardPosition(4,6) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(3,6) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(5,5) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(3,5) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(4,4) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(5,4) instanceof CircleOfInfluence);


        b.placeAtom(5,9);
        assertTrue(b.getBoardPosition(5,8) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(6,9) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(4,10) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(6,8) instanceof CircleOfInfluence);
        assertTrue(b.getBoardPosition(4,9) instanceof CircleOfInfluence);
        assertFalse(b.getBoardPosition(5,10) instanceof CircleOfInfluence);
    }

    @Test
    void testOrientation() {
        Board b = new Board();
        b.placeAtom(5, 5);

        CircleOfInfluence c = (CircleOfInfluence) b.getBoardPosition(4, 6);
        assertEquals(135, c.getOrientation());

        CircleOfInfluence c2 = (CircleOfInfluence) b.getBoardPosition(5, 6);
        assertEquals(45, c2.getOrientation());

        CircleOfInfluence c3 = (CircleOfInfluence) b.getBoardPosition(5, 4);
        assertEquals(45, c3.getOrientation());

        CircleOfInfluence c4 = (CircleOfInfluence) b.getBoardPosition(6, 4);
        assertEquals(135, c4.getOrientation());

        CircleOfInfluence c5 = (CircleOfInfluence) b.getBoardPosition(4, 5);
        assertEquals(90, c5.getOrientation());

        CircleOfInfluence c6 = (CircleOfInfluence) b.getBoardPosition(6, 5);
        assertEquals(90, c6.getOrientation());


        b.placeAtom(8, 2);

        CircleOfInfluence C = (CircleOfInfluence) b.getBoardPosition(9, 2);
        assertEquals(90, C.getOrientation());

        CircleOfInfluence C2 = (CircleOfInfluence) b.getBoardPosition(7, 2);
        assertEquals(90, C2.getOrientation());

        CircleOfInfluence C3 = (CircleOfInfluence) b.getBoardPosition(7, 3);
        assertEquals(135, C3.getOrientation());

        CircleOfInfluence C4 = (CircleOfInfluence) b.getBoardPosition(9, 1);
        assertEquals(135, C4.getOrientation());

        CircleOfInfluence C5 = (CircleOfInfluence) b.getBoardPosition(8, 1);
        assertEquals(45, C5.getOrientation());

        CircleOfInfluence C6 = (CircleOfInfluence) b.getBoardPosition(8, 3);
        assertEquals(45, C6.getOrientation());

    }


}