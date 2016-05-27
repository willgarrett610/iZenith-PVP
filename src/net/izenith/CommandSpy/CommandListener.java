package net.izenith.CommandSpy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.izenith.Main.Util;
import net.izenith.Main.Vars;

public class CommandListener implements Listener {

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		for (Player p : Vars.main.getServer().getOnlinePlayers()) {
			CommandFilter filter = Vars.commandSpy.get(p);
			if (filter != null) {
				if (filter.canPass(e)) {
					p.sendMessage(ChatColor.RED + e.getPlayer().getName() + ChatColor.GRAY + " performed the command: " + ChatColor.GREEN + e.getMessage());
				}
			}
		}

		if (Util.startsWithIgnoreCase(e.getMessage(), "/suicide")) {
			e.setCancelled(true);
			e.getPlayer().setHealth(0);
			Bukkit.getServer().broadcastMessage(ChatColor.BLUE + e.getPlayer().getName() + ChatColor.DARK_PURPLE + " took the easy way out!");
		}

		if (Util.startsWithIgnoreCase(e.getMessage(), "/reload")){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "This command is currently disabled.");
		}

		if (Util.startsWithIgnoreCase(e.getMessage(), "/plots ") || Util.startsWithIgnoreCase(e.getMessage(), "/cr")) {
			e.setCancelled(true);
			e.getPlayer().performCommand("warp plots");
		}

		if (Util.startsWithIgnoreCase(e.getMessage(), "/info")) {
			e.setCancelled(true);
			Bukkit.dispatchCommand(e.getPlayer(), "warp info");
		}
		
		if (Util.startsWithIgnoreCase(e.getMessage(), "/p ") && !e.getPlayer().hasPermission("voxelsniper.sniper")) {
			e.setMessage("/plotsquared:" + e.getMessage().substring(1,e.getMessage().length()));
		}
		
		if (Util.startsWithIgnoreCase(e.getMessage(), "/pex")) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Util.getMain(), new Runnable() {
				public void run() {
					Util.updatePlayerList();
				}
			}, 20L);
		}
	}

}
