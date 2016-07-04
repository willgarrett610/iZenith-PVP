package net.izenith.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.izenith.Kit.KitManager;
import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getEntity().getWorld().getName().equalsIgnoreCase(Util.getConfig().getString("pvp_world"))) {
			e.getDrops().clear();
		}
		KitManager.clearKit(e.getEntity());
		IPlayer iPlayer = IPlayerHandler.getPlayer(e.getEntity());
		iPlayer.setDeaths(new Double(iPlayer.getDeaths() + 1));
		iPlayer.sendTabFootHeader();
		if (e.getEntity().getKiller() != null) {
			IPlayer killer = IPlayerHandler.getPlayer(e.getEntity().getKiller());
			killer.setKills(killer.getKills() + 1);
			killer.sendTabFootHeader();
		}
	}

}
