package util

import java.nio.charset.StandardCharsets
import java.util.*

object StringResources {
    private val properties: Properties = Properties()

    init {
        val inputStream = this::class.java.getResourceAsStream("/strings.properties")
        inputStream?.reader(StandardCharsets.UTF_8)?.use {
            properties.load(it)
        }
    }

    fun get(key: String): String {
        return properties.getProperty(key) ?: "Строка не найдена"
    }
}