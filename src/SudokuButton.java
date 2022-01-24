import javax.swing.*;
import java.util.concurrent.Semaphore;
public class SudokuButton extends JButton {

    SudokuListener sudokuListener;
    boolean sL_off;
    public final Semaphore semaphore;

    public SudokuButton(Board board, int panelRow, int panelCol, int row, int col) {
        super();
        this.semaphore = new Semaphore(1);
        this.sudokuListener = new SudokuListener(this, board, panelRow, panelCol, row, col);
        this.sL_off = false;
    }

    public int getInt(){
        try{
            int out = Integer.parseInt(getText());
            return out;
        }catch (NumberFormatException ignored){
            return 0;
        }
    }

    public void removeListener(){
        sL_off = true;
        removeKeyListener(sudokuListener);
    }

    public SudokuListener getSudokuListener(){
        if(sL_off)return null;
        return sudokuListener;
    }

    public void addSudokuListener(){
        addKeyListener(sudokuListener);
        sL_off = false;
    }
}
