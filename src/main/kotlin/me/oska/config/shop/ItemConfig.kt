package me.oska.config.shop

import de.leonhard.storage.sections.FlatFileSection
import me.oska.module.ModuleType
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemConfig(config: FlatFileSection) {

    var item: ItemStack = ItemStack(Material.AIR)
        private set;
    var isPrev: Boolean
        private set;
    var isNext: Boolean
        private set;
    var requirements: MutableList<ModuleConfig> = mutableListOf()
        private set;
    var rewards: MutableList<ModuleConfig> = mutableListOf()
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
                requirement -> this.requirements.add(ModuleConfig(ModuleType.REQUIREMENT, requirement))
            }
        }

        @Suppress("UNCHECKED_CAST")
        val rewards: List<Map<*, *>> = config.getList(KEY_REWARD) as MutableList<Map<*, *>>
        if (rewards.isNotEmpty()) {
            rewards.parallelStream().forEach {
                reward -> this.rewards.add(ModuleConfig(ModuleType.REWARD, reward))
            }
        }
    }

    fun getModules(): MutableList<ModuleConfig> {
        val modules = mutableListOf<ModuleConfig>();
        modules.addAll(this.requirements);
        modules.addAll(this.rewards);
        return modules;
    }

    companion object {
        const val KEY_ITEM = "item";
        const val KEY_REQUIREMENT = "requirements";
        const val KEY_REWARD = "rewards";
        const val KEY_NEXT = "next";
        const val KEY_PREV = "prev";
    }
}