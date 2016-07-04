package net.izenith.Kit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.bobmandude9889.GUI.GUI;
import net.izenith.Main.IPlayer;
import net.izenith.Main.IPlayerHandler;
import net.izenith.Main.Util;
import net.izenith.Main.Vars;

public class KitManager {

	static HashMap<String, File> kitFiles;
	static HashMap<String, YamlConfiguration> configs;
	static HashMap<String, List<Kit>> kitMap;
	static List<String> worlds;
	static File dataFolder;
	static HashMap<String, File> worldFolders;

	public static void init() {
		kitMap = new HashMap<String, List<Kit>>();

		worlds = new ArrayList<String>();

		for (World world : Bukkit.getWorlds()) {
			String name = world.getName();
			name = name.split("_")[0];
			if (!kitMap.containsKey(name)) {
				kitMap.put(name, new ArrayList<Kit>());
				worlds.add(name);
			}
		}

		System.out.println("Worlds for kits: " + worlds);
		
		dataFolder = new File(Util.getMain().getDataFolder().getPath(), "kits");
		if (!dataFolder.exists())
			dataFolder.mkdir();

		kitFiles = new HashMap<String, File>();
		configs = new HashMap<String, YamlConfiguration>();
		worldFolders = new HashMap<String,File>();
		
		for (String world : worlds) {
			File worldFolder = new File(dataFolder.getPath(), world);
			if (!worldFolder.exists())
				worldFolder.mkdir();

			worldFolders.put(world, worldFolder);
			
			File kitFile = new File(worldFolder, "kits.yml");

			try {
				kitFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			YamlConfiguration config = YamlConfiguration.loadConfiguration(kitFile);

			kitFiles.put(world, kitFile);
			configs.put(world, config);
		}
	}

	@SuppressWarnings("deprecation")
	public static void loadKits() {
		for (String world : worlds) {
			File file = kitFiles.get(world);
			File folder = worldFolders.get(world);
			YamlConfiguration config = configs.get(world);
			for (String name : config.getKeys(false)) {
				System.out.println("Loading " + name + " into " + world + " from file " + file.getPath());
				String title = config.getString(name + ".title");
				String lore = config.getString(name + ".info");
				int id = config.getInt(name + ".item");
				short data = (short) config.getInt(name + ".data");
				ItemStack item = Util.newItemMeta(Material.getMaterial(id), Util.parseColors(title), Util.parseColors(lore), 1, data);
				if (config.getBoolean(name + ".glow"))
					Util.enchant(item);
				Kit kit = new Kit(world,name, item, new File(folder,name));
				kit.loadContents();
				List<PotionEffect> effects = new ArrayList<PotionEffect>();
				List<String> rawEffects = config.getStringList(name + ".effects");
				System.out.println("Effects for " + name + ": " + rawEffects);
				if (rawEffects != null) {
					for (String rawEffect : rawEffects) {
						String[] effectDat = rawEffect.split(",");
						PotionEffect effect = new PotionEffect(PotionEffectType.getByName(effectDat[0]), 99999, Integer.parseInt(effectDat[1]));
						effects.add(effect);
					}
					kit.setEffects(effects);
				}
				kit.setCooldown(config.getLong(name + ".cooldown"));
				kitMap.get(world).add(kit);
			}
		}
	}

	public static void setKit(String world, String name, ItemStack[][] contents) {
		for (Kit kit : kitMap.get(world)) {
			if (kit.name.equals(name)) {
				kit.setContents(contents);
				break;
			}
		}
	}

	public static void createKit(String world, String name, String title, String info, int id, short data, ItemStack[][] contents) {
		YamlConfiguration config = configs.get(world);
		File kitFile = kitFiles.get(world);
		File folder = worldFolders.get(world);
		@SuppressWarnings("deprecation")
		Kit kit = new Kit(world,name, Util.newItemMeta(Material.getMaterial(id), Util.parseColors(title), Util.parseColors(info), 1, data), new File(folder,name));
		kit.setContents(contents);
		config.createSection(name);
		ConfigurationSection sect = config.getConfigurationSection(name);
		sect.set("title", title);
		sect.set("info", info);
		sect.set("item", id);
		sect.set("data", data);
		sect.set("effects", new ArrayList<String>());
		sect.set("cooldown", 0);
		sect.set("glow", false);
		try {
			config.save(kitFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadKits();
	}

	public static Kit getKit(String world, String name) {
		for (Kit kit : kitMap.get(world)) {
			if (kit.name.equals(name)) {
				return kit;
			}
		}
		return null;
	}

	public static void clearKit(Player player) {
		PlayerInventory inv = player.getInventory();
		inv.clear();
		inv.setArmorContents(new ItemStack[4]);

		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}

		IPlayer iPlayer = IPlayerHandler.getPlayer(player);
		if (iPlayer.isGhost()) {
			iPlayer.setGhost(false);
			iPlayer.setTeam();
		}
		iPlayer.gotKit = false;
	}

	public static void openGUI(Player player) {
		final String world = player.getWorld().getName().split("_")[0];
		final GUI kitGUI = new GUI(9, Util.parseColors(Util.getConfig().getString("kit_title")), Vars.guiHandler);
		List<Kit> kits = kitMap.get(world);
		for (int i = 0; i < kits.size(); i++) {
			final Kit kit = kits.get(i);

			ItemStack item = kit.guiItem.clone();
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();

			if (!player.hasPermission("kit." + kit.name)) {
				lore.add(Util.parseColors("&4&lYou are not allowed to use this kit."));
				lore.add(Util.parseColors("&4&lYou can purchase this and other kits"));
				lore.add(Util.parseColors("&4&lat &b&lstore.izenith.net"));
			}

			long lastUse = IPlayerHandler.getPlayer(player).getLastUse(world,kit);
			String nextUseTime = Util.formatTime(lastUse + (kit.cooldown * 1000) - System.currentTimeMillis());
			if (System.currentTimeMillis() - lastUse < kit.cooldown * 1000) {
				lore.add(Util.parseColors("&4&lYou can use this kit again in &b&l" + nextUseTime));
			}

			meta.setLore(lore);
			item.setItemMeta(meta);

			kitGUI.addButton(item, i, new Runnable() {
				@Override
				public void run() {
					Player player = kitGUI.getWhoClicked();
					kit.applyKit(player,world.equals("kitpvp"));
					kitGUI.close(player);
				}
			});
		}
		kitGUI.open(player);
	}

}
