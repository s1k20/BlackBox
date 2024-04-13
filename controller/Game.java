package controller;
import jdk.dynalink.linker.GuardedInvocationTransformer;
import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

//controller part of project which runs main logic
//updates model and view
public class Game implements BoardInputListener {

    //players objects which will hold necessary information about players
    private Player player1;
    private Player player2;

    //board object which holds logic to create the game
    private Board board;
    private Board guessingBoard;

    //variable to allow for two
    private int gameNum;
    private final int numGames = 2;
    private final int numAtoms = 6;

    //player controller class to allow game to get necessary user input
    private PlayerInput playerIn;
    private GameView view;

    private GUIGameBoard guiView;

    volatile GameState currentState;
    GameObserver observer;

    boolean doneSendingRays;
    boolean nextRound;
    boolean endRound;

    //track the experimenters previously guessed atoms
    private ArrayList<Point> guessedAtoms;

    //game constructor which inits all objects/variables
    public Game() {
        board = new Board();
        view = new GameView(this);
        playerIn = new PlayerInput();

        guiView = new GUIGameBoard(this);
        observer = guiView;
        //set gameNum to 1 for the first game
        gameNum = 1;

        currentState = GameState.SETTING_ATOMS;
    }

    public Game(Player player) {
        board = new Board();
        this.player1 = player;
    }

    public GameState getCurrentState() {
        return this.currentState;
    }


    public void playGame() {
//        int choice;
        GUIMenu.showMenu();
//        do {
//            view.printStart();
//            choice = playerIn.getPlayerOption();
//            switch (choice) {
//                case 1 -> playSinglePlayerGame();
//                case 2 -> play2PlayerGame();
//            }
//        } while (choice != 3);

    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Board getBoard() {
        return this.board;
    }

    public Board getGuessingBoard() {
        return this.guessingBoard;
    }


//    public void play2PlayerGame() {
//        //create players
//
//        String name1 = GUI_UserInput.askForPlayerName("Player 1 - Enter Name");
//        if (name1 != null && !name1.trim().isEmpty()) {
//            player1 = new Player(name1, true);
//        } else {
//            // Handle cancellation or empty input
//            return; // Optionally, loop back or exit
//        }
//
//        String name2 = GUI_UserInput.askForPlayerName("Player 2 - Enter Name");
//        if (name2 != null && !name2.trim().isEmpty()) {
//            player2 = new Player(name2, false);
//        } else {
//            // Handle cancellation or empty input
//            return; // Optionally, loop back or exit
//        }
//
//        //main game loop
//        while (gameNum <= numGames) {
//            guessedAtoms = new ArrayList<>();
//            //first round player 1 is setter and player 2 is experimenter
//            view.printRound(gameNum);
//            //let setter place 6 atoms
//
//            guiView.boardScreen.showBoard();
//            guiView.boardScreen.boardVisible_ENABLE();
//
//            setAtoms();
//            guiView.boardScreen.boardVisible_DISABLE();
//
//            //let experimenter send rays
//
//            sendRays();
//
//            //let experimenter guess atoms locations
//
//            guessAtoms();
//
//            view.printEntireBoard();
//
//            //switch the roles and increase gameNum
//            switchRoles();
//            gameNum += 1;
//
//            playerIn.resetSentRays();
//            board = new Board();
//            view = new GameView(this);
//        }
//        Player winner = getWinner();
//        view.printStats(player1, player2, winner);
//    }


    public void play2PlayerGame() {
        // Initialize player names
        setupPlayers();

        //set up the initial game state
        currentState = GameState.SETTING_ATOMS;
        gameNum = 1;
        doneSendingRays = false;

        // Main game loop adapted for state management
        while (gameNum <= numGames) {
            guessedAtoms = new ArrayList<>();

            // Display round information
            view.printRound(gameNum);

            // Show the board and enable interaction based on the current game state
            guiView.showBoard(getSetter().getPlayerName(), getExperimenter().getPlayerName(), gameNum);

            // The game progresses through states based on player actions and interactions within the GUI

            // Wait for the current game phase to complete before moving on
//            waitForGameState(GameState.SettingAtoms);
//
//            waitForGameState(GameState.SendingRays);
//            // Conclude the round, switch roles, and reset for the next game
//            waitForGameState(GameState.GuessingAtoms);
//
//            waitForGameState(GameState.GameOver);
            while (currentState != GameState.NEXT_ROUND){
                nextGameState();
            }


            concludeRound();

            // Prepare for the next round
            setupNextRound();
        }

        // Determine and display the winner
        new GUIEndScreen(this).display();
        displayWinner();
    }

    private void setupPlayers() {
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
    }

//    private void waitForGameState(GameState state) {
//        // Implement logic to wait for a specific game state
//        // This could be as simple as a while loop checking the currentState, with proper thread handling to avoid freezing the UI
//        while (currentState == state) {
//            try {
//                Thread.sleep(100); // Sleep a bit before checking again to avoid hogging CPU
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt(); // Restore interrupt status
//                return; // Exit if the thread was interrupted
//            }
//        }
//    }

    private void concludeRound() {
        // Logic to handle the end of a round, such as switching player roles
        switchRoles();
        guiView.disposeBoard();
        guiView.boardVisible_ENABLE();
        view.printEntireBoard(); // Show the final board state for the round
    }

    private void setupNextRound() {
        gameNum++;
        playerIn.resetSentRays(); // Reset any necessary state for the new round
        board = new Board(); // Reset the board
        guessingBoard = new Board();
        view = new GameView(this); // Refresh the view
        currentState = GameState.SETTING_ATOMS; // Reset the game state for the new round
        doneSendingRays = false;
        nextRound = false;
        endRound = false;
    }

    private void displayWinner() {
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

        //TODO finish difficulties
        AiPlayer aiPlayer = new AiPlayer(true, 1, this.board);
        player2 = aiPlayer;

        currentState = GameState.SETTING_ATOMS;
        gameNum = 1;
        doneSendingRays = false;

        while (gameNum <= numGames) {
            aiPlayer.setNewBoard(this.board);
            guessedAtoms = new ArrayList<>();
            guiView.showBoard(getSetter().getPlayerName(), getExperimenter().getPlayerName(), gameNum);
            view.printRound(gameNum);

            if (aiPlayer.isSetter()) {
                aiPlayer.ai_setAtoms(this.numAtoms);

                while (currentState != GameState.NEXT_ROUND){
                    nextGameState();
                }
            }
            else {
                endRound = false;
                while (currentState != GameState.SENDING_RAYS) {
                    nextGameState();
                }

                ArrayList<Integer> rays = aiPlayer.ai_sendRays();

                aiSendRays(rays);

                currentState = GameState.AI_SENDING_RAYS;
                while (currentState != GameState.GUESSING_ATOMS){
                    nextGameState();
                }
                ArrayList<Point> guesses = aiPlayer.ai_guessAtoms(this.numAtoms);
                System.out.println(guesses.size() + " ================================================");
                aiGuessAtoms(guesses);
                currentState = GameState.AI_GUESSING_ATOMS;

                while (currentState != GameState.GAME_OVER) {
                    nextGameState();
                }
                observer.update();
                guiView.refreshBoard();
            }

            while (currentState != GameState.NEXT_ROUND) {
                nextGameState();
            }

            concludeRound();
            setupNextRound();
        }

        displayWinner();
        new GUIEndScreen(this).display();
    }

    private void aiGuessAtoms(ArrayList<Point> guesses) {
        Iterator<Point> iterator = guesses.iterator();
        Timer timer = new Timer(1000, null); // Delay of 1000 milliseconds (1 second)
        timer.addActionListener(e -> {
            if (!iterator.hasNext()) {
                timer.stop(); // Stop the timer if there are no more actions
            } else {
                Point action = iterator.next();

                guessAtom(action.x, action.y);
                atomOnGuessBoard(action.x, action.y);

                guiView.refreshBoard(); // Refresh the GUI to show the change
                observer.update(); // Notify observers of the update
            }
        });

        timer.setInitialDelay(0); // Start immediately
        timer.start(); // Start the timer
    }

    private void aiSendRays(ArrayList<Integer> rays) {
        Iterator<Integer> iterator = rays.iterator();
        Timer timer = new Timer(1000, null); // Delay of 1000 milliseconds (1 second)
        timer.addActionListener(e -> {
            if (!iterator.hasNext()) {
                timer.stop(); // Stop the timer if there are no more actions
            } else {
                Integer action = iterator.next();

                sendRay(action);

                guiView.refreshBoard(); // Refresh the GUI to show the change
                observer.update(); // Notify observers of the update
            }
        });

        timer.setInitialDelay(0); // Start immediately
        timer.start(); // Start the timer
    }

    private void atomOnGuessBoard(int x, int y) {
        guessingBoard.placeAtom(x, y);
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

    //    public void setAtoms() {
//        for (int i = 0; i < numAtoms; i++) {
//            System.out.print(getSetter().getPlayerName() + " - (Setter): ");
//            setAtom(true);
//            view.printEntireBoard();
//        }
//    }


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
        getExperimenter().raySent();
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

    @Override
    public void onAtomPlaced(int x, int y) {
        if (currentState == GameState.SETTING_ATOMS) {
            placeAtom(x, y);
            guiView.refreshBoard();
            nextGameState();
        }
    }

    @Override
    public void onAtomRemoved(int x, int y) {
        if (currentState == GameState.SETTING_ATOMS) {
            removeAtom(x, y);
            guiView.refreshBoard();
        }
    }

    @Override
    public void onRaySent(int number) {
        if (currentState == GameState.SENDING_RAYS) {
            sendRay(number);
            guiView.refreshBoard();
        }
    }

    @Override
    public void onFinishRays() {
        this.doneSendingRays = true;
        nextGameState();
    }

    @Override
    public void onFinishRound() {
        this.nextRound = true;
        nextGameState();
    }

    @Override
    public void onAI_endRound() {
        this.endRound = true;
        nextGameState();
    }

    @Override
    public void onAtomGuess(int x, int y) {
        guessAtom(x, y);
        guessingBoard.placeAtom(x, y);
        nextGameState();
    }

    private void nextGameState() {
        switch (currentState) {
            case SETTING_ATOMS -> {
                if (board.getNumAtomsPlaced() >= 6) {
                    currentState = GameState.SENDING_RAYS;
                    guiView.boardVisible_ENABLE();
                }
            }
            case SENDING_RAYS, AI_SENDING_RAYS -> {
                if (doneSendingRays) {
                    currentState = GameState.GUESSING_ATOMS;
                    guessingBoard = new Board();
                }
            }
            case GUESSING_ATOMS -> {
                if (guessingBoard.getNumAtomsPlaced() >= 6) {
                    currentState = GameState.GAME_OVER;
                }
            }
            case AI_GUESSING_ATOMS -> {
                if (endRound) {
                    currentState = GameState.IDLE;
                }
            }
            case IDLE -> currentState = GameState.GAME_OVER;

            case GAME_OVER -> {
                if (nextRound) {
                    currentState = GameState.NEXT_ROUND;
                }
            }
            case NEXT_ROUND -> {
            }
        }
    }

}
