package net.izenith.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener{

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getTo().getWorld().getName().equals("spawn") && e.getTo().getBlockY() < 50){
			if(Math.abs(-1000 - e.getTo().getBlockX()) < 500){
				e.getPlayer().performCommand("warp info");
			} else if(Math.abs(0 - e.getTo().getBlockX()) < 500){
				e.getPlayer().performCommand("spawn");
			}
		}
	}
	
}
