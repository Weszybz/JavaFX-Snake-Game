package example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;

public class MainMenu extends GameFrame {
    private JFrame frame;
    public static MusicPlayer musicPlayer = new MusicPlayer("src/example/frogger.mp3", true);
    private JButton startButton;
    private JButton soundButton; // Button for turning music on/off
    private JButton settingsButton;
    private JButton helpButton;
    private JTextField nameField;
    private Image soundIcon = ImageUtil.images.get("sound");// Sound icon image
    private Image settingsIcon = ImageUtil.images.get("settings");
    private Image helpIcon = ImageUtil.images.get("help");
    /** The background image for the game. */
    public Image background = ImageUtil.images.get("UI-background");

    private void displayInstructions() {
        // Create the instructions message
        String instructions = "Snake Game Instructions:\n"
                + "1. Use arrow keys to move the snake (Up, Down, Left, Right).\n"
                + "2. Eat the food to grow and earn points.\n"
                + "3. Avoid hitting walls or yourself to stay alive.\n"
                + "4. Your score is displayed on the screen.\n"
                + "5. Have fun and beat your high score!\n";

        // Show instructions in a message dialog
        JOptionPane.showMessageDialog(frame, instructions, "Snake Game Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    public MainMenu() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Main Menu");
        frame.setSize(900, 560);
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

        helpButton = new JButton(new ImageIcon(helpIcon));
        helpButton.setPreferredSize(new Dimension(32, 32));
        helpButton.setContentAreaFilled(false);
        helpButton.setBorder(null);
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInstructions();
            }
        });

        // Add sound button to the top right corner
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(soundButton);
        buttonPanel.add(settingsButton);
        buttonPanel.add(helpButton);
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
        new MainMenu();
    }
}
