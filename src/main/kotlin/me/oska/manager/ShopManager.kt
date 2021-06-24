package me.oska.manager

import de.leonhard.storage.Json
import me.oska.UniversalGUI
import me.oska.config.ApiConfig
import me.oska.config.shop.ShopConfig
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File

object ShopManager {

    private var shops: MutableList<ShopConfig> = mutableListOf();
    private var apiShop: MutableMap<String, ApiConfig> = mutableMapOf();

    fun initialize() {
        FileManager.loopFiles(UniversalGUI.getShopFolder()) {
            registerShop(it)
        }
    }

    fun shopListMessages(): List<String> {
        return listOf(
            *shops.map { "§f[§5LOCAL§f] §f" + it.id }.toTypedArray(),
            *apiShop.map { "§f[§5API§f] §f" + it.key + " - " + it.value.endpoint }.toTypedArray()
        )
    }

    private fun registerShop(file: File) {
        val shop = ShopConfig(Json(file.nameWithoutExtension, file.parent));
        shops.add(shop);
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
        val shop = shops.firstOrNull { shop -> shop.id == id }
        if (shop != null) {
            updateShop(shop);
            return true;
        }
        return false;
    }

    fun updateShop(config: ShopConfig) {
        val index = shops.indexOfFirst { shop -> shop.id == config.id };
        if (index == -1) {
            shops.add(config);
        } else {
            shops[index] = config;
        }
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
}