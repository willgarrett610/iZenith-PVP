package net.izenith.Kit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class Kit {

	//Kit info
	public String name;
	File file;
	List<PotionEffect> effects = null;
	ItemStack[] contents;
	ItemStack[] armor;
	String world;

	public ItemStack guiItem;

	long cooldown;

	protected Kit(String world, String name, ItemStack guiItem, File file) {
		this.name = name;
		this.file = file;
		this.world = world;
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.guiItem = guiItem;
	}

	public void loadContents() {
		System.out.println("Loading kit " + name + " from " + file.getPath());
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			byte[] b = new byte[in.available()];
			in.read(b);
			String fileIn = new String(b);
			String[] b64 = fileIn.split(" ");
			contents = Util.itemStackArrayFromBase64(b64[0]);
			armor = Util.itemStackArrayFromBase64(b64[1]);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setContents(ItemStack[][] items) {
		try {
			PrintStream out = new PrintStream(file);
			out.print(Util.itemStackArrayToBase64(items[0]) + " ");
			out.print(Util.itemStackArrayToBase64(items[1]));
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		contents = items[0];
		armor = items[1];
	}

	public void setEffects(List<PotionEffect> effects) {
		this.effects = effects;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public long getCooldown() {
		return cooldown;
	}

	public ItemStack[][] getContents() {
		return new ItemStack[][] { contents, armor };
	}

	public void applyKit(Player player, boolean clearInv) {
		if (player.hasPermission("kit." + name)) {
			IPlayer iPlayer = IPlayerHandler.getPlayer(player);
			long lastUse = iPlayer.getLastUse(world,this);
			if (System.currentTimeMillis() - lastUse >= cooldown * 1000 || player.hasPermission("kit.bypass_cooldown")) {
				if (clearInv) {
					PlayerInventory inv = player.getInventory();
					inv.setContents(contents);
					inv.setArmorContents(armor);
					for (PotionEffect effect : player.getActivePotionEffects()) {
						player.removePotionEffect(effect.getType());
					}
				} else {
					for(ItemStack item : contents){
						Util.giveItem(player, item);
					}
				}
				for (PotionEffect pE : effects) {
					player.addPotionEffect(pE);
				}
				iPlayer.gotKit = true;
				iPlayer.setLastUse(world,this, System.currentTimeMillis());
				iPlayer.setGhost(false);
				if (name.equals("ghost")) {
					iPlayer.setGhost(true);
				}
			} else {
				String nextUseTime = Util.formatTime(lastUse + (cooldown * 1000) - System.currentTimeMillis());
				player.sendMessage(ChatColor.RED + "You can use that kit again in " + ChatColor.AQUA + nextUseTime);
				KitManager.openGUI(player);
				IPlayerHandler.getPlayer(player).setGhost(false);
			}
		} else {
			player.sendMessage(ChatColor.RED + "You don't have access to that kit. You can purchase access to this and other kits at " + ChatColor.AQUA + " store.izenith.net");
			KitManager.openGUI(player);
			IPlayerHandler.getPlayer(player).setGhost(false);
		}
	}

}
