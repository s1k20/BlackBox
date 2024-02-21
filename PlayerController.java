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

        do{
            String input = in.nextLine();
            String[] token = input.split(",", 2);

            x = Integer.parseInt(token[0]);
            y = Integer.parseInt(token[1]);

            if(checkInvalidInput(y, x)){
                System.out.println("Invalid Co-ordinates! Please try again");
            }

        }while(checkInvalidInput(y, x));

        return new int[]{x, y};
    }

    private static boolean checkInvalidInput(int i, int j){
        return i + j <= 5 || i + j >= 15 || i <= 0 || j <= 0 || i >= 10 || j >= 10;
    }
}