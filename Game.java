import model.*;
import controller.*;

public class Game {

    //players objects which will hold necessary information about players
    private Player player1;
    private Player player2;

    //board object which holds logic to create the game
    private final Board board;

    //boolean to hold current game state; not done/done
    public boolean isDone;

    //player controller class to allow game to get necessary user input
    private final PlayerController playerIn;

    //game constructor which inits all objects/variables
    public Game(){
        board = new Board();
        playerIn = new PlayerController();
        isDone = false;
    }


    public static void main(String[] args) {

        //create game object and call playGame function
        Game game = new Game();
        game.playGame();
    }

    public void playGame(){

        //create players
        System.out.println("Player 1 - Please enter your name:");
        player1 = new Player(playerIn.inUserName(), true);
        System.out.println("Player 2 - Please enter your name:");
        player2 = new Player(playerIn.inUserName(), false);


        //main game loop
        while(!isDone){
            //first round player 1 is setter and player 2 is experimenter

            //let setter place 6 atoms
            for(int i = 0; i < 6; i++){
                System.out.print(getSetter().getPlayerName() + " - (Setter): ");
                setAtom();
                board.printTempBoard();
            }



            isDone = true;
        }

    }

    public void setAtom(){
        System.out.println("Please enter an X Co-ordinate and a Y Co-ordinate (comma separated) in which to place an model.Atom:");
        int[] co_ords;

        do{
            co_ords = playerIn.getAtomInput();

            if(board.getBoardPosition(co_ords[0], co_ords[1]) instanceof Atom){
                System.out.println(getSetter().getPlayerName() + " - You have already placed an atom in this position!");
            }

        }while(board.getBoardPosition(co_ords[0], co_ords[1]) instanceof Atom);

        board.placeAtom(co_ords[0], co_ords[1]);
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

