package net.izenith.Shop;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.izenith.Main.Util;
import net.md_5.bungee.api.ChatColor;

public class ShopManager implements Listener {

	private static HashMap<Player, Inventory> openSelling;

	private static List<Material> denySell;

	private static String sellTitle = "&4Drag items in to sell.";

	private static File dataFolder;

	private static File priceFile;
	public static YamlConfiguration priceConfig;

	private static File configFile;
	public static YamlConfiguration config;

	public ShopManager() {
		init();
	}

	public static void init() {
		dataFolder = new File(Util.getMain().getDataFolder(), "shop");

		priceFile = new File(dataFolder, "prices.yml");
		priceConfig = YamlConfiguration.loadConfiguration(priceFile);

		configFile = new File(dataFolder, "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);

		openSelling = new HashMap<Player, Inventory>();
		denySell = new ArrayList<Material>();

		for (String materialName : config.getStringList("deny_sell")) {
			denySell.add(Material.getMaterial(materialName));
		}

		SectionManager.init();
		PriceManager.init();

		for (String materialName : ShopManager.priceConfig.getKeys(false)) {
			ConfigurationSection itemSect = ShopManager.priceConfig.getConfigurationSection(materialName);
			Double price = itemSect.getDouble("price");
			String section = itemSect.getString("section");
			List<Short> dataList = itemSect.getShortList("data");
			if (dataList.size() == 0) 
				dataList.add((short) 0);

			Material material = Material.getMaterial(materialName);
			MaterialPrice materialPrice = PriceManager.priceMap.get(material);
			for (Short data : dataList) {
				if (materialPrice == null) {
					materialPrice = new MaterialPrice();
				}
				materialPrice.setPrice(data, price);
				MaterialData item = new MaterialData(material, data);
				SectionManager.sections.get(section).add(item);
			}
			PriceManager.priceMap.put(material, materialPrice);
		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(Util.getMain(), new Runnable() {
			@Override
			public void run() {
				for (Player player : openSelling.keySet()) {
					updateSellItem(player);
				}
			}
		}, 40l, 40l);
	}

	public static void updateSellItem(final Player player) {

		Bukkit.getScheduler().scheduleSyncDelayedTask(Util.getMain(), new Runnable() {
			@Override
			public void run() {
				Inventory inv = openSelling.get(player);
				if (!containsDenySell(inv.getContents())) {
					double price = getPrice(inv.getContents());
					DecimalFormat df = new DecimalFormat("#.##");

					inv.setItem(35, Util.newItemMeta(Material.STAINED_GLASS_PANE, "&7Sell for &a$" + df.format(price), null, 1, (short) 5));
				} else {
					List<String> denySellNames = new ArrayList<String>();
					for (Material material : getDenySell(inv.getContents())) {
						denySellNames.add(ChatColor.RED + material.name());
					}
					inv.setItem(35, Util.newItemMeta(Material.STAINED_GLASS_PANE, "&cThe following items cannot be sold or do not have a price set!", Util.buildString(denySellNames, ",", 0, 10), 1, (short) 5));
				}
			}
		});

	}

	public static void openSell(Player player) {
		Inventory sellInv = Bukkit.createInventory(player, 36, Util.parseColors(sellTitle));

		sellInv.setItem(35, Util.newItemMeta(Material.STAINED_GLASS_PANE, "&7Sell for &a$0", null, 1, (short) 5));
		openSelling.put(player, sellInv);
		player.openInventory(sellInv);
	}

	public static double getPrice(ItemStack[] contents) {
		double total = 0;
		for (int i = 0; i < contents.length; i++) {
			ItemStack item = contents[i];
			if (item != null) {
				if (item.getType().equals(Material.STAINED_GLASS_PANE) && (i + 1) == contents.length)
					break;
				else {
					if (!denySell.contains(item.getType())) {
						total += (PriceManager.getPrice(item.getType(), item.getDurability()) * item.getAmount()) / 2;
					}
				}
			}
		}
		return total;
	}

	public static boolean containsDenySell(ItemStack[] contents) {
		if (contents.length == 36)
			contents[35] = null;
		for (ItemStack item : contents) {
			if (item != null && (denySell.contains(item.getType()) || !PriceManager.hasPrice(item.getType())))
				return true;
		}
		return false;
	}

	public static List<Material> getDenySell(ItemStack[] contents) {
		if (contents.length == 36)
			contents[35] = null;
		List<Material> items = new ArrayList<Material>();
		for (ItemStack item : contents) {
			if (item != null && (denySell.contains(item.getType()) || !PriceManager.hasPrice(item.getType()))) {
				if (!items.contains(item.getType()))
					items.add(item.getType());
			}
		}
		return items;
	}

	public static boolean isPickup(InventoryAction a) {
		return a.equals(InventoryAction.PICKUP_ALL) || a.equals(InventoryAction.PICKUP_HALF) || a.equals(InventoryAction.PICKUP_ONE) || a.equals(InventoryAction.PICKUP_SOME);
	}

	public static int itemAmount(ItemStack[] contents) {
		int count = 0;
		for (ItemStack item : contents) {
			if (item != null) {
				count++;
			}
		}
		return count;
	}

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		final Player player = (Player) e.getWhoClicked();
		if (openSelling.containsKey(player)) {

			if (isPickup(e.getAction())) {
				if (e.getInventory().getSize() == 36 && e.getSlot() == 35) {
					e.setCancelled(true);
					if (!containsDenySell(e.getInventory().getContents())) {
						double price = getPrice(e.getInventory().getContents());
						DecimalFormat df = new DecimalFormat("#.##");
						openSelling.get(player).clear();
						openSelling.remove(player);
						player.closeInventory();
						Util.getEconomy().depositPlayer(player, price);
						player.sendMessage(Util.parseColors("&7You sold your items for &a$" + df.format(price) + "&7!"));
					}
				}
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		final Player player = (Player) e.getPlayer();
		if (openSelling.containsKey(player)) {
			Inventory inv = e.getInventory();
			final ItemStack[] contents = inv.getContents();
			int count = itemAmount(contents);
			if (count > 1) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(Util.getMain(), new Runnable() {
					@Override
					public void run() {
						openSell(player);
						openSelling.get(player).setContents(contents);
						player.sendMessage(ChatColor.RED + "Please click the sell button or remove your items before closing!");
					}
				});
			} else {
				openSelling.remove(player);
			}
		}
	}

}
