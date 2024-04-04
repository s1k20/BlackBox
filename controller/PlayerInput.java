package controller;
import java.util.HashSet;
import java.util.Scanner;

//player input class which is part of projects controller class.
//class will only receive input and in game class will update board(model)
public class PlayerInput {

    //TODO clear after round
    private HashSet<Integer> sentRays;

    //scanner to take in user input through different functions in the class
    private static final Scanner in = new Scanner(System.in);

    public PlayerInput(){
        this.sentRays = new HashSet<>();
    }

    public void resetSentRays() {
        sentRays = new HashSet<>();
    }

    public int getPlayerOption() {
        int number;

        do {
            number = in.nextInt();

            if (number <= 0 || number > 3) {
                System.out.println("Invalid option, please try again");
            }
        } while (number <= 0 || number > 3);
        in.nextLine();
        return number;
    }

    //function which will return a username for a player
    public String inUserName(){
        String username;

        //enter do-while loop to get valid username
        do{
            username = in.nextLine();

            if(username.equals(" ") || username.isEmpty()){
                System.out.println("Invalid username, please try again!");
            }

        }while(username.equals(" ") || username.isEmpty());

        //return valid username
        return username;
    }

    //function to receive two numbers (x, y) and return them as an array of 2 numbers
    //arr[0] = x, arr[1] = y
    //will be called in game and then be passed to board (model)
    public int[] getAtomInput(){
        int x, y;

        //loop to insure inputs are valid in case user accidentally puts in invalid input
        do{

            //taken in by the form of "x,y"
            //input splits the string on the comma
            String input = in.nextLine();
            String[] token = input.split(",", 2);

            //parse to integer as tokens are Strings
            x = Integer.parseInt(token[0]);
            y = Integer.parseInt(token[1]);

            //passed to function which checks if input are invalid
            //returning true if they are invalid
            if(checkInvalidInput(y, x)){
                System.out.println("Invalid Co-ordinates! Please try again");
            }

        }while(checkInvalidInput(y, x));

        //returns valid co-ordinates
        return new int[]{x, y};
    }

   public String getRayInput(){
       String input = "";
       int num;

       do{
           input = in.nextLine();

           if(input.isEmpty()){
               return input;
           }
           num = Integer.parseInt(input);

           if(num < 1 || num > 54){
               System.out.println("Invalid Ray input! Please try again");
           }
           else if(sentRays.contains(num)){
               System.out.println("You have already sent a ray from this location!");
           }
           else{
               sentRays.add(num);
               break;
           }

       }while(num < 1 || num > 54 || sentRays.contains(num));

       return input;
   }



    //function which makes sure the inputs are on the board and that they are not invalid
    //0 and 10 invalid as that is position to hold ray marker
    private static boolean checkInvalidInput(int i, int j){
        return i + j <= 5 || i + j >= 15 || i <= 0 || j <= 0 || i >= 10 || j >= 10;
    }
}