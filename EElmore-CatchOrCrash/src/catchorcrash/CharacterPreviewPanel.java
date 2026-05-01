package catchorcrash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class CharacterPreviewPanel extends JPanel { //choose your character layout

    private int previewCharacter;

    public CharacterPreviewPanel() {
        setPreferredSize(new Dimension(340, 240));
        setBackground(new Color(248, 250, 255));
    }

    public void setPreviewCharacter(int previewCharacter) {
        this.previewCharacter = previewCharacter;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(227, 236, 255));
        g2.fillOval(28, 30, 110, 35);
        g2.fillOval(170, 38, 120, 34);

        g2.setColor(new Color(205, 224, 188));
        g2.fillRect(0, 190, getWidth(), 50);

        Player previewPlayer = new Player(120, 95, previewCharacter);
        previewPlayer.draw(g2, false);

        g2.setColor(new Color(29, 43, 74));
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString(Player.CHARACTER_NAMES[previewCharacter], 78, 32);
    }
}
