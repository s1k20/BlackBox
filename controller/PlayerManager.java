//package controller;
//
//import model.Board;
//import model.Player;
//import model.AiPlayer;
//
//import view.GUI_UserInput;
//
///**
// * Manages players including human players and AI in the game.
// */
//public class PlayerManager {
//    private Player player1;
//    private Player player2;
//    private AiPlayer aiPlayer;
//    private boolean isSinglePlayer;
//
//    /**
//     * Constructor for PlayerManager, initializes player settings.
//     * @param isSinglePlayer Indicates if the game mode is single player.
//     */
//    public PlayerManager(boolean isSinglePlayer) {
//        this.isSinglePlayer = isSinglePlayer;
//        setupPlayers();
//    }
//
//    /**
//     * Setup players based on the game mode.
//     */
//    private void setupPlayers() {
//        player1 = new Player("Player 1", true);  // Default name and role, could be modified later
//
//        if (isSinglePlayer) {
//            aiPlayer = new AiPlayer(false, GUI_UserInput.getAIDifficulty(), null);
//            player2 = aiPlayer;
//        } else {
//            player2 = new Player("Player 2", false);
//        }
//    }
//
//    /**
//     * Gets player one.
//     * @return Player one.
//     */
//    public Player getPlayer1() {
//        return player1;
//    }
//
//    /**
//     * Gets player two.
//     * @return Player two or AI player if in single-player mode.
//     */
//    public Player getPlayer2() {
//        return player2;
//    }
//
//    /**
//     * Switch roles between players.
//     */
//    public void switchRoles() {
//        boolean currentSetter = player1.isSetter();
//        player1.setSetter(!currentSetter);
//        player2.setSetter(currentSetter);
//    }
//
//    /**
//     * Determine which player is currently the setter.
//     * @return The setter player.
//     */
//    public Player getSetter() {
//        return player1.isSetter() ? player1 : player2;
//    }
//
//    /**
//     * Determine which player is currently the experimenter.
//     * @return The experimenter player.
//     */
//    public Player getExperimenter() {
//        return !player1.isSetter() ? player1 : player2;
//    }
//
//    /**
//     * Update the AI board for single player games.
//     * @param board The board to be used by the AI.
//     */
//    public void setAiBoard(Board board) {
//        if (aiPlayer != null) {
//            aiPlayer.setNewBoard(board);
//        }
//    }
//
//    /**
//     * Reset players for a new game or round.
//     */
//    public void resetPlayers() {
//        // Reset scores, roles, or any other necessary fields.
//        player1.reset();
//        player2.reset();
//    }
//
//    /**
//     * Get the winner based on the score.
//     * @return The player who won, or null if it's a tie.
//     */
//    public Player getWinner() {
//        if (player1.getScore() > player2.getScore()) {
//            return player1;
//        } else if (player2.getScore() > player1.getScore()) {
//            return player2;
//        } else {
//            return null;  // It's a tie.
//        }
//    }
//}
//
