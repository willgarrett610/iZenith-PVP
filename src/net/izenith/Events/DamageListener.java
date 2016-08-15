package net.izenith.Events;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;

public class DamageListener implements Listener {

	@EventHandler(priority=EventPriority.LOWEST)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			IPlayer damager = IPlayerHandler.getPlayer((Player) e.getDamager());
			/*if (e.getDamager() instanceof Player) {
				WorldGuardPlugin wGuard = Util.getWorldGuard();

				RegionManager rgManager = wGuard.getRegionManager(Bukkit.getWorld("kitpvp"));
				ProtectedRegion rg = rgManager.getRegion("spawn");

				Player d = (Player) e.getDamager();
				Player p = (Player) e.getEntity();

				if (rg.contains(BukkitUtil.toVector(d.getLocation())) || rg.contains(BukkitUtil.toVector(p.getLocation()))) {
					CombatLog cLog = (CombatLog) Bukkit.getPluginManager().getPlugin("CombatLog");
					cLog.taggedPlayers.remove(d.getName());
					cLog.taggedPlayers.remove(p.getName());
				}
			} else */if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();
				if (arrow.getShooter() instanceof Player) {
					damager = IPlayerHandler.getPlayer((Player) arrow.getShooter());
				} else {
					return;
				}
			} else {
				return;
			}

			IPlayer player = IPlayerHandler.getPlayer((Player) e.getEntity());

			if (damager.party != null && player.party != null) {
				if (damager.party.players.contains(player)) {
					e.setCancelled(true);
				}
			}
			
			double damage = e.getDamage();
			if((damage = damage * 1.75) > 20)
				damage = 20;
			e.setDamage(damage);
			
		}

	}

}
