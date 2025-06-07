/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package model.gui;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import java.io.File;
import model.Game;

/**
 * Panel that allows users to choose their pet.
 */
public class ChoosePetPanel extends javax.swing.JPanel {

    private Game game;
    private GUI mainFrame;
    private javax.swing.JButton selectedButton = null;
    private String selectedPetType = null;

    /**
     * Creates new form ChoosePetPanel
     * 
     * @param gameInstance the current game that is running.
     * @param mainFrame the main GUI frame.
     */
    public ChoosePetPanel(Game gameInstance, GUI mainFrame) {
        this.game = gameInstance;
        this.mainFrame = mainFrame;

        initComponents();
        setupUI();

        // Create background panel with the image
        BackgroundPanel backgroundPanel = new BackgroundPanel("/model/assets/startScreenBackdrop.png");
        backgroundPanel.setLayout(new java.awt.BorderLayout());

        // Add all components to the background panel
        backgroundPanel.add(choosePetLabel, java.awt.BorderLayout.NORTH);
        backgroundPanel.add(mainContentPanel, java.awt.BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        // Remove existing components from this panel
        removeAll();

        // Add the background panel to this panel
        setLayout(new java.awt.BorderLayout());
        add(backgroundPanel, java.awt.BorderLayout.CENTER);
    }

    /**
     * Constructor for compability with form editor
     */
    public ChoosePetPanel() {
        this(new Game(), null);
    }

    /**
     * Sets up the UI components and styles.
     */
    private void setupUI() {
        // Set custom Minecraft font
        try {
            Font minecraftFont = Font.createFont(Font.TRUETYPE_FONT, 
                new File("assets/font/Minecraft.ttf")).deriveFont(48f);
            choosePetLabel.setFont(minecraftFont);
            dogLabel.setFont(minecraftFont.deriveFont(24f));
            mouseLabel.setFont(minecraftFont.deriveFont(24f));
            fishLabel.setFont(minecraftFont.deriveFont(24f));
            monkeyLabel.setFont(minecraftFont.deriveFont(24f));
            choosePetNextButton1.setFont(minecraftFont.deriveFont(24f));
            choosePetBackButton.setFont(minecraftFont.deriveFont(24f));
        } catch (Exception e) {
            // Fallback to monospace if Minecraft font not found
            Font fallbackFont = new Font("Monospaced", Font.BOLD, 48);
            choosePetLabel.setFont(fallbackFont);
            dogLabel.setFont(fallbackFont.deriveFont(24f));
            mouseLabel.setFont(fallbackFont.deriveFont(24f));
            fishLabel.setFont(fallbackFont.deriveFont(24f));
            monkeyLabel.setFont(fallbackFont.deriveFont(24f));
            choosePetNextButton1.setFont(fallbackFont.deriveFont(24f));
            choosePetBackButton.setFont(fallbackFont.deriveFont(24f));
        }
        
        // Set button styling
        choosePetNextButton1.setBorder(new EmptyBorder(10, 20, 10, 20));
        choosePetBackButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Make panels transparent
        setOpaque(false);
        mainContentPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        
        // Add action listeners for pet selection
        chooseDogButton.addActionListener(e -> selectPet(chooseDogButton, "dog"));
        chooseMouseButton.addActionListener(e -> selectPet(chooseMouseButton, "mouse"));
        chooseFishButton.addActionListener(e -> selectPet(chooseFishButton, "fish"));
        chooseMonkeyButton.addActionListener(e -> selectPet(chooseMonkeyButton, "monkey"));
        
        // Load pet images from the bin directory
        try {
            loadPetImages();
        } catch (Exception e) {
            System.err.println("Error loading pet images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads pet images from the assets directory
     */
    private void loadPetImages() {
        // Loads pet images from the assets directory with consistent sizes
        loadPetImage(chooseDogButton, "/model/assets/animalSprites/dog/normal.png", 120, 120);
        loadPetImage(chooseMouseButton, "/model/assets/animalSprites/mouse/normal.png", 120, 120);
        loadPetImage(chooseFishButton, "/model/assets/animalSprites/fish/normal.png", 120, 120);
        loadPetImage(chooseMonkeyButton, "/model/assets/animalSprites/monkey/normal.png", 120, 120);
    }
    
    /**
     * Loads an image for a pet button.
     * 
     * @param button the button on which the image will be displayed.
     * @param imagePath the path where the image is stored.
     * @param width the width of the image.
     * @param height the height of the image.
     */
    private void loadPetImage(javax.swing.JButton button, String imagePath, int width, int height) {
        try {
            java.net.URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                java.awt.Image image = new javax.swing.ImageIcon(imageUrl).getImage()
                    .getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
                button.setIcon(new javax.swing.ImageIcon(image));
                button.setDisabledIcon(new javax.swing.ImageIcon(image));
                button.setSelectedIcon(new javax.swing.ImageIcon(image));
                button.setPressedIcon(new javax.swing.ImageIcon(image));
                button.setRolloverIcon(new javax.swing.ImageIcon(image));
            } else {
                System.out.println("Image not found at: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }

    /**
     * Allows user to choose a pet.
     * 
     * <p>Handles the styling of the choose pet buttons. The selected button
     * will have a different colour and a border. The pet that the user
     * selected will set.</p>
     * 
     * @param button the choose pet button that the user selected.
     * @param petType the type of the pet that the user selected.
     */
    private void selectPet(javax.swing.JButton button, String petType) {
        // Reset all buttons to default color
        chooseDogButton.setBackground(new Color(46, 204, 113));
        chooseMouseButton.setBackground(new Color(46, 204, 113));
        chooseFishButton.setBackground(new Color(46, 204, 113));
        chooseMonkeyButton.setBackground(new Color(46, 204, 113));
        
        // Highlight selected button
        button.setBackground(new Color(39, 174, 96)); // Darker green for selected
        selectedButton = button;
        selectedPetType = petType;

        // Enable the next button
        choosePetNextButton1.setEnabled(true);
        
        // Add a subtle border to the selected button
        button.setBorderPainted(true);
        button.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(255, 255, 255), 2));
        
        // Remove borders from other buttons
        for (javax.swing.JButton b : new javax.swing.JButton[]{chooseDogButton, chooseMouseButton, chooseFishButton, chooseMonkeyButton}) {
            if (b != button) {
                b.setBorderPainted(false);
            }
        }

        // Store the selected pet in a temp variable
        // This will be used in NamePetPanel to create the pet
        if (mainFrame != null) {
            mainFrame.setSelectedPetType(petType);
        }
    }
    
    /**
     * Overrides the {@code addNotify} method to reset the selection when the panel
     * becomes visible.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        // Reset selection when panel becomes visible
        resetSelection();
    }

    /**
     * Resets the buttons, ensuring none of the buttons are selected when the panel becomes visible.
     */
    private void resetSelection() {
        // Reset all buttons
        chooseDogButton.setBackground(new Color(46, 204, 113));
        chooseMouseButton.setBackground(new Color(46, 204, 113));
        chooseFishButton.setBackground(new Color(46, 204, 113));
        chooseMonkeyButton.setBackground(new Color(46, 204, 113));

        // Remove borders
        chooseDogButton.setBorderPainted(false);
        chooseMouseButton.setBorderPainted(false);
        chooseFishButton.setBorderPainted(false);
        chooseMonkeyButton.setBorderPainted(false);

        // Reset selection
        selectedButton = null;
        selectedPetType = null;

        // Disable next button
        choosePetNextButton1.setEnabled(false);
    }

    /**
     * Overrides the {@code painComponent} method to remove the pixel grid background.
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        choosePetLabel = new javax.swing.JLabel();
        chooseDogButton = new javax.swing.JButton();
        chooseMouseButton = new javax.swing.JButton();
        chooseFishButton = new javax.swing.JButton();
        chooseMonkeyButton = new javax.swing.JButton();
        choosePetNextButton1 = new javax.swing.JButton();
        choosePetBackButton = new javax.swing.JButton();
        dogLabel = new javax.swing.JLabel();
        mouseLabel = new javax.swing.JLabel();
        fishLabel = new javax.swing.JLabel();
        monkeyLabel = new javax.swing.JLabel();
        mainContentPanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(800, 600));

        choosePetLabel.setForeground(new java.awt.Color(236, 240, 241));
        choosePetLabel.setText("Choose a Pet");
        choosePetLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Configure pet buttons
        for (javax.swing.JButton button : new javax.swing.JButton[]{chooseDogButton, chooseMouseButton, chooseFishButton, chooseMonkeyButton}) {
            button.setBackground(new java.awt.Color(46, 204, 113));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setPreferredSize(new java.awt.Dimension(160, 160));
            button.setMinimumSize(new java.awt.Dimension(160, 160));
            button.setMaximumSize(new java.awt.Dimension(160, 160));
            button.setSize(new java.awt.Dimension(160, 160));
        }

        // Configure labels
        dogLabel.setForeground(new java.awt.Color(236, 240, 241));
        dogLabel.setText("Mousetronaut");
        dogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        mouseLabel.setForeground(new java.awt.Color(236, 240, 241));
        mouseLabel.setText("Dogtronaut");
        mouseLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        fishLabel.setForeground(new java.awt.Color(236, 240, 241));
        fishLabel.setText("Monkeystronaut");
        fishLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        monkeyLabel.setForeground(new java.awt.Color(236, 240, 241));
        monkeyLabel.setText("Fishtronaut");
        monkeyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Configure navigation buttons
        choosePetNextButton1.setBackground(new java.awt.Color(46, 204, 113));
        choosePetNextButton1.setForeground(new java.awt.Color(236, 240, 241));
        choosePetNextButton1.setText("NEXT");
        choosePetNextButton1.setFocusPainted(false);
        choosePetNextButton1.setBorderPainted(false);
        choosePetNextButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosePetNextButton1ActionPerformed(evt);
            }
        });

        choosePetBackButton.setBackground(new java.awt.Color(231, 76, 60));
        choosePetBackButton.setForeground(new java.awt.Color(236, 240, 241));
        choosePetBackButton.setText("BACK");
        choosePetBackButton.setFocusPainted(false);
        choosePetBackButton.setBorderPainted(false);
        choosePetBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosePetBackButtonActionPerformed(evt);
            }
        });

        // Set up main content panel with adjusted spacing
        mainContentPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        
        // Add pet buttons and labels with reduced spacing
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 20, 5, 20); // Reduced vertical spacing
        mainContentPanel.add(chooseDogButton, gbc);
        
        gbc.gridx = 1;
        mainContentPanel.add(chooseMouseButton, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(0, 20, 15, 20); // Adjusted spacing between button and label
        mainContentPanel.add(dogLabel, gbc);
        
        gbc.gridx = 0;
        mainContentPanel.add(mouseLabel, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new java.awt.Insets(10, 20, 5, 20); // Reduced vertical spacing
        mainContentPanel.add(chooseFishButton, gbc);
        
        gbc.gridx = 1;
        mainContentPanel.add(chooseMonkeyButton, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new java.awt.Insets(0, 20, 15, 20); // Adjusted spacing between button and label
        mainContentPanel.add(fishLabel, gbc);
        
        gbc.gridx = 0;
        mainContentPanel.add(monkeyLabel, gbc);

        // Set up button panel
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 0));
        buttonPanel.add(choosePetBackButton);
        buttonPanel.add(choosePetNextButton1);

        // Load pet images
        loadPetImages();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Manages the action event for the back button. 
     * 
     * <p>If the main frame exists, main frame handles the navigation to the choose file panel.
     * Otherwise the parent component's card panel in used.</p>
     * 
     * @param evt action event triggered when the back button is clicked.
     */
    private void choosePetBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choosePetBackButtonActionPerformed
        if (mainFrame != null) {
            mainFrame.showPanel("chooseFilePanel");
        } else {
        // go back to the choose file screen
        CardLayout card = (CardLayout)getParent().getLayout();
        card.show(getParent(), "chooseFilePanel");
        }
    }//GEN-LAST:event_choosePetBackButtonActionPerformed

    /**
     * Manages the action event for the next button.
     * 
     * <p>If a pet is selected, navigate to the name pet panel. The navigation is handled by the
     * main frame if it exists, otherwise it is handled by the parent compoment's card layout.</p>
     * 
     * @param evt action event triggered when the next button is clicked.
     */
    private void choosePetNextButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choosePetNextButton1ActionPerformed
        if (selectedPetType == null) {
            // No pet selected, do nothing
            return;
        }

        if (mainFrame != null) {
            // Store the selected pet type in the game instance
            mainFrame.showPanel("namePetPanel");
        } else {
        // go to the name pet screen
        CardLayout card = (CardLayout)getParent().getLayout();
        card.show(getParent(), "namePetPanel");
        }
    }//GEN-LAST:event_choosePetNextButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseDogButton;
    private javax.swing.JButton chooseMouseButton;
    private javax.swing.JButton chooseFishButton;
    private javax.swing.JButton chooseMonkeyButton;
    private javax.swing.JButton choosePetBackButton;
    private javax.swing.JLabel choosePetLabel;
    private javax.swing.JButton choosePetNextButton1;
    private javax.swing.JLabel dogLabel;
    private javax.swing.JLabel mouseLabel;
    private javax.swing.JLabel fishLabel;
    private javax.swing.JLabel monkeyLabel;
    private javax.swing.JPanel mainContentPanel;
    private javax.swing.JPanel buttonPanel;
    // End of variables declaration//GEN-END:variables
}
