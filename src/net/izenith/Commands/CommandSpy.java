package net.izenith.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

import net.izenith.CommandSpy.CommandFilter;
import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Main;
import net.izenith.Main.Util;
import net.izenith.Main.Vars;

public class CommandSpy implements HubCommand, Listener {

	// See HubCommand for formatting

	@Override
	public String getName() {
		return "commandspy";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "commandpsy", "cs" };
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		try {
			IPlayer iPlayer = IPlayerHandler.getPlayer((Player) sender);
			switch (args[0]) {
			// Turn command spy off
			case "off":
				// Removes player from the list of commandspy players
				Vars.commandSpy.remove((Player) sender);
				// Chage and save commandspy in config
				iPlayer.setCommandSpy(null);
				// Notify player
				sender.sendMessage(ChatColor.GREEN + "CommandSpy turrned off!");
				break;
			// Create a filter
			case "create":
				// Create a list that will later hold values for accepted commands
				List<String> list = new ArrayList<String>();
				// Get filter list if it already exists
				List<String> temp = Vars.main.getConfig().getStringList("commandspy.filters." + args[2]);
				if (temp != null)
					list = temp;
				// Save blank filter in config
				Vars.main.getConfig().set("commandspy.filters." + args[1], list);
				Vars.main.saveConfig();
				// Notify player
				sender.sendMessage(ChatColor.GREEN + "Created filter " + args[1]);
				break;
			// List all filters
			case "list":
				// Get list of filters from config
				Set<String> filters = Util.getConfig().getConfigurationSection("commandspy.filters").getKeys(false);
				// Send filters to player
				sender.sendMessage(ChatColor.BLUE + "Filters:");
				for (String filter : filters) {
					sender.sendMessage(ChatColor.GREEN + filter);
				}
				break;
			default:
				// Check if filter exists
				if (Vars.main.getConfig().getStringList("commandspy.filters." + args[0]) != null && args.length > 1) {
						switch (args[1]) {
						case "add":
							String[] a = { "r", "p", "c", "a" };
							String s = args[2].substring(0, 1);
							boolean b = false;
							for (String str : a) {
								if (str.equals(s)) {
									b = true;
								}
							}
							if (!b) {
								help((Player) sender);
								return;
							}
							List<String> accept = Vars.main.getConfig().getStringList("commandspy.filters." + args[0]);
							accept.add(s + " " + args[3]);
							Vars.main.getConfig().set("commandspy.filters." + args[0], accept);
							Vars.main.saveConfig();
							Vars.commandSpy.clear();
							loadFilters();
							sender.sendMessage(ChatColor.GREEN + "Added to filter!");
							break;
						case "remove":
							String[] a1 = { "r", "p", "c", "a" };
							String s1 = args[2].substring(0, 1);
							boolean b1 = false;
							for (String str : a1) {
								if (str.equals(s1)) {
									b = true;
								}
							}
							if (!b1) {
								help((Player) sender);
								return;
							}
							List<String> accept1 = Vars.main.getConfig().getStringList("commandspy.filters." + args[0]);
							accept1.remove(s1 + " " + args[3]);
							Vars.main.getConfig().set("commandspy.filters." + args[0], accept1);
							Vars.main.saveConfig();
							Vars.commandSpy.clear();
							loadFilters();
							sender.sendMessage(ChatColor.GREEN + "Removed from filter!");
							break;
						case "list":
							List<String> accept2 = Vars.main.getConfig().getStringList("commandspy.filters." + args[0]);
							for (String str : accept2) {
								String pref = ChatColor.GREEN + "";
								switch (str.substring(0, 1)) {
								case "r":
									pref += "Rank " + ChatColor.BLACK + ": ";
									break;
								case "p":
									pref += "Player " + ChatColor.BLACK + ": ";
									break;
								case "c":
									pref += "Command " + ChatColor.BLACK + ": ";
									break;
								case "a":
									pref += "Arugment " + ChatColor.BLACK + ": ";
									break;
								}
								sender.sendMessage(pref + ChatColor.WHITE + str.substring(1, str.length()));
							}
							break;
						}
					} else {
						List<String> accept = Vars.main.getConfig().getStringList("commandspy.filters." + args[0]);
						if (Vars.commandSpy.containsKey((Player) sender)) {
							Vars.commandSpy.remove((Player) sender);
						}
						Vars.commandSpy.put((Player) sender, new CommandFilter(accept));
						iPlayer.setCommandSpy(args[0]);
						sender.sendMessage(ChatColor.GREEN + "Switched filter to " + args[0]);
					}
				break;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			help(sender);
			return;
		}
	}

	@Override
	public boolean onlyPlayers() {
		return true;
	}

	@Override
	public boolean hasPermission() {
		return true;
	}

	@Override
	public Permission getPermission() {
		return new Permission("commandspy.use");
	}

	public String[] subArray(String[] array, int start, int end) {
		String[] ret = new String[array.length - (end - start)];
		for (int i = start; i < end; i++) {
			int n = i - start;
			ret[n] = array[i];
		}
		return ret;
	}

	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN + "Commands:");
		sender.sendMessage(ChatColor.BLUE + "/commandspy all");
		sender.sendMessage(ChatColor.BLUE + "/commandspy off");
		sender.sendMessage(ChatColor.BLUE + "/commandspy default");
		sender.sendMessage(ChatColor.BLUE + "/commandspy <filter>");
		sender.sendMessage(ChatColor.BLUE + "/commandspy <filter> add rank|player|command|argument <value>");
		sender.sendMessage(ChatColor.BLUE + "/commandspy <filter> remove rank|player|command|argument <value>");
		sender.sendMessage(ChatColor.BLUE + "/commandspy <filter> list");
		sender.sendMessage(ChatColor.BLUE + "/commandspy create <name> [template filter]");
		sender.sendMessage(ChatColor.BLUE + "/commandspy list");
	}

	public static void setupFilter(Player player) {
		IPlayer iPlayer = IPlayerHandler.getPlayer(player);
		Main main = Vars.main;
		FileConfiguration config = main.getConfig();
		String filterName = iPlayer.getCommandSpy();
		System.out.println(filterName);
		if (filterName != null) {
			List<String> list = config.getStringList("commandspy.filters." + filterName);
			if (list != null) {
				Vars.commandSpy.put(player, new CommandFilter(list));
			}
		}
	}

	public static void loadFilters() {
		for (Player p : Vars.main.getServer().getOnlinePlayers()) {
			CommandSpy.setupFilter(p);
		}
	}

}
