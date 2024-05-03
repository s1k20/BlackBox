package controller;

/**
 * Manages the game states and transitions based on game events.
 */
public class GameStateManager {

    private volatile GameState currentState;
    private final Game game;

    private boolean aiSending;
    private boolean isSinglePlayer;

    private boolean nextState;
    private boolean isRunning;

    public GameStateManager(Game game) {
        this.currentState = GameState.SETTING_ATOMS;
        this.game = game;

        aiSending = false;
        isSinglePlayer = false;

        nextState = false;
        isRunning = true;
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
//    public void updateGameState() {
//        switch (currentState) {
//            case SETTING_ATOMS -> {
//
//                if (board.getNumAtomsPlaced() >= 6) {
//                    if (isSinglePlayer) currentState = GameState.SENDING_RAYS;
//                    else currentState = GameState.GUESSING_ATOMS;
//                }
////                if (doneSetting) {
////                    currentState = GameState.GUESSING_ATOMS;
////                }
//            }
//            case SENDING_RAYS, AI_HAS_SENT_RAYS -> {
//                if (doneSendingRays) {
//                    currentState = GameState.GUESSING_ATOMS;
//                }
//            }
//            case GUESSING_ATOMS -> {
//                if (guessingBoard.getNumAtomsPlaced() >= 6) {
//                    currentState = GameState.GAME_OVER;
//                }
////                if (doneGuessing) {
////                    currentState = GameState.GAME_OVER;
////                }
//            }
//            case AI_GUESSING_ATOMS -> {
//                if (endRound) {
//                    currentState = GameState.IDLE;
//                }
//            }
//            case IDLE -> currentState = GameState.GAME_OVER;
//            case GAME_OVER -> {
//                if (nextRound) {
//                    currentState = GameState.NEXT_ROUND;
//                }
//            }
//            case NEXT_ROUND -> {
//            }
//        }
//    }

    public void updateGameState() {
//        System.out.println(currentState);
        switch (currentState) {
            case SETTING_ATOMS -> {
                if (nextState) {
                    if (isSinglePlayer) currentState = GameState.SENDING_RAYS;
                    else currentState = GameState.GUESSING_ATOMS;
                    nextState = false;
                }
            }
            case SENDING_RAYS, AI_HAS_SENT_RAYS -> {
                if (nextState) {
                    currentState = GameState.GUESSING_ATOMS;
                    nextState = false;
                }
            }
            case GUESSING_ATOMS, AI_GUESSING_ATOMS -> {
                if (nextState) {
                    currentState = GameState.IDLE;
                    nextState = false;
                }
            }
            case IDLE -> {
                currentState = GameState.GAME_OVER;
                game.refreshBoard();
            }
            case GAME_OVER -> {
                if (nextState) {
                    currentState = GameState.NEXT_ROUND;
                    nextState = false;
                }
            }
            case NEXT_ROUND -> {
            }
        }
    }

    public void setNextState() {
        this.nextState = true;
        updateGameState();
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void stop() {
        this.isRunning = false;
    }


    public void setSinglePlayer(boolean b) {
        this.isSinglePlayer = b;
    }

    public boolean isSinglePlayer() {
        return this.isSinglePlayer;
    }


    /**
     * Sets whether AI is currently sending rays.
     */
    public void setAiSending() {
        this.aiSending = true;
    }

    public void setAiNotSending() {
        this.aiSending = false;
    }

    public boolean isAiSending() {
        return this.aiSending;
    }

    /**
     * Resets the state for a new game or round.
     */
    public void resetForNewGame() {
        currentState = GameState.SETTING_ATOMS;
    }

    public void initState() {
        this.setCurrentState(GameState.SETTING_ATOMS);
    }

    public void setIsSinglePlayer() {
        this.isSinglePlayer = true;
    }

    public boolean canAdvanceFromGuessing() {
        return (currentState == GameState.GUESSING_ATOMS || currentState == GameState.AI_GUESSING_ATOMS)
                && game.getGuessingBoard().getNumAtomsPlaced() >= 6;
    }

    public boolean canAdvanceFromSetting() {
        return currentState == GameState.SETTING_ATOMS && game.getBoard().getNumAtomsPlaced() >= 6;
    }

    public boolean canAdvanceState() {
        return canAdvanceFromGuessing() || canAdvanceFromSetting() || currentState == GameState.GAME_OVER
                || canAdvanceFromRays();
    }

    public boolean canAdvanceFromRays() {
        return (currentState == GameState.AI_HAS_SENT_RAYS || (currentState == GameState.SENDING_RAYS && !isAiSending()));
    }
}

