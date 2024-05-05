package model;

/**
 * Represents a player in a game, tracking their name, role, score, and game-specific statistics.
 */
public class Player {

    private final String playerName;
    private boolean isSetter;
    private int score;
    private int numSentRays;
    private int numCorrectAtoms;

    /**
     * Constructs a Player with a specified name and role.
     * @param playerName the name of the player
     * @param isSetter the initial role of the player; true if the player is a setter, false otherwise
     */
    public Player(String playerName, boolean isSetter){
        this.playerName = playerName;
        this.isSetter = isSetter;
        this.score = 0;
    }

    /**
     * Returns the player's name.
     * @return the name of the player
     */
    public String getPlayerName(){
        return this.playerName;
    }

    /**
     * Checks if the player is currently a setter.
     * @return true if the player is a setter, false otherwise
     */
    public boolean isSetter(){
        return isSetter;
    }
    /**
     * Sets the player's role to setter.
     */
    public void setSetter(){
        this.isSetter = true;
    }

    /**
     * Sets the player's role to experimenter.
     */
    public void setExperimenter(){
        this.isSetter = false;
    }

    /**
     * Updates the player's score by a specified amount.
     * @param n the amount to add to the player's score
     */
    public void updateScore(int n){
        this.score += n;
    }

    /**
     * Returns the current score of the player.
     * @return the current score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Increments the count of correctly identified atoms by the player.
     */
    public void correctAtom() {
        this.numCorrectAtoms += 1;
    }

    /**
     * Decrements the count of correctly identified atoms due to a removed guess
     */
    public void removeCorrectAtom() {
        this.numCorrectAtoms -= 1;
    }

    /**
     * Returns the total number of correctly identified atoms by the player.
     * @return number of correct atoms identified
     */
    public int getNumCorrectAtoms() {
        return this.numCorrectAtoms;
    }

    /**
     * Increments the number of rays the player has sent during the game.
     */
    public void raySent() {
        numSentRays += 1;
    }

    /**
     * Returns the total number of rays sent by the player.
     * @return the number of rays sent
     */
    public int getNumSentRays() {
        return this.numSentRays;
    }
}

