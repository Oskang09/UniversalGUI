package me.oska.config

import com.okkero.skedule.CoroutineTask
import com.okkero.skedule.schedule
import de.leonhard.storage.Json
import de.leonhard.storage.Toml
import de.leonhard.storage.Yaml
import de.leonhard.storage.sections.FlatFileSection
import me.oska.UniversalGUI
import me.oska.manager.ShopManager
import util.HttpUtil
import java.io.InputStream

class ApiConfig(config: FlatFileSection) {

    private var method: String = config.getString(KEY_METHOD);
    private var endpoint: String = config.getString(KEY_ENDPOINT);
    private var format: String = config.getString(KEY_FORMAT);
    private var headers: Map<String, String>? = null;
    private var task: CoroutineTask? = null;
    private var update: Int = config.getInt(KEY_UPDATE);
    var name: String = config.getString(KEY_NAME)

    companion object {
        const val KEY_METHOD = "method";
        const val KEY_ENDPOINT = "endpoint";
        const val KEY_FORMAT = "format";
        const val KEY_NAME = "name";
        const val KEY_HEADER = "headers";
        const val KEY_UPDATE = "update";
    }


    init {
        val headers = config.getMap(KEY_HEADER)
        if (headers != null) {
            this.headers = headers as Map<String, String>
        }

        if (update > 0) {
            task = UniversalGUI.getScheduler().schedule(UniversalGUI.getInstance()) {
                repeating(update.toLong());
                update();
            }
        }
    }

    fun update() {
        val http = HttpUtil.connect(endpoint).method(method);
        if (headers != null) {
            http.setHeader(headers!!);
        }
        val stream: InputStream = http.get();

        val shop = when (format) {
            "json" -> ShopConfig(Json(name, UniversalGUI.getApiPath().path, stream))
            "yaml" -> ShopConfig(Yaml(name, UniversalGUI.getApiPath().path, stream))
            "toml" -> ShopConfig(Toml(name, UniversalGUI.getApiPath().path, stream))
            else -> null
        }
        if (shop != null) {
            ShopManager.updateShop(shop);
        }
    }
}