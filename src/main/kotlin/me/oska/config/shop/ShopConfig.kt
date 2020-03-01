package me.oska.config.shop

import de.leonhard.storage.internal.FlatFile
import de.leonhard.storage.sections.FlatFileSection
import me.oska.config.ApiConfig
import me.oska.manager.InventoryManager
import me.oska.manager.PluginManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.util.stream.Stream
import kotlin.concurrent.thread
import kotlin.math.ceil

class ShopConfig(file: FlatFile) {

    var id: String;
    private var title: String
    private var row: Int = 0
    private var pageCount: Int = 1
    private var slotPerPage: Int = 0
    private var items: MutableMap<Int, ItemConfig> = mutableMapOf()
    var message: MessageConfig
    var activator: ActivatorConfig

    init {
        this.id = file.getString(KEY_ID);
        this.title = file.getString(KEY_TITLE)
        this.row = file.getInt(KEY_ROW)
        this.slotPerPage = this.row * 9
        this.message = MessageConfig(file.getSection(KEY_MESSAGE))
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
        const val KEY_MESSAGE = "message"
    }

    fun action(page: Int, slot: Int, player: Player) {
        this.items[page * slot]?.apply {
            if (this@ShopConfig.pageCount > 1 && this.isNext) {
                show(page + 1, player)
            } else if (page > 1 && this.isPrev) {
                show(page - 1, player)
            } else if (getStream(this.modules).allMatch { module -> module.check(player) }) {
                getStream(this.modules).forEach { module -> module.action(player) }
            }
        }
    }

    fun show(page: Int, player: Player) {
        val slots: Map<Int, ItemConfig> = this.items.filterKeys { index -> index > page - 1 * 64 && index < page * 64 }
        val inv: Inventory = Bukkit.createInventory(player, this.slotPerPage, this.title)
        val threads: MutableList<Thread> = mutableListOf()
        slots.forEach {
            (slot, config) -> run {
                inv.setItem(slot, config.item)
                threads.add(
                    thread(start = true) {
                        val item = inv.getItem(slot) ?: return@thread
                        var meta = item.itemMeta
                        if (meta != null) {
                            var lore = meta.lore ?: mutableListOf()
                            getStream(config.modules).forEach { module -> lore.add(module.display) }
                            if (!config.message.requireCheck()) {
                                lore.add(config.message.getMessage(player));
                            } else if (!message.requireCheck()) {
                                lore.add(message.getMessage(player));
                            }
                            meta.lore = lore
                            item.itemMeta = meta
                        }
                        inv.setItem(slot, item)
                    }
                );

                when {
                    config.message.requireCheck() -> {
                        startCheckThread(config.message, config, inv, player, slot);
                    }
                    message.requireCheck() -> {
                        startCheckThread(message, config, inv, player, slot);
                    }
                    else -> null
                }?.also { thread -> threads.add(thread) }
            }
        }
        player.openInventory(inv)
        val uuid: String = player.uniqueId.toString()
        InventoryManager.addPlayer(uuid, page, inv, this, threads)
    }

    private fun <T, K> getStream(data: T): Stream<K> where T: List<K> {
        return if (PluginManager.pluginConfig.useParallel) data.parallelStream() else data.stream();
    }

    private fun startCheckThread(message: MessageConfig, config: ItemConfig, inv: Inventory, player: Player, slot: Int): Thread {
        return thread(start = true) {
            val item = inv.getItem(slot) ?: return@thread;
            var meta = item.itemMeta
            var lore = meta?.lore
            if (lore == null) {
                lore = mutableListOf()
            }
            getStream(config.modules).forEach { module ->
                run {
                    when (module.check(player)) {
                        true -> lore.add(message.getFulFilled(player))
                        false -> lore.add(message.getRejected(player))
                    }
                }
            }
            meta?.lore = lore
            item.itemMeta = meta
            inv.setItem(slot, item)
        }
    }
}
