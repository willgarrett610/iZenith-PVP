package net.izenith.Main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import net.izenith.Kit.Kit;

public class IPlayer {

	public Player player;
	public YamlConfiguration config;
	public File file;
	public long joinTime = -1;
	public long afkStartTime = -1;
	public boolean ghost = false;
	public boolean gotKit = false;
	public Party party = null;

	public IPlayer(Player player) {
		this.player = player;
		File dataFolder = new File(Util.getMain().getDataFolder().getPath(), "players");
		if (!dataFolder.exists())
			dataFolder.mkdir();
		this.file = new File(dataFolder.getPath(), player.getUniqueId() + ".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
	}

	public void createFile() {
		try {
			file.createNewFile();
			config.set("last_name", player.getName());
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setOnlineTime() {
		try {
			config.set("time", getOnlineTime());
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setOnlineTime(Long time) {
		try {
			config.set("time", time);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long getOnlineTime() {
		Long time = config.getLong("time");
		time = time == null ? 0 : time;
		return time + (System.currentTimeMillis() - joinTime);
	}

	public String getOnlineTimeHours() {
		Double timeHours = new Double(getOnlineTime()) / (1000 * 60 * 60);
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(timeHours);
	}

	public void removeKit(String name) {
		name = name.toLowerCase();
		config.set("kits." + name, null);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getRankName() {
		String pref = PermissionHandler.getGroup(player).getPrefix();
		return Util.parseColors(pref + player.getName());
	}

	public String getColoredName(boolean truncate) {
		ChatColor color = Util.getGroupColor(PermissionHandler.getGroupName(player));
		String name = player.getName();
		int length = name.length();
		if (length > 14 && truncate) {
			int subtract = length - 14;
			name = name.substring(0, length - subtract - 1);
		}
		return color + name;
	}

	@SuppressWarnings("deprecation")
	public void setTeam() {
		player.setPlayerListName(getColoredName(true));
		if (!isGhost()) {
			for (Team t : Vars.teams) {
				if (t.getName().equalsIgnoreCase(PermissionHandler.getGroupName(player))) {
					t.addPlayer(player);
					return;
				}
			}
		}
	}

	public void setCommandSpy(String filter) {
		config.set("commandspy", filter);
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				System.out.println(scanner.nextLine());
			}
			config.save(file);
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCommandSpy() {
		return config.getString("commandspy");
	}

	public void setLastName(String name) {
		config.set("last_name", name);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setLanguage(String lang) {
		config.set("language", lang);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getLanguage() {
		String lang = config.getString("language");
		if (lang == null) {
			return "en";
		}
		return lang;
	}

	public void setTranslate(boolean translate) {
		config.set("translate", translate);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean getTranslate() {
		if (!config.contains("translate")) {
			return true;
		}
		return config.getBoolean("translate");
	}

	public int getPing() {
		CraftPlayer player = (CraftPlayer) this.player;
		int ping = player.getHandle().ping;
		return ping;
	}

	public String getPingWithColor() {
		int ping = getPing();
		return (ping <= 100 ? ChatColor.GREEN : ping <= 200 ? ChatColor.YELLOW : ChatColor.RED).toString() + ping;
	}

	@SuppressWarnings("deprecation")
	public void setGhost(boolean ghost) {
		this.ghost = ghost;
		setTeam();
		config.set("ghost", ghost);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!ghost) {
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
		} else {
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 0));

			player.setHealth(40);

			for (Team team : Vars.teams) {
				if (team.getName().equals("Ghost")) {
					team.addPlayer(player);
				}
			}
		}
	}

	public void loadGhost() {
		setGhost(config.getBoolean("ghost"));
	}

	public boolean isGhost() {
		return ghost;
	}

	public void sendChatMessage(String text) {
		MPlayer mPlayer = MPlayer.get((Object) player);
		Faction faction = mPlayer.getFaction();
		if (!player.hasPermission("izenith.chat.format")) {
			text = ChatColor.stripColor(text);
		} else {
			text = Util.parseColors(text);
		}
		for (final Player player : Bukkit.getOnlinePlayers()) {

			if (!Util.ess.getUser(player).isIgnoredPlayer(Util.ess.getUser(this.player))) {

				MPlayer itMPlayer = MPlayer.get((Object) player);
				Rel rel = faction.getRelationTo(itMPlayer.getFaction());

				ChatColor colorCode = faction.getColorTo(itMPlayer);

				rel = mPlayer.getRelationTo(faction);
				String prefix = rel.equals(Rel.RECRUIT) ? "-" : rel.equals(Rel.MEMBER) ? "+" : rel.equals(Rel.OFFICER) ? "*" : "**";

				String factionName = isInFaction() ? "&0[" + colorCode + prefix + faction.getName() + "&0]&r " : "";

				String message = Util.parseColors(factionName + getRankName() + " &0\u2192 &f") + text;
				if (!this.player.equals(player) && Util.containsIgnoreCase(message, player.getName())) {

					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
					Util.getMain().getServer().getScheduler().scheduleSyncDelayedTask(Util.getMain(), new Runnable() {
						@Override
						public void run() {
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1.25f);
						}
					}, 10l);

					int index = message.toLowerCase().indexOf(player.getName().toLowerCase());
					String newMessage = message.substring(0, index) + ChatColor.RED + message.substring(index, index + player.getName().length()) + ChatColor.WHITE + message.substring(index + player.getName().length(), message.length());
					player.sendMessage(newMessage);
				} else {
					player.sendMessage(message);
				}
			}
		}
	}

	public boolean isInFaction() {
		MPlayer mPlayer = MPlayer.get((Object) player);
		Faction faction = mPlayer.getFaction();
		String id = faction.getId();
		return !(id.equals(Factions.ID_NONE) || id.equals(Factions.ID_SAFEZONE) || id.equals(Factions.ID_WARZONE));
	}

	public void sendTabFootHeader() {
		PacketContainer pc = Vars.protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

		String header = replacePlaceholders(Util.getConfig().getString("tab.header"));
		String footer = replacePlaceholders(Util.getConfig().getString("tab.footer"));

		pc.getChatComponents().write(0, WrappedChatComponent.fromText(Util.parseColors(header))).write(1, WrappedChatComponent.fromText(Util.parseColors(footer)));
		try {
			Vars.protocolManager.sendServerPacket(player, pc);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public String replacePlaceholders(String string) {
		String kd = getKD();

		Double bal = Util.getEconomy().getBalance(player);
		String balFormat = Util.formatDouble(bal);

		return string.replaceAll("\\{ONLINE_PLAYERS\\}", Bukkit.getOnlinePlayers().size() + "").replaceAll("\\{PING\\}", getPingWithColor()).replaceAll("\\{KILLS\\}", getKills().intValue() + "").replaceAll("\\{DEATHS\\}", getDeaths().intValue() + "").replaceAll("\\{KD\\}", kd).replaceAll("\\{BALANCE\\}", balFormat);
	}

	public void setLastUse(String world, Kit kit, long time) {
		config.set("last_use." + world + "." + kit.name, time);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long getLastUse(String world, Kit kit) {
		String path = "last_use." + world + "." + kit.name;
		if (config.contains(path))
			return config.getLong(path);
		else
			return 0;
	}

	public void setDeaths(double deaths) {
		config.set("deaths", deaths);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setKills(double kills) {
		config.set("kills", kills);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Double getDeaths() {
		if (config.contains("deaths"))
			return config.getDouble("deaths");
		return 0d;
	}

	public Double getKills() {
		if (config.contains("kills"))
			return config.getDouble("kills");
		return 0d;
	}

	public String getKD() {
		String kd = "";
		if (this.getDeaths() == 0) {
			kd = getKills().toString();
		} else {
			DecimalFormat df = new DecimalFormat("#.##");
			kd = df.format(new Double(this.getKills()) / new Double(this.getDeaths()));
		}
		return kd;
	}

}