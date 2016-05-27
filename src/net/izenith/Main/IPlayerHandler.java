package net.izenith.Main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class IPlayerHandler {

	protected static HashMap<Player,IPlayer> players;
	
	public static void init(){
		players = new HashMap<Player,IPlayer>();
		for(Player player : Bukkit.getOnlinePlayers()){
			addPlayer(player);
			getPlayer(player).createFile();
			Util.loadOnlineTime(player);
		}
	}
	
	public static void addPlayer(Player player){
		players.put(player, new IPlayer(player));
	}
	
	public static void removePlayer(Player player){
		players.remove(player);
	}
	
	public static IPlayer getPlayer(Player player){
		return players.get(player);
	}
	
}
