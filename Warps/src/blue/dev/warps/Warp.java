package blue.dev.warps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Warp {
	private static Location wl = null;
	private static String wname = null;

	public Warp(String name) {
		for (String each : FileBuilder.getWarps()) {
			if (each.equalsIgnoreCase(name)) {
				wname = name;
				wl = new Location(Bukkit.getWorld(UUID.fromString(Main.plugin.getWarps().getString(name + ".W"))),
					Main.plugin.getWarps().getDouble(name + ".X"), Main.plugin.getWarps().getDouble(name + ".Y"),
					Main.plugin.getWarps().getDouble(name + ".Z"), Main.plugin.getWarps().getInt(name + ".Yaw"),
					Main.plugin.getWarps().getInt(name + ".Pitch"));
			}
		}
	}

	public static List<Warp> getWarps() {
		List<Warp> warps = new ArrayList<Warp>();
		for (String each : Main.plugin.getWarps().getStringList("WarpList")) {
			warps.add(new Warp(each));
		}
		return warps;
	}

	public boolean exists() {
		if (wname != null) {
			return true;
		}
		return false;
	}

	public static List<String> warpNames() {
		List<String> warps = new ArrayList<String>();
		for (String each : Main.plugin.getWarps().getStringList("WarpList")) {
			warps.add(each);
		}
		return warps;
	}

	public void warpPlayer(Player p) {
		p.teleport(wl);
		if(Main.plugin.getConfig().getBoolean("UseTeleportSound")) {
			p.playSound(p.getLocation(), Sound.valueOf(Main.plugin.getConfig().getString("TeleportSound")), 1, 1);
		}
		p.sendMessage(new Msgs().WARP_MESSAGE(wname));
	}

	public Location getLocation() {
		return wl;
	}

	public String getName() {
		return wname;
	}
}
