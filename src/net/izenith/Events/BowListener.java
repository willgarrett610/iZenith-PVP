package net.izenith.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.izenith.Main.Util;

public class BowListener implements Listener {

	List<Entity> arrows;
	HashMap<Entity, Material> arrowMaterials;

	public BowListener() {
		arrows = new ArrayList<Entity>();
		arrowMaterials = new HashMap<Entity, Material>();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onArrowFire(EntityShootBowEvent e) {
		if (e.getEntityType().equals(EntityType.PLAYER) && e.getBow().hasItemMeta()) {
			ItemMeta meta = e.getBow().getItemMeta();
			if (meta.hasDisplayName() && Util.containsIgnoreCase(meta.getDisplayName(), "dick") && e.getEntity().hasPermission("bow.dick")) {
				arrows.add(e.getProjectile());
				String name = meta.getDisplayName();
				if (name.contains(":")) {
					String[] splitName = name.split(":");
					if (splitName[0].equalsIgnoreCase("dick")) {
						splitName[1] = splitName[1].replace(" ", "");
						try {
							int id = Integer.parseInt(splitName[1]);
							arrowMaterials.put(e.getProjectile(), Material.getMaterial(id));
						} catch (NumberFormatException exc) {
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onArrowLand(ProjectileHitEvent e) {
		if (arrows.contains(e.getEntity())) {
			arrows.remove(e.getEntity());
			Location start = e.getEntity().getLocation();
			if (arrowMaterials.containsKey(e.getEntity())) {
				Material material = arrowMaterials.get(e.getEntity());
				set(start, material);
				start.add(1, 1, 0);
				set(start, material);
				start.add(0, 1, 0);
				set(start, material);
				start.add(1, -2, 0);
				set(start, material);
				arrowMaterials.remove(e.getEntity());
			} else {
				setRandom(start);
				start.add(1, 1, 0);
				setRandom(start);
				start.add(0, 1, 0);
				setRandom(start);
				start.add(1, -2, 0);
				setRandom(start);
			}
		}
	}

	public void setRandom(Location loc) {
		List<Material> blockMaterials = new ArrayList<Material>();
		for (Material material : Material.values()) {
			if (material.isBlock())
				blockMaterials.add(material);
		}
		Random gen = new Random();
		Material material = blockMaterials.get(gen.nextInt(blockMaterials.size()));
		set(loc, material);
	}

	public void set(Location loc, Material material) {
		if (loc.getBlock().getType().equals(Material.AIR)) {
			loc.getBlock().setType(material);
			if (material.equals(Material.CHEST) || material.equals(Material.TRAP_DOOR)) {
				Chest chest = (Chest) loc.getBlock().getState();
				Inventory inv = chest.getInventory();
				for (int i = 0; i < inv.getSize(); i++) {
					Random gen = new Random();
					inv.setItem(i, new ItemStack(Material.values()[gen.nextInt(Material.values().length)]));
				}
				inv.setItem(4, new ItemStack(Material.BEDROCK));
				inv.setItem(13, new ItemStack(Material.BEDROCK));
				inv.setItem(21, new ItemStack(Material.BEDROCK));
				inv.setItem(23, new ItemStack(Material.BEDROCK));
			}
		}
	}
	
}
