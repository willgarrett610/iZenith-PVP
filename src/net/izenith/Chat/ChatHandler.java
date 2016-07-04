package net.izenith.Chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.izenith.Main.Vars;

public class ChatHandler implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void chat(AsyncPlayerChatEvent e) {
		if (Vars.adminChat.contains(e.getPlayer()) || e.getMessage().startsWith(",") && e.getPlayer().hasPermission("izenith.adminchat") && (e.getMessage().length() != 1)) {
			e.setCancelled(true);
			Util.sendAdminMessage(e.getMessage().startsWith(",") ? e.getMessage().substring(1) : e.getMessage(), e.getPlayer());
			return;
		}

		try {
			if (!e.isCancelled()) {
				e.setCancelled(true);
				IPlayerHandler.getPlayer(e.getPlayer()).sendChatMessage(e.getMessage());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}