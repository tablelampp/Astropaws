package model.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JOptionPane;
import model.Game;

/**
 * This is the class for the parental control panel. It allows parents to control their child's account
 * by making time limits and reviving the pet if it dies.
 */
public class ParentalControlPanel extends javax.swing.JPanel {
    private Game game;
    private GUI mainFrame;
    private boolean isPasswordVerified = false;

    /**
     * Constructor to set up the components of the parental control panel.
     * 
     * @param gameInstance the current game.
     * @param mainFrame the main GUI frame.
     */
    public ParentalControlPanel(Game gameInstance, GUI mainFrame) {
        this.game = gameInstance;
        this.mainFrame = mainFrame;
        initComponents();
        setupUI();

        // Initially show password panel
        showPasswordPanel();
    }

    /**
     * Constructor for form editor compatibility
     */
    public ParentalControlPanel() {
        this(new Game(), null);
    }

    /**
     * Sets the styling for the background and font. Displays an error message if the font
     * cannot be found.
     */
    private void setupUI() {
        // Set background color
        setBackground(new Color(44, 62, 80)); // Dark blue-gray background
        
        // Apply custom font if available
        try {
            java.io.InputStream fontStream = getClass().getResourceAsStream("/model/assets/font/Minecraft.ttf");
            if (fontStream != null) {
                java.awt.Font minecraftFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontStream);
                titleLabel.setFont(minecraftFont.deriveFont(48f));
                
                // Apply font to components
                passwordLabel.setFont(minecraftFont.deriveFont(24f));
                passwordField.setFont(minecraftFont.deriveFont(18f));
                passwordSubmitButton.setFont(minecraftFont.deriveFont(18f));
                backButton.setFont(minecraftFont.deriveFont(18f));
                
            } else {
                System.out.println("Error loading font: Can't read /model/assets/font/Minecraft.ttf");
            }
        } catch (Exception e) {
            System.out.println("Error loading font: " + e.getMessage());
        } 
    }

    /**
     * Makes the password panel visible.
     */
    private void showPasswordPanel() {
        cardPanel.setVisible(true);
        controlPanel.setVisible(false);
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, "passwordPanel");
    }

    /**
     * Makes the control panel visible.
     */
    private void showControlPanel() {
        cardPanel.setVisible(false);
        controlPanel.setVisible(true);
        
        updateControlPanelData();   // Update UI with current values
    }

    /**
     * Updates the data on the control panel. This data includes the time restriction settings, 
     * hours played, and the pets that can be revived.
     */
    private void updateControlPanelData() {
        // Time restriction settings
        enableTimeCheckbox.setSelected(game.isTimeRestrictionEnabled());
        startHourSpinner.setValue(game.getStartHour());
        endHourSpinner.setValue(game.getEndHour());

        // Play time stats
        long totalHours = game.getTotalPlayTime() / (1000 * 60 * 60);
        long totalMinutes = (game.getTotalPlayTime() % (1000 * 60 * 60)) / (1000 * 60);
        totalPlayTimeLabel.setText("Total Play Time: " + totalHours + " hours " + totalMinutes + " minutes");

        long avgMinutes = game.getAverageSessionTime() / (1000 * 60);
        averageSessionLabel.setText("Average Session Time: " + avgMinutes + " minutes");

        // Pet list for revival
        petListComboBox.removeAllItems();
        game.getDeadPets().forEach((id, name) -> {
            petListComboBox.addItem(name + " (ID: " + id + ")");
        });

        // Handle empty dead pets list
        if (game.getDeadPets().isEmpty()) {
            petListComboBox.setVisible(false);
            revivePetButton.setEnabled(false);
            // Create and show "No pets to revive" label if it doesn't exist
            if (noPetsLabel == null) {
                noPetsLabel = new javax.swing.JLabel("No pets to revive");
                noPetsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                noPetsLabel.setForeground(Color.WHITE);
                try {
                    java.io.InputStream fontStream = getClass().getResourceAsStream("/model/assets/font/Minecraft.ttf");
                    if (fontStream != null) {
                        java.awt.Font minecraftFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontStream);
                        noPetsLabel.setFont(minecraftFont.deriveFont(16f));
                    }
                } catch (Exception e) {
                    System.out.println("Error loading font: " + e.getMessage());
                }
                petManagementPanel.add(noPetsLabel, new java.awt.GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
                    java.awt.GridBagConstraints.CENTER, java.awt.GridBagConstraints.HORIZONTAL, 
                    new java.awt.Insets(20, 20, 10, 20), 0, 0));
            }
            noPetsLabel.setVisible(true);
        } else {
            petListComboBox.setVisible(true);
            revivePetButton.setEnabled(true);
            if (noPetsLabel != null) {
                noPetsLabel.setVisible(false);
            }
        }
    }

    /**
     * Creates a pixel grid background.
     * 
     * @param g Graphics object to use for rendering.
     */
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
     * Create the components, set their properties, and add them to the panel.
     */
    private void initComponents() {
        // Create panels
        cardPanel = new javax.swing.JPanel();
        passwordPanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        
        // Create title label
        titleLabel = new javax.swing.JLabel("Parental Controls");
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        
        // Create password components
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        passwordSubmitButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        
        // Create control panel components
        tabbedPane = new javax.swing.JTabbedPane();
        
        // Time restrictions components
        timeRestrictionsPanel = new javax.swing.JPanel();
        enableTimeCheckbox = new javax.swing.JCheckBox();
        startHourLabel = new javax.swing.JLabel();
        endHourLabel = new javax.swing.JLabel();
        startHourSpinner = new javax.swing.JSpinner();
        endHourSpinner = new javax.swing.JSpinner();
        saveTimeButton = new javax.swing.JButton();
        
        // Play stats components
        playStatsPanel = new javax.swing.JPanel();
        totalPlayTimeLabel = new javax.swing.JLabel();
        averageSessionLabel = new javax.swing.JLabel();
        resetStatsButton = new javax.swing.JButton();
        
        // Pet management components
        petManagementPanel = new javax.swing.JPanel();
        petListComboBox = new javax.swing.JComboBox<>();
        revivePetButton = new javax.swing.JButton();
        
        // Set up the cards
        setLayout(new CardLayout());
        
        // Password panel setup
        cardPanel = new javax.swing.JPanel(new CardLayout());
        
        passwordPanel.setLayout(new javax.swing.BoxLayout(passwordPanel, javax.swing.BoxLayout.Y_AXIS));
        
        passwordLabel.setText("Enter Parental Password");
        passwordLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        passwordField.setMaximumSize(new java.awt.Dimension(200, 30));
        passwordField.setAlignmentX(CENTER_ALIGNMENT);
        
        passwordSubmitButton.setText("Submit");
        passwordSubmitButton.setAlignmentX(CENTER_ALIGNMENT);
        passwordSubmitButton.addActionListener(this::passwordSubmitButtonActionPerformed);
        
        backButton.setText("Back");
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.addActionListener(this::backButtonActionPerformed);
        
        // Add spacing
        passwordPanel.add(javax.swing.Box.createVerticalGlue());
        passwordPanel.add(passwordLabel);
        passwordPanel.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 20)));
        passwordPanel.add(passwordField);
        passwordPanel.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 20)));
        passwordPanel.add(passwordSubmitButton);
        passwordPanel.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 20)));
        passwordPanel.add(backButton);
        passwordPanel.add(javax.swing.Box.createVerticalGlue());
        
        cardPanel.add(passwordPanel, "passwordPanel");
        add(cardPanel);
        
        // Set up control panel with tabs
        controlPanel.setLayout(new java.awt.BorderLayout());
        
        // Time Restrictions Tab
        timeRestrictionsPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        
        enableTimeCheckbox.setText("Enable Time Restrictions");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.insets = new java.awt.Insets(20, 20, 10, 20);
        timeRestrictionsPanel.add(enableTimeCheckbox, gbc);
        
        startHourLabel.setText("Start Hour (0-23):");
        gbc.gridy = 1;
        timeRestrictionsPanel.add(startHourLabel, gbc);
        
        startHourSpinner.setModel(new javax.swing.SpinnerNumberModel(8, 0, 23, 1));
        gbc.gridx = 1;
        timeRestrictionsPanel.add(startHourSpinner, gbc);
        
        endHourLabel.setText("End Hour (0-23):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        timeRestrictionsPanel.add(endHourLabel, gbc);
        
        endHourSpinner.setModel(new javax.swing.SpinnerNumberModel(20, 0, 23, 1));
        gbc.gridx = 1;
        timeRestrictionsPanel.add(endHourSpinner, gbc);
        
        saveTimeButton.setText("Save Settings");
        saveTimeButton.addActionListener(this::saveTimeButtonActionPerformed);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(20, 20, 20, 20);
        timeRestrictionsPanel.add(saveTimeButton, gbc);
        
        // Play Statistics Tab
        playStatsPanel.setLayout(new java.awt.GridBagLayout());
        
        totalPlayTimeLabel.setText("Total Play Time: 0h 0m");
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.insets = new java.awt.Insets(20, 20, 10, 20);
        playStatsPanel.add(totalPlayTimeLabel, gbc);
        
        averageSessionLabel.setText("Average Session Time: 0m");
        gbc.gridy = 1;
        playStatsPanel.add(averageSessionLabel, gbc);
        
        resetStatsButton.setText("Reset Statistics");
        resetStatsButton.addActionListener(this::resetStatsButtonActionPerformed);
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(20, 20, 20, 20);
        playStatsPanel.add(resetStatsButton, gbc);
        
        // Pet Management Tab
        petManagementPanel.setLayout(new java.awt.GridBagLayout());
        
        javax.swing.JLabel selectPetLabel = new javax.swing.JLabel("Select Pet to Revive:");
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.insets = new java.awt.Insets(20, 20, 10, 20);
        petManagementPanel.add(selectPetLabel, gbc);
        
        gbc.gridy = 1;
        petManagementPanel.add(petListComboBox, gbc);
        
        revivePetButton.setText("Revive Pet");
        revivePetButton.addActionListener(this::revivePetButtonActionPerformed);
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(20, 20, 20, 20);
        petManagementPanel.add(revivePetButton, gbc);
        
        // Add tabs to tabbed pane
        tabbedPane.addTab("Time Restrictions", timeRestrictionsPanel);
        tabbedPane.addTab("Play Statistics", playStatsPanel);
        tabbedPane.addTab("Pet Management", petManagementPanel);
        
        // Add back button to control panel
        javax.swing.JButton controlBackButton = new javax.swing.JButton("Back to Main Menu");
        controlBackButton.addActionListener(this::controlBackButtonActionPerformed);
        
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        buttonPanel.add(controlBackButton);
        
        controlPanel.add(tabbedPane, java.awt.BorderLayout.CENTER);
        controlPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        
        add(controlPanel);
        
        // Initially hide control panel
        controlPanel.setVisible(false);
    }

    /**
     * Manages the action event for the password submit button.
     * 
     * <p>If the entered password is correct the control panel is made visible. 
     * If the password in incorrect, an error message is displayed and the 
     * password field is cleared.</p>
     * 
     * @param evt action event triggred when the password submit button is clicked.
     */
    private void passwordSubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String enteredPassword = new String(passwordField.getPassword());
        
        if (game.verifyParentPassword(enteredPassword)) {
            isPasswordVerified = true;
            showControlPanel();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Incorrect password. Please try again.", 
                "Authentication Failed", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
    
    /**
     * Manages the action event for the back button.
     * 
     * <p>If the main frame exists navigate to the start panel using the main frame.
     * Otherwise, use the parent component's card layout to go to the start panel.</p>
     * 
     * @param evt action event triggred when the back button is clicked.
     */
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Go back to start panel
        if (mainFrame != null) {
            mainFrame.showPanel("startPanel");
        } else {
            CardLayout card = (CardLayout)getParent().getLayout();
            card.show(getParent(), "startPanel");
        }
    }
    
    /**
     * Manages the control back button.
     * 
     * <p>If the main frame exists navigate to the start panel using the main frame.
     * Otherwise, use the parent component's card layout to go to the start panel.</p>
     * 
     * @param evt action event triggred when the control back button is clicked.
     */
    private void controlBackButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Go back to start panel
        if (mainFrame != null) {
            mainFrame.showPanel("startPanel");
        } else {
            CardLayout card = (CardLayout)getParent().getLayout();
            card.show(getParent(), "startPanel");
        }
    }
    
    /**
     * Manages the action event for the save time button.
     * 
     * <p>Save the time restriction settings that the parent selected.</p>
     * 
     * @param evt action event triggred when the save time button is clicked.
     */
    private void saveTimeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean enabled = enableTimeCheckbox.isSelected();
        int startHour = (Integer)startHourSpinner.getValue();
        int endHour = (Integer)endHourSpinner.getValue();
        
        game.updateParentalControls(enabled, startHour, endHour, null);
        
        JOptionPane.showMessageDialog(this, 
            "Time restriction settings saved!", 
            "Settings Saved", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Manages the action event for the reset stats button.
     * 
     * <p>Reset the play time statistics.</p>
     * 
     * @param evt action event triggred when the reset stats button is clicked.
     */
    private void resetStatsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to reset all play time statistics?",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            game.resetPlayTimeStats();
            updateControlPanelData();
            
            JOptionPane.showMessageDialog(this,
                "Play time statistics have been reset.",
                "Statistics Reset",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Manages the action event for the revive pet button. 
     * 
     * <p>Revive the pet that the parent selected. Display a success or error message 
     * depending on whether the revival succeded.</p>
     * 
     * @param evt action event triggred when the revive put button is clicked.
     */
    private void revivePetButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedItem = (String)petListComboBox.getSelectedItem();
        
        if (selectedItem != null) {
            // Extract pet ID from selected item (format: "Name (ID: X)")
            int startIndex = selectedItem.lastIndexOf("ID: ") + 4;
            int endIndex = selectedItem.lastIndexOf(")");
            
            if (startIndex > 0 && endIndex > startIndex) {
                try {
                    int petId = Integer.parseInt(selectedItem.substring(startIndex, endIndex));
                    
                    boolean success = game.revivePet(petId);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(this,
                            "Pet has been revived successfully! Restart the game to see the changes.",
                            "Pet Revived",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Failed to revive pet. Please try again.",
                            "Revival Failed",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid pet ID format.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel cardPanel;
    private javax.swing.JPanel passwordPanel;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JButton passwordSubmitButton;
    private javax.swing.JButton backButton;
    
    private javax.swing.JPanel controlPanel;
    private javax.swing.JTabbedPane tabbedPane;
    
    private javax.swing.JPanel timeRestrictionsPanel;
    private javax.swing.JCheckBox enableTimeCheckbox;
    private javax.swing.JLabel startHourLabel;
    private javax.swing.JLabel endHourLabel;
    private javax.swing.JSpinner startHourSpinner;
    private javax.swing.JSpinner endHourSpinner;
    private javax.swing.JButton saveTimeButton;
    
    private javax.swing.JPanel playStatsPanel;
    private javax.swing.JLabel totalPlayTimeLabel;
    private javax.swing.JLabel averageSessionLabel;
    private javax.swing.JButton resetStatsButton;
    
    private javax.swing.JPanel petManagementPanel;
    private javax.swing.JComboBox<String> petListComboBox;
    private javax.swing.JButton revivePetButton;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel noPetsLabel;  // New label for empty state
    // End of variables declaration


}
