package net.izenith.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DamageListener implements Listener{

	@EventHandler
	public void onDamage(PlayerDeathEvent e){
		if(e.getEntity().getLocation().getWorld().getName().equals("spawn")){
			e.getEntity().setHealth(20);
		}
	}
	
	public void onDamage(EntityDamageEvent e){
		e.getDamage();
	}
	
}
