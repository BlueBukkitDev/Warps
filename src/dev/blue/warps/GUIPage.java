package dev.blue.warps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GUIPage {
	private int page = 0;

	private Inventory gui;

	private Main main;

	private Player p;

	public GUIPage(int pageNumber, Player p, Main main) {
		this.main = main;
		this.page = pageNumber;
		this.p = p;
		this.gui = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Warps-GUI-Name").replaceAll("%index%", "" + this.page + 1)));
		addItemsToInventory();
	}

	public Inventory getGUI() {
		return this.gui;
	}

	public int getIndex() {
		return this.page;
	}

	public void openGUI() {
		this.p.openInventory(this.gui);
		this.p.playSound(this.p.getLocation(), Sound.valueOf(this.main.getConfig().getString("Open-Inv-Sound")), 0.5F,
				1.0F);
	}

	private void addItemsToInventory() {
		int slot = 9;
		if (this.main.getFileBuilder().getWarpsByPermission(this.p).size() - this.page * 27 > 27)
			this.gui.setItem(8, this.main.getUtils().next(this.page));
		if (this.page > 0)
			this.gui.setItem(0, this.main.getUtils().previous(this.page));
		for (int i = this.page * 27; i < this.main.getFileBuilder().getWarpsByPermission(this.p).size()
				&& i < (this.page + 1) * 27; i++) {
			this.gui.setItem(slot,
					(new Warp(this.main.getFileBuilder().getWarpsByPermission(this.p).get(i), this.main)).getGUIItem());
			slot++;
		}
	}
}
