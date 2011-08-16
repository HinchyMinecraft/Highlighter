// Package Declaration
package me.iffa.highlighter;

// Java Imports
import java.util.logging.Logger;

// Bukkit Imports
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

//Permissions (Nijiko)
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

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
	public static PermissionHandler permissionHandler;

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
		// Loading configuration & highlight files.
		HighlighterConfig.loadConfig();
		HighlighterPlayerManager.loadConfig();
		// Registering events.
		pm.registerEvent(Event.Type.PLAYER_CHAT, new HighlighterListener(this),
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, new HighlighterListener(this),
				Event.Priority.Normal, this);
		// Setting the commandexecutor for 'highlighter'.
		cmdExec = new HighlighterCommands();
		getCommand("highlighter").setExecutor(cmdExec);
		// Setting up Permissions (Nijiko)
		setupPermissions();
		log.info(prefix + " Enabled version " + version);
	}

	/**
	 * Sets up Permissions (Nijiko)
	 */
	private void setupPermissions() {
		if (permissionHandler != null) {
			return;
		}

		Plugin permissionsPlugin = this.getServer().getPluginManager()
				.getPlugin("Permissions");

		if (permissionsPlugin == null) {
			log.info(prefix
					+ " Permission system not detected, defaulting to OP");
			return;
		}

		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		log.info(prefix
				+ "Found and will use plugin "
				+ ((Permissions) permissionsPlugin).getDescription()
						.getFullName());
	}

}
