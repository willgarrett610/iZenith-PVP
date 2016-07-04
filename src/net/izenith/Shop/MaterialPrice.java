package net.izenith.Shop;

import java.util.HashMap;

public class MaterialPrice {

	HashMap<Short, Double> prices;

	public MaterialPrice() {
		prices = new HashMap<Short, Double>();
	}

	public void setPrice(short data, Double price) {
		prices.remove(data);
		prices.put(data, price);
	}

	public double getPrice(short data) {
		return prices.getOrDefault(data, prices.get(0));
	}

}
