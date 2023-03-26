package org.example.internship.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KProperty

@Serializable
@SerialName("component")
abstract class Component {
    val properties: MutableMap<String, String?> = mutableMapOf<String, String?>().withDefault { null }
    val children: MutableList<Component> = mutableListOf()

    var id: String? by Properties()

    operator fun Component.unaryPlus() {
        this@Component.children += this
    }

    override fun toString(): String {
        val identifier = id?.let { "[$it]" } ?: " with $properties, ${children.map { it.type }}"
        return "$type$identifier"
    }

    @Transient
    private val type: String = this.javaClass.simpleName

    class Properties {
        operator fun getValue(thisRef: Component, property: KProperty<*>): String? {
            return thisRef.properties[property.name]
        }

        operator fun setValue(thisRef: Component, property: KProperty<*>, value: String?) {
            thisRef.properties[property.name] = value
        }
    }
}
