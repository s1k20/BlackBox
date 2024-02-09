public class Board {
    //board is 9x9 but
    public final int WIDTH = 11;
    public final int HEIGHT = 11;


    //test colours
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";


    private Object[][] board = new Object[HEIGHT][WIDTH];

    public Board(){
        initBoard();
    }

    private void initBoard(){
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++){

                //conditions if i and j are at an index which could contain a ray marker

                if(checkRayMarker(i, j)){

                if(i + j == 5 || i + j == 15 || (i == 0 && j >= 5) ||
                        (j == 10 && i < 6) || (i == 10 && j <= 5) || (j == 0 && i >= 5)){


                    board[i][j] = new emptyMarker();

                }

                //conditions if i and j are at indexes which are not part of the board
                //but present in order to represent the board
                else if(i + j <= 4 || i + j > 15){

                    board[i][j] = new nullHex();
                }
            }
        }

    }

    public void placeAtom(int x, int y){
        Atom newAtom = new Atom(x, y);
        board[x][y] = newAtom;

        placeCircleOfInfluence(newAtom);

    }

    private void placeCircleOfInfluence(Atom a){
        for(CircleOfInfluence c : a.circleOfInfluence){

            //check to make sure atom is not getting overridden
            if(!(board[c.getXCo_ord()][c.getYCo_ord()] instanceof Atom) && !(board[c.getXCo_ord()][c.getYCo_ord()] instanceof emptyMarker)) {

                //in the case where one part of circle of influence intersects another part;
                if(board[c.getXCo_ord()][c.getYCo_ord()] instanceof CircleOfInfluence){
                    IntersectingCircleOfInfluence s = new IntersectingCircleOfInfluence();

                    s.addPart((CircleOfInfluence) board[c.getXCo_ord()][c.getYCo_ord()]);
                    s.addPart(c);

                    board[c.getXCo_ord()][c.getYCo_ord()] = s;
                }
                //in the case where one part of a circle of influence intersects and intersection of influences
                else if(board[c.getXCo_ord()][c.getYCo_ord()] instanceof IntersectingCircleOfInfluence){
                    IntersectingCircleOfInfluence s = (IntersectingCircleOfInfluence) board[c.getXCo_ord()][c.getYCo_ord()];

                    s.addPart(c);
                }

                else{
                    board[c.getXCo_ord()][c.getYCo_ord()] = c;
                }
            }
        }
    }

    private static boolean checkRayMarker(int i, int j){
        return i + j == 5 || i + j == 15 || (i == 0 && j >= 5) ||
                (j == 10 && i < 6) || (i == 10 && j <= 5) || (j == 0 && i >= 5);
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++){
                if(board[i][j] instanceof nullHex){
                    System.out.print(" ");
                }
                else if(board[i][j] instanceof emptyMarker){
                    System.out.print("o");
                }
                else{
                    System.out.print("x");
                }
            }
            System.out.println();
        }

    }

    private static class nullHex{
    }
    private static class emptyMarker{
    }

    public void printTempBoard(){
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++){
                if(i > 5 && j == 0){
                    for(int k = 5; k < i; k++){
                        System.out.print(" ");
                    }
                }

                if(board[i][j] instanceof nullHex){
                    System.out.print(" ");
                }
                else if(board[i][j] instanceof emptyMarker){
                    System.out.print("- ");
                }
                else if(board[i][j] instanceof Atom){
                    System.out.print(ANSI_RED + "o " + ANSI_RESET);
                }
                else if(board[i][j] instanceof IntersectingCircleOfInfluence){
                    System.out.print(ANSI_GREEN + "x " + ANSI_RESET);
                }
                else if(board[i][j] instanceof CircleOfInfluence){
                    CircleOfInfluence c = (CircleOfInfluence) board[i][j];
                    if(c.getOrientation() == 45){
                        System.out.print(ANSI_GREEN + "/ " + ANSI_RESET);
                    }
                    else if(c.getOrientation() == 90){
                        System.out.print(ANSI_GREEN + "| " + ANSI_RESET);
                    }
                    else{
                        System.out.print(ANSI_GREEN + "\\ " + ANSI_RESET);
                    }



                }
                else{
                    System.out.print("x ");
                }
            }
            System.out.println();
        }
    }
}


//    * * * *
//   * * * * *
//  * * * * * *
// * * * * * * *
//  * * * * * *
//   * * * * *
//    * * * *
