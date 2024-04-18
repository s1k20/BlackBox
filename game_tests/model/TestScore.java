package game_tests.model;
import controller.Game;
import model.AiPlayer;
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

        game.getBoard().sendRay(29);
        game.getBoard().sendRay(39);
        game.getBoard().sendRay(46);
        game.getBoard().sendRay(3);
        game.getBoard().sendRay(5);
        game.getBoard().sendRay(18);

        game.guessAtom(7, 7);
        game.guessAtom(8, 3);
        game.guessAtom(9, 4);
        game.guessAtom(6, 6);
        game.guessAtom(4, 2);
        game.guessAtom(6, 1);
        player.updateScore(game.getBoard().getRayMarkerNumbers().size());

        assertEquals(game.getExperimenter().getScore(), 27);

    }

//    @Test
//    void testSinglePlayer() {
//        Game game = new Game();
//
//        assertEquals(game.getBoard().getNumAtomsPlaced(), 6);
//    }
}