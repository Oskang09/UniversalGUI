package me.oska.config.shop

import de.leonhard.storage.internal.FlatFile
import de.leonhard.storage.sections.FlatFileSection
import me.clip.placeholderapi.PlaceholderAPI
import me.oska.UniversalGUI
import me.oska.manager.InventoryManager
import me.oska.manager.PluginManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.util.stream.Stream
import kotlin.math.ceil

class ShopConfig(file: FlatFile) {

    var id: String = file.getString(KEY_ID);
    private var title: String = file.getString(KEY_TITLE)
    private var row: Int = 0
    private var pageCount: Int = 1
    private var slotPerPage: Int = 0
    private var items: MutableMap<Int, ItemConfig> = mutableMapOf()
    var activator: ActivatorConfig

    init {
        this.row = file.getInt(KEY_ROW)
        this.slotPerPage = this.row * 9
        this.activator = ActivatorConfig(file.getSection(KEY_ACTIVATOR))

        var largestValue = 0
        var itemsMap: FlatFileSection = file.getSection(KEY_ITEMS)
        for (key in itemsMap.singleLayerKeySet()) {
            val slot = key.toInt()
            if (slot > largestValue) {
                largestValue = slot
            }
            var item = file.getSection("$KEY_ITEMS.$key")
            this.items[slot] = ItemConfig(item)
        }

        if (largestValue > this.slotPerPage) {
            this.pageCount = ceil(largestValue.toDouble() / this.slotPerPage.toDouble()).toInt()
        }
    }

    companion object {
        const val KEY_ID = "id";
        const val KEY_TITLE = "title"
        const val KEY_ROW = "row"
        const val KEY_ITEMS ="items"
        const val KEY_ACTIVATOR = "activator"
    }

    fun action(state: InventoryManager.PlayerState, slot: Int, player: Player) {
        this.items[state.page * slot]?.apply {
            if (this@ShopConfig.pageCount > 1 && this.isNext) {
                show(state.page + 1, player)
            } else if (state.page  > 1 && this.isPrev) {
                show(state.page  - 1, player)
            } else if (getStream(this.getModules()).allMatch { module -> module.check(player) }) {
                getStream(this.getModules(), this.getModules().any { x -> !x.parallel }).forEach { module -> module.action(player) };
            }
        }
    }

    fun show(page: Int, player: Player) {
        val slots: Map<Int, ItemConfig> = this.items.filterKeys { index -> index > page - 1 * 64 && index < page * 64 }
        val inv: Inventory = Bukkit.createInventory(player, this.slotPerPage, this.title)

        slots.forEach {
            (slot, config) -> run {
                if (PluginManager.isPlaceholderSupported) {
                    config.item.item.itemMeta?.lore =  PlaceholderAPI.setPlaceholders(player, config.item.item.itemMeta?.lore)
                }
                inv.setItem(slot, config.item.item)
            }
        }

        player.openInventory(inv)
        val uuid: String = player.uniqueId.toString()
        InventoryManager.addPlayer(uuid, page, inv, this)
    }

    private fun <T, K> getStream(data: T, notSupported: Boolean = false): Stream<K> where T: List<K> {
        if (notSupported) {
            return data.stream()
        }
        return if (PluginManager.pluginConfig.useParallel) data.parallelStream() else data.stream();
    }
}
