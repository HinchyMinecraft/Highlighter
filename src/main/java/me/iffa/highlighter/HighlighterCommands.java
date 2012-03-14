// Package Declaration
package me.iffa.highlighter;

// Java Imports
import java.util.List;

// Bukkit Imports
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the 'highlighter'-command.
 * 
 * @author iffa
 * 
 */
public class HighlighterCommands implements CommandExecutor {

	private HighlighterPlayerManager plrmgr;

    public HighlighterCommands(HighlighterPlayerManager man) {
        plrmgr = man;
    }

	/**
	 * Called when the 'highlighter'-command is used.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (hasPermission("highlighter.commands", player)) {
				if (args.length == 0) {
					player.sendMessage(ChatColor.GREEN + Highlighter.prefix
							+ " Usage:");
					sender.sendMessage(ChatColor.GREEN
							+ "/highlighter add abcdef - add a new highlight");
					sender.sendMessage(ChatColor.GREEN
							+ "/highlighter remove abcdef - remove a highlight");
					sender.sendMessage(ChatColor.GREEN
							+ "/highlighter list - list your highlights");
					return true;

				} else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
					if (plrmgr.getConfig().getStringList(
							player.getName()).isEmpty()) {
						player.sendMessage(ChatColor.RED + Highlighter.prefix
								+ " You have no highlights! (please make one!)");
						return true;
					}
					player.sendMessage(ChatColor.GREEN + Highlighter.prefix
							+ " Your highlights:");
					List<String> highlights = plrmgr.getConfig()
							.getStringList(player.getName());
					String[] array = highlights.toArray(new String[highlights
							.size()]);
					for (String string : array) {
						player.sendMessage(ChatColor.DARK_GREEN + "-" + string);
					}
					return true;

				} else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
					if (plrmgr.getConfig().getStringList(
							player.getName()).contains(args[1])) {
						player.sendMessage(ChatColor.RED + Highlighter.prefix
								+ " The highlight already exists!");
						return true;
					}
					List<String> newList = plrmgr.getConfig()
							.getStringList(player.getName());
					newList.add(args[1]);
					plrmgr.getConfig().set(
							player.getName(), newList);
					plrmgr.saveConfig();
					player.sendMessage(ChatColor.GREEN + Highlighter.prefix
							+ " Added '" + args[1] + "' to highlights!");

					return true;

				} else if (args.length == 2
						&& args[0].equalsIgnoreCase("remove")) {
					if (!plrmgr.getConfig().getStringList(
							player.getName()).contains(args[1])) {
						sender.sendMessage(ChatColor.RED + Highlighter.prefix
								+ " The highlight doesn't exist!");
						return true;
					}
					List<String> newList = plrmgr.getConfig()
							.getStringList(player.getName());
					newList.remove(args[1]);
					plrmgr.getConfig().set(
							player.getName(), newList);
					plrmgr.saveConfig();
					player.sendMessage(ChatColor.GREEN + Highlighter.prefix
							+ " Removed '" + args[1] + "' from highlights!");

					return true;
				}

				sender.sendMessage(ChatColor.GREEN + Highlighter.prefix
						+ " Usage:");
				sender.sendMessage(ChatColor.GREEN
						+ "/highlighter add abcdef - add a new highlight");
				sender.sendMessage(ChatColor.GREEN
						+ "/highlighter remove abcdef - remove a highlight");
				sender.sendMessage(ChatColor.GREEN
						+ "/highlighter list - list your highlights");
				return true;

			} else {
				sender.sendMessage(ChatColor.RED + Highlighter.prefix
						+ " You don't have the permission to use this command!");
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + Highlighter.prefix
				+ " You must be a player to use this command!");
		return true;
	}

	/**
	 * Checks if a player has the given permission node.
	 * 
	 * @param name
	 *            Permission node
	 * @param player
	 *            Player
	 * @return true if the player has the permision node
	 */
	private boolean hasPermission(String name, Player player) {
		/*if (Bukkit.getServer().getPluginManager().getPlugin("Permissions") != null) {
			if (Highlighter.permissionHandler.has(player, name)
					|| player.isOp()) {
				return true;
			}
			return false;
		}*/
		if (player.hasPermission(name) || player.isOp()) {
			return true;
		}
		return false;
	}

}
