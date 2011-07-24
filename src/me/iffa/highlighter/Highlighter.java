package me.iffa.highlighter;

// Java Imports
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
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
	// General Variables
	public static String prefix = "[Highlighter]";
	public static String version = "0.2";
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
		// Downloading BukkitContrib if it isn't found.
		final PluginManager pm = getServer().getPluginManager();
		if (pm.getPlugin("BukkitContrib") == null) {
			try {
				download(log, new URL("http://bit.ly/autoupdateBukkitContrib"),
						new File("plugins/BukkitContrib.jar"));
				pm.loadPlugin(new File("plugins" + File.separator
						+ "BukkitContrib.jar"));
				pm.enablePlugin(pm.getPlugin("BukkitContrib"));
			} catch (final Exception ex) {
				log.warning(prefix
						+ " Failed to install BukkitContrib, you may have to restart your server or install it manually.");
			}
		}
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
	 * Downloads BukkitContrib (or any other file from the internet)
	 * 
	 * @param log
	 *            Logger
	 * @param url
	 *            URL of the file
	 * @param file
	 *            The download location
	 * @throws IOException
	 */
	public static void download(Logger log, URL url, File file)
			throws IOException {
		if (!file.getParentFile().exists())
			file.getParentFile().mkdir();
		if (file.exists())
			file.delete();
		file.createNewFile();
		final int size = url.openConnection().getContentLength();
		log.info(prefix + " Downloading " + file.getName() + " (" + size / 1024
				+ "kb) ...");
		final InputStream in = url.openStream();
		final OutputStream out = new BufferedOutputStream(new FileOutputStream(
				file));
		final byte[] buffer = new byte[1024];
		int len, downloaded = 0, msgs = 0;
		final long start = System.currentTimeMillis();
		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
			downloaded += len;
			if ((int) ((System.currentTimeMillis() - start) / 500) > msgs) {
				log.info((int) ((double) downloaded / (double) size * 100d)
						+ "%");
				msgs++;
			}
		}
		in.close();
		out.close();
		log.info(prefix
				+ " Download finished. Don't forget to tell your players that in order to receive alerts they need BukkitContrib installed!");
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
