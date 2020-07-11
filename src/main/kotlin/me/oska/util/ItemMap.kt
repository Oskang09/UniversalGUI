package me.oska.util

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemMap(config: Map<*, *>) {

    var item: ItemStack = ItemStack(Material.AIR)
        private set

    companion object {
        const val KEY_TYPE = "type";
        const val KEY_MATERIAL = "material";
        const val KEY_DISPLAY = "display";
        const val KEY_LORE = "lore";
        const val KEY_AMOUNT = "amount";
        const val KEY_ID = "id"; // custom plugin
    }

    init {
        when (config[KEY_TYPE]) {
            null, "minecraft" -> {
                val materialConfig = config[KEY_MATERIAL]
                        ?: throw Exception("Material is not allowed to be empty.");
                val material: Material? = Material.getMaterial(materialConfig as String)
                        ?: throw Exception("Material `$materialConfig` is not a valid material.")
                val amount = config[KEY_AMOUNT] ?: 1
                val item = ItemStack(material!!, amount as Int)
                val meta = Bukkit.getItemFactory().getItemMeta(material)

                var display = config[KEY_DISPLAY]
                if (display != null) {
                    meta?.setDisplayName((display as String).replace("&", "§"))
                }

                var lores = config[KEY_LORE]
                if (lores != null) {
                    lores = lores as List<*>
                    if (lores.isNotEmpty()) {
                        meta?.lore = lores.map {lore -> lore.toString().replace("&", "§")}.toList();
                    }
                }

                item.itemMeta = meta;
                this.item = item;
            }
        }
    }
}