package controller;

import model.*;
import view.*;
import view.GUIBoard.GUIGameScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Game implements GUIInputListener {

    private Board board;
    private Board guessingBoard;

    private int gameNum;
    private final int NUM_GAMES = 2;
    public final int NUM_ATOMS = 6;

    private final GameMusic music;
    private final GUIGameScreen guiView;
    private final GameStateManager stateManager;
    private final PlayerManager playerManager;

    public Game() {
        playerManager = new PlayerManager();
        stateManager = new GameStateManager(this);
        guiView = new GUIGameScreen(this);
        music = new GameMusic();
        board = new Board();
        guessingBoard = new Board();
        gameNum = 1;
    }

    //second constructor for testing purposes
    public Game(Player player) {
        playerManager = new PlayerManager();
        board = new Board();
        music = new GameMusic();
        guessingBoard = new Board();
        guiView = new GUIGameScreen(this);
        stateManager = new GameStateManager(this);
        playerManager.setupPlayer("Bob", 1);
        playerManager.setupPlayer("Jim", 2);
    }

    public GameState getCurrentState() {
        return this.stateManager.getCurrentState();
    }

    public Board getBoard() {
        return this.board;
    }

    public Board getGuessingBoard() {
        return this.guessingBoard;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public GameStateManager getStateManager() {
        return this.stateManager;
    }

    public void play2PlayerGame() {
        initGame_2Player();

        while (gameNum <= NUM_GAMES) {
            guiView.showBoard(playerManager.getSetterName(), playerManager.getExperimenterName(), gameNum);
            waitForGameState(GameState.NEXT_ROUND);
            if (!stateManager.isRunning()) {
                return;
            }
            concludeRound();
        }
        finishGame();
    }

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

    private void setupPlayer_2Player() {
        setupPlayer(1);
        setupPlayer(2);
    }
    private void setupPlayers_1Player() {
        setupPlayer(1);
        music.playNextSound();
        playerManager.setupAiPlayer(GUIUserInput.getAIDifficulty(), this.board);
    }

    private void initGame_2Player() {
        music.playNextSound();
        setupPlayer_2Player();
        music.playMusic();

        initGame();
    }

    private void initGame_1Player() {
        music.playNextSound();
        setupPlayers_1Player();
        music.playMusic();

        stateManager.setIsSinglePlayer();
        initGame();
    }

    private void initGame () {
        gameNum = 1;
        stateManager.initState();
    }

    private void setupPlayer(int playerNum) {
        String name = GUIUserInput.askForPlayerName("Player " + playerNum + " - Enter Name");
        music.playNextSound();
        playerManager.setupPlayer(name, playerNum);
    }

    private void waitForGameState(GameState state) {
        while (stateManager.getCurrentState() != state){
            stateManager.updateGameState();
        }
    }

    private void concludeRound() {
        int numRayMarkers = board.getRayMarkerNumbers().size();
        playerManager.endRound(numRayMarkers);
        guiView.endRound();
        setupNextRound();
    }

    private void setupNextRound() {
        gameNum++;
        board = new Board();
        guessingBoard = new Board();
        stateManager.resetForNewGame();
    }

    private void finishGame() {
        new GUIEndScreen(this).display();
        music.stopMusic();
    }

    private void performAction(Object action) {
        if (action instanceof Integer) {
            sendRay((Integer) action);
        } else if (action instanceof Point point) {
            guessAtom(point.x, point.y);
        }
    }

    private void doAIActions(ArrayList<?> input) {
        Iterator<?> iterator = input.iterator();
        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            if (!iterator.hasNext()) {
                if (input.get(0) instanceof Integer) {
                    stateManager.setAiNotSending();
                    stateManager.setCurrentState(GameState.AI_HAS_SENT_RAYS);
                }
                timer.stop();
            } else {
                Object action = iterator.next();
                performAction(action);
            }
            guiView.refreshBoard();
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    private void aiSetterRound() {
        playerManager.setAiAtoms_Set(this.NUM_ATOMS);
        stateManager.setCurrentState(GameState.GUESSING_ATOMS);
        waitForGameState(GameState.NEXT_ROUND);
    }

    public void placeAtom(int x, int y) {
        board.placeAtom(x, y);
        music.playAtomPlace();
    }

    public void removeAtom(int x, int y) {
        board.removeAtom(x, y);
        music.playAtomRemoved();
    }

    private void atomOnGuessBoard(int x, int y) {
        guessingBoard.placeAtom(x, y);
    }

    private void removeAtomGuessBoard(int x, int y) {
        guessingBoard.removeAtom(x, y);
    }

    public void guessAtom(int x, int y) {
        if (!(board.getBoardPosition(x, y) instanceof Atom)) playerManager.getExperimenter().updateScore(5);
        else playerManager.getExperimenter().correctAtom();
        atomOnGuessBoard(x, y);
        music.playAtomPlace();
    }

    public void removeGuessAtom(int x, int y) {
        if ((board.getBoardPosition(x, y) instanceof Atom)) playerManager.getExperimenter().removeCorrectAtom();
        else playerManager.getExperimenter().updateScore(-5);
        removeAtomGuessBoard(x, y);
        music.playAtomRemoved();
    }

    public void sendRay(int input) {
        board.sendRay(input);
        playerManager.getExperimenter().raySent();
        music.playSendRay();
    }

    @Override
    public void onAtomPlaced(int x, int y) {
        if (stateManager.getCurrentState() == GameState.SETTING_ATOMS && board.getNumAtomsPlaced() < 6) {
            placeAtom(x, y);
            guiView.refreshBoard();
        }
    }

    @Override
    public void onAtomRemoved(int x, int y) {
        if (stateManager.getCurrentState() == GameState.SETTING_ATOMS) {
            removeAtom(x, y);
            guiView.refreshBoard();
        }
    }

    @Override
    public void onRaySent(int number) {
        if ((stateManager.getCurrentState() == GameState.SENDING_RAYS && !stateManager.isAiSending()) || stateManager.getCurrentState() == GameState.GUESSING_ATOMS) {
            sendRay(number);
            guiView.refreshBoard();
        }
    }

    @Override
    public void onAtomGuess(int x, int y) {
        if ((stateManager.getCurrentState() == GameState.GUESSING_ATOMS || stateManager.getCurrentState() == GameState.AI_GUESSING_ATOMS) && guessingBoard.getNumAtomsPlaced() < 6) {
            guessAtom(x, y);
            stateManager.updateGameState();
        }
    }

    @Override
    public void onAtomGuessRemoved(int x, int y) {
        if (stateManager.getCurrentState() == GameState.GUESSING_ATOMS || stateManager.getCurrentState() == GameState.AI_GUESSING_ATOMS) {
            removeGuessAtom(x, y);
            guiView.refreshBoard();
        }
    }

    @Override
    public void onMainMenuToggle() {
        stateManager.setCurrentState(GameState.NEXT_ROUND);
        stateManager.stop();
        GUIMenu.showMenu();
        music.stopMusic();
    }

    @Override
    public void advanceGameState() {
        stateManager.setNextState();
    }

    @Override
    public void refreshBoard() {
        guiView.refreshBoard();
    }
}


