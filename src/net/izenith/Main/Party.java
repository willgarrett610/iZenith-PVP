package net.izenith.Main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Party {

	public List<IPlayer> players;
	public List<IPlayer> invites;

	private static String[] help = { "&7/party invite <player>", "&7/party kick <player>", "&7/party join <player>", "&7/party leave", "&7/party list" };

	public Party(Player init) {
		players = new ArrayList<IPlayer>();
		invites = new ArrayList<IPlayer>();
		IPlayer player = IPlayerHandler.getPlayer(init);
		players.add(player);
		player.party = this;
	}

	public void join(Player player, Player from) {
		IPlayer iPlayer = IPlayerHandler.getPlayer(player);
		if (!players.contains(iPlayer)) {
			if (invites.contains(iPlayer)) {
				if (players.size() < 5) {
					if (iPlayer.party != null)
						iPlayer.party.leave(player);
					iPlayer.party = this;
					player.sendMessage(ChatColor.GRAY + "You have joined " + ChatColor.GREEN + from.getName() + "'s " + ChatColor.GRAY + "party");
					for (IPlayer iP : players) {
						iP.player.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.GRAY + " has joined your party");
					}
					players.add(iPlayer);
					invites.remove(iPlayer);
				} else {
					player.sendMessage(ChatColor.RED + "That party is full!");
				}
			} else {
				player.sendMessage(ChatColor.RED + "You have not been invited to that party.");
			}
		} else {
			player.sendMessage(ChatColor.RED + "You are already in that party!");
		}
	}

	public void leave(Player player) {
		IPlayer iPlayer = IPlayerHandler.getPlayer(player);
		if (players.contains(iPlayer)) {
			/*CombatLog cLog = (CombatLog) Bukkit.getPluginManager().getPlugin("CombatLog");
			if (!cLog.taggedPlayers.containsKey(player.getName())) {*/
				players.remove(iPlayer);
				iPlayer.party = null;
				player.sendMessage(ChatColor.RED + "You have left the party");
				if (players.size() != 0) {
					for (IPlayer iP : players) {
						iP.player.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " has left your party");
					}
				}
			/*} else {
				player.sendMessage(ChatColor.RED + "You cannot leave a party while Combat Logged");
			}*/
		} else {
			player.sendMessage(ChatColor.RED + "You are not in a party");
		}
	}

	public void invite(final Player player, final Player from) {
		final IPlayer iPlayer = IPlayerHandler.getPlayer(player);
		if (!players.contains(iPlayer)) {
			if (!invites.contains(iPlayer)) {
				invites.add(iPlayer);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " [\"\",{\"text\":\"_name_\",\"color\":\"green\"},{\"text\":\" has invited you to a party! Click \",\"color\":\"gray\"},{\"text\":\"HERE\",\"color\":\"aqua\",\"bold\":true,\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/party join _name_\"}},{\"text\":\" to join.\",\"color\":\"gray\",\"bold\":false,\"underlined\":false}]".replaceAll("_name_", from.getName()));
				from.sendMessage(ChatColor.GRAY + "Inviation to " + ChatColor.GREEN + player.getName() + ChatColor.GRAY + " sent");
				Bukkit.getScheduler().scheduleSyncDelayedTask(Util.getMain(), new Runnable() {
					@Override
					public void run() {
						if (invites.contains(iPlayer)) {
							invites.remove(iPlayer);
							from.sendMessage(ChatColor.GRAY + "Invitation to " + ChatColor.RED + player.getName() + ChatColor.GRAY + " has expired!");
							player.sendMessage(ChatColor.GRAY + "Invitation from " + ChatColor.RED + from.getName() + ChatColor.GRAY + " has expired!");
						}
					}
				}, 2400l);
			} else {
				from.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " has already been invited to your party!");
			}
		} else {
			from.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " is already in your party!");
		}
	}

	public void kick(Player player) {
		IPlayer iPlayer = IPlayerHandler.getPlayer(player);
		if (players.contains(iPlayer)) {
			players.remove(iPlayer);
			iPlayer.party = null;
			player.sendMessage(ChatColor.RED + "You have been kicked from the party!");
			for (IPlayer iP : players) {
				iP.player.sendMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " has been kicked from your party");
			}
		}
	}

	public void list(Player player) {
		player.sendMessage(ChatColor.GRAY + "=====================================================");
		List<String> names = new ArrayList<String>();
		for (IPlayer iPlayer : players) {
			names.add(iPlayer.getColoredName(false));
		}
		String list = Util.buildString(names, "&7, ", 0, 50);
		player.sendMessage(Util.parseColors(list));
		player.sendMessage(ChatColor.GRAY + "=====================================================");
	}

	public static void help(Player player) {
		player.sendMessage(ChatColor.GREEN + "Party help:");
		for (String command : help) {
			player.sendMessage(Util.parseColors(command));
		}
	}

}
