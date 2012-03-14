// Package Declaration
package me.iffa.highlighter;

// Java Imports
import java.util.logging.Logger;

// Bukkit Imports
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of Highlighter, a (Craft)Bukkit plugin that uses BukkitContrib to
 * highlight like in IRC. :)
 * 
 * @author iffa
 * 
 */
public class Highlighter extends JavaPlugin {
	// Variables
	public static String prefix = "[Highlighter]";
	public static String version = "0.3";
	public static Logger log;
	private HighlighterCommands cmdExec = null;
	public HighlighterPlayerManager plrmgr = new HighlighterPlayerManager(this);

	/**
	 * Called when the plugin is disabled.
	 */
	@Override
	public void onDisable() {
		log.info(prefix + " Disabled version " + version);
	}

	/**
	 * Called when the plugin is enabled.
	 */
	@Override
	public void onEnable() {		
		log = getServer().getLogger();
		final PluginManager pm = getServer().getPluginManager();
		
		// load configuration
		
		this.getConfig();
		
		if (this.getConfig().isSet("highlight") == false) {
			this.saveDefaultConfig();
			log.info("[Highlighter] Config did not exist or was invalid, default config saved.");
		}
		
		// load highlight files.
		plrmgr.loadConfig();
		
		// Registering events.
        getServer().getPluginManager().registerEvents(new HighlighterListener(this), this);
		// Setting the commandexecutor for 'highlighter'.
		cmdExec = new HighlighterCommands(plrmgr);
		getCommand("highlighter").setExecutor(cmdExec);
		log.info(prefix + " Enabled version " + version);
	}

}
