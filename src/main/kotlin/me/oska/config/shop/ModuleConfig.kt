package me.oska.config.shop

import me.oska.manager.ModuleManager
import me.oska.module.Module
import me.oska.module.ModuleNotExists
import me.oska.module.ModuleType
import org.bukkit.entity.Player

class ModuleConfig(type: ModuleType, setting: Map<*, *>) {

    var parallel: Boolean = false
        private set;
    var module: Module? = null
        private set;
    var name: String
        private set;
    var error: Throwable? = null
        private set;


    init {
        this.name = setting[KEY_MODULE] as String
        try {
            this.module = ModuleManager.getModule(this.name, type, setting) ?:
                    throw ModuleNotExists("Module \"${this.name}\" doesn't exists.");
            this.parallel = ModuleManager.isSupportParallel(this.name);
        } catch (error: Throwable) {
            error.printStackTrace();
            this.module = null;
            this.error = error;
        }
    }

    companion object {
        const val KEY_MODULE = "module";
    }

    fun check(player: Player): Boolean {
        if (this.module != null) {
            val requirementPass = this.module!!.check(player)
            if (!requirementPass) {
                this.module!!.onFail(player)
            }
            return requirementPass;
        }

        if (this.error != null) {
            this.error!!.printStackTrace();
        }
        return false;
    }

    fun action(player: Player) {
        if (this.error != null) {
            this.error!!.printStackTrace();
        }

        if (this.module != null) {
            this.module!!.action(player);
            this.module!!.onSuccess(player)
        }
    }
}
