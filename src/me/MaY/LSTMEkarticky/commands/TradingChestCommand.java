package me.MaY.LSTMEkarticky.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import me.MaY.LSTMEkarticky.LSTMEKartickyPluginMain;

public class TradingChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "You must be player in order to execute this command");
            return true;
        }
        if(!sender.isOp()) {
        	sender.sendMessage(ChatColor.RED + "Not enought privileges");
            return true;
        }
        if(args.length != 0)
        	return false;
        Player pl = (Player) sender;
        Block b = pl.getWorld().getBlockAt(pl.getLocation());
        
        if(b.getType() != Material.CHEST) {
        	sender.sendMessage("You need to stand on chest");
        	return true;
        }
        Chest chest = (Chest)b.getState();
        chest.getPersistentDataContainer().set(LSTMEKartickyPluginMain.tradingChest, PersistentDataType.SHORT, (short)1);
        chest.update();
        return true;
    }


}
