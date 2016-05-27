package net.izenith.Events;

import net.izenith.Main.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class BlockPlaceListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlace(final BlockPlaceEvent e) {
		Player p = e.getPlayer();
		// Makes all checks
		if (p.getGameMode() == GameMode.CREATIVE) {
			if (e.getBlock().getType().equals(Material.CAULDRON)) {
				e.getBlock().setData((byte) 3);
			}
			if (e.getBlock().getType().equals(Material.REDSTONE_BLOCK)
					&& p.getItemInHand().getItemMeta().hasDisplayName()) {
				// Grabs the number of redstone ticks
				try {
					double dTicks = Double.parseDouble(p.getItemInHand()
							.getItemMeta().getDisplayName());
					if (dTicks <= 100 && dTicks >= 0) {
						String sTicks = Double.toString(dTicks / 10);
						p.sendMessage(ChatColor.BLUE
								+ "You place a timed Redstone Block.");
						p.sendMessage(ChatColor.BLUE + "It will disappear in "
								+ ChatColor.GREEN + sTicks + ChatColor.BLUE
								+ " seconds");
						// Changes the number of redstone ticks to minecraft ticks
						dTicks = dTicks * 2;
						// Conversion to Int for Bukkit Scheduler
						int iTicks = (int) dTicks;
						BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
						scheduler.scheduleSyncDelayedTask(Util.getMain(),
									new Runnable() {
									@Override
									public void run() {
										if (e.getBlock()
												.getType()
												.equals(Material.REDSTONE_BLOCK)) {
											e.getBlock().setType(Material.AIR);
										}
									}
								}, iTicks);
					} else {
						p.sendMessage(ChatColor.BLUE
								+ "That was a crazy number boi!");
						p.sendMessage(ChatColor.DARK_RED
								+ "PS. Numbers can only go from 0 to 100");
					}
				} catch (NumberFormatException ex) {
				}
			}
		}
	}
}
