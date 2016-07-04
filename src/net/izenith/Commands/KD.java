package net.izenith.Commands;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class KD implements HubCommand {

	static int pageSize = 8;

	@Override
	public String getName() {
		return "kd";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		try {
			if (args[0].equalsIgnoreCase("reset")) {
				Player bPlayer = Bukkit.getPlayer(args[1]);
				if (bPlayer != null) {
					IPlayer iPlayer = IPlayerHandler.getPlayer(bPlayer);
					iPlayer.setKills(0);
					iPlayer.setDeaths(0);
				} else {
					FileConfiguration player = Util.getOfflinePlayerFile(args[1]);
					player.set("kills", 0);
					player.set("deaths", 0);
				}
			} else {
				Player bPlayer = Bukkit.getPlayer(args[0]);
				if (bPlayer != null) {
					IPlayer iPlayer = IPlayerHandler.getPlayer(bPlayer);
					Double kills = iPlayer.getKills();
					Double deaths = iPlayer.getDeaths();
					String kd = deaths == 0 ? kills.toString() : new DecimalFormat("#.##").format(kills / deaths);
					sender.sendMessage(Util.parseColors("&b" + bPlayer.getName() + "'s &7K/D: &a" + kills.intValue() + "&7/&c" + deaths.intValue() + " &7= &b" + kd));
				} else {
					FileConfiguration player = Util.getOfflinePlayerFile(args[0]);
					Double kills = player.getDouble("kills");
					Double deaths = player.getDouble("deaths");
					String kd = deaths == 0 ? kills.toString() : new DecimalFormat("#.##").format(kills / deaths);
					sender.sendMessage(Util.parseColors("&b" + player.getString("last_name") + "'s &7K/D: &a" + kills.intValue() + "&7/&c" + deaths.intValue() + " &7= &b" + kd));
				}
			}
		} catch (Exception e) {
			List<KDRatio> kds;
			kds = new ArrayList<KDRatio>();
			File dataFolder = new File(Util.getMain().getDataFolder().getPath(), "players");
			for (File pFile : dataFolder.listFiles()) {
				YamlConfiguration pConfig = YamlConfiguration.loadConfiguration(pFile);
				KDRatio kd = new KDRatio(pConfig.getString("last_name"), pConfig.getInt("kills"), pConfig.getInt("deaths"));

				if (kds.size() == 0) {
					kds.add(kd);
				} else {
					boolean insterted = false;
					for (int i = 0; i < kds.size(); i++) {
						KDRatio currKD = kds.get(i);
						if (kd.kd >= currKD.kd) {
							kds.add(i, kd);
							insterted = true;
							break;
						}
					}
					if (!insterted)
						kds.add(kds.size(), kd);
				}
			}

			try {
				int page = -1;
				int pages = (int) Math.floor((kds.size() - 1) / pageSize) + 1;
				if (args.length == 0) {
					page = 0;
				} else {
					page = Integer.parseInt(args[0]) - 1;
				}

				int pageIndex = page * pageSize;

				sender.sendMessage(Util.parseColors("&7K/D page &a" + (page + 1) + "&7/&a" + pages));
				for (int i = pageIndex; i < pageIndex + pageSize && i < kds.size(); i++) {
					KDRatio kd = kds.get(i);
					sender.sendMessage(Util.parseColors("&7" + (i + 1) + ". &b" + kd.name + "&7: &a" + kd.kills.intValue() + "&7/&c" + kd.deaths.intValue() + " &7= &b" + kd.ratio));
				}
				sender.sendMessage(Util.parseColors("&7Use &a/kd <page> &7to view other pages."));
			} catch (NumberFormatException ex) {
				sender.sendMessage(ChatColor.RED + "Could not find player!");
			}
		}
	}

	@Override
	public boolean onlyPlayers() {
		return false;
	}

	@Override
	public boolean hasPermission() {
		return false;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	class KDRatio {

		public String name;
		public Double kills;
		public Double deaths;
		public double kd;
		public String ratio;

		protected KDRatio(String name, double kills, double deaths) {
			this.name = name;
			this.kills = kills;
			this.deaths = deaths;
			if (deaths == 0)
				this.kd = kills;
			else
				this.kd = kills / deaths;
			DecimalFormat df = new DecimalFormat("#.##");
			this.ratio = df.format(this.kd);
		}

	}

}
