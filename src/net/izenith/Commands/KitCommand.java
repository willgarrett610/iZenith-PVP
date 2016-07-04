package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.izenith.Kit.KitManager;
import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class KitCommand implements HubCommand {

	@Override
	public String getName() {
		return "kit";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"kits"};
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		WorldGuardPlugin wGuard = Util.getWorldGuard();
		
		RegionManager rgManager = wGuard.getRegionManager(Bukkit.getWorld("kitpvp"));
		ProtectedRegion rg = rgManager.getRegion("spawn");

		Player player = (Player) sender;
		Location loc = player.getLocation();
		
		if(rg.contains(BukkitUtil.toVector(loc)) || player.getWorld().getName().split("_")[0].equals("faction")){
			KitManager.openGUI(player);
		} else {
			sender.sendMessage(ChatColor.RED + "You are not allowed to use /kit in this area!");
		}
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
		// TODO Auto-generated method stub
		return null;
	}

}
