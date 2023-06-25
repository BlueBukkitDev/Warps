package dev.blue.warps;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FileBuilder {
	private Main main;

	public FileBuilder(Main main) {
		this.main = main;
	}

	public boolean canAddWarp(Location loc, String warp, Player creator) {
		String name = warp.toLowerCase();
		name.replaceAll(".", "");
		if (!this.main.getWarps().contains(name)) {
			this.main.getWarps().set(String.valueOf(String.valueOf(String.valueOf(name))) + ".Location", loc);
			this.main.getWarps().set(String.valueOf(String.valueOf(String.valueOf(name))) + ".Creator",
					creator.getUniqueId().toString());
			this.main.saveWarps();
			return true;
		}
		return false;
	}

	public boolean canDeleteWarp(String name) {
		if (this.main.getWarps().contains(name.toLowerCase())) {
			this.main.getWarps().set(name.toLowerCase(), null);
			this.main.saveWarps();
			return true;
		}
		return false;
	}

	public Warp getWarp(String name) {
		return new Warp(name, this.main);
	}

	public List<String> getWarps() {
		List<String> warps = new ArrayList<>();
		for (String each : this.main.getWarps().getKeys(false))
			warps.add(each);
		return warps;
	}

	public List<String> getWarpsByPermission(Player p) {
		List<String> warps = new ArrayList<>();
		for (String each : this.main.getWarps().getKeys(false)) {
			if (p.hasPermission("warp.use.*") || p.hasPermission("warp.use." + each.toLowerCase()) || p.isOp()
					|| (new Warp(each, this.main)).getCreator().equalsIgnoreCase(p.getUniqueId().toString())
					|| !this.main.usePermissions())
				warps.add(each);
		}
		return warps;
	}
}
