package example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends GameFrame {
    private JFrame frame;
    private JButton startButton;
    private JTextField nameField; // Field for entering player's name
    /** The background image for the game. */
    public Image background = ImageUtil.images.get("UI-background");

    public MainMenu() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Main Menu");
        frame.setSize(870, 560);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Optional: for fullscreen

        // Name input field
        nameField = new JTextField(20);
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(new JLabel("Enter Name:"), gbc);
        inputPanel.add(nameField, gbc);

        // Start button
        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // Custom JPanel with background
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(startButton, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void startGame() {
        String playerName = nameField.getText().trim();
        if (!playerName.isEmpty()) {
            // Load and display the game window with the player's name
            GameScreen gameScreen = new GameScreen(playerName);
            gameScreen.loadFrame();

            // Optional: Start playing background music
            MusicPlayer.getMusicPlay("src/example/frogger.mp3");

            // Close the main menu window
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter your name.", "Name Required", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu();
            }
        });
    }
}
