package net.izenith.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.md_5.bungee.api.ChatColor;

public class WorldEditPerms implements HubCommand{

	String[] perms = {
			"worldedit.help"
	        , "worldedit.biome.info"
	        , "worldedit.biome.set"
	        , "worldedit.biome.list"
	        , "worldedit.chunkinfo"
	        , "worldedit.listchunks"
	        , "worldedit.delchunks"
	        , "worldedit.clipboard.cut"
	        , "worldedit.clipboard.paste"
	        , "worldedit.clipboard.copy"
	        , "worldedit.clipboard.flip"
	        , "worldedit.clipboard.rotate"
	        , "worldedit.generation.cylinder"
	        , "worldedit.generation.cylinder"
	        , "worldedit.generation.sphere"
	        , "worldedit.generation.sphere"
	        , "worldedit.generation.forest"
	        , "worldedit.generation.pumpkins"
	        , "worldedit.generation.pyramid"
	        , "worldedit.generation.pyramid"
	        , "worldedit.history.undo"
	        , "worldedit.navigation.unstuck"
	        , "worldedit.history.clear"
	        , "worldedit.navigation.ascend"
	        , "worldedit.navigation.descend"
	        , "worldedit.navigation.ceiling"
	        , "worldedit.navigation.thru.command"
	        , "worldedit.navigation.jumpto.command"
	        , "worldedit.navigation.up"
	        , "worldedit.region.hollow"
	        , "worldedit.region.line"
	        , "worldedit.region.curve"
	        , "worldedit.region.overlay"
	        , "worldedit.region.center"
	        , "worldedit.region.naturalize"
	        , "worldedit.region.walls"
	        , "worldedit.region.faces"
	        , "worldedit.region.smooth"
	        , "worldedit.region.move"
	        , "worldedit.region.forest"
	        , "worldedit.region.replace"
	        , "worldedit.region.stack"
	        , "worldedit.region.set"
	        , "worldedit.selection.pos"
	        , "worldedit.selection.chunk"
	        , "worldedit.selection.pos"
	        , "worldedit.selection.hpos"
	        , "worldedit.selection.hpos"
	        , "worldedit.wand"
	        , "worldedit.wand.toggle"
	        , "worldedit.selection.outset"
	        , "worldedit.selection.contract"
	        , "worldedit.selection.inset"
	        , "worldedit.analysis.count"
	        , "worldedit.selection.size"
	        , "worldedit.selection.expand"
	        , "worldedit.selection.shift"
	        , "worldedit.tool.tree"
	        , "worldedit.tool.replacer"
	        , "worldedit.tool.data,cycler"
	        , "worldedit.tool.flood,fill"
	        , "worldedit.brush.sphere"
	        , "worldedit.brush.cylinder"
	        , "worldedit.brush.clipboard"
	        , "worldedit.brush.smooth"
	        , "worldedit.brush.ex"
	        , "worldedit.brush.gravity"
	        , "worldedit.tool.deltree"
	        , "worldedit.tool.farwand"
	        , "worldedit.tool.lrbuild"
	        , "worldedit.tool.info"
	        , "worldedit.brush.options.material"
	        , "worldedit.brush.options.range"
	        , "worldedit.brush.options.size"
	        , "worldedit.brush.options.mask"
	        , "worldedit.none"
	        , "worldedit.tool.tree"
	        , "worldedit.tool.replacer"
	        , "worldedit.tool.data,cycler"
	        , "worldedit.tool.flood,fill"
	        , "worldedit.drain"
	        , "worldedit.fixlava"
	        , "worldedit.fixwater"
	        , "worldedit.removeabove"
	        , "worldedit.removebelow"
	        , "worldedit.snow"
	        , "worldedit.green"
	        , "worldedit.extinguish"
	        , "worldedit.fill"
	};
	
	@Override
	public String getName() {
		return "worldeditperms";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player){
			sender.sendMessage(ChatColor.RED + "This command can only be used from the console.");
			return;
		}
		Player player = Bukkit.getPlayer(args[0]);
		if(player != null){
			for(String perm : perms){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " add " + perm + " plots");
			}
		}
	}

	@Override
	public boolean onlyPlayers() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPermission() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Permission getPermission() {
		// TODO Auto-generated method stub
		return null;
	}

}
