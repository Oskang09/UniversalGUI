package me.oska.config.shop

import de.leonhard.storage.sections.FlatFileSection
import me.oska.module.ModuleType
import org.bukkit.inventory.ItemStack

class ItemConfig(config: FlatFileSection) {

    var item: ItemStack? = null
        private set;
    var isPrev: Boolean
        private set;
    var isNext: Boolean
        private set;
    var modules: MutableList<ModuleConfig> = mutableListOf()
        private set;
    var message: MessageConfig
        private set;

    init {
        this.isPrev = config.getBoolean(KEY_NEXT);
        this.isNext = config.getBoolean(KEY_PREV);
        this.message = MessageConfig(config);
        val itemMap = config.getMap(KEY_ITEM);
        if (itemMap != null && itemMap is MutableMap<*, *>) {
            @Suppress("UNCHECKED_CAST")
            this.item = ItemStack.deserialize(itemMap as MutableMap<String, Any>);
        }


        @Suppress("UNCHECKED_CAST")
        val requirements: List<Map<*, *>> = config.getList(KEY_REQUIREMENT) as MutableList<Map<*, *>>;
        if (requirements.isNotEmpty()) {
            requirements.parallelStream().forEach {
                requirement -> this.modules.add(ModuleConfig(ModuleType.REQUIREMENT, flatten(requirement)))
            }
        }

        @Suppress("UNCHECKED_CAST")
        val rewards: List<Map<*, *>> = config.getList(KEY_REWARD) as MutableList<Map<*, *>>
        if (rewards.isNotEmpty()) {
            rewards.parallelStream().forEach {
                reward -> this.modules.add(ModuleConfig(ModuleType.REWARD, flatten(reward)))
            }
        }
    }

    companion object {
        const val KEY_MESSAGE = "message";
        const val KEY_ITEM = "item";
        const val KEY_REQUIREMENT = "requirements";
        const val KEY_REWARD = "rewards";
        const val KEY_NEXT = "next";
        const val KEY_PREV = "prev";

        fun flatten(original: Map<*, *>): Map<String, Any> {
            val maps = mutableMapOf<String, Any>();
            val keys = mutableListOf<String>();
            flatten(original, maps, keys);
            return maps;
        }

        private fun flatten(from: Map<*, *>, into: MutableMap<String, Any>, currentKey: MutableList<String>) {
            for (key in from.keys) {
                val keyStr = key.toString()
                val value = from[key]
                if (value is Map<*, *>) {
                    currentKey.add(keyStr)
                    flatten(value, into, currentKey)
                } else if (value != null) {
                    val prefix = currentKey.joinToString(".");
                    val space = if (currentKey.size == 0) "" else "."
                    into[prefix + space + keyStr] = value;
                }
            }
        }
    }
}