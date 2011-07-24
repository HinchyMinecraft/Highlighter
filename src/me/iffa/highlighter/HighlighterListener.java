package me.iffa.highlighter;

// Bukkit Imports
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

// BukkitContrib Imports
import org.bukkitcontrib.BukkitContrib;
import org.bukkitcontrib.player.ContribCraftPlayer;
import org.bukkitcontrib.player.ContribPlayer;
import org.bukkitcontrib.sound.SoundManager;

/**
 * PlayerListener that handles highlighting.
 * 
 * @author iffa
 * 
 */
public class HighlighterListener extends PlayerListener {
	// Variables
	public Highlighter plugin;

	public HighlighterListener(Highlighter instance) {
		plugin = instance;
	}

	/**
	 * Called when a player chats.
	 */
	public void onPlayerChat(PlayerChatEvent event) {
		SoundManager soundManager = BukkitContrib.getSoundManager();
		for (Player player : event.getRecipients()) {
			ContribPlayer cPlayer = ContribCraftPlayer.getContribPlayer(player);
			List<String> highlights = HighlighterPlayerManager.myConfig
					.getStringList(player.getName(), null);
			String[] array = highlights.toArray(new String[highlights.size()]);
			for (String string : array) {
				if (event.getMessage().contains(string)) {
					soundManager.playCustomSoundEffect(null, cPlayer,
							HighlighterConfig.myConfig
									.getString("highlight.soundurl"), true);
					if (HighlighterConfig.myConfig.getBoolean(
							"highlight.playvoice", true)) {
						soundManager.playCustomSoundEffect(plugin, cPlayer,
								"http://saxxyspin.com/highlighter_voice.wav",
								true);
					}
				}
			}
		}
	}

	/**
	 * Called when a player joins the game.
	 */
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Add default highlight if the highlight is not found for the player.
		if (HighlighterPlayerManager.myConfig.getProperty(event.getPlayer()
				.getName()) == null) {
			List<String> newEntry = new ArrayList<String>();
			newEntry.add(event.getPlayer().getName());
			HighlighterPlayerManager.myConfig.setProperty(event.getPlayer()
					.getName(), newEntry);
			HighlighterPlayerManager.myConfig.save();
		}
	}
}
