package me.oska.manager

import de.leonhard.storage.Json
import de.leonhard.storage.internal.FlatFile
import me.oska.UniversalGUI
import me.oska.config.ApiConfig
import me.oska.config.shop.ShopConfig
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File

object ShopManager {

    private var shopFiles: MutableMap<String, FlatFile> = mutableMapOf()
    private var shops: MutableMap<String, ShopConfig> = mutableMapOf()
    private var apiShop: MutableMap<String, ApiConfig> = mutableMapOf()

    fun initialize() {
        FileManager.loopFiles(UniversalGUI.getShopFolder()) {
            registerShop(Json(it.nameWithoutExtension, it.parent))
        }
    }

    fun shopListMessages(): List<String> {
        return shops.map { "§f[§5LOCAL§f] §f" + it.key }.toList()
    }

    private fun registerShop(file: FlatFile) {
        val shop = ShopConfig(file);
        shops[shop.id] = shop
        shopFiles[shop.id] = file
    }

    fun registerApiShop(file: FlatFile) {
        val shop = ShopConfig(file);
        shops[shop.id] = shop
    }

    fun registerApiShop(key: String, config: ApiConfig) {
        apiShop[key] = config;
        config.update();
    }

    fun reloadApiShop(name: String): Boolean {
        val shop = apiShop[name];
        if (shop != null) {
            shop.update();
            return true;
        }
        return false;
    }

    fun reloadShop(id: String): Boolean {
        val file = shopFiles[id] ?: return false
        file.forceReload()
        registerShop(file)
        return true
    }

    fun displayShop(player: Player, npc: Int): Boolean {
        val shop = shops.values.singleOrNull { shop -> shop.activator.canActive(npc) };
        if (shop != null) {
            shop.show(1, player);
            return true;
        }
        return false;
    }

    fun displayShop(player: Player, cmd: String): Boolean {
        val shop = shops.values.singleOrNull  { shop -> shop.activator.canActive(cmd) };
        if (shop != null) {
            shop.show(1, player);
            return true;
        }
        return false;
    }

    fun displayShop(player: Player, item: ItemStack): Boolean {
        val shop = shops.values.singleOrNull  { shop -> shop.activator.canActive(item) };
        if (shop != null) {
            shop.show(1, player);
            return true;
        }
        return false;
    }
}