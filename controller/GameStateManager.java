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

    /**
     * Returns current game state
     * @param state GameState enum the game is currently in
     */
    public void setCurrentState(GameState state) {
        this.currentState = state;
    }

    /**
     * Move game state to next state
     */
    public void setNextState() {
        this.nextState = true;
        updateGameState();
    }

    /**
     * Return whether the game is currently running
     * @return true if running false otherwise
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * Sets whether AI is currently sending rays
     */
    public void setAiSending() {
        this.aiSending = true;
    }

    /**
     * Sets AI to not sending rays
     */
    public void setAiNotSending() {
        this.aiSending = false;
    }

    /**
     * Returns whether AI is sending rays
     * @return true if sending rays false otherwise
     */
    public boolean isAiSending() {
        return this.aiSending;
    }

    /**
     * Resets the state for a new game or round.
     */
    public void resetForNewGame() {
        initState();
        nextState = false;
    }

    /**
     * Sets game state to first state which is SETTING_ATOMS
     */
    public void initState() {
        this.setCurrentState(GameState.SETTING_ATOMS);
    }

    /**
     * Set the game to a single player game
     */
    public void setIsSinglePlayer() {
        this.isSinglePlayer = true;
    }

    /**
     * Stop the game running
     */
    public void stop() {
        this.isRunning = false;
    }

    /**
     * Manages transitions between game states based on the current state and conditions.
     */
    public void updateGameState() {
        switch (currentState) {
            case SETTING_ATOMS -> {
                if (nextState && canAdvanceFromSetting()) {
                    nextState = false;
                    if (isSinglePlayer) currentState = GameState.SENDING_RAYS;
                    else currentState = GameState.GUESSING_ATOMS;
                }
            }
            case SENDING_RAYS, AI_HAS_SENT_RAYS -> {
                if (nextState && canAdvanceFromRays()) {
                    nextState = false;
                    currentState = GameState.GUESSING_ATOMS;
                }
            }
            case GUESSING_ATOMS, AI_GUESSING_ATOMS -> {
                if (nextState && canAdvanceFromGuessing()) {
                    nextState = false;
                    currentState = GameState.IDLE;
                }
            }
            case IDLE -> {
                nextState = false;
                currentState = GameState.GAME_OVER;
                game.refreshBoard(); // call a refresh on the board to show full game picture
            }
            case GAME_OVER -> {
                if (nextState) {
                    nextState = false;
                    currentState = GameState.NEXT_ROUND;
                }
            }
            case NEXT_ROUND -> {
            }
        }
    }

    /**
     * Checks if the game can advance state by checking if criteria to advance is met
     * @return true if can advance
     */
    public boolean canAdvanceState() {
        return canAdvanceFromGuessing() || canAdvanceFromSetting() || currentState == GameState.GAME_OVER
                || canAdvanceFromRays();
    }

    /**
     * Checks if game can advance from setting state
     * @return true if conditions allow game to move state
     */
    public boolean canAdvanceFromSetting() {
        return currentState == GameState.SETTING_ATOMS && game.getBoard().getNumAtomsPlaced() >= 6;
    }

    /**
     * Checks if game can advance from guessing state
     * @return true if conditions allow game to move state
     */
    public boolean canAdvanceFromGuessing() {
        return (currentState == GameState.GUESSING_ATOMS || currentState == GameState.AI_GUESSING_ATOMS)
                && game.getGuessingBoard().getNumAtomsPlaced() >= 6;
    }

    /**
     * Checks if the game can advance from the sending ray state
     * @return true if game can advance false otherwise
     */
    public boolean canAdvanceFromRays() {
        return (currentState == GameState.AI_HAS_SENT_RAYS || (currentState == GameState.SENDING_RAYS && !isAiSending()));
    }
}

