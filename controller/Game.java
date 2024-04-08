package controller;
import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

//controller part of project which runs main logic
//updates model and view
public class Game {

    //players objects which will hold necessary information about players
    private Player player1;
    private Player player2;

    //board object which holds logic to create the game
    private Board board;

    //variable to allow for two
    private int gameNum;
    private final int numGames = 2;
    private final int numAtoms = 6;

    //player controller class to allow game to get necessary user input
    private PlayerInput playerIn;
    private GameView view;

    GameState state;

    //track the experimenters previously guessed atoms
    private ArrayList<Point> guessedAtoms;

    //game constructor which inits all objects/variables
    public Game() {
        board = new Board();
        view = new GameView(this);
        playerIn = new PlayerInput();

        //set gameNum to 1 for the first game
        gameNum = 1;
    }

    public Game(Player player) {
        board = new Board();
        this.player1 = player;
    }


    public void playGame() {
//        int choice;
        GUIMenu.createAndShowGUI();
//        do {
//            view.printStart();
//            choice = playerIn.getPlayerOption();
//            switch (choice) {
//                case 1 -> playSinglePlayerGame();
//                case 2 -> play2PlayerGame();
//            }
//        } while (choice != 3);

    }

    public void testGame() {
        board = new Board();
        view = new GameView(this);

        board.placeAtom(5, 3);
        board.placeAtom(7, 3);
        board.placeAtom(6, 5);
        board.placeAtom(7, 5);
        board.placeAtom(2, 8);
        board.placeAtom(4, 9);
        view.printEntireBoard();


        board.sendRay(48);
        board.sendRay(14);
        board.sendRay(30);
        view.printEntireBoard();
    }

    public Board getBoard() {
        return this.board;
    }

    public void play2PlayerGame() {
        //create players

        String name1 = GUI_UserInput.askForPlayerName("Player 1 - Enter Name");
        if (name1 != null && !name1.trim().isEmpty()) {
            player1 = new Player(name1, true);
        } else {
            // Handle cancellation or empty input
            return; // Optionally, loop back or exit
        }

        String name2 = GUI_UserInput.askForPlayerName("Player 2 - Enter Name");
        if (name2 != null && !name2.trim().isEmpty()) {
            player2 = new Player(name2, false);
        } else {
            // Handle cancellation or empty input
            return; // Optionally, loop back or exit
        }

        //main game loop
        while (gameNum <= numGames) {
            guessedAtoms = new ArrayList<>();
            //first round player 1 is setter and player 2 is experimenter
            view.printRound(gameNum);
            //let setter place 6 atoms

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Black Box Plus by Cian, Lloyd and Shlok");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new GUIGameBoard(this));
                frame.pack();
                frame.setLocationRelativeTo(null); // Center on screen
                frame.setVisible(true);
            });


            state = GameState.SettingAtoms;
            setAtoms();

            //let experimenter send rays
            state = GameState.SendingRays;
            sendRays();

            //let experimenter guess atoms locations
            state = GameState.GuessingAtoms;
            guessAtoms();

            view.printEntireBoard();

            //switch the roles and increase gameNum
            switchRoles();
            gameNum += 1;

            playerIn.resetSentRays();
            board = new Board();
            view = new GameView(this);
        }
        Player winner = getWinner();
        view.printStats(player1, player2, winner);
    }

    public void playSinglePlayerGame() {
        String name1 = GUI_UserInput.askForPlayerName("Player 1 - Enter Name");
        if (name1 != null && !name1.trim().isEmpty()) {
            player1 = new Player(name1, false);
        } else {
            // Handle cancellation or empty input
            return; // Optionally, loop back or exit
        }


//        singlePlayerSetAtoms();
    while (gameNum <= numGames) {
        guessedAtoms = new ArrayList<>();

    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Black Box Plus by Cian, Lloyd and Shlok");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GUIGameBoard(this));
        frame.pack();
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
    });

    sendRays();

    guessAtoms();
}

        view.printEntireBoard();

        view.printStats_SinglePlayer(player1);
    }

    public void setAtom(boolean isSetter) {
        if (isSetter)
            System.out.println("Please enter an X Co-ordinate and a Y Co-ordinate (comma separated) in which to PLACE an Atom:");
        else
            System.out.println("Please enter an X Co-ordinate and a Y Co-ordinate (comma separated) in which to GUESS an Atom:");
        int[] co_ords;
        Point p;

        do {
            co_ords = playerIn.getAtomInput();

            p = new Point(co_ords[1], co_ords[0]);

            if (isSetter && board.getBoardPosition(co_ords[0], co_ords[1]) instanceof Atom) {
                System.out.println(getSetter().getPlayerName() + " - You have already placed an atom in this position!");
            }
            if (!isSetter && guessedAtoms.contains(p))
                System.out.println(getExperimenter().getPlayerName() + " - You have already guessed this position!");

        } while ((isSetter && board.getBoardPosition(co_ords[0], co_ords[1]) instanceof Atom) || (!isSetter && guessedAtoms.contains(p)));

        if (isSetter) board.placeAtom(co_ords[0], co_ords[1]);
        else guessAtom(co_ords[0], co_ords[1]);
    }

    public void setAtoms() {
        for (int i = 0; i < numAtoms; i++) {
            System.out.print(getSetter().getPlayerName() + " - (Setter): ");
            setAtom(true);
            view.printEntireBoard();
        }
    }


    public void guessAtoms() {
        for (int i = 0; i < numAtoms; i++) {
            System.out.print(getExperimenter().getPlayerName() + " - (Experimenter): ");
            setAtom(false);
        }
    }

    public void guessAtom(int x, int y) {
        Point p = new Point(x, y);
        guessedAtoms.add(p);
        if (!(board.getBoardPosition(x, y) instanceof Atom)) getExperimenter().updateScore(5);
        else getExperimenter().correctAtom();
    }

    public void sendRays() {
        String input = "";

        do {
            System.out.print(getExperimenter().getPlayerName() + " - (Experimenter): ");
            System.out.println("Enter a number between 1 and 54 to send a ray or hit 'ENTER' to stop:");
            input = playerIn.getRayInput();
            if (input.isEmpty()) {
                break;
            }
            sendRay(Integer.parseInt(input));

            view.printLiveBoard();
            getExperimenter().raySent();

        } while (true);

    }

    public void placeAtom(int x, int y) {
        board.placeAtom(x, y);
    }

    public void removeAtom(int x, int y) {
        board.removeAtom(x, y);
    }

    public void sendRay(int input) {
        if (board.sendRay(input)) {
            getExperimenter().updateScore(1);
        } else {
            getExperimenter().updateScore(2);
        }
    }


    //returns setter
    public Player getSetter() {
        return player1.isSetter() ? player1 : player2;
    }

    //returns experimenter
    public Player getExperimenter() {
        return !player1.isSetter() ? player1 : player2;
    }


    //function to just swap who is setter and who is experimenter
    public void switchRoles() {
        if (player1.isSetter()) {
            player1.setExperimenter();
            player2.setSetter();
        } else {
            player1.setSetter();
            player2.setExperimenter();
        }
    }

    public Player getWinner() {
        if (player1.getScore() < player2.getScore()) return player1;
        else if (player2.getScore() < player1.getScore()) return player2;
        else return null;
    }

}
