package modules

import me.oska.module.Module
import me.oska.module.ModuleInformation
import me.oska.module.ModuleNotConfigured
import me.oska.module.ModuleType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ItemstackModule: ModuleInformation() {

    override fun isSupported() {

    }

    override fun getAuthor(): String {
        return "Oska";
    }

    override fun getName(): String {
        return "ItemstackModule"
    }

    override fun getVersion(): String {
        return "0.0.1"
    }

    override fun getIdentifier(): String {
        return "item";
    }

    override fun getModule(type: ModuleType, config: Map<*, *>): Module {
        return ActionModule(type, config);
    }

    class ActionModule internal constructor(private val type: ModuleType, private val config: Map<*, *>): Module() {

        private var itemStack: ItemStack;

        init {
            val itemMap = config["item"] ?: throw ModuleNotConfigured("Missing 'item' from configuration.");

            @Suppress("UNCHECKED_CAST")
            itemStack = ItemStack.deserialize(itemMap as MutableMap<String, Any>);
        }

        override fun check(player: Player): Boolean {
            if (type == ModuleType.REWARD) {
                return true;
            }
            return player.inventory.containsAtLeast(itemStack, itemStack.amount);
        }

        override fun action(player: Player) {
            if (type == ModuleType.REWARD) {
                player.inventory.addItem(itemStack);
            } else {
                player.inventory.removeItem(itemStack);
            }
        }
    }
}