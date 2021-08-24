package me.MaY.LSTMEkarticky.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import me.MaY.LSTMEkarticky.LSTMEKartickyPluginMain;
import me.MaY.LSTMEkarticky.utils.Utils;


public class TradeItemsWithChestEvent implements Listener{
	@EventHandler
	public void onShiftRightClicked(PlayerInteractEvent e) {
		if(e.getClickedBlock() == null)
			return;
		if(e.getClickedBlock().getType() != Material.CHEST)
			return;
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if(!e.getPlayer().isSneaking())
			return;
		Chest chest = (Chest)e.getClickedBlock().getState();
		if(!chest.getPersistentDataContainer().has(LSTMEKartickyPluginMain.tradingChest, PersistentDataType.SHORT))
			return;
		e.setCancelled(true);
		if(!Utils.hasAtLeastOneTrade(chest.getInventory())) {
			e.getPlayer().sendMessage(ChatColor.RED + "Nemas dostatok itemov");
			return;
		}
        e.getPlayer().sendMessage(ChatColor.BOLD + "Napíš sem tvoje id: ");
        ChatInputListener.listening.put(e.getPlayer().getUniqueId(), chest.getLocation());
	}
}
