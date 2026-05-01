package catchorcrash;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class FallingObject {

    public static final int COOKIE = 0;
    public static final int LAPTOP = 1;
    public static final int DOLLAR_BILL = 2;
    public static final int A_PAPER = 3;
    public static final int WIFI_FAIL = 4;
    public static final int LOW_BATTERY = 5;
    public static final int F_PAPER = 6;
    public static final int CODE_ERROR = 7;
    public static final int POP_QUIZ = 8;

    private int x;
    private int y;
    private int speed;
    private int type;
    private int size;

    public FallingObject(int x, int y, int speed, int type) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.type = type;
        this.size = 34;
    }

    public void fall() {
        y += speed;
    }

    public boolean isGood() {
        return type <= A_PAPER;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public void draw(Graphics2D g2) {
        if (type == COOKIE) {
            drawCookie(g2);
        } else if (type == LAPTOP) {
            drawLaptop(g2);
        } else if (type == DOLLAR_BILL) {
            drawDollarBill(g2);
        } else if (type == A_PAPER) {
            drawAPaper(g2);
        } else if (type == WIFI_FAIL) {
            drawWifiFail(g2);
        } else if (type == LOW_BATTERY) {
            drawLowBattery(g2);
        } else if (type == F_PAPER) {
            drawFPaper(g2);
        } else if (type == CODE_ERROR) {
            drawCodeError(g2);
        } else {
            drawPopQuiz(g2);
        }
    }

    private void drawCookie(Graphics2D g2) {
        g2.setColor(new Color(191, 137, 85));
        g2.fillOval(x, y, size, size);
        g2.setColor(new Color(88, 46, 25));
        for (int i = 0; i < 5; i++) {
            int chipX = x + 6 + (i * 4);
            int chipY = y + 7 + ((i % 3) * 7);
            g2.fillOval(chipX, chipY, 4, 4);
        }
    }

    private void drawLaptop(Graphics2D g2) {
        g2.setColor(new Color(98, 119, 161));
        g2.fillRect(x + 4, y + 2, 24, 16);
        g2.setColor(new Color(160, 170, 185));
        g2.fillRect(x, y + 18, 32, 10);
        g2.drawLine(x + 4, y + 18, x + 28, y + 18);
    }

    private void drawDollarBill(Graphics2D g2) {
        g2.setColor(new Color(120, 191, 118));
        g2.fillRoundRect(x + 2, y + 8, 30, 18, 4, 4);
        g2.setColor(new Color(79, 135, 77));
        g2.drawRoundRect(x + 2, y + 8, 30, 18, 4, 4);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("$", x + 12, y + 22);
        g2.drawOval(x + 6, y + 12, 4, 4);
        g2.drawOval(x + 24, y + 18, 4, 4);
    }

    private void drawAPaper(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(x + 5, y + 3, 24, 28);
        g2.setColor(new Color(40, 122, 72));
        g2.drawRect(x + 5, y + 3, 24, 28);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("A+", x + 8, y + 21);
    }

    private void drawWifiFail(Graphics2D g2) {
        g2.setColor(new Color(90, 90, 90));
        g2.fillRect(x + 9, y + 22, 16, 5);
        g2.drawArc(x + 8, y + 10, 18, 18, 25, 130);
        g2.drawArc(x + 4, y + 6, 26, 26, 30, 120);
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x + 5, y + 4, x + 28, y + 28);
        g2.drawLine(x + 28, y + 4, x + 5, y + 28);
        g2.setStroke(new BasicStroke(1));
    }

    private void drawLowBattery(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawRect(x + 4, y + 8, 24, 16);
        g2.drawRect(x + 28, y + 13, 3, 6);
        g2.setColor(Color.RED);
        g2.fillRect(x + 7, y + 11, 6, 10);
    }

    private void drawFPaper(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(x + 5, y + 3, 24, 28);
        g2.setColor(Color.RED);
        g2.drawRect(x + 5, y + 3, 24, 28);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("F", x + 13, y + 22);
    }

    private void drawCodeError(Graphics2D g2) {
        g2.setColor(new Color(232, 232, 232));
        g2.fillRoundRect(x + 2, y + 4, 30, 24, 8, 8);
        g2.setColor(new Color(61, 61, 61));
        g2.drawRoundRect(x + 2, y + 4, 30, 24, 8, 8);
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.drawString("404", x + 6, y + 20);
        g2.setColor(Color.RED);
        g2.drawLine(x + 4, y + 6, x + 30, y + 26);
    }

    private void drawPopQuiz(Graphics2D g2) {
        g2.setColor(new Color(214, 38, 38));
        g2.fillOval(x + 4, y + 4, 26, 26);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x + 11, y + 11, x + 23, y + 23);
        g2.drawLine(x + 23, y + 11, x + 11, y + 23);
        g2.setStroke(new BasicStroke(1));
    }

    public int getY() {
        return y;
    }
}
