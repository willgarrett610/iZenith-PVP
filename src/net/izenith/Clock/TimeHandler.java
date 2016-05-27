package net.izenith.Clock;

import java.util.Calendar;

import org.bukkit.Location;
import org.bukkit.Material;

import net.izenith.Main.Util;
import net.izenith.Main.Vars;

public class TimeHandler {

	public TimeHandler() {
		// TODO finish and comment
		Vars.main.getServer().getScheduler().scheduleSyncRepeatingTask(Vars.main, new Runnable() {
			@Override
			public void run() {
				if (Vars.clocks != null) {
					for (String c : Vars.clocks) {
						String[] cSplit = c.split(",");
						Location center = new Location(Vars.main.getServer().getWorld(cSplit[1]), Double.parseDouble(cSplit[2]), Double.parseDouble(cSplit[3]), Double.parseDouble(cSplit[4]));
						Material centerMaterial = center.getBlock().getType();
						String direction = cSplit[0];
						Calendar now = Calendar.getInstance();
						int hour = now.get(Calendar.HOUR_OF_DAY) % 12;
						int minute = now.get(Calendar.MINUTE);
						double hourX = (Math.sin(hour * 30) * 3);
						double hourY = (Math.cos(hour * 30) * 3);
						double minuteX = (Math.sin(minute * 6) * 4);
						double minuteY = (Math.cos(minute * 6) * 4);
						Location endHour = center;
						Location endMinute = center;
						if (direction.equals("NORTH") || direction.equals("SOUTH")) {
							endHour = new Location(center.getWorld(), center.getBlockX() + hourX, center.getBlockY() + hourY, center.getBlockZ());
							endMinute = new Location(center.getWorld(), center.getBlockX() + minuteX, center.getBlockY() + minuteY, center.getBlockZ());
							for (int x = -4; x < 5; x++) {
								for (int y = -4; y < 5; y++) {
									Location loc = new Location(center.getWorld(), center.getBlockX() + x, center.getBlockY() + y, center.getBlockZ());
									if (loc.getBlock().getType().equals(Material.COBBLE_WALL)) {
										loc.getBlock().setType(Material.AIR);
									}
								}
							}
						} else {
							endHour = new Location(center.getWorld(), center.getBlockX(), center.getBlockY() + hourY, center.getBlockZ() + hourX);
							endMinute = new Location(center.getWorld(), center.getBlockX(), center.getBlockY() + minuteY, center.getBlockZ() + minuteX);
							for (int x = -4; x < 5; x++) {
								for (int y = -4; y < 5; y++) {
									Location loc = new Location(center.getWorld(), center.getBlockX(), center.getBlockY() + y, center.getBlockZ() + x);
									if (loc.getBlock().getType().equals(Material.COBBLE_WALL)) {
										loc.getBlock().setType(Material.AIR);
									}
								}
							}
						}
						Util.line(center, endHour, Material.COBBLE_WALL);
						Util.line(center, endMinute, Material.COBBLE_WALL);
						center.getBlock().setType(centerMaterial);
					}
				}
			}
		}, 0l, 20l);
	}

}
