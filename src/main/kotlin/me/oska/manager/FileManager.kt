package me.oska.manager

import java.io.File

object FileManager {
    fun loopFiles(folder: File, func: (File) -> Unit) {
        val files = folder.listFiles()
        for (file in files) {
            if (file.isDirectory) {
                loopFiles(file, func)
            } else if (file.isFile) {
                func(file);
            }
        }
    }

}