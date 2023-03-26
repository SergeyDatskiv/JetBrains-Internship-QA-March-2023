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
    var isValid: Boolean = true

    var id: String? by Properties()

    /**
     * Generates a validity report for a component.
     * Overridden in each subclass of a component.
     */
    open fun validate(): MutableList<ValidityReport> {
        isValid = true
        return mutableListOf<ValidityReport>(ValidityReport(this, "", true))
    }

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
