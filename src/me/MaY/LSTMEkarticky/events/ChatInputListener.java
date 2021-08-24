package me.MaY.LSTMEkarticky.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.MaY.LSTMEkarticky.LSTMEKartickyPluginMain;
import me.MaY.LSTMEkarticky.utils.Utils;

public class ChatInputListener implements Listener{
	public static HashMap<UUID, Location> listening = new HashMap<>();
	@EventHandler
	public void onInput(AsyncPlayerChatEvent e) {
		if(!listening.containsKey(e.getPlayer().getUniqueId()))
			return;
		e.setCancelled(true);
		String msg = e.getMessage().replace(" ", "");
		if(msg.length() != 6) {
			e.getPlayer().sendMessage(ChatColor.RED + "Invalid id");
			listening.remove(e.getPlayer().getUniqueId());
			return;
		}
		try {
			int parsed = Integer.parseInt(msg);
			new BukkitRunnable() {
				@Override
				public void run() {
					Utils.performChangeChest(e.getPlayer(), null, listening.get(e.getPlayer().getUniqueId()), parsed);
					listening.remove(e.getPlayer().getUniqueId());
				}
			}.runTask(LSTMEKartickyPluginMain.getINSTANCE());
			
		} catch (NumberFormatException ex) {
			e.getPlayer().sendMessage(ChatColor.RED + "Invalid id");
			listening.remove(e.getPlayer().getUniqueId());
		}
	}
}
