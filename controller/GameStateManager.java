package controller;


import model.Board;
import view.GUIGameBoard;

/**
 * Manages the game states and transitions based on game events.
 */
public class GameStateManager {

    private volatile GameState currentState;
    private Board board;
    private Board guessingBoard;
    private final GUIGameBoard guiView;


    private boolean doneSendingRays;
    private boolean doneSetting;
    private boolean doneGuessing;
    private boolean nextRound;
    private boolean endRound;
    private boolean aiSending;
    private boolean isSinglePlayer;



    public GameStateManager(Board board, Board guessingBoard, GUIGameBoard guiView) {
        this.currentState = GameState.SETTING_ATOMS;
        this.board = board;
        this.guessingBoard = guessingBoard;
        this.guiView = guiView;
        aiSending = false;
    }

    /**
     * Returns the current game state.
     */
    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState state) {
        this.currentState = state;
    }

    /**
     * Manages transitions between game states based on the current state and conditions.
     */
    public void updateGameState() {
//        System.out.println(currentState);
        switch (currentState) {
            case SETTING_ATOMS -> {

                if (board.getNumAtomsPlaced() >= 6) {
                    if (isSinglePlayer) currentState = GameState.SENDING_RAYS;
                    else currentState = GameState.GUESSING_ATOMS;
                }
//                if (doneSetting) {
//                    currentState = GameState.GUESSING_ATOMS;
//                }
            }
            case SENDING_RAYS, AI_HAS_SENT_RAYS -> {
                if (doneSendingRays) {
                    currentState = GameState.GUESSING_ATOMS;
                }
            }
            case GUESSING_ATOMS -> {
                if (guessingBoard.getNumAtomsPlaced() >= 6) {
                    currentState = GameState.GAME_OVER;
                }
//                if (doneGuessing) {
//                    currentState = GameState.GAME_OVER;
//                }
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
    public void setDoneSetting(boolean b) {
        this.doneSetting = b;
    }

    public void setDoneGuessing(boolean b) {
        this.doneGuessing = b;
    }

    public void setSinglePlayer(boolean b) {
        this.isSinglePlayer = b;
    }

    public boolean isSinglePlayer() {
        return this.isSinglePlayer;
    }


    /**
     * Signals that rays have finished being sent by the experimenter.
     */
    public void setDoneSendingRays(boolean b) {
        this.doneSendingRays = b;
    }

    /**
     * Signals that the current round has ended and the next one should begin.
     */
    public void setNextRound(boolean b) {
        this.nextRound = b;
    }

    /**
     * Signals the end of AI's round.
     */
    public void setEndRound(boolean b) {
        this.endRound = b;
    }

    /**
     * Sets whether ai is currently sending rays.
     */
    public void setAiSending(boolean b) {
        this.aiSending = b;
    }

    public boolean isAiSending() {
        return this.aiSending;
    }

    /**
     * Resets the state for a new game or round.
     */
    public void resetForNewGame(Board gameBoard, Board guessingBoard) {
        currentState = GameState.SETTING_ATOMS;
        doneSendingRays = false;
        nextRound = false;
        endRound = false;
        doneSetting = false;
        doneGuessing = false;

        this.board = gameBoard;
        this.guessingBoard = guessingBoard;
    }
}

