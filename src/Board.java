import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class Board extends JPanel {

    private SudokuPanel[][] board;
    private int Rows, Col;
    private JTextField inProgress;


    public Board(int Rows, int Col, JTextField inProgress) {
        super(new GridLayout(Rows/3, Col/3));
        this.Rows = Rows; this.Col = Col;
        this.board = new SudokuPanel[Rows/3][Col/3];
        this.inProgress = inProgress;

        for(int i = 0; i < Rows/3; i++){
            for(int j = 0; j < Col/3; j++){

                board[i][j] = new SudokuPanel(Rows/3, Col/3, this, i, j);
                board[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                Font font = new Font("Arial", Font.PLAIN, 20);
                board[i][j].setFont(font);

                board[i][j].setBackground(Color.WHITE);

                add(board[i][j]);
            }
        }

        initialPlacements();
    }

    private int[] validPlacements(int panelRow, int panelCol, int row, int col){
        int[] temp = new int[9];

        // checks that placement is not one of the initial placements
        if(board[panelRow][panelCol].slots[row][col].getSudokuListener() == null) return null;
        int outLen = 0;

        for(int i = 1; i < 10; i++){
            if(legalPosition(panelRow, panelCol, row, col, i)) temp[outLen++] = i;
        }
        int[] out = new int[outLen];
        for(int i = 0; i < outLen; i++) { out[i] = temp[i]; }

        return out;
    }
    // checks if two int arrays have same values
    private boolean equalArrays(int[] a1, int[] a2){
        if(a1.length != a2.length)return false;
        for(int i = 0; i < a1.length; i++){
            if(a1[i] != 0 && !existsInArr(a2, a1[i]))return false;
        }
        return true;
    }

    private boolean existsInArr(int[] arr, int num){
        if(arr == null) return false;
        for(int i : arr){
            if(num == i)return true;
        }
        return false;
    }


    private int randomIntInArr(int[] arr){
        if(arr.length == 0 || arr[0] == 0){
            System.out.println("empty list");
            return 0;
        }
        int out;
        int size = 0;
        for (int value : arr) {
            if (value != 0) size++;
        }
        out = arr[ThreadLocalRandom.current().nextInt(0, size)];
        return out;
    }

    private boolean playable() {

        int fails = 0;
        for(int panelRow = 0; panelRow < Rows/3; panelRow++) {
            for(int panelCol = 0; panelCol < Col/3; panelCol++){
                int[] req = {1,2,3,4,5,6,7,8,9};
                int[] playableInPanel = new int[9];
                fails = 0;
                int cnt = 0;
                Set<Integer> integers = new HashSet<>();
                // determine if puzzle is solvable
                for(int row = 0; row < 3; row++){
                    for(int col = 0; col < 3; col++){
                        if(board[panelRow][panelCol].slots[row][col].getSudokuListener() == null)
                            playableInPanel[cnt++] = board[panelRow][panelCol].slots[row][col].getInt();
                        else {
                            int[] vals = validPlacements(panelRow, panelCol, row, col);
                            for(int v : vals) if(v != 0) integers.add(v);
                        }
                    }
                }

                for(int i : integers) if(i > 0 && i < 10) if(cnt < 9 && !existsInArr(playableInPanel, i)) playableInPanel[cnt++] = i;

                if(!equalArrays(req, playableInPanel)){
                    fails++;
                    return false;
                }


            }
        }
        return true;
    }

    public void clearBoard() {
        for(int pr = 0; pr < Rows/3; pr++) {
            for(int pc = 0; pc < Col/3; pc++) {
                for(int r = 0; r < 3; r++) {
                    for(int c = 0; c < 3; c++) {
                        board[pr][pc].slots[r][c].setText("");
                        board[pr][pc].slots[r][c].addSudokuListener();
                    }
                }
            }
        }
    }

    // init board function
    private void initialPlacements() {

        // Find cell with smallest number of valid placements
        while(true) {
            int panelRow, panelCol, row, col;
            SudokuButton chosen = null;
            int[] validValues = new int[20];
            // find button with smallest number of valid placements
            for(panelRow = 0; panelRow < Rows/3; panelRow++) {
                for(panelCol = 0; panelCol < Col/3; panelCol++) {

                    for(row = 0; row < 3; row++) {
                        for(col = 0; col < 3; col++) {
                            int[] tempValidVals = validPlacements(panelRow, panelCol, row, col);
                            if(tempValidVals != null && tempValidVals.length > 0 && tempValidVals.length < validValues.length) {
                                chosen = board[panelRow][panelCol].slots[row][col];
                                validValues = tempValidVals;

                                // if list is empty then board is unplayable
                                // clear the board and try to generate a new one
                            } else if(tempValidVals != null && tempValidVals.length == 0) {
                                clearBoard();
                                initialPlacements();
                                return;
                            }

                        }
                    }

                }
            }
            // if no cell has smallest number of valid placements
            // then end algorithm
            if(chosen == null) break;
            else {
                do {
                    int num = randomIntInArr(validValues);
                    if(num != 0) {
                        chosen.setText(num + "");
                        chosen.setFont(chosen.getFont().deriveFont(Font.BOLD));
                        chosen.removeListener();
                    }
                } while(!playable());
            }
        }

        finishInit();
    }

    // helper function for init
    private void finishInit() {
        int remove = ThreadLocalRandom.current().nextInt(55, 60);
        for(int i = 0; i < remove; i++) {

            int pr,pc,r,c;
            pr = ThreadLocalRandom.current().nextInt(0, 3);
            pc = ThreadLocalRandom.current().nextInt(0, 3);
            r = ThreadLocalRandom.current().nextInt(0, 3);
            c = ThreadLocalRandom.current().nextInt(0, 3);
            SudokuButton btn = board[pr][pc].slots[r][c];
            // check if is initial placement and hasn't been removed already
            if(btn.getSudokuListener() == null) {
                int num = btn.getInt();
                btn.setText("");
                btn.setFont(btn.getFont().deriveFont(Font.PLAIN));
                btn.addSudokuListener();

                // check all slots on board
                // if solution is not unique then try again
                for(pr = 0; pr < 3; pr++){
                    for(pc = 0; pc < 3; pc++) {
                        for(r = 0; r < 3; r++){
                            for(c = 0; c < 3; c++) {
                                if(solve(pr,pc, r, c, 0) != 1) {
                                    btn.setText(num + "");
                                    btn.setFont(btn.getFont().deriveFont(Font.BOLD));
                                    btn.removeListener();
                                    i--;
                                }
                            }
                        }
                    }
                }

            }

        }

    }

    // returns number of solutions available for cell
    private int solve(int pr, int pc, int r, int c, int count) {
        if (r == 3) {
            r = 0;
            if (++c == 3) return 1+count;
        }
        if (board[pr][pc].slots[r][c].getInt() != 0) return solve(pr, pc,r+1,c, count);

        // search for 2 solutions and break if count is 2 or more
        for (int val = 1; val < 10 && count < 2; ++val) {
            if(legalPosition(pr, pc, r, c, val)) {
                board[pr][pc].slots[r][c].setText(val + "");

                count = solve(pr, pc,r+1,c, count);
            }
        }
        board[pr][pc].slots[r][c].setText(""); // reset when backtracking
        return count;
    }

    public boolean inRow(int panelRow, int panelCol, int row, int col, int key) {
        boolean out = false;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                int num = board[panelRow][i].slots[row][j].getInt();

                if(board[panelRow][i].slots[row][j] != board[panelRow][panelCol].slots[row][col]) {
                    if (num == key) {
                        out = true;
                    }
                }

            }
        }
        return out;
    }

    public boolean inPanel(int panelRow, int panelCol, int row, int col, int key) {
        boolean out = false;
        for(int i = 0; i < 3; i++){
            for(int k = 0; k < 3; k++){
                if(board[panelRow][panelCol].slots[i][k] != board[panelRow][panelCol].slots[row][col]) {
                    // sets all bad placements to Color RED
                    if (board[panelRow][panelCol].slots[i][k].getInt() == key) {
                        out = true;
                    }
                }
            }
        }
        return out;
    }

    public boolean inCol(int panelRow, int panelCol, int row, int col, int key) {
        boolean out = false;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){

                int num = board[i][panelCol].slots[j][col].getInt();

                if(board[i][panelCol].slots[j][col] != board[panelRow][panelCol].slots[row][col]) {
                    if (num == key) {
                        out = true;
                    }
                }

            }
        }

        return out;
    }

    private boolean legalPosition(int panelRow, int panelCol, int row, int col, int key){
        return !inRow(panelRow, panelCol, row, col, key) && !inCol(panelRow, panelCol, row, col, key)
                && !inPanel(panelRow, panelCol, row, col, key);
    }

    // returns number of solutions available for cell
    private int solver(int pr, int pc, int r, int c, int count) {
        if (r == 3) {
            r = 0;
            if (++c == 3) return 1+count;
        }
        if (board[pr][pc].slots[r][c].getSudokuListener() == null || board[pr][pc].slots[r][c].getInt() != 0) return solver(pr, pc,r+1,c, count);
        // search for 2 solutions instead of 1
        // break, if 2 solutions are found
        for (int val = 1; val < 10 && count < 2; ++val) {
            if(legalPosition(pr, pc, r, c, val)) {
                int v = board[pr][pc].slots[r][c].getInt();
                if(v != val)board[pr][pc].setInt(r, c, val);
                // add additional solutions
                count = solver(pr, pc,r+1,c, count);
            }
        }
        return count;
    }

    public boolean parallelSolver(int depth, int threads) {
        ExecutorService e = Executors.newFixedThreadPool(threads);

        for(int i = 0; i < depth; i++) {
            int pr, pc, r, c;
            pr = ThreadLocalRandom.current().nextInt(0, 3);
            pc = ThreadLocalRandom.current().nextInt(0, 3);
            r = ThreadLocalRandom.current().nextInt(0, 3);
            c = ThreadLocalRandom.current().nextInt(0, 3);
            e.submit(() -> {
                //stuff
                if(board[pr][pc].slots[r][c].getSudokuListener() != null
                        && board[pr][pc].slots[r][c].semaphore.tryAcquire()){
                    solver(pr, pc, r, c, 0);

                    board[pr][pc].slots[r][c].semaphore.release();
                }
            });

        }
        e.shutdown();
        try {
            e.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) { System.out.println("Parallel Solver Interrupted"); }
        updateBoard();
        return true;
    }

    public void updateBoard(){
        boolean gameOver = true;
        for(int i = 0; i < Rows/3; i++) {
            for(int j = 0; j < Col/3; j++){

                for(int f = 0; f < 3; f++) {
                    for(int k = 0; k < 3; k++) {
                        int key = board[i][j].slots[f][k].getInt();
                        if(key == 0 || !legalPosition(i, j, f, k, key)) {
                            board[i][j].slots[f][k].setForeground(Color.RED);
                            gameOver = false;
                        } else {
                            board[i][j].slots[f][k].setForeground(Color.BLACK);
                        }
                    }
                }

            }
        }
        if(gameOver)inProgress.setText("Game Status: Game Over");
        else inProgress.setText("Game Status: In Progress");
    }

    public void reset() {
        clearBoard();
        initialPlacements();
        updateBoard();
    }

}
