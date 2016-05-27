package net.izenith.CommandSpy;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import net.izenith.Main.PermissionHandler;

public class CommandFilter {

	List<String> accept;

	public CommandFilter(List<String> list) {
		accept = list;
	}

	public boolean canPass(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		try {
			String[] split = e.getMessage().split(" ");
			String command = split[0].substring(1, split[0].length());
			for (String s : accept) {
				if (s.equals("*"))
					return true;
				String arg = s.substring(2, s.length());
				switch (s.substring(0, 1)) {
				case "r":
					return arg.equalsIgnoreCase(PermissionHandler.getGroupName(p));
				case "p":
					return arg.equalsIgnoreCase(p.getName());
				case "c":
					return arg.equalsIgnoreCase(command);
				case "a":
					for (int i = 1; i < split.length; i++) {
						if (arg.equalsIgnoreCase(split[i]))
							return true;
					}
				}
			}
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean canPass(ServerCommandEvent e){
		Player p = (Player) e.getSender();
		try {
			String[] split = e.getCommand().split(" ");
			String command = split[0].substring(1, split[0].length());
			for (String s : accept) {
				if (s.equals("*"))
					return true;
				String arg = s.substring(2, s.length());
				switch (s.substring(0, 1)) {
				case "r":
					return arg.equalsIgnoreCase(PermissionHandler.getGroupName(p));
				case "p":
					return arg.equalsIgnoreCase(p.getName());
				case "c":
					return arg.equalsIgnoreCase(command);
				case "a":
					for (int i = 1; i < split.length; i++) {
						if (arg.equalsIgnoreCase(split[i]))
							return true;
					}
				}
			}
			return false;
		} catch (Exception ex) {
			return false;
		}
	}
	
}
