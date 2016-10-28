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
        add(statusBar, BorderLayout.NORTH);
        add(highScoreDisplay, BorderLayout.AFTER_LAST_LINE);
        Board board = new Board(this);
        add(board);
        //start lines down
        board.start();

        //add one piece
        board.newPiece();
        board.repaint();
        //createView();
        Font bigFont = statusBar.getFont().deriveFont(Font.PLAIN, 20f);
        statusBar.setFont(bigFont);
        highScoreDisplay.setFont(bigFont);

        setSize(300,600);
        setTitle("My Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void createView(){
        JPanel panelMain = new JPanel();
        getContentPane().add(panelMain);

        JPanel panelTetris = new JPanel(new GridBagLayout());
        JPanel panelRight = new JPanel(new GridBagLayout());
        panelMain.add(panelTetris);
        panelMain.add(panelRight);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;



        panelMain.add(statusBar, constraints);
        //add(highScoreDisplay, BorderLayout.AFTER_LAST_LINE);

        panelRight.add(new JLabel("Next: "), constraints);


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
