package net.izenith.Commands;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class PlayTime implements HubCommand {

	@Override
	public String getName() {
		return "playtime";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "play", "givemearimjobandbitemyear" };
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Long time = null;
		String name = null;
		IPlayer iPlayer = null;
		String shortTime = null;
		if (args.length > 0) {
			if (args.length > 2 && args[0].equalsIgnoreCase("set")) {
				if (sender.hasPermission("playtime.set")) {
					try {
						Player player = Bukkit.getPlayer(args[1]);
						if (player == null) {
							for (File file : new File(Util.getMain().getDataFolder().getPath() + System.getProperty("file.separator") + "players").listFiles()) {
								YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
								if (config.contains("last_name") && config.getString("last_name") != null) {
									if (config.getString("last_name").toLowerCase().equals(args[1].toLowerCase())) {
										Long newTime = Long.parseLong(args[2]);
										config.set("time", newTime * 60 * 60 * 1000);
										try {
											config.save(file);
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
							}
						} else {
							iPlayer = IPlayerHandler.getPlayer(player);
							Double newTime = Double.parseDouble(args[2]);
							Long longTime = new Double(newTime * 60 * 60 * 1000).longValue();
							iPlayer.setOnlineTime(longTime);
						}
						sender.sendMessage(ChatColor.BLUE + "Set " + ChatColor.GREEN + (player == null ? args[1] : player.getName()) + "'s" + ChatColor.BLUE + " playtime to " + ChatColor.GREEN + args[2]);
					} catch (Exception e) {
						sender.sendMessage(ChatColor.RED + "Invalid time");
					}
					return;
				} else {
					sender.sendMessage(ChatColor.RED + "You do not have permission!");
				}
			} else if (args[0].equalsIgnoreCase("top")) {
				long top = 0l;
				String topName = "";
				Util.setAllOnlineTimes();
				for (File file : new File(Util.getMain().getDataFolder().getPath() + System.getProperty("file.separator") + "players").listFiles()) {
					YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
					if (config.contains("last_name") && config.getString("last_name") != null) {
						Long currentTime = config.getLong("time");
						if (currentTime > top) {
							top = currentTime;
							topName = config.getString("last_name");
						}
					}
				}
				Double timeHours = new Double(top) / (1000 * 60 * 60);
				DecimalFormat df = new DecimalFormat("#.##");
				String formatedTop = df.format(timeHours);
				sender.sendMessage(ChatColor.BLUE + "The player who has played most is " + ChatColor.GREEN + topName + ChatColor.BLUE + " who has played for " + ChatColor.GREEN + formatedTop);
				return;
			} else {
				Player player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					for (File file : new File(Util.getMain().getDataFolder().getPath() + System.getProperty("file.separator") + "players").listFiles()) {
						YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
						if (config.contains("last_name") && config.getString("last_name") != null) {
							if (config.getString("last_name").toLowerCase().equals(args[0].toLowerCase())) {
								time = config.getLong("time");
								name = args[0];
								Double timeHours = new Double(time) / (1000 * 60 * 60);
								DecimalFormat df = new DecimalFormat("#.##");
								shortTime = df.format(timeHours);
								break;
							}
						}
					}
				} else {
					iPlayer = IPlayerHandler.getPlayer(player);
					time = iPlayer.getOnlineTime();
					name = player.getName();
				}
			}
		} else {
			iPlayer = new IPlayer((Player) sender);
			System.out.println(iPlayer);
			time = iPlayer.getOnlineTime();
			name = sender.getName();
		}
		if (time == null) {
			sender.sendMessage(ChatColor.RED + "Player not found.");
			return;
		}
		if (shortTime == null)
			shortTime = iPlayer.getOnlineTimeHours();
		sender.sendMessage(ChatColor.BLUE + (name + " has played for " + ChatColor.GREEN + shortTime + " hours"));
	}

	@Override
	public boolean onlyPlayers() {
		return true;
	}

	@Override
	public boolean hasPermission() {
		return false;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

}
