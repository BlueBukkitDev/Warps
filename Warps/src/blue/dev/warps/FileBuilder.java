package blue.dev.warps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FileBuilder
{
  public boolean canAddWarp(Location loc, String warp)
  {
    String name = warp.toLowerCase();
    if (!Main.plugin.getWarps().contains(name))
    {
      Main.plugin.getWarps().set(name + ".W", loc.getWorld().getUID().toString());
      Main.plugin.getWarps().set(name + ".X", Double.valueOf(loc.getBlockX() + 0.5D));
      Main.plugin.getWarps().set(name + ".Y", Double.valueOf(loc.getBlockY() + 0.25D));
      Main.plugin.getWarps().set(name + ".Z", Double.valueOf(loc.getBlockZ() + 0.5D));
      Main.plugin.getWarps().set(name + ".Yaw", Integer.valueOf(Math.round(loc.getYaw())));
      Main.plugin.getWarps().set(name + ".Pitch", Integer.valueOf(Math.round(loc.getPitch())));
      List<String> warps = new ArrayList<String>();
      for (String each : getWarps()) {
        warps.add(each);
      }
      warps.add(name);
      Main.plugin.getWarps().set("WarpList", warps);
      Main.plugin.saveWarps();
      return true;
    }
    return false;
  }
  
  public boolean canDeleteWarp(String name)
  {
    if (Main.plugin.getWarps().contains(name))
    {
      Main.plugin.getWarps().set(name, null);
      List<String> warps = new ArrayList<String>();
      for (String each : getWarps()) {
        warps.add(each);
      }
      warps.remove(name);
      Main.plugin.getWarps().set("WarpList", warps);
      Main.plugin.saveWarps();
      return true;
    }
    return false;
  }
  
  public void addWarpSign(Location l, String warp)
  {
    Main.plugin.getSigns().set(getStringFromLoc(l), warp);
    Main.plugin.saveSigns();
  }
  
  public void removeWarpSign(Location l)
  {
    Main.plugin.getSigns().set(getStringFromLoc(l), null);
    Main.plugin.saveSigns();
  }
  
  public Location getLocFromString(String s)
  {
    String[] parts = s.split(":");
    String w = parts[0].replaceAll("|", "");
    String xs = parts[1].replaceAll("|", "");
    String ys = parts[2].replaceAll("|", "");
    String zs = parts[3].replaceAll("|", "");
    World world = Bukkit.getWorld(UUID.fromString(w));
    int x = Integer.parseInt(xs);
    int y = Integer.parseInt(ys);
    int z = Integer.parseInt(zs);
    return new Location(world, x, y, z);
  }
  
  public String getStringFromLoc(Location l)
  {
    return l.getWorld().getUID().toString() + "|" + l.getBlockX() + "|" + l.getBlockY() + "|" + l.getBlockZ();
  }
  
  public String getWarpNameBySignLocation(Location l)
  {
    String s = getStringFromLoc(l);
    return Main.plugin.getSigns().getString(s);
  }
  
  public Location getWarp(String name)
  {
    return new Location(Bukkit.getWorld(UUID.fromString(Main.plugin.getWarps().getString(name + ".W"))), 
      Main.plugin.getWarps().getDouble(name + ".X"), Main.plugin.getWarps().getDouble(name + ".Y"), Main.plugin.getWarps().getDouble(name + ".Z"), 
      (float)Main.plugin.getWarps().getDouble(name + ".Yaw"), (float)Main.plugin.getWarps().getDouble(name + ".Pitch"));
  }
  
  public static List<String> getWarps()
  {
    List<String> warps = new ArrayList<String>();
    for (String each : Main.plugin.getWarps().getStringList("WarpList")) {
      warps.add(each);
    }
    return warps;
  }
  
  public static List<String> getWarps(Player p)
  {
    List<String> warps = new ArrayList<String>();
    for (String each : Main.plugin.getWarps().getStringList("WarpList")) {
      if ((p.hasPermission("warp.use.*")) || (p.hasPermission("warp.use." + each.toLowerCase())) || (p.isOp())) {
        warps.add(each);
      }
    }
    return warps;
  }
  
  public void setItemForWarp(String warp, ItemStack stack)
  {
    Main.plugin.getWarps().set(warp + ".GUI-Item", stack.getType() + ":" + stack.getDurability());
    Main.plugin.saveWarps();
  }
}
