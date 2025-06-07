package model.gui;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * A reusable panel that displays a background image.
 */
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    /**
     * Creates a BackgroundPanel with the specified image if it is found.
     * 
     * @param imagePath the image to be displayed in the background.
     */
    public BackgroundPanel(String imagePath) {
        try {
            java.net.URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                backgroundImage = new ImageIcon(imageUrl).getImage();
                System.out.println("Successfully loaded background image: " + imagePath);
            } else {
                System.err.println("Could not find background image: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
        setLayout(new java.awt.BorderLayout());
        setOpaque(false);
    }
    
    /**
     * Overrides the {@code paintComponent} method to create a background image.
     * If the image exists, it is scaled to fit the the panel.
     * 
     * @param g graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}