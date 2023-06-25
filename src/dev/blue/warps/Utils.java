package dev.blue.warps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class Utils {
	private Main main;

	public NamespacedKey indexKey;

	public NamespacedKey warpKey;

	private HashMap<Player, Long> warpers;

	public Utils(Main main) {
		this.main = main;
		this.indexKey = new NamespacedKey((Plugin) main, "warps.index");
		this.warpKey = new NamespacedKey((Plugin) main, "warps.warp");
		this.warpers = new HashMap<>();
	}

	public ItemStack next(int currentIndex) {
		String mat = this.main.getConfig().getString("GUI-Next-Page.Item");
		ItemStack stack = new ItemStack(Material.getMaterial(mat), 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				this.main.getConfig().getString("GUI-Next-Page.Name").replaceAll("%index%", "" + currentIndex + 1)));
		meta.setLore(this.main.getConfig().getStringList("GUI-Next-Page.Lore"));
		meta.getPersistentDataContainer().set(this.indexKey, PersistentDataType.INTEGER,
				Integer.valueOf(currentIndex + 1));
		stack.setItemMeta(meta);
		return stack;
	}

	public ItemStack previous(int currentIndex) {
		String mat = this.main.getConfig().getString("GUI-Previous-Page.Item");
		ItemStack stack = new ItemStack(Material.getMaterial(mat), 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.main.getConfig()
				.getString("GUI-Previous-Page.Name").replaceAll("%index%", "" + currentIndex + 1)));
		meta.setLore(this.main.getConfig().getStringList("GUI-Previous-Page.Lore"));
		meta.getPersistentDataContainer().set(this.indexKey, PersistentDataType.INTEGER,
				Integer.valueOf(currentIndex - 1));
		stack.setItemMeta(meta);
		return stack;
	}

	public int getDestPageIndex(ItemStack stack) {
		if (!stack.getItemMeta().getPersistentDataContainer().has(this.indexKey, PersistentDataType.INTEGER))
			return -1;
		return ((Integer) stack.getItemMeta().getPersistentDataContainer().get(this.indexKey,
				PersistentDataType.INTEGER)).intValue();
	}

	public Warp getWarp(ItemStack stack) {
		if (!stack.getItemMeta().getPersistentDataContainer().has(this.warpKey, PersistentDataType.STRING))
			return null;
		return new Warp(
				(String) stack.getItemMeta().getPersistentDataContainer().get(this.warpKey, PersistentDataType.STRING),
				this.main);
	}
	
	public List<Warp> getWarps(Player p) {
		List<Warp> warps = new ArrayList<>();
		for (String each : this.main.getWarps().getKeys(false)) {
			Warp warp = new Warp(each, this.main);
			if(warp.getCreator() == null || (warp.getCreator() != null && !warp.getCreator().equalsIgnoreCase(p.getUniqueId().toString()))) {
				continue;
			}
			warps.add(warp);
		}
		return warps;
	}
	
	public List<Warp> getUsableWarps(Player p){
		List<Warp> warps = new ArrayList<>();
		for (String each : this.main.getWarps().getKeys(false)) {
			Warp warp = new Warp(each, this.main);
			if((warp.getCreator() != null && warp.getCreator().equalsIgnoreCase(p.getUniqueId().toString()))||
					p.hasPermission("warp.use.*") || p.hasPermission("warp.use."+warp.getName())) {
				warps.add(warp);
			}
			warps.add(warp);
		}
		return warps;
	}

	public List<Warp> getWarps() {
		List<Warp> warps = new ArrayList<>();
		for (String each : this.main.getWarps().getKeys(false))
			warps.add(new Warp(each, this.main));
		return warps;
	}

	public boolean isReadyToWarp(Player p, long id) {
		if (!this.warpers.keySet().contains(p))
			return false;
		if (((Long) this.warpers.get(p)).longValue() == id)
			return true;
		return false;
	}

	public void preparePlayerToWarp(Player p, long id) {
		if (!this.warpers.keySet().contains(p))
			this.warpers.put(p, Long.valueOf(id));
	}

	public boolean cancelPreparePlayerForWarp(Player p) {
		if (this.warpers.keySet().contains(p)) {
			this.warpers.remove(p);
			return true;
		}
		return false;
	}

	public String formatItemName(String name) {
		String[] parts = name.split("_");
		String result = "";
		for (int i = 0; i < parts.length; i++) {
			if (i > 0 && i < parts.length) {
				result = String.valueOf(String.valueOf(result)) + " ";
			}
			result = String.valueOf(String.valueOf(result)) + StringUtils.capitalize(parts[i].toLowerCase());
		}
		return "§6" + result;
	}
}
