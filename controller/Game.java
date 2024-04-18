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

    private GameMusic music;
    private GUIGameBoard guiView;
    public GameStateManager stateManager;

    GameObserver observer;

    //game constructor which inits all objects/variables
    public Game() {
        board = new Board();
        guessingBoard = new Board();
        music = new GameMusic();

        guiView = new GUIGameBoard(this);
        observer = guiView;

        //set gameNum to 1 for the first game
        gameNum = 1;

        stateManager = new GameStateManager(board, guessingBoard, guiView);
    }

    //second constructor for testing purposes
    public Game(Player player) {
        board = new Board();
        this.player1 = player;
    }

    public GameState getCurrentState() {
        return this.stateManager.getCurrentState();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getSetter() {
        return player1.isSetter() ? player1 : player2;
    }

    public Player getExperimenter() {
        return !player1.isSetter() ? player1 : player2;
    }

    public Board getBoard() {
        return this.board;
    }

    public Board getGuessingBoard() {
        return this.guessingBoard;
    }

    public void test() {
        board.placeAtom(5, 4);
        board.sendRay(19);
        board.sendRay(28);
        board.sendRay(10);
        board.sendRay(48);
        board.sendRay(53);
        board.sendRay(30);
        stateManager.setCurrentState(GameState.GAME_OVER);
        player1 = new Player("John", false);
        player2 = new Player("erw", true);
        guiView.showBoard("erw", "erw", 0);
    }

    public void play2PlayerGame() {
        initGame_2Player();

        while (gameNum <= NUM_GAMES) {
            System.out.println(getCurrentState());
            guiView.showBoard(getSetter().getPlayerName(), getExperimenter().getPlayerName(), gameNum);
            waitForGameState(GameState.NEXT_ROUND);
            concludeRound();
        }
        finishGame();
    }

    public void playSinglePlayerGame() {
        initGame_1Player();

        while (gameNum <= NUM_GAMES) {
            aiPlayer.setNewBoard(this.board);
            guiView.showBoard(getSetter().getPlayerName(), getExperimenter().getPlayerName(), gameNum);

            if (aiPlayer.isSetter()) aiSetterRound();
            else aiExperimenterRound();

            waitForGameState(GameState.NEXT_ROUND);
            concludeRound();
        }
        finishGame();
    }

    private void aiExperimenterRound() {
        stateManager.setEndRound(false);
        stateManager.setAiSending(true);

        waitForGameState(GameState.SENDING_RAYS);

        ArrayList<Integer> rays = aiPlayer.ai_sendRays();

        doAIActions(rays);

        waitForGameState(GameState.GUESSING_ATOMS);
        ArrayList<Point> guesses = aiPlayer.ai_guessAtoms(this.NUM_ATOMS);
        doAIActions(guesses);
        stateManager.setCurrentState(GameState.AI_GUESSING_ATOMS);

        waitForGameState(GameState.GAME_OVER);
        observer.update();
        guiView.refreshBoard();
    }

    private void setupPlayer_2Player() {
        setupPlayer(1);
        setupPlayer(2);
    }
    private void setupPlayers_1Player() {
        setupPlayer(1);
        AiPlayer ai = new AiPlayer(false, GUI_UserInput.getAIDifficulty(), this.board);
        music.playNextSound();
        player2 = ai;
        aiPlayer = ai;
        switchRoles();
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

        initGame();
    }

    private void initGame () {
        stateManager.setCurrentState(GameState.SETTING_ATOMS);
        gameNum = 1;
        stateManager.setDoneSendingRays(false);
    }

    private void setupPlayer(int playerNum) {
        String name = GUI_UserInput.askForPlayerName("Player " + playerNum + " - Enter Name");
        music.playNextSound();
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

    private void waitForGameState(GameState state) {
        while (stateManager.getCurrentState() != state){
            stateManager.updateGameState();
        }
    }

    private void concludeRound() {
        switchRoles();
        guiView.disposeBoard();
        guiView.boardVisible_ENABLE();
        setupNextRound();
    }

    private void setupNextRound() {
        gameNum++;
        board = new Board();
        guessingBoard = new Board();
        stateManager.resetForNewGame(board, guessingBoard);
    }

    private void finishGame() {
        new GUIEndScreen(this).display();
        music.stopMusic();
    }

    private void doAIActions(ArrayList<?> input) {
        Iterator<?> iterator = input.iterator();
        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            if (!iterator.hasNext()) {
                if (input.get(0) instanceof Integer) {
                    stateManager.setAiSending(false);
                    stateManager.setCurrentState(GameState.AI_HAS_SENT_RAYS);
                }
                timer.stop();
            } else {
                Object action = iterator.next();
                performAction(action);
            }
            guiView.refreshBoard();
            observer.update();
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    private void performAction(Object action) {
        if (action instanceof Integer) {
            sendRay((Integer) action);
        } else if (action instanceof Point point) {
            guessAtom(point.x, point.y);
        }
    }

    private void aiSetterRound() {
        aiPlayer.ai_setAtoms(this.NUM_ATOMS);
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
        if (!(board.getBoardPosition(x, y) instanceof Atom)) getExperimenter().updateScore(5);
        else getExperimenter().correctAtom();
        atomOnGuessBoard(x, y);
        music.playAtomPlace();
    }

    public void removeGuessAtom(int x, int y) {
        if ((board.getBoardPosition(x, y) instanceof Atom)) getExperimenter().removeCorrectAtom();
        else getExperimenter().updateScore(-5);
        removeAtomGuessBoard(x, y);
        music.playAtomRemoved();
    }

    public void sendRay(int input) {
        if (board.sendRay(input)) {
            getExperimenter().updateScore(1);
        } else {
            getExperimenter().updateScore(2);
        }
        getExperimenter().raySent();
        music.playSendRay();
    }

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
        placeAtom(x, y);
        guiView.refreshBoard();
        stateManager.updateGameState();
    }

    @Override
    public void onAtomRemoved(int x, int y) {
        removeAtom(x, y);
        guiView.refreshBoard();

    }

    @Override
    public void onRaySent(int number) {
        sendRay(number);
        guiView.refreshBoard();
    }

    @Override
    public void onFinishRays() {
        stateManager.setDoneSendingRays(true);
        stateManager.updateGameState();
    }

    @Override
    public void onFinishRound() {
        stateManager.setNextRound(true);
        stateManager.updateGameState();
    }

    @Override
    public void onAI_endRound() {
        stateManager.setEndRound(true);
        stateManager.updateGameState();
    }

    @Override
    public void onAtomGuess(int x, int y) {
        guessAtom(x, y);
        stateManager.updateGameState();
    }

    @Override
    public void onAtomGuessRemoved(int x, int y) {
        removeGuessAtom(x, y);
        guiView.refreshBoard();
    }

    @Override
    public void onMainMenuToggle() {
        GUIMenu.showMenu();
        music.stopMusic();
    }

}


