package myTetris;

import java.util.Random;

public class Block {


    public Shape pieceOrientation;
    private int[][] coords;

    public Block() {
        coords = new int[4][2];
    }

    public void setShape(Shape shape) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = shape.getCoords(i, j);
            }
        }
        pieceOrientation = shape;
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int x(int index) {
        return coords[index][0];
    }

    public int y(int index) {
        return coords[index][1];
    }

    public Shape getShape() {
        return pieceOrientation;
    }

    public void setRandomShape() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Shape[] values = Shape.values();
        setShape(values[x]);
    }

    public int minX() {
        int m = coords[0][0];

        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }

    public int minY() {
        int m = coords[0][1];

        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    //rotate shapes
    public Block rotateLeft() {
        if (pieceOrientation == Shape.SquareShape) {
            return this;
        }

            Block result = new Block();
            result.pieceOrientation = pieceOrientation;

            for (int i = 0; i < 4; i++) {
                result.setX(i, y(i));
                result.setY(i, -x(i));
            }
            return result;
        }

    public Block rotateRight() {
        if (pieceOrientation == Shape.SquareShape) {
            return this;
        }

        Block result = new Block();
        result.pieceOrientation = pieceOrientation;

        for (int i = 0; i < 4; i++) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }


}
