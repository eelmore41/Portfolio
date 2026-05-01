package catchorcrash;

import java.awt.Color;
import java.awt.Graphics2D;

public class ConfettiPiece {

    private int x;
    private int y;
    private int speed;
    private int size;
    private Color color;

    public ConfettiPiece() {
        reset();
        y = -(int) (Math.random() * 500);
    }

    public void fall() {
        y += speed;

        if (y > GamePanel.HEIGHT) {
            reset();
            y = -10;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect(x, y, size, size + 3);
    }

    private void reset() {
        x = (int) (Math.random() * GamePanel.WIDTH);
        y = (int) (Math.random() * GamePanel.HEIGHT);
        speed = 3 + (int) (Math.random() * 5);
        size = 6 + (int) (Math.random() * 6);

        Color[] colors = { new Color(255, 87, 87), new Color(255, 201, 87), new Color(87, 181, 255),
                new Color(89, 207, 142), new Color(194, 120, 255) };
        color = colors[(int) (Math.random() * colors.length)];
    }
}
