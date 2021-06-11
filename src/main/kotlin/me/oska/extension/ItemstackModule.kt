package me.oska.extension

import me.oska.module.Module
import me.oska.module.ModuleInformation
import me.oska.module.ModuleNotConfigured
import me.oska.module.ModuleType
import me.oska.util.ItemMap
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemstackModule : ModuleInformation() {

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

    override fun supportParallel(): Boolean {
        return false;
    }

    override fun getModule(type: ModuleType, config: Map<*, *>): Module {
        val configItem = config["item"] ?: throw ModuleNotConfigured("missing 'item' from configuration.");

        @Suppress("UNCHECKED_CAST")
        val serializedMap = configItem as? MutableMap<String, Any> ?: throw ModuleNotConfigured("item is not a serialized item map, received $configItem")
        val itemstack = ItemMap(serializedMap).item;
        return ActionModule(type, itemstack);
    }

    internal class ActionModule constructor(
        private val type: ModuleType,
        private val itemStack: ItemStack
    ): Module() {

        override fun check(player: Player): Boolean {
            if (type == ModuleType.REQUIREMENT) {
                return player.inventory.containsAtLeast(itemStack.clone(), itemStack.amount);
            }
            return player.inventory.firstEmpty() != -1;
        }

        override fun action(player: Player) {
            if (type == ModuleType.REWARD) {
                player.inventory.addItem(itemStack.clone());
            } else {
                player.inventory.removeItem(itemStack.clone());
            }
        }
    }
}
