import javax.swing.*;
import java.awt.*;

public class SudokuPanel extends JPanel{

    SudokuButton[][] slots;
    int panelRow, panelCol;

    public int checkSlot(int Row, int Col) {
        return Integer.parseInt(slots[Row][Col].getText());
    }

    public void setInt(int Row, int Col, int num) {
        slots[Row][Col].setText(num + "");
    }

    public SudokuPanel(int Rows, int Col, Board board, int panelRow, int panelCol) {
        super(new GridLayout(Rows, Col));
        this.panelRow = panelRow; this.panelCol = panelCol;
        slots = new SudokuButton[Rows][Col];

        for(int i = 0; i < Rows; i++){
            for(int j = 0; j < Col; j++){

                final int I = i, J = j;
                slots[i][j] = new SudokuButton(board, panelRow, panelCol, i, j);
                slots[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                Font font = new Font("Arial", Font.PLAIN, 20);
                slots[i][j].setFont(font);
                slots[i][j].setBackground(Color.WHITE);

                slots[i][j].addSudokuListener();

                //slots[i][j].addActionListener(e -> {
                    //System.out.println("Row: " + I + " Col: " + J + " " + slots[I][J].getText());
                //});

                add(slots[i][j]);
            }
        }

    }

}
