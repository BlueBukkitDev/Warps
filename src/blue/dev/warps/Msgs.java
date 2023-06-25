package blue.dev.warps;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Msgs
{
  public String PREFIX()
  {
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Prefix"));
  }
  
  public String WARP_ALREADY_EXISTS(String warp)
  {
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Warp-Already-Exists")
      .replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
  }
  
  public String WARP_DOES_NOT_EXIST(String warp)
  {
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Invalid-Warp-Name")
      .replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
  }
  
  public String WARPS_LIST(Player p)
  {
    if (!FileBuilder.getWarps(p).isEmpty())
    {
      String msg = "";
      for (String each : FileBuilder.getWarps(p)) {
        msg = msg + each + ", ";
      }
      msg.trim();
      if (msg.endsWith(",")) {
        msg.substring(0, msg.length());
      }
      return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Warps-List")
        .replaceAll("%warps%", msg).replaceAll("%prefix%", PREFIX()));
    }
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("No-Warps")
      .replaceAll("%prefix%", PREFIX()));
  }
  
  public String WARP_MESSAGE(String warp)
  {
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Warp-Message")
      .replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
  }
  
  public String WARP_CREATED(String warp)
  {
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Warp-Set")
      .replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
  }
  
  public String WARP_DELETED(String warp)
  {
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Warp-Deleted")
      .replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
  }
  
  public String SIGN_CREATED(String warp)
  {
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Warp-Sign-Created")
      .replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
  }
  
  public String SIGN_BROKEN(String warp)
  {
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Warp-Sign-Broken")
      .replaceAll("%warp%", warp).replaceAll("%prefix%", PREFIX()));
  }
  
  public String SIGN_CREATE_NO_PERMISSION()
  {
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Sign-Create-No-Permission")
      .replaceAll("%prefix%", PREFIX()));
  }
  
  public String SIGN_DESTROY_NO_PERMISSION()
  {
    return ChatColor.translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Sign-Destroy-No-Permission")
      .replaceAll("%prefix%", PREFIX()));
  }
}
