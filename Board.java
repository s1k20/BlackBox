public class Board {
    //board is 9x9 but
    public final int WIDTH = 11;
    public final int HEIGHT = 11;

    private Object[][] board = new Object[HEIGHT][WIDTH];

    public Board(){
        initBoard();
    }

    private void initBoard(){
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++){

                //conditions if i and j are at an index which could contain a ray marker
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
}
