package me.oska.manager

import me.oska.UniversalGUI
import me.oska.module.Module
import me.oska.module.ModuleInformation
import me.oska.module.ModuleNotSupported
import me.oska.module.ModuleType
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

object ModuleManager {
    private var modules: MutableMap<String, ModuleInformation> = mutableMapOf();

    fun initialize() {
        FileManager.loopFiles(UniversalGUI.getModuleFolder(), ModuleManager::registerModule);
    }

    fun isSupportParellel(name: String): Boolean {
        return modules[name]!!.supportParallel();
    }

    fun getModule(name: String, type: ModuleType, setting: Map<*, *>): Module? {
        return modules[name]?.getModule(type, setting);
    }

    private fun registerModule(file: File) {
        if (file.extension != "jar") {
            return;
        }

        try {
            val jarfile = JarFile(file)
            val entry = jarfile.entries()
            val urls = arrayOf(URL("jar:file:" + file.path + "!/"))
            val loader = URLClassLoader.newInstance(urls, UniversalGUI.getInstance().javaClass.classLoader)
            while (entry.hasMoreElements()) {
                val je = entry.nextElement()
                if (je.isDirectory || !je.name.endsWith(".class")) {
                    continue
                }
                val classname = je.name.substring(0, je.name.length - 6).replace('/', '.')
                val clazz = Class.forName(classname, true, loader)
                if (ModuleInformation::class.java.isAssignableFrom(clazz)) {
                    val pl = clazz.asSubclass(ModuleInformation::class.java)
                    val cst = pl.getConstructor();
                    val module = cst.newInstance()
                    try {
                        module.isSupported()
                        modules[module.getIdentifier()] = module
                        UniversalGUI.log("Registered new module ${module.getName()} (${module.getVersion()}) by ${module.getAuthor()}")
                    } catch (ex: ModuleNotSupported) {
                        UniversalGUI.log("Fail to register module ${module.getName()} due to " + ex.message);
                    }
                }
            }
            loader.close()
            jarfile.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}