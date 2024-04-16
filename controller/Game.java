package controller;

import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

//controller part of project which runs main logic
//updates model and view
public class Game implements GUIInputListener {

    //players objects which will hold necessary information about players
    private Player player1;
    private Player player2;

    private AiPlayer aiPlayer;

    //board object which holds logic to create the game
    private Board board;
    private Board guessingBoard;

    //variable to allow for two
    private int gameNum;
    private final int NUM_GAMES = 2;
    public final int NUM_ATOMS = 6;

    //player controller class to allow game to get necessary user input
    private PlayerInput playerIn;
    private GameView view;

    private GameMusic music;
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
        guessingBoard = new Board();
        view = new GameView(this);
        playerIn = new PlayerInput();
        music = new GameMusic();

        guiView = new GUIGameBoard(this);
        observer = guiView;
        //set gameNum to 1 for the first game
        gameNum = 1;

        currentState = GameState.SETTING_ATOMS;
    }

    //second constructor for testing purposes
    public Game(Player player) {
        board = new Board();
        this.player1 = player;
    }

    public GameState getCurrentState() {
        return this.currentState;
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

    public void play2PlayerGame() {
        playNextSound();

        // Initialize player names
        setupPlayer_2Player();
        music.playMusic("blackboxgamemusic.wav");

        //set up the initial game state
        currentState = GameState.SETTING_ATOMS;
        gameNum = 1;
        doneSendingRays = false;

        // Main game loop adapted for state management
        while (gameNum <= NUM_GAMES) {
            guessedAtoms = new ArrayList<>();

            // Display round information
//            view.printRound(gameNum);

            // Show the board and enable interaction based on the current game state
            guiView.showBoard(getSetter().getPlayerName(), getExperimenter().getPlayerName(), gameNum);

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
        music.stopMusic();
    }

    public void test() {
        board.placeAtom(5, 4);
        board.sendRay(19);
        board.sendRay(28);
        board.sendRay(10);
        board.sendRay(48);
        board.sendRay(53);
        currentState = GameState.GAME_OVER;
        player1 = new Player("John", false);
        player2 = new Player("erw", true);
        guiView.showBoard("erw", "erw", 0);
    }

    public void playSinglePlayerGame() {
        playNextSound();
        setupPlayers_1Player();
        music.playMusic("blackboxgamemusic.wav");



        currentState = GameState.SETTING_ATOMS;
        gameNum = 1;
        doneSendingRays = false;

        while (gameNum <= NUM_GAMES) {
            aiPlayer.setNewBoard(this.board);
            guessedAtoms = new ArrayList<>();
            guiView.showBoard(getSetter().getPlayerName(), getExperimenter().getPlayerName(), gameNum);
            view.printRound(gameNum);

            if (aiPlayer.isSetter()) {
                aiPlayer.ai_setAtoms(this.NUM_ATOMS);

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
                ArrayList<Point> guesses = aiPlayer.ai_guessAtoms(this.NUM_ATOMS);
                aiGuessAtoms(guesses);
                currentState = GameState.AI_GUESSING_ATOMS;

                while (currentState != GameState.GAME_OVER) {
                    System.out.println(currentState);
                    nextGameState();
                }
                observer.update();
                guiView.refreshBoard();
            }

            while (currentState != GameState.NEXT_ROUND) {
                System.out.println(currentState);
                nextGameState();
            }

            concludeRound();
            setupNextRound();
        }

        displayWinner();
        new GUIEndScreen(this).display();
    }

    private void setupPlayer_2Player() {
        setupPlayer(1);
        setupPlayer(2);
    }
    private void setupPlayers_1Player() {
        setupPlayer(1);
        AiPlayer ai = new AiPlayer(false, GUI_UserInput.getAIDifficulty(), this.board);
        playNextSound();
        player2 = ai;
        aiPlayer = ai;
        switchRoles();
    }

    private void setupPlayer(int playerNum) {
        String name = GUI_UserInput.askForPlayerName("Player " + playerNum + " - Enter Name");
        playNextSound();
        if (name == null || name.trim().isEmpty()) {
            name = "Player " + playerNum;
        }
        if (playerNum == 1) {
            player1 = new Player(name, true);
        }
        else {
            player2 = new Player(name, false);
        }
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

    private void playNextSound() {
        music.playSound("sfx/next_sound.wav");
    }

    private void displayWinner() {
        Player winner = getWinner();
        view.printStats(player1, player2, winner);
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
//                music.playSound("sfx/atom_place.wav");
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



    public void guessAtom(int x, int y) {
        Point p = new Point(x, y);
        guessedAtoms.add(p);
        if (!(board.getBoardPosition(x, y) instanceof Atom)) getExperimenter().updateScore(5);
        else getExperimenter().correctAtom();
        music.playSound("sfx/atom_place.wav");
    }

    public void placeAtom(int x, int y) {
        board.placeAtom(x, y);
        music.playSound("sfx/atom_place.wav");
    }

    public void removeAtom(int x, int y) {
        board.removeAtom(x, y);
        music.playSound("sfx/remove_atom.wav");
    }

    public void sendRay(int input) {
        if (board.sendRay(input)) {
            getExperimenter().updateScore(1);
        } else {
            getExperimenter().updateScore(2);
        }
        getExperimenter().raySent();
        music.playSound("sfx/send_ray.wav");
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
        view.printEntireBoard();
        nextGameState();
    }

    @Override
    public void onMainMenuToggle() {
        GUIMenu.showMenu();
        music.stopMusic();
    }

}


