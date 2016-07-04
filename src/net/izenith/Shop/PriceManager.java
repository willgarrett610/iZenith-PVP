package net.izenith.Shop;

import java.util.HashMap;

import org.bukkit.Material;

public class PriceManager {

	protected static HashMap<Material, MaterialPrice> priceMap;
	
	public static void init() {
		priceMap = new HashMap<Material, MaterialPrice>();
	}
	
	public static double getPrice(Material material){
		if(priceMap.containsKey(material)){
			return priceMap.get(material).getPrice((short) 0);
		}
		return -1;
	}

	public static double getPrice(Material material, Short data){
		if(priceMap.containsKey(material)){
			return priceMap.get(material).getPrice(data);
		}
		return -1;
	}
	
	public static boolean hasPrice(Material material){
		return getPrice(material) > -1;
	}
	
}
