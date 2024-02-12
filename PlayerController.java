import java.util.Scanner;

public class PlayerController {
    private static final Scanner in = new Scanner(System.in);

    public static String inUserName(){
        String username = "";

        do{
            username = in.nextLine();

            if(username.equals(" ") || username.isEmpty()){
                System.out.println("Invalid username, please try again!");
            }

        }while(username.equals(" ") || username.isEmpty());

        return username;
    }

    public static int[] getAtomInput(){
        int x, y;

        //TODO someone please fix loop to make sure input is valid
//        do{
        String input = in.nextLine();
        String[] token = input.split(",", 2);

        x = Integer.parseInt(token[0]);
        y = Integer.parseInt(token[1]);


        //TODO someone please do this, i cba working it out
//            if(x < 1 || x > 9){
//                System.out.println("Invalid X Co-ordinate, please try again");
//            }
//            else if(y < 0 || y > 5){
//                System.out.println("Invalid Y Co-ordinate, please try again");
//            }

//        }while(x < 5 || x > 9 || y < 0 || y > 6);

        return new int[]{x, y};
    }
}