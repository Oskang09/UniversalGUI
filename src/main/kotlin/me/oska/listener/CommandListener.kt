package me.oska.listener

import me.oska.manager.ShopManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandListener: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, cmd: String, args: Array<String>): Boolean {
        if (sender is Player) {
            return ShopManager.displayShop(sender, args[0]);
        }
        return false;
    }
}