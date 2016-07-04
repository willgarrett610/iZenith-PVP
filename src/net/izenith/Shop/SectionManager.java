package net.izenith.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class SectionManager {

	public static List<String> sectionNames;
	public static HashMap<String, List<MaterialData>> sections;
	public static HashMap<String, Material> sectionIcons;

	public static void init() {
		ConfigurationSection sectionList = ShopManager.config.getConfigurationSection("sections");
		
		sectionNames = new ArrayList<String>();
		for(String name : sectionList.getKeys(false)){
			sectionNames.add(name);
		}
		sections = new HashMap<String, List<MaterialData>>();
		sectionIcons = new HashMap<String, Material>();
		
		for (String sectionName : sectionNames) {
			sections.put(sectionName.toLowerCase(), new ArrayList<MaterialData>());
			String materialName = sectionList.getString(sectionName);
			sectionIcons.put(sectionName.toLowerCase(), Material.getMaterial(materialName));
		}
	}

}
