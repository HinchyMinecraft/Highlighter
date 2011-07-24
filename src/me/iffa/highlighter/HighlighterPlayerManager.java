package me.iffa.highlighter;

// Java Imports
import java.io.File;
import java.io.IOException;

// Bukkit Imports
import org.bukkit.Bukkit;
import org.bukkit.util.config.Configuration;

/**
 * Player highlight file handler class.
 * 
 * @author iffa
 * 
 */
public class HighlighterPlayerManager {
	// Variables
	public static Configuration myConfig;

	/**
	 * Loads the highlight file
	 * 
	 * @author Pandarr
	 */
	public static void loadConfig() {
		File configFile = new File(Bukkit.getServer().getPluginManager()
				.getPlugin("Highlighter").getDataFolder(), "highlights.yml");
		if (configFile.exists()) {
			myConfig = new Configuration(configFile);
			myConfig.load();
			myConfig.setHeader("# Highlights file of Highlighter\r\n# Author: iffa");
		} else {
			try {
				Bukkit.getServer().getPluginManager().getPlugin("Highlighter")
						.getDataFolder().mkdir();
				configFile.createNewFile();
				myConfig = new Configuration(configFile);
				myConfig.setHeader("# Highlights file of Highlighter\r\n# Author: iffa");
				myConfig.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
