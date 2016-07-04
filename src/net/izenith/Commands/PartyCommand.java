package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Party;
import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class PartyCommand implements HubCommand {

	String[] requireParty = {
			"kick",
			"leave",
			"list"
	};
	
	@Override
	public String getName() {
		return "party";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		IPlayer iPlayer = IPlayerHandler.getPlayer(player);
		
		if(args.length == 0){
			Party.help(player);
			return;
		}
		
		if (iPlayer.party == null) {
			if (args[0].equalsIgnoreCase("invite")) {
				iPlayer.party = new Party(player);
			} else if(Util.contains(requireParty, args[0])){
				player.sendMessage(ChatColor.RED + "You are not in a party!");
				return;
			}
		} 

		Party party = iPlayer.party;

		switch (args[0]) {
		case "invite":
			try {
				Player to = Bukkit.getPlayer(args[1]);
				if (to == null) {
					player.sendMessage(ChatColor.RED + "Player not found!");
					return;
				}
				party.invite(to, player);
			} catch (ArrayIndexOutOfBoundsException e) {
				player.sendMessage("/party invite <player>");
			}
			break;
		case "help":
			Party.help(player);
			break;
		case "leave":
			party.leave(player);
			break;
		case "kick":
			try {
				Player to = Bukkit.getPlayer(args[1]);
				IPlayer toPlayer = IPlayerHandler.getPlayer(to);
				if (to == null) {
					player.sendMessage(ChatColor.RED + "Player not found!");
					return;
				}
				if(!party.players.contains(toPlayer)){
					player.sendMessage(ChatColor.RED + args[1] + ChatColor.GRAY + " is not in your party!");
					return;
				}
				party.kick(to);
			} catch (ArrayIndexOutOfBoundsException e) {
				player.sendMessage("/party kick <player>");
			}
			break;
		case "join":
			try {
				Player to = Bukkit.getPlayer(args[1]);
				if (to == null) {
					player.sendMessage(ChatColor.RED + "Player not found!");
					return;
				}
				IPlayer joinPlayer = IPlayerHandler.getPlayer(to);
				if(joinPlayer.party == null){
					player.sendMessage(ChatColor.GREEN + args[1] + ChatColor.RED + " is not in a party!");
					return;
				}
				joinPlayer.party.join(player,to);
			} catch (ArrayIndexOutOfBoundsException e) {
				player.sendMessage("/party join <player>");
			}
			break;
		case "list":
			party.list(player);
			break;
		default:
			Party.help(player);
			break;
		}
	}

	@Override
	public boolean onlyPlayers() {
		return true;
	}

	@Override
	public boolean hasPermission() {
		return false;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

}
