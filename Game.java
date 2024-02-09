public class Game {
    public static void main(String[] args) {
        Board board = new Board();

//        board.placeAtom(4, 4);
////        board.placeAtom(3, 4);
//        board.placeAtom(5, 4);
        board.placeAtom(5, 2);
        board.placeAtom(3, 5);
//        board.placeAtom(2, 5);
        board.placeAtom(9, 2);
        board.placeAtom(6, 7);
        board.placeAtom(9, 4);
//        board.placeAtom(4, 9);
        board.placeAtom(4, 9);
        board.placeRayMarker(0, 10);
//        board.placeRayMarker(1, 10);
//        board.placeRayMarker(2, 10);
        board.placeRayMarker(3, 10);
//        board.placeRayMarker(4, 10);
//        board.placeRayMarker(5, 10);
//
//        board.placeRayMarker(board.WIDTH - 1, 0);
//        board.placeRayMarker(board.WIDTH - 2, 0);
        board.placeRayMarker(board.WIDTH - 3, 0);
//        board.placeRayMarker(board.WIDTH - 4, 0);
        board.placeRayMarker(board.WIDTH - 5, 0);
//        board.placeRayMarker(board.WIDTH - 6, 0);
        board.placeRayMarker(10, 4);

        board.printTempBoard();
    }

}
