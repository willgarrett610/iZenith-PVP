package net.izenith.Shop;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.bobmandude9889.GUI.GUI;
import net.izenith.Main.Util;
import net.izenith.Main.Vars;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;

public class BuyShop {

	static int pageSize = 36;
	static int guiSize = 54;

	public static void openShop(Player player, String sectionName, Integer page) {
		GUI gui = new GUI(guiSize, Util.parseColors("&a&lItem Shop"), Vars.guiHandler);
		gui.open(player);
		setupGUI(player, sectionName, page, gui);
	}

	private static void clearGUI(GUI gui) {
		for (int i = 0; i < guiSize; i++) {
			gui.removeButton(i);
		}
	}

	private static void setupGUI(final Player player, final String sectionName, Integer page, final GUI gui) {
		List<MaterialData> section = SectionManager.sections.get(sectionName);
		int pages = (int) Math.floor((section.size() - 1) / pageSize) + 1;
		if (page >= pages)
			page = 0;
		if (page < 0)
			page = pages - 1;
		int start = page * pageSize;
		int end = (page + 1) * pageSize;
		for (int i = start; i < end && i < section.size(); i++) {
			final MaterialData item = section.get(i);
			Double price = PriceManager.getPrice(item.material, item.data);
			ItemInfo itemInfo = Items.itemByType(item.material, item.data);
			ItemStack itemStack = Util.newItemMeta(item.material, Util.parseColors("&b" + itemInfo.name + " &7(&a$" + Util.formatDouble(price, "#.##") + "&7)"), "&bClick to select amount.", 1, item.data);
			int slotNumber = i - start;
			gui.addButton(itemStack, slotNumber, new Runnable() {
				@Override
				public void run() {
					openAmountSelection(player, item);
				}
			});
		}

		final int currentPage = page;

		gui.addButton(Util.newItemMeta(Material.STAINED_GLASS_PANE, Util.parseColors("&f&lPrevious page"), null, 1), 36, new Runnable() {
			@Override
			public void run() {
				clearGUI(gui);
				setupGUI(player, sectionName, currentPage - 1, gui);
			}
		});

		gui.addButton(Util.newItemMeta(Material.STAINED_GLASS_PANE, Util.parseColors("&f&lNext page"), null, 1), 44, new Runnable() {
			@Override
			public void run() {
				clearGUI(gui);
				setupGUI(player, sectionName, currentPage + 1, gui);
			}
		});

		for (int i = 0; i < SectionManager.sectionNames.size(); i++) {
			final String sectionNameI = SectionManager.sectionNames.get(i);
			String index = sectionNameI.toLowerCase();
			Material material = SectionManager.sectionIcons.get(index);
			ItemStack item = Util.newItemMeta(material, sectionNameI, null, 1);
			if (sectionNameI.equalsIgnoreCase(sectionName))
				Util.enchant(item);
			gui.addButton(item, 45 + i, new Runnable() {
				@Override
				public void run() {
					clearGUI(gui);
					setupGUI(player, sectionNameI.toLowerCase(), 0, gui);
				}
			});
		}
		gui.updateInventory(player, Vars.guiHandler.getOpenInvs().get(player).inv);
	}

	public static void openAmountSelection(final Player player, final MaterialData item) {
		ItemInfo itemInfo = Items.itemByType(item.material, item.data);
		String title = Util.parseColors("&a&lBuying " + itemInfo.name);
		
		if(title.length() > 28)
			title = title.substring(0,28);
		
		final GUI gui = new GUI(27, title, Vars.guiHandler);
		gui.addButton(Util.newItemMeta(Material.STAINED_GLASS_PANE, "&a&lAdd 1", null, 1, (short) 5), 3, new Runnable() {
			@Override
			public void run() {
				int amount = getAmount(gui);
				amount++;
				if (amount > 64)
					amount = 64;
				if (amount < 1)
					amount = 1;
				setAmount(player, gui, item, amount);
			}
		});
		gui.addButton(Util.newItemMeta(Material.STAINED_GLASS_PANE, "&a&lAdd 10", null, 10, (short) 5), 4, new Runnable() {
			@Override
			public void run() {
				int amount = getAmount(gui);
				amount += 10;
				if (amount > 64)
					amount = 64;
				if (amount < 1)
					amount = 1;
				setAmount(player, gui, item, amount);
			}
		});
		gui.addButton(Util.newItemMeta(Material.STAINED_GLASS_PANE, "&a&lSet to 64", null, 64, (short) 5), 5, new Runnable() {
			@Override
			public void run() {
				setAmount(player, gui, item, 64);
			}
		});
		gui.addButton(Util.newItemMeta(Material.STAINED_GLASS_PANE, "&c&lRemove 1", null, 1, (short) 14), 21, new Runnable() {
			@Override
			public void run() {
				int amount = getAmount(gui);
				amount--;
				if (amount > 64)
					amount = 64;
				if (amount < 1)
					amount = 1;
				setAmount(player, gui, item, amount);
			}
		});
		gui.addButton(Util.newItemMeta(Material.STAINED_GLASS_PANE, "&c&lRemove 10", null, 10, (short) 14), 22, new Runnable() {
			@Override
			public void run() {
				int amount = getAmount(gui);
				amount -= 10;
				if (amount > 64)
					amount = 64;
				if (amount < 1)
					amount = 1;
				setAmount(player, gui, item, amount);
			}
		});
		gui.addButton(Util.newItemMeta(Material.STAINED_GLASS_PANE, "&c&lSet to 1", null, 1, (short) 14), 23, new Runnable() {
			@Override
			public void run() {
				setAmount(player, gui, item, 1);
			}
		});
		gui.addButton(Util.newItemMeta(Material.STAINED_GLASS, Util.parseColors("&c&lCancel"), null, 1, (short) 14), 10, new Runnable() {
			@Override
			public void run() {
				openShop(player, SectionManager.sectionNames.get(0).toLowerCase(), 0);
			}
		});
		setAmount(player,gui,item,1);
		gui.open(player);
	}

	private static int getAmount(GUI gui) {
		return gui.invs.get(13).getItem().getAmount();
	}

	private static void setAmount(final Player player, GUI gui, final MaterialData item, final int amount) {
		final double price = PriceManager.getPrice(item.material, item.data) * amount;
		gui.addButton(Util.newItemMeta(item.material, Util.parseColors("&b$" + Util.formatDouble(price,"#.##")), null, amount, item.data), 13, new Runnable() {
			@Override
			public void run() {
			}
		});
		gui.addButton(Util.newItemMeta(Material.STAINED_GLASS, Util.parseColors("&a&lBuy &7(&b$" + Util.formatDouble(price,"#.##") + "&7)"), null, 1, (short) 5), 16, new Runnable() {
			@Override
			public void run() {
				double price = PriceManager.getPrice(item.material, item.data) * amount;
				Economy econ = Util.getEconomy();
				if (econ.getBalance(player) >= price) {
					econ.withdrawPlayer(player, price);
					PlayerInventory inv = player.getInventory();
					inv.addItem(new ItemStack(item.material,amount,item.data));
					player.closeInventory();
					player.sendMessage(Util.parseColors("&a$" + Util.formatDouble(price, "#.##") + " &7was removed from your account."));
				} else {
					player.sendMessage(ChatColor.RED + "You do not have enough money!");
				}
			}
		});
		gui.updateInventory(player, Vars.guiHandler.getOpenInvs().get(player).inv);
	}

}
