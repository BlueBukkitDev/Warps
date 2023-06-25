package dev.blue.warps;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class GUIListener implements Listener {
	private Main main;

	public GUIListener(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory() == null)
			return;
		Player p = (Player) e.getWhoClicked();
		ItemStack stack = e.getCurrentItem();
		if (stack == null)
			return;
		if (!StringUtils.containsIgnoreCase(e.getView().getTitle(), ChatColor.translateAlternateColorCodes('&',
				this.main.getConfig().getString("Warps-GUI-Name").replaceAll("%index%", ""))))
			return;
		if (this.main.getUtils().getDestPageIndex(stack) != -1) {
			e.setCancelled(true);
			new GUIPage(((Integer) stack.getItemMeta().getPersistentDataContainer().get((this.main.getUtils()).indexKey, PersistentDataType.INTEGER)).intValue(), p, this.main).openGUI();
		} else if (stack.getItemMeta().getPersistentDataContainer().has((this.main.getUtils()).warpKey,
				PersistentDataType.STRING)) {
			e.setCancelled(true);
			this.main.getUtils().getWarp(stack).warpPlayer(p);
		}
	}
}
