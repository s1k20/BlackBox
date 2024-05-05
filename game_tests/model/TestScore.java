package game_tests.model;
import controller.Game;
import model.AiPlayer;
import model.Player;
import org.junit.jupiter.api.Test;
import view.TUIBoard;

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
        game.getPlayerManager().getPlayer2().updateScore(game.getBoard().getRayMarkerNumbers().size());

        assertEquals(27, game.getPlayerManager().getExperimenter().getScore());
    }

    @Test
    void testGameScore2() {
        Player player = new Player("Test Player", false);
        Game game = new Game(player);

        game.getBoard().placeAtom(6, 8);
        game.getBoard().placeAtom(3, 9);
        game.getBoard().placeAtom(7, 1);
        game.getBoard().placeAtom(8, 1);
        game.getBoard().placeAtom(9, 4);
        game.getBoard().placeAtom(5, 3);

        game.getBoard().sendRay(35);
        game.getBoard().sendRay(31);
        game.getBoard().sendRay(15);
        game.getBoard().sendRay(2);
        game.getBoard().sendRay(10);
        game.getBoard().sendRay(20);

        game.guessAtom(6, 8);
        game.guessAtom(3, 9);
        game.guessAtom(7, 1);
        game.guessAtom(8, 1);
        game.guessAtom(9, 4);
        game.guessAtom(6, 1);
        game.getPlayerManager().getPlayer2().updateScore(game.getBoard().getRayMarkerNumbers().size());

        assertEquals(14, game.getPlayerManager().getExperimenter().getScore());
    }


    @Test
    void testGameScore3() {
        Player player = new Player("Test Player", false);
        Game game = new Game(player);

        game.getBoard().placeAtom(1, 7);
        game.getBoard().placeAtom(1,9);
        game.getBoard().placeAtom(1,8);
        game.getBoard().placeAtom(6,7);
        game.getBoard().placeAtom(3,3);
        game.getBoard().placeAtom(2,5);

        game.getBoard().sendRay(46);
        game.getBoard().sendRay(50);
        game.getBoard().sendRay(48);
        game.getBoard().sendRay(32);
        game.getBoard().sendRay(5);
        game.getBoard().sendRay(1);

        game.guessAtom(1, 8);
        game.guessAtom(1, 9);
        game.guessAtom(1, 7);
        game.guessAtom(2, 5);
        game.guessAtom(3, 3);
        game.guessAtom(6, 7);
        game.getPlayerManager().getPlayer2().updateScore(game.getBoard().getRayMarkerNumbers().size());

        assertEquals(10, game.getPlayerManager().getExperimenter().getScore());
    }

//    @Test
//    void testSinglePlayer() {
//        Game game = new Game();
//
//        assertEquals(game.getBoard().getNumAtomsPlaced(), 6);
//    }
}