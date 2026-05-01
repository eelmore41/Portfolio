package catchorcrash;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameFrame extends JFrame {

    private GamePanel gamePanel;
    private int selectedCharacter;

    public GameFrame() { //Start of game 
        selectedCharacter = 0;

        setTitle("Catch or Crash");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gamePanel = new GamePanel(this);
        add(gamePanel, BorderLayout.CENTER);

        setResizable(false);
        pack();
        setMinimumSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
        setLocationRelativeTo(null);
        setVisible(true);

        gamePanel.showTitleScreen();
    }

    public void showMainMenu() { //Main menu buttons
        gamePanel.stopGame();

        while (true) {
            String[] options = { "Level Mode", "Survival Mode", "Character Select", "How to Play", "Exit" };
            int choice = JOptionPane.showOptionDialog(this,
                    "Catch good items.\nAvoid bad items.\nPick a mode to start.",
                    "Catch or Crash", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
                    options[0]);

            if (choice == 0) {
                gamePanel.startGame(GamePanel.LEVEL_MODE, selectedCharacter);
                break;
            }

            if (choice == 1) {
                gamePanel.startGame(GamePanel.SURVIVAL_MODE, selectedCharacter);
                break;
            }

            if (choice == 2) {
                chooseCharacter();
            } else if (choice == 3) {
                showHowToPlay();
            } else {
                dispose();
                break;
            }
        }
    }

    private void chooseCharacter() { //Choose your character layout
        final JDialog dialog = new JDialog(this, "Character Selection", true);
        dialog.setLayout(new BorderLayout(12, 12));

        final CharacterPreviewPanel previewPanel = new CharacterPreviewPanel();
        previewPanel.setPreviewCharacter(selectedCharacter);

        JLabel topLabel = new JLabel("Choose Your Character!", JLabel.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        for (int i = 0; i < Player.CHARACTER_NAMES.length; i++) {
            final int index = i;
            JButton button = new JButton(Player.CHARACTER_NAMES[i]);
            button.setFocusPainted(false);
            button.setBackground(new Color(241, 245, 255));

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    previewPanel.setPreviewCharacter(index);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    previewPanel.setPreviewCharacter(selectedCharacter);
                }
            });

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCharacter = index;
                    previewPanel.setPreviewCharacter(selectedCharacter);
                    dialog.dispose();
                }
            });

            buttonPanel.add(button);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        bottomPanel.add(closeButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(bottomPanel, BorderLayout.SOUTH);

        dialog.add(topLabel, BorderLayout.NORTH);
        dialog.add(previewPanel, BorderLayout.CENTER);
        dialog.add(southPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        gamePanel.requestFocusInWindow();
    }

    private void showHowToPlay() { // How to play menu
        JOptionPane.showMessageDialog(this,
                "Controls:\n"
                        + "Left Arrow or A = move left\n"
                        + "Right Arrow or D = move right\n"
                        + "P = pause\n"
                        + "E = exit to menu\n\n"
                        + "Good items give points.\n"
                        + "Bad items cause mistakes.\n"
                        + "In Survival Mode, 1 mistake ends the run.\n"
                        + "In Level Mode, 3 mistakes ends the run.\n"
                        + "How long can you survive?!",
                "How to Play", JOptionPane.INFORMATION_MESSAGE);
    }

    public void askToRestart(int mode, String title, String message) { 
        String[] options = { "Restart", "Main Menu", "Exit" };
        int choice = JOptionPane.showOptionDialog(this, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            gamePanel.startGame(mode, selectedCharacter);
        } else if (choice == 1) {
            showMainMenu();
        } else {
            dispose();
        }
    }
}
