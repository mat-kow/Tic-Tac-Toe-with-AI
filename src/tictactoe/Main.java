package tictactoe;

import java.util.*;

import static tictactoe.Player.printField;
import static tictactoe.Player.setFieldFromString;

public class Main {
    private final static List<String> possibleParameters = Arrays.asList("user", "easy", "medium", "hard");
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command;
        while (true) {
            System.out.print("Input command: ");
            command = scanner.nextLine();
            if (command.equals("exit")) break;
            String[] params = command.split("\\s+");
            if (params.length != 3) {
                System.out.println("Bad parameters!");
                continue;
            }
            if (!params[0].equals("start") || !possibleParameters.contains(params[1])
                    || !possibleParameters.contains(params[2])) {
                System.out.println("Bad parameters!");
                continue;
            }
            Player playerOne = new Player(params[1], 'X');
            Player playerTwo = new Player(params[2], 'O');
            play(playerOne, playerTwo);
        }
    }
    private static void play(Player playerOne, Player playerTwo) {
        setFieldFromString("_".repeat(9));
        printField();
        while (Player.checkStateOfGame(playerTwo.getPlayerChar())) {
            playerOne.makeMove();
            printField();
            if (!Player.checkStateOfGame(playerOne.getPlayerChar())) {
                break;
            }
            playerTwo.makeMove();
            printField();
        }

    }

//    private static void mainStage1() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter cells: ");
//        setFieldFromString(scanner.nextLine());
//        printField();
//        boolean goodCoords;
//        goodCoords = false;
//        while (!goodCoords) {
//            System.out.print("Enter coordinates: ");
//            goodCoords = getAndCheckCoords(scanner.nextLine());
//        }
//        printField();
//        checkStateOfGame();
//
//    }

//    private void mainStage2() {
//        Scanner scanner = new Scanner(System.in);
//        setFieldFromString("_________");
//        printField();
//        while (checkStateOfGame()) {
//            boolean goodCoords = false;
//            while (!goodCoords) {
//                System.out.print("Enter coordinates: ");
//                goodCoords = getAndCheckCoords(scanner.nextLine());
//            }
//            printField();
//            if (!checkStateOfGame()) {
//                break;
//            }
//            easyMove();
//            printField();
//        }
//    }








}
