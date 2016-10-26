package myTetris;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Tetris extends JFrame {
    private JLabel statusBar;
    private JLabel highScore;
    private int highScoreNumber = 0;

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
            highScore = new JLabel("Highscore: " + highScoreNumber); //to display high score

        }catch(Exception ex){

        }

        add(statusBar, BorderLayout.NORTH);
        add(highScore, BorderLayout.AFTER_LAST_LINE);
        Font bigFont = statusBar.getFont().deriveFont(Font.PLAIN, 35f);
        statusBar.setFont(bigFont);
        highScore.setFont(bigFont);
        Board board = new Board(this);
        add(board);
        //start lines down
        board.start();

        //add one piece
        board.newPiece();
        board.repaint();

        setSize(300,600);
        setTitle("My Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public JLabel getStatusBar(){
        return statusBar;
    }

    public int getHighScoreNumber() {return highScoreNumber; }

    public static void main(String[] args) {
        Tetris myTetris = new Tetris();
        myTetris.setLocationRelativeTo(null); //center
        myTetris.setVisible(true);
    }
}
