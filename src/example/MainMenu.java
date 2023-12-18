package example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends GameFrame {
    private JFrame frame;
    private JButton startButton;
    /** The background image for the game. */
    public Image background = ImageUtil.images.get("UI-background");

    public MainMenu() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);

        // Get the local graphics environment and default screen device
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();

        // Set the frame to fullscreen
        device.setFullScreenWindow(frame);

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // Custom panel for the start button
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Make the button transparent
        buttonPanel.add(startButton);

        // Custom JPanel with background
        JPanel panel = new JPanel() {
          protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
          }
        };
        panel.setLayout(new FlowLayout());
        panel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void startGame() {

        // Load and display the game window
        new GameScreen().loadFrame();

        //Start playing background music
        MusicPlayer.getMusicPlay("src/example/frogger.mp3");

        // Close the main menu window
        frame.dispose();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(background, 0, 0, null);
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
