package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinimaxAlgorithm {
    public static BestMove compute(char[] field, int turn, char charPlayerMakingMove, boolean isMaximizing) { //turn 1 - computer "me", -1 opponent
        if (turn != 1 && turn != -1) throw new IllegalArgumentException();
        char opponent = charPlayerMakingMove == 'X' ? 'O' : 'X';
        int value = checkStateOfGame(field);
        if (value != -1){
            value *= -turn;
            return new BestMove(value, -1);
        }
        if (isMaximizing) {value = Integer.MIN_VALUE;}
        else {value = Integer.MAX_VALUE;}
        int[] freeSpots = setFreeSpots(field);
        int indexOfExtreme = -1;
        for (int freeSpot : freeSpots) {
            char[] tempField = Arrays.copyOf(field, 9);
            tempField[freeSpot] = charPlayerMakingMove;
            BestMove computed = compute(tempField, -turn, opponent, !isMaximizing);
            if (isMaximizing) {
                if (computed.value > value) {
                    value = computed.value;
                    indexOfExtreme = freeSpot;
                }
            } else {
                if (computed.value < value) {
                    value = computed.value;
                    indexOfExtreme = freeSpot;
                }
            }
        }
        return new BestMove(value, indexOfExtreme);
    }

    private static int[] setFreeSpots(char[] field) {
        List<Integer> listFreeSpots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (field[i] == ' ') {
                listFreeSpots.add(i);
            }
        }
        int[] freeSpots = new int[listFreeSpots.size()];
        for (int i = 0; i < freeSpots.length; i++) {
            freeSpots[i] = listFreeSpots.get(i);
        }
        return freeSpots;
    }

    public static int checkStateOfGame(char[] field) {//return 0 if draw, 1 if last move wins
        if (field[0] != ' ' && field[0] == field[1] && field[0] == field[2]    || field[0] != ' ' && field[0] == field[3] && field[0] == field[6]
                || field[3] != ' ' && field[3] == field[4] && field[3] == field[5]     || field[1] != ' ' && field[1] == field[4] && field[1] == field[7]
                || field[6] != ' ' && field[6] == field[7] && field[6] == field[8]     || field[2] != ' ' && field[2] == field[5] && field[2] == field[8]
                || field[0] != ' ' && field[0] == field[4] && field[0] == field[8] || field[6] != ' ' && field[6] == field[4] && field[6] == field[2]) {
            return 1; //last move was winning
        }
        for (char c : field) {
            if (c == ' ') {
                return -1;//not finish
            }
        }
        return 0; //draw
    }
}

class BestMove {
    int value;
    int indexOfBestValue;

    public BestMove(int value, int index) {
        this.value = value;
        this.indexOfBestValue = index;
    }
}
