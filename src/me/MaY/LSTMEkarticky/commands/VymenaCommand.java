package me.MaY.LSTMEkarticky.commands;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.MaY.LSTMEkarticky.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.chat.ChatMessage;

public class VymenaCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be player in order to execute this command");
			return true;
		}
		Player pl = (Player) sender;
		if(args.length == 0) {
			pl.sendMessage(ChatColor.AQUA + "| " +
			    ChatColor.BOLD + ""  + ChatColor.WHITE + "Itemy na vymenu:");

			for (ItemStack stack : Utils.itemList)
			{
			    pl.sendMessage(ChatColor.BOLD + (stack.getAmount() + "") + "x " + stack.getType().name());
			}
			
			return true;
		}
		return false;
	}

}
