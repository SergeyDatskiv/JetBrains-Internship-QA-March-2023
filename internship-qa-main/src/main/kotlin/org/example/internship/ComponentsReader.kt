package org.example.internship

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.example.internship.data.*
import java.nio.file.Files
import java.nio.file.Path

object ComponentsReader {
    private val module = SerializersModule {
        polymorphic(Component::class) {
            subclass(org.example.internship.data.List::class)
            subclass(Paragraph::class)
            subclass(Chapter::class)
            subclass(Image::class)
            subclass(Format::class)
            subclass(Link::class)
            subclass(Text::class)
        }
    }

    val json = Json {
        serializersModule = module
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private fun getComponents(path: String): List<Component> {
        return Files.list(Path.of(path)).toList()
            .filter { it.toString().endsWith(".json") }
            .map { json.decodeFromString<List<Component>>(Files.readString(it)) }
            .flatten()
    }

    fun getSafeComponents(): List<Component> {
        return getComponents("jsons-safe")
    }

    fun getUnsafeComponents(): List<Component> {
        return getComponents("jsons-unsafe")
    }
}