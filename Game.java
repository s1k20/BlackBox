import com.sun.source.util.Plugin;

public class Game {

    private Player player1;
    private Player player2;

    private final Board board;

    public boolean isDone;

//    PlayerController playerIn;

    public Game(){
        board = new Board();
        isDone = false;
    }


    public static void main(String[] args) {
        Game game = new Game();
        game.playGame();
    }

    public void playGame(){

        //create players
        System.out.println("Player 1 - Please enter your name:");
        player1 = new Player(PlayerController.inUserName(), true);
        System.out.println("Player 2 - Please enter your name:");
        player2 = new Player(PlayerController.inUserName(), false);


        //main game loop
        while(!isDone){
            //first round player 1 is setter and player 2 is experimenter

            for(int i = 0; i < 6; i++){
                System.out.print(getSetter().getPlayerName() + " - (Setter): ");
                setAtom();
                board.printTempBoard();
            }
            isDone = true;

        }

    }

    public void setAtom(){
        System.out.println("Please enter an X Co-ordinate and a Y Co-ordinate (comma separated) in which to place an Atom:");
        int[] co_ords = PlayerController.getAtomInput();

        board.placeAtom(co_ords[0], co_ords[1]);
    }

    public Player getSetter(){
        return player1.isSetter() ? player1 : player2;
    }

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

//        public void SetPlayer() {
//            int count = 1;
//            System.out.println();
//            System.out.println("Round number: "+count);
//
//            if(isExperimenter()) {
//                setter=getPlayerTwo();
//                playerChoice=getPlayerOne();
//                System.out.println(getPlayerOne()+" is the experimenter");
//                System.out.println(getPlayerTwo()+" is the setter");
//                System.out.println();
//            }
//            else {
//                setter=getPlayerOne();
//                playerChoice=getPlayerOne();
//                System.out.println(getPlayerOne()+" is the setter");
//                System.out.println(getPlayerTwo()+" is the experimenter");
//                System.out.println();
//            }
//        }
