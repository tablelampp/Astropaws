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
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import model.Game;

/**
 * This panel allows the user to choose which saved game they would like to play.
 */
public class ChooseFilePanel extends javax.swing.JPanel {

    private Game game;
    private GUI mainFrame;
    // Maps display names to pet IDs
    private Map<String, Integer> saveFilesMap;

    /**
     * Creates new form ChooseFilePanel.
     * 
     * @param gameInstance the current game that is running.
     * @param mainFrame the main GUI frame.
     */
    public ChooseFilePanel(Game gameInstance, GUI mainFrame) {
        this.game = gameInstance;
        this.mainFrame = mainFrame;
        this.saveFilesMap = new HashMap<>();

        initComponents();
        setupUI();
    }

    /**
     * Constructor for  compatibility with form editor
     */
    public ChooseFilePanel() {
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
            chooseFileLabel.setFont(minecraftFont);
            newSaveButton.setFont(minecraftFont.deriveFont(24f));
            fileStartButton.setFont(minecraftFont.deriveFont(24f));
            fileBackButton.setFont(minecraftFont.deriveFont(24f));
            fileComboBox.setFont(minecraftFont.deriveFont(24f));
        } catch (Exception e) {
            // Fallback to monospace if Minecraft font not found
            Font fallbackFont = new Font("Monospaced", Font.BOLD, 48);
            chooseFileLabel.setFont(fallbackFont);
            newSaveButton.setFont(fallbackFont.deriveFont(24f));
            fileStartButton.setFont(fallbackFont.deriveFont(24f));
            fileBackButton.setFont(fallbackFont.deriveFont(24f));
            fileComboBox.setFont(fallbackFont.deriveFont(24f));
        }
        
        // Set button styling
        newSaveButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        fileStartButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        fileBackButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Set background color
        setBackground(new Color(44, 62, 80)); // Dark blue-gray background
    }

    /**
     * Loads save files when panel becomes visible
     */
    public void loadSaveFiles() {
        saveFilesMap.clear();

        // Get available pets from game
        Map<Integer, String> availablePets = game.getAlivePets();

        if (availablePets.isEmpty()) {
            fileComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"No saved pets found"}));
            fileStartButton.setEnabled(false);
        } else {
            // Create display names for combo box
            String[] displayNames = new String[availablePets.size()];
            int index = 0;

            for (Map.Entry<Integer, String> entry : availablePets.entrySet()) {
                int petId = entry.getKey();
                String petName = entry.getValue();

                String displayName = petName;
                
                displayNames[index] = displayName;
                saveFilesMap.put(displayName, petId);
                index++;
            }

            fileComboBox.setModel(new DefaultComboBoxModel<>(displayNames));
            fileStartButton.setEnabled(true);
        }
    }

    /**
     * Overrides the {@code addNotify} method.
     * Loads save files and start the main theme background music.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        // Load save files when the panel is shown
        loadSaveFiles();
        // Start playing the main theme when the panel is shown
        model.SoundManager.getInstance().playMainTheme();
    }

    /**
     * Overrides the {@code removeNotify} method.
     * Stops the main theme background music when the user leaves the panel.
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        // Stop the main theme when leaving the panel
        model.SoundManager.getInstance().stopMainTheme();
    }

    /**
     * Overrides the {@code paintComponent} method to create a pixel grid background.
     * 
     * @param g the Graphics object used for creating the background.
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        chooseFileLabel = new javax.swing.JLabel();
        newSaveButton = new javax.swing.JButton();
        fileStartButton = new javax.swing.JButton();
        fileComboBox = new javax.swing.JComboBox<>();
        fileBackButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(800, 600));

        chooseFileLabel.setForeground(new java.awt.Color(236, 240, 241));
        chooseFileLabel.setText("Choose a Save File");
        chooseFileLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        newSaveButton.setBackground(new java.awt.Color(46, 204, 113));
        newSaveButton.setForeground(new java.awt.Color(236, 240, 241));
        newSaveButton.setText("NEW SAVE");
        newSaveButton.setFocusPainted(false);
        newSaveButton.setBorderPainted(false);
        newSaveButton.setPreferredSize(new java.awt.Dimension(200, 40));
        newSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSaveButtonActionPerformed(evt);
            }
        });

        fileStartButton.setBackground(new java.awt.Color(46, 204, 113));
        fileStartButton.setForeground(new java.awt.Color(236, 240, 241));
        fileStartButton.setText("START");
        fileStartButton.setFocusPainted(false);
        fileStartButton.setBorderPainted(false);
        // Disabled until a save is selected
        fileStartButton.setEnabled(false);
        fileStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileStartButtonActionPerformed(evt);
            }
        });

        fileComboBox.setBackground(new java.awt.Color(52, 73, 94));
        fileComboBox.setForeground(new java.awt.Color(236, 240, 241));
        // Empty by default
        fileComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        fileComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileComboBoxActionPerformed(evt);
            }
        });

        fileBackButton.setBackground(new java.awt.Color(231, 76, 60));
        fileBackButton.setForeground(new java.awt.Color(236, 240, 241));
        fileBackButton.setText("BACK");
        fileBackButton.setFocusPainted(false);
        fileBackButton.setBorderPainted(false);
        fileBackButton.setPreferredSize(new java.awt.Dimension(200, 40));
        fileBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBackButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(chooseFileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(fileStartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fileBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(chooseFileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(fileComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileStartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(200, 200, 200)
                .addComponent(fileBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(133, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Manages the action event for the new save button.
     * 
     * <p>The sound effect plays, and the panel changes to the choose pet panel.
     * If the main frame exists, main frame handles the navigation to the choose pet panel,
     * otherwise it is handled using the parent component's card layout.</p>
     * 
     * @param evt the action event triggered by the button click.
     */
    private void newSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        // go to the choose pet screen
        if (mainFrame != null) {
            mainFrame.showPanel("choosePetPanel");
        } else {
        CardLayout card = (CardLayout)getParent().getLayout();
        card.show(getParent(), "choosePetPanel");
        }
    }

    /**
     * Manages the action event for the back button.
     * 
     * <p>Plays the sound effect, and changes the panel to the start panel.
     * If the main frame exists, main frame handles the navigation to the start panel,
     * otherwise it is handled by the parent component's card layout.</p>
     * 
     * @param evt action event triggered by the button click.
     */
    private void fileBackButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        // go back to the start screen
        if (mainFrame != null) {
            mainFrame.showPanel("startPanel");
        } else {
        CardLayout card = (CardLayout)getParent().getLayout();
            card.show(getParent(), "startPanel");
        }
    }

    /**
     * Manages the action event for the file combo box.
     * Ensures the start button can be clicked only if a save file is selected.
     * 
     * @param evt action event triggered when an item is selected.
     */
    private void fileComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        // Enable start button only if a save file is selected
        String selectedItem = (String) fileComboBox.getSelectedItem();
        fileStartButton.setEnabled(selectedItem != null && !selectedItem.equals("No saved pets found") && saveFilesMap.containsKey(selectedItem));
    }

    /**
     * Manages the action event for the start button.
     * 
     * <p>Plays a sound effect and loads the selected pet. If the pet was loaded 
     * successfully, navigate to the game panel using main frame if it exists and card
     * layout otherwise. Display an error message if the pet could not be loaded.</p>
     * 
     * @param evt action event triggered when the start button is clicked.
     */
    private void fileStartButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        String selectedItem = (String) fileComboBox.getSelectedItem();
        
        if (selectedItem != null && saveFilesMap.containsKey(selectedItem)) {
            int petID = saveFilesMap.get(selectedItem);
            
            // Load the selected pet
            boolean loadSuccess = game.loadPet(petID);
            
            if (loadSuccess) {
                // Start the game
                game.startGame();
                
                // Navigate to game panel
                if (mainFrame != null) {
                    mainFrame.showPanel("gamePanel");
                } else {
                    CardLayout card = (CardLayout)getParent().getLayout();
                    card.show(getParent(), "gamePanel");
                }
            } else {
                // Show error message with JOptionPane instead of just printing to console
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to load pet. This could be due to parental time restrictions or an error.",
                    "Load Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel chooseFileLabel;
    private javax.swing.JButton fileBackButton;
    private javax.swing.JComboBox<String> fileComboBox;
    private javax.swing.JButton fileStartButton;
    private javax.swing.JButton newSaveButton;
    // End of variables declaration//GEN-END:variables
}
