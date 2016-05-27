package net.izenith.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.izenith.Commands.CommandSpy;
import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;

public class PlayerLogListener extends Util implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent e) {
		final PlayerJoinEvent event = e;
		Player player = e.getPlayer();
		IPlayerHandler.addPlayer(player);
		final IPlayer iPlayer = IPlayerHandler.getPlayer(player);
		Util.updatePlayerList();
		Util.loadOnlineTime(player);

		if (!Util.hasJoined(e.getPlayer())) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Util.getMain(), new Runnable(){
				@Override
				public void run(){
					String name = event.getPlayer().getName();
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title " + name + " times 20 60 20");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							"title " + name + " subtitle {text:\"" + name + "\",color:\"gray\"}");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							"title " + name + " title {text:\"Welcome to \",color:\"dark_gray\",extra:[{text:\"iZenith\",color:\"white\"},{text:\" Minecraft\",color:\"gold\"}]}");
					iPlayer.createFile();
				}
			},20);
		}
		
		String message = Util.getConfig().getString("join_message");
		message = Util.parseColors(message);
		message = message.replaceAll("%player%",e.getPlayer().getName());
		e.setJoinMessage(message);
		iPlayer.setLastName(e.getPlayer().getName());
		iPlayer.sendTabFootHeader();
		CommandSpy.setupFilter(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onLeave(PlayerQuitEvent e){
		String message = Util.getConfig().getString("leave_message");
		message = Util.parseColors(message);
		message = message.replace("%player%",e.getPlayer().getName());
		e.setQuitMessage(message);
		
		IPlayerHandler.getPlayer(e.getPlayer()).setOnlineTime();
		IPlayerHandler.removePlayer(e.getPlayer());
	}
	
}
