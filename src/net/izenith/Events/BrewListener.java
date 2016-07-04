package net.izenith.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BrewerInventory;

import net.izenith.Main.Util;

public class BrewListener implements Listener {

	List<BrewingStand> tagged;
	
	public BrewListener() {
		tagged = new ArrayList<BrewingStand>();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Util.getMain(), new Runnable(){

			@Override
			public void run() {
				for(BrewingStand stand : tagged){
					stand.setFuelLevel(20);
					stand.update();
				}
			}
			
		},100l,100l);
	}
	
	@EventHandler
	public void onBrewingStandInteract(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.BREWING_STAND)) {
			BrewingStand stand = (BrewingStand) e.getClickedBlock().getState();
			if(!tagged.contains(stand))
				tagged.add(stand);
		}
	}

}
