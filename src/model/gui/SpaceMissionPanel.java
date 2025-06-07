package model.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import model.Game;
import model.pet.Pet;
import java.io.File;

/**
 * This class is used when a pet goes on a space mission.
 */
public class SpaceMissionPanel extends JPanel {
    private Game game;
    private GUI mainFrame;
    private Timer cutsceneTimer;
    private Timer animationTimer;
    private ImageIcon backgroundImage;
    private ImageIcon petSpaceImage;
    private static final int CUTSCENE_DURATION = 3000; // 3 seconds
    private static final int ANIMATION_INTERVAL = 50; // 50ms between frames
    private JLabel missionLabel;
    private double rotationAngle = 0.0;

    /**
     * Constructor to create a SpaceMissionPanel.
     * 
     * @param game the current game.
     * @param mainFrame the main GUI frame.
     */
    public SpaceMissionPanel(Game game, GUI mainFrame) {
        this.game = game;
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(800, 600));
        setOpaque(false);
        setupUI();
        setupTimer();
        setupAnimation();
    }

    private void setupAnimation() {
        animationTimer = new Timer(ANIMATION_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotationAngle += 5.0; // Rotate 5 degrees each frame
                if (rotationAngle >= 360.0) {
                    rotationAngle = 0.0;
                }
                repaint(); // Trigger repaint to update the rotation
            }
        });
        animationTimer.start();
    }

    /**
     * Sets up the font, background, sprite, and general styling for the UI.
     */
    private void setupUI() {
        setLayout(new BorderLayout());
        
        // Create and configure mission label
        missionLabel = new JLabel();
        try {
            Font minecraftFont = Font.createFont(Font.TRUETYPE_FONT, 
                new File("assets/font/Minecraft.ttf")).deriveFont(24f);
            missionLabel.setFont(minecraftFont);
        } catch (Exception e) {
            // Fallback to monospace if Minecraft font not found
            Font fallbackFont = new Font("Monospaced", Font.BOLD, 24);
            System.out.println("Space mission fallback font used: " + fallbackFont);
            missionLabel.setFont(fallbackFont);
        }
        missionLabel.setForeground(Color.WHITE);
        missionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        missionLabel.setOpaque(false);
        
        // Update mission label text
        if (game != null && game.getPlayer() != null && game.getPlayer().getPet() != null) {
            Pet pet = game.getPlayer().getPet();
            missionLabel.setText(pet.getName() + " is on a space mission!");
        }
        
        // Create a panel to hold the mission label with padding
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BorderLayout());
        labelPanel.setOpaque(false);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0)); // Increased top padding to 80px
        labelPanel.add(missionLabel, BorderLayout.CENTER);
        
        // Add the label panel to the top of the main panel
        add(labelPanel, BorderLayout.NORTH);
        
        // Load background image
        try {
            java.net.URL imageUrl = getClass().getResource("/model/assets/spaceMissionBackground.png");
            if (imageUrl != null) {
                backgroundImage = new ImageIcon(imageUrl);
                System.out.println("Space mission background loaded successfully");
            } else {
                System.err.println("Could not find space mission background image");
            }
        } catch (Exception e) {
            System.err.println("Error loading space mission background: " + e.getMessage());
        }

        // Load pet space sprite
        if (game != null && game.getPlayer() != null && game.getPlayer().getPet() != null) {
            Pet pet = game.getPlayer().getPet();
            String petType = pet.getClass().getSimpleName().toLowerCase();
            System.out.println("Loading space sprite for pet type: " + petType);
            try {
                // First try to load the space sprite from the pet's directory
                String spritePath = "/model/assets/animalSprites/" + petType + "/space.png";
                System.out.println("Attempting to load sprite from: " + spritePath);
                java.net.URL imageUrl = getClass().getResource(spritePath);
                if (imageUrl == null) {
                    System.out.println("Pet-specific space sprite not found, trying general space sprite");
                    // If not found, try the general space sprite
                    spritePath = "/model/assets/space.png";
                    imageUrl = getClass().getResource(spritePath);
                }
                
                if (imageUrl != null) {
                    System.out.println("Successfully loaded sprite from: " + spritePath);
                    ImageIcon originalIcon = new ImageIcon(imageUrl);
                    // Scale the pet image to a reasonable size (e.g., 300x300)
                    Image scaledImage = originalIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                    petSpaceImage = new ImageIcon(scaledImage);
                    System.out.println("Pet space sprite loaded and scaled successfully");
                } else {
                    System.err.println("Could not find pet space sprite at: " + spritePath);
                }
            } catch (Exception e) {
                System.err.println("Error loading pet space sprite: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Game, Player, or Pet is null when trying to load space sprite");
        }
    }

    /**
     * Set up the timer for the cut scene.
     */
    private void setupTimer() {
        cutsceneTimer = new Timer(CUTSCENE_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToGame();
            }
        });
        cutsceneTimer.setRepeats(false);
    }

    /**
     * Override the {@code paintComponent} method to render the background and pet sprite.
     * 
     * @param g Graphics object to use for the rendering.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fallback background if image fails to load
            g2d.setColor(new Color(44, 62, 80));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Draw pet sprite in the center with rotation
        if (petSpaceImage != null) {
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            
            // Save the current transform
            AffineTransform oldTransform = g2d.getTransform();
            
            // Translate to center, rotate, and translate back
            g2d.translate(centerX, centerY);
            g2d.rotate(Math.toRadians(rotationAngle));
            g2d.translate(-petSpaceImage.getIconWidth()/2, -petSpaceImage.getIconHeight()/2);
            
            // Draw the image
            g2d.drawImage(petSpaceImage.getImage(), 0, 0, this);
            
            // Restore the original transform
            g2d.setTransform(oldTransform);
        }
    }

    /**
     * Starts the cut scene timer.
     */
    public void startCutscene() {
        cutsceneTimer.start();
    }

    /**
     * Stops the cut scene and retrun to the game panel.
     */
    private void returnToGame() {
        cutsceneTimer.stop();
        if (mainFrame != null) {
            mainFrame.showPanel("gamePanel");
            // Force an immediate UI update when returning to game panel
            GamePanel gamePanel = (GamePanel)mainFrame.getPanel("gamePanel");
            if (gamePanel != null) {
                gamePanel.updateUI();
                // Show feedback message about the space mission
                if (game != null && game.getPlayer() != null && game.getPlayer().getPet() != null) {
                    Pet pet = game.getPlayer().getPet();
                    gamePanel.showFeedbackMessage(pet.getName() + " went on a space mission. \nScore +1!");
                }
            }
        }
    }

    /**
     * Overrides the {@code removeNotify} method to stop the cut scene when leaving the panel.
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (cutsceneTimer != null) {
            cutsceneTimer.stop();
        }
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    /**
     * Overrides the {@code addNotify} method reload the current game state 
     * when the panel is shown.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        // Refresh game state when panel is shown
        if (mainFrame != null) {
            game = mainFrame.getGame();
            if (game != null && game.getPlayer() != null && game.getPlayer().getPet() != null) {
                setupUI(); // Reload UI with current game state
                System.out.println("SpaceMissionPanel: Game state refreshed successfully");
            } else {
                System.err.println("SpaceMissionPanel: Game state is invalid when showing panel");
            }
        } else {
            System.err.println("SpaceMissionPanel: Main frame is null when showing panel");
        }
    }

    /**
     * Refreashes the UI by getting the current game state from the main frame.
     * 
     * <p>If the game state is invalid or the main frame is null, an error message
     * will be displayed.</p>
     */
    public void refreshUI() {
        if (mainFrame != null) {
            game = mainFrame.getGame();
            if (game != null && game.getPlayer() != null && game.getPlayer().getPet() != null) {
                setupUI();
                System.out.println("SpaceMissionPanel: UI refreshed successfully");
            } else {
                System.err.println("SpaceMissionPanel: Game state is invalid when refreshing UI");
            }
        } else {
            System.err.println("SpaceMissionPanel: Main frame is null when refreshing UI");
        }
    }
} 