package example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;

public class MainMenu extends GameFrame {
    private JFrame frame;
    MusicPlayer musicPlayer = new MusicPlayer("src/example/frogger.mp3");
    private JButton startButton;
    private JButton soundButton; // Button for turning music on/off
    private JButton settingsButton;
    private JTextField nameField;
    private Image soundIcon = ImageUtil.images.get("sound");// Sound icon image
    private Image settingsIcon = ImageUtil.images.get("settings");// Sound icon image
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
        //frame.setUndecorated(true); // Optional: for fullscreen

        // Snake Game Title
        JLabel titleLabel = new JLabel("Snake Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 64));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.MAGENTA);

        // Name input field
        nameField = new JTextField(20);
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.setOpaque(false);
        inputPanel.add(new JLabel("Enter Name:"));
        inputPanel.add(nameField);

        // Combine the title panel and input panel in a parent panel
        JPanel centerPanel = new JPanel(new GridLayout(0, 1));
        centerPanel.setOpaque(false);
        centerPanel.add(titleLabel);
        centerPanel.add(inputPanel);


        // Start button
        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // Sound button
        soundButton = new JButton(new ImageIcon(soundIcon));
        soundButton.setPreferredSize(new Dimension(32, 32));
        soundButton.setContentAreaFilled(false);
        soundButton.setBorder(null);
        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMusic(); // Toggle music state when the button is clicked
            }
        });

        // Settings button
        settingsButton = new JButton(new ImageIcon(settingsIcon));
        settingsButton.setPreferredSize(new Dimension(32, 32));
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorder(null);
        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        // Add sound button to the top right corner
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(soundButton);
        buttonPanel.add(settingsButton);
        buttonPanel.setOpaque(false);

        // Custom JPanel with background
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        panel.add(buttonPanel, BorderLayout.EAST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.PAGE_END);

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
            // MusicPlayer.getMusicPlay("src/example/frogger.mp3");

            // Close the main menu window
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter your name.", "Name Required", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleMusic() {
        if (musicPlayer != null) {
            // Check if musicPlayer is initialized
            musicPlayer.toggle(); // Call the toggle method of MusicPlayer
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
