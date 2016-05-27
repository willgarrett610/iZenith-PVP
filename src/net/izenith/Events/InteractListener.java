package net.izenith.Events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.izenith.Main.Util;
import net.izenith.Main.Vars;

public class InteractListener implements Listener {

	@EventHandler
	public void onClick(final PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		if (e.getPlayer().getLocation().getWorld().getName().equals(Util.getConfig().getString("spawn_world")) && item != null && e.getItem().getType().equals(Material.NETHER_STAR)) {
			Vars.tpGUI.open(e.getPlayer());
		}

		if(e.getPlayer().getWorld().getName().equals(Util.getConfig().getString("spawn_world")) && e.getClickedBlock().getType().equals(Material.STONE_BUTTON)){
			e.setCancelled(false);
		}
	}
}
