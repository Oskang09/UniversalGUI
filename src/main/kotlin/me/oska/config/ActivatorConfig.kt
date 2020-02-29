package me.oska.config

import de.leonhard.storage.sections.FlatFileSection
import org.bukkit.inventory.ItemStack

class ActivatorConfig(config: FlatFileSection) {

    companion object {
        const val KEY_CITIZEN_ID = "citizens.npc";
        const val KEY_CITIZEN_PERMISSION = "citizens.permission";
        const val KEY_INTERACT_ITEM = "interact.item";
        const val KEY_INTERACT_PERMISSION = "interact.permission";
        const val KEY_COMMAND = "command.cmd";
        const val KEY_COMMAND_PERMISSION = "command.permission";
    }

    private var npc: Int = -1;
    private var npcPermission: String;

    private var item: ItemStack? = null;
    private var itemPermission: String;

    private var command: String;
    private var commandPermission: String;

    init {
        npc = config.getInt(KEY_CITIZEN_ID);
        npcPermission = config.getString(KEY_CITIZEN_PERMISSION);

        val itemMap = config.getMap(KEY_INTERACT_ITEM)
        if (itemMap != null && itemMap is MutableMap<*, *>) {
            @Suppress("UNCHECKED_CAST")
            item = ItemStack.deserialize(itemMap as MutableMap<String, Any>);
        }

        itemPermission = config.getString(KEY_INTERACT_PERMISSION);
        command = config.getString(KEY_COMMAND);
        commandPermission = config.getString(KEY_COMMAND_PERMISSION);
    }

    fun canActive(cmd: String): Boolean {
        return this.command == cmd;
    }

    fun canActive(npc: Int): Boolean {
        return this.npc == npc;
    }

    fun canActive(item: ItemStack): Boolean {
        if (this.item != null) {
            return this.item!!.isSimilar(item);
        }
        return false;
    }
}