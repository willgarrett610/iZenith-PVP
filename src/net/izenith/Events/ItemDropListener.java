package net.izenith.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import net.izenith.Main.Util;

public class ItemDropListener implements Listener {

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		if(!player.hasPermission("spawn.interact") && Util.getConfig().getStringList("inventory_interact_worlds").contains(player.getWorld().getName())){
			e.setCancelled(true);
		}
	}

}
