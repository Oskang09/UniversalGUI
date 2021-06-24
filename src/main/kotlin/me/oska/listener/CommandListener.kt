package me.oska.listener

import me.oska.manager.ShopManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandListener: CommandExecutor {

    private fun sendHelp(sender: CommandSender): Boolean {
        sender.sendMessage("§a----- §fUniversalGUI §a-----")
        sender.sendMessage("/ugui open [shop] - Open a specified shop.")
        if (sender.isOp || sender.hasPermission("ugui.admin")) {
            sender.sendMessage("/ugui list - List all existing shops.")
            sender.sendMessage("/ugui reload local [shop] - Reload specified shop config.")
            sender.sendMessage("/ugui reload api [shop] - Reload specified shop config.")
        }
        sender.sendMessage("§a----- ----- ----- -----")
        return true
    }

    override fun onCommand(sender: CommandSender, command: Command, cmd: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            return sendHelp(sender)
        }


        if (args.size == 1 && args[0] == "list") {
            if (sender is Player && (!sender.isOp || !sender.hasPermission("ugui.list"))) {
                return sendHelp(sender)
            }

            sender.sendMessage("§a----- §fUniversalGUI §a-----")
            sender.sendMessage(ShopManager.shopListMessages().toTypedArray())
            sender.sendMessage("§a----- ----- ----- -----")
            return true
        }

        if (args.size == 2 && args[0] == "open"&& sender is Player) {
            return ShopManager.displayShop(sender, args[1]);
        }

        if (args.size == 3 && args[0] == "reload") {
            if (sender is Player && (!sender.isOp || !sender.hasPermission("ugui.admin"))) {
                return sendHelp(sender)
            }
            return when (args[1]) {
                "api" -> {
                    if (!ShopManager.reloadApiShop(args[2])) {
                        sender.sendMessage("Api shop ${args[2]} not found");
                    }
                    return true
                }
                "local" -> {
                    if (!ShopManager.reloadShop(args[2])) {
                        sender.sendMessage("Local shop ${args[2]} not found");
                    }
                    return true
                }
                else -> sendHelp(sender)
            }
        }

        return sendHelp(sender)
    }
}