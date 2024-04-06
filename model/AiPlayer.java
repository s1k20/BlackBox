package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AiPlayer extends Player {

    //1 == hardest
    //2 == medium
    //3 == easiest
    private final int difficulty;
    private final Board board;

    private final ArrayList<Integer> sentRays;
    private final ArrayList<Point> guessedAtoms;

    private static final Random random = new Random();

    private static final String[] names = {"Cian", "Lloyd", "Shlok"};

    public AiPlayer(boolean isSetter, int difficulty, Board board) {
        super(getRandomName(), isSetter);
        this.difficulty = difficulty;
        this.board = board;

        sentRays = new ArrayList<>();
        guessedAtoms = new ArrayList<>();
    }

    private static String getRandomName() {
        return names[random.nextInt(names.length)];
    }

    public void ai_sendRays() {
        int numRaysToSend = random.nextInt(2, 5) * difficulty;

        for (int i = 0; i < numRaysToSend; i++) {
            board.sendRay(pickRayInput());
        }
    }

    private int pickRayInput() {
        int input;
        do {
            input = random.nextInt(1, 55);
        } while (sentRays.contains(input));
        return input;
    }

    public void ai_setAtoms(int numAtoms) {

        for (int i = 0; i < numAtoms; i++) {
            Point newAtom = ai_randomAtom(true);

            board.placeAtom(newAtom.x, newAtom.y);
        }
    }

    private Point ai_randomAtom(boolean isSetting) {
        int x;
        int y;

        do {
            x = random.nextInt(9) + 1;
            y = random.nextInt(9) + 1;
        } while (board.checkInvalidInput(x, y) || (board.getBoardPosition(x, y) instanceof Atom && isSetting) || (!isSetting && guessedAtoms.contains(new Point(x, y))));

        return new Point(x, y);
    }

    public void ai_guessAtoms(int numAtoms) {
        switch (difficulty){
            case 1 -> guessAtoms_hard(numAtoms);
            case 2 -> guessAtoms_medium(numAtoms);
            case 3 -> guessAtoms_easy(numAtoms);
            default -> throw new IllegalArgumentException("invalid difficulty");
        }
    }

    private void guessAtoms_hard(int numAtoms) {

    }

    private void guessAtoms_medium(int numAtoms) {

    }

    private void guessAtoms_easy(int numAtoms) {
        for (int i = 0; i < numAtoms; i++) {
            Point guess = ai_randomAtom(false);


        }
    }

}
