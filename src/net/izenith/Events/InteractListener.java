package net.izenith.Events;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.izenith.Main.Vars;
import net.md_5.bungee.api.ChatColor;

public class InteractListener implements Listener {

	@EventHandler
	public void onClick(final PlayerInteractEvent e) {
		Action a = e.getAction();
		if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
			Player p = e.getPlayer();
			BlockState b = e.getClickedBlock().getState(); //b is the block
			if (b instanceof Skull) { //this test if b is a skull
				if (!(p.isSneaking()) || (p.getItemInHand().getType() == Material.AIR)){
					Skull s = (Skull) b;
					String owner = s.getOwner();
					if (owner == null) {
					} else {
						p.sendMessage(ChatColor.YELLOW + "That is the head of " + ChatColor.GOLD + owner + ChatColor.YELLOW + ".");
						e.setCancelled(true);
					}
				}
			}
		}

		ItemStack item = e.getItem();
		if (e.getPlayer().getLocation().getWorld().getName().equals("spawn") && item != null && e.getItem().getType().equals(Material.NETHER_STAR)) {
			Vars.tpGUI.open(e.getPlayer());
		}

		if(e.getPlayer().getWorld().getName().equals("spawn") && e.getClickedBlock().getType().equals(Material.STONE_BUTTON)){
			e.setCancelled(false);
		}
	}
}
