package me.oska.manager

import de.leonhard.storage.Json
import me.oska.UniversalGUI
import me.oska.config.ApiConfig
import me.oska.config.ShopConfig
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File

object ShopManager {

    private var shops: MutableList<ShopConfig> = mutableListOf();
    private var apiShop: MutableMap<String, ApiConfig> = mutableMapOf();

    fun initialize() {
        FileManager.loopFiles(UniversalGUI.getShopFolder(), ShopManager::registerShop);
    }

    fun displayShop(player: Player, npc: Int): Boolean {
        val shop = shops.firstOrNull { shop -> shop.activator.canActive(npc) };
        if (shop != null) {
            shop.show(1, player);
            return true;
        }
        return false;
    }

    fun displayShop(player: Player, cmd: String): Boolean {
        val shop = shops.firstOrNull { shop -> shop.activator.canActive(cmd) };
        if (shop != null) {
            shop.show(1, player);
            return true;
        }
        return false;
    }

    fun displayShop(player: Player, item: ItemStack): Boolean {
        val shop = shops.firstOrNull { shop -> shop.activator.canActive(item) };
        if (shop != null) {
            shop.show(1, player);
            return true;
        }
        return false;
    }

    fun updateShop(config: ShopConfig) {
        val index = shops.indexOfFirst { shop -> shop.api.name == config.api.name };
        if (index == -1) {
            shops.add(config);
        } else {
            shops[index] = config;
        }
    }

    private fun registerShop(file: File) {
        val shop = ShopConfig(Json(file.nameWithoutExtension, file.parent));
        shops.add(shop);
    }
}