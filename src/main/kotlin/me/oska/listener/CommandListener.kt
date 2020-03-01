package me.oska.listener

import me.oska.manager.ShopManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandListener: CommandExecutor {

    companion object {
        const val KEY_RELOAD = "reload";
        const val KEY_API = "api";
        const val KEY_SHOP = "shop";
    }

    override fun onCommand(sender: CommandSender, command: Command, cmd: String, args: Array<String>): Boolean {

        if (sender is Player) {
            return ShopManager.displayShop(sender, args[0]);
        }

        if (args.size == 3 && args[0] == KEY_RELOAD) {
            if (args[1] == KEY_API) {
                if (!ShopManager.reloadApiShop(args[2])) {
                    sender.sendMessage("Shop not found");
                }
            }
            if (args[1] == KEY_SHOP) {
                if (!ShopManager.reloadShop(args[2])) {
                    sender.sendMessage("Shop not found");
                }
            }
        }
        return true;
    }
}