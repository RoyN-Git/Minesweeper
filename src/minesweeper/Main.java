package minesweeper;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mines do you want on the field?\n> ");
        int size = 9;
        int numberOfMines = scanner.nextInt();
        char[][] field = new char[size][size];
        char[][] overlay = new char[size][size];
        int foundMines;
        boolean fieldIsNotGenerated = true;
        int x;
        int y;
        String operation;
        ArrayList<int[]> completedCoordinates = new ArrayList<>();
        boolean gameIsRunning = true;

        //CREATING THE FIELD
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = '/';
            }
        }
            //+ Overlay
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                overlay[i][j] = '.';
            }
        }


        do {
            //PLAYER CHOOSES STARTING POSITION
            printField(field, overlay, size, gameIsRunning);
            do {
                System.out.println("Set/unset mines marks or claim a cell as free: > ");
                y = scanner.nextInt() - 1;
                x = scanner.nextInt() - 1;
                operation = scanner.next().toLowerCase();
            }while (x > size -1 || x < 0 || y > size -1 || y < 0);


            switch (operation) {
                case "free": {

                    generatingField(field, x, y, numberOfMines, size);

                    fieldIsNotGenerated = false;

                    gameIsRunning = stepOnCell(field, overlay,size, x, y, completedCoordinates);
                    break;
                }
                case "mine": {

                    if (overlay[x][y] == '*') {
                        overlay[x][y] = '.';
                    } else {
                        overlay[x][y] = '*';
                    }
                    break;
                }
                default: break;
            }
        }while (fieldIsNotGenerated);


        //OUTPUT
        printField(field, overlay, size, gameIsRunning);


    //MARKER
    do {
        System.out.println("Set/delete mines marks (x and y coordinates): > ");

        y = scanner.nextInt() - 1;
        x = scanner.nextInt() - 1;
        operation = scanner.next().toLowerCase();

        switch (operation) {
            case "free": {

                if (overlay[x][y] == 0) {
                    System.out.println("Wrong coordinates!");
                } else if (overlay[x][y] == '*') {
                    System.out.println("You already set a marker here!");
                } else {

                    gameIsRunning = stepOnCell(field, overlay, size, x, y, completedCoordinates);



                    break;
                }
                break;
            }
            case "mine": {

                if (overlay[x][y] == 0) {
                    System.out.println("Wrong coordinates!");
                } else if (overlay[x][y] == '*') {
                    overlay[x][y] = '.';
                } else {
                    overlay[x][y] = '*';
                }
                break;
            }
            default: System.out.println("Wrong operation!");
        }


        ///////////////////////////////////////////////////
        //OUTPUT with Marker
        printField(field, overlay, size, gameIsRunning);

    //checking markers
        foundMines = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (overlay[i][j] == '*' && field[i][j] == 'X') {
                    foundMines++;
                }
            }
        }
        if (foundMines == numberOfMines) {
            gameIsRunning = false;
            System.out.println("Congratulations! You found all the mines!");
        }

    } while(gameIsRunning);
    }



    public static boolean stepOnCell(char[][] field, char[][] overlay, int size, int x, int y, ArrayList<int[]> completedCoordinates) {

        if (field[x][y] == 'X') {
            System.out.println("You stepped on a mine and failed!");
            return false;
        } else {
            freeCell(field, overlay, size, x, y, completedCoordinates);
            return true;
        }
    }
    public static void freeCell(char[][] field, char[][] overlay, int size, int x, int y, ArrayList<int[]> completedCoordinates) {

        Deque<int[]> stack = new ArrayDeque<>();
        int[] tempCord;
        int[] temp1 = {x, y};
        stack.offerLast(temp1);

        do {
            tempCord = stack.pop();
            x = tempCord[0];
            y = tempCord[1];
            int[] temp2 = {x, y};
            completedCoordinates.add(temp2);
            overlay[x][y] = 0;

            if (field[x][y] == '/') {

                if (x == 0) {//upper side
                    if (y == 0) {//upper left corner
                        addToStack(x, y + 1, stack, completedCoordinates);

                        addToStack(x + 1, y + 1, stack, completedCoordinates);
                        addToStack(x + 1, y, stack, completedCoordinates);

                    } else if (y == size - 1) {//upper right corner
                        addToStack(x + 1, y, stack, completedCoordinates);

                        addToStack(x + 1, y - 1, stack, completedCoordinates);
                        addToStack(x, y - 1, stack, completedCoordinates);

                    } else {//just upper side
                        addToStack(x, y + 1, stack, completedCoordinates);

                        addToStack(x + 1, y + 1, stack, completedCoordinates);
                        addToStack(x + 1, y, stack, completedCoordinates);

                        addToStack(x + 1, y - 1, stack, completedCoordinates);
                        addToStack(x, y - 1, stack, completedCoordinates);
                    }

                } else if (y == 0) {//left side
                    if (x == size - 1) {//lower left corner
                        addToStack(x - 1, y, stack, completedCoordinates);

                        addToStack(x - 1, y + 1, stack, completedCoordinates);
                        addToStack(x, y + 1, stack, completedCoordinates);

                    } else {//just left side
                        addToStack(x - 1, y, stack, completedCoordinates);

                        addToStack(x - 1, y + 1, stack, completedCoordinates);
                        addToStack(x, y + 1, stack, completedCoordinates);

                        addToStack(x + 1, y + 1, stack, completedCoordinates);
                        addToStack(x + 1, y, stack, completedCoordinates);

                    }

                } else if (y == size - 1) {//right side
                    if (x == size - 1) {//lower right corner
                        addToStack(x - 1, y - 1, stack, completedCoordinates);
                        addToStack(x - 1, y, stack, completedCoordinates);
                        addToStack(x, y - 1, stack, completedCoordinates);

                    } else {//just right side
                        addToStack(x - 1, y - 1, stack, completedCoordinates);
                        addToStack(x - 1, y, stack, completedCoordinates);
                        addToStack(x + 1, y, stack, completedCoordinates);

                        addToStack(x + 1, y - 1, stack, completedCoordinates);
                        addToStack(x, y - 1, stack, completedCoordinates);

                    }
                } else if (x == size - 1) {//just lower side
                    addToStack(x - 1, y - 1, stack, completedCoordinates);
                    addToStack(x - 1, y, stack, completedCoordinates);

                    addToStack(x - 1, y + 1, stack, completedCoordinates);
                    addToStack(x, y + 1, stack, completedCoordinates);
                    addToStack(x, y - 1, stack, completedCoordinates);

                } else {//center

                    addToStack(x - 1, y - 1, stack, completedCoordinates);
                    addToStack(x - 1, y, stack, completedCoordinates);

                    addToStack(x - 1, y + 1, stack, completedCoordinates);
                    addToStack(x, y + 1, stack, completedCoordinates);

                    addToStack(x + 1, y + 1, stack, completedCoordinates);
                    addToStack(x + 1, y, stack, completedCoordinates);

                    addToStack(x + 1, y - 1, stack, completedCoordinates);
                    addToStack(x, y - 1, stack, completedCoordinates);

                }
            }


        }while(!stack.isEmpty());
    }
    public static void addToStack(int x, int y, Deque<int[]> stack, ArrayList<int[]> completedCoordinates) {
        int[] newCoordinate = {x,y};
        int[] temp;
        for (int[] completedCoordinate : completedCoordinates) {
            temp = completedCoordinate;
            if (temp[0] == newCoordinate[0] && temp[1] == newCoordinate[1]) {
                return;
            }
        }
            stack.offerLast(newCoordinate);
        }


    public static void printUpperCoordinateScale(int size) {
        // First line (Coordinate Scale)    " |123456789|
        //                                   -|          "
        System.out.print(" |");
        for (int i = 1; i <= size; i++) {
            System.out.print(i);
        }
        System.out.print("|\n-|");

        // Second line                      "  ---------|
        //                                               "
        for (int i = 0; i < size; i++) {
            System.out.print("-");
        }
        System.out.print("|\n");

        /*
        RESULT with size = 9:
         |123456789|
        -|---------|\n
        */
    }
    public static void printLowerBorder(int size) {
        //Lower Border
        System.out.print("-|");
        for (int i = 0; i < size; i++) {
            System.out.print("-");
        }
        System.out.print("|\n");
    }
    public static void printField(char[][] field, char[][] overlay, int size, boolean gameIsRunning) {
        printUpperCoordinateScale(size);

        //Actual Field + Side Borders
        for (int i = 0; i < size; i++) {
            System.out.print(i + 1 + "|");  //printing side coordinate scale

            for (int j = 0; j < size; j++) {
                if (overlay[i][j] != 0 && gameIsRunning) {
                    System.out.print(overlay[i][j]);
                } else {
                    System.out.print(field[i][j]);
                }
            }

            System.out.print("|\n");    //printing right border
        }
        printLowerBorder(size);
    }


    public static void setHint(int i, int j, int size, char[][] field) {

        field[i][j] = '0';

        if (i == 0) {//upper side
            if (j == 0) {//upper left corner
                checkUpperLeftCorner(i, j, field);

            } else if (j == size -1) {//upper right corner
                checkUpperRightCorner(i, j, field);

            } else {//just upper side
                checkUpperLeftCorner(i, j, field);

                if (field[i+1][j-1] == 'X') {
                    field[i][j]++;
                }
                if (field[i][j-1] == 'X') {
                    field[i][j]++;
                }
            }

        } else if (j == 0) {//left side
            if (i == size -1) {//lower left corner
                checkLowerLeftCorner(i, j, field);

            } else {//just left side
                if (field[i-1][j] == 'X') {
                    field[i][j]++;
                }

                if (field[i-1][j+1] == 'X') {
                    field[i][j]++;
                }
                checkUpperLeftCorner(i, j, field);

            }

        } else if (j == size -1) {//right side
            if (i == size -1) {//lower right corner
                checkLowerRightCorner(i, j, field);

            } else {//just right side
                if (field[i-1][j-1] == 'X') {
                    field[i][j]++;
                }
                if (field[i-1][j] == 'X') {
                    field[i][j]++;
                }
                checkUpperRightCorner(i, j, field);
            }
        }
        else if (i == size -1) {//just lower side
            if (field[i-1][j-1] == 'X') {
                field[i][j]++;
            }
            checkLowerLeftCorner(i, j, field);
            if (field[i][j-1] == 'X') {
                field[i][j]++;
            }

        } else {//center
            if (field[i-1][j-1] == 'X') {
                field[i][j]++;
            }
            if (field[i-1][j] == 'X') {
                field[i][j]++;
            }
            if (field[i-1][j+1] == 'X') {
                field[i][j]++;
            }
            checkUpperLeftCorner(i, j, field);
            if (field[i+1][j-1] == 'X') {
                field[i][j]++;
            }
            if (field[i][j-1] == 'X') {
                field[i][j]++;
            }

        }

        if (field[i][j] == '0') {
            field[i][j] = '/';
        }
    }
    private static void checkLowerRightCorner(int i, int j, char[][] field) {
        if (field[i-1][j-1] == 'X') {
            field[i][j]++;
        }
        if (field[i-1][j] == 'X') {
            field[i][j]++;
        }
        if (field[i][j-1] == 'X') {
            field[i][j]++;
        }
    }
    private static void checkLowerLeftCorner(int i, int j, char[][] field) {
        if (field[i-1][j] == 'X') {
            field[i][j]++;
        }

        if (field[i-1][j+1] == 'X') {
            field[i][j]++;
        }
        if (field[i][j+1] == 'X') {
            field[i][j]++;
        }
    }
    private static void checkUpperRightCorner(int i, int j, char[][] field) {
        if (field[i+1][j] == 'X') {
            field[i][j]++;
        }

        if (field[i+1][j-1] == 'X') {
            field[i][j]++;
        }
        if (field[i][j-1] == 'X') {
            field[i][j]++;
        }
    }
    private static void checkUpperLeftCorner(int i, int j, char[][] field) {
        if (field[i][j+1] == 'X') {
            field[i][j]++;
        }

        if (field[i+1][j+1] == 'X') {
            field[i][j]++;
        }
        if (field[i+1][j] == 'X') {
            field[i][j]++;
        }
    }

    public static void generatingField(char[][] field, int x, int y, int numberOfMines, int size) {

        int line;
        int column;
        Random random = new Random();
        for (int i = 0; i < numberOfMines; i++) {
            do {
                line = random.nextInt(size);
                column = random.nextInt(size);
            } while(!isFreeCell(field, line, column) || isPlayerPosition(x,y,line,column));

            field[line][column] = 'X';
        }

        //INCLUDING MINE HINTS
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == '/') {
                    setHint(i, j, size, field);
                }
            }
        }
    }

public static boolean isPlayerPosition(int x, int y, int line, int column) {
    return line == x && column == y;
}
public static boolean isFreeCell(char[][] field, int line, int column) {
    return field[line][column] == '/';
}

}