package controller;

import model.AiPlayer;
import model.Board;
import model.Player;

import java.awt.*;
import java.util.ArrayList;

/**
 * Manages players in the game, including human players and AI players.
 * It handles player roles, updates scores, and manages the state of the AI player's board.
 */
public class PlayerManager {

    private Player player1;
    private Player player2;

    private AiPlayer aiPlayer;

    /**
     * Gets the AI player instance.
     * @return the AI player
     */
    public AiPlayer getAiPlayer() {
        return this.aiPlayer;
    }

    /**
     * Sets the AI player instance.
     * @param aiPlayer the AI player to set
     */
    public void setAiPlayer(AiPlayer aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    /**
     * Sets a new board for the AI player.
     * @param board the board to set for the AI player
     */
    public void setAiPlayerBoard(Board board) {
        this.aiPlayer.setNewBoard(board);
    }

    /**
     * Retrieves the rays sent by the AI player.
     * @return a list of integers representing the rays sent by the AI
     */
    public ArrayList<Integer> getAiRays() {
        return getAiPlayer().ai_sendRays();
    }

    /**
     * Instructs the AI player to set a specified number of atoms on its board.
     * @param numAtoms the number of atoms the AI should set
     */
    public void setAiAtoms_Set(int numAtoms) {
        getAiPlayer().ai_setAtoms(numAtoms);
    }

    /**
     * Instructs the AI player to guess the position of atoms.
     * @param numAtoms the number of atoms to guess
     * @return a list of points where the AI guesses atoms are located
     */
    public ArrayList<Point> getAiAtoms_Guess(int numAtoms) {
        return getAiPlayer().ai_guessAtoms(numAtoms);
    }

    /**
     * Gets the first player.
     * @return the first player
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Sets the first player.
     * @param player1 the first player to set
     */
    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    /**
     * Gets the second player.
     * @return the second player
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Sets the second player.
     * @param player2 the second player to set
     */
    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    /**
     * Returns the player currently assigned the role of the setter.
     * @return the setter player
     */
    public Player getSetter() {
        return player1.isSetter() ? player1 : player2;
    }

    /**
     * Gets the name of the player who is the setter.
     * @return the setter's name
     */
    public String getSetterName() {
        return getSetter().getPlayerName();
    }

    /**
     * Returns the player currently assigned the role of the experimenter.
     * @return the experimenter player
     */
    public Player getExperimenter() {
        return !player1.isSetter() ? player1 : player2;
    }

    /**
     * Gets the name of the player who is the experimenter.
     * @return the experimenter's name
     */
    public String getExperimenterName() {
        return this.getExperimenter().getPlayerName();
    }

    public void raySent() {
        getExperimenter().raySent();
    }

    /**
     * Sets up a player with the provided name and assigns a role based on the player number.
     * @param name the name of the player
     * @param playerNum the player number (1 or 2)
     */
    public void setupPlayer(String name, int playerNum) {
        if (name == null || name.trim().isEmpty()) {
            name = "Player " + playerNum;
        }
        if (playerNum == 1) {
            setPlayer1(new Player(name, true));
        }
        else {
            setPlayer2(new Player(name, false));
        }
    }

    /**
     * Ends the current round, updates the experimenter's score, and switches player roles.
     * @param experimenterScore the score to add to the experimenter
     */
    public void endRound(int experimenterScore) {
        getExperimenter().updateScore(experimenterScore);
        switchRoles();
    }

    /**
     * Sets up an AI player with specified difficulty and assigns it to player two.
     * @param difficulty the difficulty level of the AI
     * @param board the board for the AI player
     */
    public void setupAiPlayer(int difficulty, Board board) {
        AiPlayer aiPlayer = new AiPlayer(false, difficulty, board);
        this.setPlayer2(aiPlayer);
        this.setAiPlayer(aiPlayer);
        this.switchRoles();
    }

    /**
     * Checks if the AI player is currently the setter.
     * @return true if the AI is the setter, false otherwise
     */
    public boolean isAiSetter() {
        return getAiPlayer().isSetter();
    }

    /**
     * Switches the roles between the two players.
     */
    public void switchRoles() {
        if (player1.isSetter()) {
            player1.setExperimenter();
            player2.setSetter();
        } else {
            player1.setSetter();
            player2.setExperimenter();
        }
    }

    /**
     * Determines the winner based on scores. Returns null if it's a tie.
     * @return the player with the higher score, or null if the scores are equal
     */
    public Player getWinner() {
        if (player1.getScore() < player2.getScore()) return player1;
        else if (player2.getScore() < player1.getScore()) return player2;
        else return null;
    }

}
