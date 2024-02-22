package test.model;
import model.*;
import model.CircleOfInfluence;
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


 /*   //TODO do this
   @Test
    void testInvalidPosition(){
        Board b = new Board();
        b.placeAtom(6,9);
*/



    /*   assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(3, 2);
       });

        assertThrows(IllegalArgumentException.class, () -> {
            b.placeAtom(8, 0);
        });*/


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

  /*  @Test
    void testOrientation() {
        Board b = new Board();
        b.placeAtom(5, 5);

        (model.Atom) b.getBoardPosition(5, 5);
    }*/





}
