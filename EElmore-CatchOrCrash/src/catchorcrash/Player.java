package catchorcrash;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;

public class Player {

    public static final String[] CHARACTER_NAMES = { "Milo Mushroom", "Luna Linda", "Ghosty Greg", "Pirate Pete",
            "Balloon Bella", "Robo-Stick 3000" };
 //Character movements
    private int x;
    private int y;
    private int width;
    private int speed;
    private int characterType;

    public Player(int x, int y, int characterType) {
        this.x = x;
        this.y = y;
        this.width = 90;
        this.speed = 10;
        this.characterType = characterType;
    }

    public void moveLeft() {
        x -= speed;

        if (x < 0) {
            x = 0;
        }
    }

    public void moveRight(int panelWidth) {
        x += speed;

        if (x > panelWidth - width) {
            x = panelWidth - width;
        }
    }

    public Rectangle getCatchBox() {
        return new Rectangle(x + 8, y + 65, width - 16, 30);
    }

    public void draw(Graphics2D g2, boolean squished) { //Death scene for each character
        if (squished) {
            drawSquished(g2);
            return;
        }

        if (characterType == 0) {
            drawMiloMushroom(g2);
        } else if (characterType == 1) {
            drawLunaLinda(g2);
        } else if (characterType == 2) {
            drawGhost(g2);
        } else if (characterType == 3) {
            drawPirate(g2);
        } else if (characterType == 4) {
            drawBalloon(g2);
        } else {
            drawRobot(g2);
        }
    }

    private void drawMiloMushroom(Graphics2D g2) { // Mushroom Character
        g2.setColor(new Color(209, 70, 70));
        g2.fillOval(x + 15, y + 6, 60, 34);
        g2.setColor(Color.WHITE);
        g2.fillOval(x + 25, y + 14, 8, 8);
        g2.fillOval(x + 52, y + 12, 7, 7);
        g2.fillOval(x + 44, y + 22, 7, 7);
        g2.setColor(new Color(245, 229, 195));
        g2.fillRect(x + 30, y + 38, 30, 26);
        g2.fillOval(x + 26, y + 52, 38, 18);
        g2.setColor(Color.BLACK);
        g2.fillOval(x + 37, y + 45, 5, 5);
        g2.fillOval(x + 48, y + 45, 5, 5);
        g2.drawArc(x + 39, y + 52, 12, 7, 180, 180);
        drawBasket(g2, new Color(160, 117, 83));
    }

    private void drawLunaLinda(Graphics2D g2) { // Moon Character
        g2.setColor(new Color(198, 198, 202));
        g2.fillArc(x + 10, y + 12, 66, 72, 40, 260);
        g2.setColor(new Color(165, 165, 170));
        g2.drawArc(x + 14, y + 16, 54, 60, 40, 250);
        g2.fillOval(x + 34, y + 19, 7, 7);
        g2.fillOval(x + 28, y + 41, 6, 6);
        g2.fillOval(x + 43, y + 43, 7, 7);
        g2.setColor(Color.BLACK);
        g2.fillOval(x + 36, y + 28, 5, 5);
        g2.fillOval(x + 48, y + 28, 5, 5);
        g2.drawArc(x + 38, y + 38, 12, 8, 180, 180);
        drawBasket(g2, new Color(146, 150, 157));
    }

    private void drawGhost(Graphics2D g2) { // Ghost Character
        g2.setColor(new Color(240, 240, 255));
        g2.fillOval(x + 18, y + 8, 54, 58);
        g2.fillRect(x + 18, y + 38, 54, 28);
        for (int i = 0; i < 4; i++) {
            g2.fillOval(x + 18 + (i * 14), y + 52, 16, 20);
        }
        g2.setColor(Color.BLACK);
        g2.fillOval(x + 34, y + 28, 6, 8);
        g2.fillOval(x + 50, y + 28, 6, 8);
        g2.fillOval(x + 42, y + 42, 8, 10);
        drawBasket(g2, new Color(182, 208, 230));
    }

    private void drawPirate(Graphics2D g2) { // Pirate Character
        g2.setColor(new Color(244, 205, 168));
        g2.fillOval(x + 22, y + 10, 46, 46);
        g2.setColor(new Color(30, 30, 30));
        g2.fillRect(x + 18, y + 8, 54, 10);
        int[] hatX = { x + 22, x + 45, x + 68 };
        int[] hatY = { y + 18, y - 4, y + 18 };
        g2.fillPolygon(hatX, hatY, 3);
        g2.fillOval(x + 35, y + 30, 10, 8);
        g2.drawLine(x + 30, y + 34, x + 46, y + 34);
        g2.fillOval(x + 53, y + 31, 4, 4);
        g2.drawArc(x + 40, y + 42, 14, 8, 180, 180);
        g2.setColor(new Color(177, 62, 42));
        g2.fillRect(x + 28, y + 56, 34, 30);
        drawBasket(g2, new Color(133, 82, 44));
    }

    private void drawBalloon(Graphics2D g2) { // Balloon Character
        g2.setColor(new Color(255, 115, 130));
        g2.fillOval(x + 18, y - 6, 54, 54);
        g2.setColor(Color.BLACK);
        g2.fillOval(x + 34, y + 14, 5, 5);
        g2.fillOval(x + 50, y + 14, 5, 5);
        g2.drawArc(x + 38, y + 24, 12, 8, 180, 180);
        g2.setColor(new Color(85, 65, 80));
        g2.drawLine(x + 45, y + 48, x + 45, y + 78);
        drawBasket(g2, new Color(134, 97, 187));
    }

    private void drawRobot(Graphics2D g2) { // Robot Character
        g2.setColor(new Color(175, 181, 194));
        g2.fillRect(x + 24, y + 8, 42, 38);
        g2.fillRect(x + 28, y + 48, 34, 30);
        g2.setColor(new Color(56, 84, 145));
        g2.fillRect(x + 33, y + 22, 7, 7);
        g2.fillRect(x + 50, y + 22, 7, 7);
        g2.setColor(Color.BLACK);
        g2.drawLine(x + 45, y + 8, x + 45, y - 6);
        g2.fillOval(x + 42, y - 10, 6, 6);
        drawBasket(g2, new Color(130, 142, 160));
    }

    private void drawBasket(Graphics2D g2, Color color) { 
        g2.setColor(color);
        g2.fill(new Arc2D.Double(x + 14, y + 66, 62, 32, 0, -180, Arc2D.CHORD));
        g2.drawLine(x + 20, y + 64, x + 70, y + 64);
    }

    private void drawSquished(Graphics2D g2) {  //Death scene 
        g2.setColor(new Color(170, 70, 70));
        g2.fillOval(x + 10, y + 86, 70, 18);
        g2.setColor(new Color(255, 232, 143));
        g2.fillOval(x + 6, y + 80, 12, 12);
        g2.fillOval(x + 72, y + 82, 10, 10);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
