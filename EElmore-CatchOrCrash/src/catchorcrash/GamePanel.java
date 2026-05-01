package catchorcrash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener { //Layout for game modes and good/bad items 

    public static final int WIDTH = 900;
    public static final int HEIGHT = 650;

    public static final int LEVEL_MODE = 1;
    public static final int SURVIVAL_MODE = 2;

    private static final int GROUND_Y = 540;

    private GameFrame frame;
    private Timer timer;
    private Player player;
    private ArrayList<FallingObject> objects;
    private ArrayList<ConfettiPiece> confetti;
    private HighScoreManager highScoreManager;

    private int gameMode;
    private int level;
    private int score;
    private int mistakes;
    private int spawnCounter;
    private int survivalTicks;

    private boolean moveLeft;
    private boolean moveRight;
    private boolean paused;
    private boolean gameRunning;
    private boolean gameOver;
    private boolean winState;
    private boolean titleScreen;
    private boolean endMenuShown;

    private int slamY;
    private int animationTimer;
    private int levelBannerTimer;
    private String levelBannerText;

    private int catchY;
    private int orY;
    private int crashY;
    private int titleTimer;
    private boolean titleFinished;
    private boolean titleMenuOpened;
    private boolean deathSoundPlayed;

    public GamePanel(GameFrame frame) {
        this.frame = frame;
        this.timer = new Timer(20, this);
        this.objects = new ArrayList<FallingObject>();
        this.confetti = new ArrayList<ConfettiPiece>();
        this.highScoreManager = new HighScoreManager();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        timer.start();
    }

    public void showTitleScreen() {
        stopGame();
        titleScreen = true;
        gameOver = false;
        winState = false;
        endMenuShown = false;
        titleFinished = false;
        titleMenuOpened = false;
        titleTimer = 0;

        catchY = -120;
        orY = -120;
        crashY = -120;

        requestFocusInWindow();
        repaint();
    }

    public void startGame(int mode, int selectedCharacter) {
        this.gameMode = mode;
        this.player = new Player(400, GROUND_Y, selectedCharacter);
        this.objects.clear();
        this.confetti.clear();

        this.level = 1;
        this.score = 0;
        this.mistakes = 0;
        this.spawnCounter = 0;
        this.survivalTicks = 0;
        this.levelBannerTimer = 90;
        this.levelBannerText = mode == LEVEL_MODE ? "LEVEL 1" : "SURVIVAL MODE";

        this.moveLeft = false;
        this.moveRight = false;
        this.paused = false;
        this.gameRunning = true;
        this.gameOver = false;
        this.winState = false;
        this.titleScreen = false;
        this.endMenuShown = false;

        this.slamY = -160;
        this.animationTimer = 0;
        this.deathSoundPlayed = false;

        requestFocusInWindow();
        repaint();
    }

    public void stopGame() {
        gameRunning = false;
        paused = false;
        moveLeft = false;
        moveRight = false;
        objects.clear();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (titleScreen) {
            updateTitleScreen();
            repaint();
            return;
        }

        if (paused) {
            repaint();
            return;
        }

        if (gameRunning) {
            updatePlayer();
            spawnObjects();
            updateObjects();
            checkLevelProgress();

            if (gameMode == SURVIVAL_MODE) {
                survivalTicks++;
            }

            if (levelBannerTimer > 0) {
                levelBannerTimer--;
            }
        } else if (gameOver) {
            updateGameOverAnimation();
        } else if (winState) {
            updateWinAnimation();
        }

        repaint();
    }

    private void updateTitleScreen() {
        titleTimer++;

        if (catchY < 220) {
            catchY += 16;
            if (catchY > 220) {
                catchY = 220;
            }
        } else if (orY < 320) {
            orY += 16;
            if (orY > 320) {
                orY = 320;
            }
        } else if (crashY < 430) {
            crashY += 16;
            if (crashY > 430) {
                crashY = 430;
            }
        } else {
            titleFinished = true;
        }
    }

    private void updatePlayer() {
        if (moveLeft) {
            player.moveLeft();
        }

        if (moveRight) {
            player.moveRight(WIDTH);
        }
    }

    private void spawnObjects() {
        spawnCounter++;

        int spawnDelay = 38 - (level * 6);
        if (spawnDelay < 12) {
            spawnDelay = 12;
        }

        if (spawnCounter >= spawnDelay) {
            spawnCounter = 0;

            //Decides if the next falling item is good or bad
            boolean badItem = false;
            int badChance = 29 + (level * 8);

            if (gameMode == SURVIVAL_MODE) {
                badChance += 7;
            }

            if (Math.random() * 100 < badChance) {
                badItem = true;
            }

            int type;
            if (badItem) {
                type = 4 + (int) (Math.random() * 5);
            } else {
                type = (int) (Math.random() * 4);
            }

            int speed = 4 + level + (int) (Math.random() * 2);
            if (gameMode == SURVIVAL_MODE) {
                speed += 1;
            } else if (level >= 2) {
                speed += 1;
            }
            int x = 30 + (int) (Math.random() * (WIDTH - 100));

            objects.add(new FallingObject(x, -40, speed, type));
        }
    }

    private void updateObjects() {
        Rectangle playerBox = player.getCatchBox();

        //Checks every falling item against the basket hit box
        for (int i = objects.size() - 1; i >= 0; i--) {
            FallingObject object = objects.get(i);
            object.fall();

            if (object.getBounds().intersects(playerBox)) {
                if (object.isGood()) {
                    score++;
                    ToolkitSound.playGoodSound();
                } else {
                    handleBadHit();
                }

                objects.remove(i);
            } else if (object.getY() > HEIGHT) {
                objects.remove(i);
            }
        }
    }

    private void handleBadHit() {
        if (gameMode == SURVIVAL_MODE) {
            triggerGameOver();
            return;
        }

        ToolkitSound.playBadSound();

        mistakes++;

        if (mistakes >= 3) {
            triggerGameOver();
        }
    }

    private void checkLevelProgress() {
        if (gameMode != LEVEL_MODE || gameOver) {
            return;
        }

        if (score >= 8 && level == 1) {
            level = 2;
            levelBannerText = "LEVEL 2";
            levelBannerTimer = 90;
            ToolkitSound.playLevelUp();
        } else if (score >= 18 && level == 2) {
            level = 3;
            levelBannerText = "LEVEL 3";
            levelBannerTimer = 90;
            ToolkitSound.playLevelUp();
        } else if (score >= 30 && level == 3) {
            triggerWin();
        }
    }

    private void triggerGameOver() {
        gameRunning = false;
        gameOver = true;
        winState = false;
        animationTimer = 0;
        slamY = -160;
        endMenuShown = false;
        deathSoundPlayed = false;

        if (gameMode == SURVIVAL_MODE) {
            int seconds = survivalTicks / 50;
            highScoreManager.saveIfHigher(seconds);
        }
    }

    private void triggerWin() {
        gameRunning = false;
        gameOver = false;
        winState = true;
        animationTimer = 0;
        endMenuShown = false;
        createConfetti();
        ToolkitSound.playCongratsSound();
    }

    private void updateGameOverAnimation() {
        animationTimer++;

        //Game over scene
        if (slamY < player.getY() - 6) {
            slamY += 18;
            if (slamY > player.getY() - 6) {
                slamY = player.getY() - 6;
            }
        }

        if (animationTimer >= 25 && !deathSoundPlayed) {
            deathSoundPlayed = true;
            ToolkitSound.playDeathSound();
        }

        if (animationTimer > 70 && !endMenuShown) {
            endMenuShown = true;
            if (gameMode == SURVIVAL_MODE) {
                int seconds = survivalTicks / 50;
                frame.askToRestart(gameMode, "The End", "Your survival time was: " + seconds + " seconds.");
            } else {
                frame.askToRestart(gameMode, "Crash!", "Too many mistakes. Try that level run again?");
            }
        }
    }

    private void createConfetti() {
        confetti.clear();

        //Win screen confetti
        for (int i = 0; i < 70; i++) {
            confetti.add(new ConfettiPiece());
        }
    }

    private void updateWinAnimation() {
        animationTimer++;

        for (int i = 0; i < confetti.size(); i++) {
            confetti.get(i).fall();
        }

        if (animationTimer > 120 && !endMenuShown) {
            endMenuShown = true;
            frame.askToRestart(gameMode, "Congrats!", "You beat Level Mode.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (titleScreen) {
            drawTitleScreen(g2);
            return;
        }

        drawBackground(g2);
        drawHud(g2);

        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).draw(g2);
        }

        if (player != null) {
            player.draw(g2, gameOver && slamY >= player.getY() - 6);
        }

        if (levelBannerTimer > 0 && levelBannerText != null) {
            drawLevelBanner(g2);
        }

        if (paused) {
            g2.setColor(new Color(0, 0, 0, 140));
            g2.fillRect(0, 0, WIDTH, HEIGHT);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 42));
            g2.drawString("PAUSED", 355, 280);
        }

        if (gameOver) {
            drawGameOverAnimation(g2);
        }

        if (winState) {
            drawWinAnimation(g2);
        }
    }

    private void drawTitleScreen(Graphics2D g2) {
        //Main title animation
        GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 241, 211), 0, HEIGHT,
                new Color(184, 220, 255));
        g2.setPaint(gradient);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        g2.setColor(new Color(255, 255, 255, 150));
        for (int i = 0; i < WIDTH; i += 140) {
            g2.fillOval(i, 60, 110, 38);
        }

        g2.setColor(new Color(37, 52, 92));
        g2.setFont(new Font("Arial", Font.BOLD, 82));
        g2.drawString("Catch", 270, catchY);

        g2.setColor(new Color(199, 72, 72));
        g2.setFont(new Font("Arial", Font.BOLD, 62));
        g2.drawString("or", 405, orY);

        g2.setColor(new Color(29, 110, 82));
        g2.setFont(new Font("Arial", Font.BOLD, 90));
        g2.drawString("Crash", 250, crashY);

        if (titleFinished && (titleTimer / 20) % 2 == 0) {
            g2.setFont(new Font("Arial", Font.BOLD, 28));
            g2.drawString("Press ENTER", 356, 575);
        }
    }

    private void drawBackground(Graphics2D g2) {
        Color skyTop;
        Color skyBottom;
        Color cloudColor;
        Color groundColor;
        Color hillColor;

        if (gameMode == SURVIVAL_MODE) {
            skyTop = new Color(201, 231, 255);
            skyBottom = new Color(236, 246, 255);
            cloudColor = new Color(255, 255, 255, 185);
            groundColor = new Color(210, 219, 168);
            hillColor = new Color(154, 183, 132);
        } else if (level == 1) {
            skyTop = new Color(204, 233, 255);
            skyBottom = new Color(238, 247, 255);
            cloudColor = new Color(255, 255, 255, 180);
            groundColor = new Color(199, 230, 189);
            hillColor = new Color(165, 209, 150);
        } else if (level == 2) {
            skyTop = new Color(255, 203, 157);
            skyBottom = new Color(255, 235, 194);
            cloudColor = new Color(255, 246, 222, 180);
            groundColor = new Color(209, 187, 142);
            hillColor = new Color(187, 151, 109);
        } else {
            skyTop = new Color(34, 46, 90);
            skyBottom = new Color(92, 116, 175);
            cloudColor = new Color(195, 210, 255, 120);
            groundColor = new Color(110, 125, 108);
            hillColor = new Color(79, 95, 84);
        }

        GradientPaint gradient = new GradientPaint(0, 0, skyTop, 0, HEIGHT, skyBottom);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        g2.setColor(cloudColor);
        for (int i = 0; i < WIDTH; i += 150) {
            g2.fillOval(i, 65, 110, 34);
        }

        g2.setColor(hillColor);
        g2.fillOval(-60, GROUND_Y + 25, 360, 150);
        g2.fillOval(210, GROUND_Y + 20, 300, 140);
        g2.fillOval(500, GROUND_Y + 15, 340, 145);

        g2.setColor(groundColor);
        g2.fillRect(0, GROUND_Y + 90, WIDTH, HEIGHT - GROUND_Y - 90);
    }

    private void drawHud(Graphics2D g2) {
        g2.setColor(new Color(20, 27, 45));
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Score: " + score, 20, 30);

        if (gameMode == LEVEL_MODE) {
            g2.drawString("Level: " + level, 20, 58);
            g2.drawString("Mistakes: " + mistakes + "/3", 20, 86);
        }

        if (gameMode == SURVIVAL_MODE) {
            int seconds = survivalTicks / 50;
            g2.drawString("Time: " + seconds + "s", 20, 58);
            g2.drawString("Best: " + highScoreManager.getHighScore() + "s", 20, 86);
        }

        g2.drawString("P = Pause", WIDTH - 170, 30);
        g2.drawString("E = Exit", WIDTH - 160, 58);
    }

    private void drawLevelBanner(Graphics2D g2) {
        Font bannerFont = new Font("Arial", Font.BOLD, 36);
        int bannerWidth = g2.getFontMetrics(bannerFont).stringWidth(levelBannerText);
        int textX = (WIDTH - bannerWidth) / 2;

        g2.setColor(new Color(255, 255, 255, 210));
        g2.fillRoundRect(290, 110, 320, 80, 30, 30);
        g2.setColor(new Color(39, 53, 99));
        g2.setFont(bannerFont);
        g2.drawString(levelBannerText, textX, 160);
    }

    private void drawGameOverAnimation(Graphics2D g2) {
        int crusherX = player.getX() - 25;

        drawBookStack(g2, crusherX, slamY);

        if (slamY >= player.getY() - 6) {
            g2.setColor(new Color(255, 236, 149, 160));
            g2.fillOval(player.getX() - 26, player.getY() + 55, 145, 55);
        }
    }

    private void drawBookStack(Graphics2D g2, int x, int y) { //Book death scene 
        drawBookLayer(g2, x + 12, y + 66, 114, 15, new Color(113, 82, 160));
        drawBookLayer(g2, x + 6, y + 48, 128, 16, new Color(78, 112, 180));
        drawBookLayer(g2, x + 18, y + 29, 102, 16, new Color(198, 84, 84));
        drawBookLayer(g2, x + 2, y + 10, 136, 16, new Color(89, 156, 109));
    }

    private void drawBookLayer(Graphics2D g2, int x, int y, int width, int height, Color coverColor) {
        g2.setColor(coverColor);
        g2.fillRoundRect(x, y, width, height, 8, 8);
        g2.setColor(new Color(244, 238, 226));
        g2.fillRect(x + 10, y + 3, width - 20, height - 6);
        g2.setColor(new Color(66, 56, 47));
        g2.drawLine(x + 7, y + 2, x + 7, y + height - 2);
        g2.setColor(coverColor.darker());
        g2.drawRoundRect(x, y, width, height, 8, 8);
    }

    private void drawWinAnimation(Graphics2D g2) {
        for (int i = 0; i < confetti.size(); i++) {
            confetti.get(i).draw(g2);
        }

        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(240, 165, 420, 150, 35, 35);
        g2.setColor(new Color(28, 96, 66));
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.drawString("CONGRATS!", 305, 230);
        g2.setFont(new Font("Arial", Font.BOLD, 32));
        g2.drawString("YOU SURVIVED!", 300, 278);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (titleScreen) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER && titleFinished && !titleMenuOpened) {
                titleMenuOpened = true;
                frame.showMainMenu();
            }
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            moveLeft = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            moveRight = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_P && gameRunning) {
            paused = !paused;
        }

        if (e.getKeyCode() == KeyEvent.VK_E && (gameRunning || paused)) {
            boolean wasPaused = paused;
            paused = false;
            int choice = JOptionPane.showConfirmDialog(frame, "Return to the main menu?", "Exit to Menu",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                frame.showMainMenu();
            } else {
                paused = wasPaused;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            moveLeft = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            moveRight = false;
        }
    }
}
