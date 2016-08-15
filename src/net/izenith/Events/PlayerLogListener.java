package net.izenith.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.izenith.Commands.CommandSpy;
import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Title;
import net.izenith.Main.Util;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;

public class PlayerLogListener extends Util implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent e) {
		//		for (Player player : Bukkit.getOnlinePlayers()) {
		//			if (player.getAddress().getHostName().equals(e.getPlayer().getAddress().getHostName()) && !player.equals(e.getPlayer())) {
		//				e.getPlayer().kickPlayer("A player with the same IP Address as you is already online!");
		//				e.setJoinMessage(null);
		//				return;
		//			}
		//		}
		final PlayerJoinEvent event = e;
		final Player player = e.getPlayer();
		IPlayerHandler.addPlayer(player);
		final IPlayer iPlayer = IPlayerHandler.getPlayer(player);
		Util.updatePlayerList();
		Util.loadOnlineTime(player);

		Bukkit.getScheduler().scheduleSyncDelayedTask(Util.getMain(), new Runnable() {
			@Override
			public void run() {
				String name = event.getPlayer().getName();

				Title title = new Title("&8Welcome to &4iZenith&fPVP","&7" + name);
				title.setFadeInTime(1);
				title.setStayTime(3);
				title.setFadeOutTime(1);
				title.send(player);

				iPlayer.createFile();
			}
		}, 20);

		if(!Util.hasJoined(player)){
			player.setGameMode(GameMode.SURVIVAL);
		}
		
		String message = Util.getConfig().getString("join_message");
		message = Util.parseColors(message);
		message = message.replaceAll("%player%", e.getPlayer().getName());
		e.setJoinMessage(null);
		Bukkit.broadcastMessage(message);

		ProtocolVersion ver = ProtocolSupportAPI.getProtocolVersion(e.getPlayer());
		if (ver.equals(ProtocolVersion.MINECRAFT_1_7_5) || ver.equals(ProtocolVersion.MINECRAFT_1_7_10)) {
			e.getPlayer().sendMessage(Util.parseColors("&c&lWARNING! &7Some parts of the server were built in 1.8 and will not look the same in 1.7!"));
		}

		iPlayer.setLastName(e.getPlayer().getName());
		iPlayer.sendTabFootHeader();
		iPlayer.loadGhost();
		CommandSpy.setupFilter(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onLeave(PlayerQuitEvent e) {
		String message = Util.getConfig().getString("leave_message");
		message = Util.parseColors(message);
		message = message.replace("%player%", e.getPlayer().getName());
		e.setQuitMessage(message);

		IPlayerHandler.getPlayer(e.getPlayer()).setOnlineTime();
		IPlayerHandler.removePlayer(e.getPlayer());
	}

}
