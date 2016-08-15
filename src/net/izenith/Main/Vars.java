package net.izenith.Main;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import net.bobmandude9889.GUI.GUIHandler;
import net.izenith.Chat.ChatHandler;
import net.izenith.CommandSpy.CommandFilter;
import net.izenith.CommandSpy.CommandListener;
import net.izenith.Commands.AdminChat;
import net.izenith.Commands.Buy;
import net.izenith.Commands.BuyMoney;
import net.izenith.Commands.ClearChat;
import net.izenith.Commands.ClearKit;
import net.izenith.Commands.CommandSpy;
import net.izenith.Commands.Donated;
import net.izenith.Commands.HubCommand;
import net.izenith.Commands.KD;
import net.izenith.Commands.KitCommand;
import net.izenith.Commands.PartyCommand;
import net.izenith.Commands.Ping;
import net.izenith.Commands.PlayTime;
import net.izenith.Commands.RankBuy;
import net.izenith.Commands.Ranks;
import net.izenith.Commands.Reload;
import net.izenith.Commands.ReloadKits;
import net.izenith.Commands.Rename;
import net.izenith.Commands.Report;
import net.izenith.Commands.Sell;
import net.izenith.Commands.ServerIp;
import net.izenith.Commands.SetKit;
import net.izenith.Commands.UpdateList;
import net.izenith.Commands.Warps;
import net.izenith.Events.BrewListener;
import net.izenith.Events.DamageListener;
import net.izenith.Events.InventoryListener;
import net.izenith.Events.ItemDropListener;
import net.izenith.Events.PlayerDeathListener;
import net.izenith.Events.PlayerLogListener;
import net.izenith.Events.PlayerMoveListener;
import net.izenith.Events.ServerListHandler;
import net.izenith.Gamemode.TeleportListener;
import net.izenith.Shop.ShopManager;

public class Vars {

	public static HubCommand[] commands;
	public static HashMap<Player, CommandFilter> commandSpy;
	public static HashMap<Player, String> createClock;
	public static List<String> clocks;
	public static Main main;
	public static ScoreboardManager manager;
	public static Scoreboard scoreboard;
	public static List<Team> teams;
	public static Team ghostTeam;
	public static GUIHandler guiHandler;
	public static List<Player> adminChat;
	public static ProtocolManager protocolManager;
	public static ServerSocket remoteConsoleSocket;
	public static int messageIndex = 0;
	public static List<String> autoMessages;
 
	public static void init(Main plugin) {
		guiHandler = new GUIHandler(plugin);
		
		manager = Bukkit.getScoreboardManager();
		scoreboard = manager.getMainScoreboard();
		teams = new ArrayList<Team>();
		main = plugin;
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){

			@Override
			public void run() {
				for(IPlayer player : IPlayerHandler.players.values()){
					player.sendTabFootHeader();
				}
				Util.kickBoats();
			}
			
		}, 20l, 20l);
		
		commands = new HubCommand[] { 
				new CommandSpy(),
				new Report(),
				new Ranks(),
				new ServerIp(),
				new Donated(),
				new PlayTime(),
				new UpdateList(),
				new AdminChat(),
				new ClearChat(),
				new Rename(),
				new Ping(),
				new KitCommand(),
				new SetKit(),
				new ReloadKits(),
				new ClearKit(),
				new RankBuy(),
				new PartyCommand(),
				new KD(),
				new Sell(),
				new Buy(),
				new Reload(),
				new Warps(),
				new BuyMoney()
				//new Console()
				};
		commandSpy = new HashMap<Player, CommandFilter>();
		adminChat = new ArrayList<Player>();
		
		/*
		 * createClock = new HashMap<Player, String>(); clocks =
		 * plugin.getConfig().getStringList("clocks");
		 */
		List<Listener> lis = new ArrayList<Listener>();
		//lis.add(new PlayerInteractHandler());
		lis.add(new ChatHandler());
		lis.add(new PlayerLogListener());
		lis.add(new TeleportListener());
		lis.add(new CommandListener());
		lis.add(new Report());
		lis.add(new PlayerMoveListener());
		lis.add(new DamageListener());
		lis.add(new InventoryListener());
		lis.add(new ItemDropListener());
		lis.add(new PlayerDeathListener());
		lis.add(new ServerListHandler());
		lis.add(new ShopManager());
		lis.add(new BrewListener());
		//lis.add(new FrameListener());
		for (Listener l : lis) {
			plugin.getServer().getPluginManager().registerEvents(l, plugin);
		}
		/* new TimeHandler(); */
		protocolManager = ProtocolLibrary.getProtocolManager();
		
		autoMessages = Util.getConfig().getStringList("auto_messages");
	}

}
