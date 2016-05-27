package net.izenith.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.izenith.Main.Util;

public class PlayerMoveListener implements Listener{

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getTo().getWorld().getName().equals(Util.getConfig().getString("spawn_world")) && e.getTo().getBlockY() < 50){
			e.getPlayer().performCommand(Util.getConfig().getString("spawn_world"));
		}
	}
	
}
