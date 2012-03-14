// Package Declaration
package me.iffa.highlighter;

// Java Imports
import java.util.ArrayList;
import java.util.List;

// Bukkit Imports
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// Spout Imports
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.sound.SoundManager;

/**
 * PlayerListener that handles highlighting.
 * 
 * @author iffa
 * 
 */
public class HighlighterListener implements Listener {
	// Variables
	private Highlighter plugin;

	// Constructor
	public HighlighterListener(Highlighter instance) {
		plugin = instance;
	}

	/**
	 * Called when a player chats.
	 * 
	 * @param event
	 *            Event data
	 */
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		SoundManager soundManager = SpoutManager.getSoundManager();
		for (Player player : event.getRecipients()) {
			SpoutPlayer sPlayer = (SpoutPlayer) player;
			List<String> highlights = HighlighterPlayerManager.getConfig().getStringList(player.getName());
			String[] array = highlights.toArray(new String[highlights.size()]);
			for (String string : array) {
				if (event.getMessage().contains(string)) {
					soundManager.playCustomSoundEffect(null, sPlayer, plugin.getConfig().getString("highlight.soundurl"), true);
					if (plugin.getConfig().getBoolean("highlight.playvoice", true)) {
						soundManager.playCustomSoundEffect(plugin, sPlayer, "http://saxxyspin.com/highlighter_voice.wav", true);
					}
				}
			}
		}
	}

	/**
	 * Called when a player joins the game.
	 * 
	 * @param event
	 *            Event data
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Add default highlight if the highlight is not found for the player.
		if (!HighlighterPlayerManager.getConfig().isSet(event.getPlayer().getName())) {
			List<String> newEntry = new ArrayList<String>();
			newEntry.add(event.getPlayer().getName());
			HighlighterPlayerManager.getConfig().set(event.getPlayer()
					.getName(), newEntry);
			HighlighterPlayerManager.saveConfig();
		}
	}
}
