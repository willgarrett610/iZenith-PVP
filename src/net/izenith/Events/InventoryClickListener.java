package net.izenith.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.izenith.Main.Util;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if(!player.hasPermission("spawn.interact") && Util.getConfig().getStringList("inventory_interact_worlds").contains(player.getWorld().getName())){
			e.setCancelled(true);
		}
	}

}
