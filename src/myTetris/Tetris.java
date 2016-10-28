package myTetris;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Tetris extends JFrame {
    private JLabel statusBar;
    private JLabel highScoreDisplay;
    private int highScoreNumber = 0;
    private Board board;

    public Tetris() {
        statusBar = new JLabel("0"); //to display lines number
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream("Highscore.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            if(properties.containsKey("score")){
                highScoreNumber = Integer.parseInt(properties.getProperty("score"));
            }
            highScoreDisplay = new JLabel("Highscore: " + highScoreNumber); //to display high score

        }catch(Exception ex){
            highScoreNumber = 0;
            highScoreDisplay = new JLabel("Highscore: " + highScoreNumber); //to display high score
        }
        //add(statusBar, BorderLayout.NORTH);
        //add(highScoreDisplay, BorderLayout.AFTER_LAST_LINE);

        board = new Board(this);

        createView();

        //add(board);
        //start lines down
        board.start();

        //add one piece
        board.newPiece();
        board.repaint();
        //createView();
        Font bigFont = statusBar.getFont().deriveFont(Font.PLAIN, 20f);
        statusBar.setFont(bigFont);
        highScoreDisplay.setFont(bigFont);

        setSize(500,800);
        setTitle("My Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void createView() {
        JPanel panelMain = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 20;

        panelMain.add(statusBar, constraints);

        constraints.gridx = 1;
        panelMain.add(highScoreDisplay, constraints);

        //Main Tetris board settings start

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 2;
        constraints.gridwidth = 1;
        constraints.fill=GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        //Main Tetris board settings end

        panelMain.add(board, constraints);

        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        constraints.gridx = 1;
        constraints.gridy = 1;

        panelMain.add(new JLabel("Next item to go here"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.ipady = 300;

        panelMain.add(new JLabel("Hold item to go here"), constraints);

        getContentPane().add(panelMain);

    }

    public JLabel getStatusBar(){
        return statusBar;
    }

    public int getHighScoreNumber() {return highScoreNumber; }

    public JLabel getHighScoreLabel() {return highScoreDisplay; }

    public static void main(String[] args) {
        Tetris myTetris = new Tetris();
        myTetris.setLocationRelativeTo(null); //center
        myTetris.setVisible(true);
    }
}
