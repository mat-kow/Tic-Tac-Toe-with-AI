package tictactoe;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Player {
    private Level level;
    private final char playerChar;
    private static char[] field;
    private static int[] freeIndexes;

    public Player(String levelName, char playerChar) {
        setLevel(levelName);
        this.playerChar = playerChar;
    }


    public void setLevel(String levelName) {
        switch (levelName) {
            case "user":
                this.level = Level.USER;
                break;
            case "easy":
                this.level = Level.EASY;
                break;
            case "medium":
                this.level = Level.MEDIUM;
                break;
            case "hard":
                this.level = Level.HARD;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
    public void makeMove() {
        switch (level) {
            case EASY:
                easyMove();
                break;
            case USER:
                userMove();
                break;
            case MEDIUM:
                mediumMove();
                break;
            case HARD:
                hardMove();
                break;
        }
    }

    private void hardMove() {
        int value = MinimaxAlgorithm.compute(field, 1, playerChar, true).indexOfBestValue;
        System.out.println("Making move level \"hard\"");
        makeSignOnField(value);

    }

    private void userMove() {
        boolean goodCoords = false;
        while (!goodCoords) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter coordinates: ");
            goodCoords = getAndCheckCoords(scanner.nextLine(), this);
        }

    }

    public static void setFieldFromString(String input) {
        field = new char[9];
        char[] tempField = input.trim().replaceAll("_", " ").toCharArray();
        for (int i = 0; i < 9; i++) {
            if (i < 3) {
                field[i + 6] = tempField[i];
            } else if (i > 5) {
                field[i - 6] = tempField[i];
            } else {
                field[i] = tempField[i];
            }
        }
        setFreeIndexes();
    }
    private static void setFreeIndexes() {
        freeIndexes = new int[9];
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (field[i] == ' ') {
                freeIndexes[count] = i;
                count++;
            }
        }
    }

    public static boolean checkStateOfGame(char lastMoveSign) {//return true if game is still on
        if (field[0] != ' ' && field[0] == field[1] && field[0] == field[2]    || field[0] != ' ' && field[0] == field[3] && field[0] == field[6]
                || field[3] != ' ' && field[3] == field[4] && field[3] == field[5]     || field[1] != ' ' && field[1] == field[4] && field[1] == field[7]
                || field[6] != ' ' && field[6] == field[7] && field[6] == field[8]     || field[2] != ' ' && field[2] == field[5] && field[2] == field[8]
                || field[0] != ' ' && field[0] == field[4] && field[0] == field[8] || field[6] != ' ' && field[6] == field[4] && field[6] == field[2]) {
            System.out.println(lastMoveSign + " wins");
            return false;
        }
        for (char c : field) {
            if (c == ' ') {
                return true;
            }
        }
        System.out.println("Draw");
        return false;
    }
    private static void removeFreeIndex(int index) {
        int[]temp = Arrays.copyOf(freeIndexes, freeIndexes.length);
        freeIndexes = new int[freeIndexes.length - 1];
        int count = 0;
        for (int t : temp) {
            if (t != index) {
                freeIndexes[count] = t;
                count++;
                if (count == freeIndexes.length) return;
            }
        }
    }
    private void easyMove() {
        System.out.println("Making move level \"easy\"");
        randomMove();
    }
    private void randomMove() {
        Random random = new Random(LocalDateTime.now().getNano());
        int moveIndexOfList = random.nextInt(freeIndexes.length);
        int move = freeIndexes[moveIndexOfList];
        makeSignOnField(move);
    }
    private void makeSignOnField(int move) {
        removeFreeIndex(move);
        field[move] = playerChar;
    }
    private static boolean getAndCheckCoords(String input, Player player) {
        String[] temp = input.trim().split("\\s+");
        int[] coords = new int[2];
        try {
            coords[0] = Integer.parseInt(temp[0]);
            coords[1] = Integer.parseInt(temp[1]);
        } catch (NumberFormatException e) {
            System.out.println("You should enter numbers!");
            return false;
        }
        if (coords[0] > 3 || coords[0] < 1 || coords[1] > 3 || coords[1] < 1) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }
        int index = (coords[0] - 1) + (coords[1] - 1) * 3;
        if (field[index] == ' ') {
            field[index] = player.getPlayerChar();
            removeFreeIndex(index);
            return true;
        }
        System.out.println("This cell is occupied! Choose another one!");
        return false;
    }
    public static void printField() {
        char[] field = Player.getField();
        String sb = "-".repeat(9) + "\n" +
                "| " + field[6] + ' ' + field[7] + ' ' + field[8] + " |\n" +
                "| " + field[3] + ' ' + field[4] + ' ' + field[5] + " |\n" +
                "| " + field[0] + ' ' + field[1] + ' ' + field[2] + " |\n" +
                "-".repeat(9) + "\n";
        System.out.println(sb);
    }
    private int[] getCoordinatesFromString(String input) {
        String[] temp = input.trim().split("\\s+");
        int[] coords = new int[2];
        coords[0] = Integer.parseInt(temp[0]);
        coords[1] = Integer.parseInt(temp[1]);
        return coords;
    }

    private void mediumMove() {
        System.out.println("Making move level \"medium\"");
        if (complete2inLineWithThird(true)) return;
        if (complete2inLineWithThird( false)) return;
        randomMove();
    }

    private boolean complete2inLineWithThird (boolean forWin) {
        char sign;
        if (forWin) {
            sign = this.playerChar;
        } else {
            if (this.playerChar == 'X') sign = 'O';
            else sign = 'X';
        }
        char[] line = new char[3];
        for (int i = 0; i < 9; i += 3) { //find winning row
            for (int j = 0; j < 3; j ++) {
                line[j] = field[i + j];
            }
            int winningPlace = checkIfLineIsWinning(line, sign);
            if (winningPlace != -1) {
                makeSignOnField(winningPlace + i);
                return true;
            }
        }
        for (int i = 0; i < 3; i ++) { //find winning column
            for (int j = 0; j < 9; j += 3) {
                line[j / 3] = field[i + j];
            }
            int winningPlace = checkIfLineIsWinning(line, sign);
            if (winningPlace != -1) {
                makeSignOnField(winningPlace * 3 + i);
                return true;
            }
        }
        line = new char[]{field[0], field[4], field[8]};  // winning diagonal /
        int winningPlace = checkIfLineIsWinning(line, sign);
        if (winningPlace != -1) {
            makeSignOnField(winningPlace * 4);
            return true;
        }
        line = new char[]{field[2], field[4], field[6]}; //winning diagonal \
        winningPlace = checkIfLineIsWinning(line, sign);
        if (winningPlace != -1) {
            makeSignOnField((winningPlace + 1) * 2);
            return true;
        }
        return false;
    }

    private int checkIfLineIsWinning(char[] line, char expectedCharInLine) { //returns winning index of line or -1
        if (line.length != 3) throw new IllegalArgumentException();
        if (line[0] == expectedCharInLine && line[0] == line[1] && line[2] == ' ') {
            return 2;
        }
        if (line[1] == expectedCharInLine && line[1] == line[2] && line[0] == ' ') {
            return 0;
        }
        if (line[2] == expectedCharInLine && line[2] == line[0] && line[1] == ' ') {
            return 1;
        }
        return -1;
    }


    public static char[] getField() {
        return field;
    }

    public char getPlayerChar() {
        return playerChar;
    }

}
