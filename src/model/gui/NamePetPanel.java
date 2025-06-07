/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package model.gui;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.border.EmptyBorder;
import java.io.File;
import javax.swing.JOptionPane;
import model.Game;

/**
 * This screen is where the user will name their pet.
 */
public class NamePetPanel extends javax.swing.JPanel {

    private Game game;
    private GUI mainFrame;

    /**
     * Constructor to create a NamePetPanel. Sets up the components of the panel.
     * 
     * @param gameInstance the current game that is running.
     * @param mainFrame the main GUI frame.
     */
    public NamePetPanel(Game gameInstance, GUI mainFrame) {
        this.game = gameInstance;
        this.mainFrame = mainFrame;

        initComponents();
        setupUI();

        // Create background panel with the image
        BackgroundPanel backgroundPanel = new BackgroundPanel("/model/assets/startScreenBackdrop.png");
        backgroundPanel.setLayout(new java.awt.BorderLayout());

        // Add all components to the background panel
        backgroundPanel.add(namePetLabel, java.awt.BorderLayout.NORTH);
        backgroundPanel.add(mainContentPanel, java.awt.BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        // Remove existing components from this panel
        removeAll();

        // Add the background panel to this panel
        setLayout(new java.awt.BorderLayout());
        add(backgroundPanel, java.awt.BorderLayout.CENTER);
    }

    /**
     * Constructor for compatibility with form editor
     */
    public NamePetPanel() {
        this(new Game(), null);
    }

    /**
     * Sets the font and button styling to set up the UI.
     */
    private void setupUI() {
        // Set custom Minecraft font
        try {
            Font minecraftFont = Font.createFont(Font.TRUETYPE_FONT, 
                new File("assets/font/Minecraft.ttf")).deriveFont(48f);
            namePetLabel.setFont(minecraftFont);
            namePetTextField.setFont(minecraftFont.deriveFont(24f));
            namePetNextButton.setFont(minecraftFont.deriveFont(24f));
            namePetBackButton.setFont(minecraftFont.deriveFont(24f));
            validationLabel.setFont(minecraftFont.deriveFont(16f));
        } catch (Exception e) {
            // Fallback to monospace if Minecraft font not found
            Font fallbackFont = new Font("Monospaced", Font.BOLD, 48);
            namePetLabel.setFont(fallbackFont);
            namePetTextField.setFont(fallbackFont.deriveFont(24f));
            namePetNextButton.setFont(fallbackFont.deriveFont(24f));
            namePetBackButton.setFont(fallbackFont.deriveFont(24f));
            validationLabel.setFont(fallbackFont.deriveFont(16f));
        }
        
        // Set button styling
        namePetNextButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        namePetBackButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Make panels transparent
        setOpaque(false);
        mainContentPanel.setOpaque(false);
        buttonPanel.setOpaque(false);

        // Add text field listener to validate input
        namePetTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validateInput();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validateInput();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validateInput();
            }
        });

        // Disable start button until valid name is entered
        namePetNextButton.setEnabled(false);
    }

    /**
     * Validated the input of the name pet text field.
     */
    private void validateInput() {
        String name = namePetTextField.getText().trim();
        boolean isValid = !name.isEmpty() && name.length() <= 10;
        namePetNextButton.setEnabled(isValid);
        validationLabel.setVisible(!isValid && name.length() > 10);
    }

    /**
     * Overrides the {@code addNotify} method to reset the input when the 
     * panel becomes visible.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        namePetTextField.setText("");
        namePetNextButton.setEnabled(false);

        // Set focus to text field
        namePetTextField.requestFocusInWindow();

        // Update UI with selected pet type
        if (mainFrame != null && mainFrame.getSelectedPetType() != null) {
            String petType = mainFrame.getSelectedPetType();
            String displayType = petType.substring(0, 1).toUpperCase() + petType.substring(1);
            namePetLabel.setText("Name Your " + displayType + "tronaut");
        } else {
            namePetLabel.setText("Name Your Pet");
        }
    }

    /**
     * Overrides the {@code paintComponent} method to remove the pixel grid background.
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
        namePetLabel = new javax.swing.JLabel();
        namePetTextField = new javax.swing.JTextField();
        namePetNextButton = new javax.swing.JButton();
        namePetBackButton = new javax.swing.JButton();
        mainContentPanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        validationLabel = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(800, 600));

        namePetLabel.setForeground(new java.awt.Color(236, 240, 241));
        namePetLabel.setText("Name Your Pet");
        namePetLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        namePetTextField.setBackground(new java.awt.Color(44, 62, 80));
        namePetTextField.setForeground(new java.awt.Color(236, 240, 241));
        namePetTextField.setCaretColor(new java.awt.Color(236, 240, 241));
        namePetTextField.setFont(new java.awt.Font("Monospaced", 0, 24));
        namePetTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        namePetTextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(46, 204, 113), 2));
        namePetTextField.setPreferredSize(new java.awt.Dimension(300, 40));

        // Set up validation label
        validationLabel.setForeground(new java.awt.Color(231, 76, 60));
        validationLabel.setText("Must be 1-10 characters");
        validationLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        validationLabel.setVisible(false);

        namePetNextButton.setBackground(new java.awt.Color(46, 204, 113));
        namePetNextButton.setForeground(new java.awt.Color(236, 240, 241));
        namePetNextButton.setText("NEXT");
        namePetNextButton.setFocusPainted(false);
        namePetNextButton.setBorderPainted(false);
        namePetNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namePetNextButtonActionPerformed(evt);
            }
        });

        namePetBackButton.setBackground(new java.awt.Color(231, 76, 60));
        namePetBackButton.setForeground(new java.awt.Color(236, 240, 241));
        namePetBackButton.setText("BACK");
        namePetBackButton.setFocusPainted(false);
        namePetBackButton.setBorderPainted(false);
        namePetBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namePetBackButtonActionPerformed(evt);
            }
        });

        // Set up main content panel with GridBagLayout
        mainContentPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        
        // Add text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(20, 20, 5, 20);
        mainContentPanel.add(namePetTextField, gbc);

        // Add validation label below text field
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(0, 20, 10, 20);
        mainContentPanel.add(validationLabel, gbc);

        // Set up button panel
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 0));
        buttonPanel.add(namePetBackButton);
        buttonPanel.add(namePetNextButton);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Manages the action event for the next button.
     * 
     * <p>In order for the game to move to the next screen, the pet must have a 
     * valid name. If this condition is met, the pet's name is set and the 
     * game panel is shown.</p>
     * 
     * @param evt action event triggered when the next button is clicked.
     */
    private void namePetNextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String petName = namePetTextField.getText().trim();

        if (petName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name for your pet.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (petName.length() > 10) {
            JOptionPane.showMessageDialog(this, "Pet name must be 10 characters or less.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (mainFrame != null) {
            mainFrame.setPetName(petName);  // Store pet name in the main frame
            mainFrame.showPanel("gamePanel");   // Go to the game screen
        } else {
            // Fallback if mainFrame reference is not available
            CardLayout card = (CardLayout)getParent().getLayout();
            card.show(getParent(), "gamePanel");
        }
    }//GEN-LAST:event_namePetNextButtonActionPerformed

    /**
     * Manages the event action for the back button. 
     * 
     * <p>When the back button is clicked, navigate to the choose pet panel.
     * If the main frame exists, main frame is used to navigate to the choose pet panel,
     * otherwise, the parent component's card layout it used.</p>
     * 
     * @param evt
     */
    private void namePetBackButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainFrame != null) {
            mainFrame.showPanel("choosePetPanel"); // Go back to the choose pet screen
        } else {
            // go back to the choose pet screen
            CardLayout card = (CardLayout)getParent().getLayout();
            card.show(getParent(), "choosePetPanel");
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton namePetBackButton;
    private javax.swing.JLabel namePetLabel;
    private javax.swing.JButton namePetNextButton;
    private javax.swing.JTextField namePetTextField;
    private javax.swing.JPanel mainContentPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel validationLabel;
    // End of variables declaration//GEN-END:variables
}
