package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AiPlayer extends Player {
    //general - will place random atoms and send a random number of rays at different points (guessing independent of sending rays)
    //1 == hardest - will guess 4 atoms correctly and have a 40% chance of guessing each other atom
    //2 == medium - will guess 2 atoms correctly and have a 30% chance of guessing each other atom
    //3 == easiest - will guess 1 atom correctly and then randomly guess 5 other atoms (could be correct or incorrect)
    private final int difficulty;
    private Board board;

    private final ArrayList<Integer> sentRays;
    private final ArrayList<Point> guessedAtoms;

    private static final Random random = new Random();

    private static final String[] names = {"Cian", "Lloyd", "Shlok"};

    public AiPlayer(boolean isSetter, int difficulty, Board board) {
        super(getRandomName() + " (AI)", isSetter);
        this.difficulty = difficulty;
        this.board = board;

        sentRays = new ArrayList<>();
        guessedAtoms = new ArrayList<>();
    }

    private static String getRandomName() {
        return names[random.nextInt(names.length)];
    }

    public ArrayList<Integer> ai_sendRays() {
        int multiplier = difficulty;
        if (difficulty == 1) multiplier = 2;

        int numRaysToSend = (random.nextInt(2, 5) * (multiplier));

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
        sentRays.add(input);
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

        Point randomPosition = new Point(x, y);
        guessedAtoms.add(randomPosition);

        return randomPosition;
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

    public void setNewBoard(Board board) {
        this.board = board;
    }

    private ArrayList<Point> guessAtoms_hard(int numAtoms) {
        ArrayList<Point> atomGuesses = new ArrayList<>();

        int i = 0;
        for (; i < 4; i++) {
            int xAtom = board.getPlacedAtoms().get(i).getXCo_ord();
            int yAtom = board.getPlacedAtoms().get(i).getYCo_ord();

            atomGuesses.add(new Point(xAtom, yAtom));
        }

        calculatedAtomGuess(atomGuesses, i, numAtoms);

        return atomGuesses;
    }

    private ArrayList<Point> guessAtoms_medium(int numAtoms) {
        ArrayList<Point> atomGuesses = new ArrayList<>();

        int i = 0;
        for (; i < 2; i++) {
            int xAtom = board.getPlacedAtoms().get(i).getXCo_ord();
            int yAtom = board.getPlacedAtoms().get(i).getYCo_ord();

            atomGuesses.add(new Point(xAtom, yAtom));
        }

        calculatedAtomGuess(atomGuesses, i, numAtoms);

        return atomGuesses;
    }

    private ArrayList<Point> guessAtoms_easy(int numAtoms) {
        ArrayList<Point> atomGuesses = new ArrayList<>();
        int xAtom = board.getPlacedAtoms().get(0).getXCo_ord();
        int yAtom = board.getPlacedAtoms().get(0).getYCo_ord();

        atomGuesses.add(new Point(xAtom, yAtom));

        for (int i = 1; i < numAtoms; i++) {
            Point guess = ai_randomAtom(false);
            atomGuesses.add(guess);
        }
        return atomGuesses;
    }

    private void calculatedAtomGuess(ArrayList<Point> atomGuesses, int i, int nAtoms) {
        for (int j = i; j < nAtoms; j++) {
            int xAtom = board.getPlacedAtoms().get(i).getXCo_ord();
            int yAtom = board.getPlacedAtoms().get(i).getYCo_ord();

            int addX;
            int addY;

            int bound = 3;
            int minus = 1;

            do {
                addX = random.nextInt(bound) - minus;
                addY = random.nextInt(bound) - minus;

                bound += 2;
                minus += 1;

            } while (board.checkInvalidInput(xAtom + addX, yAtom + addY) || atomGuesses.contains(new Point(xAtom + addX, yAtom + addY)));

            atomGuesses.add(new Point(xAtom + addX, yAtom + addY));
        }
    }


}
