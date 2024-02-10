import java.util.Scanner;

public class Player {

        private Scanner scanThis = new Scanner(System.in);

        private static String playerOne;
        private static String playerTwo;
        private static String playerChoice;
        private static String setter;

        public String getSetter() {
            return setter;
        }

        public String getPlayerOne(){
            return playerOne;
        }

        public String getPlayerTwo(){
            return playerTwo;
        }

        public String getPlayerChoice() {
            return playerChoice;
        }

        private void setPlayerName() {
            System.out.println("Enter name for player one:");
            playerOne = scanThis.nextLine();
            System.out.println("Enter name for player two:");
            playerTwo = scanThis.nextLine();
        }

        public void PlayerInfo() {
            setPlayerName();
            System.out.println("Would "+getPlayerOne()+" like to be the setter or the experimenter?");
            playerChoice = scanThis.nextLine();
            while (!(playerChoice.equalsIgnoreCase("setter")
                    ||(playerChoice.equalsIgnoreCase("experimenter")))) {
                System.out.println("Would "+getPlayerOne()+" like to be the setter or the experimenter?");
                playerChoice = scanThis.nextLine();
            }
        }

        private boolean isExperimenter() {
            return getPlayerChoice().equalsIgnoreCase("experimenter");
        }

        public void SetPlayer() {
            int count = 1;
            System.out.println();
            System.out.println("Round number: "+count);

            if(isExperimenter()) {
                setter=getPlayerTwo();
                playerChoice=getPlayerOne();
                System.out.println(getPlayerOne()+" is the experimenter");
                System.out.println(getPlayerTwo()+" is the setter");
                System.out.println();
            }
            else {
                setter=getPlayerOne();
                playerChoice=getPlayerOne();
                System.out.println(getPlayerOne()+" is the setter");
                System.out.println(getPlayerTwo()+" is the experimenter");
                System.out.println();
            }
        }
    }

