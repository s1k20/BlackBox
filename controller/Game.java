package controller;
import model.*;
import view.*;

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
    private final int numAtoms = 6;

    //player controller class to allow game to get necessary user input
    private final PlayerInput playerIn;
    private GameView view;



    //game constructor which inits all objects/variables
    public Game(){
        board = new Board();
        playerIn = new PlayerInput();
        view = new GameView(board);

        //set gameNum to 1 for the first game
        gameNum = 1;
    }

    public void playGame(){
        //create players
        System.out.println("Player 1 - Please enter your name:");
        player1 = new Player(playerIn.inUserName(), true);
        System.out.println("Player 2 - Please enter your name:");
        player2 = new Player(playerIn.inUserName(), false);


        //main game loop
        while(gameNum <= numGames){
            //first round player 1 is setter and player 2 is experimenter

            //let setter place 6 atoms
            setAtoms();

            //let experimenter send rays
            sendRays();

            //switch the roles and increase gameNum
            switchRoles();
            gameNum += 1;

            board = new Board();
            view = new GameView(board);
        }

    }

    public void setAtom(){
        System.out.println("Please enter an X Co-ordinate and a Y Co-ordinate (comma separated) in which to place an Atom:");
        int[] co_ords;

        do{
            co_ords = playerIn.getAtomInput();

            if(board.getBoardPosition(co_ords[0], co_ords[1]) instanceof Atom){
                System.out.println(getSetter().getPlayerName() + " - You have already placed an atom in this position!");
            }

        }while(board.getBoardPosition(co_ords[0], co_ords[1]) instanceof Atom);

        board.placeAtom(co_ords[0], co_ords[1]);
    }

    public void setAtoms(){
        for(int i = 0; i < numAtoms; i++){
            System.out.print(getSetter().getPlayerName() + " - (Setter): ");
            setAtom();
            view.printEntireBoard();
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
            board.sendRay(Integer.parseInt(input));
            view.printEntireBoard();

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

}

