package net.izenith.Chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.izenith.Main.Vars;

public class ChatHandler implements Listener {

	// Custom chat handler for mentioning players in chat and per group chat
	// formats.

	// Set to highest priority in order to override essentials chat handler

	@EventHandler(priority = EventPriority.HIGHEST)
	public void chat(AsyncPlayerChatEvent e) {
		if (Vars.adminChat.contains(e.getPlayer())
				|| e.getMessage().startsWith(",")
				&& e.getPlayer().hasPermission("izenith.adminchat")
				&& (e.getMessage().length() != 1)) {
			e.setCancelled(true);
			Util.sendAdminMessage(e.getMessage().startsWith(",") ? e
					.getMessage().substring(1) : e.getMessage(), e.getPlayer());
			return;
		}

		// Get main plugin class from Util
		try {
			e.setCancelled(true);
			IPlayer player = IPlayerHandler.getPlayer(e.getPlayer());
			//if (Util.ess.getUser(e.getPlayer()).isMuted()) {
				player.sendChatMessage(e.getMessage());
			//}
			// t = translated
			/*
			 * String language = Util.detectLanguage(pMessage);
			 * if(!language.equals("en")){ String tMessage =
			 * message.replaceAll("%message%",
			 * Util.getTranslation(pMessage,language,"en"));
			 * Bukkit.broadcastMessage(tMessage); }
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}