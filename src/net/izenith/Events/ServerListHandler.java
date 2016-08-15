package net.izenith.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.izenith.Main.Vars;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;

public class ServerListHandler implements Listener {

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
		ping.setVersionName("iZenith 1.7.x, 1.8.x, 1.9.x");
		List<String> playerNames = new ArrayList<String>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			playerNames.add(IPlayerHandler.getPlayer(player).getColoredName(false));
		}
		String players = Util.parseColors(Util.buildString(playerNames, "&7, ", 0, 40));
		ping.setPlayers(Arrays.asList(new WrappedGameProfile("id1", Util.parseColors(Util.getConfig().getString("player_list_message"))), new WrappedGameProfile("id2", Util.parseColors("&7") + players)));
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		ProtocolVersion ver = ProtocolSupportAPI.getProtocolVersion(e.getPlayer());
		if (ver.isBefore(ProtocolVersion.MINECRAFT_1_7_5)) {
			e.getPlayer().kickPlayer(Util.parseColors("&c" + ver.getName() + " &fis not supported by iZenith.\n" + "Supported versions are &a1.7.x&f, &a1.8.x &fand &a1.9.x"));
		}
	}
	
}
