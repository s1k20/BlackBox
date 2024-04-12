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

    public ArrayList<Integer> ai_sendRays() {
        int numRaysToSend = (int) (random.nextInt(2, 3) * (difficulty / 0.4));
        ArrayList<Integer> rays = new ArrayList<>();

        for (int i = 0; i < numRaysToSend; i++) {
            int ray = pickRayInput();
            rays.add(ray);
        }
        return rays;
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

    public ArrayList<Point> ai_guessAtoms(int numAtoms) {
        switch (difficulty){
            case 1 -> {
                return guessAtoms_hard(numAtoms);
            }
            case 2 -> {
                return guessAtoms_medium(numAtoms);
            }
            case 3 -> {
                return guessAtoms_easy(numAtoms);
            }
            default -> throw new IllegalArgumentException("invalid difficulty");
        }
    }

    private ArrayList<Point> guessAtoms_hard(int numAtoms) {
        return null;
    }

    private ArrayList<Point> guessAtoms_medium(int numAtoms) {
        return null;
    }

    private ArrayList<Point> guessAtoms_easy(int numAtoms) {
        ArrayList<Point> atomGuesses = new ArrayList<>();

        for (int i = 0; i < numAtoms; i++) {
            Point guess = ai_randomAtom(false);
            atomGuesses.add(guess);
        }
        return atomGuesses;
    }

}
