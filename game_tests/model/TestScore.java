package game_tests.model;
import controller.Game;
import model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestScore {

    @Test
    void testGameScore() {
        Player player = new Player("Test Player", false);
        Game game = new Game(player);

        game.getBoard().placeAtom(5, 5);
        game.getBoard().placeAtom(7,5);
        game.getBoard().placeAtom(8,1);
        game.getBoard().placeAtom(6,6);
        game.getBoard().placeAtom(1,5);
        game.getBoard().placeAtom(4,2);

        game.sendRay(29);
        game.sendRay(39);
        game.sendRay(46);
        game.sendRay(3);
        game.sendRay(5);
        game.sendRay(18);

        game.guessAtom(7, 7);
        game.guessAtom(8, 3);
        game.guessAtom(9, 4);
        game.guessAtom(6, 6);
        game.guessAtom(4, 2);
        game.guessAtom(6, 1);

        assertEquals(game.getExperimenter().getScore(), 30);
    }

    @Test
    void testSinglePlayer() {
        Game game = new Game();
        game.singlePlayerSetAtoms();
        assertEquals(game.getBoard().getNumAtomsPlaced(), 6);
    }
}