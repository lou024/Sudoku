import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private JPanel controlPanel;
    private Board board;
    private JButton evalButton, resetButton;
    private JTextField userInput, parallelEvalText, gameStatus;

    public MainFrame() {
        super();
        mainPanel = new JPanel(new BorderLayout());
        controlPanel = new JPanel(new GridBagLayout());
        parallelEvalText = new JTextField("Depth of Search: ");
        parallelEvalText.setEditable(false);
        gameStatus = new JTextField("Game Status: In Progress");
        gameStatus.setEditable(false);

        board = new Board(9, 9, gameStatus);

        GridBagConstraints gbc = new GridBagConstraints();

        userInput = new JTextField();
        userInput.setPreferredSize(new Dimension(50, 25));
        evalButton = new JButton("Perform Evaluation");
        evalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int input = Integer.parseInt(userInput.getText());
                    if(input > 0) {
                        evalButton.setEnabled(false);
                        userInput.setEnabled(false);
                        // perform parallel computation
                        board.parallelSolver(input, 8);
                        userInput.setEnabled(true);
                        evalButton.setEnabled(true);
                    }
                } catch(NumberFormatException ignored) {}
            }
        });

        resetButton = new JButton("Generate New Board");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evalButton.setEnabled(false);
                userInput.setEnabled(false);
                // perform reset of board and generate a new grid
                board.reset();
                gameStatus.setText("Game Status: In Progress");
                userInput.setEnabled(true);
                evalButton.setEnabled(true);

            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(resetButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets.bottom = 150;
        gbc.insets.left = 10;
        gbc.insets.right = 10;
        controlPanel.add(gameStatus, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets.bottom = 5;
        gbc.insets.left = 15;
        gbc.insets.right = 1;
        controlPanel.add(parallelEvalText, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets.bottom = 5;
        gbc.insets.left = 1;
        gbc.insets.right = 15;
        controlPanel.add(userInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets.bottom = 1;
        gbc.insets.left = 5;
        gbc.insets.right = 5;
        controlPanel.add(evalButton, gbc);
        setTitle("Assignment 3: Game Search");

        mainPanel.add(board, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.LINE_END);

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // get screen dimensions
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension preference = new Dimension(screen.width * 2/4, screen.height * 2/3);
        setPreferredSize(preference);
        setSize(preference);
        setMinimumSize(preference);
    }



}
