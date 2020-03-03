package me.oska.util

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL

class HttpUtil(api: String?) {
    private val connection: HttpURLConnection;

    companion object {
        @Throws(IOException::class)
        fun connect(url: String?): HttpUtil {
            return HttpUtil(url)
        }
    }

    init {
        val url = URL(api)
        connection = url.openConnection() as HttpURLConnection
        connection.useCaches = false
        connection.doInput = true
        connection.doOutput = true
    }

    @Throws(ProtocolException::class)
    fun method(method: String?): HttpUtil {
        connection.requestMethod = method
        return this
    }

    fun setHeader(headers: Map<String, String>): HttpUtil {
        headers.forEach { (s: String, s1: String) -> connection.setRequestProperty(s, s1) }
        return this
    }

    fun header(key: String, value: String): HttpUtil {
        connection.setRequestProperty(key, value)
        return this
    }

    @Throws(IOException::class)
    fun get(): InputStream {
        return connection.inputStream
    }
}
