import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SudokuListener implements KeyListener {
    SudokuButton button;
    Board board;

    int panelRow, panelCol, row, col;

    public SudokuListener(SudokuButton button, Board board,
                          int panelRow, int panelCol, int row, int col) {
        super();
        this.panelRow = panelRow;
        this.panelCol = panelCol;
        this.row = row;
        this.col = col;
        this.button = button;
        this.board = board;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if((int)e.getKeyChar() == 8 && button.getSudokuListener() != null) {
            button.setText("");
            board.updateBoard();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            int key = Integer.parseInt(e.getKeyChar() + "");
            if(0 < key && 10 > key) {
                button.setText(key + "");
                board.updateBoard();
            }

        }catch (NumberFormatException ignored) {}
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
