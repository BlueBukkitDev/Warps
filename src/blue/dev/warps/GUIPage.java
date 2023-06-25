package blue.dev.warps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIPage {
	private int page = 0;
	private Inventory gui;
	private Player p;
	public static HashMap<Player, GUIPage> warpers = new HashMap<Player, GUIPage>();

	public GUIPage(int pageNumber, Player p) {
		this.page = pageNumber;
		this.p = p;
		this.gui = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Warps-GUI-Name").replaceAll("%index%", "" + (this.page + 1))));
		addItemsToInventory();
	}

	public Inventory getGUI() {
		return gui;
	}

	public int getIndex() {
		return page;
	}

	public void openGUI() {
		p.openInventory(this.gui);
		if(Main.plugin.getConfig().getBoolean("UseGUISound")) {
			p.playSound(p.getLocation(), Sound.valueOf(Main.plugin.getConfig().getString("GUISound")), 0.5F, 1.0F);
		}
		warpers.put(p, this);
	}

	private void addItemsToInventory() {
		int slot = 9;
		if (FileBuilder.getWarps(p).size() - page * 27 > 27) {
			gui.setItem(8, new GUIListener().next(page));
		}
		if (page > 0) {
			gui.setItem(0, new GUIListener().previous(page));
		}
		for (int i = page * 27; (i < FileBuilder.getWarps(this.p).size()) && (i < (page + 1) * 27); i++) {
			gui.setItem(slot, warpItem((String) FileBuilder.getWarps(p).get(i)));
			slot++;
		}
	}

	private ItemStack warpItem(String warp) {
		ItemStack stack = null;
		for (String each : FileBuilder.getWarps()) {
			if (each.equalsIgnoreCase(warp)) {
				String configMat = Main.plugin.getConfig().getString("Default-GUI-Warp-Item");
				String[] matArray = configMat.split(":");
				String mat = matArray[0].replaceAll(":", "");
				int data = Integer.parseInt(matArray[1].replaceAll(":", ""));
				stack = new ItemStack(Material.getMaterial(mat), 1, (byte) data);
				ItemMeta meta = stack.getItemMeta();
				meta.setDisplayName(ChatColor.GREEN + StringUtils.capitalize(warp));
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.AQUA + "Click here to warp");
				lore.add(ChatColor.AQUA + "to " + StringUtils.capitalize(warp) + "!");
				meta.setLore(lore);
				stack.setItemMeta(meta);
				if (Main.plugin.getWarps().contains(warp + ".GUI-Item")) {
					String wMat = Main.plugin.getWarps().getString(warp + ".GUI-Item");
					String[] wmatArray = wMat.split(":");
					String wmat = wmatArray[0].replaceAll(":", "");
					int wdata = Integer.parseInt(wmatArray[1].replaceAll(":", ""));
					stack.setType(Material.getMaterial(wmat));
					stack.setDurability((byte) wdata);
				}
			}
		}
		return stack;
	}
}
