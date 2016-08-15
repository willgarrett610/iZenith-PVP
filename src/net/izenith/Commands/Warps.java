package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.bobmandude9889.GUI.GUI;
import net.izenith.Main.Util;
import net.izenith.Main.Vars;

public class Warps implements HubCommand {

	@Override
	public String getName() {
		return "warps";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "warp" };
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if (args.length > 0)
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:warp " + args[0] + " " + player.getName());
		else
			openGUI(player);
	}

	public void openGUI(Player player) {
		GUI gui = new GUI(18, Util.parseColors("&4&liZenith &f&lWarps"), Vars.guiHandler);

		gui.addButton(Util.newItemMeta(Material.ENCHANTED_BOOK, Util.parseColors("&f&lEnchant"), null, 1), 1, new WarpRunnable("enchant", player));
		gui.addButton(Util.newItemMeta(Material.DIAMOND_CHESTPLATE, Util.parseColors("&4&lKit PVP"), null, 1), 3, new WarpRunnable("kitpvp", player));
		gui.addButton(Util.newItemMeta(Material.DIAMOND_SWORD, Util.parseColors("&f&lPVP"), null, 1), 5, new WarpRunnable("pvp", player));
		gui.addButton(Util.newItemMeta(Material.EYE_OF_ENDER, Util.parseColors("&4&lSpawn"), null, 1), 7, new WarpRunnable("spawn", player));
		gui.addButton(Util.newItemMeta(Material.EXP_BOTTLE, Util.parseColors("&4&lXP"), null, 1), 10, new WarpRunnable("xp", player));
		gui.addButton(Util.newItemMeta(Material.DRAGON_EGG, Util.parseColors("&f&lEnd"), null, 1), 12, new WarpRunnable("end", player));
		gui.addButton(Util.newItemMeta(Material.NETHERRACK, Util.parseColors("&4&lNether"), null, 1), 14, new WarpRunnable("nether", player));
		gui.addButton(Util.newItemMeta(Material.SAPLING, Util.parseColors("&f&lWild"), null, 1, (short) 3), 16, new WarpRunnable("wild", player));
		
		gui.open(player);
	}

	@Override
	public boolean onlyPlayers() {
		return true;
	}

	@Override
	public boolean hasPermission() {
		return false;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	private class WarpRunnable implements Runnable {

		String warp;

		Player player;

		protected WarpRunnable(String warp, Player player) {
			this.warp = warp;
			this.player = player;
		}

		@Override
		public void run() {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "essentials:warp " + warp + " " + player.getName());
		}

	}

}
