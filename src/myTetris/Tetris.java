package myTetris;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Tetris extends JFrame {
    private JLabel scoreBar;
    private JLabel highScoreDisplay;
    private JLabel levelDisplay;
    private int highScoreNumber = 0;
    private int currentScoreNumber = 0;
    public int level = 1;
    private Board board;

    public Tetris() {
        scoreBar = new JLabel("Score: " + currentScoreNumber); //to display lines number

        if(currentScoreNumber > 10){
            level = (currentScoreNumber/10);
        }

        levelDisplay = new JLabel("Level: " + level); //to display level



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

        board = new Board(this);

        createView();
        pack();
        board.start();
        board.setMinimumSize(new Dimension(300,600));
        board.setPreferredSize(new Dimension(300, 600));


        //add one piece
        board.newPiece();
        board.repaint();

        Font bigFont = scoreBar.getFont().deriveFont(Font.PLAIN, 20f);
        scoreBar.setFont(bigFont);
        levelDisplay.setFont(bigFont);
        highScoreDisplay.setFont(bigFont);

        setSize(650,800);
        setTitle("My Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public void createView() {
        JPanel panelMain = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        panelMain.add(scoreBar, constraints);

        constraints.gridx=1;
        panelMain.add(levelDisplay,constraints);

        constraints.gridx = 2;
        panelMain.add(highScoreDisplay, constraints);

        //Main Tetris board settings start

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.gridheight = 2;
        constraints.gridwidth = 2;
//        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill=GridBagConstraints.BOTH;
//        constraints.weightx = 1;
//        constraints.weighty = 1;

        //Main Tetris board settings end

        panelMain.add(board, constraints);

        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.gridx = 2;
        constraints.gridy = 1;

        //Add next piece functionality

        try{
            panelMain.add(new JLabel("Next piece: " + board.getCurrentPiece()), constraints);
        }catch(Exception ex){
            panelMain.add(new JLabel("Next piece: " + "none"), constraints);
        }

        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.ipady = 300;

        panelMain.add(new JLabel("Hold item to go here"), constraints);

        getContentPane().add(panelMain);

    }

    public JLabel getScoreBar(){
        return scoreBar;
    }

    public JLabel getLevelBar(){
        return levelDisplay;
    }

    public int getHighScoreNumber() {return highScoreNumber; }

    public JLabel getHighScoreLabel() {return highScoreDisplay; }

    public static void main(String[] args) {
        Tetris myTetris = new Tetris();
        myTetris.setLocationRelativeTo(null); //center
        myTetris.setVisible(true);
    }



}
