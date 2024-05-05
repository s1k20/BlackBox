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
        game.getBoard().placeAtom(7, 5);
        game.getBoard().placeAtom(8, 1);
        game.getBoard().placeAtom(6, 6);
        game.getBoard().placeAtom(1, 5);
        game.getBoard().placeAtom(4, 2);

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
        game.getBoard().placeAtom(1, 9);
        game.getBoard().placeAtom(1, 8);
        game.getBoard().placeAtom(6, 7);
        game.getBoard().placeAtom(3, 3);
        game.getBoard().placeAtom(2, 5);

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

    @Test
    void testGameScore4() {
        Player player = new Player("Test Player", false);
        Game game = new Game(player);

        game.getBoard().placeAtom(2, 4);
        game.getBoard().placeAtom(6, 2);
        game.getBoard().placeAtom(1, 8);
        game.getBoard().placeAtom(5, 4);
        game.getBoard().placeAtom(5, 5);
        game.getBoard().placeAtom(3, 3);

        game.getBoard().sendRay(5);
        game.getBoard().sendRay(23);
        game.getBoard().sendRay(41);
        game.getBoard().sendRay(37);
        game.getBoard().sendRay(15);
        game.getBoard().sendRay(12);

        game.guessAtom(3, 3);
        game.guessAtom(1, 9);
        game.guessAtom(1, 7);
        game.guessAtom(6, 2);
        game.guessAtom(3, 8);
        game.guessAtom(3, 9);
        game.getPlayerManager().getPlayer2().updateScore(game.getBoard().getRayMarkerNumbers().size());

        assertEquals(28, game.getPlayerManager().getExperimenter().getScore());
    }

    @Test
    void testGameScore5() {
        Player player = new Player("Test Player", false);
        Game game = new Game(player);

        game.getBoard().placeAtom(9, 1);
        game.getBoard().placeAtom(7, 4);
        game.getBoard().placeAtom(5, 2);
        game.getBoard().placeAtom(4, 2);
        game.getBoard().placeAtom(4, 9);
        game.getBoard().placeAtom(3, 5);

        game.getBoard().sendRay(7);
        game.getBoard().sendRay(30);
        game.getBoard().sendRay(12);
        game.getBoard().sendRay(11);
        game.getBoard().sendRay(42);
        game.getBoard().sendRay(50);

        game.guessAtom(9, 1);
        game.guessAtom(7, 4);
        game.guessAtom(3, 5);
        game.guessAtom(4, 9);
        game.guessAtom(9, 2);
        game.guessAtom(6, 1);
        game.getPlayerManager().getPlayer2().updateScore(game.getBoard().getRayMarkerNumbers().size());

        assertEquals(20, game.getPlayerManager().getExperimenter().getScore());
    }

    @Test
    void testGameScore6() {
        Player player = new Player("Test Player", false);
        Game game = new Game(player);

        game.getBoard().placeAtom(1, 9);
        game.getBoard().placeAtom(7, 2);
        game.getBoard().placeAtom(5, 5);
        game.getBoard().placeAtom(5, 6);
        game.getBoard().placeAtom(4, 7);
        game.getBoard().placeAtom(2, 6);

        game.getBoard().sendRay(17);
        game.getBoard().sendRay(32);
        game.getBoard().sendRay(2);
        game.getBoard().sendRay(3);
        game.getBoard().sendRay(49);
        game.getBoard().sendRay(48);

        game.guessAtom(6, 6);
        game.guessAtom(4, 5);
        game.guessAtom(1, 7);
        game.guessAtom(8, 5);
        game.guessAtom(7, 2);
        game.guessAtom(6, 3);
        game.getPlayerManager().getPlayer2().updateScore(game.getBoard().getRayMarkerNumbers().size());

        assertEquals(33, game.getPlayerManager().getExperimenter().getScore());
    }
}

