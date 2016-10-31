package myTetris;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;

public class Board extends JPanel implements ActionListener {

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 22;
    private static final Color[] COLORS = { new Color(0,0,0),
        new Color(204, 102, 102), new Color(102,204,102),
        new Color(102, 102, 204), new Color(204, 204, 102),
        new Color(204, 102, 204), new Color(102, 204, 204),
        new Color(218, 170, 0) };
    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isStarted = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private int nextLevelLines = 16;
    private int timerNumber = 500;
    private JLabel statusBar;
    private JLabel levelBar;
    private JLabel highScoreDisplay;
    private int currentRecord;
    private int level = 1;

    private Shape currentPiece;
    private Tetrominos[] board;

    public Board(Tetris tetris){
        setFocusable(true);
        currentPiece = new Shape();
        timer = new Timer(timerNumber,this); //timer for lines down
        statusBar = tetris.getScoreBar();
        levelBar = tetris.getLevelBar();
        currentRecord = tetris.getHighScoreNumber();
        highScoreDisplay = tetris.getHighScoreLabel();
        board = new Tetrominos[BOARD_WIDTH * BOARD_HEIGHT];
        this.setBorder(new BevelBorder(BevelBorder.RAISED,Color.BLACK,Color.BLACK));
        clearBoard();
        addKeyListener(new MyTetrisAdapter());
    }

    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++){
            board[i] = Tetrominos.NoShape;
        }
    }

    public int squareWidth(){
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    public int squareHeight(){
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    public Tetrominos shapeAt(int rowIndex, int columnIndex){
        return board[columnIndex * BOARD_WIDTH + rowIndex];
    }

    private void pieceDropped(){
        for(int i=0; i<4; i++){
            int x = curX + currentPiece.x(i);
            int y = curY - currentPiece.y(i);
            setBoardPiece(currentPiece.getShape(), x, y);
        }

        removeFullLines();

        if(!isFallingFinished){
            newPiece();
        }

    }

    private void setBoardPiece(Tetrominos shape, int row, int column) {
        board[column * BOARD_WIDTH + row] = shape;
    }

    public void newPiece() {
        currentPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + currentPiece.minX();
        curY = BOARD_HEIGHT + currentPiece.minY();

        if (!tryMove(currentPiece, curX + 2, curY - 1)) {
            timer.stop();
            isStarted = false;
            statusBar.setText("Game Over");
            updateHighscore();
        }

    }

    private void updateHighscore() {
        if (numLinesRemoved > currentRecord){
            OutputStream output = null;
            try {
                output = new FileOutputStream("Highscore.properties");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Properties properties = new Properties();
            properties.setProperty("score", String.valueOf(numLinesRemoved));
            highScoreDisplay.setText("New Highscore!");
            try {
                properties.store(output, "");
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error reading properties file");
            }
        }
    }

    private void oneLineDown(){
            if(!tryMove(currentPiece, curX, curY-1)){
                pieceDropped();
            }
    }


    @Override
    public void actionPerformed(ActionEvent ae) {
        if(isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        }
        else{
            oneLineDown();
        }
    }

    //Draw Tetrominos
    public void drawSquare(Graphics2D g, int x, int y, Tetrominos shape){
        Color color = COLORS[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x+1,y+1,squareWidth()-2, squareHeight()-2);
        g.setColor(color.brighter());
        g.drawLine(x, y+squareHeight()-1,x,y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x+1, y+squareHeight()-1, x + squareWidth() -1, y + squareHeight() -1);
        g.drawLine(x+squareWidth() -1, y+squareHeight() - 1, x+squareWidth() -2, y+1 );
    }

    //paint the board
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        HashMap<RenderingHints.Key, Object> map = new HashMap();
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(renderingHints);
        Dimension size = getSize();

        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for(int i=0; i<BOARD_HEIGHT; i++){
            for(int j = 0; j<BOARD_WIDTH; ++j){
                Tetrominos shape = shapeAt(j, BOARD_HEIGHT - i - 1);

                if(shape != Tetrominos.NoShape){
                    drawSquare(g2d, j*squareWidth(), boardTop + i * squareHeight(), shape);
                }
            }
        }

        if (currentPiece.getShape() != Tetrominos.NoShape){
            for(int i =0; i<4; ++i){
                int x = curX + currentPiece.x(i);
                int y = curY - currentPiece.y(i);
                drawSquare(g2d, x * squareWidth(), boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(), currentPiece.getShape());
                //System.out.println(currentPiece.getShape());
            }
        }

    }

    public String getCurrentPiece(){
        return currentPiece.getShape().toString();
    }

    public void start(){
        if(isPaused){
            return;
        }

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();
        newPiece();
        timer.start(); //start javax timer that call actPerf
    }

    public void pause(){
        if(!isStarted){
            return;}
        isPaused = !isPaused;

        if(isPaused){
            timer.stop();
            statusBar.setText("Paused");
        }else{
            timer.start();
            statusBar.setText(String.valueOf("Score: " + numLinesRemoved));
        }
        repaint();
    }

    private boolean tryMove(Shape newPiece, int newX, int newY){
        for(int i=0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }

            if (shapeAt(x, y) != Tetrominos.NoShape) {
                return false;
            }
        }

        currentPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();

        return true;
    }

    private void removeFullLines(){
        int numFullLines = 0;

        for(int rowIndex = BOARD_HEIGHT - 1; rowIndex >=0; --rowIndex){
            boolean lineIsFull = isLineFull(rowIndex);

            if (lineIsFull){
                ++numFullLines;

                removeFullRow(rowIndex);
            }

            if (numFullLines > 0){
                numLinesRemoved += numFullLines;
                statusBar.setText(String.valueOf("Score: " + numLinesRemoved));

                if(numLinesRemoved >= nextLevelLines){
                    level = level + 1;
                    nextLevelLines = (int)(nextLevelLines * 1.5);
                    timer.stop();
                    timerNumber = timerNumber - 40;
                    timer = new Timer(timerNumber,this);
                    timer.start();
                    System.out.println(timerNumber);
                }

                levelBar.setText(String.valueOf("Level: " + level));
                isFallingFinished = true;
                currentPiece.setShape(Tetrominos.NoShape);
                repaint();
            }
        }
    }


    private void removeFullRow(int rowIndex) {
        for(int secondaryRowIndex = rowIndex; secondaryRowIndex < BOARD_HEIGHT - 1; ++secondaryRowIndex){
            for(int columnIndex = 0; columnIndex < BOARD_WIDTH; ++columnIndex){
                Tetrominos blockAbove = shapeAt(columnIndex, secondaryRowIndex + 1);
                setBoardPiece(blockAbove, columnIndex, secondaryRowIndex);
            }
        }
    }

    private boolean isLineFull(int i) {
        for(int j=0; j<BOARD_WIDTH; ++j){
            if(shapeAt(j,i) == Tetrominos.NoShape){
                return false;
            }
        }
        return true;
    }

    //add drop down method
    private void dropDown(){
        int newY = curY;

        while (newY > 0){
            if (!tryMove(currentPiece, curX, newY - 1)){
                break;
            }
            --newY;
        }
        pieceDropped();
    }


    public class MyTetrisAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke){
            if(!isStarted || currentPiece.getShape() == Tetrominos.NoShape){
                return;
            }

            int keyCode = ke.getKeyCode();

            if(keyCode == 'p' || keyCode == 'P'){
                pause();
            }

            if(isPaused){
                return;
            }

            switch(keyCode){
                case KeyEvent.VK_LEFT :
                    tryMove(currentPiece, curX -1, curY);
                    break;
                case KeyEvent.VK_RIGHT :
                    tryMove(currentPiece, curX + 1, curY);
                    break;
                case KeyEvent.VK_DOWN :
                    tryMove(currentPiece, curX, curY-1);
                    break;
                case KeyEvent.VK_SPACE :
                    tryMove(currentPiece.rotateLeft(), curX, curY);
                    break;
                case KeyEvent.VK_UP :
                    dropDown();
                    break;
                case 'd' :
                    oneLineDown();
                    break;
                case 'D' :
                    oneLineDown();
            }
        }
    }
}
