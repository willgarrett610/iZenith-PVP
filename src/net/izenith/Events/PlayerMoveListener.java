package net.izenith.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.izenith.Main.Vars;

public class PlayerMoveListener implements Listener{

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		RegionManager rgManager = Util.getWorldGuard().getRegionManager(Bukkit.getWorld("kitpvp"));
		ProtectedRegion rg = rgManager.getRegion("kit");
		if(rg.contains(BukkitUtil.toVector(e.getTo())) && !Vars.guiHandler.getOpenInvs().containsKey(e.getPlayer()) && !IPlayerHandler.getPlayer(e.getPlayer()).gotKit){
			e.getPlayer().performCommand("kit");
		}
		
		GameMode gm = e.getPlayer().getGameMode();
		if((gm.equals(GameMode.SURVIVAL) || gm.equals(GameMode.ADVENTURE)) && e.getPlayer().getInventory().contains(Material.GLASS_BOTTLE)){
			e.getPlayer().getInventory().remove(Material.GLASS_BOTTLE);
		}
	}
	
}
