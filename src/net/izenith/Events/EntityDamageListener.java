package net.izenith.Events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity().getType().equals(EntityType.PLAYER)) {
			if (e.getDamager().getType().equals(EntityType.SPLASH_POTION)) {
				e.setCancelled(true);
			}
		}
	}
}