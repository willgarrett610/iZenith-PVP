package net.izenith.Main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.earth2me.essentials.Essentials;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.izenith.Commands.AdminChat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

@SuppressWarnings("deprecation")
public class Util {

	public static Essentials ess;

	private static Economy econ;

	public static WorldGuardPlugin getWorldGuard() {
		return (WorldGuardPlugin) Util.getMain().getServer().getPluginManager().getPlugin("WorldGuard");
	}

	public static void LoadEssentials() {
		ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	}

	public static Economy getEconomy() {
		if (econ == null) {
			if (getServer().getPluginManager().getPlugin("Vault") == null) {
				return null;
			}
			RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
			if (rsp == null) {
				return null;
			}
			econ = rsp.getProvider();
		}
		return econ;
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

	public static boolean contains(String[] array, String regex) {
		for (String s : array) {
			if (s.equalsIgnoreCase(regex))
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

	public static void giveItem(Player player, ItemStack item){
		PlayerInventory inv = player.getInventory();
		for(int i = 0; i < inv.getSize(); i++){
			if(inv.getItem(i) == null){
				inv.setItem(i,item);
				return;
			}
		}
		player.getWorld().dropItem(player.getLocation(), item);
	}
	
	//	public static ItemStack enchant(ItemStack item, Player player){
	//		int ver = ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion();
	//	}

	public static void initScoreboard() {
		try {
			Vars.scoreboard.registerNewTeam("Member").setPrefix(parseColors("&7"));
			Vars.scoreboard.registerNewTeam("Iron").setPrefix(parseColors("&f"));
			Vars.scoreboard.registerNewTeam("Gold").setPrefix(parseColors("&6"));
			Vars.scoreboard.registerNewTeam("Emerald").setPrefix(parseColors("&a"));
			Vars.scoreboard.registerNewTeam("Diamond").setPrefix(parseColors("&b"));
			Vars.scoreboard.registerNewTeam("Admin").setPrefix(parseColors("&c"));
			Vars.scoreboard.registerNewTeam("Owner").setPrefix(parseColors("&4"));
			Team ghost = Vars.scoreboard.registerNewTeam("Ghost");
			ghost.setNameTagVisibility(NameTagVisibility.NEVER);
			ghost.setCanSeeFriendlyInvisibles(false);
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

	public static void saveConfig(){
		getMain().saveConfig();
	}
	
	public static void reloadConfig(){
		getMain().reloadConfig();
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
		for (File file : new File(Util.getMain().getDataFolder().getPath(), "players").listFiles()) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			if (config.getString("last_name").equalsIgnoreCase(name)) {
				return config;
			}
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
			ret = ret.substring(0, (ret.length() - seperator.length()) - 1);
		}
		return ret;
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

	public static String formatTime(long timeMili) {
		String suffix = "Miliseconds";
		double time = timeMili;
		if (time > 1000) {
			time /= 1000;
			suffix = "Seconds";
		}
		if (time > 60) {
			time /= 60;
			suffix = "Minutes";
		}
		if (time > 60) {
			time /= 60;
			suffix = "Hours";
		}

		DecimalFormat df = new DecimalFormat("#.#");
		return df.format(time) + " " + suffix;
	}

	public static void setupAutoBroadcast() {
		final List<String> messages = getConfig().getStringList("auto_messages");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(getMain(), new Runnable() {
			@Override
			public void run() {
				String message = messages.get(Vars.messageIndex);
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(parseColors("&8&m---------------------&f\u2606&4&liZenith&f&lPVP&f\u2606&8&m---------------------"));
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(parseColors(message));
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(parseColors("&8&m-----------------------------------------------------"));
				Bukkit.broadcastMessage("");
				Vars.messageIndex++;
				if (Vars.messageIndex == messages.size()) {
					Vars.messageIndex = 0;
				}
			}
		}, getConfig().getLong("broadcast_delay"), getConfig().getLong("broadcast_delay"));
	}

	//	public static void registerConv() {
	//		ConfigurationSection conv = getConfig().getConfigurationSection("block_conv");
	//		BlockRemapperControl remap_1_7_5 = ProtocolSupportAPI.getBlockRemapper(ProtocolVersion.MINECRAFT_1_7_5);
	//		BlockRemapperControl remap_1_7_10 = ProtocolSupportAPI.getBlockRemapper(ProtocolVersion.MINECRAFT_1_7_10);
	//		for (String from : conv.getKeys(false)) {
	//			String[] fromSplit = from.split(",");
	//			int idFrom = Integer.parseInt(fromSplit[0]);
	//			int dataFrom = Integer.parseInt(fromSplit[1]);
	//			String[] toSplit = conv.getString(from).split(",");
	//			int idTo = Integer.parseInt(toSplit[0]);
	//			int dataTo = Integer.parseInt(toSplit[1]);
	//			//			remap_1_7_5.setRemap(idFrom, dataFrom, idTo, dataTo);
	//			//			remap_1_7_10.setRemap(idFrom, dataFrom, idTo, dataTo);
	//		}
	//	}

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

	public static void enchant(ItemStack item) {
		item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 32);
	}

	private static void addGlow(ItemStack[] stacks) {
		for (ItemStack stack : stacks) {
			if (stack != null) {
				// Only update those stacks that have our flag enchantment
				if (stack.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 32) {
					NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(stack);
					compound.put(NbtFactory.ofList("ench"));
				}
			}
		}
	}

	public static String formatDouble(Double num){
		String s = num.toString();
		String[] split = s.split("\\.");
		String format = "";
		char[] chars = split[0].toCharArray();
		String append = "";
		for(int i = 0; i < chars.length; i++){
			append = chars[chars.length - (i+1)] + append;
			if((i + 1) % 3 == 0 || i == chars.length - 1){
				format = append + "," + format;
				append = "";
			}
		}
		return format.substring(0,format.length() - 1) + "." + split[1];
	}
	
	public static String formatDouble(Double num, String format){
		DecimalFormat df = new DecimalFormat(format);
		return df.format(num);
	}
	
}
