// Package Declaration
package me.iffa.highlighter;

// Java Imports
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

// Bukkit Imports
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Player highlight file handler class.
 * 
 * @author iffa
 * 
 */
public class HighlighterPlayerManager {
	// Variables	
	private static FileConfiguration myConfig;
	private static File myConfigFile;
	private static File myDataFolder;
	private static Highlighter plugin;
	
	public HighlighterPlayerManager(Highlighter instance) {
		plugin = instance;
	}

	/**
	 * Loads the highlight file
	 * 
	 * @author Pandarr
	 */
	public static void loadConfig() {
		if (myConfigFile == null) {
			myConfigFile = new File(plugin.getDataFolder(), "highlights.yml");
		}
		myConfig = YamlConfiguration.loadConfiguration(myConfigFile);
 
		// Look for defaults in the jar
		InputStream defConfigStream = plugin.getResource("highlights.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			myConfig.setDefaults(defConfig);
		}
	}

	public static FileConfiguration getConfig() {
		if (myConfig == null) {
			loadConfig();
		}
		return myConfig;
	}


	/**
	 * Saves the highlight file
	 * 
	 * @author Hinchy
	 */
	public static void saveConfig() {
		if (myConfig == null || myConfigFile == null) {
			return;
		}
		try {
			myConfig.save(myConfigFile);
		} catch (IOException ex) {
			plugin.log.severe("Could not save config to " + myConfigFile);
		}
	}
}
