package controller;

import model.*;
import view.*;
import view.GUIBoard.GUIGameScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is the central controller in MVC architecture
 * Handles all communication between Model (Board) and View (GUIGameScreen)
 * and maintains proper architecture through taking requests from View and
 * passing them to the Model through a listener which is implemented
 * into the class called GUIInputListener
 */
public class Game implements GUIInputListener {

    // Board objects which act as the model of the software
    private Board board;
    private Board guessingBoard; // Board to handle experimenter guesses to avoid overwriting original board

    // GUIGameScreen which is central to the view of the software
    public final GUIGameScreen guiView;

    private final GameStateManager stateManager; // Class which solely handles game state and progressing through a game
    private final PlayerManager playerManager; // Class to handle all actions concerning players
    private final GameMusic music; // Class to handle music and SFX

    private int gameNum;
    private final int NUM_GAMES = 2;
    public final int NUM_ATOMS = 6;

    /**
     * Games constructor which handles initialising all instance variables
     * through creating instances of classes necessary for the game
     */
    public Game() {
        playerManager = new PlayerManager();
        stateManager = new GameStateManager(this);
        guiView = new GUIGameScreen(this);
        music = new GameMusic();
        board = new Board();
        guessingBoard = new Board();
        gameNum = 1;
    }

    /**
     * Second constructor used in simulating a game for testing purposes
     * @param player a test player for ability to test sending rays and placing atoms
     */
    public Game(Player player) {
        playerManager = new PlayerManager();
        board = new Board();
        music = new GameMusic();
        guessingBoard = new Board();
        guiView = new GUIGameScreen(this);
        stateManager = new GameStateManager(this);
        playerManager.setupPlayer(player.getPlayerName(), 1);
        playerManager.setupPlayer("Test2", 2);
    }

    /**
     * Retrieves current game state from the state manager
     * @return an Enum of type GameState
     */
    public GameState getCurrentState() {
        return this.stateManager.getCurrentState();
    }

    /**
     * Gets board class which is used as main playing board
     * @return Board class instance
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Gets board class which is used for guessing purposes in the game
     * @return Board class instance
     */
    public Board getGuessingBoard() {
        return this.guessingBoard;
    }

    /**
     * Gets PlayerManager object which encapsulates all details regarding players
     * @return PlayerManager class instance
     */
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    /**
     * Gets StateManager object which holds all details regarding the games current state
     * @return StateManager class instance
     */
    public GameStateManager getStateManager() {
        return this.stateManager;
    }

    /**
     * Method which handles playing a 2 player game (two actual users)
     * Method initialises players for playing against each other and enters a loop to
     * simulate 2 games were both players play as both setter and experimenter
     * Method calls other methods to properly finish one round and also finish the game e.g.
     * switching rolls, displaying final scores
     */
    public void play2PlayerGame() {
        initGame_2Player();

        while (gameNum <= NUM_GAMES) {
            guiView.showBoard(playerManager.getSetterName(), playerManager.getExperimenterName(), gameNum);
            waitForGameState(GameState.NEXT_ROUND);
            if (!stateManager.isRunning()) return;
            concludeRound();
        }
        finishGame();
    }

    /**
     * Method for initialising a two player game by
     * setting up players and also initialising the game
     */
    private void initGame_2Player() {
        music.playNextSound();
        setupPlayer_2Player();
        music.playMusic();
        initGame();
    }

    /**
     * Method for setting up 2 players which call
     * setupPlayer twice for both actual payers
     */
    private void setupPlayer_2Player() {
        setupPlayer(1);
        setupPlayer(2);
    }

    /**
     * Method for setting up a single player (real user) which retrieves
     * users desired name and passes to playerManager to create instance of player
     * @param playerNum an integer signifying what player is being created, either player 1 or player 2
     */
    private void setupPlayer(int playerNum) {
        if (playerNum <= 0 || playerNum > 2) throw new IllegalArgumentException("Invalid player number");

        String name = GUIUserInput.askForPlayerName("Player " + playerNum + " - Enter Name");
        music.playNextSound();
        playerManager.setupPlayer(name, playerNum);
    }

    /**
     * initialising game by setting current gameNum to 1 for first game
     * and also setting stateManager to the init state
     */
    private void initGame () {
        gameNum = 1;
        stateManager.initState();
    }

    /**
     * Method which handles playing a 1 player game (second player is the computer)
     * Initialises players for game through creating one actual user and one instance of AIPlayer
     * Method then properly finishes rounds and finishes game
     */
    public void playSinglePlayerGame() {
        initGame_1Player();

        while (gameNum <= NUM_GAMES) {
            playerManager.setAiPlayerBoard(this.board);

            guiView.showBoard(playerManager.getSetterName(), playerManager.getExperimenterName(), gameNum);

            if (playerManager.isAiSetter()) aiSetterRound();
            else aiExperimenterRound();

            if (!stateManager.isRunning()) {
                return;
            }

            waitForGameState(GameState.NEXT_ROUND);
            concludeRound();
        }
        finishGame();
    }

    /**
     * Method for initialising single player game by setting up
     * players for single player and also intitialising the game
     */
    private void initGame_1Player() {
        music.playNextSound();
        setupPlayers_1Player();
        music.playMusic();

        stateManager.setIsSinglePlayer();
        initGame();
    }

    /**
     * Method for setting up 1 player and one
     * AIPlayer for single player game
     */
    private void setupPlayers_1Player() {
        setupPlayer(1);
        music.playNextSound();
        playerManager.setupAiPlayer(GUIUserInput.getAIDifficulty(), this.board);
    }

    /**
     * Method which simulates AI Playing roll of experimenter which
     * includes both sending rays and guessing atoms
     * Method includes various waitForGameState() calls as this allows user
     * to control pace of the game and not advance to AIPlayer doing everything consecutively
     * To simulate an actual person, AI guessing/sending rays are handles by a separate method
     * to introduce delay between each atom guess/ray send
     */
    private void aiExperimenterRound() {
        stateManager.setAiSending();

        waitForGameState(GameState.SENDING_RAYS);

        ArrayList<Integer> rays = playerManager.getAiRays();

        doAIActions(rays);

        waitForGameState(GameState.GUESSING_ATOMS);
        ArrayList<Point> guesses = playerManager.getAiAtoms_Guess(NUM_ATOMS);
        doAIActions(guesses);
        stateManager.setCurrentState(GameState.AI_GUESSING_ATOMS);

        waitForGameState(GameState.GAME_OVER);
        guiView.refreshBoard();
    }

    /**
     * Method to simulate a round where AI takes roll setter and simply
     * has to set 6 random atoms onto the board
     */
    private void aiSetterRound() {
        playerManager.setAiAtoms_Set(this.NUM_ATOMS);
        stateManager.setCurrentState(GameState.GUESSING_ATOMS);
        waitForGameState(GameState.NEXT_ROUND);
    }

    /**
     * Method which waits for a desired game state
     * @param state GameState enum which is being waited for
     */
    private void waitForGameState(GameState state) {
        while (stateManager.getCurrentState() != state){
            stateManager.updateGameState();
        }
    }

    /**
     * Method to conclude a round by adding end number of RayMarker to
     * experimenters score, telling the GUI to conclude the round and also then
     * calling to set up the next round
     */
    private void concludeRound() {
        int numRayMarkers = board.getRayMarkerNumbers().size();
        playerManager.endRound(numRayMarkers);
        guiView.endRound();
        setupNextRound();
    }

    /**
     * Method to advance game by making new board for new round
     * and resetting stateManager for the new round
     */
    private void setupNextRound() {
        gameNum++;
        board = new Board();
        guessingBoard = new Board();
        stateManager.resetForNewGame();
    }

    /**
     * Method to properly end game and display the scores of the game
     */
    private void finishGame() {
        new GUIEndScreen(this).display();
        music.stopMusic();
    }

    /**
     * Method to carry out AI actions and creating a visible delay to create
     * illusion of someone actually playing. Makes use of Swing timer to ensure
     * all delays are on the same thread as GUI to avoid behaviour issues
     * @param input A list of actions to completed by the AI
     */
    private void doAIActions(ArrayList<?> input) {
        // iterator used for cycling through actions
        Iterator<?> iterator = input.iterator();

        // set time to a delay of 1000 ms
        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            if (!iterator.hasNext()) {
                // Once no more actions to execute, check whether arraylist
                // was of type integer (sending rays) if so, tell game state
                // that AI has finished sending rays, so it can properly advance game states
                if (input.get(0) instanceof Integer) {
                    stateManager.setAiNotSending();
                    stateManager.setCurrentState(GameState.AI_HAS_SENT_RAYS);
                }
                timer.stop();
            } else {
                // get object from iterator which is an action, of type Integer (sending rays) or Point (guessing atom)
                Object action = iterator.next();
                performAction(action);
            }
            guiView.refreshBoard();
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    /**
     * Method to carry out the AIPlayers actions, sending and guessing
     * Takes an object of any type, either Point or Integer and will carry out
     * correct action based on the information passed to the method
     * @param action Object which the AIPlayer is trying to carry out
     */
    private void performAction(Object action) {
        if (action instanceof Integer) {
            sendRay((Integer) action);
        } else if (action instanceof Point point) {
            guessAtom(point.x, point.y);
        }
    }

    /**
     * Method which directly interacts with the board to place an atom
     * into a given position given certain x and y coordinate
     * @param x X-coordinate (column index)
     * @param y Y-coordinate (row index)
     */
    public void placeAtom(int x, int y) {
        board.placeAtom(x, y);
        music.playAtomPlace();
    }

    /**
     * Method which directly interacts with the board to remove an atom
     * from a given position given certain x and y coordinate
     * @param x X-coordinate (column index)
     * @param y Y-coordinate (row index)
     */
    public void removeAtom(int x, int y) {
        board.removeAtom(x, y);
        music.playAtomRemoved();
    }

    /**
     * Method which directly interacts with the guessing board to guess an atom
     * into a given position given certain x and y coordinate
     * Method will also update the players score based on whether the atom was incorrect or not
     * Done by comparing it guess to the actual game board
     * @param x X-coordinate (column index)
     * @param y Y-coordinate (row index)
     */
    public void guessAtom(int x, int y) {
        if (!(board.getBoardPosition(x, y) instanceof Atom)) playerManager.getExperimenter().updateScore(5);
        else playerManager.getExperimenter().correctAtom();
        guessingBoard.placeAtom(x, y);
        music.playAtomPlace();
    }

    /**
     * Method which directly interacts with the guessing board to remove
     * an atom guess from a given position given certain x and y coordinate
     * Method will also update a players score accordingly,
     * removing 5 if the guess atom was incorrect according to same position on actual game board
     * @param x X-coordinate (column index)
     * @param y Y-coordinate (row index)
     */
    public void removeGuessAtom(int x, int y) {
        if ((board.getBoardPosition(x, y) instanceof Atom)) playerManager.getExperimenter().removeCorrectAtom();
        else playerManager.getExperimenter().updateScore(-5);
        guessingBoard.removeAtom(x, y);
        music.playAtomRemoved();
    }

    /**
     * Method which handles interaction with board object to send a ray
     * Updates player manager that a ray was sent to allow users to see
     * stats at the end of the game
     * @param input a number which surrounds the board in which the experimenter wants to send a ray
     */
    public void sendRay(int input) {
        board.sendRay(input);
        playerManager.raySent();
        music.playSendRay();
    }
    /**
     * Handles the placement of an atom on the board during the Setting state
     * Method is triggered by an action and will only execute when the game state is
     * SETTING_ATOMS and fewer than 6 atoms have been placed. It places an atom at the
     * specified coordinates and refreshes the GUI board.
     * @param x the x-coordinate where the atom is to be placed.
     * @param y the y-coordinate where the atom is to be placed.
     */
    @Override
    public void onAtomPlaced(int x, int y) {
        if (stateManager.getCurrentState() == GameState.SETTING_ATOMS && board.getNumAtomsPlaced() < 6) {
            placeAtom(x, y);
            guiView.refreshBoard();
        }
    }
    /**
     * Handles the removal of an atom from the board during the Setting state
     * Method is triggered by an action and will only execute when the game state
     * is SETTING_ATOMS. It removes an atom at the specified coordinates and refreshes
     * the GUI board.
     * @param x the x-coordinate where the atom is to be removed.
     * @param y the y-coordinate where the atom is to be removed.
     */
    @Override
    public void onAtomRemoved(int x, int y) {
        if (stateManager.getCurrentState() == GameState.SETTING_ATOMS) {
            removeAtom(x, y);
            guiView.refreshBoard();
        }
    }
    /**
     * Sends a ray identified by the specified number if the game is in the RAY SENDING or
     * GUESSING phase, and it's not the AI's turn. Refreshes the GUI board after sending.
     * @param number the identifier of the ray to be sent.
     */
    @Override
    public void onRaySent(int number) {
        if ((stateManager.getCurrentState() == GameState.SENDING_RAYS && !stateManager.isAiSending())
                || stateManager.getCurrentState() == GameState.GUESSING_ATOMS) {
            sendRay(number);
            guiView.refreshBoard();
        }
    }

    /**
     * Guesses an atom's location at specified coordinates during the GUESSING phase if
     * fewer than 6 atoms are on the guessing board. Updates game state after guessing.
     * @param x the x-coordinate for atom guessing.
     * @param y the y-coordinate for atom guessing.
     */
    @Override
    public void onAtomGuess(int x, int y) {
        if ((stateManager.getCurrentState() == GameState.GUESSING_ATOMS || stateManager.getCurrentState() == GameState.AI_GUESSING_ATOMS) && guessingBoard.getNumAtomsPlaced() < 6) {
            guessAtom(x, y);
            stateManager.updateGameState();
        }
    }

    /**
     * Removes a guessed atom from the specified coordinates during the GUESSING state
     * Refreshes the GUI board after removal
     * @param x the x-coordinate for guessed atom removal
     * @param y the y-coordinate for guessed atom removal
     */
    @Override
    public void onAtomGuessRemoved(int x, int y) {
        if (stateManager.getCurrentState() == GameState.GUESSING_ATOMS || stateManager.getCurrentState() == GameState.AI_GUESSING_ATOMS) {
            removeGuessAtom(x, y);
            guiView.refreshBoard();
        }
    }
    /**
     * Toggles the main menu and stops the current game state. Stops the game music.
     */
    @Override
    public void onMainMenuToggle() {
        stateManager.setCurrentState(GameState.NEXT_ROUND);
        stateManager.stop();
        GUIMenu.showMenu();
        music.stopMusic();
    }

    /**
     * Advances the game to the next state based on game rules and conditions.
     */
    @Override
    public void advanceGameState() {
        stateManager.setNextState();
    }

    /**
     * Refreshes the visual representation of the game board on the GUI.
     */
    @Override
    public void refreshBoard() {
        guiView.refreshBoard();
    }
}


