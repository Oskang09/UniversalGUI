package me.oska.config

import de.leonhard.storage.internal.FlatFile
import me.oska.manager.ShopManager

class PluginConfig(config: FlatFile) {

    var useParallel: Boolean = config.getBoolean(KEY_PARALLEL);

    init {
        config.getSection(KEY_API).singleLayerKeySet().forEach {
            key -> ShopManager.registerApiShop(key, ApiConfig(config.getSection("$KEY_API.$key")))
        }
    }

    companion object {
        const val KEY_API = "api";
        const val KEY_PARALLEL = "use_parallel";
    }

}