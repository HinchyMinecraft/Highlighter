// Package Declaration
package me.iffa.highlighter;

// Java Imports
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Bukkit Imports
import org.bukkit.Bukkit;
import org.bukkit.util.config.Configuration;

/**
 * Configuration file handler class.
 * 
 * @author Pandarr
 * @author iffa
 * 
 */
public class HighlighterConfig {
	// Variables
	public static final Map<String, Object> CONFIG_DEFAULTS = new HashMap<String, Object>();
	public static Configuration myConfig;
	static {
		CONFIG_DEFAULTS.put("highlight.soundurl",
				"http://saxxyspin.com/default_highlighter_sound.wav");
		CONFIG_DEFAULTS.put("highlight.playvoice", true);
	}

	/**
	 * Loads the configuration file
	 */
	public static void loadConfig() {
		File configFile = new File(Bukkit.getServer().getPluginManager()
				.getPlugin("Highlighter").getDataFolder(), "config.yml");
		if (configFile.exists()) {
			myConfig = new Configuration(configFile);
			myConfig.load();
			myConfig.setHeader("# Configuration file of Highlighter\r\n# Author: iffa");
			for (String prop : CONFIG_DEFAULTS.keySet()) {
				if (myConfig.getProperty(prop) == null) {
					myConfig.setProperty(prop, CONFIG_DEFAULTS.get(prop));
				}
			}
		} else {
			try {
				Bukkit.getServer().getPluginManager().getPlugin("Highlighter")
						.getDataFolder().mkdir();
				configFile.createNewFile();
				myConfig = new Configuration(configFile);
				myConfig.setHeader("# Configuration file of Highlighter\r\n# Author: iffa");
				for (String prop : CONFIG_DEFAULTS.keySet()) {
					myConfig.setProperty(prop, CONFIG_DEFAULTS.get(prop));
				}
				myConfig.save();
				Highlighter.log.info(Highlighter.prefix
						+ " Created default configuration file.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
