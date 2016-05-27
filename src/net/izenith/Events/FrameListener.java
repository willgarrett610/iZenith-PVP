package net.izenith.Events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.izenith.FrameAPI.FrameAction;

public class FrameListener implements Listener{

	@EventHandler
	public void onFrameDestroy(FrameDestroyEvent e){
		if(e.getAction().equals(FrameAction.LeftClick)){
			e.setCancelled(true);
			ItemFrame frame = e.getItemFrame();
			frame.setRotation(frame.getRotation().rotateCounterClockwise());
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntityType().equals(EntityType.ITEM_FRAME)) {
			ItemFrame itemFrame = (ItemFrame) e.getEntity();
			if (e.getDamager() instanceof Player) {
				Player player = (Player) e.getDamager();
				if(player.isSneaking()){
					itemFrame.setRotation(itemFrame.getRotation().rotateCounterClockwise());
					e.setCancelled(true);
				}
			}
		}
	}
	
}
