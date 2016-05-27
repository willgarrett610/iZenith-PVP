package net.izenith.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.izenith.Main.Vars;

public class ServerListHandler {

	public ServerListHandler() {
		ProtocolManager pManager = ProtocolLibrary.getProtocolManager();
		pManager.removePacketListeners(Util.getMain());
		pManager.addPacketListener(new PacketAdapter(Vars.main, Arrays.asList(PacketType.Status.Server.OUT_SERVER_INFO)) {
			@Override
			public void onPacketSending(PacketEvent event) {
				StructureModifier<WrappedServerPing> pings = event.getPacket().getServerPings();
				WrappedServerPing ping = pings.read(0);
				handlePing(ping);
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void handlePing(WrappedServerPing ping) {
		ping.setVersionName("iZenith Server 1.8.8");
		List<String> playerNames = new ArrayList<String>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			IPlayer iPlayer = IPlayerHandler.getPlayer(player);
			System.out.println(player.getName() + " in server list = " + iPlayer);
			playerNames.add(iPlayer.
					getColoredName(false));
		}
		String players = Util.parseColors(Util.buildString(playerNames, "&7, ", 0, 40));
		ping.setPlayers(Arrays.asList(new WrappedGameProfile("id1", Util.parseColors(Util.getConfig().getString("player_list_message"))), new WrappedGameProfile("id2", Util.parseColors("&7") + players)));
	}

}
