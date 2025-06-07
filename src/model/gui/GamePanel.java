/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package model.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import model.Game;
import model.item.Food;
import model.item.Toy;
import model.pet.Pet;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.BoxLayout;
import javax.swing.Box;

/**
 * This is the game panel where the game play takes place. It displays the pet and 
 * buttons to allow users to interact with their pet.
 */
public class GamePanel extends javax.swing.JPanel {

    
    private Game game;
    private GUI mainFrame;
    private Timer uiUpdateTimer;
    // private String petType;
    private Pet.PetState lastPetState;
    private ImageIcon petImage;
    private Random random = new Random();
    private Timer itemDropTimer;
    private boolean itemAvailable = false;
    private model.item.Item currentDroppedItem = null;
    
    // Animation variables
    private Timer animationTimer;
    private int animationOffset = 0;
    private boolean movingRight = true;
    private static final int ANIMATION_SPEED = 200; // milliseconds between updates (increased from 50)
    private static final int MAX_OFFSET = 3; // maximum pixels to move (reduced from 5)

    /**
     * Constructor to create a game panel. It sets up the components
     * and starts the timer.
     * 
     * @param gameInstance the current game that is running.
     */
    public GamePanel(Game gameInstance) {
        this.game = gameInstance;
        initComponents();
        setupUI();
        // setupKeyboardShortcuts();

        // Start timer to update UI regularly
        uiUpdateTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePetUI();
            }
        });
        
        // Initialize animation timer
        setupAnimationTimer();
    }

    /**
     * Constructor to create a game panel. It sets up the components
     * and starts the timer.
     * 
     * @param gameInstance the current game that is running.
     * @param mainFrame the main GUI frame.
     */
    public GamePanel(Game gameInstance, GUI mainFrame) {
        this.game = gameInstance;
        this.mainFrame = mainFrame;
        initComponents();
        setupUI();
        // setupKeyboardShortcuts();

        // Initialize UI update timer
        uiUpdateTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePetUI();
            }
        });
        
        // Initialize animation timer
        setupAnimationTimer();
    }

    /**
     * Sets up the components of the GUI.
     */
    private void setupUI() {
        // Set background color
        setBackground(new Color(44, 62, 80));

        // Apply Minecraft font to all text elements
        try {
            java.io.InputStream fontStream = getClass().getResourceAsStream("/model/assets/font/Minecraft.ttf");
            if (fontStream != null) {
                java.awt.Font minecraftFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontStream);
                
                // Apply font to labels with larger size
                petNameLabel.setFont(minecraftFont.deriveFont(28f)); // Increased size for better visibility
                stateLabel.setFont(minecraftFont.deriveFont(14f));  // Reduced from 20f
                scoreLabel.setFont(minecraftFont.deriveFont(14f));  // Reduced from 20f
                dropItemLabel.setFont(minecraftFont.deriveFont(18f));
                feedbackLabel.setFont(minecraftFont.deriveFont(10f)); // Reduced Minecraft font size

                // Store the Minecraft font for use in dialogs and tooltips
                UIManager.put("OptionPane.messageFont", minecraftFont.deriveFont(16f));
                UIManager.put("OptionPane.buttonFont", minecraftFont.deriveFont(14f));
                UIManager.put("ToolTip.font", minecraftFont.deriveFont(12f));

                // Apply font to stat labels
                healthLabel.setFont(minecraftFont.deriveFont(16f));
                hungerLabel.setFont(minecraftFont.deriveFont(16f));
                energyLabel.setFont(minecraftFont.deriveFont(16f));
                happinessLabel.setFont(minecraftFont.deriveFont(16f));
                spaceReadinessLabel.setFont(minecraftFont.deriveFont(16f));

                // Apply font to buttons
                feedButton.setFont(minecraftFont.deriveFont(16f));
                playButton.setFont(minecraftFont.deriveFont(16f));
                sleepButton.setFont(minecraftFont.deriveFont(16f));
                vetButton.setFont(minecraftFont.deriveFont(16f));
                spaceButton.setFont(minecraftFont.deriveFont(16f));
                giftButton.setFont(minecraftFont.deriveFont(16f));
                exerciseButton.setFont(minecraftFont.deriveFont(16f));
                collectButton.setFont(minecraftFont.deriveFont(16f));
                inventoryButton.setFont(minecraftFont.deriveFont(16f));
                settingsButton.setFont(minecraftFont.deriveFont(16f));

                // Apply font to progress bars
                healthProgressBar.setFont(minecraftFont.deriveFont(12f));
                hungerProgressBar.setFont(minecraftFont.deriveFont(12f));
                energyProgressBar.setFont(minecraftFont.deriveFont(12f));
                happinessProgressBar.setFont(minecraftFont.deriveFont(12f));
                spaceReadinessProgressBar.setFont(minecraftFont.deriveFont(12f));
            } else {
                System.out.println("Error loading font: Can't read /model/assets/font/Minecraft.ttf");
            }
        } catch (Exception e) {
            System.out.println("Error loading font: " + e.getMessage());
        }

        // Initially disable action buttons until pet is loaded
        feedButton.setEnabled(false);
        playButton.setEnabled(false);
        sleepButton.setEnabled(false);
        vetButton.setEnabled(false);
        spaceButton.setEnabled(false);
        giftButton.setEnabled(false);
        exerciseButton.setEnabled(false);

        // Set up progress bars
        hungerProgressBar.setStringPainted(true);
        happinessProgressBar.setStringPainted(true);
        energyProgressBar.setStringPainted(true);
        healthProgressBar.setStringPainted(true);
        spaceReadinessProgressBar.setStringPainted(true);

        // Set initial text
        petNameLabel.setText("Loading pet...");
        stateLabel.setForeground(new java.awt.Color(0, 0, 0));
        stateLabel.setBackground(new java.awt.Color(255, 255, 255));
        stateLabel.setOpaque(true);
        stateLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        stateLabel.setText("State: Normal");

        scoreLabel.setForeground(new java.awt.Color(0, 0, 0));
        scoreLabel.setBackground(new java.awt.Color(255, 255, 255));
        scoreLabel.setOpaque(true);
        scoreLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        scoreLabel.setText("Score: 0");

        // Make pet image area visible
        petImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        petImageLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        petImageLabel.setPreferredSize(new java.awt.Dimension(300, 300));
        
        // Keyboard shortcuts for actions
        feedButton.setMnemonic(KeyEvent.VK_F);  // F for Feed
        playButton.setMnemonic(KeyEvent.VK_P);  // P for Play
        sleepButton.setMnemonic(KeyEvent.VK_S); // S for Sleep
        giftButton.setMnemonic(KeyEvent.VK_G);  // G for Gift
        exerciseButton.setMnemonic(KeyEvent.VK_E); // E for Exercise
        vetButton.setMnemonic(KeyEvent.VK_V);   // V for Vet
        
        // Set up item drop timer (30000 ms = 30 seconds)
        itemDropTimer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropRandomItem();
            }
        });
        itemDropTimer.start();
        
        // Scale action buttons
        scaleButtonIcon(feedButton, "/model/assets/feed.png", 60, 60);
        scaleButtonIcon(playButton, "/model/assets/play.png", 60, 60);
        scaleButtonIcon(sleepButton, "/model/assets/sleep.png", 60, 60);
        scaleButtonIcon(exerciseButton, "/model/assets/exercise.png", 60, 60);
        scaleButtonIcon(giftButton, "/model/assets/gift.png", 60, 60);
        scaleButtonIcon(vetButton, "/model/assets/vet.png", 60, 60);
        scaleButtonIcon(spaceButton, "/model/assets/space.png", 60, 60);
        scaleButtonIcon(settingsButton, "/model/assets/settings.png", 24, 24);
        scaleButtonIcon(inventoryButton, "/model/assets/inventory.png", 24, 24);
        inventoryButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        inventoryButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        // Initially disable collect button until an item is available
        collectButton.setEnabled(false);
        dropItemLabel.setText("");
        
        // feedback Label setup
        feedbackLabel.setEditable(false);
        feedbackLabel.setBackground(new java.awt.Color(255, 255, 255));
        feedbackLabel.setForeground(new java.awt.Color(0, 0, 0));
        feedbackLabel.setLineWrap(true);
        feedbackLabel.setWrapStyleWord(true);
        feedbackLabel.setMaximumSize(new java.awt.Dimension(250, 60));
        feedbackLabel.setPreferredSize(new java.awt.Dimension(250, 60));
        feedbackLabel.setMinimumSize(new java.awt.Dimension(250, 60));
        feedbackLabel.setOpaque(true);
        feedbackLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        feedbackLabel.setAlignmentX(javax.swing.SwingConstants.CENTER);
        feedbackLabel.setAlignmentY(javax.swing.SwingConstants.CENTER);
        feedbackLabel.setVisible(false);
        feedbackLabel.setColumns(25);
        feedbackLabel.setRows(2);
        feedbackLabel.setFocusable(false);
        feedbackLabel.setRequestFocusEnabled(false);
        
        // Set tooltips for progress bars
        healthProgressBar.setToolTipText("Health: How healthy your pet is");
        hungerProgressBar.setToolTipText("Hunger: How full your pet is");
        energyProgressBar.setToolTipText("Energy: How well-rested your pet is");
        happinessProgressBar.setToolTipText("Happiness: How happy your pet is");
        spaceReadinessProgressBar.setToolTipText("Space Readiness: How prepared your pet is for a space mission");
        healthLabel.setToolTipText("Health: How healthy your pet is");
        hungerLabel.setToolTipText("Hunger: How full your pet is");
        energyLabel.setToolTipText("Energy: How well-rested your pet is");
        happinessLabel.setToolTipText("Happiness: How happy your pet is");
        spaceReadinessLabel.setToolTipText("Space Readiness: How prepared your pet is for a space mission");
        
        // Set up the background
        try {
            // Set background image using resource path
            java.net.URL imageUrl = getClass().getResource("/model/assets/gameBackdrop.png");
            if (imageUrl != null) {
                final ImageIcon backgroundImage = new ImageIcon(imageUrl);

                // Create a simple background panel that extends JPanel
                JPanel backgroundPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        // Draw the image to fill the panel
                        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                    }
                };

                // Set the layout and make all panels transparent
                backgroundPanel.setLayout(new BorderLayout());
                setOpaque(false);
                northPanel.setOpaque(false);
                centerPanel.setOpaque(false);
                actionsPanel.setOpaque(false);

                // Add components to background panel
                backgroundPanel.add(northPanel, BorderLayout.NORTH);
                backgroundPanel.add(centerPanel, BorderLayout.CENTER);
                backgroundPanel.add(actionsPanel, BorderLayout.SOUTH);

                // Add background panel to the main panel
                removeAll();
                setLayout(new BorderLayout());
                add(backgroundPanel, BorderLayout.CENTER);
            } else {
                System.err.println("Background image not found at: /model/assets/backdrop.png");
            }
        } catch (Exception e) {
            System.err.println("Error setting background: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Drops items randomly for the player to pick up.
     * 
     * <p>Only drops an item if there is not available to be picked up. The item 
     * is chosen randomly from lists of either food or toys. If possible, the 
     * image of the item is displayed, otherwise, it will be represented with text.</p>
     */
    private void dropRandomItem() {
        // If an item is already available, don't drop a new one
        if (itemAvailable) return;

        Random random = new Random();
        int itemType = random.nextInt(2); // 0 for food, 1 for toy

        if (itemType == 0) {
            // Create a random food
            String[] foodTypes = {"Kibble", "Apple", "Cheese", "Bread", "IceCream", "Chicken"};
            String foodName = foodTypes[random.nextInt(foodTypes.length)];
            currentDroppedItem = new model.item.Food(foodName, "A tasty treat");
        } else {
            // Create a random toy
            String[] toyTypes = {"Ball", "Rocket", "Frisbee", "Alien", "Starplush"};
            String toyName = toyTypes[random.nextInt(toyTypes.length)];
            currentDroppedItem = new model.item.Toy(toyName, "A fun toy");
        }

        // Show the item image instead of text
        String imagePath = "/model/assets/items/" + currentDroppedItem.getName().toLowerCase() + ".png";
        try {
            java.net.URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                java.awt.Image scaledImage = icon.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
                dropItemLabel.setIcon(new ImageIcon(scaledImage));
                dropItemLabel.setText("");
            } else {
                // Fallback to text if image not found
                dropItemLabel.setIcon(null);
                dropItemLabel.setText("New item: " + currentDroppedItem.getName());
                System.out.println("DROP ITEM- ERROR LOADING IMAGE PATH: " + imagePath);
            }
        } catch (Exception e) {
            // Fallback to text if error loading image
            dropItemLabel.setIcon(null);
            dropItemLabel.setText("New item: " + currentDroppedItem.getName());
            System.err.println("Error loading item image: " + e.getMessage());
        }

        collectButton.setEnabled(true);
        itemAvailable = true;

        System.out.println("Dropped item: " + currentDroppedItem.getName());

        // Update the appearance of the drop panel to highlight it
        dropPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(255, 215, 0), 2)); // Gold border
    }

    /**
     * Overrides the {@code addNotify} method to start the timer
     * and play the game theme music when the game panel is visible.
     */
    @Override
        public void addNotify() {
            super.addNotify();
            // Start UI update timer
            uiUpdateTimer.start();
            itemDropTimer.start();
            // Do initial UI update
            updatePetUI();
            // Start playing the game theme when the panel is shown
            model.SoundManager.getInstance().playGameTheme();

            setupKeyboardShortcuts();

            // Requests focus for keyboard shortcuts
            this.requestFocusInWindow();
        }

    /**
     * Overrides the {@code removeNotify} method to stop the timer
     * and game theme music when the panel is closed.
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        // Stop UI update timer
        uiUpdateTimer.stop();
        itemDropTimer.stop();
        // Stop animation timer
        if (animationTimer != null) {
            animationTimer.stop();
        }
        // Stop the game theme when leaving the panel
        model.SoundManager.getInstance().stopGameTheme();
    }
    
    /**
     * Updates the GUI. 
     * 
     * <p>Specifically, it updates the pet's name, progress bars,
     * mission count, pet state, enables the buttons depending on the pet's state, displays tool tips
     * for the buttons if they are disabled. It also handles the pet's death by displaying an
     * error message if the pet dies and returns to the start panel.</p>
     */
    private void updatePetUI() {
        if (game == null || game.getPlayer() == null || game.getPlayer().getPet() == null) return;

        // Update pet name
        Pet pet = game.getPlayer().getPet();
        petNameLabel.setText(pet.getName());

        // Update progress bars
        hungerProgressBar.setValue(pet.getHunger());
        hungerProgressBar.setString(pet.getHunger() + "%");

        happinessProgressBar.setValue(pet.getHappiness());
        happinessProgressBar.setString(pet.getHappiness() + "%");

        energyProgressBar.setValue(pet.getSleep());
        energyProgressBar.setString(pet.getSleep() + "%");

        healthProgressBar.setValue(pet.getHealth());
        healthProgressBar.setString(pet.getHealth() + "%");

        spaceReadinessProgressBar.setValue(pet.getSpaceReadiness());
        spaceReadinessProgressBar.setString(pet.getSpaceReadiness() + "%");

        // Update missions count with smaller font
        scoreLabel.setText("Score: " + pet.getTotalMission());
        scoreLabel.setPreferredSize(new Dimension(150, 30)); // Set fixed size for score label

        // Update pet state
        Pet.PetState currentState = pet.getCurrentState();
        if (currentState != lastPetState) {
            lastPetState = currentState;
            stateLabel.setText("State: " + currentState.toString());
            updatePetImage();
        }

        // Enable/disable buttons based on pet state
        boolean isAlive = pet.isAlive();
        boolean isSleeping = pet.isInState(Pet.PetState.SLEEPING);
        boolean isAngry = pet.isInState(Pet.PetState.ANGRY);

        // Update button states based on pet state
        feedButton.setEnabled(isAlive && !isSleeping && (!isAngry || pet.isInState(Pet.PetState.HUNGRY)));
        playButton.setEnabled(isAlive && !isSleeping && !game.isActionOnCooldown("play"));
        sleepButton.setEnabled(isAlive && !isSleeping && !isAngry);
        vetButton.setEnabled(isAlive && !isSleeping && !isAngry && !game.isActionOnCooldown("vet"));
        giftButton.setEnabled(isAlive && !isSleeping);
        spaceButton.setEnabled(isAlive && !isSleeping && !isAngry && pet.isSpaceReady());
        exerciseButton.setEnabled(isAlive && !isSleeping && !isAngry);
        
        // Feed button tooltip
        if (!isAlive) {
            feedButton.setToolTipText("Your pet is dead and can't be fed.");
        } else if (isSleeping) {
            feedButton.setToolTipText("Your pet is sleeping and can't be fed. Wait for it to wake up.");
        } else if (isAngry && !pet.isInState(Pet.PetState.HUNGRY)) {
            feedButton.setToolTipText("Your pet is angry and refuses food.");
        } else {
            feedButton.setToolTipText("Feed your pet to increase fullness.");
        }

        // Play button tooltip
        if (!isAlive) {
            playButton.setToolTipText("Your pet is dead and can't play.");
        } else if (isSleeping) {
            playButton.setToolTipText("Your pet is sleeping and can't play. Wait for it to wake up.");
        } else if (game.isActionOnCooldown("play")) {
            long remainingCooldown = game.getRemainingCooldown("play") / 1000;
            playButton.setToolTipText(String.format("On cooldown. Available in %d seconds.", remainingCooldown));
        } else {
            playButton.setToolTipText("Play with your pet to increase happiness.");
        }

        // Sleep button tooltip
        if (!isAlive) {
            sleepButton.setToolTipText("Your pet is dead and can't sleep.");
        } else if (isSleeping) {
            sleepButton.setToolTipText("Your pet is already sleeping.");
        } else if (isAngry) {
            sleepButton.setToolTipText("Your pet is too angry to sleep.");
        } else {
            sleepButton.setToolTipText("Send your pet to bed to restore energy.");
        }

        // Vet button tooltip
        if (!isAlive) {
            vetButton.setToolTipText("Your pet is dead and can't visit the vet.");
        } else if (isSleeping) {
            vetButton.setToolTipText("Your pet is sleeping and can't visit the vet. Wait for it to wake up.");
        } else if (isAngry) {
            vetButton.setToolTipText("Your pet is too angry to visit the vet.");
        } else if (game.isActionOnCooldown("vet")) {
            long remainingCooldown = game.getRemainingCooldown("vet") / 1000;
            vetButton.setToolTipText(String.format("On cooldown. Available in %d seconds.", remainingCooldown));
        } else {
            vetButton.setToolTipText("Take your pet to the vet to increase health.");
        }

        // Gift button tooltip
        if (!isAlive) {
            giftButton.setToolTipText("Your pet is dead and can't receive gifts.");
        } else if (isSleeping) {
            giftButton.setToolTipText("Your pet is sleeping and can't receive gifts. Wait for it to wake up.");
        } else {
            giftButton.setToolTipText("Give your pet a gift to increase happiness.");
        }

        // Exercise button tooltip
        if (!isAlive) {
            exerciseButton.setToolTipText("Your pet is dead and can't exercise.");
        } else if (isSleeping) {
            exerciseButton.setToolTipText("Your pet is sleeping and can't exercise. Wait for it to wake up.");
        } else if (isAngry) {
            exerciseButton.setToolTipText("Your pet is too angry to exercise.");
        } else {
            exerciseButton.setToolTipText("Exercise your pet to increase health and decrease sleepiness.");
        }

        // Space button tooltip
        if (!isAlive) {
            spaceButton.setToolTipText("Your pet is dead and can't go to space.");
            spaceButton.setBackground(new Color(200, 200, 200)); // Gray when disabled
        } else if (isSleeping) {
            spaceButton.setToolTipText("Your pet is sleeping and can't go to space. Wait for it to wake up.");
            spaceButton.setBackground(new Color(200, 200, 200)); // Gray when disabled
        } else if (isAngry) {
            spaceButton.setToolTipText("Your pet is too angry to go to space.");
            spaceButton.setBackground(new Color(200, 200, 200)); // Gray when disabled
        } else if (!pet.isSpaceReady()) {
            spaceButton.setToolTipText("Your pet is not ready for a space mission yet. Keep its stats high to build readiness.");
            spaceButton.setBackground(new Color(200, 200, 200)); // Gray when disabled
        } else {
            spaceButton.setToolTipText("Send your pet on a space mission!");
            spaceButton.setBackground(new Color(255, 215, 0)); // FFD700 (Gold) when enabled
        }
        spaceButton.setEnabled(isAlive && !isSleeping && !isAngry && pet.isSpaceReady());

        // Handle pet death
        if (!isAlive) {
            // Display a simple death message dialog
            JOptionPane.showMessageDialog(
                this,
                game.getPlayer().getPet().getName() + " has died!",
                "Game Over",
                JOptionPane.ERROR_MESSAGE
            );

            // Return to main menu
            game.clearCurrentPet();
            if (mainFrame != null) {
                mainFrame.showPanel("startPanel");
            } else {
                CardLayout card = (CardLayout) getParent().getLayout();
                card.show(getParent(), "startPanel");
            }
        }

        // Update colors based on values
        updateProgressBarColours();
    }

    /**
     * Updates progress bars by changing the colours of the progress bars depending on 
     * the value of each bar.
     */
    private void updateProgressBarColours() {
        // Health: Green (>60), Yellow (30-60), Red (<30)
        if (healthProgressBar.getValue() > 60) {
            healthProgressBar.setForeground(new Color(0, 204, 0)); // Green
            healthProgressBar.setString(healthProgressBar.getValue() + "%");
            healthProgressBar.setStringPainted(true);
        } else if (healthProgressBar.getValue() > 30) {
            healthProgressBar.setForeground(new Color(255, 204, 0)); // Yellow
            healthProgressBar.setString(healthProgressBar.getValue() + "%");
            healthProgressBar.setStringPainted(true);
        } else {
            healthProgressBar.setForeground(new Color(255, 0, 0)); // Red
            healthProgressBar.setString(healthProgressBar.getValue() + "%");
            healthProgressBar.setStringPainted(true);
        }

        // Hunger: Green (>60), Yellow (30-60), Red (<30)
        if (hungerProgressBar.getValue() > 60) {
            hungerProgressBar.setForeground(new Color(0, 204, 0)); // Green
            hungerProgressBar.setString(hungerProgressBar.getValue() + "%");
            hungerProgressBar.setStringPainted(true);
        } else if (hungerProgressBar.getValue() > 30) {
            hungerProgressBar.setForeground(new Color(255, 204, 0)); // Yellow
            hungerProgressBar.setString(hungerProgressBar.getValue() + "%");
            hungerProgressBar.setStringPainted(true);
        } else {
            hungerProgressBar.setForeground(new Color(255, 0, 0)); // Red
            hungerProgressBar.setString(hungerProgressBar.getValue() + "%");
            hungerProgressBar.setStringPainted(true);
        }

        // Happiness: Green (>60), Yellow (30-60), Red (<30)
        if (happinessProgressBar.getValue() > 60) {
            happinessProgressBar.setForeground(new Color(0, 204, 0)); // Green
            happinessProgressBar.setString(happinessProgressBar.getValue() + "%");
            happinessProgressBar.setStringPainted(true);
        } else if (happinessProgressBar.getValue() > 30) {
            happinessProgressBar.setForeground(new Color(255, 204, 0)); // Yellow
            happinessProgressBar.setString(happinessProgressBar.getValue() + "%");
            happinessProgressBar.setStringPainted(true);
        } else {
            happinessProgressBar.setForeground(new Color(255, 0, 0)); // Red
            happinessProgressBar.setString(happinessProgressBar.getValue() + "%");
            happinessProgressBar.setStringPainted(true);
        }

        // Energy: Green (>60), Yellow (30-60), Red (<30)
        if (energyProgressBar.getValue() > 60) {
            energyProgressBar.setForeground(new Color(0, 204, 0)); // Green
            energyProgressBar.setString(energyProgressBar.getValue() + "%");
            energyProgressBar.setStringPainted(true);
        } else if (energyProgressBar.getValue() > 30) {
            energyProgressBar.setForeground(new Color(255, 204, 0)); // Yellow
            energyProgressBar.setString(energyProgressBar.getValue() + "%");
            energyProgressBar.setStringPainted(true);
        } else {
            energyProgressBar.setForeground(new Color(255, 0, 0)); // Red
            energyProgressBar.setString(energyProgressBar.getValue() + "%");
            energyProgressBar.setStringPainted(true);
        }

        // Space Readiness: Blue
        spaceReadinessProgressBar.setForeground(new Color(52, 152, 219)); // Blue
        spaceReadinessProgressBar.setString(spaceReadinessProgressBar.getValue() + "%");
        spaceReadinessProgressBar.setStringPainted(true);
    }
    
    /**
     * Updates the image of the pet being displayed depending on the state
     * of the pet. Uses getResourse to load the image and display an error message if
     * it fails.
     */
    private void updatePetImage() {
        if (game == null || game.getPlayer() == null || game.getPlayer().getPet() == null) return;

        Pet pet = game.getPlayer().getPet();
        String petTypeName = pet.getClass().getSimpleName().toLowerCase();
        Pet.PetState state = pet.getCurrentState();

        // Use resource-based path instead of file-based path
        String imagePath = "/model/assets/animalSprites/" + petTypeName + "/";

        switch (state) {
            case NORMAL:
                imagePath += "normal.png";
                break;
            case SLEEPING:
                imagePath += "sleeping.png";
                break;
            case HUNGRY:
                imagePath += "hungry.png";
                break;
            case ANGRY:
                imagePath += "angry.png";
                break;
            case DEAD:
                imagePath += "dead.png";
                break;
            default:
                imagePath += "normal.png";
                break;
        }

        // Use getResource to load from classpath instead of direct file access
        try {
            java.net.URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                java.awt.Image scaledImage = icon.getImage().getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
                petImage = new ImageIcon(scaledImage);
                petImageLabel.setIcon(petImage);
                petImageLabel.setText("");
                
                // Reset animation when image changes
                animationOffset = 0;
                petImageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                
                // Start animation timer if not already running
                if (animationTimer != null && !animationTimer.isRunning()) {
                    animationTimer.start();
                }
            } else {
                System.err.println("Pet image not found: " + imagePath);
                petImageLabel.setIcon(null);
                petImageLabel.setText(petTypeName.substring(0, 1).toUpperCase() + petTypeName.substring(1) + " (Image not found)");
            }
        } catch (Exception e) {
            System.err.println("Error loading pet image: " + e.getMessage());
            petImageLabel.setIcon(null);
            petImageLabel.setText(petTypeName.substring(0, 1).toUpperCase() + petTypeName.substring(1) + " (Error loading image)");
        }
    }
    
    /**
     * Allows users to feed their pet. If there is food in their inventory, the
     * user can select food to feed to their pet.
     */
    private void feedPet() {
        if (game == null || game.getPlayer() == null || game.getPlayer().getPet() == null) return;

        // Get all food items from inventory
        java.util.ArrayList<model.item.Item> items = game.getPlayer().getInventory().getInventoryItems();
        java.util.ArrayList<model.item.Food> foodItems = new java.util.ArrayList<>();

        for (model.item.Item item : items) {
            if (item instanceof model.item.Food) {
                foodItems.add((model.item.Food) item);
            }
        }

        if (foodItems.isEmpty()) {
            showFeedbackMessage("You don't have any food items!");
            return;
        }

        // Create array of food names for the dialog
        String[] foodNames = new String[foodItems.size()];
        for (int i = 0; i < foodItems.size(); i++) {
            Food food = foodItems.get(i);
            foodNames[i] = food.getName() + " (+" + food.getFullnessValue() + " fullness)";
        }

        // Show selection dialog
        String selectedName = (String) JOptionPane.showInputDialog(
            this,
            "Choose food to feed your pet:",
            "Feed Pet",
            JOptionPane.QUESTION_MESSAGE,
            null,
            foodNames,
            foodNames[0]);

        // If user cancels, return
        if (selectedName == null) return;

        // Find the selected food item
        int selectedIndex = -1;
        for (int i = 0; i < foodNames.length; i++) {
            if (foodNames[i].equals(selectedName)) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex >= 0) {
            Food selectedFood = foodItems.get(selectedIndex);
            game.getPlayer().feedPet(selectedFood);
            showFeedbackMessage("You fed your pet " + selectedFood.getName() + "!");
        }
    }
    
    /**
     * Allows the user to play with their pet as long as enough time as passed
     * since the last time the pet played.
     */
    private void playWithPet() {
        if (game == null || game.getPlayer() == null || game.getPlayer().getPet() == null) return;

        if (game.isActionOnCooldown("play")) {
            long remainingSeconds = game.getRemainingCooldown("play") / 1000;
            showFeedbackMessage("You can't play with your pet right now. Try again in " + remainingSeconds + " seconds.");
            return;
        }

        game.getPlayer().playWithPet();
        game.setActionCooldown("play");
        showFeedbackMessage("Your pet had fun!");
    }

    /**
     * Allows the user to put their pet to bed.
     */
    private void putPetToBed() {
        if (game == null || game.getPlayer() == null || game.getPlayer().getPet() == null) return;
        game.getPlayer().sendToBed();
        showFeedbackMessage(game.getPlayer().getPet().getName() + " is now sleeping.");
    }

    /**
     * Allows the user give their pet a gift. 
     * 
     * <p>It ensures the user has toys in their
     * inventory and displays and error message if they do not. The used can choose a toy
     * and that top will be gified to the pet.</p>
     */
    private void giveGift() {
        if (game == null || game.getPlayer() == null || game.getPlayer().getPet() == null) return;

        // Get all toy items from inventory
        java.util.ArrayList<model.item.Item> items = game.getPlayer().getInventory().getInventoryItems();
        java.util.ArrayList<model.item.Toy> toyItems = new java.util.ArrayList<>();

        for (model.item.Item item : items) {
            if (item instanceof model.item.Toy) {
                toyItems.add((model.item.Toy) item);
            }
        }

        if (toyItems.isEmpty()) {
            showFeedbackMessage("You don't have any gifts!");
            return;
        }

        // Create array of toy names for the dialog
        String[] toyNames = new String[toyItems.size()];
        for (int i = 0; i < toyItems.size(); i++) {
            Toy toy = toyItems.get(i);
            toyNames[i] = toy.getName() + " (+" + toy.getFunValue() + " happiness)";
        }

        // Show selection dialog
        String selectedName = (String) JOptionPane.showInputDialog(
            this,
            "Choose a gift to give to your pet:",
            "Give Gift",
            JOptionPane.QUESTION_MESSAGE,
            null,
            toyNames,
            toyNames[0]);

        // If user cancels, return
        if (selectedName == null) return;

        // Find the selected toy item
        int selectedIndex = -1;
        for (int i = 0; i < toyNames.length; i++) {
            if (toyNames[i].equals(selectedName)) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex >= 0) {
            Toy selectedToy = toyItems.get(selectedIndex);
            game.getPlayer().giftToPet(selectedToy);
            showFeedbackMessage("You gave your pet a " + selectedToy.getName() + "!");
        }
    }

    /**
     * This method allows the user to take their pet to the vet as long as
     * enough time has passes since the last time the pet went to the vet.
     */
    private void takeToVet() {
        if (game == null || game.getPlayer() == null || game.getPlayer().getPet() == null) return;

        if (game.isActionOnCooldown("vet")) {
            long remainingSeconds = game.getRemainingCooldown("vet") / 1000;
            showFeedbackMessage("You can't take your pet to the vet right now. Try again in " + remainingSeconds + " seconds.");
            return;
        }

        game.getPlayer().takeToVet();
        game.setActionCooldown("vet");
        showFeedbackMessage(game.getPlayer().getPet().getName() + " went to the vet!");
    }

    /**
     * Sends the pet on a space mission if it is space ready. 
     * 
     * <p>A cut scene will be displayed by using the main frame to show the 
     * space mission panel. An error message will display if the pet is not
     * ready for a space mission.</p>
     */
    private void sendOnSpaceMission() {
        if (game == null || game.getPlayer() == null || game.getPlayer().getPet() == null) return;

        Pet pet = game.getPlayer().getPet();
        if (!pet.isSpaceReady()) {
            showFeedbackMessage(game.getPlayer().getPet().getName() + " is not ready for a space mission yet! Keep taking care of it and it will be ready soon.");
            return;
        }

        // First send the pet to space and update the game state
        game.getPlayer().sendToSpace();
        showFeedbackMessage(game.getPlayer().getPet().getName() + " is on a space mission!");
        
        // Then show the space mission cutscene
        if (mainFrame != null) {
            // Get the space mission panel and ensure it has the latest game state
            SpaceMissionPanel spacePanel = (SpaceMissionPanel)mainFrame.getPanel("spaceMissionPanel");
            if (spacePanel != null) {
                spacePanel.refreshUI(); // Force a UI update with the latest game state
                mainFrame.showPanel("spaceMissionPanel");
                spacePanel.startCutscene();
            }
        }
    }

    /**
     * Returns to the main menu by clearing the pet and navigating to the start panel.
     * 
     * <p>The navigation will be handled by the main frame if it exists or the parent componet's
     * card layout otherwise.</p>
     */
    private void returnToMenu() {
        // Save game state before returning to menu
        if (game != null) game.savePet();

        // Show confirmation dialog
        int response = JOptionPane.showConfirmDialog(
            this, 
            "Return to the main menu? Your progress has been saved.", 
            "Return to Menu", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            game.clearCurrentPet();   // Clear current Pet.java instance
            if (mainFrame != null) {
                mainFrame.showPanel("startPanel");
            } else {
                CardLayout card = (CardLayout) getParent().getLayout();
                card.show(getParent(), "startPanel");
            }
        }
    }

    /**
     * Displays the game settings by navigating to the settings panel.
     * 
     * <p>The navigation will be handled by the main frame if it exists or the parent componet's
     * card layout otherwise.</p>
     */
    private void openSettings() {
        if (mainFrame != null) {
            mainFrame.showPanel("settingsPanel");
        } else {
            CardLayout card = (CardLayout) getParent().getLayout();
            card.show(getParent(), "settingsPanel");
        }
    }

    /**
     * Displays a message to the user by making the label visible
     * and then removing the message after a few seconds.
     * 
     * @param message the message to be displayed to the user.
     */
    public void showFeedbackMessage(String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setVisible(true);
        feedbackLabel.setCaretPosition(0); // Scroll to top

        // Set up a timer to clear the message after a few seconds
        Timer clearTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                feedbackLabel.setText("");
                feedbackLabel.setVisible(false);
            }
        });
        clearTimer.setRepeats(false);
        clearTimer.start();
    }
    
    /**
     * Scales an image to fit on a button. The image is loaded, scaled
     * to the correct size, and displayed on the button.
     * 
     * @param button the button on which to display the image.
     * @param resourcePath the path of the image.
     * @param width desired width of the image.
     * @param height desired height of the image.
     */
    private void scaleButtonIcon(javax.swing.JButton button, String resourcePath, int width, int height) {
        try {
            java.net.URL imageUrl = getClass().getResource(resourcePath);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                java.awt.Image scaledImage = icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            System.err.println("Error scaling button icon: " + e.getMessage());
        }
    }
    
    /**
     * Displays the inventory. Creates a dialog and displays the user's inventory
     * items.
     */
    private void showInventoryDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Inventory");
        dialog.setModal(true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        // Create main panel with grid layout - reduced gaps between items
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 15, 15)); // Reduced from 30,30 to 15,15
        mainPanel.setBackground(new Color(44, 62, 80));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Reduced from 30 to 20

        // Load Minecraft font
        try {
            java.io.InputStream fontStream = getClass().getResourceAsStream("/model/assets/font/Minecraft.ttf");
            if (fontStream != null) {
                java.awt.Font minecraftFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontStream);
                java.awt.Font itemFont = minecraftFont.deriveFont(14f);

                // Get inventory items
                ArrayList<model.item.Item> items = game.getPlayer().getInventory().getInventoryItems();

                // Create a panel for each item
                for (model.item.Item item : items) {
                    JPanel itemPanel = new JPanel();
                    itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS)); // Changed to BoxLayout for better control
                    itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    itemPanel.setBackground(new Color(44, 62, 80));
                    itemPanel.setOpaque(false);

                    // Load and scale item image
                    String imagePath = "/model/assets/items/" + item.getName().toLowerCase() + ".png";
                    try {
                        java.net.URL imageUrl = getClass().getResource(imagePath);
                        if (imageUrl != null) {
                            ImageIcon icon = new ImageIcon(imageUrl);
                            Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Reduced from 50x50 to 40x40
                            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                            imageLabel.setHorizontalAlignment(JLabel.CENTER);
                            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Add spacing after image
                            itemPanel.add(imageLabel);
                        }
                    } catch (Exception e) {
                        System.err.println("Error loading item image: " + e.getMessage());
                    }

                    // Create text panel for name and description
                    JPanel textPanel = new JPanel();
                    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS)); // Changed to BoxLayout for better text alignment
                    textPanel.setBackground(new Color(44, 62, 80));
                    textPanel.setOpaque(false);

                    JLabel nameLabel = new JLabel(item.getName());
                    nameLabel.setForeground(Color.WHITE);
                    nameLabel.setFont(itemFont);
                    nameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

                    JLabel descLabel = new JLabel(item.getDescription());
                    descLabel.setForeground(new Color(200, 200, 200)); // Slightly dimmer color for description
                    descLabel.setFont(itemFont.deriveFont(12f)); // Slightly smaller font for description
                    descLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                    
                    textPanel.add(nameLabel);
                    textPanel.add(Box.createVerticalStrut(2)); // Minimal spacing between name and description
                    textPanel.add(descLabel);
                    
                    itemPanel.add(textPanel);
                    itemPanel.add(Box.createHorizontalGlue()); // Push content to the left
                    mainPanel.add(itemPanel);
                }

                if (items.isEmpty()) {
                    JPanel emptyPanel = new JPanel();
                    emptyPanel.setBackground(new Color(44, 62, 80));
                    JLabel emptyLabel = new JLabel("Inventory is empty");
                    emptyLabel.setForeground(Color.WHITE);
                    emptyLabel.setFont(itemFont);
                    emptyLabel.setHorizontalAlignment(JLabel.CENTER);
                    emptyPanel.add(emptyLabel);
                    mainPanel.add(emptyPanel);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading font: " + e.getMessage());
        }

        // Add scroll pane with custom styling
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBackground(new Color(44, 62, 80));
        scrollPane.getViewport().setBackground(new Color(44, 62, 80));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        dialog.add(scrollPane);

        dialog.setVisible(true);
    }

    /**
     * Set up proper keyboard shortcuts using InputMap and ActionMap
     * This allows keys to work without requiring Alt
     */
    private void setupKeyboardShortcuts() {
        if (getRootPane() == null) {
            System.out.println("getRootPane() is null. Keyboard shortcuts will be set up when panel is added to a window.");
            return;
        }
        // Make the panel focusable to receive key events
        this.setFocusable(true);
        this.requestFocusInWindow();

        // Map F key to Feed action
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "feed");
        getRootPane().getActionMap().put("feed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (feedButton.isEnabled()) {
                    feedButtonActionPerformed(null);
                    System.out.println("Feed shortcut triggered");
                }
            }
        });

        // Map P key to Play action
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "play");
        getRootPane().getActionMap().put("play", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playButton.isEnabled()) {
                    playButtonActionPerformed(null);
                    System.out.println("Play shortcut triggered");
                }
            }
        });

        // Map S key to Sleep action
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "sleep");
        getRootPane().getActionMap().put("sleep", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sleepButton.isEnabled()) {
                    sleepButtonActionPerformed(null);
                    System.out.println("Sleep shortcut triggered");
                }
            }
        });

        // Map G key to Gift action
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), "gift");
        getRootPane().getActionMap().put("gift", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (giftButton.isEnabled()) {
                    giftButtonActionPerformed(null);
                    System.out.println("Gift shortcut triggered");
                }
            }
        });

        // Map E key to Exercise action
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "exercise");
        getRootPane().getActionMap().put("exercise", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (exerciseButton.isEnabled()) {
                    exerciseButtonActionPerformed(null);
                    System.out.println("Exercise shortcut triggered");
                }
            }
        });

        // Map V key to Vet action
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_V, 0), "vet");
        getRootPane().getActionMap().put("vet", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vetButton.isEnabled()) {
                    vetButtonActionPerformed(null);
                    System.out.println("Vet shortcut triggered");
                }
            }
        });

        // Map R key to Space/Rocket action
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rocket");
        getRootPane().getActionMap().put("rocket", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (spaceButton.isEnabled()) {
                    spaceButtonActionPerformed(null);
                    System.out.println("Space mission shortcut triggered");
                }
            }
        });
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        northPanel = new javax.swing.JPanel();
        statsPanel = new javax.swing.JPanel();
        healthLabel = new javax.swing.JLabel();
        healthProgressBar = new javax.swing.JProgressBar();
        hungerLabel = new javax.swing.JLabel();
        hungerProgressBar = new javax.swing.JProgressBar();
        energyLabel = new javax.swing.JLabel();
        energyProgressBar = new javax.swing.JProgressBar();
        happinessLabel = new javax.swing.JLabel();
        happinessProgressBar = new javax.swing.JProgressBar();
        spaceReadinessLabel = new javax.swing.JLabel();
        spaceReadinessProgressBar = new javax.swing.JProgressBar();
        settingsButton = new javax.swing.JButton();
        actionsPanel = new javax.swing.JPanel();
        mainActionsPanel = new javax.swing.JPanel();
        feedButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        sleepButton = new javax.swing.JButton();
        exerciseButton = new javax.swing.JButton();
        giftButton = new javax.swing.JButton();
        vetButton = new javax.swing.JButton();
        spaceButton = new javax.swing.JButton();
        centerPanel = new javax.swing.JPanel();
        petImageLabel = new javax.swing.JLabel();
        petNameLabel = new javax.swing.JLabel();
        stateLabel = new javax.swing.JLabel();
        scoreLabel = new javax.swing.JLabel();
        feedbackLabel = new javax.swing.JTextArea();
        dropPanel = new javax.swing.JPanel();
        dropItemLabel = new javax.swing.JLabel();
        collectButton = new javax.swing.JButton();
        inventoryButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(222, 248, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 10));
        setPreferredSize(new java.awt.Dimension(1000, 800));
        setLayout(new java.awt.BorderLayout());

        northPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        northPanel.setOpaque(false);
        northPanel.setLayout(new java.awt.BorderLayout(20, 0)); // Add horizontal gap between components

        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new java.awt.Dimension(220, 120));
        statsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 2));

        // Create panels for each stat to control icon-bar spacing
        JPanel healthRow = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        JPanel hungerRow = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        JPanel energyRow = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        JPanel happinessRow = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        JPanel spaceRow = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        // Set fixed size for each row to ensure proper spacing
        Dimension rowSize = new java.awt.Dimension(220, 20);
        healthRow.setPreferredSize(rowSize);
        hungerRow.setPreferredSize(rowSize);
        energyRow.setPreferredSize(rowSize);
        happinessRow.setPreferredSize(rowSize);
        spaceRow.setPreferredSize(rowSize);

        // Make stat row panels transparent
        healthRow.setOpaque(false);
        hungerRow.setOpaque(false);
        energyRow.setOpaque(false);
        happinessRow.setOpaque(false);
        spaceRow.setOpaque(false);

        // Configure all labels to have same size
        Dimension labelSize = new java.awt.Dimension(24, 24);
        Dimension progressSize = new java.awt.Dimension(170, 12);

        healthLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/health.png"))); // NOI18N
        healthLabel.setPreferredSize(labelSize);
        healthProgressBar.setForeground(new java.awt.Color(27, 190, 88));
        healthProgressBar.setValue(100);
        healthProgressBar.setPreferredSize(progressSize);
        healthRow.add(healthLabel);
        healthRow.add(healthProgressBar);

        hungerLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/hunger.png"))); // NOI18N
        hungerLabel.setPreferredSize(labelSize);
        hungerProgressBar.setForeground(new java.awt.Color(27, 190, 88));
        hungerProgressBar.setValue(100);
        hungerProgressBar.setPreferredSize(progressSize);
        hungerRow.add(hungerLabel);
        hungerRow.add(hungerProgressBar);

        energyLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/energy.png"))); // NOI18N
        energyLabel.setPreferredSize(labelSize);
        energyProgressBar.setForeground(new java.awt.Color(27, 190, 88));
        energyProgressBar.setValue(100);
        energyProgressBar.setPreferredSize(progressSize);
        energyRow.add(energyLabel);
        energyRow.add(energyProgressBar);

        happinessLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/smile.png"))); // NOI18N
        happinessLabel.setPreferredSize(labelSize);
        happinessProgressBar.setForeground(new java.awt.Color(27, 190, 88));
        happinessProgressBar.setValue(100);
        happinessProgressBar.setPreferredSize(progressSize);
        happinessRow.add(happinessLabel);
        happinessRow.add(happinessProgressBar);

        spaceReadinessLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/rocketIcon.png"))); // NOI18N
        spaceReadinessLabel.setPreferredSize(labelSize);
        spaceReadinessProgressBar.setForeground(new java.awt.Color(27, 190, 88));
        spaceReadinessProgressBar.setValue(100);
        spaceReadinessProgressBar.setPreferredSize(progressSize);
        spaceRow.add(spaceReadinessLabel);
        spaceRow.add(spaceReadinessProgressBar);

        // Add all rows to stats panel
        statsPanel.add(healthRow);
        statsPanel.add(hungerRow);
        statsPanel.add(energyRow);
        statsPanel.add(happinessRow);
        statsPanel.add(spaceRow);

        // Create center panel for pet name with proper styling
        JPanel nameCenterPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        nameCenterPanel.setOpaque(false);
        petNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        petNameLabel.setPreferredSize(new java.awt.Dimension(200, 30));
        nameCenterPanel.add(petNameLabel);

        // Create a panel for the settings button to control its positioning
        JPanel settingsPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));
        settingsPanel.setOpaque(false);
        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/settings.png"))); // NOI18N
        settingsButton.setBorderPainted(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setMargin(new java.awt.Insets(0, 0, 0, 0)); // Remove button margins
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });
        settingsPanel.add(settingsButton);

        // Add components to north panel in correct order
        northPanel.add(statsPanel, java.awt.BorderLayout.WEST);
        northPanel.add(nameCenterPanel, java.awt.BorderLayout.CENTER);
        northPanel.add(settingsPanel, java.awt.BorderLayout.EAST);

        add(northPanel, java.awt.BorderLayout.NORTH);

        actionsPanel.setOpaque(false);
        actionsPanel.setLayout(new java.awt.BorderLayout(10, 40));

        mainActionsPanel.setOpaque(false);
        mainActionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 20));

        feedButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/feed.png"))); // NOI18N
        feedButton.setPreferredSize(new java.awt.Dimension(80, 80));
        feedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feedButtonActionPerformed(evt);
            }
        });
        mainActionsPanel.add(feedButton);

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/play.png"))); // NOI18N
        playButton.setPreferredSize(new java.awt.Dimension(80, 80));
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        mainActionsPanel.add(playButton);

        sleepButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/sleep.png"))); // NOI18N
        sleepButton.setPreferredSize(new java.awt.Dimension(80, 80));
        sleepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sleepButtonActionPerformed(evt);
            }
        });
        mainActionsPanel.add(sleepButton);

        exerciseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/exercise.png"))); // NOI18N
        exerciseButton.setPreferredSize(new java.awt.Dimension(80, 80));
        exerciseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exerciseButtonActionPerformed(evt);
            }
        });
        mainActionsPanel.add(exerciseButton);

        giftButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/gift.png"))); // NOI18N
        giftButton.setPreferredSize(new java.awt.Dimension(80, 80));
        giftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                giftButtonActionPerformed(evt);
            }
        });
        mainActionsPanel.add(giftButton);

        vetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/vet.png"))); // NOI18N
        vetButton.setPreferredSize(new java.awt.Dimension(80, 80));
        vetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vetButtonActionPerformed(evt);
            }
        });
        mainActionsPanel.add(vetButton);

        actionsPanel.add(mainActionsPanel, java.awt.BorderLayout.WEST);

        spaceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/model/assets/space.png"))); // NOI18N
        spaceButton.setPreferredSize(new java.awt.Dimension(80, 80));
        spaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spaceButtonActionPerformed(evt);
            }
        });
        actionsPanel.add(spaceButton, java.awt.BorderLayout.EAST);

        add(actionsPanel, java.awt.BorderLayout.SOUTH);

        centerPanel.setBackground(new java.awt.Color(222, 248, 255));
        centerPanel.setOpaque(false);

        petImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        petImageLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        petImageLabel.setPreferredSize(new java.awt.Dimension(300, 300));
        petImageLabel.setText("PET SPRITE HERE");
        petImageLabel.setFocusable(false);

        petNameLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        petNameLabel.setForeground(new java.awt.Color(204, 255, 255));
        petNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        petNameLabel.setText("Pet Name");

        stateLabel.setForeground(new java.awt.Color(0, 0, 0));
        stateLabel.setBackground(new java.awt.Color(255, 255, 255));
        stateLabel.setOpaque(true);
        stateLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        stateLabel.setText("State: Normal");

        scoreLabel.setForeground(new java.awt.Color(0, 0, 0));
        scoreLabel.setBackground(new java.awt.Color(255, 255, 255));
        scoreLabel.setOpaque(true);
        scoreLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        scoreLabel.setText("Score: 0");

        dropPanel.setLayout(new java.awt.BorderLayout());

        dropItemLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dropItemLabel.setText("ITEM");
        dropPanel.add(dropItemLabel, java.awt.BorderLayout.CENTER);

        collectButton.setText("Collect");
        collectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collectButtonActionPerformed(evt);
            }
        });
        dropPanel.add(collectButton, java.awt.BorderLayout.SOUTH);

        inventoryButton.setText("Inventory");
        inventoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout centerPanelLayout = new javax.swing.GroupLayout(centerPanel);
        centerPanel.setLayout(centerPanelLayout);
        centerPanelLayout.setHorizontalGroup(
            centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centerPanelLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inventoryButton, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                    .addComponent(dropPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40)
                .addGroup(centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(stateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scoreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(petImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(feedbackLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );
        centerPanelLayout.setVerticalGroup(
            centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centerPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(centerPanelLayout.createSequentialGroup()
                        .addGap(65, 65, 65)  // Center the feedback label vertically
                        .addComponent(feedbackLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(centerPanelLayout.createSequentialGroup()
                            .addComponent(dropPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)
                            .addComponent(inventoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(centerPanelLayout.createSequentialGroup()
                            .addComponent(petImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)
                            .addComponent(stateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(5, 5, 5)
                            .addComponent(scoreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        add(centerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>                        

    /**
     * Manages the action event for the feed button. 
     * 
     * <p>Plays a sound effect and calls the method to feed the pet.</p>
     * 
     * @param evt event action that triggers when the feed button is clicked.
     */
    private void feedButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        feedPet();
    }

    /**
     * Manages the action event for the play button. 
     * 
     * <p>Plays a sound effect and calls the method to play with the pet.</p>
     * 
     * @param evt event action that triggers when the play button is clicked.
     */
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        playWithPet();
    }

    /**
     * Manages the action event for the sleep button. 
     * 
     * <p>Plays a sound effect and calls the method to put the put to bed.</p>
     * 
     * @param evt event action that triggers when the sleep button is clicked.
     */
    private void sleepButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        putPetToBed();
    }

    /**
     * Manages the action event for the exercise button. 
     * 
     * <p>Plays a sound effect and calls the method to make the pet exercise.</p>
     * 
     * @param evt event action that triggers when the exercise button is clicked.
     */
    private void exerciseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        if (game == null || game.getPlayer() == null || game.getPlayer().getPet() == null) return;
    
        game.getPlayer().petExercise();
        showFeedbackMessage(game.getPlayer().getPet().getName() + " got some exercise!");
    }

    /**
     * Manages the action event for the gift button. 
     * 
     * <p>Plays a sound effect and calls the method to give a gift to the pet.</p>
     * 
     * @param evt event action that triggers when the gift button is clicked.
     */
    private void giftButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        giveGift();
    }

    /**
     * Manages the action event for the vet button. 
     * 
     * <p>Plays a sound effect and calls the method to take the pet to the vet.</p>
     * 
     * @param evt event action that triggers when the vet button is clicked.
     */
    private void vetButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        takeToVet();
    }

    /**
     * Manages the action event for the space button. 
     * 
     * <p>Plays a sound effect and calls the method to send the pet on a space mission.</p>
     * 
     * @param evt event action that triggers when the space button is clicked.
     */
    private void spaceButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        sendOnSpaceMission();
    }

    /**
     * Manages the action event for the settings button. 
     * 
     * <p>Plays a sound effect and calls the method to open the settings panel.</p>
     * 
     * @param evt event action that triggers when the settings button is clicked.
     */
    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playClickSound();
        openSettings();
    }

    /**
     * Manages the action event for the collect button. 
     * 
     * <p>Plays a sound effect and add the item to the user's inventory if it is not full. 
     * An error message will be displayed if the inventory is full.</p>
     * 
     * @param evt event action that triggers when the collect button is clicked.
     */
    private void collectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        model.SoundManager.getInstance().playCollectSound();
        if (itemAvailable && currentDroppedItem != null && game != null && game.getPlayer() != null) {
            // Add the item to the player's inventory
            boolean added = game.getPlayer().getInventory().addItem(currentDroppedItem);

            if (added) {
                showFeedbackMessage("You collected: " + currentDroppedItem.getName());

                // Reset drop area
                currentDroppedItem = null;
                itemAvailable = false;
                collectButton.setEnabled(false);
                dropItemLabel.setIcon(null);
                dropItemLabel.setText("");
                dropPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // Reset border
            } else {
                showFeedbackMessage("Inventory is full! Make room by using some items.");
            }
        }
    }

    /**
     * Manages the action event for the inventory button. 
     * 
     * <p>Displays the inventory on the screen.</p>
     * 
     * @param evt event action that triggers when the inventory button is clicked.
     */
    private void inventoryButtonActionPerformed(java.awt.event.ActionEvent evt) {
        showInventoryDialog();
    }

    /**
     * Sets up the animation timer for pet movement
     */
    private void setupAnimationTimer() {
        animationTimer = new Timer(ANIMATION_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Only animate if pet is not sleeping
                if (game != null && game.getPlayer() != null && game.getPlayer().getPet() != null) {
                    Pet pet = game.getPlayer().getPet();
                    if (pet.getCurrentState() != Pet.PetState.SLEEPING) {
                        // Update animation offset
                        if (movingRight) {
                            animationOffset++;
                            if (animationOffset >= MAX_OFFSET) {
                                movingRight = false;
                            }
                        } else {
                            animationOffset--;
                            if (animationOffset <= 0) {
                                movingRight = true;
                            }
                        }
                        
                        // Apply the offset to the pet image
                        if (petImage != null) {
                            petImageLabel.setBorder(BorderFactory.createEmptyBorder(0, animationOffset, 0, 0));
                        }
                    } else {
                        // Reset position when sleeping
                        animationOffset = 0;
                        petImageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                    }
                }
            }
        });
        animationTimer.start();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel actionsPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JButton collectButton;
    private javax.swing.JLabel dropItemLabel;
    private javax.swing.JPanel dropPanel;
    private javax.swing.JLabel energyLabel;
    private javax.swing.JProgressBar energyProgressBar;
    private javax.swing.JButton exerciseButton;
    private javax.swing.JButton feedButton;
    private javax.swing.JTextArea feedbackLabel;
    private javax.swing.JButton giftButton;
    private javax.swing.JLabel happinessLabel;
    private javax.swing.JProgressBar happinessProgressBar;
    private javax.swing.JLabel healthLabel;
    private javax.swing.JProgressBar healthProgressBar;
    private javax.swing.JLabel hungerLabel;
    private javax.swing.JProgressBar hungerProgressBar;
    private javax.swing.JButton inventoryButton;
    private javax.swing.JPanel mainActionsPanel;
    private javax.swing.JPanel northPanel;
    private javax.swing.JLabel petImageLabel;
    private javax.swing.JLabel petNameLabel;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel scoreLabel;
    private javax.swing.JButton settingsButton;
    private javax.swing.JButton sleepButton;
    private javax.swing.JButton spaceButton;
    private javax.swing.JLabel spaceReadinessLabel;
    private javax.swing.JProgressBar spaceReadinessProgressBar;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JButton vetButton;
    // End of variables declaration                   
}
