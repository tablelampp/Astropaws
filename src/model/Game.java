package model;

import model.pet.*;
import model.pet.Pet.PetState;
import model.item.*;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

/**
 * This class represents a game. It keeps track of the player, timer, play stats. It provides
 * functionality for staring a new game, saving a game, loading a game, and parental options.
 */
public class Game {
   
    private Player player;
    private boolean isRunning;
    private long lastUpdateTime;	// might need for future, no use now
    private Timer gameTimer;
    private Map<String, Long> cooldowns;	// Command name -> timestamp when available again
    
    // Constants for directories and file operations
    private static final String DATA_DIR = "data";
    private static final String PETS_DIR = DATA_DIR + File.separator + "pets";
    private static final String PARENT_CONFIG_FILE = DATA_DIR + File.separator + "parent_config.properties";
    
    // Cooldown values in milliseconds
    private static final long VET_COOLDOWN = 180000;	// 3 mins in ms
    private static final long PLAY_COOLDOWN = 90000;	// 1.5 mins in ms
    
    // Game timing constant
    private static final int UPDATE_INTERVAL = 30000;	// Update pet stats every 30 seconds
    
    // Time tracking for parental controls -- not priority right now
    private long sessionStartTime;
    private long totalPlayTime;
    private long lastSessionTime;
    private int sessionCount;
    private boolean timeRestrictionEnabled;
    private int startHour;	//0-23 hour format
    private int endHour;	//0-23 hour format
    private String parentPassword;
    
    /**
     * Constructs new Game instance.
     */
    public Game() {
    	player = new Player(null);
    	
    	cooldowns = new HashMap<>();
    	isRunning = false;
    	createDirectories();
    	loadParentalSettings();
    	
    	gameTimer = new Timer();
    	sessionStartTime = System.currentTimeMillis();
    	
    	cooldowns.put("vet", 0L);
    	cooldowns.put("play", 0L);
    }
    
	/**
	 * Create directories to store files for game data and pets and add a new file 
	 * to each directory.
	 */
    private void createDirectories() {
    	File dataDir = new File(DATA_DIR);
    	File petsDir = new File(PETS_DIR);
    	
    	if (!dataDir.exists()) {
    		dataDir.mkdir();
    	}
    	
    	if (!petsDir.exists()) {
    		petsDir.mkdir();
    	}
    }
    
	/**
	 * Start the game by setting isRunning to true and starting the timer. 
	 */
    public void startGame() {
    	if (isRunning) return;
    	
    	isRunning = true;
    	lastUpdateTime = System.currentTimeMillis();	// might need for future, no use currently
    	
    	// Create a new timer if the previous one was cancelled
    	if (gameTimer == null || gameTimer.purge() == -1) {
    		gameTimer = new Timer();
    	}
    	
    	gameTimer.scheduleAtFixedRate(new TimerTask() {
    		@Override
    		public void run() {
    			updateGame();
    		}
    	}, UPDATE_INTERVAL, UPDATE_INTERVAL);
    }
    
	/**
	 * Stop the game if it is running by stopping the timer and setting
	 * isRunning to false. Then update the play time variables to include
	 * the time played in this session.
	 */
    public void stopGame() {
    	if (!isRunning) return;
    	
    	gameTimer.cancel();
    	isRunning = false;
    	
    	long sessionTime = System.currentTimeMillis() - sessionStartTime;
    	totalPlayTime += sessionTime;
    	lastSessionTime = sessionTime;
    	sessionCount++;
    	
    	saveParentalSettings();
    }
    
	/**
	 * Updates the game by updating and saving the pet's progress bars and state.
	 */
    private void updateGame() {
    	if (player.getPet() == null) return;
    	
    	player.getPet().updateProgressBars();
		player.getPet().updateState();
		savePet();
    }
    
	/**
	 * Creates a new pet of the specifed type with the specified name.
	 * 
	 * @param petName name to assign to the pet.
	 * @param petType type to assign to the pet.
	 * @return true if the pet was successfull created and false otherwise.
	 */
    public boolean createNewPet(String petName, String petType) {
    	if (petName == null || petName.isEmpty() || petType == null || petType.isEmpty()) return false;
    	
    	Pet newPet = null;
    	
    	switch (petType.toLowerCase()) {
    	case "dog":
    		newPet = new Dog(petName);
    		break;
    	case "fish":
    		newPet = new Fish(petName);
    		break;
    	case "monkey":
    		newPet = new Monkey(petName);
    		break;
    	case "mouse":
    		newPet = new Mouse(petName);
    		break;
    	default:
    		return false;
    	}
    	
    	newPet.setID(generateNewPetID());
    	player.setPet(newPet);
    	player.setInventory(new Inventory());
    	addStarterItems();
    	
    	return savePet();
    }
    
	/**
	 * Add some basic food and toy items to the player's inventory to get them started.
	 */
	private void addStarterItems() {
		Inventory inventory = player.getInventory();
		
		// Add starter food items
		inventory.addItem(new Food("Kibble", "Basic pet food. Made of space plants."));
		inventory.addItem(new Food("Apple", "A fresh apple. Very nutritious."));
		inventory.addItem(new Food("Cheese", "A piece of cheese. Pets love it."));
		
		// Add starter toys
		inventory.addItem(new Toy("Ball", "Looks like the moon."));
		inventory.addItem(new Toy("Rocket", "A toy rocket. Makes space noises."));
	}
    
	/**
	 * Generates a new pet ID by finding the highest current pet ID and returning
	 * the next available integer.
	 * 
	 * @return the newly generated pet ID.
	 */
    private int generateNewPetID() {
    	List<Integer> existingIDs = getExistingPetIDs();
    	
    	int maxID = 0;
    	for (Integer id : existingIDs) {
    		if (id > maxID) {
    			maxID = id;
    		}
    	}
    	
    	return maxID + 1;
    }
    
	/**
	 * Searches the pets files to get all the existing pet IDs.
	 * 
	 * @return a list of existing pet IDs.
	 */
    public List<Integer> getExistingPetIDs() {
    	List<Integer> petIDs = new ArrayList<>();
    	File petsFolder = new File(PETS_DIR);
    	
    	if (petsFolder.exists() && petsFolder.isDirectory()) {
    		File[] files = petsFolder.listFiles((dir, name) -> name.endsWith(".save"));
    		
    		if (files != null) {
	    		for (File file : files) {
	    			String fileName = file.getName();
	    			try {
	    				int petID = Integer.parseInt(fileName.substring(0, fileName.lastIndexOf(".")));
	    				petIDs.add(petID);
	    			} catch (NumberFormatException e) {
	    				// Skip files with invalid names
	    			}
	    		}
    		}
    	}
    	
    	return petIDs;
    }
    
	/**
	* Gets a mapping of pet IDs to pet names for display in load game UI -- used in UI
	* to show available save slots/pets
	*
	* @return a mapping of pet IDs to pet names.
	*/
    public Map<Integer, String> getAvailablePets() {
    	Map<Integer, String> pets = new HashMap<>();
    	List<Integer> petIDs = getExistingPetIDs();
    	
    	for (Integer id : petIDs) {
    		try {
    			String filename = PETS_DIR + File.separator + id + ".save";
    			File saveFile = new File(filename);
    			
    			try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
    				String line;
    				String petName = null;
    				
    				while ((line = reader.readLine()) != null) {
    					if (line.startsWith("PET_NAME=")) {
    						petName = line.substring(9);
    						break;
    					}
    				}
    				
    				if (petName != null) pets.put(id,  petName);
    				
    			}
    		} catch (IOException e) {
				System.err.println("Error reading pet save file: " + e.getMessage());
			}
    	}
    	
    	return pets;
    }

	/**
	 * Gets a mapping of only alive pet IDs to pet names for display in load game UI
	 * 
	 * @return a mapping of the living pet IDs to pet names.
	 */
	public Map<Integer, String> getAlivePets() {
		Map<Integer, String> pets = new HashMap<>();
		List<Integer> petIDs = getExistingPetIDs();
		
		for (Integer id : petIDs) {
			try {
				String filename = PETS_DIR + File.separator + id + ".save";
				File saveFile = new File(filename);
				
				try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
					String line;
					String petName = null;
					boolean isDead = false;
					
					while ((line = reader.readLine()) != null) {
						if (line.startsWith("PET_NAME=")) {
							petName = line.substring(9);
						} else if (line.startsWith("STATE=")) {
							// Check if the pet's state is DEAD
							isDead = line.substring(6).equals("DEAD");
						}
						
						// If we found both the name and state, we can stop reading
						if (petName != null && line.startsWith("STATE=")) {
							break;
						}
					}
					
					// Only add the pet to the map if it has a name and is not dead
					if (petName != null && !isDead) {
						pets.put(id, petName);
					}
				}
			} catch (IOException e) {
				System.err.println("Error reading pet save file: " + e.getMessage());
			}
		}
		
		return pets;
	}

	/**
	 * Gets a mapping of only dead pet IDs to pet names for display
	 * 
	 * @return a mapping of dead pet IDs to pet names.
	 */
	public Map<Integer, String> getDeadPets() {
		Map<Integer, String> pets = new HashMap<>();
		List<Integer> petIDs = getExistingPetIDs();
		
		for (Integer id : petIDs) {
			try {
				String filename = PETS_DIR + File.separator + id + ".save";
				File saveFile = new File(filename);
				
				try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
					String line;
					String petName = null;
					boolean isDead = false;
					
					while ((line = reader.readLine()) != null) {
						if (line.startsWith("PET_NAME=")) {
							petName = line.substring(9);
						} else if (line.startsWith("STATE=")) {
							// Check if the pet's state is DEAD
							isDead = line.substring(6).equals("DEAD");
						}
						
						// If we found both the name and state, we can stop reading
						if (petName != null && line.startsWith("STATE=")) {
							break;
						}
					}
					
					// Only add the pet to the map if it has a name and IS dead
					if (petName != null && isDead) {
						pets.put(id, petName);
					}
				}
			} catch (IOException e) {
				System.err.println("Error reading pet save file: " + e.getMessage());
			}
		}
		
		return pets;
	}
    
    /**
	 * Saves current pet's state and inventory to a file.
	 * 
	 * @return true if the pet was saved successfully and false otherwise.
	 */
    public boolean savePet() {
    	try {
    		Pet currentPet = player.getPet();
    		if (currentPet == null) return false;
    		
    		int petID = currentPet.getID();
    		if (petID <= 0) return false;
    		
    		String filename = PETS_DIR + File.separator + petID + ".save";
    		File saveFile = new File(filename);
    		
    		saveFile.getParentFile().mkdirs();
    		
    		try (PrintWriter writer = new PrintWriter(new FileWriter(saveFile))) {
                writer.println("PET_TYPE=" + currentPet.getClass().getSimpleName());
                writer.println("PET_ID=" + petID);
                writer.println("PET_NAME=" + currentPet.getName());
                writer.println("HEALTH=" + currentPet.getHealth());
                writer.println("SLEEP=" + currentPet.getSleep());
                writer.println("HUNGER=" + currentPet.getHunger());
                writer.println("HAPPINESS=" + currentPet.getHappiness());
                writer.println("SPACE_READINESS=" + currentPet.getSpaceReadiness());
                writer.println("STATE=" + currentPet.getCurrentState());
                writer.println("TOTAL_MISSIONS=" + currentPet.getTotalMission());
                
                ArrayList<Item> items = player.getInventory().getInventoryItems();
                writer.println("INVENTORY_COUNT=" + items.size());
                
                for (int i = 0; i < items.size(); i++) {
                	Item item = items.get(i);
                	writer.println("ITEM_"+ i + "_TYPE=" + item.getClass().getSimpleName());		// FIGURE OUT WHERE "getSimpleName" comes from...
                	writer.println("ITEM_" + i + "_NAME=" + item.getName());
                	writer.println("ITEM_" + i + "_DESC=" + item.getDescription());
                }
    		}
    		
    		return true;
    	} catch (IOException e) {
    		System.out.println("Error saving pet: " + e.getMessage());
    		return false;
    	}
    }
    
	/**
	 * Loads the pet's data from the save file and initializes  the pet and inventory.
	 * 
	 * <p>This method reads the pet's attributes, state, and inventory items from  a save file based
	 *  on the given pet ID. It checks if the pet can be loaded based on the time restrictions. The loading
	 * fails if the pet type is not recognized or the file cannot be found.</p>
	 * 
	 * @param petID the unique identifier of the pet to be loaded
	 * @return true if the pet was successfull loaded and false otherwise.
	 */
   public boolean loadPet(int petID) {
	   try {
		   String filename = PETS_DIR + File.separator + petID + ".save";
		   File saveFile = new File(filename);
		   
		   if(!saveFile.exists()) {
				System.out.println("Save file doesn't exist");
			   return false;
		   }
		   
		   if (timeRestrictionEnabled && !isWithinAllowedTime()) {
			   System.out.println("Cannot play at this time due to parental restrictions");
			   return false;
		   }
		   
		   Map<String, String> properties = new HashMap<>();
		   
		   try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
			   String line;
			   while ((line = reader.readLine()) != null) {
				   String[] parts = line.split("=", 2);
				   if (parts.length == 2) {
					   properties.put(parts[0], parts[1]);
				   }
			   }
		   }
		   
		   String petType = properties.get("PET_TYPE");
		   String petName = properties.get("PET_NAME");
		   
		   if (petType == null || petName == null) return false;
		   
		   Pet loadedPet = null;
		   
		   
		   switch (petType.toLowerCase()) {
		   		case "dog":
		   			loadedPet = new Dog(petName);
		   			break;
		   		case "fish":
		   			loadedPet = new Fish(petName);
		   			break;
		   		case "monkey":
		   			loadedPet = new Monkey(petName);
		   			break;
		   		case "mouse":
		   			loadedPet = new Mouse(petName);
		   			break;
		   		default:
		   			return false;
		   }
		   
	        loadedPet.setID(Integer.parseInt(properties.get("PET_ID")));
	        loadedPet.setHealth(Integer.parseInt(properties.get("HEALTH")));
	        loadedPet.setSleep(Integer.parseInt(properties.get("SLEEP")));
	        loadedPet.setHunger(Integer.parseInt(properties.get("HUNGER")));
	        loadedPet.setHappiness(Integer.parseInt(properties.get("HAPPINESS")));
			loadedPet.setSpaceReadiness(Integer.parseInt(properties.get("SPACE_READINESS")));
			loadedPet.setCurrentState(PetState.valueOf(properties.get("STATE")));
			loadedPet.setTotalMissions(Integer.parseInt(properties.get("TOTAL_MISSIONS")));

	        
	        Inventory loadedInventory = new Inventory();
	        
	        int inventoryCount = Integer.parseInt(properties.getOrDefault("INVENTORY_COUNT", "0"));
	        
	        for (int i = 0; i < inventoryCount; i++) {
	        	String itemType = properties.get("ITEM_" + i + "_TYPE");
	        	String itemName = properties.get("ITEM_" + i + "_NAME");
	        	String itemDesc = properties.get("ITEM_" + i + "_DESC");
	        	
	        	if (itemType != null && itemName != null && itemDesc != null) {
	        		Item item = null;
	        		
	        		if (itemType.equals("Food")) item = new Food(itemName, itemDesc);	// figure out where itemType comes from. is it just from the save file? it's not a private Item.java variable.
	        		else if (itemType.equals("Toy")) item = new Toy(itemName, itemDesc);
	        		
	        		if (item != null) loadedInventory.addItem(item);
	        	}
	        }
	        
	        player.setPet(loadedPet);
	        player.setInventory(loadedInventory);
	        
	        return true;
	   } catch (Exception e) {
		   System.err.println("Error loading pet: " + e.getMessage());
		   return false;
	   }
   }
   
   /**
	* Revives the pet by resting all its stats.
	*
	* <p>If the pet cannot be loaded or the pet is null,
	* the revival fails. If the pet is revived, the timer
	* is started.</p>
	*
	* @param petID
	* @return true if the pet was revived successfully and false otherwise.
    */
	public boolean revivePet(int petID) {
		if (!loadPet(petID)) {
			System.out.println("Failed to find pet ID to revive");
			return false;
		}
		
		Pet pet = player.getPet();
		if (pet == null) {
			System.out.println("Pet is null");
			return false;
		}
		
		// Reset all stats to full
		pet.setHealth(100);
		pet.setHappiness(100);
		pet.setHunger(100);
		pet.setSleep(100);
		pet.setSpaceReadiness(0);  // Reset space readiness
		pet.setCurrentState(PetState.NORMAL);
		
		// Save the revived pet state
		if (!savePet()) {
			System.out.println("Failed to save revived pet");
			return false;
		}
		
		// Start the game timer for the revived pet
		startGame();
		
		return true;
	}
   
	/**
	 * Checks if the player is within the allowed play time if restrictions are enabled.
	 * 
	 * @return true if the player is within their allowed play time and false otherwise.
	 */
   public boolean isWithinAllowedTime() {
	   if (!timeRestrictionEnabled) return true;
	   
	   int currentHour = LocalDateTime.now().getHour();
	   
	   if (startHour <= endHour) {
		   return currentHour >= startHour && currentHour < endHour;
	   } else {
		   return currentHour >= startHour || currentHour < endHour;
	   }
   }
   
   /**
	* Loads the parental contol settings from the file.

	*<p>If the file exists, this method reads the settings and initializes  
 	* time restrictions, session tracking, and the parental password. If the 
	* file is not found or another error occurs, deafult values are used.</p>
    */
   private void loadParentalSettings() {
	   Properties props = new Properties();
	   File file = new File(PARENT_CONFIG_FILE);
	   
	   if (file.exists()) {
		   try (FileInputStream in = new FileInputStream(file)) {
			   props.load(in);
			   
			   // parse properties from file
			   timeRestrictionEnabled = Boolean.parseBoolean(props.getProperty("timeRestrictionEnabled", "false"));
	           startHour = Integer.parseInt(props.getProperty("startHour", "8"));
	           endHour = Integer.parseInt(props.getProperty("endHour", "20"));
	           totalPlayTime = Long.parseLong(props.getProperty("totalPlayTime", "0"));
	           lastSessionTime = Long.parseLong(props.getProperty("lastSessionTime", "0"));
	           sessionCount = Integer.parseInt(props.getProperty("sessionCount", "0"));
	           parentPassword = props.getProperty("parentPassword", "0000");	// Default password
	            
		   } catch (IOException e) {
			   System.err.println("Error loading parental settings: " + e.getMessage());
			   
			   timeRestrictionEnabled = false;
			   startHour = 8;
			   endHour = 20;
			   totalPlayTime = 0;
			   lastSessionTime = 0;
			   sessionCount = 0;
			   parentPassword = "0000";		// Default password
		   }
	   } else {
		   // Set defaults
		   timeRestrictionEnabled = false;
		   startHour = 8;
		   endHour = 20;
		   totalPlayTime = 0;
		   lastSessionTime = 0;
		   sessionCount = 0;
		   parentPassword = "0000";
		   
		   saveParentalSettings();
	   }
   }
   
   /**
	* Saves parental settings to a save file.
    */
   private void saveParentalSettings() {
	   Properties props = new Properties();
	   
	    
	   props.setProperty("timeRestrictionEnabled", Boolean.toString(timeRestrictionEnabled));
	   props.setProperty("startHour", Integer.toString(startHour));
	   props.setProperty("endHour", Integer.toString(endHour));
	   props.setProperty("totalPlayTime", Long.toString(totalPlayTime));
	   props.setProperty("lastSessionTime", Long.toString(lastSessionTime));
	   props.setProperty("sessionCount", Integer.toString(sessionCount));
	   props.setProperty("parentPassword", parentPassword);
	   
	   try (FileOutputStream out = new FileOutputStream(PARENT_CONFIG_FILE)) {
		   props.store(out, "Parental Settings");
	   } catch (IOException e) {
		   System.err.print("Error saving parental settings: " + e.getMessage());
	   }
   }
   
   /**
	* Updates the parental controls based on the specified values.
	*
	* @param enabled boolean value representing whether parental controls are enabled.
	* @param start the start hour for allowed play time.
	* @param end the end hour for allowed play time.
	* @param password the parental control password.
    */
   public void updateParentalControls(boolean enabled, int start, int end, String password) {
	   timeRestrictionEnabled = enabled;
	   
	   if (start >= 0 && start < 24) startHour = start;
	   if (end >= 0 && end < 24) endHour = end;
	   if (password != null && !password.isEmpty()) parentPassword = password;
	   
	   saveParentalSettings();
   }
   
   /**
	* Resets the play time statistics. 
	*
	* <p>Each play time variable (totalPlayTime, lastSessionTime, sessionCount)
	* will be set to 0.</p>
    */
   public void resetPlayTimeStats() {
	   totalPlayTime = 0;
	   lastSessionTime = 0;
	   sessionCount = 0;
	   
	   saveParentalSettings();
   }
   
   /**
	* Check if the specified password is equal to the parental control password.
	*
	* @param password the password to be checked against the parental control password.
	* @return true if the specified password is not null and is equal to the parental control password
	* and false otherwise.
    */
   public boolean verifyParentPassword(String password) {
	   return parentPassword != null && parentPassword.equals(password);
   }
   
   /**
	* Check if the specified action is on cooldown or if it can be preformed.
   	*
	* @param action the name of the action to check.
	* @return true is the action is on cooldown and false otherwise.
    */
   public boolean isActionOnCooldown(String action) {
	   Long cooldownTime = cooldowns.get(action);
	   if (cooldownTime == null) return false;
	   
	   return System.currentTimeMillis() < cooldownTime;
   }
   
   /**
	* Gets the remaining cooldown time of the specifed action.
	*
	* @param action the action to check the remaining cooldown for.
	* @return the remaining cooldown time in milliseconds, or 0 if action
	* is not on cooldown.
    */
   public long getRemainingCooldown(String action) {
	   Long cooldownTime = cooldowns.get(action);
	   if (cooldownTime == null) return 0;
	   
	   long remaining = cooldownTime - System.currentTimeMillis();
	   return Math.max(0, remaining);
   }
   
   /**
	* Sets a cooldown time for the specifed action.
	*
	* @param action the name of the action to set the cooldown for.
    */
   public void setActionCooldown(String action) {
	   long cooldownAmount = 0;
	   
	   switch (action) {
	   		case "vet":
	   			cooldownAmount = VET_COOLDOWN;
	   			break;
	   		case "play":
	   			cooldownAmount = PLAY_COOLDOWN;
	   			break;
	   }
	   
	   if (cooldownAmount > 0) cooldowns.put(action, System.currentTimeMillis() + cooldownAmount);
   }

   /**
	* Clears the current pet assigned to the player.
    */
   public void clearCurrentPet() {
		// First ensure pet state is saved
		savePet();
		
		// Then clear the pet reference
		if (player != null) {
			player.setPet(null);
		}
	}
   
   /**
	* Gets the player.
	*
	* @return the player.
    */
   public Player getPlayer() {
	   return player;
   }
   
   /**
	* Gets the player's total play time.
	*
	* @return the player's total play time.
    */
   public long getTotalPlayTime() {
	   return totalPlayTime;
   }
   
   /**
	* Get the player's last session time.
	*
	* @return the player's last session time.
    */
   public long getLastSessionTime() {
	   return lastSessionTime;
   }
   
   /**
	* Calculates the player's average session time. 
	*
	* @return the player's average session time. 
    */
   public long getAverageSessionTime() {
	   if (sessionCount == 0) return 0;
	   return totalPlayTime / sessionCount;
   }
   
   /**
	* Checks whether time restrictions are enabled.
	*
	* @return true if time restrictions are enables and false otherwise.
    */
   public boolean isTimeRestrictionEnabled() {
	   return timeRestrictionEnabled;
   }
   
   /**
	* Gets the player's start hour.
	*
	* @return the player's start hour.
    */
   public int getStartHour() {
	   return startHour;
   }
   
   /**
	* Gets the player's end hour.
	*
	* @return the player's end hour.
    */
   public int getEndHour() {
	   return endHour;
   }
    
}
