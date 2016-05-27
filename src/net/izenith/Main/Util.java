package net.izenith.Main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.earth2me.essentials.Essentials;

import net.izenith.Commands.AdminChat;
import net.milkbowl.vault.permission.Permission;

public class Util {

	public static Essentials ess;

	public static void LoadEssentials() {
		ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	}
	
	public static void line(Location l1, Location l2, Material material) {
		double xSlope = (l1.getBlockX() - l2.getBlockX());
		double ySlope = (l1.getBlockY() - l2.getBlockY()) / xSlope;
		double zSlope = (l1.getBlockZ() - l2.getBlockZ()) / xSlope;
		double y = l1.getBlockY();
		double z = l1.getBlockZ();
		double interval = 1 / (Math.abs(ySlope) > Math.abs(zSlope) ? ySlope : zSlope);
		for (double x = l1.getBlockX(); x - l1.getBlockX() < Math.abs(xSlope); x += interval, y += ySlope, z += zSlope) {
			new Location(l1.getWorld(), x, y, z).getBlock().setType(material);
		}
	}

	public static String parseColors(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static boolean contains(char[] array, Character character) {
		for (Character c : array) {
			if (c == character)
				return true;
		}
		return false;
	}

	public static String getItemName(ItemStack is) {
		if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
			return is.getItemMeta().getDisplayName();
		}
		return null;
	}

	public static Permission getPermissions() {
		Permission permission = null;
		RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return permission;
	}

	public static boolean isIn(Location loc1, Location loc2, Location loc3) {
		int n = 0;
		if (loc2.getBlockX() > loc3.getBlockX() && loc1.getBlockX() >= loc3.getBlockX() && loc1.getBlockX() <= loc2.getBlockX()) {
			n++;
		} else if (loc1.getBlockX() >= loc2.getBlockX() && loc1.getBlockX() <= loc3.getBlockX()) {
			n++;
		}
		if (loc2.getBlockY() > loc3.getBlockY() && loc1.getBlockY() >= loc3.getBlockY() && loc1.getBlockY() <= loc2.getBlockY()) {
			n++;
		} else if (loc1.getBlockY() >= loc2.getBlockY() && loc1.getBlockY() <= loc3.getBlockY()) {
			n++;
		}
		if (loc2.getBlockZ() > loc3.getBlockZ() && loc1.getBlockZ() >= loc3.getBlockZ() && loc1.getBlockZ() <= loc2.getBlockZ()) {
			n++;
		} else if (loc1.getBlockZ() >= loc2.getBlockZ() && loc1.getBlockZ() <= loc3.getBlockZ()) {
			n++;
		}
		if (n == 3) {
			return true;
		} else {
			return false;
		}
	}

	public static ItemStack newItemMeta(Material material, String name, String lore, int amount) {
		name = parseColors(name);
		ItemStack is = new ItemStack(material, amount);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		if (lore != null) {
			String[] loreA = lore.split(",");
			List<String> loreL = new ArrayList<String>();
			for (String l : loreA) {
				l = parseColors(l);
				loreL.add(l);
			}
			im.setLore(loreL);
		}
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack newItemMeta(Material material, String name, String lore, int amount, short durability) {
		ItemStack is = newItemMeta(material, name, lore, amount);
		is.setDurability(durability);
		return is;
	}

	public static void initScoreboard() {
		try {
			Vars.scoreboard.registerNewTeam("Visitor").setPrefix(parseColors("&7"));
			Vars.scoreboard.registerNewTeam("Member").setPrefix(parseColors("&a"));
			Vars.scoreboard.registerNewTeam("MemberP").setPrefix(parseColors("&a"));
			Vars.scoreboard.registerNewTeam("Trusted").setPrefix(parseColors("&b"));
			Vars.scoreboard.registerNewTeam("Mod").setPrefix(parseColors("&9"));
			Vars.scoreboard.registerNewTeam("Developer").setPrefix(parseColors("&e"));
			Vars.scoreboard.registerNewTeam("Admin").setPrefix(parseColors("&c"));
			Vars.scoreboard.registerNewTeam("Owner").setPrefix(parseColors("&4"));
		} catch (Exception e) {
		}
		for (Team t : Vars.scoreboard.getTeams()) {
			Vars.teams.add(t);
		}
	}

	public static ChatColor getGroupColor(String group) {
		ChatColor color = ChatColor.WHITE;
		for (String s : Bukkit.getPluginManager().getPlugin("IZenith").getConfig().getStringList("colors")) {
			String[] sp = s.split(",");
			if (group.equalsIgnoreCase(sp[0])) {
				color = ChatColor.getByChar(sp[1].toCharArray()[0]);
				break;
			}
		}
		return color;
	}

	public static Main getMain() {
		return (Main) Vars.main;
	}

	public static Server getServer() {
		return getMain().getServer();
	}

	public static FileConfiguration getConfig() {
		return getMain().getConfig();
	}

	public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
		// get the main content part, this doesn't return the armor
		String content = itemStackArrayToBase64(playerInventory.getContents());
		String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

		return new String[] { content, armor };
	}

	public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			// Write the size of the inventory
			dataOutput.writeInt(items.length);

			// Save every element in the list
			for (int i = 0; i < items.length; i++) {
				dataOutput.writeObject(items[i]);
			}

			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack[] items = new ItemStack[dataInput.readInt()];

			// Read the serialized inventory
			for (int i = 0; i < items.length; i++) {
				items[i] = (ItemStack) dataInput.readObject();
			}

			dataInput.close();
			return items;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	public static void setGlobalKit(Player player, String name) {
		name = name.toLowerCase();
		String[] arrayKit = playerInventoryToBase64(player.getInventory());
		List<String> kit = new ArrayList<String>();
		kit.add(arrayKit[0]);
		kit.add(arrayKit[1]);
		getConfig().set("kits.global." + name, kit);
		getMain().saveConfig();
	}

	public static void removeGlobalKit(String name) {
		name = name.toLowerCase();
		getConfig().set("kits.global." + name, null);
		getMain().saveConfig();
	}

	public static boolean hasJoined(Player player) {
		File dataFolder = new File(Util.getMain().getDataFolder().getPath() + System.getProperty("file.separator") + "players");
		return new File(dataFolder.getPath() + System.getProperty("file.separator") + player.getUniqueId() + ".yml").exists();
	}

	public static boolean containsIgnoreCase(String message, String regex) {
		return message.toLowerCase().contains(regex.toLowerCase());
	}

	public static boolean startsWithIgnoreCase(String message, String regex) {
		return message.toLowerCase().startsWith(regex.toLowerCase());
	}

	public static void suspend(Player player) {
		getConfig().set("suspended." + player.getUniqueId().toString(), getPermissions().getPlayerGroups(player)[0]);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " group set suspended");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " add -*");
		player.sendMessage(ChatColor.RED + "You are now suspended and no longer have any permissions");
		Util.getMain().saveConfig();
	}

	public static void removeSuspend(Player player) {
		Util.getMain().reloadConfig();
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " group set " + getConfig().getString("suspended." + player.getUniqueId().toString()));
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " remove -*");
		getConfig().set("suspended." + player.getUniqueId().toString(), null);
		player.sendMessage(ChatColor.GREEN + "You are no longer suspended!");
		Util.getMain().saveConfig();
	}

	public static void setAllOnlineTimes() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			IPlayerHandler.getPlayer(player).setOnlineTime();
		}
	}

	public static void loadOnlineTime(Player player) {
		IPlayerHandler.getPlayer(player).joinTime = System.currentTimeMillis();
	}

	public static void loadAllOnlineTimes() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			loadOnlineTime(player);
		}
	}

	public static void updatePlayerList() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			IPlayer iPlayer = IPlayerHandler.getPlayer(player);
			iPlayer.setTeam();
			iPlayer.sendTabFootHeader();
		}
	}

	public static void sendAdminMessage(String message, Player sender) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission(new AdminChat().getPermission())) {
				player.sendMessage(ChatColor.WHITE + "Admin" + ChatColor.GOLD + "Chat " + sender.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.RED + message);
			}
		}
	}

	public static FileConfiguration getOfflinePlayerFile(String name) {
		for (File file : new File(Util.getMain().getDataFolder().getPath() + System.getProperty("file.separator") + "players").listFiles()) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			return config;
		}
		return null;
	}

	/*
	 * public static void convertFileSystem(){ for(String uuid :
	 * getConfig().getStringList("players")){ File dataFolder = new
	 * File(Util.getMain().getDataFolder().getPath() +
	 * System.getProperty("file.separator") + "players"); if
	 * (!dataFolder.exists()) dataFolder.mkdir(); File file = new
	 * File(dataFolder.getPath() + System.getProperty("file.separator") + uuid +
	 * ".yml"); try { file.createNewFile(); } catch (IOException e) {
	 * e.printStackTrace(); } YamlConfiguration config =
	 * YamlConfiguration.loadConfiguration(file); config.set("last_name",
	 * "unknown"); config.set("commandspy",
	 * getConfig().getString("commandspy.players." + uuid + ".filter"));
	 * config.set("kits", getConfig().get("kits." + uuid)); config.set("time",
	 * getConfig().getLong("times." + uuid)); try { config.save(file); } catch
	 * (IOException e) { e.printStackTrace(); } } }
	 */

	public static String buildString(Collection<String> args, String seperator, int startingArg, int maxLength) {
		List<String> retList = new ArrayList<String>();
		String ret = "";
		for (int i = startingArg; i < args.size(); i++) {
			String s = (String) args.toArray()[i];
			ret += s + seperator;
			if (ret.length() > maxLength) {
				retList.add(ret);
				ret = "";
			}
		}
		retList.add(ret);
		ret = "";
		for (String s : retList) {
			ret += s + "\n";
		}
		if (ret.length() > 2) {
			ret = ret.substring(0, ((ret.length() - 2) - seperator.length()) + 1);
		}
		return ret;
	}

	public static boolean isVerified(String key) {
		return getConfig().getStringList("console_keys").contains(key);
	}

	public static void addKey(String key) {
		List<String> keys = getConfig().getStringList("console_keys");
		if (keys == null)
			keys = new ArrayList<String>();
		keys.add(key);
		getConfig().set("console_keys", keys);
		getMain().saveConfig();
	}

	public static void removeKey(String key) {
		List<String> keys = getConfig().getStringList("console_keys");
		if (keys == null)
			return;
		keys.remove(key);
		getConfig().set("console_keys", keys);
		getMain().saveConfig();
	}

	public static String addRandomKey() {
		Random gen = new Random();
		int max = (int) ('z');
		int min = (int) ('a');
		String key = "";
		for (int i = 0; i < 15; i++) {
			int code = gen.nextInt(max - min);
			key += (char) (code + min);
		}
		addKey(key);
		return key;
	}

	public static boolean isURL(String s) {
		try {
			new URL(s);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	public static boolean isPlayerName(String s) {
		return Bukkit.getPlayer(s) != null;
	}

	/*
	 * public static ConnectionServer openRemoteConsoleServer(){ PacketHandler
	 * handler = new PacketHandler(); handler.addListener("key", new
	 * PacketListener(){
	 * 
	 * @Override public void packetReceived(Packet packet, Connection
	 * connection) {
	 * 
	 * } }); ConnectionServer server = null; try { server = new
	 * ConnectionServer(25566,handler); } catch (IOException e) {
	 * e.printStackTrace(); } }
	 */

}
