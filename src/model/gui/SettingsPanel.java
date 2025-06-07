/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package model.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.io.File;

import model.*;

/**
 * Settings panel that gives players the option to save and exit the game and view 
 * the tutorial.
 */
public class SettingsPanel extends javax.swing.JPanel {
    
    private Game game;
    private GUI mainFrame;

    /**
     * Construtor to create a SettingsPanel and set up the componments.
     */
    public SettingsPanel(Game gameInstance, GUI mainFrame) {
        this.game = gameInstance;
        this.mainFrame = mainFrame;
        initComponents();
        setupUI();

        // Create background panel with the image
        BackgroundPanel backgroundPanel = new BackgroundPanel("/model/assets/startScreenBackdrop.png");
        backgroundPanel.setLayout(new java.awt.BorderLayout());

        // Add all components to the background panel
        backgroundPanel.add(title, java.awt.BorderLayout.NORTH);
        backgroundPanel.add(buttonPanel, java.awt.BorderLayout.CENTER);
        backgroundPanel.add(backButton, java.awt.BorderLayout.SOUTH);

        // Remove existing components from this panel
        removeAll();

        // Add the background panel to this panel
        setLayout(new java.awt.BorderLayout());
        add(backgroundPanel, java.awt.BorderLayout.CENTER);
    }
    
    /**
     * Default constructor.
     */
    public SettingsPanel() {
        initComponents();
        setupUI();
    }
    
    /**
     * Sets up the font, buttons, and other styling for the UI.
     */
    private void setupUI() {
        // Set custom Minecraft font
        try {
            java.io.InputStream fontStream = getClass().getResourceAsStream("/model/assets/font/Minecraft.ttf");
            if (fontStream != null) {
                Font minecraftFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                
                // Apply Minecraft font to all text elements
                title.setFont(minecraftFont.deriveFont(48f));
                tutorialButton.setFont(minecraftFont.deriveFont(24f));
                saveExitButton.setFont(minecraftFont.deriveFont(24f));
                backButton.setFont(minecraftFont.deriveFont(24f));
            }
        } catch (Exception e) {
            System.err.println("Error loading Minecraft font: " + e.getMessage());
        }

        // Set button colors and borders
        tutorialButton.setBackground(new Color(52, 152, 219)); // Blue
        tutorialButton.setForeground(Color.WHITE);
        tutorialButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        tutorialButton.setFocusPainted(false);
        tutorialButton.setBorderPainted(false);
        tutorialButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tutorialButton.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        tutorialButton.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        tutorialButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        
        saveExitButton.setBackground(new Color(46, 204, 113)); // Green
        saveExitButton.setForeground(Color.WHITE);
        saveExitButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        saveExitButton.setFocusPainted(false);
        saveExitButton.setBorderPainted(false);
        saveExitButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        saveExitButton.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        saveExitButton.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        saveExitButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        backButton.setBackground(new Color(52, 73, 94)); // Dark blue-gray
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        backButton.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        backButton.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        backButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        // Make panels transparent
        setOpaque(false);
        buttonPanel.setOpaque(false);
    }
    
    /**
     * Override the {@code paintComponent} method to create a custom background.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Remove the pixel grid background since we're using an image now
        super.paintComponent(g);
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
        buttonPanel = new javax.swing.JPanel();
        tutorialButton = new javax.swing.JButton();
        saveExitButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(800, 600));

        title.setForeground(new java.awt.Color(236, 240, 241));
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Settings");

        buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.setLayout(new java.awt.GridLayout(3, 0, 0, 10));

        tutorialButton.setText("How to Play");
        tutorialButton.setFocusPainted(false);
        tutorialButton.setBorderPainted(false);
        tutorialButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tutorialButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(tutorialButton);

        saveExitButton.setText("Save and Exit");
        saveExitButton.setFocusPainted(false);
        saveExitButton.setBorderPainted(false);
        saveExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveExitButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(saveExitButton);

        backButton.setText("Back to Game");
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(backButton);
    }// </editor-fold>                        

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
     * Manages the action event for the save exit button.
     * 
     * <p>Plays a sound effect and attempt to save the game. Display a 
     * success or error message depending on whether the game saved
     * successfully. Finally, return to the start panel.</p>
     * 
     * @param evt action event triggred when the save exit button is clicked.
     */
    private void saveExitButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        model.SoundManager.getInstance().playClickSound();
        boolean saveSuccess = false;
        if (game != null && game.getPlayer() != null && game.getPlayer().getPet() != null) {
            saveSuccess = game.savePet();
            game.stopGame();
        }

        if (saveSuccess) {
            JOptionPane.showMessageDialog(this, 
            "Game saved successfully.", 
            "Save Game", 
            JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
            "No pet to save or save failed.", 
            "Save Game", 
            JOptionPane.WARNING_MESSAGE);
        }
        
        // Return to main menu
        if (mainFrame != null) {
            mainFrame.showPanel("startPanel");
        } else {
            CardLayout card = (CardLayout) getParent().getLayout();
            card.show(getParent(), "startPanel");
        }
    }                                              

    /**
     * Manages the action event for the back button.
     * 
     * <p>Play a sound effect then go back to the game panel using the main frame if
     * it exists or the parent component's card layout otherwise.</p>
     * 
     * @param evt action event triggred when the back button is clicked.
     */
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        model.SoundManager.getInstance().playClickSound();
        if (mainFrame != null) {
            mainFrame.showPanel("gamePanel");
        } else {
            // Go back to the game screen if mainFrame is null
            CardLayout card = (CardLayout) getParent().getLayout();
            card.show(getParent(), "gamePanel");
        }
    }                                          


    // Variables declaration - do not modify                     
    private javax.swing.JButton backButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton saveExitButton;
    private javax.swing.JLabel title;
    private javax.swing.JButton tutorialButton;
    // End of variables declaration                   
}
