package net.izenith.Shop;

import org.bukkit.Material;

public class MaterialData {

	Material material;
	Short data;
	
	public MaterialData(Material material, Short data) {
		this.material = material;
		this.data = data;
	}

	@Override
	public String toString() {
		return material + " : " + data;
	}
	
}
