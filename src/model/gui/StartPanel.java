//NETBEANS VERSION

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package model.gui;

import java.awt.Color;
import model.Game;
import java.awt.RenderingHints;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.border.EmptyBorder;
import java.io.File;

/**
 * This is the main menu of the game. It has the option to start the game, view a tutorial, exit the game,
 * or view parental controls.
 */
public class StartPanel extends javax.swing.JPanel {

    private Game game;
    private GUI mainFrame;
    
    /**
     * Constructor to create a start panel. Sets the background image, add components to the panel
     * and set the layout.
     */
    public StartPanel(Game gameInstance, GUI mainFrame) {
        this.game = gameInstance;
        this.mainFrame = mainFrame;
        initComponents();
        setupUI();

        // Create background panel with the image
        BackgroundPanel backgroundPanel = new BackgroundPanel("/model/assets/startScreenBackdrop.png");
        backgroundPanel.setLayout(new java.awt.BorderLayout());
        
        // Create a panel for title and credits with more space
        javax.swing.JPanel titlePanel = new javax.swing.JPanel();
        titlePanel.setLayout(new java.awt.GridBagLayout());
        titlePanel.setOpaque(false);
        
        // Setup GridBagConstraints for precise positioning
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        
        // Add the title with top padding to move it down
        title.setBorder(new javax.swing.border.EmptyBorder(50, 0, 0, 0));
        titlePanel.add(title, gbc);
        
        // Add credits label with enough spacing below the title
        javax.swing.JLabel creditsLabel = new javax.swing.JLabel();
        creditsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        creditsLabel.setText("Made by Yonas Asmelash, Julia Norrish, and Jessica Wang | CS 2212 @ Western University, Winter 2025");
        creditsLabel.setForeground(new Color(255, 255, 255)); // White text for better visibility
        
        // Try to apply Minecraft font to credits
        try {
            java.io.InputStream fontStream = getClass().getResourceAsStream("/model/assets/font/Minecraft.ttf");
            if (fontStream != null) {
                Font minecraftFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                creditsLabel.setFont(minecraftFont.deriveFont(16f)); // Larger font size for better readability
            }
        } catch (Exception e) {
            System.err.println("Error loading Minecraft font for credits: " + e.getMessage());
        }
        
        // Position credits below the title with significant spacing
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(20, 0, 20, 0); // Add padding above and below
        titlePanel.add(creditsLabel, gbc);

        // Add all components to the background panel with proper spacing
        backgroundPanel.add(titlePanel, java.awt.BorderLayout.NORTH);
        backgroundPanel.add(mainButtonsPanel, java.awt.BorderLayout.CENTER);
        
        // Add some padding around the parental controls button
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new javax.swing.border.EmptyBorder(0, 0, 20, 0));
        bottomPanel.add(parentalControlsButton, java.awt.BorderLayout.CENTER);
        backgroundPanel.add(bottomPanel, java.awt.BorderLayout.SOUTH);

        // Remove existing components from this panel
        removeAll();

        // Add the background panel to this panel
        setLayout(new java.awt.BorderLayout());
        add(backgroundPanel, java.awt.BorderLayout.CENTER);
    }

    /**
     * Empty default constructor.
     */
    public StartPanel() {
        this(new Game(), null);
    }
    
    /**
     * Sets the font, colours, and button styling.
     */
    private void setupUI() {
        // Set custom Minecraft font
        try {
            java.io.InputStream fontStream = getClass().getResourceAsStream("/model/assets/font/Minecraft.ttf");
            if (fontStream != null) {
                Font minecraftFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                
                // Apply Minecraft font to all text elements
                title.setFont(minecraftFont.deriveFont(48f));
                playButton.setFont(minecraftFont.deriveFont(24f));
                tutorialButton.setFont(minecraftFont.deriveFont(24f));
                quitButton.setFont(minecraftFont.deriveFont(24f));
                parentalControlsButton.setFont(minecraftFont.deriveFont(24f));
            }
        } catch (Exception e) {
            System.err.println("Error loading Minecraft font: " + e.getMessage());
        }

        // Set button styling
        playButton.setBackground(new Color(46, 204, 113)); // Green
        playButton.setForeground(Color.WHITE);
        playButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        playButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        playButton.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        playButton.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        playButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        tutorialButton.setBackground(new Color(52, 152, 219)); // Blue
        tutorialButton.setForeground(Color.WHITE);
        tutorialButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        tutorialButton.setFocusPainted(false);
        tutorialButton.setBorderPainted(false);

        quitButton.setBackground(new Color(231, 76, 60)); // Red
        quitButton.setForeground(Color.WHITE);
        quitButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        quitButton.setFocusPainted(false);
        quitButton.setBorderPainted(false);

        parentalControlsButton.setBackground(new Color(52, 73, 94)); // Dark blue-gray
        parentalControlsButton.setForeground(Color.WHITE);
        parentalControlsButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        parentalControlsButton.setFocusPainted(false);
        parentalControlsButton.setBorderPainted(false);

        // Set title color
        title.setForeground(new Color(236, 240, 241));

        // Make panels transparent
        setOpaque(false);
        mainButtonsPanel.setOpaque(false);
    }

    /**
     * Overrides the {@code paintComponent} method to create the pixel grid background.
     * 
     * @param g Graphics object to use for the rendering.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        
        // Draw pixel grid background
        g2d.setColor(new Color(52, 73, 94)); // Slightly lighter blue-gray
        for (int x = 0; x < getWidth(); x += 8) {
            for (int y = 0; y < getHeight(); y += 8) {
                g2d.fillRect(x, y, 1, 1);
            }
        }
    }
    
    /**
    * A reusable panel that displays a background image.
    */
    class BackgroundPanel extends javax.swing.JPanel {
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        title = new javax.swing.JLabel();
        mainButtonsPanel = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        tutorialButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        parentalControlsButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("ASTROPAWS");
        title.setPreferredSize(new java.awt.Dimension(800, 100));
        add(title, java.awt.BorderLayout.NORTH);

        mainButtonsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 50, 50, 50));
        mainButtonsPanel.setOpaque(false);
        mainButtonsPanel.setLayout(new java.awt.GridLayout(3, 0, 0, 20));

        playButton.setText("START");
        playButton.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        mainButtonsPanel.add(playButton);

        tutorialButton.setText("HOW TO PLAY?");
        tutorialButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tutorialButtonActionPerformed(evt);
            }
        });
        mainButtonsPanel.add(tutorialButton);

        quitButton.setText("QUIT");
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });
        mainButtonsPanel.add(quitButton);

        add(mainButtonsPanel, java.awt.BorderLayout.CENTER);

        parentalControlsButton.setText("Parental Controls");
        parentalControlsButton.setPreferredSize(new java.awt.Dimension(50, 50));
        parentalControlsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parentalControlsButtonActionPerformed(evt);
            }
        });
        add(parentalControlsButton, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>                        

    /**
     * Manages the action event for the play button.
     * 
     * <p>Plays the sound effect, and changes the panel to the choose file panel.
     * If the main frame exists, main frame handles the navigation to the choose file panel.</p>
     * 
     * @param evt action event triggered by the button click.
     */
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        model.SoundManager.getInstance().playClickSound();
        if (mainFrame != null) {
            mainFrame.showPanel("chooseFilePanel");
        }
    }                                          

    /**
     * Manages the action event for the quit button.
     * 
     * <p>Plays the sound effect, saves the game, and exits the program.</p>
     * 
     * @param evt action event triggered by the button click.
     */
    private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        model.SoundManager.getInstance().playClickSound();
        // save game if needed
        if (game != null && game.getPlayer() != null && game.getPlayer().getPet() != null) {
            game.savePet();
            game.stopGame();
        }
        System.exit(0);
    }                                          

   /**
     * Manages the action event for the parental controls button.
     * 
     * <p>Plays the sound effect, and changes the panel to the parental control panel.
     * If the main frame exists, it handles the navigation to the choose file panel.</p>
     * 
     * @param evt action event triggered by the button click.
     */
    private void parentalControlsButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                       
        model.SoundManager.getInstance().playClickSound();
        if (mainFrame != null) {
            mainFrame.showPanel("parentalControlPanel");
        }
    }                                                      

    /**
     * Manages the action event for the tutorial button. 
     * 
     * <p>Play a sound effect and display the tutorial on how to play the game.</p>
     * 
     * @param evt action event triggred when the tutorial button is clicked.
     */
    private void tutorialButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        model.SoundManager.getInstance().playClickSound();
        JOptionPane.showMessageDialog(this,
            "Welcome to Astropaws!\n\n" +
            "You are an astronaut training pets for space missions. Your mission is to prepare" +  
            "\nyour pet for the challenges of space travel by keeping it healthy, happy, and well-rested.\n\n" +
            "How to play:\n\n" +
            "• Feed your pet to keep it from getting hungry (F)\n" +
            "• Play with your pet to keep it happy (P)\n" +
            "• Put your pet to sleep when it's tired (S)\n" +
            "• Take your pet to the vet when it's sick (V)\n" +
            "• Give your pet gifts to cheer it up (G)\n" +
            "• Exercise your pet to keep it healthy (E)\n\n" +
            "Space Mission Requirements:\n" +
            "• Your pet must have all stats (health, hunger, energy, happiness) above 90%\n" +
            "• The space readiness bar will increase when all stats are high\n" +
            "• Once ready, send your pet on exciting space missions to increase its score!\n\n" +
            "Good luck, astronaut! Your pet is counting on you!",
            "How to Play", 
            JOptionPane.INFORMATION_MESSAGE);
    }                                              

    /**
     * Overrides the {@code addNotify} method to start the main
     * theme music when the panel is shown.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        // Start playing the main theme when the panel is shown
        model.SoundManager.getInstance().playMainTheme();
    }

    /**
     * Overrides the {@code removeNotify} method to stop the main theme 
     * music when leaving the panel.
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        // Stop the main theme when leaving the panel
        model.SoundManager.getInstance().stopMainTheme();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel mainButtonsPanel;
    private javax.swing.JButton parentalControlsButton;
    private javax.swing.JButton playButton;
    private javax.swing.JButton quitButton;
    private javax.swing.JLabel title;
    private javax.swing.JButton tutorialButton;
    // End of variables declaration                   
}