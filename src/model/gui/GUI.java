// NETBEANS VERSION

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package model.gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import model.Game;
/**
 * This is the main frame that handles the panels of the GUI.
 */
public class GUI extends javax.swing.JFrame {
    
    private Game game;
    private String selectedPetType;
    private String petName;

    /**
     * Constructor to create a GUI object. Sets the properties of of the JFrame.
     */
    public GUI() {
        initComponents();
        setSize(800, 600);

        // Create background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("/model/assets/startScreenBackdrop.png");

        // Save reference to main panel
        JPanel contentPanel = mainPanel;

        // Remove main panel from frame
        getContentPane().remove(mainPanel);

        // Add background panel to frame
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(backgroundPanel, java.awt.BorderLayout.CENTER);

        // Add main panel to background panel
        backgroundPanel.add(contentPanel, java.awt.BorderLayout.CENTER);

        // Initialize game
        game = new Game();

        setupPanels();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (game != null) {
                    game.savePet();
                    game.stopGame();
                }
            }
        });

        // Set the title of the window
        setTitle("ASTROPAWS");
    }
    
    /**
     * Sets up each of the panels and adds them to the main panel.
     */
    private void setupPanels() {
        // Create panels with names
        JPanel startPanel = new StartPanel(game, this);
        startPanel.setName("startPanel");
        
        JPanel chooseFilePanel = new ChooseFilePanel(game, this);
        chooseFilePanel.setName("chooseFilePanel");
        
        JPanel choosePetPanel = new ChoosePetPanel(game, this);
        choosePetPanel.setName("choosePetPanel");
        
        JPanel namePetPanel = new NamePetPanel(game, this);
        namePetPanel.setName("namePetPanel");
        
        JPanel gamePanel = new GamePanel(game, this);
        gamePanel.setName("gamePanel");
        
        JPanel settingsPanel = new SettingsPanel(game, this);
        settingsPanel.setName("settingsPanel");
        
        JPanel parentalControlPanel = new ParentalControlPanel(game, this);
        parentalControlPanel.setName("parentalControlPanel");
        
        JPanel spaceMissionPanel = new SpaceMissionPanel(game, this);
        spaceMissionPanel.setName("spaceMissionPanel");

        // Add panels to main panel
        mainPanel.add(startPanel, "startPanel");
        mainPanel.add(chooseFilePanel, "chooseFilePanel");
        mainPanel.add(choosePetPanel, "choosePetPanel");
        mainPanel.add(namePetPanel, "namePetPanel");
        mainPanel.add(gamePanel, "gamePanel");
        mainPanel.add(settingsPanel, "settingsPanel");
        mainPanel.add(parentalControlPanel, "parentalControlPanel");
        mainPanel.add(spaceMissionPanel, "spaceMissionPanel");

        // Show the start panel initially
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "startPanel");
    }
    
    /**
     * Switch panels using card layout.
     * 
     * @param panelName the name of the panel to switch to.
     */
    public void showPanel(String panelName) {
        JPanel targetPanel = getPanel(panelName);
        if (targetPanel != null) {
            if (panelName.equals("gamePanel")) {
                // Create and start new pet when transitioning to game panel
                if (selectedPetType != null && petName != null && !petName.trim().isEmpty()) {
                    boolean success = createAndStartNewPet();
                    if (!success) {
                        JOptionPane.showMessageDialog(this, "Failed to create pet.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            CardLayout card = (CardLayout) mainPanel.getLayout();
            card.show(mainPanel, panelName);
        }
    }

    /**
     * Get a panel based on its name.
     * 
     * @param panelName the name of the panel to return.
     * @return the panel with the name panelName or null if panelName does not exist.
     */
    public JPanel getPanel(String panelName) {
        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof JPanel && comp.getName() != null && comp.getName().equals(panelName)) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    /**
     * Getter for the game.
     * 
     * @return the game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Setter for the selectedPetType.
     * 
     * @param petType value to assign to the selectedPetType.
     */
    public void setSelectedPetType(String petType) {
        this.selectedPetType = petType;
    }

    /**
     * Getter for the selectedPetType.
     * 
     * @return the selectedPetType.
     */
    public String getSelectedPetType() {
        return selectedPetType;
    }

    /**
     * Setter for the petName.
     * 
     * @param petName value to assign as the pet's name.
     */
    public void setPetName(String petName) {
        this.petName = petName;
    }

    /**
     * Getter for the petName.
     * 
     * @return the name of the pet.
     */
    public String getPetName() {
        return petName;
    }

    /**
     * Create and new pet if the there is a selected pet type and the pet has a name.
     * If the pet is created successfully, start the game.
     * 
     * @return true if the game has be started successfully and false otherwise.
     */
    public boolean createAndStartNewPet() {
        if (selectedPetType != null && petName != null && !petName.trim().isEmpty()) {
            boolean success = game.createNewPet(petName, selectedPetType);

            if (success) {
                game.startGame();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Panel for the background image for the game.
     */
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        /**
         * Constructor for the BackgroundPanel. It attemps to load the image for the 
         * background and displays an error message it it fails.
         * 
         * @param imagePath the path of the image to be used for the background.
         */
        public BackgroundPanel(String imagePath) {
            try {
                java.net.URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    backgroundImage = new ImageIcon(imageUrl).getImage();
                    System.out.println("Background image loaded: " + imagePath);
                } else {
                    System.err.println("Could not find background image: " + imagePath);
                }
            } catch (Exception e) {
                System.err.println("Error loading background image: " + e.getMessage());
            }
            setLayout(new java.awt.BorderLayout());
        }

        /**
         * Overrides the {@code paintComponent} method to draw a background image.
         * If an image is already set, it scales the image to fit the panel.
         * 
         * @param g the Graphics object used for rendering.
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

        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ASTRO PAWS"); 
        setResizable(false);
        setSize(new java.awt.Dimension(1000, 800));

        mainPanel.setSize(new java.awt.Dimension(200, 100));
        mainPanel.setLayout(new java.awt.CardLayout());
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>                        

    /**
     * The main method for the game.
     * 
     * <p>Sets the look and feel to Nimbus if possible. Then it
     * creates and displays the main GUI frame.</p>
     * 
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel mainPanel;
    // End of variables declaration                   
}
