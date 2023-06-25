package blue.dev.warps;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIListener implements Listener {
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory() != null) {
			Player p = (Player) e.getWhoClicked();
			String name = ChatColor.translateAlternateColorCodes('&',
					Main.plugin.getConfig().getString("Warps-GUI-Name").replaceAll("%index%", ""));
			if (e.getInventory().getName().contains(name)) {
				e.setCancelled(true);
				if (e.getCurrentItem() != null) {
					ItemStack stack = e.getCurrentItem();
					if ((stack != null) && (GUIPage.warpers.containsKey(p))) {
						GUIPage page = (GUIPage) GUIPage.warpers.get(p);
						if (stack.isSimilar(next(page.getIndex()))) {
							new GUIPage(page.getIndex() + 1, p).openGUI();
						} else if (stack.isSimilar(previous(page.getIndex()))) {
							new GUIPage(page.getIndex() - 1, p).openGUI();
						} else {
							for (String each : FileBuilder.getWarps()) {
								if ((stack.hasItemMeta()) && (stack.getItemMeta().hasDisplayName()) && (ChatColor
										.stripColor(stack.getItemMeta().getDisplayName()).equalsIgnoreCase(each))) {
									new Warp(each).warpPlayer(p);
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	public void onCloseGUI(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (GUIPage.warpers.containsKey(p)) {
			GUIPage.warpers.remove(p);
		}
	}

	public ItemStack next(int currentIndex) {
		String mat = Main.plugin.getConfig().getString("GUI-Next-Page.Item");
		String[] parts = mat.split(":");
		int q = 1;
		String type = parts[0];
		if (parts.length == 2) {
			q = Integer.parseInt(parts[1]);
		}
		ItemStack stack = new ItemStack(Material.getMaterial(type), 1, (byte) q);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				Main.plugin.getConfig().getString("GUI-Next-Page.Name").replaceAll("%index%", "" + currentIndex + 1)));
		List<String> lore = new ArrayList<String>();
		for (String each : Main.plugin.getConfig().getStringList("GUI-Next-Page.Lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&',
					ChatColor.translateAlternateColorCodes('&', each.replaceAll("%index%", "" + currentIndex + 1))));
		}
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}

	public ItemStack previous(int currentIndex) {
		String mat = Main.plugin.getConfig().getString("GUI-Previous-Page.Item");
		String[] parts = mat.split(":");
		int q = 1;
		String type = parts[0];
		if (parts.length == 2) {
			q = Integer.parseInt(parts[1]);
		}
		ItemStack stack = new ItemStack(Material.getMaterial(type), 1, (byte) q);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig()
				.getString("GUI-Previous-Page.Name").replaceAll("%index%", "" + currentIndex + 1)));
		List<String> lore = new ArrayList<String>();
		for (String each : Main.plugin.getConfig().getStringList("GUI-Previous-Page.Lore")) {
			lore.add(ChatColor.translateAlternateColorCodes('&',
					ChatColor.translateAlternateColorCodes('&', each.replaceAll("%index%", "" + currentIndex + 1))));
		}
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
}
