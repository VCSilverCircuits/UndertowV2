package vcsc.teamcode.abstracts.abstracts;

import android.graphics.Color;

public class Block {
    private COLOR color;
    private double x, y, angle;

    public Block(COLOR color, double x, double y) {
        new Block(color, x, y, 0);
    }

    public Block(COLOR color, double x, double y, double angle) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public COLOR getColor() {
        return color;
    }

    public enum COLOR {
        RED, BLUE, YELLOW;

        Color min;
        Color max;

        public Color getMin() {
            return min;
        }

        public Color getMax() {
            return max;
        }
    }
}
