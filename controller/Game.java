package controller;
import model.*;
import view.*;

import java.util.Random;

//controller part of project which runs main logic
//updates model and view
public class Game {

    //players objects which will hold necessary information about players
    private Player player1;
    private Player player2;

    //board object which holds logic to create the game
    private Board board;

    //variable to allow for two
    private int gameNum;
    private final int numGames = 2;
    private final int numAtoms = 1;

    //player controller class to allow game to get necessary user input
    private final PlayerInput playerIn;
    private GameView view;



    //game constructor which inits all objects/variables
    public Game(){
        board = new Board();
        view = new GameView(this);

        playerIn = new PlayerInput();

        //set gameNum to 1 for the first game
        gameNum = 1;
    }

    public void playGame() {
        view.printStart();
        int choice;

        do {
            choice = playerIn.getPlayerOption();
            switch (choice) {
                case 1 -> playSinglePlayerGame();
                case 2 -> play2PlayerGame();
                case 3 -> System.out.println("insert rules");
                case 4 -> System.out.println("lolol");
            }
        } while (choice != 5);

    }

    public void testGame() {
        board = new Board();
        view = new GameView(this);

        board.placeAtom(1, 7);
//        board.placeAtom(4, 6);
        board.sendRay(23);
        view.printEntireBoard();
    }

    public Board getBoard() {
         return this.board;
    }

    public void play2PlayerGame(){
        //create players
        System.out.println("Player 1 - Please enter your name:");
        player1 = new Player(playerIn.inUserName(), true);
        System.out.println("Player 2 - Please enter your name:");
        player2 = new Player(playerIn.inUserName(), false);


        //main game loop
        while(gameNum <= numGames){
            //first round player 1 is setter and player 2 is experimenter
            view.printRound(gameNum);
            //let setter place 6 atoms
            setAtoms();

            //let experimenter send rays
            sendRays();

            //let experimenter guess atoms locations
            guessAtoms();

            //switch the roles and increase gameNum
            switchRoles();
            gameNum += 1;

            playerIn.resetSentRays();
            board = new Board();
            view = new GameView(this);
        }
        Player winner = getWinner();
        view.printStats(player1, player2, winner);
    }

    public void playSinglePlayerGame() {
        System.out.println("Please enter your name:");
        player1 = new Player(playerIn.inUserName(), false);
        singlePlayerSetAtoms();

    }

    public void setAtom(boolean isSetter){
        if (isSetter) System.out.println("Please enter an X Co-ordinate and a Y Co-ordinate (comma separated) in which to PLACE an Atom:");
        else System.out.println("Please enter an X Co-ordinate and a Y Co-ordinate (comma separated) in which to GUESS an Atom:");
        int[] co_ords;

        do{
            co_ords = playerIn.getAtomInput();

            if(isSetter && board.getBoardPosition(co_ords[0], co_ords[1]) instanceof Atom){
                System.out.println(getSetter().getPlayerName() + " - You have already placed an atom in this position!");
            }

        }while(isSetter && board.getBoardPosition(co_ords[0], co_ords[1]) instanceof Atom);

        if (isSetter) board.placeAtom(co_ords[0], co_ords[1]);
        else if (!(board.getBoardPosition(co_ords[0], co_ords[1]) instanceof Atom)) getExperimenter().updateScore(5);
        else getExperimenter().correctAtom();
    }

    public void setAtoms(){
        for(int i = 0; i < numAtoms; i++){
            System.out.print(getSetter().getPlayerName() + " - (Setter): ");
            setAtom(true);
            view.printEntireBoard();
        }
    }

    public void singlePlayerSetAtoms() {
        Random random = new Random();

        for (int i = 0; i < numAtoms; i++) {
            int x;
            int y;
            do {
                x = random.nextInt(9) + 1;
                y = random.nextInt(9) + 1;
            } while (board.checkInvalidInput(x, y) || board.getBoardPosition(x, y) instanceof Atom);
            board.placeAtom(x, y);
            view.printEntireBoard();
        }
    }

    public void guessAtoms() {
        for(int i = 0; i < numAtoms; i++){
            System.out.print(getExperimenter().getPlayerName() + " - (Experimenter): ");
            setAtom(false);
        }
    }

    public void sendRays(){
        String input = "";

        do{
            System.out.print(getExperimenter().getPlayerName() + " - (Experimenter): ");
            System.out.println("Enter a number between 1 and 54 to send a ray or hit 'ENTER' to stop:");
            input = playerIn.getRayInput();
            if(input.isEmpty()){
                break;
            }
            if (board.sendRay(Integer.parseInt(input))) {
                getExperimenter().updateScore(1);
            }
            else {
                getExperimenter().updateScore(2);
            }

            view.printLiveBoard();
            getExperimenter().raySent();

        }while(true);

    }

    //returns setter
    public Player getSetter(){
        return player1.isSetter() ? player1 : player2;
    }

    //returns experimenter
    public Player getExperimenter(){
        return !player1.isSetter() ? player1 : player2;
    }


    //function to just swap who is setter and who is experimenter
    public void switchRoles(){
        if(player1.isSetter()){
            player1.setExperimenter();
            player2.setSetter();
        }
        else{
            player1.setSetter();
            player2.setExperimenter();
        }
    }

    public Player getWinner() {
        if (player1.getScore() < player2.getScore()) return player1;
        else if (player2.getScore() < player1.getScore()) return player2;
        else return null;
    }

}

