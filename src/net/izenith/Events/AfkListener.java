package net.izenith.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.ess3.api.events.AfkStatusChangeEvent;
import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;

public class AfkListener implements Listener {

	@EventHandler
	public void onAfkEvent(AfkStatusChangeEvent e) {
		IPlayer player = IPlayerHandler.getPlayer(e.getAffected().getBase());
		if (e.getValue()) {
			player.afkStartTime = System.currentTimeMillis();
		} else {
			if (player.afkStartTime != -1) {
				player.joinTime += System.currentTimeMillis() - player.afkStartTime;
				player.afkStartTime = -1;
			}
		}
	}

}
