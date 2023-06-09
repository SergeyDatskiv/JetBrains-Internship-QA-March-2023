package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.example.internship.data.ValidityReport
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

    /**
     * [invalidate] is a function that sets the [isValid] variable to false.
     * It also appends a specified [errorMessage] to the [inputMessage].
     * Returns an informative message.
     */
    open fun invalidate(inputMessage: String, errorMessage: String): String {
        isValid = false
        return inputMessage + errorMessage
    }

    /**
     * [generateChildValidityReports] generates a [ValidityReport] for each child.
     * Returns an informative message.
     */
    open fun generateChildValidityReports(
        child: Component,
        reports: MutableList<ValidityReport>,
        message: String
    ): String {
        var outputMessage = message
        val childValidityReport: MutableList<ValidityReport> = child.validate()
        reports.addAll(childValidityReport)
        for (childReport in childValidityReport) {
            if (!childReport.isValid) {
                outputMessage =
                    invalidate(outputMessage, "This child component (${childReport.component}) is invalid.\n")
            }
        }
        return outputMessage
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
