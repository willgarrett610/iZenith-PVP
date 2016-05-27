package net.izenith.Gamemode;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class TeleportListener implements Listener {

	@EventHandler
	public void onTeleport(final PlayerTeleportEvent e) {
		final Player p = e.getPlayer();
		if (e.getCause().equals(TeleportCause.SPECTATE)
				&& !(p.hasPermission("essentials.tp"))){ 
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You do not have permission to /tp");
		}
		
		if (!e.getFrom().getWorld().equals(e.getTo().getWorld())) {
			MultiverseCore mv = (MultiverseCore) Bukkit.getServer()
					.getPluginManager().getPlugin("Multiverse-Core");
			MVWorldManager wm = mv.getMVWorldManager();
			MultiverseWorld toWorld = wm.getMVWorld(e.getTo().getWorld()
					.getName());
			MultiverseWorld fromWorld = wm.getMVWorld(e.getFrom().getWorld()
					.getName());
			System.out.println(toWorld.getName());
			if (toWorld != fromWorld
					&& !p.getGameMode().equals(toWorld.getGameMode())) {
				p.setGameMode(toWorld.getGameMode());
			}
		}
		if (e.getTo().getWorld().getName().equals("spawn")) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Util.getMain(),
					new Runnable() {
						@Override
						public void run() {
							IPlayerHandler.getPlayer(p).getKit("nether_star",
									false);
						}
					}, 40l);
		}
	}
}
